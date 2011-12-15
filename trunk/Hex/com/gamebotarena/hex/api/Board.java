package com.gamebotarena.hex.api;

public class Board 
{
	public static int SIZE = 11;
	
	public int[][] b = new int[SIZE][SIZE];
	public boolean[][] c = new boolean[SIZE][SIZE];
	public boolean[][] c2 = new boolean[SIZE][SIZE];
	public boolean[][] check = new boolean[SIZE][SIZE];
	
	public int[][] getHexes()
	{
		return b;
	}
	
	public static int getSize()
	{
		return SIZE;
	}
	
	public void clearConnections()
	{
		for(int i=0; i < Board.SIZE; i++)
		{
			for(int j=0; j < Board.SIZE; j++)
			{
				c[i][j] = false;
			}
		}		
	}
	
	public void clearCheck()
	{
		for(int i=0; i < Board.SIZE; i++)
		{
			for(int j=0; j < Board.SIZE; j++)
			{
				check[i][j] = false;
			}
		}				
	}
}
