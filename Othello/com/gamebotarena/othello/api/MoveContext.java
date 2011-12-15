package com.gamebotarena.othello.api;

import com.gamebotarena.othello.core.OthelloUtils;

public class MoveContext
{
	Board board;
	Move oppMove;
	int playerNum;
	
	public MoveContext(Board board, Move oppMove, int playerNum)
	{
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
		return OthelloUtils.isMoveValid(board, move, playerNum);
	}

	public Move getRandomMove()
	{
		return OthelloUtils.getRandomMove(this);
	}
}
