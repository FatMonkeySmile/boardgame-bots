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

package com.gamebotarena.gobblet.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.GameUiBase;
import com.gamebotarena.gobblet.api.Piece;
import com.gamebotarena.gobblet.api.Square;

//todo: 3 in a row bring from external ok, repition rule, check for returning null (no valid moves)...
public class GameUi extends GameUiBase
{
	Color ARROW_COLOR = Color.LIGHT_GRAY;
	int OVAL_SIZE = 8;
	
	static int boardSize;
	static int mainSquareSize;
	static int halfSquare;
	
	int boardOffsetX;
	int boardOffsetY;
	
	Game game;
	
	int stack;
	int stackPlayer;
	Point fromPoint;
	Point toPoint;
	
	int downSize = 15;
	
	int[] px;
	int[] py;
	
	int pyOffset;
	
	public GameUi(Game game)
	{
		this.game = game;
		
		boardSize = 550;
		mainSquareSize = boardSize / 6;

		boardOffsetX = 115;
		boardOffsetY = 90;

		px = new int[2];
		py = new int[2];
		
		px[0] = 13;
		py[0] = 90;
		pyOffset = 90;
		
		px[1] = mainSquareSize * 5 + 30;
		py[1] = 190;
		
		halfSquare = mainSquareSize / 2;
		
		ArrayList<Color> colors = new ArrayList<Color>();
		//colors.add(new Color(10, 10, 10));
		colors.add(new Color(190, 180, 165));
		colors.add(Color.DARK_GRAY); //new Color(92, 76, 44));
		//colors.add(new Color(255, 255, 255));
		setupUi("Gobblet", this, game.botInfos, true, colors);
	}
	
