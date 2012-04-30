/*
 * Copyright (C) 2011-2012 Boardgame Bots (http://code.google.com/p/boardgame-bots)
 * 
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *   
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gamebotarena.risk.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.risk.api.AttackMove;
import com.gamebotarena.risk.api.Board;
import com.gamebotarena.risk.api.Card;
import com.gamebotarena.risk.api.Move;
import com.gamebotarena.risk.api.MoveContext;
import com.gamebotarena.risk.api.ReinforcementsMove;
import com.gamebotarena.risk.api.ReinforcementsStruct;
import com.gamebotarena.risk.api.RiskGameBot;
import com.gamebotarena.risk.api.SetupMove;
import com.gamebotarena.risk.api.TradeCardsMove;

public class Game extends GameBase
{
	public static final String ID = "Risk";
	
	public int getTurn()
	{
		return turn;
	}
	
	Board board;
    boolean show = false;
	boolean gameOver = false;
	int turn;
	public Cards cards;
	boolean cardDrawn;

	String gameOverMessage = "";
	
	public static final int STATE_LAND_GRAB_SETUP = 0;
	public static final int STATE_PLAY = 2;
	public static final int STATE_GAME_OVER = 3;
	
	public int state;

	public static int NUM_PLAYERS = 3;
	public int numPlayers = NUM_PLAYERS;
	
	public int[] armiesAtBeginning;

	Random rand = Misc.rand;

	public boolean started = false;

	GameUi ui;
	ArrayList<BotInfo> botInfos;
	RiskGameBot[] bots;
	
	public static void main(String[] args)
	{
		new Game().start(args);
	}

	public void start(final String[] args)
	{
		try
		{
			armiesAtBeginning = new int[]{0, 0, 40, 35, 30, 25};
			
			botInfos = Misc.getBots(this, args, numPlayers, "com.gamebotarena.risk.api.RandomRiskGameBot");
			
			bots = new RiskGameBot[numPlayers + 1];
	
			for (int i = 1; i <= numPlayers; i++)
			{
				bots[i] = (RiskGameBot) botInfos.get(i - 1).bot;
			}
			
			ui = new GameUi(this);
			
			setGameUiBase(ui);
			runGames(args);
		}
		catch(Exception e)
		{
			D.out(e);
		}
	}

	public void play()
	{
	    show = false;
		gameOver = false;
		cardDrawn = false;

		started = false;

		turn = 0;
		state = STATE_LAND_GRAB_SETUP;
		board = new Board();
		cards = new Cards(numPlayers);
		
		for(int i=1; i <= numPlayers; i++)
		{
			bots[i].setup(i);
		}
		
		int[] initialArmies = new int[numPlayers + 1];
		
		for (int i = 1; i <= numPlayers; i++)
		{
			initialArmies[i] = armiesAtBeginning[numPlayers];
		}
		
		/* 
		boolean b = true;
		while(b)
		{
			ui.showDottedArrow(0, null);
		}
		
		*/
		
		int player = 1;
		for (int i = 0; i < Board.NUM_COUNTRIES; i++)
		{
			MoveContext context = new MoveContext(this, board, 1, player);
			SetupMove move = bots[player].getSetupMove(context);
			executeSetupMove(context, move);
			initialArmies[player]--;
			player++;
			if (player > numPlayers)
			{
				player = 1;
			}
			
			if(ui.getStopEarly())
			{
				break;
			}
		}
		
		for (int i = 1; i < initialArmies.length; i++)
		{
			int initialArmy = initialArmies[i];
			//D.out("initialArmy for " + i + ":" + initialArmy);
		}
		
		while (!ui.getStopEarly() && initialArmies[numPlayers] > 0)
		{
			MoveContext context = new MoveContext(this, board, 1, player);
			ReinforcementsMove move = bots[player].getReinforcementsMove(context);
			executeReinforcementMoves(context, move, true);

			initialArmies[player]--;

			player++;
			if (player > numPlayers)
			{
				player = 1;
			}
		}

        show = true;
        //D.out("After setup");
		started = true;
		ui.showBoardPlain();
		
		while(!ui.getStopEarly() && !gameOver)
		{
			turn++;
			D.out("Starting turn:" + turn);
			for (int i = 1; i <= numPlayers; i++)
			{
				if(ui.getStopEarly())
				{
					break;
				}
				if (isPlayerInactive(i))
				{
					//todo: Check for all players inactive
					continue;
				}
				
				playReinforcementsPhase(i);
				cardDrawn = false;
				while (!ui.getStopEarly() && !gameOver && playAttackPhase(i))
				{
					checkGameOver();
				}

				if(!gameOver && !ui.getStopEarly())
				{
					playMovePhase(i);
				}
				
				ui.showBoardPlain();
			}
		}

		ui.gameOver(gameOverMessage);
	}
	
	public void  executeSetupMove(MoveContext context, SetupMove move)
	{
		if (!RiskUtils.isSetupMoveValid(context, move))
		{
			move = RiskUtils.getRandomSetupMove(context);
		}
		
		board.getArmies()[move.getTerritory()][0] = context.getPlayerNum();
		board.getArmies()[move.getTerritory()][1] = 1;
		ui.showHighlight(move.getTerritory(), true);
	}
	
	public void  playReinforcementsPhase(int player)
	{
		int reinforcements = calcReinforcements(player);
		
		reinforcements += playTradeCardsPhase(player);
		
		MoveContext context = new MoveContext(this, board, reinforcements, player);
		ReinforcementsMove move = bots[player].getReinforcementsMove(context);
		executeReinforcementMoves(context, move);
	}
	
	public void  executeReinforcementMoves(MoveContext context, ReinforcementsMove move)
	{
		executeReinforcementMoves(context, move, false);
	}
	
	public void executeReinforcementMoves(MoveContext context, ReinforcementsMove move, boolean hurry)
	{
		//todo: Accept or check for null?
		if (!RiskUtils.isReinforcementsMoveValid(context, move))
		{
			move = RiskUtils.getRandomReinforcementsMove(context);
			//todo: report
		}
		
		for (int j = 0; j < move.getArmies().length; j++)
		{
			ReinforcementsStruct army = move.getArmies()[j];
			if(!hurry)
			{
				ui.showHighlight(army.getTerritory(), false);
			}
			board.getArmies()[army.getTerritory()][1] += army.getArmies();
			//ui.showReinforcements(context.Player, army);
			
			if(!hurry || ui.getSpeed() < 2)
			{
				ui.showHighlight(army.getTerritory(), false);
			}
		}
		
		//todo: Have a report reinforcements, report attack, report move method(?)
	}
	
	public int playTradeCardsPhase(int player)
	{
		int cardReinforcements = 0;
		boolean trade = cards.canTrade(player);
		boolean countryReinforcementMade = false;
		while(!ui.getStopEarly() && trade)
		{
			MoveContext context = new MoveContext(this, board, 0, player);
			TradeCardsMove move = bots[player].getTradeCardsMove(context);
			
			if (!RiskUtils.isTradeCardsMoveValid(context, move))
			{
				//todo: Report bad move?
				move = new TradeCardsMove(cards.suggestTrade(player));
			}
			
			if (!countryReinforcementMade && suggestReinforcements(player, move.getCards()) != - 1)
			{
				//todo: Call bot?
				int country = suggestReinforcements(player, move.getCards()); //bots[player].GetTradeCardsReinforcementCountry(context, move.GetCards());
				
				if (country != - 1)
				{
					if (country >= 0 && country < Board.NUM_COUNTRIES && board.getArmies()[country][0] == player && move.getCards().contains(country))
					{
						board.getArmies()[country][1] += 2;
						countryReinforcementMade = true;
					}
					else
					{
						//todo: report invalid reinforcement
					}
				}
			}
			
			cardReinforcements += executeTradeCardsPhase(context, move);
			
			if (move.getCards() == null && !cards.mustTrade(player))
			{
				trade = false;
			}
		}
		
		return cardReinforcements;
	}
	
	public int executeTradeCardsPhase(MoveContext context, TradeCardsMove move)
	{
		return cards.trade(context.getPlayerNum(), move.getCards());
	}
	
	public int calcReinforcements(int player)
	{
		int ownedTerritories = 0;
		int reinforcements = 0;
		
		boolean countryOwned = true;
		for (int i = 0; i < Board.NUM_COUNTRIES; i++)
		{
			if (board.getArmies()[i][0] == player)
			{
				ownedTerritories += 1;
			}
			else
			{
				countryOwned = false;
			}
			
			if (i == 8)
			{
				// North America
				if (countryOwned)
				{
					reinforcements += 5;
				}
				countryOwned = true;
			}
			else if (i == 15)
			{
				// Europe
				if (countryOwned)
				{
					reinforcements += 5;
				}
				countryOwned = true;
			}
			else if (i == 27)
			{
				// Asia
				if (countryOwned)
				{
					reinforcements += 7;
				}
				countryOwned = true;
			}
			else if (i == 33)
			{
				// Africa
				if (countryOwned)
				{
					reinforcements += 3;
				}
				countryOwned = true;
			}
			else if (i == 37)
			{
				// Australia
				if (countryOwned)
				{
					reinforcements += 2;
				}
				countryOwned = true;
			}
			else if (i == 41)
			{
				// South America
				if (countryOwned)
				{
					reinforcements += 2;
				}
				countryOwned = true;
			}
		}
		
		int territoryBonus = ownedTerritories / 3;
		if (territoryBonus < 3)
		{
			territoryBonus = 3;
		}
		
		reinforcements += territoryBonus;
		
		
		return reinforcements;
	}
	
	public boolean playAttackPhase(int player)
	{
		MoveContext context = new MoveContext(this, board, 0, player);
		AttackMove move = bots[player].getAttackMove(context);
		return executeAttackMove(context, move);
	}
	
	public boolean executeAttackMove(MoveContext context, AttackMove move)
	{
		if (move == null)
		{
			return false;
		}
		
		if (!RiskUtils.isAttackMoveValid(context, move))
		{
			debugOut("Bot " + bots[context.getPlayerNum()].getClass().getName() + " made invalid Attack move", move);
			return false;
		}
		
		ui.showArrow(context.getPlayerNum(), move);
		resolveAttack(move);
		ui.showArrow(context.getPlayerNum(), move);
		
		return true;
	}
	
	public void  resolveAttack(AttackMove attack)
	{
		int to = attack.getTo();
		int from = attack.getFrom();
		int attacking = attack.getArmies();
		int defending = board.getArmies()[to][1];
		int player = board.getArmies()[from][0];
		int lastNumAttackingDie = 0;
		
		while (attacking != 0 && defending != 0)
		{
			int attackingDice = Math.min(attacking, 3);
			lastNumAttackingDie = attackingDice;
			
			int defendingDice = Math.min(defending, 2);
			
			int[] attackRolls = new int[attackingDice];
			int[] defendingRolls = new int[defendingDice];
			
			for (int i = 0; i < attackingDice; i++)
			{
				attackRolls[i] = dieRoll();
			}
			
			for (int i = 0; i < defendingDice; i++)
			{
				defendingRolls[i] = dieRoll();
			}
			
			Arrays.sort(attackRolls);
			Arrays.sort(defendingRolls);
			
			int battles = Math.min(attackingDice, defendingDice);
			
			int attackDieAt = attackRolls.length - 1;
			int defendDieAt = defendingRolls.length - 1;
			int attackWins = 0;
			int defenseWins = 0;
			
			for (int i = 0; i < battles; i++)
			{
				if (attackRolls[attackDieAt--] > defendingRolls[defendDieAt--])
				{
					attackWins++;
				}
				else
				{
					defenseWins++;
				}
			}
			
			board.getArmies()[from][1] -= defenseWins;
			attacking -= defenseWins;
			
			board.getArmies()[to][1] -= attackWins;
			defending -= attackWins;
			
			if (attacking > 0 && defending > 0)
			{
				MoveContext context = new MoveContext(this, board, 0, player);
				attacking = bots[player].getBattleOrders(context, new AttackMove(attacking, from, to));
				if (attacking <= 0 || attacking > board.getArmies()[from][1] - 1)
				{
					attacking = 0;
				}
			}
		}
		
		if (defending == 0)
		{
			if (!cardDrawn)
			{
				cards.draw(player);
				cardDrawn = true;
			}
			
			int defendingPlayer = board.getArmies()[to][0];
			
			MoveContext context = new MoveContext(this, board, 0, player);
			attacking = bots[player].getPossesionNumber(context, new AttackMove(attacking, from, to));
			if (attacking < lastNumAttackingDie || attacking > board.getArmies()[from][1] - 1)
			{
				attacking = lastNumAttackingDie;
			}
			
			board.getArmies()[to][0] = player;
			board.getArmies()[to][1] = attacking;
			board.getArmies()[from][1] -= attacking;
			
			boolean lastCountry = true;
			for (int i = 0; i < board.getArmies().length; i++)
			{
				int[] army = board.getArmies()[i];
				if (army[0] == defendingPlayer)
				{
					lastCountry = false;
					break;
				}
			}
			
			if (lastCountry)
			{
				//todo: set defending player inactive
				cards.transferCards(defendingPlayer, player);
			}
			
			//D.out_Renamed("Player " + player + " takes control of " + Board.countries[to]);
		}
	}
	
	public int dieRoll()
	{
		return rand.nextInt(6) + 1;
	}
	
	public void  playMovePhase(int player)
	{
		MoveContext context = new MoveContext(this, board, 0, player);
		Move move = bots[player].getMove(context);
		executeMove(context, move);
	}
	
	public void  executeMove(MoveContext context, Move move)
	{
		if (move == null)
		{
			return ;
		}
		
		if (!RiskUtils.isMoveValid(context, move))
		{
			//todo: Output move
			D.out("Bot " + bots[context.getPlayerNum()].getClass().getName() + " made invalid move.");
			return ;
		}
		
		AttackMove s = new AttackMove(move.getArmies(), move.getFrom(), move.getTo());
		ui.showDottedArrow(context.getPlayerNum(), s);
		board.getArmies()[move.getTo()][1] += move.getArmies();
		board.getArmies()[move.getFrom()][1] -= move.getArmies();
		ui.showDottedArrow(context.getPlayerNum(), s);
	}
	
	public void  checkGameOver()
	{
		if (turn == 1000)
		{
			for(int i=0; i < numPlayers; i++)
			{
				botInfos.get(i).draw();
			}
			
			gameOverMessage = "Draw";
			gameOver = true;
			return ;
		}
		
		int possibleWinner = - 1;
		for (int i = 0; i < Board.NUM_COUNTRIES; i++)
		{
			int owner = board.getArmies()[i][0];
			//todo: Handle all players inactive(?)
			if (!isPlayerInactive(owner))
			{
				if (possibleWinner == - 1)
				{
					possibleWinner = owner;
				}
				else
				{
					if (owner != possibleWinner)
					{
						return ;
					}
				}
			}
		}
		
		int winner = possibleWinner - 1;
		
		for(int i=0; i < botInfos.size(); i++)
		{
			if(i == winner)
			{
				botInfos.get(i).won();
			}
			else
			{
				botInfos.get(i).lost();				
			}
		}
		gameOverMessage = "Player " + possibleWinner + " Wins";
		gameOver = true;
	}
	
	boolean isPlayerInactive(int player)
	{
		for (int i = 0; i < Board.NUM_COUNTRIES; i++)
		{
			if (board.getArmies()[i][0] == player)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public int suggestReinforcements(int player, ArrayList<Card> cards)
	{
		if (cards == null)
		{
			return - 1;
		}
		
		ArrayList ownedCountries = new ArrayList();
		for (int i = 0; i < cards.size(); i++)
		{
			int country = cards.get(i).getTerritory();
			if (country > 41)
			{
				continue;
			}
			
			if (board.getArmies()[country][0] == player)
			{
				ownedCountries.add(country);
			}
		}
		
		int suggest = - 1;
		int strength = 0;
		
		for (int i = 0; i < ownedCountries.size(); i++)
		{
			Integer country = (Integer) ownedCountries.get(i);
			if (strength == 0)
			{
				suggest = country;
				strength = 1;
			}
			
			if (strength < 3)
			{
				if (RiskUtils.isBorderedByEnemy(board, country))
				{
					suggest = country;
					strength = 2;
				}
			}
		}
		
		return suggest;
	}
	
	private static void debugOut(String s, AttackMove attack)
	{
		if(D.debugOn())
		{
			D.out("++++++++++++++++");
			D.out(s);
			D.out("AttackMove from=" + attack.getFrom() + " armies=" + attack.getArmies() + " to=" + attack.getTo());
			D.out("++++++++++++++++");
		}
	}
}
