package com.gamebotarena.chess.api;

public class Board
{
	public char[][] b;

	public Board()
	{
		b = new char[8][];
	    b[0] = new char[]{'r', 'p', ' ', ' ', ' ', ' ', 'P', 'R'};
		b[1] = new char[]{'n', 'p', ' ', ' ', ' ', ' ', 'P', 'N'};
		b[2] = new char[]{'b', 'p', ' ', ' ', ' ', ' ', 'P', 'B'};
		b[3] = new char[]{'k', 'p', ' ', ' ', ' ', ' ', 'P', 'K'};
		b[4] = new char[]{'q', 'p', ' ', ' ', ' ', ' ', 'P', 'Q'};
		b[5] = new char[]{'b', 'p', ' ', ' ', ' ', ' ', 'P', 'B'};
		b[6] = new char[]{'n', 'p', ' ', ' ', ' ', ' ', 'P', 'N'};
		b[7] = new char[]{'r', 'p', ' ', ' ', ' ', ' ', 'P', 'R'};
	}
	
	public char[][] getSquares()
	{
		return b;
	}
}
