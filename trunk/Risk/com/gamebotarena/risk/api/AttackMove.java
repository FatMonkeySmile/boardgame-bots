package com.gamebotarena.risk.api;

public class AttackMove
{
	int armies;
	int from;
	int to;
	
	public AttackMove(int armies, int from, int to)
	{
		this.armies = armies;
		this.from = from;
		this.to = to;
	}
	
	public AttackMove(int armies, Territory from, Territory to)
	{
		this.armies = armies;
		this.from = from.getTerritoryNumber();
		this.to = to.getTerritoryNumber();
	}
	
	public int getArmies()
	{
		return armies;
	}
	
	public int getFrom()
	{
		return from;
	}
	
	public int getTo()
	{
		return to;
	}
}
