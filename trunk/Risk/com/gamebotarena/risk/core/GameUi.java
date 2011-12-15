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

package com.gamebotarena.risk.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.framework.core.GameBase;
import com.gamebotarena.framework.core.GameUiBase;
import com.gamebotarena.framework.core.Misc;
import com.gamebotarena.risk.api.AttackMove;
import com.gamebotarena.risk.api.Board;

public class GameUi extends GameUiBase
{
	Font drawFont1 = new Font("Arial", Font.BOLD, 15);
	Font drawFont2 = new Font("Arial", Font.BOLD, 14);
	Font drawFont3 = new Font("Arial", Font.BOLD, 13);	
	
	Game game;
	
	static final int yFudge = 22;
	
	ImageIcon board;
	AttackMove attack;
	boolean dottedArrow = false;
	int attackPlayer;
	int highlightCountry = -1;
	public static int highlightSize = 30;
	public static int halfHighlight = highlightSize / 2;

	int textOffsetX = -5; //-9;
	int textOffsetY = 5; //-11;

	Color[] colors = new Color[]{
			Color.RED,
			Color.BLUE,
			Color.GREEN,
			Color.YELLOW,
		};			
	
	public GameUi(Game game)
	{
		this.game = game;
		
		try
		{
			byte[] bytes = getResourceAsBytes("com/gamebotarena/risk/core/res/Board.gif");
			
			if(bytes != null)
			{
				board = new ImageIcon(bytes);
			}
			else
			{
				board = new ImageIcon();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			board = new ImageIcon();
		}

		//setLayout(new BorderLayout());
		//add(new JLabel(board), BorderLayout.CENTER);

		ArrayList<Color> cs = new ArrayList<Color>();
		
		for(Color c : colors)
		{
			cs.add(Misc.darken(c, 35));
		}
		
		setupUi("Risk", this, game.botInfos, false, cs);
	}
	
	public void update(Graphics graphics)
	{
		if(stopEarly)
		{
			return;
		}
		
		Graphics2D g = getG();
		
		board.paintIcon(this, g, 0, -100);
		{
			int currentTextOffsetX;
			int currentTextOffsetY;

			Color shadow = Color.black;
			Color[] drawColor = new Color[5];
			drawColor[0] = colors[0];
			drawColor[1] = colors[1];
			drawColor[2] = colors[2];
			if(game.numPlayers > 3)
			{
				drawColor[3] = Color.YELLOW;
				if(game.numPlayers > 4)
				{
					drawColor[4] = new Color(255, 0, 255);
				}
			}

			for (int i = 0; i < Board.NUM_COUNTRIES; i++)
			{
				int x = Board.getCountryLocations()[i][0];
				int y = Board.getCountryLocations()[i][1];
				try
				{
					if(game.board.getArmies()[i][0] > 0)
					{
						int armies = game.board.getArmies()[i][1];
						Font drawFont;

						if(armies < 10)
						{
							drawFont = drawFont1;
							currentTextOffsetX = textOffsetX;
							currentTextOffsetY = textOffsetY;
						}
						else if(armies < 100)
						{
							drawFont = drawFont2;
							currentTextOffsetX = textOffsetX - 3;
							currentTextOffsetY = textOffsetY;
						}
						else if(armies < 1000)
						{
							drawFont = drawFont3;
							currentTextOffsetX = textOffsetX - 6;
							currentTextOffsetY = textOffsetY;
						}
						else 
						{
							drawFont = drawFont3;
							currentTextOffsetX = textOffsetX - 10;
							currentTextOffsetY = textOffsetY;
						}

						int x1 = x + currentTextOffsetX;
						int y1 = y + currentTextOffsetY;
						
						Color color = drawColor[game.board.getArmies()[i][0] - 1];
						Color color2 = color;

						color2 = new Color(Math.max(0, color.getRed() - 50), 
							      Math.max(0, color.getGreen() - 50), 
							      Math.max(0, color.getBlue() - 50));
						
						Color trans = new Color(color.getRed(), color.getGreen(), color.getBlue(), 90);
						Color transShadow = new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(), 90);

						boolean attck = false;
						if(attack != null && (i == attack.getTo() || i == attack.getFrom()))
						{
							attck = true;
						}
						
						if(attck)
						{
							transShadow = new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(), 200);
							trans = new Color(trans.getRed(), trans.getGreen(), trans.getBlue(), 200);
						}
						
						if(highlightCountry == i)
						if(true)
						{
							transShadow = Color.BLACK;
							trans = new Color(trans.getRed(), trans.getGreen(), trans.getBlue(), 200);
						}
						
						g.setColor(trans);
						g.fillOval(x - halfHighlight, y - halfHighlight, highlightSize, highlightSize);
						g.setColor(transShadow);
						g.drawOval(x - halfHighlight, y - halfHighlight, highlightSize, highlightSize);
						
						g.setColor(shadow);
						g.setFont(drawFont);
						g.drawString("" + armies, x1 + 1, y1 + 1);
						
						g.setColor(color2);
						g.drawString("" + armies, x1, y1);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}

			if(attack != null)
			{
				Color c;
				if(dottedArrow)
				{
					c = drawColor[attackPlayer - 1];
				}
				else
				{
					c = Color.black;
				}
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 200));
				if(attack.getFrom() == 25 && attack.getTo() == 0)
				{
					float[] i = getPoints(0,
						Board.getCountryLocations()[attack.getFrom()][1],
						Board.getCountryLocations()[attack.getTo()][0],
						Board.getCountryLocations()[attack.getTo()][1]);

					//- 1 * 2 then +1 if y
					try
					{
						g.drawLine(0, (int) i[5], (int) i[8], (int) i[9]);
						Point[] points = new Point[]{new Point((int) i[6], (int) i[7]), new Point((int) i[12], (int) i[13]), new Point((int) i[10], (int) i[11])};
						
						int size = points.length;
						int [] xs = new int[size];
						int [] ys = new int[size];
						
						for(int index = 0; index < size; index++)
						{
							xs[index] = points[index].x;
							ys[index] = points[index].y;
						}
						
						g.fillPolygon(xs, ys, size);

						i = getPoints(Board.getCountryLocations()[attack.getFrom()][0],
							Board.getCountryLocations()[attack.getFrom()][1],
							getWidth(),
							Board.getCountryLocations()[attack.getTo()][1]);
						
						g.drawLine((int) i[4], (int) i[5], getWidth(), (int) i[9]);
					}
					catch(Exception e)
					{
						D.out("Invalid coordinates in drawline*********************");
						D.out("x3 = " + i[4] + ", y3 = " + i[5] + ", x4 = " + i[6] + ", x5 = " + i[8]);
					}
				}
				else if(attack.getFrom() == 0 && attack.getTo() == 25)
				{
					float[] i;
					i = getPoints(getWidth(),
						Board.getCountryLocations()[attack.getFrom()][1],
						Board.getCountryLocations()[attack.getTo()][0],
						Board.getCountryLocations()[attack.getTo()][1]);
					//- 1 * 2 then +1 if y
					try					
					{
						g.drawLine((int) i[4], (int) i[5], (int) i[8], (int) i[9]);
						Point[] points = new Point[]{new Point((int) i[6], (int) i[7]), new Point((int) i[12], (int) i[13]), new Point((int) i[10], (int) i[11])};

						int size = points.length;
						int [] xs = new int[size];
						int [] ys = new int[size];
						
						for(int index = 0; index < size; index++)
						{
							xs[index] = points[index].x;
							ys[index] = points[index].y;
						}
						
						g.fillPolygon(xs, ys, size);

						i = getPoints(Board.getCountryLocations()[attack.getFrom()][0],
							Board.getCountryLocations()[attack.getFrom()][1],
							0,
							Board.getCountryLocations()[attack.getTo()][1]);
						
						g.drawLine((int) i[4], (int) i[5], 0, (int) i[9]);
					}
					catch(Exception e)
					{
						D.out("Invalid coordinates in drawline*********************");
						D.out("x3 = " + i[4] + ", y3 = " + i[5] + ", x4 = " + i[6] + ", x5 = " + i[8]);
					}
				}
				else
				{
					float[] i = getPoints(Board.getCountryLocations()[attack.getFrom()][0],
						Board.getCountryLocations()[attack.getFrom()][1],
						Board.getCountryLocations()[attack.getTo()][0],
						Board.getCountryLocations()[attack.getTo()][1]);

					//- 1 * 2 then +1 if y
					try
					{
						g.drawLine((int) i[4], (int) i[5], (int) i[8], (int) i[9]);
						Point[] points = new Point[]{new Point((int) i[6], (int) i[7]), new Point((int) i[12], (int) i[13]), new Point((int) i[10], (int) i[11])};
						int size = points.length;
						int [] xs = new int[size];
						int [] ys = new int[size];
						
						for(int index = 0; index < size; index++)
						{
							xs[index] = points[index].x;
							ys[index] = points[index].y;
						}
						
						g.fillPolygon(xs, ys, size);
					}
					catch(Exception e)
					{
						D.out("Invalid coordinates in drawline*********************");
						D.out("x3 = " + i[4] + ", y3 = " + i[5] + ", x4 = " + i[6] + ", x5 = " + i[8]);
					}
				}
			}

			drawMessageIfNeeded(g);
		}
		
