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
