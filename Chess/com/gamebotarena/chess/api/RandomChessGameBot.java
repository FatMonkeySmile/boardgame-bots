package com.gamebotarena.chess.api;

public class RandomChessGameBot extends ChessGameBot
{
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
	
	public UpgradePawnMove getUpgradePawnMove(MoveContext context)
	{
		return ChessUtils.getRandomUpgradePawnMove(context);
	}
}
