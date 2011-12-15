package com.gamebotarena.hex.api;

public abstract class HexGameBot
{
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract Move getMove(MoveContext context);
}
