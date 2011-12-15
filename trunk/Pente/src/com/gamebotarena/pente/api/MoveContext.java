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

import com.gamebotarena.pente.core.Game;
import com.gamebotarena.pente.core.PenteUtils;

public class MoveContext
{
	Board board;
	Move oppMove;
	int playerNum;
	public Game game;
	
	public MoveContext(Game game, Board board, Move oppMove, int playerNum)
	{
		this.game = game;
		this.board = board;
		this.oppMove = oppMove;
		this.playerNum = playerNum;
	}
	
	public int getPlayerNum()
	{
		return playerNum;
	}
	
	public Board getBoard()
	{
		return board;
	}

	public Move getLastMove()
	{
		return oppMove;
	}

	public boolean isMoveValid(Move move)
	{
		return PenteUtils.isMoveValid(board, move, playerNum);
	}

	public Move getRandomMove()
	{
		return PenteUtils.getRandomMove(this);
	}
	
	public int getCaptureCount(int player)
	{
		return PenteUtils.getCaptureCount(this, player);
	}
}
