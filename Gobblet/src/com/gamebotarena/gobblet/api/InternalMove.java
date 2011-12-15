package com.gamebotarena.gobblet.api;

public class InternalMove extends Move
{
	public int fromx;
	public int fromy;
	public int tox;
	public int toy;
	
	public InternalMove(int fromx, int fromy, int tox, int toy)
	{
		this.fromx = fromx;
		this.fromy = fromy;
		this.tox = tox;
		this.toy = toy;
	}
}
