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

package com.gamebotarena.gobblet.api;

import java.util.Stack;

public class Square 
{
	Stack<Piece> pieces = new Stack<Piece>();
	
	public Square()
	{
	}
	
	public void play(Piece piece)
	{
		if(pieces.size() != 0 && pieces.peek().getSize() >= piece.getSize())
		{
			//todo: report correctly...
			System.out.println("Invalid play: " + piece.getSize());
			return;
		}
		pieces.add(piece);
	}
	
	public Piece getPiece()
	{
		if(pieces.size() > 0)
		{
			return pieces.peek();
		}
		
		return null;
	}
	
	//internal...
	public Piece pop()
	{
		if(pieces.size() == 0)
		{
			return null;
		}
		
		return pieces.pop();
	}
	
	public Piece peek()
	{
		if(pieces.size() == 0)
		{
			return null;
		}
		
		return pieces.peek();
	}
}
