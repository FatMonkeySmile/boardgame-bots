package com.gamebotarena.risk.api;

public class ReinforcementsStruct
{
	int armies;
	int territory;
	
	public ReinforcementsStruct(int armies, int territory)
	{
		this.armies = armies;
		this.territory = territory;
	}

	public ReinforcementsStruct(int armies, Territory territory)
	{
		this.armies = armies;
		this.territory = territory.getTerritoryNumber();
	}
	
	public int getArmies()
	{
		return armies;
	}

	public void setArmies(int value)
	{
		this.armies = value;
	}
	
	public int getTerritory()
	{
		return territory;
	}
	
	public void setTerritory(int value)
	{
		this.territory = value;
	}
}
