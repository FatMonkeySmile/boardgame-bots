package com.gamebotarena.hex.api;

import java.util.Random;

public class RandomHexGameBot extends HexGameBot
{
	public int size = 11;
	Random rand = new Random();
	
	public void reset(int playerNum)
	{
		size = Board.getSize();
	}
	
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
}
