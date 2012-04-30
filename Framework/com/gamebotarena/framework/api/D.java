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

package com.gamebotarena.framework.api;

import java.io.PrintWriter;
import java.io.Writer;

public class D
{
	//out is just for testing... not thread safe...
	public static Writer out;
	public static final boolean debug = true;
	
	public static boolean debugOn()
	{
		return debug;
	}
	
	public static void out(String s)
	{
		if(debug)
		{
			System.out.println(s);
			
			if(out != null)
			{
				try
				{
					out.write(s + "<br/>");
				}
				catch(Exception e)
				{
				}
			}
		}
	}
	
	public static void out(Throwable t)
	{
		if(debug)
		{
			t.printStackTrace();
			if(out != null)
			{
				try
				{
					t.printStackTrace(new PrintWriter(out));
				}
				catch(Exception e)
				{
				}
			}
		}
	}
}
