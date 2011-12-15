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
