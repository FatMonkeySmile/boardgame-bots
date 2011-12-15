package com.gamebotarena.othello.api;

public abstract class OthelloGameBot
{
	// 1 is white, 2 is black
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract Move getMove(MoveContext context);
}
