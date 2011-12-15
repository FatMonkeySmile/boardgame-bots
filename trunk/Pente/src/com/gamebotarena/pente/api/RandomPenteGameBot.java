package com.gamebotarena.pente.api;

public class RandomPenteGameBot extends PenteGameBot
{
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
}
