package com.gamebotarena.xiangqi.api;

public class RandomXiangqiGameBot extends XiangqiGameBot
{
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
}
