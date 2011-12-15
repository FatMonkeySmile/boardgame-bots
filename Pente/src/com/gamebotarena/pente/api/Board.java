/*
 * Copyright (C) 2011 Boardgame Bots (http://code.google.com/p/boardgame-bots)
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