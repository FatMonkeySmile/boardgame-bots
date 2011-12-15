package com.gamebotarena.risk.core;

import java.util.ArrayList;
import java.util.Random;

import com.gamebotarena.risk.api.AttackMove;
import com.gamebotarena.risk.api.Board;
import com.gamebotarena.risk.api.Move;
import com.gamebotarena.risk.api.MoveContext;
import com.gamebotarena.risk.api.ReinforcementsMove;
import com.gamebotarena.risk.api.ReinforcementsStruct;
import com.gamebotarena.risk.api.SetupMove;
import com.gamebotarena.risk.api.TradeCardsMove;

public class RiskUtils
{
	static Random rand = new Random();

	public static boolean isSetupMoveValid(MoveContext context, SetupMove move)
	{
		if (move.getTerritory() < 0 || move.getTerritory() >= Board.NUM_COUNTRIES)
		{
			return false;
		}
		return (context.getBoard().getArmies()[move.getTerritory()][0] == 0);
	}
	
	public static boolean isTradeCardsMoveValid(MoveContext context, TradeCardsMove move)
	{
		if (move.getCards() == null && context.game.cards.mustTrade(context.getPlayerNum()))
		{
			return false;
		}
		
		if (move.getCards() == null)
		{
			return true;
		}
		
		return context.game.cards.isValidTrade(context.getPlayerNum(), move.getCards());
	}
	
	public static boolean isReinforcementsMoveValid(MoveContext context, ReinforcementsMove move)
	{
		int reinforcements = 0;
		for (int i = 0; i < move.getArmies().length; i++)
		{
			ReinforcementsStruct reinforcementsStruct = move.getArmies()[i];
			if (!isCountryValid(reinforcementsStruct.getTerritory()))
			{
				return false;
			}
			
			if (context.getBoard().getArmies()[reinforcementsStruct.getTerritory()][0] != context.getPlayerNum())
			{
				return false;
			}
			
			if (reinforcementsStruct.getArmies() < 0)
			{
				return false;
			}
			
			reinforcements += reinforcementsStruct.getArmies();
		}
		
		if (reinforcements > context.getReinforcements())
		{
			return false;
		}
		
		return true;
	}
	
