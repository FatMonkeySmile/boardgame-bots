package com.gamebotarena.xiangqi.api;


public class MoveContext
{
	Board board;
	int player;
	
	public MoveContext(Board board, int player)
	{
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
		return XiangqiUtils.isMoveValid(this, move);
	}

	public Move getRandomMove()
	{
		Move move = XiangqiUtils.getRandomMove(this);

		return move;
	}
}
