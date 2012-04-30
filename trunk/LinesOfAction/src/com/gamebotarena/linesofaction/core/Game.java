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

package com.gamebotarena.linesofaction.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.linesofaction.api.Board;
import com.gamebotarena.linesofaction.api.LinesOfActionGameBot;
import com.gamebotarena.linesofaction.api.Move;
import com.gamebotarena.linesofaction.api.MoveContext;

public class Game extends GameBase
{
	public static final String ID = "LinesOfAction";
	
	int numPlayers = 2;
	
	GameUi ui;
	ArrayList<BotInfo> botInfos;
	LinesOfActionGameBot[] bots;

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
			//todo: should this be setup(i + 1) ?
			bots[i].setup(i);
		}
		
		Move lastMove = null;
		while (!gameOver && !ui.getStopEarly())
		{
			ui.showBoard();
			
			//ui.clearMove();
			//ui.showBoard();
			
			//todo: pass last move...
			MoveContext context = new MoveContext(this, board, 1);
			Move move = bots[0].getMove(context);
			move = playMove(move, context);
			lastMove = move;

			checkGameOver(1);
			
			//todo: May not need this gameOver check
			if (!gameOver && !ui.getStopEarly())
			{
				ui.showBoard();
				
				//ui.clearMove();
				//ui.showBoard();
				
				context = new MoveContext(this, board, 2);
				move = bots[1].getMove(context);
				move = playMove(move, context);
				lastMove = move;

				checkGameOver(2);
			}
			
			turn++;
		}

		if(gameOver)
		{
			ui.clearMove();
			
			if(blackWins && !draw)
			{
				botInfos.get(0).won();
				botInfos.get(1).lost();
				ui.gameOver("Player 1 Wins");
			}
			else if(!draw)
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

	private Move playMove(Move move, MoveContext context)
	{
		if (!isMoveValid(context, move))
		{
			//todo: report in dev mode.
			D.out("Move invalid");
			move = getRandomMove(context);
		}
		
		if(move != Move.Pass)
		{
			ui.setMove(new Point(move.fromx, move.fromy), new Point(move.tox, move.toy));
		}
		else
		{
			ui.clearMove();
		}
		
		ui.showBoard();
		
		if (!(move == Move.Pass))
		{
			board.b[move.fromx][move.fromy] = 0;
			board.b[move.tox][move.toy] = context.getPlayerNum();
		}
		
		return move;
	}
	
	public Move getRandomMove(MoveContext context)
	{
		//todo: optimize

		ArrayList<Move> validMoves = new ArrayList<Move>();
		for(int x1=0; x1 < 8; x1++)
		{
			for(int y1=0; y1 < 8; y1++)
			{
				for(int x2=0; x2 < 8; x2++)
				{
					for(int y2=0; y2 < 8; y2++)
					{
						Move move = new Move(x1, y1, x2, y2);
						if(isMoveValid(context, move))
						{
							validMoves.add(move);
						}
					}
				}
			}
		}
		
		if(validMoves.isEmpty())
		{
			return Move.Pass;
		}
		
		return validMoves.get(rand.nextInt(validMoves.size()));
	}
	
	public boolean isMoveValid(MoveContext context, Move move)
	{
		//xtodo: can pass if not forced?
		if(move == Move.Pass)
		{
			return true;
		}
		
		if(move.fromx == move.tox && move.fromy == move.toy)
		{
			return false;
		}

		int[][] b = board.b;
		
		//isValid also checks that square is occupied by player's piece
		if(!isValid(move.fromx, move.fromy, context.getPlayerNum()))
		{
			return false;
		}

		if(move.tox < 0 || move.tox >= 8 || move.toy < 0 || move.toy >= 8)
		{
			return false;
		}
		
		if(b[move.tox][move.toy] == context.getPlayerNum())
		{
			return false;
		}
		
		int xDiff = move.tox - move.fromx;
		int yDiff = move.toy - move.fromy;
		
		if(xDiff != 0 && yDiff != 0)
		{
			if(Math.abs(xDiff) != Math.abs(yDiff))
			{
				return false;
			}
		}
		
		xDiff = (int) Math.signum(xDiff);
		yDiff = (int) Math.signum(yDiff);
		
		int opp = 2;
		if(context.getPlayerNum() == 2)
		{
			opp = 1;
		}
		int atX = move.fromx + xDiff;
		int atY = move.fromy + yDiff;
		
		while(atX != move.tox || atY != move.toy)
		{
			if(b[atX][atY] == opp)
			{
				return false;
			}

			atX += xDiff;
			atY += yDiff;
		}
		
		atX = move.fromx;
		atY = move.fromy;

		int count = 0;
		
		do
		{
			//System.out.println("checking: " + atX + ", " + atY + ":" + xDiff + "," + yDiff + ";" + move.fromx + "," + move.fromy);
			if(b[atX][atY] != 0)
			{
				count++;
			}
			
			if(atX + xDiff < 0)
			{
				if(yDiff == 0)
				{
					atX = 7;
				}
				else
				{
					if(yDiff > 0)
					{
						int oldAtX = atX;
						atX = atY;
						atY = oldAtX;
					}
					else
					{
						atX = 7 - atY;
						atY = 7;
					}
				}
			}
			else if(atY + yDiff < 0)
			{
				if(xDiff == 0)
				{
					atY = 7;
				}
				else
				{
					if(xDiff > 0)
					{
						int oldAtX = atX;
						atX = atY;
						atY = oldAtX;
					}
					else
					{
						atY = 7 - atX;
						atX = 7;
					}
				}
			}
			else if(atX + xDiff >= 8)
			{
				if(yDiff == 0)
				{
					atX = 0;
				}
				else
				{
					if(yDiff > 0)
					{
						atX = 7 - atY;
						atY = 0;
					}
					else
					{
						int oldAtX = atX;
						atX = atY;
						atY = oldAtX;
					}
				}
			}
			else if(atY + yDiff >= 8)
			{
				if(xDiff == 0)
				{
					atY = 0;
				}
				else
				{
					if(xDiff > 0)
					{
						atY = 7 - atX;
						atX = 0;
					}
					else
					{
						int oldAtX = atX;
						atX = atY;
						atY = oldAtX;
					}
				}
			}
			else
			{
				atX += xDiff;
				atY += yDiff;
			}
		}
		while(atX != move.fromx || atY != move.fromy);
		
		int distX = Math.abs(move.tox - move.fromx);
		int distY = Math.abs(move.toy - move.fromy);
		int dist = Math.max(distX, distY);
		
		if(dist != count)
		{
			return false;
		}
		
		return true;
	}
	
	public void checkGameOver(int playerNum)
	{
		boolean[] conn = new boolean[3];
		conn[1] = checkPlayerConnected(1);
		conn[2] = checkPlayerConnected(2);
		
		if(conn[1] || conn[2])
		{
			gameOver = true;
		}
		
		if(conn[1] && conn[2])
		{
			if(playerNum == 1)
			{
				blackWins = true;
			}
			else
			{
				blackWins = false;
			}
		}
		else if(conn[1])
		{
			blackWins = true;
		}
		else
		{
			blackWins = false;
		}
	}
	
	public boolean checkPlayerConnected(int playerNum)
	{
		int[][] b = board.b;
		boolean[][] connected = new boolean[8][8];
		
		int originX = -1;
		int originY = -1;
		
		loop: 
		for(int x=0; x < 8; x++)
		{
			for(int y=0; y < 8; y++)
			{
				if(b[x][y] == playerNum)
				{
					originX = x;
					originY = y;
					break loop;
				}
			}
		}
		
	    checkNeighbors(originX, originY, playerNum, connected);

	    boolean allConnected = true;
	    
		loop2: 
		for(int x=0; x < 8; x++)
		{
			for(int y=0; y < 8; y++)
			{
				if(b[x][y] == playerNum)
				{
					if(!connected[x][y])
					{
						allConnected = false;
						break loop2;
					}
				}
			}
		}

	    return allConnected;
	}
	
	public void checkNeighbors(int x, int y, int playerNum, boolean[][] connected)
	{
		if(isValid(x, y, playerNum) && connected[x][y])
		{
			// already checked, just return right away
			return;
		}
		
		connected[x][y] = true;
		
		if(isValid(x - 1, y, playerNum))
		{
			checkNeighbors(x - 1, y, playerNum, connected);
		}
		if(isValid(x + 1, y, playerNum))
		{
			checkNeighbors(x + 1, y, playerNum, connected);
		}
		if(isValid(x, y - 1, playerNum))
		{
			checkNeighbors(x, y - 1, playerNum, connected);
		}
		if(isValid(x, y + 1, playerNum))
		{
			checkNeighbors(x, y + 1, playerNum, connected);
		}
		if(isValid(x - 1, y - 1, playerNum))
		{
			checkNeighbors(x - 1, y - 1, playerNum, connected);
		}
		if(isValid(x - 1, y + 1, playerNum))
		{
			checkNeighbors(x - 1, y + 1, playerNum, connected);
		}
		if(isValid(x + 1, y - 1, playerNum))
		{
			checkNeighbors(x + 1, y - 1, playerNum, connected);
		}
		if(isValid(x + 1, y + 1, playerNum))
		{
			checkNeighbors(x + 1, y + 1, playerNum, connected);
		}
	}
	
	public boolean isValid(int x, int y, int playerNum)
	{
		if(x >=0 && x < 8 && y >= 0 && y < 8)
		{
			if(board.b[x][y] == playerNum)
			{
				return true;
			}
		}
		
		return false;
	}

	public static void main(String[] args)
	{
		new Game().start(args);
	}

	public void start(final String[] args)
	{
		try
		{
			botInfos = Misc.getBots(this, args, 2, "com.gamebotarena.linesofaction.api.RandomLinesOfActionGameBot");
			
			bots = new LinesOfActionGameBot[2];
	
			for (int i = 0; i < 2; i++)
			{
				bots[i] = (LinesOfActionGameBot) botInfos.get(i).bot;
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
