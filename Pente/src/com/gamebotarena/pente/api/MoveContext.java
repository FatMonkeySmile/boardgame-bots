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
