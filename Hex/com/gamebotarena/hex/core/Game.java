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

package com.gamebotarena.hex.core;

import java.util.ArrayList;
import java.util.Random;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.hex.api.Board;
import com.gamebotarena.hex.api.HexGameBot;
import com.gamebotarena.hex.api.Move;
import com.gamebotarena.hex.api.MoveContext;

public class Game extends GameBase
{
	public static final String ID = "Hex";
	
	Board board;
	boolean redWins;
	boolean gameOver = false;
	int turn;
	Move lastMove;
	
	static Random rand = Misc.rand;

	GameUi ui;
	ArrayList<BotInfo> botInfos;
	
	HexGameBot[] bots;
	
	public static void main(String[] args)
	{
		new Game().start(args);
	}

	public void start(final String[] args)
	{
		try
		{
			botInfos = Misc.getBots(this, args, 2, "com.gamebotarena.hex.api.RandomHexGameBot");
			
			bots = new HexGameBot[3];
	
			for (int i = 1; i <= 2; i++)
			{
				bots[i] = (HexGameBot) botInfos.get(i - 1).bot;
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

	public void play()
	{
		gameOver = false;
		turn = 0;
		board = new Board();
		lastMove = null;
		
		for(int i=1; i <= 2; i++)
		{
			bots[i].setup(i);
		}

		while(!ui.getStopEarly() && !gameOver)
		{
			turn++;
			for(int i = 1; i <= 2; i++)
			{
				ui.showBoard();
				MoveContext context = new MoveContext(i, turn, board, lastMove);
				Move move = bots[i].getMove(context);
				
				if(!isMoveValid(context, move))
				{
					move = getRandomMove(context);
					//todo: report...
					D.out("P" + (i) + ":Move invalid...");
				}

				boolean swapped = false;
				if(move.getX() == -1 && move.getY() == -1)
				{
					swapped = true;
					board.b[lastMove.getX()][lastMove.getY()] = 0;
					board.c[lastMove.getX()][lastMove.getY()] = false;
					
					int x = lastMove.getY();
					int y = lastMove.getX();
					
					move = new Move(x, y);
				}
				
				lastMove = move;
				
				if(swapped)
				{
					lastMove = Move.SwapMove;
				}
				
				int x = move.getX();
				int y = move.getY();
				
				board.b[x][y] = i;
				checkConnection(x, y);
					
				checkGameOver();
				
				if(gameOver || ui.getStopEarly())
				{
					break;
				}
			}
		}
		
		if(gameOver)
		{
			board.clearConnections();
	
			if(redWins)
			{
				for(int i=0; i < Board.SIZE; i++)
				{
					if(board.b[i][0] == 1)
					{
						gameOverCheckConnection(i, 0, board.c, true);
					}
					
					if(board.b[i][Board.SIZE - 1] == 1)
					{
						gameOverCheckConnection(i, Board.SIZE - 1, board.c2, false);
					}
				}
				
				int startX = -1;
				int startY = -1;
				for(int i=0; i < Board.SIZE; i++)
				{
					if(board.c2[i][0])
					{
						startX = i;
						startY = 0;
					}
				}
				
				for(int i=0; i < Board.SIZE; i++)
				{
					for(int j=0; j < Board.SIZE; j++)
					{
						if(i == startX && j == startY)
						{
							continue;
						}
						
						if(board.c[i][j] && board.c2[i][j])
						{
							board.b[i][j] = 0;
							board.clearCheck();
							if(isConnectedToEnd(startX, startY))
							{
								board.c[i][j] = false;
							}
							board.b[i][j] = 1;
							
							if(j == 0 && board.c[i][j])
							{
								board.b[startX][startY] = 0;
								board.clearCheck();
								boolean newStart = false;
								if(isConnectedToEnd(i, j))
								{
									newStart = true;
									board.c[startX][startY] = false;
								}
								board.b[startX][startY] = 1;
								if(newStart)
								{
									startX = i;
									startY = j;
								}
							}
						}
					}
				}
				/*
	
				for(int i=startX; i > 1; i--)
				{
					if(board.c2[i][0])
					{
						board.b[i][0] = 0;
						board.clearCheck();
						if(board.c2[i-1][0] && isConnectedToEnd(i-1, 0))
						{
							startX = i-1;
							board.c2[i][0] = false;
						}
						board.b[i][0] = 1;
					}
				}
	
				for(int i=0; i < Board.SIZE; i++)
				{
					for(int j=0; j < Board.SIZE; j++)
					{
						if(i == startX && j == startY)
						{
							continue;
						}
						
						if(board.c[i][j] && board.c2[i][j])
						{
							board.b[i][j] = 0;
							board.clearCheck();
							if(isConnectedToEnd(startX, startY))
							{
								board.c[i][j] = false;
							}
							board.b[i][j] = 1;
						}
					}
				}
				*/
			}
			else
			{
				for(int i=0; i < Board.SIZE; i++)
				{
					if(board.b[0][i] == 2)
					{
						gameOverCheckConnection(0, i, board.c, true);
					}
					
					if(board.b[Board.SIZE - 1][i] == 2)
					{
						gameOverCheckConnection(Board.SIZE - 1, i, board.c2, false);
					}
				}			
				int startX = -1;
				int startY = -1;
				for(int i=0; i < Board.SIZE; i++)
				{
					if(board.c2[0][i])
					{
						startX = 0;
						startY = i;
					}
				}
				
				for(int i=0; i < Board.SIZE; i++)
				{
					for(int j=0; j < Board.SIZE; j++)
					{
						if(i == startX && j == startY)
						{
							continue;
						}
						
						if(board.c[i][j] && board.c2[i][j])
						{
							board.b[i][j] = 0;
							board.clearCheck();
							if(isConnectedToEnd(startX, startY))
							{
								board.c[i][j] = false;
							}
							board.b[i][j] = 2;
						}
						
						if(i == 0 && board.c[i][j])
						{
							board.b[startX][startY] = 0;
							board.clearCheck();
							boolean newStart = false;
							if(isConnectedToEnd(i, j))
							{
								newStart = true;
								board.c[startX][startY] = false;
							}
							board.b[startX][startY] = 2;
							if(newStart)
							{
								startX = i;
								startY = j;
							}
						}
					}
				}
			}
			
			if(redWins)
			{
				botInfos.get(0).won();
				botInfos.get(1).lost();
				ui.gameOver("Red wins");
			}
			else
			{
				botInfos.get(1).won();
				botInfos.get(0).lost();
				ui.gameOver("Blue wins");
			}
		}
	}
	
	private void checkGameOver()
	{
		int max =  Board.SIZE - 1;
		for(int i=0; i < Board.SIZE; i++)
		{
			if(board.c[i][max] && board.b[i][max] == 1)
			{
				redWins = true;
				gameOver = true;
			}
			
			if(board.c[max][i] && board.b[max][i] == 2)
			{
				redWins = false;
				gameOver = true;
			}
		}
	}
	
	private boolean isOnBoard(int x, int y)
	{
		if(x < 0 || y < 0 || x >= Board.SIZE || y >= Board.SIZE)
		{
			return false;
		}
		
		return true;
	}
	
	private void checkConnection(int x, int y)
	{
		int playerNum = board.b[x][y];
		boolean connected = false;
		if(playerNum == 1 && y == 0)
		{
			connected = true;
		}
		if(playerNum == 2 && x == 0)
		{
			connected = true;
		}
		int x1;
		int y1;
		
		x1 = x + 1;
		y1 = y - 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				connected = true;
			}
		}
		
		x1 = x + 1;
		y1 = y;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				connected = true;
			}
		}

