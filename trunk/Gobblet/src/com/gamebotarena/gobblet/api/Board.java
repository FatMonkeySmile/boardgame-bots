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
