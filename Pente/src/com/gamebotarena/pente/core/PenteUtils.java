package com.gamebotarena.pente.core;

import java.util.Random;

import com.gamebotarena.pente.api.Board;
import com.gamebotarena.pente.api.Move;
import com.gamebotarena.pente.api.MoveContext;

public class PenteUtils
{
	static Random rand = new Random();

	public static boolean isMoveValid(Board board, Move move, int player)
	{
		if(move.x < 0 || move.x >= Board.SIZE || move.y < 0 || move.y >= Board.SIZE)
		{
			return false;
		}
		
		if(board.getSquares()[move.x][move.y] != 0)
		{
			return false;
		}
		
		return true;
	}
	
	static int getSquareSafe(Board board, int x, int y)
	{
		if(x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE)
		{
			return 0;
		}
		
		return board.squares[x][y];
	}
	
	static void processMove(Board board, Move move, int player, int[] captures)
	{
		checkLine(board, player, move, - 1, 0, captures);
		checkLine(board, player, move, 1, 0, captures);
		checkLine(board, player, move, 0, - 1, captures);
		checkLine(board, player, move, 0, 1, captures);
		checkLine(board, player, move, - 1, - 1, captures);
		checkLine(board, player, move, 1, 1, captures);
		checkLine(board, player, move, - 1, 1, captures);
		checkLine(board, player, move, 1, - 1, captures);
	}
	
	static void checkLine(Board board, int player, Move move, int xdiff, int ydiff, int[] captures)
	{
		int x = move.x;
		int y = move.y;
		
		if(isOpp(board, player, x + xdiff, y + xdiff))
		{
			if(isOpp(board, player, x + xdiff + xdiff, y + ydiff + ydiff))
			{
				if(isPlayer(board, player, x + xdiff + xdiff + xdiff, y + ydiff + ydiff + ydiff))
				{
					captures[player - 1]++;
					board.squares[x + xdiff][y + ydiff] = 0;
					board.squares[x + xdiff + xdiff][y + ydiff + ydiff] = 0;
				}
			}
		}
	}
	
	static boolean isOpp(Board board, int player, int x, int y)
	{
		if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE)
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
	
	static boolean isPlayer(Board board, int player, int x, int y)
	{
		if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE)
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
	
	static boolean isCurrentPlayer(Board board, int player, int x, int y)
	{
		if (x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE)
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

		do 
		{
			move.x = rand.nextInt(Board.SIZE);
			move.y = rand.nextInt(Board.SIZE);
		}
		while (!context.isMoveValid(move) && tries++ < 500);
		
		if(tries == 500)
		{
			return null;
		}
		
		return move;
	}
	
	public static int getCaptureCount(MoveContext context, int player)
	{
		if(player < 0 || player >= context.game.numPlayers)
		{
			return -1;
		}
		
		return context.game.captures[player];
	}
}
