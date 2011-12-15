package com.gamebotarena.pente.api;

public abstract class PenteGameBot
{
	// 1 is amber, 2 is blue
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract Move getMove(MoveContext context);
}