		graphics.drawImage(buffer, 0, 0, null);
	}

	public float[] getPoints(int x1, int y1, int x2, int y2)
	{
		int xDiff = x2 - x1;
		int yDiff = y2 - y1;

		int lineMargin = 11;

		float m = ((float) (yDiff)) / (xDiff);
		float mperp = -1 / m;

		float marginDistanceX = crawlLineX(m, lineMargin);

		if(x2 < x1)
		{
			marginDistanceX = -marginDistanceX;
		}

		float x3 = x1 + marginDistanceX;
		float y3 = y1 + marginDistanceX * m;
		float x4 = x2 - marginDistanceX;
		float y4 = y2 - marginDistanceX * m;

		float x5 = x4 - marginDistanceX;
		float y5 = y4 - marginDistanceX * m;

		int arrowHalfWidth = 5;
				
		float arrowHalfDistanceX = crawlLineX(mperp, arrowHalfWidth);

		float x6 = x5 + arrowHalfDistanceX;
		float y6 = y5 + arrowHalfDistanceX * mperp;

		float x7 = x5 - arrowHalfDistanceX;
		float y7 = y5 - arrowHalfDistanceX * mperp;

		return new float[]{ x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, x6, y6, x7, y7 };
	}
	
	public void showBoardPlain()
	{
		this.attack = null;
		this.highlightCountry = -1;
		super.showBoard();
	}

	public void showArrow(int attackPlayer, AttackMove attack)
	{
		this.highlightCountry = -1;
		this.attackPlayer = attackPlayer;
		this.attack = attack;
		dottedArrow = false;
		showBoard();
	}

	public void showDottedArrow(int attackPlayer, AttackMove attack)
	{
		this.highlightCountry = -1;
		this.attackPlayer = attackPlayer;
		this.attack = attack;
		dottedArrow = true;
		showBoard();
	}

	public void showHighlight(int country, boolean hurry)
	{
        this.highlightCountry = country;
        this.attack = null;
		showBoard(hurry);
	}

	public float crawlLineX(float m, int c)
	{
		return (float) Math.sqrt((c * c) / (1 + m * m));
	}
}
