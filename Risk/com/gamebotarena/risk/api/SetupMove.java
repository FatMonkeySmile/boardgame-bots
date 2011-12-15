package com.gamebotarena.risk.api;

public class SetupMove
{
	int territory;
	
	public SetupMove(int territory)
	{
		this.territory = territory;
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
