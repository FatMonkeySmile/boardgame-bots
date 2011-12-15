package com.gamebotarena.linesofaction.api;

public class RandomLinesOfActionGameBot extends LinesOfActionGameBot
{
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
}
