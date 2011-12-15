package com.gamebotarena.gobblet.api;

public class Board
{
	public Square[][] squares;
	public Square[][] unPlayed;

	public Board()
	{
		squares = new Square[4][4];
		unPlayed = new Square[2][3];
		
		for(int i=0; i < 4; i++)
		{
			for(int j=0; j < 4; j++)
			{
				squares[i][j]= new Square();
			}
		}
		
		for(int i=0; i < 2; i++)
		{
			for(int j=0; j < 3; j++)
			{
				unPlayed[i][j] = new Square();
				for(int k=1; k <= 4; k++)
				{
					unPlayed[i][j].play(new Piece(i, k));
				}
			}
		}
	}
	
	public Square[][] getSquares()
	{
		return squares;
	}
	
	public Square[] getUnPlayed(int player)
	{
		if(player < 0 || player > 1)
		{
			return null;
		}
		
		return unPlayed[player];
	}
}
