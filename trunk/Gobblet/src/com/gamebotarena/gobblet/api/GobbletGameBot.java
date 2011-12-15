package com.gamebotarena.gobblet.api;

public abstract class GobbletGameBot
{
	// 0 is white, 1 is black
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract Move getMove(MoveContext context);
}
