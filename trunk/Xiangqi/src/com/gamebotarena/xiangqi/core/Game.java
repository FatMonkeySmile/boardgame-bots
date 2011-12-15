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

package com.gamebotarena.xiangqi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.xiangqi.api.Board;
import com.gamebotarena.xiangqi.api.Location;
import com.gamebotarena.xiangqi.api.Move;
import com.gamebotarena.xiangqi.api.MoveContext;
import com.gamebotarena.xiangqi.api.XiangqiGameBot;
import com.gamebotarena.xiangqi.api.XiangqiUtils;

public class Game extends GameBase
{
	public static String ID = "Xiangqi";

	GameUi ui;
	ArrayList<BotInfo> botInfos;
	XiangqiGameBot[] bots;

//	HashMap[] previousBoards = new HashMap[2];
//	
//	class BoardStateCount
//	{
//		public int count;
//	}

	boolean draw;
	boolean redWins;

	Board board;
	boolean gameOver = false;

	Random rand;
	int turn = 1;

	int movesWithoutProgress = 0;

	public void play()
	{
		gameOver = false;
		movesWithoutProgress = 0;
		turn = 1;
		board = new Board();
		draw = false;
		
		for(int i=0; i < 2; i++)
		{
			bots[i].setup(i);
		}
		
//		for(int i=0; i < 2; i++)
//		{
//			previousBoards[i] = new HashMap();
//		}
		
		while (!gameOver && !ui.getStopEarly())
		{
			for (int i = 0; i < 2; i++)
			{
				if(gameOver || ui.getStopEarly())
				{
					break;
				}

				ui.showBoard();

				MoveContext context = new MoveContext(board, i);
				Move move = bots[i].getMove(context);

				if (!XiangqiUtils.isMoveValid(context, move))
				{
					//todo:report
					move = XiangqiUtils.getRandomMove(context);
				}
				
				ui.move = move;
				ui.showBoard();
				
				//System.out.println("" + move.fromx + ", " + move.fromy + " : " + move.tox + ", " + move.toy + " ; " + context.getPlayerNum());
				char killedPiece = performMove(context, move);

				if(killedPiece != ' ')
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
				if (redWins)
				{
					botInfos.get(0).won();
					botInfos.get(1).lost();
					ui.gameOver("Red wins");
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
		if(movesWithoutProgress == 50)
		{
			draw = true;
			return true;
		}

		MoveContext context = new MoveContext(board, currentPlayer);
		MoveContext oppContext = XiangqiUtils.getOpponentsContext(context);
		
		if(XiangqiUtils.isAnyMoveValid(oppContext))
		{
			return false;
		}

		if (currentPlayer == 0)
		{
			redWins = true;
		}
		else
		{
			redWins = false;
		}

		return true;
	}
	
	public static char performMove(MoveContext context, Move move)
	{
		Board board = context.getBoard();
		char oldPiece = board.b[move.tox][move.toy];
		char piece = board.b[move.fromx][move.fromy];

		board.b[move.tox][move.toy] = piece;
		board.b[move.fromx][move.fromy] = ' ';

		return oldPiece;
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
	
			botInfos = Misc.getBots(this, args, 2, "com.gamebotarena.xiangqi.api.RandomXiangqiGameBot");
			
			bots = new XiangqiGameBot[2];
	
			for (int i = 0; i < 2; i++)
			{
				bots[i] = (XiangqiGameBot) botInfos.get(i).bot;
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