	public void update(Graphics graphics)
	{
		if(stopEarly)
		{
			return;
		}
		
		if(game == null || game.board == null || game.board.squares == null || game.board.squares[0][0] == null)
		{
			return;
		}
		
		Graphics2D g = getG();
		
		fillBackground();

		boolean drawWhite = true;
		Color[] colors = new Color[2];
//x		colors[0] = new Color(246, 240, 222);
//x		colors[1] = Color.DARK_GRAY; //new Color(120, 144, 128);
		colors[0] = new Color(246, 240, 222);
		colors[1] = new Color(105, 100, 100); //Color.GRAY; //new Color(120, 144, 128);
		
		Color[] boardColors = new Color[2];
		//boardColors[0] = new Color(237, 208, 174);
		boardColors[0] = new Color(245, 245, 230); //new Color(237, 218, 184);
		boardColors[1] = new Color(230, 230, 230); //new Color(200, 178, 144);
		
		
		Color curr = null;
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(boardOffsetX - 1, boardOffsetY - 1, mainSquareSize * 4 + 1, mainSquareSize * 4 + 1);
		g.drawRect(boardOffsetX - 2, boardOffsetY - 2, mainSquareSize * 4 + 3, mainSquareSize * 4 + 3);
		
		for(int x=0; x < 4; x++)
		{
			for(int y=0; y < 4; y++)
			{
				if(drawWhite)
				{
					curr = boardColors[1];
					drawWhite = false;
				}
				else
				{
					curr = boardColors[0];
					drawWhite = true;
				}

				//xtodo: make sure that 0, 0 is lower left
				int upperLeftx = x * mainSquareSize + boardOffsetX;
				//int upperLefty = (7-y) * squareSize + boardOffsetY;
				int upperLefty = y * mainSquareSize + boardOffsetY;

				g.setColor(curr);
				g.fillRect(upperLeftx, upperLefty, mainSquareSize, mainSquareSize);
				
				Square sq = game.board.squares[x][y];
				
				Piece piece = sq.getPiece();
				if(piece != null)
				{
					int p = sq.getPiece().getPlayer();
					int sz = sq.getPiece().getSize();
					
					int downSizeValue = ((4 - sz) * downSize);
					int downSizeOffset = (downSizeValue) / 2;
					int squareSize = mainSquareSize - downSizeValue;
	
					int margin = 6;
					int marginX2 = margin * 2;
					
					//g.drawImage(images.get(image).getImage(), upperLeftx + pieceOffsetX, upperLefty + pieceOffsetY, (int) pieceSize, (int) pieceSize, this);
					
					g.setColor(colors[p]);
					g.fillOval(upperLeftx + margin + downSizeOffset, upperLefty + margin + downSizeOffset, squareSize - marginX2, squareSize - marginX2);
					
					//margin = 16;
					//marginX2 = margin * 2;
					
//					{
//						g.setColor(Color.black);
//						g.drawOval(upperLeftx + margin, upperLefty + margin, squareSize - marginX2, squareSize - marginX2);
//					}
					if(p == 0)
					{
						g.setColor(colors[1]);
						g.drawOval(upperLeftx + margin + downSizeOffset, upperLefty + margin + downSizeOffset, squareSize - marginX2, squareSize - marginX2);
					}
				}

				synchronized(this)
				{
					if(fromPoint != null)
					{
						g.setColor(ARROW_COLOR);
						
						int ovalSize = OVAL_SIZE;
						int halfOval = ovalSize / 2;
						
						g.fillOval(fromPoint.x - halfOval, fromPoint.y - halfOval, ovalSize, ovalSize);
						
						drawArrow(fromPoint, toPoint, 0, 12, true);
					}
				}
			}
			
			
			for(int player=0; player < 2; player++)
			{
				for(int stack=0; stack < 3; stack++)
				{
					int upperLeftx = px[player];
					int upperLefty = py[player] + pyOffset * stack;
					
					Square sq = game.board.unPlayed[player][stack];
					
					Piece piece = sq.getPiece();
					if(piece != null)
					{
						int p = sq.getPiece().getPlayer();
						int sz = sq.getPiece().getSize();
						
						int downSizeValue = ((4 - sz) * downSize);
						int downSizeOffset = (downSizeValue) / 2;
						int squareSize = mainSquareSize - downSizeValue;
		
						int margin = 6;
						int marginX2 = margin * 2;
						
						//g.drawImage(images.get(image).getImage(), upperLeftx + pieceOffsetX, upperLefty + pieceOffsetY, (int) pieceSize, (int) pieceSize, this);
						
						if(p == 1)
						{
							g.setColor(colors[1]);
						}
						else
						{
							g.setColor(colors[0]);
						}
						g.fillOval(upperLeftx + margin + downSizeOffset, upperLefty + margin + downSizeOffset, squareSize - marginX2, squareSize - marginX2);
						
						//margin = 16;
						//marginX2 = margin * 2;
						
						if(p == 1)
						{
//							g.setColor(Color.black);
//							g.drawOval(upperLeftx + margin, upperLefty + margin, squareSize - marginX2, squareSize - marginX2);
						}
						else
						{
							g.setColor(colors[1]);
							g.drawOval(upperLeftx + margin + downSizeOffset, upperLefty + margin + downSizeOffset, squareSize - marginX2, squareSize - marginX2);
						}
					}

					if(this.stack != -1 && stackPlayer == player && this.stack == stack)
					{
						g.setColor(ARROW_COLOR);
						
						int ovalSize = OVAL_SIZE;
						int halfOval = ovalSize / 2;
						
						g.fillOval(upperLeftx + halfSquare - halfOval, upperLefty + halfSquare - halfOval, ovalSize, ovalSize);
						//g.fillOval(upperLeftx , upperLefty , ovalSize, ovalSize);
						
						drawArrow(new Point(upperLeftx + halfSquare, upperLefty + halfSquare), toPoint, 0, 12, true);
					}
				}
			}
			

			drawWhite = !drawWhite;
		}
		
		drawMessageIfNeeded(g);
		graphics.drawImage(buffer, 0, 0, null);
	}
	
	synchronized public void setInternalMove(Point fromPoint, Point toPoint)
	{
		this.fromPoint = new Point(fromPoint.x * mainSquareSize + halfSquare + boardOffsetX, fromPoint.y * mainSquareSize + halfSquare + boardOffsetY);
		this.toPoint = new Point(toPoint.x * mainSquareSize + halfSquare + boardOffsetX, toPoint.y * mainSquareSize + halfSquare + boardOffsetY);
		this.stack = -1;
		this.stackPlayer = -1;
	}
	
	synchronized public void setExternalMove(int stack, Point toPoint, int stackPlayer)
	{
		this.stack = stack;
		this.stackPlayer = stackPlayer;
		this.fromPoint = null;
		this.toPoint = new Point(toPoint.x * mainSquareSize + halfSquare + boardOffsetX, toPoint.y * mainSquareSize + halfSquare + boardOffsetY);
	}
	
	
	synchronized public void clearMove()
	{
		stack = -1;
		stackPlayer = -1;
		fromPoint = null;
		toPoint = null;
	}
}