	public static boolean isBorderedByEnemy(Board board, int country)
	{
		int player = board.getArmies()[country][0];
		for (int i = 0; i < Board.getConnections()[country].length; i++)
		{
			int borderCountry = Board.getConnections()[country][i];
			if (board.getArmies()[borderCountry][0] != player)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isAttackMoveValid(MoveContext context, AttackMove attack)
	{
		if (attack == null)
		{
			return true;
		}
		
		if (!isCountryValid(attack.getFrom()) || !isCountryValid(attack.getTo()))
		{
			return false;
		}
		
		if (attack.getArmies() <= 0)
		{
			return false;
		}
		
		if (context.getBoard().getArmies()[attack.getFrom()][0] != context.getPlayerNum())
		{
			return false;
		}
		
		if (attack.getArmies() > context.getBoard().getArmies()[attack.getFrom()][1] - 1)
		{
			return false;
		}
		
		if (!areCountriesConnected(attack.getFrom(), attack.getTo()))
		{
			return false;
		}
		
		if (context.getBoard().getArmies()[attack.getTo()][0] == context.getPlayerNum())
		{
			return false;
		}
		
		return true;
	}
	
	public static boolean isMoveValid(MoveContext context, Move move)
	{
		if (!isCountryValid(move.getFrom()) || !isCountryValid(move.getTo()))
		{
			return false;
		}
		
		if (move.getArmies() <= 0)
		{
			return false;
		}
		
		if (context.getBoard().getArmies()[move.getFrom()][0] != context.getPlayerNum())
		{
			return false;
		}
		
		if (move.getArmies() > context.getBoard().getArmies()[move.getFrom()][1] - 1)
		{
			return false;
		}
		
		if (!areCountriesConnected(move.getFrom(), move.getTo()))
		{
			return false;
		}
		
		if (context.getBoard().getArmies()[move.getTo()][0] != context.getPlayerNum())
		{
			return false;
		}
		
		return true;
	}
	
	public static boolean areCountriesConnected(int c1, int c2)
	{
		if (!isCountryValid(c1))
		{
			return false;
		}
		
		int[] conn = Board.getConnections()[c1];
		
		for (int i = 0; i < conn.length; i++)
		{
			if (conn[i] == c2)
			{
				return true;
			}
		}
		
		return false;
	}
	
	static boolean isCountryValid(int country)
	{
		return (country >= 0 && country < Board.NUM_COUNTRIES);
	}

	public static Move getRandomMove(MoveContext context)
	{
		Move move = null;

		int tries = 0;
		int from;
		int to;
		int armies;
		do 
		{
			from = rand.nextInt(Board.NUM_COUNTRIES);
			armies = context.getBoard().getArmies()[from][1] - 1;
			int[] connections = Board.getConnections()[from];
			to = connections[rand.nextInt(connections.length)];
			//todo: Make all calls consistent
			move = new Move(armies, from, to);
			tries++;
		}
		while(!context.isMoveValid(move) && tries < 1000);

		if(tries == 1000)
		{
			return null;
		}
        return move;
	}

	public static SetupMove getRandomSetupMove(MoveContext context)
	{
		SetupMove move = new SetupMove(-1);
		
		int tries = 100;
		
		int[] count = new int[tries];
		int[] weight = new int[tries];
		
		for(int i=0; i < tries; i++)
		{
			int country = -1;
			do 
			{
				country = rand.nextInt(Board.NUM_COUNTRIES);
				move.setTerritory(country);
			}
			while (!context.isValid(move));
			
			count[i] = country;
		}
		
		Board b = context.getBoard();
		for(int i=0; i < tries; i++)
		{
			for(int j=0; j < Board.getConnections()[count[i]].length; j++)
			{
				if(b.getArmies()[Board.getConnections()[count[i]][j]][0] == context.getPlayerNum())
				{
					weight[i]++;
				}
			}
		}
		
		int high = -1;
		int at = 0;
		
		for(int i=0; i < tries; i++)
		{
			if(weight[i] >= high)
			{
				high = weight[i];
				at = i;
			}
		}
		
		return new SetupMove(count[at]);
	}

	public static ReinforcementsMove getRandomReinforcementsMove(MoveContext context)
	{
		ReinforcementsMove move = new ReinforcementsMove(null);
	
		/*
		int splits = rand.Next(context.Reinforcements) + 1;
		int reinforcements = context.Reinforcements;
		int each = reinforcements / splits;
		int mod = reinforcements % splits;
        
		ReinforcementsStruct[] moveStructs = new ReinforcementsStruct[splits];
		for(int i=0; i < splits; i++)
		{
			int from;
			do 
			{
				from = rand.Next(Board.NUM_COUNTRIES);
			}
			while(context.getBoard().getArmies()[from][0] != context.getPlayerNum());
			if(i == splits - 1 && mod > 0)
			{
				moveStructs[i] = new ReinforcementsStruct(mod, from);
			}
			else
			{
				moveStructs[i] = new ReinforcementsStruct(each, from);
			}
		}
		*/

		ArrayList rs = new ArrayList();
		int left = context.getReinforcements();
		while(left > 0)
		{
			int toDeploy = rand.nextInt(left) + 1;
			if(left < 4)
			{
				toDeploy = left; 
			}
			int from;
			boolean valid = false;
			do 
			{
				from = rand.nextInt(Board.NUM_COUNTRIES);
				if(context.getBoard().getArmies()[from][0] == context.getPlayerNum())
				{
					for(int neighbor : Board.getConnections()[from])
					{
						if(context.getBoard().getArmies()[neighbor][0] != context.getPlayerNum())
						{
							valid = true;
						}
					}
				}
			}
			while(!valid);
			rs.add(new ReinforcementsStruct(toDeploy, from));
			left -= toDeploy;
		}

		ReinforcementsStruct[] moveStructs = new ReinforcementsStruct[rs.size()];
		for(int i=0; i < moveStructs.length; i++)
		{
			moveStructs[i] = (ReinforcementsStruct) rs.get(i);
		}
		
		move = new ReinforcementsMove(moveStructs);
		return move;
	}

	public static AttackMove getRandomAttackMove(MoveContext context)
	{
		//      if(turn == context.GetTurn())
		//      {
		//         return null;
		//      }

        //if (rand.nextInt(10) > 8)
        {
          //  return null;
        }

        int turn = context.getTurn();
		int playerNum = context.getPlayerNum();
		
		AttackMove move = null;
		
		int tries = 0;
		int high = 0;
		int highCountry = -1;
		
		for(int i=0; i < Board.NUM_COUNTRIES; i++)
		{
			if(context.getBoard().getArmies()[i][0] == playerNum && context.getBoard().getArmies()[i][1] > high)
			{
				high = context.getBoard().getArmies()[i][1];
				highCountry = i;
			}
		}
		
		do 
		{
			int from = 0;
			int cTries = 0;
			
			if(tries < 10)
			{
				from = highCountry;
			}
			else
			{
				do 
				{
					from = rand.nextInt(Board.NUM_COUNTRIES);
					cTries++;
				}
				while (cTries != 200 && 
						!(context.getBoard().getArmies()[from][0] == playerNum && 
								context.getBoard().getArmies()[from][1] > 2));
				
				if (cTries == 200)
				{
					return null;
				}
			}
			
			//int armies = rand.nextInt(context.GetBoard().armies[from][1] - 1) + 1;
			int armies = context.getBoard().getArmies()[from][1] - 1;
			
			int to = Board.getConnections()[from][rand.nextInt(Board.getConnections()[from].length)];
			move = new AttackMove(armies, from, to);
			tries++;
		}
		while (tries != 100 && !context.isValid(move));
		
		if (tries == 100)
		{
			return null;
		}
		
		return move;
	}
}
