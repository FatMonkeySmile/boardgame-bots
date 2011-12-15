package com.gamebotarena.risk.api;

public class Card 
{
	public static int TYPE_WILD = 0;
	public static int TYPE_INFANTRY = 1;
	public static int TYPE_CALVARY = 2;
	public static int TYPE_CANNON = 3;
	
	public static final int NUM_CARDS = 44;
	
	int territory;
	int type;

	public Card(int territory, int type)
	{
		this.territory = territory;
		this.type = type;
	}
	
	public int getTerritory()
	{
		return territory;
	}
	
	public int getType()
	{
		return type;
	}
}
