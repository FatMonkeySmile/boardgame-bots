/*
 * Copyright (C) 2011-2012 Boardgame Bots (http://code.google.com/p/boardgame-bots)
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

package com.gamebotarena.othello.core;

import java.util.ArrayList;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.othello.api.Board;
import com.gamebotarena.othello.api.Move;
import com.gamebotarena.othello.api.MoveContext;
import com.gamebotarena.othello.api.OthelloGameBot;

public class Game extends GameBase
{
	public static final String ID = "Othello";
	
	int numPlayers = 2;
	
	GameUi ui;
	ArrayList<BotInfo> botInfos;
	OthelloGameBot[] bots;
	
	int whiteTiles = 0;
	int blackTiles = 0;

	boolean draw;
	Board board;
	
	int turn;
	boolean gameOver;

	public void play()
	{
		//todo: reset all games...
		turn = 0;
		gameOver = false;
		draw = false;
		board = new Board();

		for(int i=0; i < numPlayers; i++)
		{
			bots[i].setup(i);
		}
		
		boolean canSwitch = true;
		
		Move lastMove = null;
		while (!gameOver && !ui.getStopEarly())
		{
			ui.showBoard();
			MoveContext context = new MoveContext(board, lastMove, 1);
			Move move = bots[0].getMove(context);
			move = playMove(1, move, context);
			lastMove = move;
			
			//todo: May not need this gameOver check
			if (!gameOver && !ui.getStopEarly())
			{
				ui.showBoard();
				context = new MoveContext(board, lastMove, 2);
				move = bots[1].getMove(context);
				move = playMove(2, move, context);
				lastMove = move;
			}
			
			turn++;
		}

		if(gameOver)
		{
			//game.ui.showBoard();
			countTiles();

			if(whiteTiles > blackTiles)
			{
				botInfos.get(0).won();
				botInfos.get(1).lost();
				ui.gameOver("Player 1 Wins");
			}
			else if(blackTiles > whiteTiles)
			{
				botInfos.get(0).lost();
				botInfos.get(1).won();
				ui.gameOver("Player 2 Wins");
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

	private void countTiles()
	{
		int[] tileCount = new int[3];

		for(int y=0; y < 8; y++)
		{
			for(int x=0; x < 8; x++)
			{
				tileCount[board.squares[x][y]]++;
			}
		}

		whiteTiles = tileCount[1];
		blackTiles = tileCount[2];
	}
	
	private Move playMove(int player, Move move, MoveContext context)
	{
		if (!OthelloUtils.isMoveValid(board, move, player))
		{
			//todo: report in dev mode.
			move = OthelloUtils.getRandomMove(context);
		}
		
		if (!(move == Move.Pass))
		{
			board.squares[move.x][move.y] = player;
			OthelloUtils.processMove(board, move, player, true);
		}
		
		checkGameOver();
		
		return move;
	}
	
	public void checkGameOver()
	{
		//todo: Is there any way to get to game over besides all squares full?
		for (int x = 0; x < 8; x++)
		{
			for (int y = 0; y < 8; y++)
			{
				if (board.squares[x][y] == 0)
				{
					Move move = new Move(x, y);
					if (OthelloUtils.isMoveValid(board, move, 1) || OthelloUtils.isMoveValid(board, move, 2))
					{
						return;
					}
				}
			}
		}
		
		gameOver = true;
	}

	public static void main(String[] args)
	{
		new Game().start(args);
	}

	public void start(final String[] args)
	{
		try
		{
			botInfos = Misc.getBots(this, args, 2, "com.gamebotarena.othello.api.RandomOthelloGameBot");
			
			bots = new OthelloGameBot[2];
	
			for (int i = 0; i < 2; i++)
			{
				bots[i] = (OthelloGameBot) botInfos.get(i).bot;
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
