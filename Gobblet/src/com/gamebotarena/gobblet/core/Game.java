package com.gamebotarena.gobblet.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.gobblet.api.Board;
import com.gamebotarena.gobblet.api.ExternalMove;
import com.gamebotarena.gobblet.api.GobbletGameBot;
import com.gamebotarena.gobblet.api.InternalMove;
import com.gamebotarena.gobblet.api.Move;
import com.gamebotarena.gobblet.api.MoveContext;
import com.gamebotarena.gobblet.api.Piece;
import com.gamebotarena.gobblet.api.Square;

public class Game extends GameBase
{
	public static final String ID = "Gobblet";
	
	int numPlayers = 2;
	
	GameUi ui;
	ArrayList<BotInfo> botInfos;
	GobbletGameBot[] bots;

	boolean draw;
	boolean blackWins;
	
	Board board;
	
	int turn;
	boolean gameOver;
	
	Random rand;

	public void play()
	{
		//todo: reset all games...
		rand = new Random();
		turn = 1;
		gameOver = false;
		draw = false;
		board = new Board();
		ui.clearMove();

		for(int i=0; i < numPlayers; i++)
		{
			bots[i].setup(i);
		}
		
		Move lastMove = null;
		while (!gameOver && !ui.getStopEarly())
		{
			ui.showBoard();
			
			//todo: pass last move...
			MoveContext context = new MoveContext(this, board, 0, lastMove);
			Move move = bots[0].getMove(context);
			move = playMove(move, context);
			lastMove = move;

			checkGameOver(0);
			
			//todo: May not need this gameOver check
			if (!gameOver && !ui.getStopEarly())
			{
				ui.showBoard();
				
				context = new MoveContext(this, board, 1, lastMove);
				move = bots[1].getMove(context);
				move = playMove(move, context);
				lastMove = move;

				checkGameOver(1);
			}
			
			turn++;
		}

		if(gameOver)
		{
			ui.clearMove();
			ui.showBoard();
			
			if(blackWins && !draw)
			{
				botInfos.get(1).won();
				botInfos.get(0).lost();
				ui.gameOver("Player 2 Wins");
			}
			else if(!draw)
			{
				botInfos.get(1).lost();
				botInfos.get(0).won();
				ui.gameOver("Player 1 Wins");
			}
			else
			{
				botInfos.get(0).draw();
				botInfos.get(1).draw();
				ui.gameOver("draw...");
			}
		}
		else
		{
			ui.gameOver("");
		}
	}

	private Move playMove(Move move, MoveContext context)
	{
		if (!isMoveValid(context, move))
		{
			//todo: report in dev mode.
			D.out("Move invalid");
			move = getRandomMove(context);
		}
		
		if(move instanceof InternalMove)
		{
			InternalMove iMove = (InternalMove) move;
			ui.setInternalMove(new Point(iMove.fromx, iMove.fromy), new Point(iMove.tox, iMove.toy));
			
			//System.out.println("" + context.getPlayerNum() + ": internal move: " + iMove.fromx + ", " + iMove.fromy + " - " + iMove.tox + ", " + iMove.toy);
		}
		else if(move instanceof ExternalMove)
		{
			ExternalMove eMove = (ExternalMove) move;
			ui.setExternalMove(eMove.stack, new Point(eMove.tox, eMove.toy), context.getPlayerNum());
			//System.out.println("" + context.getPlayerNum() + ": external move: " + eMove.stack + " - " + eMove.tox + ", " + eMove.toy);
		}
		else
		{
			//todo: ever get here?
			ui.clearMove();
		}
		
		ui.showBoard();
		
		if(move instanceof InternalMove)
		{
			InternalMove iMove = (InternalMove) move;
			Piece p = board.squares[iMove.fromx][iMove.fromy].pop();
			board.squares[iMove.tox][iMove.toy].play(p);
		}
		else if(move instanceof ExternalMove)
		{
			ExternalMove eMove = (ExternalMove) move;
			int player = context.getPlayerNum();
			Piece p = board.getUnPlayed(player)[eMove.stack].pop();
			board.squares[eMove.tox][eMove.toy].play(p);
		}
		
		return move;
	}
	
