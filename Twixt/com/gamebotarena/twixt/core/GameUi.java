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

package com.gamebotarena.twixt.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;

import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.GameUiBase;
import com.gamebotarena.twixt.api.Board;

public class GameUi extends GameUiBase
{
	Game game;
	
	Color highlight = new Color(75, 75, 75);
	ArrayList<Color> c = new ArrayList<Color>();
	ArrayList<Color> b = new ArrayList<Color>();
	
	static int boardSize = 560;
	static int distanceBetweenHoles = boardSize / Board.SIZE;
	static int holeSize = 3;
	static int pegSize = 10;
	static int halfHoleSize = holeSize / 2;
	static int halfPegSize = pegSize / 2;

	static int boardOffsetX = 15;
	static int boardOffsetY = 5;
	
	static int pegOffsetX = boardOffsetX + distanceBetweenHoles / 2;
	static int pegOffsetY = boardOffsetY + distanceBetweenHoles / 2;

	public GameUi(Game game)
	{
		this.game = game;
		c.add(new Color(180, 10, 10));
		c.add(new Color(30, 30, 30));
		b.add(new Color(210, 10, 10));
		b.add(new Color(60, 60, 60));
		setupUi("Twixt", this, game.botInfos, true, c);
		
		c.add(0, new Color(60, 60, 60));		
		b.add(0, new Color(60, 60, 60));		
	}
	
	public void update(Graphics graphics)
	{
		if(stopEarly || c.size() < 3 || game == null || game.board == null)
		{
			return;
		}
		
		Graphics2D g = getG();
		
		Stroke s = new BasicStroke(2);
		g.setStroke(s);
		
		int d1 = distanceBetweenHoles;
		int d2 = boardSize - (2 * d1);

		int firstFudge = 1;
		int lastFudge = 7;

		int x1 = boardOffsetX + firstFudge;
		int x2 = boardOffsetX + d1 + firstFudge;
		int x3 = boardOffsetX + boardSize - d1 - lastFudge;
		int x4 = boardOffsetX + boardSize - lastFudge;
		
		int y1 = boardOffsetY + firstFudge;
		int y2 = boardOffsetY + d1 + firstFudge;
		int y3 = boardOffsetY + boardSize - d1 - lastFudge;
		int y4 = boardOffsetY + boardSize - lastFudge;

		// Draw the board.
		g.setColor(new Color(195, 195, 195));
		g.fillRect(x1, y1, x4 - boardOffsetX, y4 - boardOffsetY);
		
		g.setColor(b.get(1));
		g.fillRect(x1, y2, x2 - x1, y3 - y2); 
		g.fillRect(x3, y2, x4 - x3, y3 - y2); 

		g.setColor(b.get(2));
		g.fillRect(x2, y1, x3 - x2, y2 - y1); 
		g.fillRect(x2, y3, x3 - x2, y4 - y3); 

		Polygon poly;
		g.setColor(b.get(1));
		poly = new Polygon();
		poly.addPoint(x1, y1);		poly.addPoint(x1, y2);		poly.addPoint(x2, y2);
		g.fillPolygon(poly);
		poly = new Polygon();
		poly.addPoint(x1, y4);		poly.addPoint(x1, y3);		poly.addPoint(x2, y3);
		g.fillPolygon(poly);
		poly = new Polygon();
		poly.addPoint(x4, y1);		poly.addPoint(x4, y2);		poly.addPoint(x3, y2);
		g.fillPolygon(poly);
		poly = new Polygon();
		poly.addPoint(x4, y4);		poly.addPoint(x4, y3);		poly.addPoint(x3, y3);
		g.fillPolygon(poly);

		g.setColor(b.get(2));
		poly = new Polygon();
		poly.addPoint(x1, y1);		poly.addPoint(x2, y2);		poly.addPoint(x2, y1);
		g.fillPolygon(poly);
		poly = new Polygon();
		poly.addPoint(x4, y1);		poly.addPoint(x3, y1);		poly.addPoint(x3, y2);
		g.fillPolygon(poly);
		poly = new Polygon();
		poly.addPoint(x4, y4);		poly.addPoint(x3, y4);		poly.addPoint(x3, y3);
		g.fillPolygon(poly);
		poly = new Polygon();
		poly.addPoint(x1, y4);		poly.addPoint(x2, y4);		poly.addPoint(x2, y3);
		g.fillPolygon(poly);
		
		for(int x=0; x < Board.SIZE; x++)
		{
			for(int realY=0; realY < Board.SIZE; realY++)
			{
				//Inversing y like this so that y = 0 will be at the bottom of the screen.
				int y = (Board.SIZE - realY - 1);
				int p = game.board.b[x][realY];

				int xLoc = x * distanceBetweenHoles + pegOffsetX;
				int yLoc = y * distanceBetweenHoles + pegOffsetY;

				if(p == 0)
				{
					if(!TwixtUtils.isCorner(x, realY))
					{
						g.setColor(c.get(0));
						g.fillOval(xLoc - halfHoleSize, yLoc - halfHoleSize, holeSize + 2, holeSize + 2);
						g.setColor(highlight);
						g.fillOval(xLoc - halfHoleSize + 1, yLoc - halfHoleSize + 1, holeSize, holeSize);
					}
				}
				else
				{
					g.setColor(Color.gray);
					g.fillOval(xLoc - halfPegSize, yLoc - halfPegSize, pegSize + 1, pegSize + 1);
					g.setColor(c.get(p));
					g.fillOval(xLoc - halfPegSize, yLoc - halfPegSize, pegSize, pegSize);
				}

				if(p > 0)
				{
					for(int i=1; i < 5; i++)
					{
						if(game.board.l[x][realY][i])
						{
							int[] diff = Board.getDifference(i);
							int xLoc1 = (diff[0] + x) * distanceBetweenHoles + pegOffsetX;
							int yLoc1 = (Board.SIZE - 1 - (diff[1] + realY)) * distanceBetweenHoles + pegOffsetY;

							Color color = c.get(p);
							g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
							
							g.drawLine(xLoc, yLoc, xLoc1, yLoc1);
						}
					}
				}
			}
		}

		drawMessageIfNeeded(g);
	
		graphics.drawImage(buffer, 0, 0, null);
	}
}
