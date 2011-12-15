package com.gamebotarena.chess.api;

public abstract class ChessGameBot
{
	// 0 is white, 1 is black
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract Move getMove(MoveContext context);

	public UpgradePawnMove getUpgradePawnMove(MoveContext context)
	{
		return new UpgradePawnMove('q');
	}
}
