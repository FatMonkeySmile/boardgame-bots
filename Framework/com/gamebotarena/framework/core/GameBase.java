package com.gamebotarena.framework.core;

import java.util.Date;

import javax.swing.JPanel;

public abstract class GameBase
{
	GameUiBase ui;

	long oneMonth = 1000L * 60 * 60 * 24 * 31;
	long elapse = oneMonth * 6;
	// year, mo (0 based...), day
	long start = new Date(108, 2 - 1, 1).getTime();
	
	public void destroy() 
	{
		if(ui != null)
		{
			ui.stopEarly = true;
		}
	}

	public void stop() 
	{
		if(ui != null)
		{
			ui.stopEarly = true;
		}
	}
	
	public void runGames(final String[] args)
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				while(!ui.getStopEarly())
				{
					play();
					
					if(ui.getStopEarly())
					{
						System.exit(1);
					}
					
					if(Misc.isTournament(args))
					{
						start(args);
						break;
					}
				}
			}
		});
		
		t.start();
	}
	
	public abstract void play();
	
	public abstract void start(final String[] args);

	public void setGameUiBase(GameUiBase ui) 
	{
		this.ui = ui;
	}
}
