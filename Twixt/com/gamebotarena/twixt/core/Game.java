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

package com.gamebotarena.twixt.core;

import java.util.ArrayList;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.twixt.api.Board;
import com.gamebotarena.twixt.api.CheckTable;
import com.gamebotarena.twixt.api.Move;
import com.gamebotarena.twixt.api.MoveContext;
import com.gamebotarena.twixt.api.TwixtGameBot;

public class Game extends GameBase
{
	public static String ID = "Twixt";

	int numPlayers = 2;
	
	GameUi ui;
	ArrayList<BotInfo> botInfos;
	TwixtGameBot[] bots;
	
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

		for(int i=1; i <= numPlayers; i++)
		{
			bots[i].setup(i);
		}
		
		//todo: provide an init!!!
		
		//for(int i=1; i <= numPlayers; i++)
		//{
		//	bots[i].init(i);
		//}

		boolean canSwitch = true;
		
		int lastPlayer = 0;
		Move lastMove = null;
		while (!gameOver && !ui.getStopEarly())
		{
			for (int i = 1; i < 3; i++)
			{
				if(ui.getStopEarly())
				{
					break;
				}

				ui.showBoard();

				lastPlayer = i;
				Board b = board;
				if(i == 2)
				{
					b = swap(board);
				}

				MoveContext context = new MoveContext(canSwitch, turn, b, i, lastMove);
				Move move = bots[i].getMove(context);

				if (!TwixtUtils.isMoveValid(context, move))
				{
					move = TwixtUtils.getRandomMove(context);
				}
				
				//D.out("move = " + move.x + "," + move.y);

				if(turn == 1 && move == Move.SwapMove && canSwitch)
				{
					//todo: Fix this, make sure the swapping player doesn't get to move twice...
					canSwitch = false;
					Move swappedLast = inverseSwap(lastMove);
					board.b[swappedLast.y][Board.SIZE - swappedLast.x - 1] = 2;
					board.b[swappedLast.x][swappedLast.y] = 0;
					//todo: really set turn back one?
					turn--;
				}
				else
				{
					if(i == 2)
					{
						move = swap(move);
					}

					if(i == 2)
					{
						lastMove = move;
					}
					else
					{
						lastMove = swap(move);
					}

					board.b[move.x][move.y] = i;

					try
					{
						for(int li=1; li < 9; li++)
						{
							int liToCheck = li;
							int[] dif = Board.getDifference(liToCheck);
							int x1 = move.x;
							int y1 = move.y;
							int x2 = move.x + dif[0];
							int y2 = move.y + dif[1];
							if(TwixtUtils.isPositionValid(x2, y2))
							{
								if(board.b[x2][y2] == i)
								{
									if(liToCheck > 4)
									{
										liToCheck = Board.inverse(liToCheck);
										int x3 = x1;
										int y3 = y1;
										x1 = x2;
										y1 = y2;
										x2 = x3;
										y2 = y3;
									}

									if(!areThereConflicts(x1, y1, liToCheck, move.forceLinks, i))
									{
										board.l[x1][y1][liToCheck] = true;
										board.l[x2][y2][Board.inverse(liToCheck)] = true;

										if(move.forceLinks)
										{
											CheckTable[] ct = Board.CHECKTABLE[liToCheck];

											for(int i2=0; i2 < ct.length; i2++)
											{
												CheckTable table = ct[i2];
												int xToCheck = table.x + x1;
												int yToCheck = table.y + y1;

												if(TwixtUtils.isPositionValid(xToCheck, yToCheck))
												{
													boolean[] linesToCheck = board.l[xToCheck][yToCheck];
													for(int j=0; j < table.lines.length; j++)
													{
														int line = table.lines[j];
														if(linesToCheck[line])
														{
															removeLink(xToCheck, yToCheck, line);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					catch(Exception e)
					{
						D.out("error" + e);
					}
				}

				turn++;
				gameOver = checkGameOver(i);
				if(gameOver)
				{
					break;
				}
				//todo: Check for board full?
			}
		}
		
		if(gameOver)
		{
			if(draw)
			{
				botInfos.get(0).draw();
				botInfos.get(1).draw();
				ui.gameOver("draw...");
			}
			else
			{
				if(lastPlayer == 1)
				{
					botInfos.get(0).won();
					botInfos.get(1).lost();
					ui.gameOver("Red wins");
				}
				else
				{
					botInfos.get(0).lost();
					botInfos.get(1).won();
					ui.gameOver("Black wins");
				}
			}
		}
		
		//todo: double check that we are not reporting wins/losses after stop early gets set to true...
	}

	public void removeLink(int x, int y, int line)
	{
		int x1 = x + Board.DIFFERENCE[line][0];
		int y1 = y + Board.DIFFERENCE[line][1];
		int line1 = Board.inverse(line);

		board.l[x][y][line] = false;
		board.l[x1][y1][line1] = false;
	}

	public Board swap(Board b)
	{
		Board swappedBoard = new Board();

		for(int x=0; x < Board.SIZE; x++)
		{
			for(int y=0; y < Board.SIZE; y++)
			{
				swappedBoard.b[y][x] = b.b[x][Board.SIZE - 1 - y];
			}
		}

		return swappedBoard;
	}

	public Move swap(Move m)
	{
		return new Move(m.y, Board.SIZE - 1 - m.x);
	}

	public Move inverseSwap(Move m)
	{
		return new Move(Board.SIZE - 1 - m.y, m.x);
	}

	public boolean checkGameOver(int currentPlayer)
	{
		if(turn > (Board.SIZE - 2) * (Board.SIZE - 2))
		{
			draw = true;

			Board b = board;
			int opposingPlayer = 1;
			if(currentPlayer == 1)
			{
				b = swap(board);
				opposingPlayer = 2;
			}

			MoveContext mc = new MoveContext(false, turn, b, opposingPlayer, null);
			for(int x = 0; x < Board.SIZE; x++)
			{
				for(int y = 0; y < Board.SIZE; y++)
				{
					if(TwixtUtils.isMoveValid(mc, new Move(x, y)))
					{
						draw = false;
						break;
					}
				}
			}

			if(draw)
			{
				return true;
			}
		}

		boolean[][] check = new boolean[Board.SIZE][];
		for(int i=0; i < Board.SIZE; i++)
		{
			check[i] = new boolean[Board.SIZE];
		}

		boolean check1 = false;
		boolean check2 = false;
		for(int i=0; i < Board.SIZE; i += Board.SIZE - 1)
		{
			for(int j=0; j < Board.SIZE; j++)
			{
				int x = i;
				int y = j;

				if(currentPlayer == 2)
				{
					x = j;
					y = i;
				}

				if(i == 0)
				{
					if(board.b[x][y] == currentPlayer)
					{
						check1 = true;
					}
				}
				else
				{
					if(board.b[x][y] == currentPlayer)
					{
						check2 = true;
					}
				}
			}
		}

		if(!check1 || !check2)
		{
			return false;
		}

		int i2=0;
		for(int j=0; j < Board.SIZE; j++)
		{
			int x = i2;
			int y = j;

			if(currentPlayer == 2)
			{
				x = j;
				y = i2;
			}

			if(board.b[x][y] == currentPlayer)
			{
				if(checkLinks(x, y, currentPlayer, check))
				{
					return true;
				}
			}
		}

		return false;
	}

	public boolean checkLinks(int x, int y, int currentPlayer, boolean[][] check)
	{
		if(check[x][y])
		{
			return false;
		}

		if(currentPlayer == 1)
		{
			if(x + 1 == Board.SIZE)
			{
				return true;
			}
		}
		else
		{
			if(y + 1 == Board.SIZE)
			{
				return true;
			}
		}

		check[x][y] = true;
		
		for(int i=1; i < 9; i++)
		{
			if(board.l[x][y][i])
			{
				int[] diff = Board.getDifference(i);
				int x2 = x + diff[0];
				int y2 = y + diff[1];
				if(checkLinks(x2, y2, currentPlayer, check))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean areThereConflicts(int x1, int y1, int li, boolean forceLinks, int player)
	{
		CheckTable[] ct = Board.CHECKTABLE[li];

		for(int i=0; i < ct.length; i++)
		{
			CheckTable table = ct[i];
			int xToCheck = table.x + x1;
			int yToCheck = table.y + y1;

			if(TwixtUtils.isPositionValid(xToCheck, yToCheck))
			{
				boolean[] linesToCheck = board.l[xToCheck][yToCheck];
				int playerToCheck = board.b[xToCheck][yToCheck];
				for(int j=0; j < table.lines.length; j++)
				{
					if(linesToCheck[table.lines[j]])
					{
						if(!forceLinks || playerToCheck != player)
						{
							return true;
						}
					}
				}
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
			botInfos = Misc.getBots(this, args, 2, "com.gamebotarena.twixt.api.RandomTwixtGameBot");
			
			bots = new TwixtGameBot[3];
	
			for (int i = 1; i <= 2; i++)
			{
				bots[i] = (TwixtGameBot) botInfos.get(i - 1).bot;
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
