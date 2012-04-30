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

public class MoveContext
{
	Board board;
	int player;
	boolean[][] castleChecks;
	Location enpassentPossibleTarget;

	Board swappedBoard;
	// Not sure if they should have access to this or not...
	//todo: Give them last move
	Location swappedEnpassentPossibleTarget;
	
	public MoveContext(Board board, int player, boolean[][] castleChecks, Location enpassentPossibleTarget)
	{
		this.board = board;
		this.player = player;
		this.castleChecks = castleChecks;
		this.enpassentPossibleTarget = enpassentPossibleTarget;

		if(player == 1)
		{
			swappedBoard = new Board();
			for(int x=0; x < 8; x++)
			{
				for(int y=0; y < 8; y++)
				{
					char p = board.b[x][y];
					if(p != ' ')
					{
						if(Character.isLowerCase(p))
						{
							p = Character.toUpperCase(p);
						}
						else
						{
							p = Character.toLowerCase(p);
						}
					}
					swappedBoard.b[7 - x][7 - y] = p;
				}
			}
			if(enpassentPossibleTarget != null)
			{
				swappedEnpassentPossibleTarget = new Location(7 - enpassentPossibleTarget.x, 7 - enpassentPossibleTarget.y);
			}
		}
	}
	
	public Board getBoard()
	{
		if(player == 1)
		{
			return swappedBoard;
		}
		else
		{
			return board;
		}
	}

	public int getPlayerNum()
	{
		return player;
	}
	
	public boolean[][] getCastleChecks()
	{
		return castleChecks;
	}
	
	public boolean isMoveValid(Move move)
	{
		if(player == 1)
		{
			move = ChessUtils.swapMove(move);
		}
		return ChessUtils.isMoveValid(this, move);
	}

	public Move getRandomMove()
	{
		Move move = ChessUtils.getRandomMove(this);
		if(player == 1)
		{
			move = ChessUtils.swapMove(move);
		}

		return move;
	}
}
