package com.gamebotarena.twixt.api;

public class RandomTwixtGameBot extends TwixtGameBot
{
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
}
