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

import com.gamebotarena.linesofaction.core.Game;

public class MoveContext
{
	Board board;
	int player;
	//todo: make game private in all Games...
	private Game game;

	public MoveContext(Game game, Board board, int player)
	{
		this.game = game;
		this.board = board;
		this.player = player;
	}
	
	public Board getBoard()
	{
		return board;
	}

	public int getPlayerNum()
	{
		return player;
	}
	
	public boolean isMoveValid(Move move)
	{
		return game.isMoveValid(this, move);
	}

	public Move getRandomMove()
	{
		return game.getRandomMove(this);
	}
}
