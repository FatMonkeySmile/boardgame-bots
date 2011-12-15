package com.gamebotarena.hex.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.GameUiBase;
import com.gamebotarena.hex.api.Board;

public class GameUi extends GameUiBase
{
	Game game;
	ArrayList<Color> colors = new ArrayList<Color>();
	//Color frame = new Color(220, 220, 220);
	Color frame = new Color(200, 220, 200, 50);
	Color pieceFrame = new Color(80, 80, 80, 20);
	
	public GameUi(Game game)
	{
		this.game = game;
		
		colors.add(new Color(220, 30, 30));
		colors.add(new Color(30, 30, 220));
		
		setupUi("Hex", this, game.botInfos, true, colors);

		ArrayList<Color> c2 = new ArrayList<Color>();
		c2.add(Color.black);
		
		for(int i=0; i < 2; i++)
		{
			Color c = colors.get(i);
			c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 30);
			c2.add(c);
		}
		
		colors = c2;
	}
	
	public void drawPieceSide(Graphics g, Color c, int[] xt, int[] yt, boolean connection, boolean connection2, int piece)
	{
		g.setColor(c);
		boolean darken = false;
		if(message != null)
		{
			int winner = 2;
			if(game.redWins)
			{
				winner = 1;
			}
			
			if(piece == winner && connection && connection2)
			{
				darken = true;
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 140));
			}
		}
		g.fillPolygon(xt, yt, Math.min(6, xt.length));
		
		//if(xt.length > 4)
		{
			g.setColor(pieceFrame);
			//if(darken)
			//{
			//	g.setColor(Color.gray);
			//}

			g.drawPolygon(xt, yt, Math.min(6, xt.length));
		}
	}
	
	public void update(Graphics graphics)
	{
		isPainting = true;
		try
		{
		if(stopEarly)
		{
			return;
		}
		
		if(colors.size() < 3 || game == null || game.board == null)
		{
			return;
		}
		
		int last = -1;
		
		try
		{
			Graphics2D g = getG();

			fillBackground();
			int xMargin = 5;
			int size = Math.min(getWidth() - (xMargin * 2), getHeight());
			
			int hexSize = size / (Board.getSize() + 4);
			int halfHex = hexSize / 2;
			double diff = Math.sqrt(Math.pow(hexSize, 2) / 2);
			double yDiff = halfHex;

			int x = xMargin + halfHex;
			int y = getHeight() / 2;
			double diff2 = diff - halfHex;
			
			int ySkew = 5;
			int xSkew = 1;
			
			for(int ax = 0; ax < 2; ax++)
			{
				for(int i=0; i < Board.SIZE; i++)
				{
					for(int j=0; j < Board.SIZE; j++)
					{
						int x1 = (int) (x + (diff * i) + (diff * j));
						int y1 = (int) (y - (yDiff * i) + (yDiff * j));
						
						last = game.board.b[i][j];
						
						int[] xs = new int[7];
						int[] ys = new int[7];
						
						xs[0] = x1 - halfHex;
						ys[0] = y1;
						
						xs[1] = (int) (x1 - diff2);
						ys[1] = y1 - halfHex;
						
						xs[2] = (int) (x1 + diff2);
						ys[2] = y1 - halfHex;
						
						xs[3] = x1 + halfHex;
						ys[3] = y1;
						
						xs[4] = (int) (x1 + diff2);
						ys[4] = y1 + halfHex;
						
						xs[5] = (int) (x1 - diff2);
						ys[5] = y1 + halfHex;
						
						xs[6] = x1 - halfHex;
						ys[6] = y1;
						
						g.setColor(frame);
						if(ax == 0)
						{
							/*
							g.setColor(Color.gray);
							g.setFont(new Font("Arial", Font.PLAIN, 12));
							int xDiff = 9;
							if(i > 9)
							{
								xDiff += 3;
							}
							if(j > 9)
							{
								xDiff += 3;
							}
							g.drawString("" + i + "," + j, x1 - xDiff, y1 + 2);
							g.setColor(frame);
							*/
							
							g.drawPolygon(xs, ys, 6);
							
							if(i == 0)
							{
								g.setColor(colors.get(2));
								g.setColor(new Color(g.getColor().getRed(),
										g.getColor().getGreen(),
										g.getColor().getBlue() - 100,
										70));
								g.drawLine(xs[5], ys[5], xs[0], ys[0]);
								g.drawLine(xs[5], ys[5], xs[4], ys[4]);
							}
							
							if(i == Board.SIZE - 1)
							{
								g.setColor(colors.get(2));
								g.setColor(new Color(g.getColor().getRed(),
										g.getColor().getGreen(),
										g.getColor().getBlue() - 100,
										70));
								g.drawLine(xs[1], ys[1], xs[2], ys[2]);
								g.drawLine(xs[2], ys[2], xs[3], ys[3]);								
							}
							
							if(j == 0)
							{
								g.setColor(colors.get(1));
								g.setColor(new Color(g.getColor().getRed() - 10,
										g.getColor().getGreen(),
										g.getColor().getBlue(),
										70));
								g.drawLine(xs[0], ys[0], xs[1], ys[1]);
								g.drawLine(xs[1], ys[1], xs[2], ys[2]);
							}
							
							if(j == Board.SIZE - 1)
							{
								g.setColor(colors.get(1));
								g.setColor(new Color(g.getColor().getRed() - 10,
										g.getColor().getGreen(),
										g.getColor().getBlue(),
										70));
								g.drawLine(xs[3], ys[3], xs[4], ys[4]);
								g.drawLine(xs[4], ys[4], xs[5], ys[5]);								
							}
						}
						else
						{
							if(game.board.b[i][j] != 0)
							{
								int[] xt = new int[7];
								int[] yt = new int[7];
								
								for(int k=0; k < 7; k++)
								{
									xt[k] = xs[k] - xSkew;
									yt[k] = ys[k] - ySkew;
								}
		
								for(int k=0; k < 6; k++)
								{
									int[] xa = new int[4];
									int[] ya = new int[4];
									
									xa[0] = xt[k];
									xa[1] = xs[k];
									xa[2] = xs[k + 1];
									xa[3] = xt[k + 1];
		
									ya[0] = yt[k];
									ya[1] = ys[k];
									ya[2] = ys[k + 1];
									ya[3] = yt[k + 1];
									
									drawPieceSide(g, colors.get(game.board.b[i][j]), xa, ya, game.board.c[i][j], game.board.c2[i][j], game.board.b[i][j]);
								}
						
								Color topColor = colors.get(game.board.b[i][j]);
								topColor = new Color(topColor.getRed(), topColor.getGreen(), topColor.getBlue(), 80);
								drawPieceSide(g, topColor, xt, yt, game.board.c[i][j], game.board.c2[i][j], game.board.b[i][j]);
							}
						}
					}
				}
			}
			
			drawMessageIfNeeded(g);
			graphics.drawImage(buffer, 0, 0, null);
		}
		catch(Exception e)
		{
			D.out(e);
			D.out(":" + last);			
		}
		}
		finally
		{
			isPainting = false;
		}
	}
}
