package com.gamebotarena.pente.api;

public class Board
{
	public static final int SIZE = 19;
	
	public int[][] squares;
	
	public Board()
	{
		squares = new int[SIZE][];
		for (int i = 0; i < SIZE; i++)
		{
			squares[i] = new int[SIZE];
		}
	}

	public int[][] getSquares()
	{
		return squares;
	}
}
