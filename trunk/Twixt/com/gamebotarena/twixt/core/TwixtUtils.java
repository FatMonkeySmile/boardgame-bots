package com.gamebotarena.twixt.core;

import java.util.Random;

import com.gamebotarena.twixt.api.Board;
import com.gamebotarena.twixt.api.Move;
import com.gamebotarena.twixt.api.MoveContext;

// Game util methods for getting random moves, checking for valid moves, etc.
public class TwixtUtils
{
	static Random rand = new Random();

	static boolean NextBool()
	{
		return (rand.nextInt(2) % 2 == 0);
	}

	public static Move getRandomMove(MoveContext context)
	{
		if(context.isMoveValid(Move.SwapMove))
		{
			if(NextBool())
			{
				return Move.SwapMove;
			}
		}

		Move move = null;

		int tries = 0;
		do
		{
			move = new Move(rand.nextInt(Board.SIZE), rand.nextInt(Board.SIZE), NextBool());
			tries++;
		}
		while(!context.isMoveValid(move) && tries < 10000);

		if(tries == 10000)
		{
			for(int x=0; x < Board.SIZE; x++)
			{
				for(int y=0; y < Board.SIZE; y++)
				{
					move = new Move(x, y, NextBool());
					if(context.isMoveValid(move))
					{
						return move;
					}
				}
			}

			// Should never get here if game over check is working properly in Game.
			move = null;
		}

		return move;
	}

	public static boolean isMoveValid(MoveContext context, Move move)
	{
		if(move == Move.SwapMove)
		{
			if(context.getTurn() == 1 && context.canSwitch && context.lastMove.y != 0 && context.lastMove.y + 1 != Board.SIZE)
			{
				return true;
			}

			return false;
		}

		Board board = context.board;
		if(!isPositionValid(move.x, move.y))
		{
			return false;
		}

		if(board.b[move.x][move.y] != 0)
		{
			return false;
		}

		if(move.y == 0 || move.y + 1 == Board.SIZE)
		{
			return false;
		}

		return true;
	}

	static boolean isPositionValid(int x, int y)
	{
		if(x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE)
		{
			return false;
		}

		if(isCorner(x, y))
		{
			return false;
		}

		return true;
	}

	static boolean isCorner(int x, int y)
	{
		if(x == 0 && y == 0)
		{
			return true;
		}

		if(x == 0 && y + 1 == Board.SIZE)
		{
			return true;
		}

		if(x + 1 == Board.SIZE && y == 0)
		{
			return true;
		}

		if(x + 1 == Board.SIZE && y + 1 == Board.SIZE)
		{
			return true;
		}

		return false;
	}
}
