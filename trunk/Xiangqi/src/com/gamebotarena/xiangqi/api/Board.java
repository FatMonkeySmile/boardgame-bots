package com.gamebotarena.xiangqi.api;

public class Board
{
	public char[][] b;

	public Board()
	{
		b = new char[9][];
	    b[0] = new char[]{'r', ' ', ' ', 'p', ' ', ' ', 'P', ' ', ' ', 'R'};
	    b[1] = new char[]{'h', ' ', 'c', ' ', ' ', ' ', ' ', 'C', ' ', 'H'};
	    b[2] = new char[]{'e', ' ', ' ', 'p', ' ', ' ', 'P', ' ', ' ', 'E'};
	    b[3] = new char[]{'a', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'A'};
	    b[4] = new char[]{'k', ' ', ' ', 'p', ' ', ' ', 'P', ' ', ' ', 'K'};
	    b[5] = new char[]{'a', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'A'};
	    b[6] = new char[]{'e', ' ', ' ', 'p', ' ', ' ', 'P', ' ', ' ', 'E'};
	    b[7] = new char[]{'h', ' ', 'c', ' ', ' ', ' ', ' ', 'C', ' ', 'H'};
	    b[8] = new char[]{'r', ' ', ' ', 'p', ' ', ' ', 'P', ' ', ' ', 'R'};
	}
	
	public char[][] getSquares()
	{
		return b;
	}
}
