package com.gamebotarena.risk.api;

public class Move
{
	public int getArmies()
	{
		return armies;
	}
	
	public void setArmies(int value)
	{
		this.armies = value;
	}

	public int getFrom()
	{
		return from;
	}
	
	public void setFrom(int value)
	{
		this.from = value;
	}

	public int getTo()
	{
		return to;
	}

	public void setTo(int value)
	{
		this.to = value;
	}
	
	private int armies;
	private int from;
	private int to;
	
	public Move(int armies, int from, int to)
	{
		this.armies = armies;
		this.from = from;
		this.to = to;
	}
	
	public Move(int armies, Territory from, Territory to)
	{
		this.armies = armies;
		this.from = from.getTerritoryNumber();
		this.to = to.getTerritoryNumber();
	}
}

