package com.gamebotarena.gobblet.api;

public class ExternalMove extends Move
{
	public int stack;
	public int tox;
	public int toy;
	
	public ExternalMove(int stack, int tox, int toy)
	{
		this.stack = stack;
		this.tox = tox;
		this.toy = toy;
	}
}