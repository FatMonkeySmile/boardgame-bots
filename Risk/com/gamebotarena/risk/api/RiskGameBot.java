package com.gamebotarena.risk.api;

import java.util.ArrayList;

public abstract class RiskGameBot
{
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract SetupMove getSetupMove(MoveContext context);
	
	public TradeCardsMove getTradeCardsMove(MoveContext context)
	{
		return context.suggestTrade();
	}
	
	public int getTradeCardsReinforcementTerritory(MoveContext context, ArrayList<Card> cards)
	{
		return context.suggestTradeCardsReinforcementTerritory(cards);
	}
	
	public abstract ReinforcementsMove getReinforcementsMove(MoveContext context);
	
	public abstract AttackMove getAttackMove(MoveContext context);
	
	public int getBattleOrders(MoveContext context, AttackMove attack)
	{
		return attack.getArmies();
	}
	
	public int getPossesionNumber(MoveContext context, AttackMove attack)
	{
		return attack.getArmies();
	}
	
	public abstract Move getMove(MoveContext context);
}
