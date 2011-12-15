package com.gamebotarena.xiangqi.api;

public abstract class XiangqiGameBot
{
	// 0 is red, 1 is black
	protected int playerNum;
	
	public void setup(int playerNum)
	{
		this.playerNum = playerNum;
	}
	
	public abstract Move getMove(MoveContext context);
}
