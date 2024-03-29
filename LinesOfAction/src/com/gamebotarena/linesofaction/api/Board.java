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
