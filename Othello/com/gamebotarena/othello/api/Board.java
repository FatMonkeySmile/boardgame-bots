/*
 * Copyright (C) 2011-2012 Boardgame Bots (http://code.google.com/p/boardgame-bots)
 * 
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *   
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
