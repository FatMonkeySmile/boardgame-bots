package com.gamebotarena.chess.api;

public class MoveContext
{
	Board board;
	int player;
	boolean[][] castleChecks;
	Location enpassentPossibleTarget;

	Board swappedBoard;
	// Not sure if they should have access to this or not...
	//todo: Give them last move
	Location swappedEnpassentPossibleTarget;
	
	public MoveContext(Board board, int player, boolean[][] castleChecks, Location enpassentPossibleTarget)
	{
		this.board = board;
		this.player = player;
		this.castleChecks = castleChecks;
		this.enpassentPossibleTarget = enpassentPossibleTarget;

		if(player == 1)
		{
			swappedBoard = new Board();
			for(int x=0; x < 8; x++)
			{
				for(int y=0; y < 8; y++)
				{
					char p = board.b[x][y];
					if(p != ' ')
					{
						if(Character.isLowerCase(p))
						{
							p = Character.toUpperCase(p);
						}
						else
						{
							p = Character.toLowerCase(p);
						}
					}
					swappedBoard.b[7 - x][7 - y] = p;
				}
			}
			if(enpassentPossibleTarget != null)
			{
				swappedEnpassentPossibleTarget = new Location(7 - enpassentPossibleTarget.x, 7 - enpassentPossibleTarget.y);
			}
		}
	}
	
	public Board getBoard()
	{
		if(player == 1)
		{
			return swappedBoard;
		}
		else
		{
			return board;
		}
	}

	public int getPlayerNum()
	{
		return player;
	}
	
	public boolean[][] getCastleChecks()
	{
		return castleChecks;
	}
	
	public boolean isMoveValid(Move move)
	{
		if(player == 1)
		{
			move = ChessUtils.swapMove(move);
		}
		return ChessUtils.isMoveValid(this, move);
	}

	public Move getRandomMove()
	{
		Move move = ChessUtils.getRandomMove(this);
		if(player == 1)
		{
			move = ChessUtils.swapMove(move);
		}

		return move;
	}
}
