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

import com.gamebotarena.hex.core.Game;

public class MoveContext 
{
	int playerNum;
	int turn;
	Board board;
	Move lastMove;
	
	public MoveContext(int playerNum, int turn, Board board, Move lastMove)
	{
		this.playerNum = playerNum;
		this.turn = turn;
		this.board = board;
		this.lastMove = lastMove;
	}
	
	public int getPlayerNum()
	{
		return playerNum;
	}
	
	public int getTurn()
	{
		return turn;
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	public Move getLastMove()
	{
		return lastMove;
	}
	
	public Move getRandomMove()
	{
		return Game.getRandomMove(this);
	}
	
	public boolean isMoveValid(Move move)
	{
		return Game.isMoveValid(this, move);
	}
}