		x1 = x;
		y1 = y + 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				connected = true;
			}
		}

		x1 = x - 1;
		y1 = y + 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				connected = true;
			}
		}

		x1 = x - 1;
		y1 = y;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				connected = true;
			}
		}

		x1 = x;
		y1 = y - 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				connected = true;
			}
		}

		if(connected)
		{
			connect(playerNum, x, y);
		}
	}
	
	private void connect(int playerNum, int x, int y)
	{
		if(!isOnBoard(x, y))
		{
			return;
		}
		
		if(board.c[x][y])
		{
			return;
		}

		if(board.b[x][y] != playerNum)
		{
			return;
		}
		
		board.c[x][y] = true;

		int x1;
		int y1;
		
		x1 = x + 1;
		y1 = y - 1;
		connect(playerNum, x1, y1);
		
		x1 = x + 1;
		y1 = y;
		connect(playerNum, x1, y1);

		x1 = x;
		y1 = y + 1;
		connect(playerNum, x1, y1);

		x1 = x - 1;
		y1 = y + 1;
		connect(playerNum, x1, y1);

		x1 = x - 1;
		y1 = y;
		connect(playerNum, x1, y1);

		x1 = x;
		y1 = y - 1;
		connect(playerNum, x1, y1);
	}
	
	public static boolean isMoveValid(MoveContext context, Move move)
	{
		if(move == null)
		{
			return false;
		}
		
		int x = move.getX();
		int y = move.getY();
		
		if(context.getTurn() == 1 && context.getPlayerNum() == 2)
		{
			if(x == -1 && y == -1)
			{
				return true;
			}
		}
		
		if(x < 0 || x >= Board.SIZE || y < 0 || y >= Board.SIZE)
		{
			return false;
		}
		
		if(context.getBoard().b[x][y] != 0)
		{
			return false;
		}
		
		return true;
	}

	public static Move getRandomMove(MoveContext context) 
	{
		if(context.getTurn() == 1 && context.getPlayerNum() == 2 && Math.random() < 0.5)
		{
			return Move.SwapMove;
		}
		
		Board board = context.getBoard();
		ArrayList<Move> validMoves = new ArrayList<Move>();
		for(int x=0; x < Board.SIZE; x++)
		{
			for(int y=0; y < Board.SIZE; y++)
			{
				if(board.b[x][y] == 0)
				{
					validMoves.add(new Move(x, y));
				}
			}
		}
		
		return validMoves.get(rand.nextInt(validMoves.size()));
	}
	
	private boolean isConnectedToEnd(int x, int y)
	{
		if(board.check[x][y])
		{
			return false;
		}
		board.check[x][y] = true;
		int playerNum = board.b[x][y];
		if(playerNum == 0)
		{
			return false;
		}
		
		if(playerNum == 1)
		{
			if(y == Board.SIZE - 1)
			{
				return true;
			}
		}
		else if(playerNum == 2)
		{
			if(x == Board.SIZE - 1)
			{
				return true;
			}
		}
		
		int x1;
		int y1;
		
		x1 = x + 1;
		y1 = y - 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				boolean b = isConnectedToEnd(x1, y1);
				if(b)
				{
					return true;
				}
			}
		}
		
		x1 = x + 1;
		y1 = y;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				boolean b = isConnectedToEnd(x1, y1);
				if(b)
				{
					return true;
				}
			}
		}

		x1 = x;
		y1 = y + 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				boolean b = isConnectedToEnd(x1, y1);
				if(b)
				{
					return true;
				}
			}
		}

		x1 = x - 1;
		y1 = y + 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				boolean b = isConnectedToEnd(x1, y1);
				if(b)
				{
					return true;
				}
			}
		}

		x1 = x - 1;
		y1 = y;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				boolean b = isConnectedToEnd(x1, y1);
				if(b)
				{
					return true;
				}
			}
		}

		x1 = x;
		y1 = y - 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && board.c[x1][y1])
			{
				boolean b = isConnectedToEnd(x1, y1);
				if(b)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void gameOverCheckConnection(int x, int y, boolean[][] c, boolean start)
	{
		int playerNum = board.b[x][y];
		boolean connected = false;
		
		if(start)
		{
			if(playerNum == 1 && y == 0)
			{
				connected = true;
			}
			if(playerNum == 2 && x == 0)
			{
				connected = true;
			}
		}
		else
		{
			if(playerNum == 1 && y == Board.SIZE - 1)
			{
				connected = true;
			}
			if(playerNum == 2 && x == Board.SIZE - 1)
			{
				connected = true;
			}			
		}
		int x1;
		int y1;
		
		x1 = x + 1;
		y1 = y - 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && c[x1][y1])
			{
				connected = true;
			}
		}
		
		x1 = x + 1;
		y1 = y;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && c[x1][y1])
			{
				connected = true;
			}
		}

		x1 = x;
		y1 = y + 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && c[x1][y1])
			{
				connected = true;
			}
		}

		x1 = x - 1;
		y1 = y + 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && c[x1][y1])
			{
				connected = true;
			}
		}

		x1 = x - 1;
		y1 = y;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && c[x1][y1])
			{
				connected = true;
			}
		}

		x1 = x;
		y1 = y - 1;
		if(isOnBoard(x1, y1))
		{
			if(board.b[x1][y1] == playerNum && c[x1][y1])
			{
				connected = true;
			}
		}

		if(connected)
		{
			gameOverConnect(playerNum, x, y, c);
		}
	}
	
	private void gameOverConnect(int playerNum, int x, int y, boolean[][] c)
	{
		if(!isOnBoard(x, y))
		{
			return;
		}
		
		if(c[x][y])
		{
			return;
		}

		if(board.b[x][y] != playerNum)
		{
			return;
		}
		
		c[x][y] = true;

		int x1;
		int y1;
		
		x1 = x + 1;
		y1 = y - 1;
		gameOverConnect(playerNum, x1, y1, c);
		
		x1 = x + 1;
		y1 = y;
		gameOverConnect(playerNum, x1, y1, c);

		x1 = x;
		y1 = y + 1;
		gameOverConnect(playerNum, x1, y1, c);

		x1 = x - 1;
		y1 = y + 1;
		gameOverConnect(playerNum, x1, y1, c);

		x1 = x - 1;
		y1 = y;
		gameOverConnect(playerNum, x1, y1, c);

		x1 = x;
		y1 = y - 1;
		gameOverConnect(playerNum, x1, y1, c);
	}	
}
