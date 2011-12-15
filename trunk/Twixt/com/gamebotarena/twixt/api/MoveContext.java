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

package com.gamebotarena.twixt.api;

import com.gamebotarena.twixt.core.*;

public class MoveContext
{
	public int getTurn()
	{
			return turn;
	}
	public Board getBoard()
	{
			if(boardCopy == null)
			{
				boardCopy = new Board(board);
			}

			return boardCopy;
	}
	public int getPlayerNum()
	{
			return player;
		
	}
	public Move getLastMove()
	{
			return lastMove;
	}

	public Board boardCopy;
	public int turn;
	public Board board;
	public int player;
	public Move lastMove;
	public boolean canSwitch;
	
	public MoveContext(boolean canSwitch, int turn, Board board, int player, Move lastMove)
	{
		this.canSwitch = canSwitch;
		this.turn = turn;
		this.board = board;
		this.player = player;
		this.lastMove = lastMove;
	}
	
	public boolean isMoveValid(Move move)
	{
		return TwixtUtils.isMoveValid(this, move);
	}

	public Move getRandomMove()
	{
		return TwixtUtils.getRandomMove(this);
	}
}
