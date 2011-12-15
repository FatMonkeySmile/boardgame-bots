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

package com.gamebotarena.pente.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.GameUiBase;
import com.gamebotarena.pente.api.Board;

public class GameUi extends GameUiBase
{
	static int boardSize = 555;
	static int squareSize;
	static int halfSquare;

	int boardOffsetX = 10;
	int boardOffsetY = 10;

	static int circleMargin;
	static int circleSize;

	Game game;
	ArrayList<Color> colors = new ArrayList<Color>();
	
	Font captureFont = new Font("Arial", Font.BOLD, 18);
	ArrayList<Color> captureColors = new ArrayList<Color>();
	
	int captureX = 435;
	int[] captureY = new int[]{510, 538};
	
	ImageIcon[] imgs = new ImageIcon[3];
	
	public GameUi(Game game)
	{
		this.game = game;
		
		imgs[1] = new ImageIcon(getResourceAsBytes("com/gamebotarena/pente/core/res/amber.png"));
		imgs[2] = new ImageIcon(getResourceAsBytes("com/gamebotarena/pente/core/res/blue.png"));

		squareSize = boardSize / Board.SIZE;
		halfSquare = squareSize / 2;
		circleMargin = squareSize / 9;
		circleSize = squareSize - (2 * circleMargin);		
		
		Color blue = new Color(121, 172, 177);
		Color amber = new Color(219, 154, 80);
		
		int fade = 165;
		captureColors.add(new Color(amber.getRed(), amber.getGreen(), amber.getBlue(), fade));
		captureColors.add(new Color(blue.getRed(), blue.getGreen(), blue.getBlue(), fade));
		
		ArrayList<Color> nameColors = new ArrayList<Color>();
		nameColors.add(amber);
		nameColors.add(blue);
		setupUi("Pente", this, game.botInfos, true, nameColors);
		
		colors.add(amber);
		colors.add(blue);
		
		colors.add(amber.brighter().brighter());
		colors.add(blue.brighter().brighter());
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
		g.setColor(new Color(255, 255, 255));
		g.fillRect(boardOffsetX, boardOffsetY, Board.SIZE * squareSize, Board.SIZE * squareSize);
		
		for(int x=0; x < Board.SIZE; x++)
		{
			for(int y=0; y < Board.SIZE; y++)
			{
				int upperLeftx = x * squareSize + boardOffsetX;
				int upperLefty = y * squareSize + boardOffsetY;

				g.setColor(new Color(200, 200, 200));
				
				g.drawLine(upperLeftx, upperLefty + halfSquare, upperLeftx + squareSize, upperLefty + halfSquare);
				g.drawLine(upperLeftx + halfSquare, upperLefty, upperLeftx + halfSquare, upperLefty + squareSize);
				
				g.setColor(new Color(239, 239, 239));
				g.drawLine(upperLeftx, upperLefty + halfSquare + 1, upperLeftx + squareSize, upperLefty + halfSquare + 1);
				g.drawLine(upperLeftx + halfSquare + 1, upperLefty, upperLeftx + halfSquare + 1, upperLefty + squareSize);

				g.setColor(new Color(252, 252, 252));
				g.drawLine(upperLeftx, upperLefty + halfSquare + 2, upperLeftx + squareSize, upperLefty + halfSquare + 2);
				g.drawLine(upperLeftx + halfSquare + 2, upperLefty, upperLeftx + halfSquare + 2, upperLefty + squareSize);
				
				if(game.board.squares[x][y] > 0)
				{
					int i = game.board.squares[x][y];
					
					if(i > 2)
					{
						i -= 2;
					}
					g.drawImage(imgs[i].getImage(), upperLeftx + circleMargin, upperLefty + circleMargin, (ImageObserver) null);
					
					if(game.board.squares[x][y] > 2)
					{
						g.setColor(new Color(110, 30, 30, 65));
						g.fillOval(upperLeftx + circleMargin + 1, upperLefty + circleMargin + 1, circleSize - 1, circleSize - 1);
					}
				}
			}
		}
		
		for(int i=1; i <= game.numPlayers; i++)
		{
			String s = "Captures  " + game.captures[i - 1];
			g.setFont(captureFont);
			Color color = captureColors.get(i - 1);
			if(game.captures[i - 1] >= 5)
			{
				color = setAlpha(color, 200);
				Color c = Color.black;
				g.setColor(setAlpha(c, 150));
				g.drawString(s, captureX + 1, captureY[i - 1] + 1);
				
//				g.setColor(Color.white);
//				g.drawString(s, captureX, captureY[i - 1]);
			}
			g.setColor(color);
			g.drawString(s, captureX, captureY[i - 1]);
		}
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(boardOffsetX, boardOffsetY, Board.SIZE * squareSize, Board.SIZE * squareSize);
		
		drawMessageIfNeeded(g);
		graphics.drawImage(buffer, 0, 0, null);
	}
}
