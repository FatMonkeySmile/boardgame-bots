package com.gamebotarena.gobblet.api;

public class RandomGobbletGameBot extends GobbletGameBot
{
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
}
