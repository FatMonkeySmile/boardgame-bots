package com.gamebotarena.chess.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gamebotarena.chess.api.*;
import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.BotInfo;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.GameUiBase;

public class GameUi extends GameUiBase
{
	static int boardSize;
	static int squareSize;
	static int pieceOffsetX;
	static int pieceOffsetY;
	static int pieceSize = 58;
	
	ImageIcon board;

	static final Color arrowColor = new Color(160, 10, 10, 200); 
	
	HashMap<String, ImageIcon> images = new HashMap<String, ImageIcon>();
	HashMap<String, ImageIcon> imageMasks = new HashMap<String, ImageIcon>();

	int boardOffsetX;
	int boardOffsetY;
	
	Game game;
	
	Move move;
	
	public GameUi(Game game)
	{
		this.game = game;
		
		board = new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/board.gif"));
		
		images.put("wqw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wqw.gif")));
		images.put("wkw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wkw.gif")));
		images.put("wbw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wbw.gif")));
		images.put("wrw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wrw.gif")));
		images.put("wnw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wnw.gif")));
		images.put("wpw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wpw.gif")));
		images.put("wqb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wqb.gif")));
		images.put("wkb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wkb.gif")));
		images.put("wbb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wbb.gif")));
		images.put("wrb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wrb.gif")));
		images.put("wnb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wnb.gif")));
		images.put("wpb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/wpb.gif")));
		images.put("bqw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bqw.gif")));
		images.put("bkw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bkw.gif")));
		images.put("bbw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bbw.gif")));
		images.put("bnw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bnw.gif")));
		images.put("bpw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bpw.gif")));
		images.put("bqb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bqb.gif")));
		images.put("bkb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bkb.gif")));
		images.put("bnb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bnb.gif")));
		images.put("bpb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bpb.gif")));


		images.put("brb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/brb.gif")));
		images.put("brw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/brb.gif")));
		images.put("bbb", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bbb.gif")));
		images.put("bbw", new ImageIcon(getResourceAsBytes("com/gamebotarena/chess/core/res/bbb.gif")));
		
		boardSize = 550;
		squareSize = boardSize / 8;
		pieceOffsetX = squareSize / 18;
		pieceOffsetY = squareSize / 24;

		boardOffsetX = 15;
		boardOffsetY = 15;
		
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(new Color(150, 150, 150));
		colors.add(new Color(92, 76, 44));
		setupUi("Chess", this, game.botInfos, true, colors);
	}
	
	public void update(Graphics graphics)
	{
		//todo: Arrow in chess like loa?
		if(stopEarly)
		{
			return;
		}
		
		Graphics2D g = getG();

		boolean drawWhite = true;
		Color[] colors = new Color[2];
		
		colors[0] = new Color(120, 144, 128);
		colors[1] = new Color(246, 240, 222);
		
		//todo: move font up...
		Font font = new Font("Arial", Font.PLAIN, 18);
		
		Color curr = null;
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(boardOffsetX - 1, boardOffsetY - 1, squareSize * 8 + 1, squareSize * 8 + 1);
		
		for(int x=0; x < 8; x++)
		{
			for(int y=0; y < 8; y++)
			{
				if(drawWhite)
				{
					curr = colors[1];
					drawWhite = false;
				}
				else
				{
					curr = colors[0];
					drawWhite = true;
				}

				int upperLeftx = x * squareSize + boardOffsetX;
				//int upperLefty = (7-y) * squareSize + boardOffsetY;
				int upperLefty = y * squareSize + boardOffsetY;

				g.setColor(curr);
				g.fillRect(upperLeftx, upperLefty, squareSize, squareSize);
				char c = game.board.b[x][y];
				if(c != ' ')
				{
					String image;
					if(Character.isLowerCase(c))
					{
						image = "w";
					}
					else
					{
						image = "b";
					}

					image += Character.toLowerCase(c);

					boolean black = true;
					if(x % 2 == 0)
					{
						if(y % 2 == 0)
						{
							black = false;
						}
					}
					else
					{
						if(y % 2 == 1)
						{
							black = false;
						}
					}

					if(black)
					{
						image += "b";
					}
					else
					{
						image += "w";
					}


					if(imageMasks.containsKey(image))
					{
						int xf = -6;
						int yf = 6;
						Composite comp = g.getComposite();
						//g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));
						g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
						g.drawImage(imageMasks.get(image).getImage(), upperLeftx + pieceOffsetX + xf, upperLefty + pieceOffsetY + yf, (int) pieceSize, (int) pieceSize, this);
						g.setComposite(comp);
						g.drawImage(images.get(image).getImage(), upperLeftx + pieceOffsetX + xf, upperLefty + pieceOffsetY + yf, (int) pieceSize, (int) pieceSize, this);
					}
					else
					{
						//g.drawImage(images.get(image).getImage(), upperLeftx + pieceOffsetX, upperLefty + pieceOffsetY, (int) pieceSize, (int) pieceSize, this);
						g.drawImage(images.get(image).getImage(), upperLeftx + pieceOffsetX + 3, upperLefty + pieceOffsetY + 3, this);
					}
				}
			}

			drawWhite = !drawWhite;
		}
		
		if(move != null)
		{
			g.setColor(arrowColor);
			drawArrow(xlatPoint(move.fromx, move.fromy), xlatPoint(move.tox, move.toy), 10, 10);
		}
		
		drawMessageIfNeeded(g);
		graphics.drawImage(buffer, 0, 0, null);
	}
	
	Point xlatPoint(int x, int y)
	{
		int screenX = x * squareSize + boardOffsetX + squareSize / 2;
		int screenY = y * squareSize + boardOffsetY + squareSize / 2;
		
		return new Point(screenX, screenY);
	}
}
