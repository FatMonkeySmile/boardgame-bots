package com.gamebotarena.twixt.api;

public class Move
{
	public static Move SwapMove = new Move(-1, -1);

	public int x;
	public int y;
	public boolean forceLinks;

	public Move(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Move(int x, int y, boolean forceLinks)
	{
		this.x = x;
		this.y = y;
		this.forceLinks = forceLinks;
	}
}
