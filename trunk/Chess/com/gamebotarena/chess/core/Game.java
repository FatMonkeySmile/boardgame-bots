/*
 * Copyright (C) 2011 Boardgame Bots (http://code.google.com/p/boardgame-bots)
 * 
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *   
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.gamebotarena.chess.core;

import java.util.*;

import com.gamebotarena.chess.api.*;
import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;

public class Game extends GameBase
{
	public static String ID = "Chess";

	GameUi ui;
	ArrayList<BotInfo> botInfos;
	ChessGameBot[] bots;

	class BoardStateCount
	{
		public int count;
	}

	boolean draw;
	boolean win;
	boolean whiteWins;

	HashMap[] previousBoards = new HashMap[2];
	
	Board board;
	boolean gameOver = false;
	boolean[][] castleChecks = new boolean[2][];

	Random rand;
	int turn = 1;

	Location enpassentPossibleTarget = null;

	int movesWithoutProgress = 0;

	public void play()
	{
		gameOver = false;
		movesWithoutProgress = 0;
		enpassentPossibleTarget = null;
		turn = 1;
		board = new Board();
		win = false;
		draw = false;
		
		for(int i=0; i < 2; i++)
		{
			bots[i].setup(i);
		}
		
		for(int i=0; i < 2; i++)
		{
			castleChecks[i] = new boolean[3];
		}
		for(int i=0; i < 2; i++)
		{
			previousBoards[i] = new HashMap();
		}
		
		while (!gameOver && !ui.getStopEarly())
		{
			for (int i = 0; i < 2; i++)
			{
				if(gameOver || ui.getStopEarly())
				{
					break;
				}

				ui.showBoard();

				MoveContext context = new MoveContext(board, i, castleChecks, enpassentPossibleTarget);
				Move move = bots[i].getMove(context);

				if(i == 1)
				{
					move = ChessUtils.swapMove(move);
				}

				if (!ChessUtils.isMoveValid(context, move))
				{
					//todo:report
					move = ChessUtils.getRandomMove(context);
				}
				
				ui.move = move;
				ui.showBoard();
				
				char piece = board.b[move.fromx][move.fromy];

				char killedPiece = ChessUtils.performMove(context, move);

				enpassentPossibleTarget = null;
				if(Character.toLowerCase(piece) == 'p')
				{
					if(Math.abs(move.fromy - move.toy) == 2)
					{
						enpassentPossibleTarget = new Location(move.tox, move.toy);
					}
				}

				int yCheck = 0;
				if(i == 1)
				{
					yCheck = 7;
				}

				if(move.fromx == 0 && move.fromy == yCheck)
				{
					castleChecks[i][1] = true;
				}

				if(move.fromx == 7 && move.fromy == yCheck)
				{
					castleChecks[i][2] = true;
				}

				if(Character.toLowerCase(piece) == 'k')
				{
					castleChecks[i][0] = true;
				}
				
				if((i == 0 && move.toy == 7 && piece == 'p') ||
				   (i == 1 && move.toy == 0 && piece == 'P'))
				{
					UpgradePawnMove upgradeMove = bots[i].getUpgradePawnMove(context);
					if(i == 1)
					{
						upgradeMove = new UpgradePawnMove(Character.toUpperCase(upgradeMove.piece));
					}
					if(!ChessUtils.isUpgradePawnMoveValid(context, upgradeMove))
					{
						//todo: report(?) at least in a developer mode
						upgradeMove = ChessUtils.getRandomUpgradePawnMove(context);
						if(i == 1)
						{
							upgradeMove = new UpgradePawnMove(Character.toUpperCase(upgradeMove.piece));
						}
					}

					board.b[move.tox][move.toy] = upgradeMove.piece;
				}

				if(killedPiece != ' ' || Character.toLowerCase(piece) == 'p')
				{
					movesWithoutProgress = 0;
				}
				else
				{
					movesWithoutProgress++;
				}
				
				ui.move = move;

				//pause();
				gameOver = checkGameOver(i);
				
				if(gameOver)
				{
					// If we are jumping to game over, show the last move end state first
					ui.showBoard();
				}
				
				turn++;
			}
		}
		
		ui.move = null;
		
		if(gameOver)
		{
			if (draw)
			{
				botInfos.get(0).draw();
				botInfos.get(1).draw();
				ui.gameOver("draw...");
			}
			else
			{
				if (whiteWins)
				{
					botInfos.get(0).won();
					botInfos.get(1).lost();
					ui.gameOver("White wins");
				}
				else
				{
					botInfos.get(1).won();
					botInfos.get(0).lost();
					ui.gameOver("Black wins");
				}
			}
		}
	}

	public boolean checkGameOver(int currentPlayer)
	{
		//todo: check the memory that this takes up
		char[] charBoard = new char[64];
		int at=0;
		for(int x=0; x < 8; x++)
		{
			for(int y=0; y < 8; y++)
			{
				charBoard[at++] = board.b[x][y];
			}
		}

		String boardState = new String(charBoard);
		BoardStateCount boardStateCount = (BoardStateCount) previousBoards[currentPlayer].get(boardState);
		if(boardStateCount == null)
		{
			boardStateCount = new BoardStateCount();
			previousBoards[currentPlayer].put(boardState, boardStateCount);
		}

		boardStateCount.count++;
		if(boardStateCount.count == 3)
		{
			draw = true;
			return true;
		}

		if(movesWithoutProgress == 50)
		{
			draw = true;
			return true;
		}

		boolean enoughPieces = false;
		for(int x=0; x < 8; x++)
		{
			for(int y=0; y < 8; y++)
			{
				char p = board.b[x][y];
				p = Character.toLowerCase(p);
				//todo: Better algorithm for finding if enough pieces.  Find out what are enough pieces.
				if(p == 'p' || p == 'r' || p == 'q' || p == 'b' || p == 'n')
				{
					enoughPieces = true;
					break;
				}
			}
		}
		

		if(!enoughPieces)
		{
			draw = true;
			return true;
		}

		MoveContext context = new MoveContext(board, currentPlayer, castleChecks, null);
		HashMap h = ChessUtils.getAllValidMoves(context);
		Set keys = h.keySet();
		Iterator iter = keys.iterator();
		
		boolean check = false;
		int kingX = - 1;
		int kingY = - 1;
		while(iter.hasNext() && !check)
		{
			Location from = (Location) iter.next();
			ArrayList a = (ArrayList) h.get(from);
			for (int j = 0; j < a.size(); j++)
			{
				Location to = (Location) a.get(j);
				if (Character.toLowerCase(board.b[to.x][to.y]) == 'k' && ChessUtils.isOpponentsPiece(context, board.b[to.x][to.y]))
				{
					kingX = to.x;
					kingY = to.y;
					check = true;
					break;
				}
			}
		}
		
		MoveContext oppContext = ChessUtils.getOpponentsContext(context);
		if (!check)
		{
			if (!ChessUtils.isAnyMoveValid(oppContext))
			{
				draw = true;
				return true;
			}
			return false;
		}
		
		if(ChessUtils.isAnyMoveValid(oppContext))
		{
			return false;
		}

		win = true;
		if (currentPlayer == 0)
		{
			whiteWins = true;
		}
		else
		{
			whiteWins = false;
		}

		return true;
	}
	
	boolean checkKingKilled()
	{
		boolean whiteKing = false;
		boolean blackKing = false;
		
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				char p = board.b[x][y];
				
				if (p == 'K')
				{
					blackKing = true;
					if (whiteKing)
					{
						return false;
					}
				}
				else if (p == 'k')
				{
					whiteKing = true;
					if (blackKing)
					{
						return false;
					}
				}
			}
		}
		
		win = true;
		if (whiteKing)
		{
			whiteWins = true;
		}
		else
		{
			whiteWins = false;
		}
		
		return true;
	}
	
	public static void main(String[] args)
	{
		new Game().start(args);
	}

	public void start(final String[] args)
	{
		try
		{
			rand = new Random();
			board = new Board();
	
			botInfos = Misc.getBots(this, args, 2, "com.gamebotarena.chess.api.RandomChessGameBot");
			
			bots = new ChessGameBot[2];
	
			for (int i = 0; i < 2; i++)
			{
				bots[i] = (ChessGameBot) botInfos.get(i).bot;
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
