package com.gamebotarena.gobblet.api;

public class Piece 
{
	int player;
	int size;
	
	public Piece(int player, int size)
	{
		this.player = player;
		this.size = size;
	}
	
	public int getPlayer()
	{
		return player;
	}
	
	public int getSize()
	{
		return size;
	}
}
