package com.gamebotarena.twixt.api;

public abstract class TwixtGameBot
{
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract Move getMove(MoveContext context);
}
