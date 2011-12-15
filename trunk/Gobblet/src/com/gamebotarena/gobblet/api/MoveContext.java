package com.gamebotarena.gobblet.api;

import com.gamebotarena.gobblet.core.Game;

public class MoveContext
{
	Board board;
	int player;
	Move lastMove;
	private Game game;

	public MoveContext(Game game, Board board, int player, Move lastMove)
	{
		this.game = game;
		this.board = board;
		this.player = player;
		this.lastMove = lastMove;
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
	
	public Move getLastMove()
	{
		return lastMove;
	}

	public Move getRandomMove()
	{
		return game.getRandomMove(this);
	}
}
