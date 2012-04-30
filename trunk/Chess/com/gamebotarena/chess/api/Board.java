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
