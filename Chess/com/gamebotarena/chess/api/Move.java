package com.gamebotarena.chess.api;

public class Move
{
	public int fromx;
	public int fromy;
	public int tox;
	public int toy;
	
	public Move(int fromx, int fromy, int tox, int toy)
	{
		this.fromx = fromx;
		this.fromy = fromy;
		this.tox = tox;
		this.toy = toy;
	}
}
