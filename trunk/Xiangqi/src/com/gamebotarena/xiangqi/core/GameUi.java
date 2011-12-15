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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.GameUiBase;
import com.gamebotarena.xiangqi.api.Move;

public class GameUi extends GameUiBase
{
	static int crossSize = 9;
	static int crossBorder = 3;
	
	static int boardSizeX;
	static int boardSizeY;
	static int squareSizeX;
	static int squareSizeY;
	static int pieceOffsetX;
	static int pieceOffsetY;
	static int border;
	
	static int pieceSize;
	
	//ImageIcon board;
	
	static Color arrowColor = new Color(255, 10, 10).darker();
	static
	{
		arrowColor = new Color(arrowColor.getRed(), arrowColor.getGreen(), arrowColor.getBlue(), 200); 
	}

	HashMap<String, ImageIcon> images = new HashMap<String, ImageIcon>();
	HashMap<String, ImageIcon> imageMasks = new HashMap<String, ImageIcon>();

	int boardOffsetX;
	int boardOffsetY;
	
	Game game;
	
	Move move;
	
	public GameUi(Game game)
	{
		this.game = game;
		
		//board = new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/board.png"));
		
		images.put("k", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/rk.png")));
		images.put("a", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/ra.png")));
		images.put("e", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/re.png")));
		images.put("h", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/rh.png")));
		images.put("r", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/rr.png")));
		images.put("c", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/rc.png")));
		images.put("p", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/rp.png")));

		images.put("K", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/bk.png")));
		images.put("A", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/ba.png")));
		images.put("E", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/be.png")));
		images.put("H", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/bh.png")));
		images.put("R", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/br.png")));
		images.put("C", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/bc.png")));
		images.put("P", new ImageIcon(getResourceAsBytes("com/gamebotarena/xiangqi/core/res/bp.png")));
		
		boardSizeY = 525;
		boardSizeX = 475;

		border = 5;
		pieceSize = 45;
		
		squareSizeX = (boardSizeX - border * 2) / 9;
		squareSizeY = (boardSizeY - border * 2) / 10;
		pieceOffsetX = (squareSizeX - pieceSize) / 2;
		pieceOffsetY = (squareSizeY - pieceSize) / 2;

		boardOffsetX = 65;
		boardOffsetY = 15;
		
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(new Color(190, 20, 20));
		//colors.add(new Color(10, 10, 10));
		colors.add(new Color(20, 20, 20));
		setupUi("Xiangqi", this, game.botInfos, true, colors);
	}
	
	public void update(Graphics graphics)
	{
		if(stopEarly)
		{
			return;
		}
		
		Graphics2D g = getG();
		Stroke stroke3 = new BasicStroke(4);
		Stroke stroke2 = new BasicStroke(2);
		Stroke stroke1 = new BasicStroke(1);
		g.setStroke(stroke2);
		
		g.setColor(Color.white);
		g.fillRect(boardOffsetX, boardOffsetY, boardSizeX - border, boardSizeY - border);
		
		g.setStroke(stroke3);
		g.setColor(Color.black);
		g.drawRect(boardOffsetX, boardOffsetY, boardSizeX - border, boardSizeY - border);
		g.setStroke(stroke2);
		
		g.setColor(Color.gray);
		g.drawRect(boardOffsetX, boardOffsetY, boardSizeX - border, boardSizeY - border);
		
		g.setColor(Color.DARK_GRAY);
		
		int halfSquareX = squareSizeX / 2;
		int halfSquareY = squareSizeY / 2;
		
		int yEnd = boardOffsetY + border + squareSizeY * 10;
		for(int x=0; x < 9; x++)
		{
			int xLoc = boardOffsetX + border + squareSizeX * x + squareSizeX / 2;
			g.drawLine(xLoc, boardOffsetY + border + halfSquareY, xLoc, yEnd - halfSquareY - 1);
		}
		
		int xEnd = boardOffsetX + border + squareSizeX * 9;
		for(int y=0; y < 10; y++)
		{
			int yLoc = boardOffsetY + border + squareSizeY * y + squareSizeY / 2;
			g.drawLine(boardOffsetX + border + halfSquareX, yLoc, xEnd - halfSquareX - 1, yLoc);
		}
		
		Point start = null;
		Point end = null;
		
		start = xlatPoint(3, 0);
		end = xlatPoint(5, 2);
		
		g.drawLine(start.x, start.y, end.x, end.y);
		
		start = xlatPoint(5, 0);
		end = xlatPoint(3, 2);
		
		g.drawLine(start.x, start.y, end.x, end.y);
		
		start = xlatPoint(3, 9);
		end = xlatPoint(5, 7);
		
		g.drawLine(start.x, start.y, end.x, end.y);
		
		start = xlatPoint(5, 9);
		end = xlatPoint(3, 7);
		
		g.drawLine(start.x, start.y, end.x, end.y);
		
		g.setStroke(stroke1);
		
		crossPoint(g, 1, 2);
		crossPoint(g, 7, 2);
		crossPoint(g, 0, 3);
		crossPoint(g, 2, 3);
		crossPoint(g, 4, 3);
		crossPoint(g, 6, 3);
		crossPoint(g, 8, 3);
		
		crossPoint(g, 1, 7);
		crossPoint(g, 7, 7);
		crossPoint(g, 0, 6);
		crossPoint(g, 2, 6);
		crossPoint(g, 4, 6);
		crossPoint(g, 6, 6);
		crossPoint(g, 8, 6);
		
		g.setStroke(stroke2);
		
		
		Point p1 = xlatPoint(0, 4);
		Point p4 = xlatPoint(8, 5);
		
		g.setColor(Color.white);
		g.fillRect(p1.x + 1, p1.y + 1, p4.x - p1.x - 2, p4.y - p1.y - 2);
		
//		g.drawImage(board.getImage(), 0, -1, this);

//		g.setColor(Color.DARK_GRAY);
//		g.drawRect(boardOffsetX - 1, boardOffsetY - 1, squareSize * 8 + 1, squareSize * 8 + 1);
		
		for(int x=0; x < 9; x++)
		{
			for(int y=0; y < 10; y++)
			{
				char c = game.board.b[x][y];
				
				if(c != ' ')
				{
					String image = "" + c;
					
					int xFudge = -3;
					int yFudge = -3;
					
					int upperLeftx = x * squareSizeX + boardOffsetX + border + xFudge;
					int upperLefty = y * squareSizeY + boardOffsetY + border + yFudge;
					g.drawImage(images.get(image).getImage(), upperLeftx + pieceOffsetX + 3, upperLefty + pieceOffsetY + 3, this);
				}
			}
		}
		
		if(move != null)
		{
			g.setColor(arrowColor);
			Point from = xlatPoint(move.fromx, move.fromy);
			Point to = xlatPoint(move.tox, move.toy);

			Stroke stroke = g.getStroke();
			
			Stroke s = new BasicStroke(4);
			g.setStroke(s);
			drawArrow(bump(from), bump(to), 10, 15);
			
			g.setStroke(stroke);
		}
		
		
		drawMessageIfNeeded(g);
		graphics.drawImage(buffer, 0, 0, null);
	}
	
	Point bump(Point p)
	{
		return p;
		//return new Point(p.x + game.rand.nextInt(5) - 2, p.y + game.rand.nextInt(5) - 2);
	}
	
	Point xlatPoint(int x, int y)
	{
		int screenX = boardOffsetX + border + squareSizeX * x + squareSizeX / 2;
		int screenY = boardOffsetY + border + squareSizeY * y + squareSizeY / 2;
			
		return new Point(screenX, screenY);
	}
	
	void crossPoint(Graphics g, int x, int y)
	{
		Point p = xlatPoint(x, y);
		
		if(x != 0)
		{
			g.drawLine(p.x - crossSize - crossBorder - 1, p.y - crossBorder - 1, p.x - crossBorder - 1, p.y - crossBorder - 1);
			g.drawLine(p.x - crossBorder - 1, p.y - crossBorder - crossSize - 1, p.x - crossBorder - 1, p.y - crossBorder - 1);
			
			g.drawLine(p.x - crossSize - crossBorder - 1, p.y + crossBorder, p.x - crossBorder - 1, p.y + crossBorder);
			g.drawLine(p.x - crossBorder - 1, p.y + crossBorder + crossSize, p.x - crossBorder - 1, p.y + crossBorder);
		}

		if(x != 8)
		{
			g.drawLine(p.x + crossSize + crossBorder, p.y - crossBorder - 1, p.x + crossBorder, p.y - crossBorder - 1);
			g.drawLine(p.x + crossBorder, p.y - crossBorder - crossSize - 1, p.x + crossBorder, p.y - crossBorder - 1);
			
			g.drawLine(p.x + crossSize + crossBorder, p.y + crossBorder, p.x + crossBorder, p.y + crossBorder);
			g.drawLine(p.x + crossBorder, p.y + crossBorder + crossSize, p.x + crossBorder, p.y + crossBorder);
		}
	}
}
