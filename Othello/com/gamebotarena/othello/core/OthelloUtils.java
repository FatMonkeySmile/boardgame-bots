package com.gamebotarena.othello.core;

import java.util.Random;

import com.gamebotarena.othello.api.Board;
import com.gamebotarena.othello.api.Move;
import com.gamebotarena.othello.api.MoveContext;

public class OthelloUtils
{
	static Random rand = new Random();

	public static boolean isMoveValid(Board board, Move move, int player)
	{
		if (move == Move.Pass)
		{
			for (int x = 0; x < 8; x++)
			{
				for (int y = 0; y < 8; y++)
				{
					if (isMoveValid(board, new Move(x, y), player))
					{
						return false;
					}
				}
			}
			return true;
		}
		
		if(move.x < 0 || move.x >= 8 || move.y < 0 || move.y >= 8)
		{
			return false;
		}
		
		if(board.getSquares()[move.x][move.y] != 0)
		{
			return false;
		}
		
		return processMove(board, move, player, false);
	}
	
	static boolean processMove(Board board, Move move, int player, boolean reverse)
	{
		if (checkLine(board, player, move, - 1, 0, reverse))
		{
			return true;
		}
		if (checkLine(board, player, move, 1, 0, reverse))
		{
			return true;
		}
		if (checkLine(board, player, move, 0, - 1, reverse))
		{
			return true;
		}
		if (checkLine(board, player, move, 0, 1, reverse))
		{
			return true;
		}
		if (checkLine(board, player, move, - 1, - 1, reverse))
		{
			return true;
		}
		if (checkLine(board, player, move, 1, 1, reverse))
		{
			return true;
		}
		if (checkLine(board, player, move, - 1, 1, reverse))
		{
			return true;
		}
		if (checkLine(board, player, move, 1, - 1, reverse))
		{
			return true;
		}
		
		return false;
	}
	
	static boolean checkLine(Board board, int player, Move move, int xdiff, int ydiff, boolean reverse)
	{
		int x = move.x + xdiff;
		int y = move.y + ydiff;
		int startx = x;
		int starty = y;
		boolean opp = false;
		int num = 0;
		
		while (isOpp(board, player, x, y))
		{
			num++;
			x += xdiff;
			y += ydiff;
			opp = true;
		}
		if (opp)
		{
			if (isCurrentPlayer(board, player, x, y))
			{
				if (!reverse)
				{
					return true;
				}
				else
				{
					reverseSquares(player, board, startx, starty, xdiff, ydiff, num);
				}
			}
		}
		
		return false;
	}
	
	static void  reverseSquares(int player, Board board, int startx, int starty, int xdiff, int ydiff, int num)
	{
		for (int i = 0; i < num; i++)
		{
			board.squares[startx + (xdiff * i)][starty + (ydiff * i)] = player;
		}
	}
	
	static boolean isOpp(Board board, int player, int x, int y)
	{
		if (x < 0 || x >= 8 || y < 0 || y >= 8)
		{
			return false;
		}
		
		int val = board.squares[x][y];
		
		if (val != 0 && val != player)
		{
			return true;
		}
		
		return false;
	}
	
	static boolean isCurrentPlayer(Board board, int player, int x, int y)
	{
		if (x < 0 || x >= 8 || y < 0 || y >= 8)
		{
			return false;
		}
		
		int val = board.squares[x][y];
		
		if (val == player)
		{
			return true;
		}
		
		return false;
	}

	public static Move getRandomMove(MoveContext context)
	{
		Move move = new Move(- 1, - 1);
		int tries = 0;

		boolean noMoveValid = context.isMoveValid(Move.Pass);
		
		do 
		{
			move.x = rand.nextInt(8);
			move.y = rand.nextInt(8);
		}
		while (!context.isMoveValid(move) && (tries++ < 100 || !noMoveValid));
		
		if (tries == 100 && noMoveValid)
		{
			move = Move.Pass;
		}
		
		return move;
	}
}
