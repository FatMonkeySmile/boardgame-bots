package com.gamebotarena.othello.api;

public class Move
{
	public static final Move Pass = new Move(-1, -1);

	public int x;
	public int y;
	
	public Move(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
