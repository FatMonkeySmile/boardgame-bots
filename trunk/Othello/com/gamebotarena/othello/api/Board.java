package com.gamebotarena.othello.api;

public class Board
{
	public int[][] squares;
	
	public Board()
	{
		squares = new int[8][];
		for (int i = 0; i < 8; i++)
		{
			squares[i] = new int[8];
		}
		
		squares[3][3] = 2;
		squares[4][4] = 2;
		squares[3][4] = 1;
		squares[4][3] = 1;
	}

	public int[][] getSquares()
	{
		return squares;
	}
}
