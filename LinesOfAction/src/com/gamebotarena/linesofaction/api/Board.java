package com.gamebotarena.linesofaction.api;

public class Board
{
	public int[][] b;

	public Board()
	{
		b = new int[8][8];
		
		for(int i=1; i < 7; i++)
		{
			b[i][0] = 1;
			b[i][7] = 1;
			
			b[0][i] = 2;
			b[7][i] = 2;
		}
	}
	
	public int[][] getSquares()
	{
		return b;
	}
}
