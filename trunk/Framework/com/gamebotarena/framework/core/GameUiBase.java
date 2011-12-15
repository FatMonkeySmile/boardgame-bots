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

package com.gamebotarena.framework.core;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gamebotarena.framework.api.D;

public class GameUiBase extends Canvas
{
	//static final boolean fast = true;
	static final boolean fast = false;
	
	protected boolean multiPaint = false;
	static JFrame lastFrame;
	protected boolean isPainting;
	protected boolean stopEarly;
	protected static int speed = 5;
	protected String message;
	int lastWidth = -1;
	int lastHeight = -1;
	Graphics2D g;
	protected Image buffer;
	Font messageFont = new Font("Arial", Font.ITALIC, 48);
	Color messageColor = new Color(180, 20, 50, 150);
	public boolean okToKill = false;
	
	protected boolean dontPause = false;
	protected boolean side;

	protected Container outerPanel = null;
	protected Container panel = null;
	
	protected void fillBackground()
	{
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());		
	}
	
	protected void drawMessageIfNeeded(Graphics2D g)
	{
		if(message != null)
		{
			g.setColor(messageColor);
			g.setFont(messageFont);
			if(side)
			{
				g.drawString(message, 225, 279);
			}
			else
			{
				g.drawString(message, 375, 279);
			}
		}
	}
	
	public Color setAlpha(Color c, int alpha)
	{
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	public Graphics2D getG()
	{
		if(lastWidth != getWidth() || lastHeight != getHeight())
		{
			buffer = createImage(getWidth(), getHeight());
			g = (Graphics2D) buffer.getGraphics();
			lastHeight = getHeight();
			lastWidth = getWidth();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		return g;
	}
	
	public void setupUi(String gameName, Canvas gamePanel, ArrayList botInfos, boolean side, ArrayList colors)
	{
		Color backgroundColor = gamePanel.getBackground();
		boolean setBack = false;
		this.side = side;
		gamePanel.setBackground(new Color(238, 238, 238));
		JFrame frame = null;
		
		int x = 815;
		int y = 610;
		if(lastFrame != null)
		{
			x = lastFrame.getWidth();
			y = lastFrame.getHeight();
			lastFrame.setVisible(false);
			lastFrame.dispose();
			//lastFrame.getContentPane().removeAll();
		}
		//else
		{
			lastFrame = frame = new JFrame();
			frame.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e) 
				{
					stopEarly = true;
					if(message != null || speed == 0 || okToKill)
					{
						System.exit(1);
					}
				}
			});
			
			frame.setSize(x, y);
			lastFrame.setTitle(gameName);
		}
		outerPanel = lastFrame.getContentPane();
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(gamePanel, BorderLayout.CENTER);
		
		outerPanel.setLayout(new BorderLayout());
		outerPanel.add(panel, BorderLayout.CENTER);
		
		JPanel nonGamePanel = new JPanel();
		
		final JSlider slider = new JSlider();
		slider.setMaximum(10);
		slider.setMinimum(0);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		if(setBack)
		{
			slider.setBackground(backgroundColor);
		}
		
		slider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				speed = slider.getValue();
			}
		});
		
		slider.setValue(speed);
		
		nonGamePanel.setLayout(new BorderLayout());
		
		if(side)
		{
			nonGamePanel.add(slider, BorderLayout.NORTH);
			
			JPanel playerInfoPanel = new JPanel();
			playerInfoPanel.setLayout(new GridLayout(2, 1));
			for(int i=0; i < botInfos.size(); i++)
			{
				Color c = null;
				if(colors != null)
				{
					c = (Color) colors.get(i);
				}
				JPanel botInfoComp = ((BotInfo) botInfos.get(i)).getBotInfoPanel(true, c, i + 1);
				if(setBack)
				{
					botInfoComp.setBackground(backgroundColor);
					
					Component[] comps = botInfoComp.getComponents();
					for(int j=0; j < comps.length; j++)
					{
						Component comp = comps[j];						
						comp.setBackground(backgroundColor);
					}
				}
				playerInfoPanel.add(botInfoComp);
			}
			
			nonGamePanel.add(playerInfoPanel, BorderLayout.CENTER);
			panel.add(nonGamePanel, BorderLayout.EAST);
			if(setBack)
			{
				//setForeground(Color.lightGray);
				playerInfoPanel.setBackground(backgroundColor);
				nonGamePanel.setBackground(backgroundColor);
				nonGamePanel.setBorder(BorderFactory.createRaisedBevelBorder());
				panel.setBackground(backgroundColor);
				//slider.setPaintTrack(false);
				//slider.setUI();
			}
		}
		else
		{
			nonGamePanel.add(slider, BorderLayout.EAST);
			
			JPanel playerInfoPanel = new JPanel();
			for(int i=0; i < botInfos.size(); i++)
			{
				Color c = null;
				if(colors != null)
				{
					c = (Color) colors.get(i);
				}
				playerInfoPanel.add(((BotInfo) botInfos.get(i)).getBotInfoPanel(false, c, i + 1));
			}
			
			nonGamePanel.add(playerInfoPanel, BorderLayout.CENTER);
			panel.add(nonGamePanel, BorderLayout.NORTH);
		}
		
		if(frame != null)
		{
			frame.setVisible(true);
		}
	}
	
	public void drawArrow(Point p1, Point p2, int lineMargin, int arrowSize)
	{
		drawArrow(p1, p2, lineMargin, arrowSize, false);
	}
	
	public void drawArrow(Point p1, Point p2, int lineMargin, int arrowSize, boolean dashed)
	{
		Graphics2D g = getG();
		
		float[] i = getPoints(p1.x, p1.y, p2.x, p2.y, lineMargin, arrowSize);

		//- 1 * 2 then +1 if y
		try
		{
			Stroke s = null;
			if(dashed)
			{
				s = g.getStroke();
				g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{ 10 }, 0));
			}
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

			if(s != null)
			{
				g.setStroke(s);
			}
			g.fillPolygon(xs, ys, size);
		}
		catch(Exception e)
		{
			D.out("Invalid coordinates in drawline*********************");
			D.out("x3 = " + i[4] + ", y3 = " + i[5] + ", x4 = " + i[6] + ", x5 = " + i[8]);
		}		
	}
	
	public float[] getPoints(int x1, int y1, int x2, int y2, int lineMargin, int arrowSize)
	{
		int xDiff = x2 - x1;
		int yDiff = y2 - y1;

		float m = ((float) (yDiff)) / (xDiff);
		if(m == Float.NEGATIVE_INFINITY)
		{
			m = - Float.MAX_VALUE;
		}
		
		if(m == Float.POSITIVE_INFINITY)
		{
			m = Float.MAX_VALUE;
		}
		
		double mperp = -1 / m;
		
		if(mperp == Float.NEGATIVE_INFINITY)
		{
			mperp = - Float.MAX_VALUE;
		}
		
		if(mperp == Float.POSITIVE_INFINITY)
		{
			mperp = Float.MAX_VALUE;
		}

		float marginDistanceX = crawlLineX(m, lineMargin);
		float arrowDistanceX = crawlLineX(m, arrowSize);

		if(x2 < x1)
		{
			marginDistanceX = -marginDistanceX;
			arrowDistanceX = -arrowDistanceX;
		}

		float x3 = x1 + marginDistanceX;
		float y3 = (float) (y1 + marginDistanceX * m);
		float x4 = x2 - marginDistanceX;
		float y4 = (float) (y2 - marginDistanceX * m);

		float x5 = x4 - arrowDistanceX;
		float y5 = (float) (y4 - arrowDistanceX * m);

		int arrowHalfWidth = arrowSize / 2;
				
		float arrowHalfDistanceX = crawlLineX(mperp, arrowHalfWidth);

		float x6 = x5 + arrowHalfDistanceX;
		float y6 = (float) (y5 + arrowHalfDistanceX * mperp);

		float x7 = x5 - arrowHalfDistanceX;
		float y7 = (float) (y5 - arrowHalfDistanceX * mperp);

		return new float[]{ x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, x6, y6, x7, y7 };
	}
	
	public float crawlLineX(double m, int c)
	{
		return (float) Math.sqrt((c * c) / (1 + m * m));
	}
	
	public byte[] getResourceAsBytes(String resource)
	{
		InputStream is = null;
		try
		{
			if(resource == null)
			{
				return null;
			}
			is = getClass().getClassLoader().getResourceAsStream(resource);
			
			if(is == null)
			{
				return null;
			}
			
			int buffSize = 4096;
			byte[] bytes = new byte[0];
			byte[] buff = new byte[buffSize];
			int read;
			int at = 0;
			while((read = is.read(buff)) > 0)
			{
				byte[] newbytes = new byte[bytes.length + read];
				for(int i=0; i < bytes.length; i++)
				{
					newbytes[i] = bytes[i];
				}
				for(int i=0; i < read; i++)
				{
					newbytes[i + at] = buff[i];
				}
				
				bytes = newbytes;
				
				at += read;
			}
			
			return bytes;
		}
		catch(Exception e)
		{
			D.out(e);
			return null;
		}
		finally
		{
			try
			{
				is.close();
			}
			catch(Exception e2)
			{
				//ignore
			}
		}
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public void gameOver(String message)
	{
		this.message = message;
        if(stopEarly)
        {
        	System.exit(1);
        }
        else
        {
        	showBoard();
        	try
        	{
        		if(!fast)
        		{
        			Thread.sleep(3000);
        		}
        		else
        		{
        			Thread.sleep(20);
        		}
        	}
        	catch(Exception e)
        	{
        		D.out(e);
        	}
        	showBoard();
        	this.message = null;
        }
	}
	
	public void paint(Graphics graphics) 
	{
		update(graphics);
	}	
	
	public boolean getStopEarly()
	{
		return stopEarly;
	}
	
	public void showBoard()
	{
		showBoard(false);
	}
	
	public void showBoard(boolean hurry)
	{
		if(pause(hurry))
		{
			//repaint(0, 0, getWidth(), getHeight());
		}
	}
	
	public boolean pause(boolean hurry)
	{
		if(stopEarly || dontPause)
		{
			return false;
		}

		try
		{
			//Get a snapshot of speed since it may change in a different thread.
			int localSpeed = getSpeed();
			
			if(localSpeed == 0)
			{
				repaint(0, 0, getWidth(), getHeight());				
			}
			
			while(localSpeed == 0 && !stopEarly)
			{
				Thread.sleep(300);
				localSpeed = getSpeed();
			}
	
			if(hurry && localSpeed > 1)
			{
//				if(localSpeed == 10)
//				{
//					return false;
//				}
//				else
//				{
					repaint(0, 0, getWidth(), getHeight());
					Thread.sleep(220 - (localSpeed * 20));
					return true;
//				}
			}

//			if(localSpeed == 10)
//			{
//				if(message == null)
//				{
//					return false;
//				}
//				
//				repaint(0, 0, getWidth(), getHeight());
//				return true;
//			}
	
			if(localSpeed != 0)
			{
				int wait = 10 - localSpeed;
				wait *= 100;
				if(wait <= 5)
				{
					wait /= 3;
				}
				
				if(multiPaint)
				{
					wait /= 2.5;
				}
				
				repaint(0, 0, getWidth(), getHeight());
				if(wait <= 0) {
					wait = 20;
				}
				Thread.sleep(wait);
			}

			while(isPainting)
			{
				Thread.sleep(100);
			}
			return true;
		}
		catch(Exception e)
		{
			D.out(e);
			return false;
		}
	}
}
