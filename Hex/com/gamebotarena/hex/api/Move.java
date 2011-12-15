package com.gamebotarena.hex.api;

public class Move 
{
	public static final Move SwapMove = new Move(-1, -1);
	
	private int x;
	private int y;
	
	public Move(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
