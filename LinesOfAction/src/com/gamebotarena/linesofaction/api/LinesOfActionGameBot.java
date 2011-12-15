package com.gamebotarena.linesofaction.api;

public abstract class LinesOfActionGameBot
{
	// 1 is black, 2 is white
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract Move getMove(MoveContext context);
}
