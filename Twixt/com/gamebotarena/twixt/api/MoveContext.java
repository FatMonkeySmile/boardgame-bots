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
