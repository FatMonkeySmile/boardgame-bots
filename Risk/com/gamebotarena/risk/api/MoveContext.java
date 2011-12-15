package com.gamebotarena.risk.api;

import java.util.ArrayList;

import com.gamebotarena.risk.core.Game;
import com.gamebotarena.risk.core.RiskUtils;

public class MoveContext
{
	public Game game; //todo: make not visible...
	Board board;
	int player;
	int reinforcements;
	
	public MoveContext(Game game, Board board, int reinforcements, int player)
	{
		this.game = game;
		this.board = board;
		this.player = player;
		this.reinforcements = reinforcements;
	}

	public int getPlayerNum()
	{
		return player; 
	}

	public Board getBoard()
	{
		return board;
	}

	public ArrayList<Card> getCards()
	{
		return game.cards.getHand(player);
	}
	
	public int getReinforcements()
	{
		return reinforcements;
	}
	
	public int getTurn()
	{
		return game.getTurn(); 
	}

	public Move getRandomMove()
	{
		return RiskUtils.getRandomMove(this);
	}
	
	public TradeCardsMove suggestTrade()
	{
		return new TradeCardsMove(game.cards.suggestTrade(player));
	}
	
	public boolean isValid(SetupMove setupMove)
	{
		return RiskUtils.isSetupMoveValid(this, setupMove);
	}
	
	public boolean isValid(ReinforcementsMove move)
	{
		return RiskUtils.isReinforcementsMoveValid(this, move);
	}
	
	public boolean isValid(AttackMove move)
	{
		return RiskUtils.isAttackMoveValid(this, move);
	}
	
	public boolean isMoveValid(Move move)
	{
		return RiskUtils.isMoveValid(this, move);
	}
	
	public boolean isBorderedByEnemy(int territory)
	{
		return RiskUtils.isBorderedByEnemy(board, territory);
	}
	
	public int suggestTradeCardsReinforcementTerritory(ArrayList<Card> cards)
	{
		return game.suggestReinforcements(player, cards);
	}

	public SetupMove getRandomSetupMove()
	{
		return RiskUtils.getRandomSetupMove(this);
	}

	public ReinforcementsMove getRandomReinforcementsMove()
	{
		return RiskUtils.getRandomReinforcementsMove(this);
	}

	public AttackMove getRandomAttackMove()
	{
		return RiskUtils.getRandomAttackMove(this);
	}
}
