package com.gamebotarena.pente.core;

import java.util.ArrayList;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.pente.api.Board;
import com.gamebotarena.pente.api.Move;
import com.gamebotarena.pente.api.MoveContext;
import com.gamebotarena.pente.api.PenteGameBot;

public class Game extends GameBase
{
	public static final String ID = "Pente";
	
	int numPlayers = 2;
	
	GameUi ui;
	ArrayList<BotInfo> botInfos;
	PenteGameBot[] bots;
	int[] captures;
	
	boolean draw;
	Board board;
	
	int turn;
	boolean gameOver;
	boolean[] winner;

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
		
		captures = new int[numPlayers];
		winner = new boolean[numPlayers];
		
		Move lastMove = null;
		while (!gameOver && !ui.getStopEarly())
		{
			ui.showBoard();
			MoveContext context = new MoveContext(this, board, lastMove, 1);
			Move move = bots[0].getMove(context);
			playMove(1, move, context);
			lastMove = move;
			
			//todo: May not need this gameOver check
			if (!gameOver && !ui.getStopEarly())
			{
				ui.showBoard();
				context = new MoveContext(this, board, lastMove, 2);
				move = bots[1].getMove(context);
				playMove(2, move, context);
				lastMove = move;
			}
			
			turn++;
		}

		if(gameOver)
		{
			//game.ui.showBoard();

			if(winner[0] == true)
			{
				botInfos.get(0).won();
				botInfos.get(1).lost();
				if(captures[0] >= 5)
				{
					//ui.gameOver("Player 1 Wins (" + captures[0] + " Captures)");
					ui.gameOver("Player 1 Wins");
				}
				else
				{
					ui.gameOver("Player 1 Wins");
				}
			}
			else if(winner[1] == true)
			{
				botInfos.get(0).lost();
				botInfos.get(1).won();
				if(captures[1] >= 5)
				{
					//ui.gameOver("Player 2 Wins (" + captures[1] + " Captures)");
					ui.gameOver("Player 2 Wins");
				}
				else
				{
					ui.gameOver("Player 2 Wins");
				}
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

	private void playMove(int player, Move move, MoveContext context)
	{
		if (!PenteUtils.isMoveValid(board, move, player))
		{
			move = PenteUtils.getRandomMove(context);
		}
		
		board.squares[move.x][move.y] = player;
		PenteUtils.processMove(board, move, player, captures);
		
		checkGameOver();
	}
	
	public void checkGameOver()
	{
		if(captures[0] >= 5)
		{
			//System.out.println("captures:0 - " + captures[0]);
			gameOver = true;
			winner[0] = true;
			return;
		}
		
		if(captures[1] >= 5)
		{
			//System.out.println("captures:1 - " + captures[1]);
			gameOver = true;
			winner[1] = true;
			return;
		}
		
		for (int x = 0; x < Board.SIZE; x++)
		{
			for (int y = 0; y < Board.SIZE; y++)
			{
				int player = board.squares[x][y]; 
				if (player != 0)
				{
					int max = 1;
					
					int x1 = 0;
					int y1 = 0;
					
					x1 = -1;
					y1 = 0;
					max = Math.max(max, countLine(player, x, y, x1, y1));
					
					if(max < 5)
					{
						x1 = -1;
						y1 = -1;
						max = Math.max(max, countLine(player, x, y, x1, y1));
	
						if(max < 5)
						{
							x1 = 0;
							y1 = -1;
							max = Math.max(max, countLine(player, x, y, x1, y1));
		
							if(max < 5)
							{
								x1 = 1;
								y1 = -1;
								max = Math.max(max, countLine(player, x, y, x1, y1));
								
								if(max < 5)
								{
									x1 = 1;
									y1 = 0;
									max = Math.max(max, countLine(player, x, y, x1, y1));
				
									if(max < 5)
									{
										x1 = 1;
										y1 = 1;
										max = Math.max(max, countLine(player, x, y, x1, y1));
					
										if(max < 5)
										{
											x1 = 0;
											y1 = 1;
											max = Math.max(max, countLine(player, x, y, x1, y1));
						
											if(max < 5)
											{
												x1 = -1;
												y1 = 1;
												max = Math.max(max, countLine(player, x, y, x1, y1));
											}
										}
									}
								}
							}
						}
					}
					
					if(max >= 5)
					{
						//System.out.println("5 in a row:" + player);
						gameOver = true;
						winner[player - 1] = true;
						return;
					}
				}
			}
		}
	}
	
	public int countLine(int player, int x, int y, int x1, int y1)
	{
		int count = 1;
		
		int startX = x;
		int startY = y;
		
		while(x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE)
		{
			x += x1;
			y += y1;

			if(PenteUtils.getSquareSafe(board, x, y) != player)
			{
				break;
			}
			
			count++;
		}
		
		x = startX;
		y = startY;
		
		if(count >= 5)
		{
			while(x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE)
			{
				if(PenteUtils.getSquareSafe(board, x, y) != player)
				{
					break;
				}

				board.squares[x][y] += 2;

				x += x1;
				y += y1;
			}
		}
		
		return count;
	}

	public static void main(String[] args)
	{
		new Game().start(args);
	}

	public void start(final String[] args)
	{
		try
		{
			botInfos = Misc.getBots(this, args, 2, "com.gamebotarena.pente.api.RandomPenteGameBot");
			
			bots = new PenteGameBot[2];
	
			for (int i = 0; i < 2; i++)
			{
				bots[i] = (PenteGameBot) botInfos.get(i).bot;
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
