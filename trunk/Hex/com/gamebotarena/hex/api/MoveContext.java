package com.gamebotarena.hex.api;

import com.gamebotarena.hex.core.Game;

public class MoveContext 
{
	int playerNum;
	int turn;
	Board board;
	Move lastMove;
	
	public MoveContext(int playerNum, int turn, Board board, Move lastMove)
	{
		this.playerNum = playerNum;
		this.turn = turn;
		this.board = board;
		this.lastMove = lastMove;
	}
	
	public int getPlayerNum()
	{
		return playerNum;
	}
	
	public int getTurn()
	{
		return turn;
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	public Move getLastMove()
	{
		return lastMove;
	}
	
	public Move getRandomMove()
	{
		return Game.getRandomMove(this);
	}
	
	public boolean isMoveValid(Move move)
	{
		return Game.isMoveValid(this, move);
	}
}
