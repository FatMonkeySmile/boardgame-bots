package com.gamebotarena.linesofaction.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.GameUiBase;

public class GameUi extends GameUiBase
{
	static int boardSize;
	static int squareSize;
	static int halfSquare;
	
	int boardOffsetX;
	int boardOffsetY;
	
	Game game;
	
	Point fromPoint;
	Point toPoint;
	
	public GameUi(Game game)
	{
		this.game = game;
		
		boardSize = 550;
		squareSize = boardSize / 8;

		boardOffsetX = 15;
		boardOffsetY = 15;
		
		halfSquare = squareSize / 2;
		
		ArrayList<Color> colors = new ArrayList<Color>();
		//colors.add(new Color(10, 10, 10));
		colors.add(Color.DARK_GRAY); //new Color(92, 76, 44));
		colors.add(new Color(190, 180, 165));
		//colors.add(new Color(255, 255, 255));
		setupUi("Lines of Action", this, game.botInfos, true, colors);
	}
	
	public void update(Graphics graphics)
	{
		if(stopEarly)
		{
			return;
		}
		
		if(game == null || game.board == null || game.board.b == null)
		{
			return;
		}
		
		Graphics2D g = getG();

		boolean drawWhite = true;
		Color[] colors = new Color[2];
		colors[0] = new Color(246, 240, 222);
		colors[1] = Color.DARK_GRAY; //new Color(120, 144, 128);
		
		Color[] boardColors = new Color[2];
		//boardColors[0] = new Color(237, 208, 174);
		boardColors[0] = new Color(237, 218, 184);
		boardColors[1] = new Color(207, 178, 144);
		
		
		Color curr = null;
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(boardOffsetX - 1, boardOffsetY - 1, squareSize * 8 + 1, squareSize * 8 + 1);
		g.drawRect(boardOffsetX - 2, boardOffsetY - 2, squareSize * 8 + 3, squareSize * 8 + 3);
		
		for(int x=0; x < 8; x++)
		{
			for(int y=0; y < 8; y++)
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
				int upperLeftx = x * squareSize + boardOffsetX;
				//int upperLefty = (7-y) * squareSize + boardOffsetY;
				int upperLefty = y * squareSize + boardOffsetY;

				g.setColor(curr);
				g.fillRect(upperLeftx, upperLefty, squareSize, squareSize);
				
				int p = game.board.b[x][y];

				int margin = 6;
				int marginX2 = margin * 2;
				
				//g.drawImage(images.get(image).getImage(), upperLeftx + pieceOffsetX, upperLefty + pieceOffsetY, (int) pieceSize, (int) pieceSize, this);
				
				if(p != 0)
				{
					if(p == 1)
					{
						g.setColor(colors[1]);
					}
					else
					{
						g.setColor(colors[0]);
					}
					g.fillOval(upperLeftx + margin, upperLefty + margin, squareSize - marginX2, squareSize - marginX2);
					
					margin = 6;
					marginX2 = margin * 2;
					
					if(p == 1)
					{
//						g.setColor(Color.black);
//						g.drawOval(upperLeftx + margin, upperLefty + margin, squareSize - marginX2, squareSize - marginX2);
					}
					else
					{
						g.setColor(colors[1]);
						g.drawOval(upperLeftx + margin, upperLefty + margin, squareSize - marginX2, squareSize - marginX2);
					}
				}
				
				if(fromPoint != null)
				{
					g.setColor(Color.red);
					
					int ovalSize = 8;
					int halfOval = ovalSize / 2;
					
					g.fillOval(fromPoint.x - halfOval, fromPoint.y - halfOval, ovalSize, ovalSize);
					
					drawArrow(fromPoint, toPoint, 0, 16);
				}
			}

			drawWhite = !drawWhite;
		}
		
		drawMessageIfNeeded(g);
		graphics.drawImage(buffer, 0, 0, null);
	}
	
	public void setMove(Point fromPoint, Point toPoint)
	{
		this.fromPoint = new Point(fromPoint.x * squareSize + halfSquare + boardOffsetX, fromPoint.y * squareSize + halfSquare + boardOffsetY);
		this.toPoint = new Point(toPoint.x * squareSize + halfSquare + boardOffsetX, toPoint.y * squareSize + halfSquare + boardOffsetY);
	}
	
	public void clearMove()
	{
		fromPoint = null;
		toPoint = null;
	}
}
