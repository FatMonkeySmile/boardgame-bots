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