	public Move getRandomMove(MoveContext context)
	{
		ArrayList<Move> validMoves = new ArrayList<Move>();
		for(int x1=0; x1 < 4; x1++)
		{
			for(int y1=0; y1 < 4; y1++)
			{
				for(int x2=0; x2 < 4; x2++)
				{
					for(int y2=0; y2 < 4; y2++)
					{
						Move move = new InternalMove(x1, y1, x2, y2);
						if(isMoveValid(context, move))
						{
							validMoves.add(move);
						}
					}
				}
			}
		}
		
		for(int x=0; x < 4; x++)
		{
			for(int y=0; y < 4; y++)
			{
				for(int stack=0; stack < 3; stack++)
				{
					Move move = new ExternalMove(stack, x, y);
					if(isMoveValid(context, move))
					{
						validMoves.add(move);
					}
				}
			}
		}
		
		if(validMoves.size() == 0)
		{
			return null;
		}
		
		return validMoves.get(rand.nextInt(validMoves.size()));
	}
	
	public boolean isMoveValid(MoveContext context, Move move)
	{
		if(move == null)
		{
			return false;
		}
		
		if(move instanceof InternalMove)
		{
			InternalMove iMove = (InternalMove) move;
			if(iMove.fromx == iMove.tox && iMove.fromy == iMove.toy)
			{
				return false;
			}
			
			if(iMove.fromx < 0 || iMove.fromx >= 4 || iMove.fromy < 0 || iMove.fromy >= 4)
			{
				return false;
			}
			
			if(iMove.tox < 0 || iMove.tox >= 4 || iMove.toy < 0 || iMove.toy >= 4)
			{
				return false;
			}
			
			Piece piece = board.squares[iMove.fromx][iMove.fromy].peek(); 
			if(piece == null || piece.getPlayer() != context.getPlayerNum())
			{
				return false;
			}
			
			Piece toPiece = board.squares[iMove.tox][iMove.toy].peek();
			if(toPiece != null && toPiece.getSize() >= piece.getSize())
			{
				return false;
			}
			
			return true;
		}
		
		if(move instanceof ExternalMove)
		{
			ExternalMove eMove = (ExternalMove) move;
			if(eMove.tox < 0 || eMove.tox >= 4 || eMove.toy < 0 || eMove.toy >= 4)
			{
				return false;
			}
			
			Piece toPiece = board.squares[eMove.tox][eMove.toy].peek();
			if(toPiece != null)
			{
				//todo: handle 3 in a row ok...
				return false;
			}
			
			if(eMove.stack < 0 || eMove.stack >= 3)
			{
				return false;
			}
			
			if(board.getUnPlayed(context.getPlayerNum())[eMove.stack].peek() == null)
			{
				return false;
			}
			
			return true;
		}

		
		return false;
	}
	
	public void checkGameOver(int playerNum)
	{
		for(int i=0; i < 2; i++)
		{
			int check = playerNum;
			if(i == 0)
			{
				if(playerNum == 0)
				{
					check = 1;
				}
				else
				{
					check = 0;
				}
			}
			
			boolean won = false;

			for(int x=0; x < 4; x++)
			{
				won = true;
				for(int y=0; y < 4; y++)
				{
					Piece s = board.squares[x][y].peek();
					if(s == null || s.getPlayer() != check)
					{
						won = false;
						break;
					}
				}
				
				if(won)
				{
					break;
				}
			}
			
			if(!won)
			{
				for(int y=0; y < 4; y++)
				{
					won = true;
					for(int x=0; x < 4; x++)
					{
						Piece s = board.squares[x][y].peek();
						if(s == null || s.getPlayer() != check)
						{
							won = false;
							break;
						}
					}
					
					if(won)
					{
						break;
					}
				}
			}
			
			if(!won)
			{
				won = true;
				for(int xy=0; xy < 4; xy++)
				{
					Piece s = board.squares[xy][xy].peek();
					if(s == null || s.getPlayer() != check)
					{
						won = false;
						break;
					}
				}
			}
			
			if(!won)
			{
				won = true;
				for(int x=0; x < 4; x++)
				{
					int y = 3 - x;
					Piece s = board.squares[x][y].peek();
					if(s == null || s.getPlayer() != check)
					{
						won = false;
						break;
					}
				}
			}
			
			if(won)
			{
				gameOver = true;
				if(check == 1)
				{
					blackWins = true;
				}
				else
				{
					blackWins = false;
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Game().start(args);
	}

	public void start(final String[] args)
	{
		try
		{
			botInfos = Misc.getBots(this, args, 2, "com.gamebotarena.gobblet.api.RandomGobbletGameBot");
			bots = new GobbletGameBot[2];
	
			for (int i = 0; i < 2; i++)
			{
				bots[i] = (GobbletGameBot) botInfos.get(i).bot;
			}
			
			ui = new GameUi(this);
			
			setGameUiBase(ui);
			runGames(args);
		}
		catch(Exception e)
		{
			D.out(e);
		}
	}
}
