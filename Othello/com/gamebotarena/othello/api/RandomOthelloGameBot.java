package com.gamebotarena.othello.api;

public class RandomOthelloGameBot extends OthelloGameBot
{
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
}
