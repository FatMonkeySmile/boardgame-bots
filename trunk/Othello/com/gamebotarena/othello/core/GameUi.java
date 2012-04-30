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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.gamebotarena.framework.core.GameUiBase;

public class GameUi extends GameUiBase
{
	static int boardSize = 555;
	static int squareSize;

	int boardOffsetX = 10;
	int boardOffsetY = 10;

	static int circleMargin;
	static int circleSize;

	Game game;
	ArrayList<Color> colors = new ArrayList<Color>();
	
	public GameUi(Game game)
	{
		this.game = game;

		squareSize = boardSize / 8;
		circleMargin = squareSize / 8;
		circleSize = squareSize - (2 * circleMargin);		
		
		ArrayList<Color> nameColors = new ArrayList<Color>();
		nameColors.add(new Color(160, 160, 150));
		nameColors.add(new Color(10, 10, 10));
		setupUi("Othello", this, game.botInfos, true, nameColors);
		
		colors.add(new Color(228, 228, 210));
		colors.add(new Color(30, 30, 30));
	}
	
	public void update(Graphics graphics)
	{
		if(stopEarly)
		{
			return;
		}
		
		if(game.board == null || game.board.squares == null || colors.size() < 2)
		{
			return;
		}
		
		Graphics2D g = getG();
		fillBackground();
		//g.setColor(new Color(255, 255, 255));
		g.setColor(new Color(60, 120, 60));
		g.fillRect(boardOffsetX, boardOffsetY, 8 * squareSize, 8 * squareSize);
		
		for(int x=0; x < 8; x++)
		{
			for(int y=0; y < 8; y++)
			{
				int upperLeftx = x * squareSize + boardOffsetX;
				int upperLefty = y * squareSize + boardOffsetY;

				//g.setColor(new Color(160, 160, 160));
				g.setColor(new Color(180, 180, 180));
				g.drawRect(upperLeftx, upperLefty, squareSize, squareSize);
				
				if(game.board.squares[x][y] > 0)
				{
					Color c = colors.get(game.board.squares[x][y] - 1);
					//Color c2 = colors.get(0);
					//if(c == c2)
					//{
					//	c2 = colors.get(1);
					//}
					//int offset = -3;
					
					//g.setColor(c2);
					//g.fillOval(upperLeftx + circleMargin + offset, upperLefty + circleMargin + offset, circleSize + 6, circleSize + 6);

					int buff1 = 5;
					int buff2 = 4;
					int buff3 = 2;
					int buff4 = 3;
					int shift = 3;
					
					upperLefty -= 1;
					
//					g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
//					g.fillOval(upperLeftx + circleMargin - buff1, upperLefty + circleMargin - buff1, circleSize + buff1 + buff1, circleSize + buff1 + buff1);
//					
//					g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 180));
//					g.fillOval(upperLeftx + circleMargin - buff2, upperLefty + circleMargin - buff2, circleSize + buff2 + buff2, circleSize + buff2 + buff2);
//
//					g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 210));
//					g.fillOval(upperLeftx + circleMargin - buff3, upperLefty + circleMargin - buff3, circleSize + buff3 + buff3, circleSize + buff3 + buff3);
					
					g.setColor(c);
					g.fillOval(upperLeftx + circleMargin - shift, upperLefty + circleMargin - shift, circleSize, circleSize);
					
					g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 215));
					g.fillOval(upperLeftx + circleMargin - shift, upperLefty + circleMargin - shift, circleSize + buff3 + buff3, circleSize + buff3 + buff3);
					
					g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 60));
					g.fillOval(upperLeftx + circleMargin - shift, upperLefty + circleMargin - shift, circleSize + buff4 + buff4, circleSize + buff4 + buff4);

					g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 180));
					g.fillOval(upperLeftx + circleMargin - shift, upperLefty + circleMargin - shift, circleSize + buff2 + buff2, circleSize + buff2 + buff2);

					g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
					//g.setColor(Color.yellow);
					g.fillOval(upperLeftx + circleMargin - shift - 2, upperLefty + circleMargin - shift - 2, circleSize + buff1 + buff1, circleSize + buff1 + buff1);
				}
			}
		}
		
		g.setColor(new Color(180, 180, 180));
		g.drawRect(boardOffsetX + 1, boardOffsetY + 1, 8 * squareSize - 2, 8 * squareSize - 2);
		g.drawRect(boardOffsetX - 1, boardOffsetY - 1, 8 * squareSize + 2, 8 * squareSize + 2);
		
		g.setColor(colors.get(1));
		g.drawRect(boardOffsetX, boardOffsetY, 8 * squareSize, 8 * squareSize);
		
		drawMessageIfNeeded(g);
		graphics.drawImage(buffer, 0, 0, null);
	}
}
