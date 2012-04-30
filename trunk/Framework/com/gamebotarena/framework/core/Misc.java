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

package com.gamebotarena.framework.core;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import com.gamebotarena.framework.api.D;

public class Misc 
{
	public static Random rand = new Random();
	static Hashtable tournCache = new Hashtable();
	
	public static BotInfo loadFromJar(GameBase base, String jarName, boolean loadBot)
	{
		int size = 0;
		long date = 0;
		String id = jarName.substring(0, jarName.length() - ".jar".length());
		
		String fileName = id;
		fileName += ".xml";
		fileName = fileName.replace("\\", "/");
		int last = fileName.lastIndexOf("/");
		if(last != -1)
		{
			fileName = fileName.substring(last + 1);
		}

		try
		{
			File file = new File(jarName);
			size = (int) file.length();
			date = file.lastModified();
			if(!file.exists() || !file.isFile())
			{
				D.out("Cannot find game bot jar file:" + jarName);
				throw new Exception("Unable to load game bot jar file.");
			}
			URL url = new File(jarName).toURL();

			ClassLoader loader = loadLoader(base, url);
		   
		   InputStream xmlInputStream = null;
		   URL xmlFileURL = loader.getResource(fileName);
		   
		   if(xmlFileURL != null)
		   {
			   xmlInputStream = xmlFileURL.openStream();
		   }

		   if(xmlInputStream == null)
		   {
			   D.out("Cannot find " + fileName + " game bot descriptor file in jar file:" + jarName);
				throw new Exception("Unable to load game bot xml file.");
		   }
		   
		   BotInfo botInfo = new BotInfo();
		   botInfo.readXml(xmlInputStream, id);

		   botInfo.size = size;
		   botInfo.date = date;
		   xmlInputStream.close();
		   if(loadBot)
		   {
			   botInfo.bot = loader.loadClass(botInfo.getClassName()).newInstance();
		   }
		   return botInfo;
		}
		catch(Exception e)
		{
			D.out(e);
			return null;
		}
	}
	
	public static ArrayList getBots(GameBase base, String[] args, int num, String random) throws Exception
	{
		if(isTournament(args))
		{
			String dir = args[1];
			File fileDir = new File(dir);
			if(fileDir.exists() && fileDir.isDirectory() && !tournCache.isEmpty())
			{
				File results = new File(fileDir, "results.xml");
				Writer fw = null;
				
				try
				{
					fw = new BufferedWriter(new FileWriter(results));
					fw.write("<results>\r\n");
					Set keys = tournCache.keySet();
					Iterator iter = keys.iterator();
					while(iter.hasNext())
					{
						String key = (String) iter.next();
						BotInfo botInfo = (BotInfo) tournCache.get(key);
						
						fw.write("   <bot id=\"" + botInfo.getId() + "\">\r\n");
						fw.write("      <wins>" + botInfo.wins + "</wins>\r\n");
						fw.write("      <draws>" + botInfo.draws + "</draws>\r\n");
						fw.write("      <played>" + botInfo.played + "</played>\r\n");
						fw.write("   </bot>\r\n");
					}
					fw.write("</results>");
				}
				catch(Exception e)
				{
					D.out(e);
				}
				finally
				{
					try
					{
						fw.close();
					}
					catch(Exception e)
					{
						//ignore.
					}
				}
			}
			
			File[] children = fileDir.listFiles();
			ArrayList allBots = new ArrayList();
			ArrayList bots = new ArrayList();
			for(int i=0; i < children.length; i++)
			{
				if(!children[i].getName().equals("results.xml"))
				{
					allBots.add(children[i]);
				}
			}
			for(int i=0; i < num && allBots.size() > 0; i++)
			{
				int r = rand.nextInt(allBots.size());
				bots.add(((File) allBots.get(r)).getAbsolutePath());
				allBots.remove(r);
			}
			args = (String[]) bots.toArray(new String[]{});
		}
		
		ArrayList bots = new ArrayList();
		for(int i=0; i < num; i++)
		{
			String name = null;
			
			if(i < args.length)
			{
				name = args[i];
			}
			
			if(name != null && name.equals("[RandomBot]"))
			{
				name = null;
			}
			
			if(name != null && name.startsWith("-"))
			{
				continue;
			}
			
			if(name == null)
			{
				BotInfo botInfo = new BotInfo();
				botInfo.bot = Class.forName(random).newInstance();
				botInfo.id = "RandomBot";
				botInfo.name = "RandomBot";
				botInfo.version = "1.0";
				botInfo.author = "GameBotArena";
				botInfo.country = "USA";
				botInfo.className = random;
				bots.add(botInfo);
			}
			else
			{
				BotInfo botInfo = (BotInfo) tournCache.get(name);
				if(botInfo == null)
				{
					if(name.toLowerCase().endsWith(".jar") || name.toLowerCase().endsWith(".zip"))
					{
						botInfo = loadFromJar(base, name, true);
					}
					else
					{
						botInfo = new BotInfo();
						File xmlFile = new File(name);
						if(!xmlFile.exists())
						{
							if(name.toLowerCase().endsWith(".xml"))
							{
								D.out("Cannot find game bot xml descriptor file:" + xmlFile.getAbsolutePath());
								throw new Exception("Unable to load game bot xml file.");
							}

							try
							{
								Class cl = Class.forName(name);
								botInfo.bot = cl.newInstance();
								botInfo.name = cl.getName();
								int at = botInfo.name.lastIndexOf('.');
								if(at != -1 && at + 1 < botInfo.name.length())
								{
									botInfo.name = botInfo.name.substring(at + 1);
								}
								
								botInfo.id = botInfo.name;
								botInfo.version = "0.0";
								botInfo.author = "";
								botInfo.country = "";
							}
							catch(Exception e)
							{
								e.printStackTrace();
								D.out("Cannot find game bot class file:" + name);
								throw new Exception("Unable to load game bot class file.");
							}
						}
						else
						{
							botInfo.readXml(name);
							//todo: Use sandboxed loader
							
							File dir = xmlFile.getParentFile();
							
							if(!dir.exists() || !dir.isDirectory())
							{
								D.out("Cannot find directory to add to classpath:" + dir.getAbsolutePath());
								throw new Exception("Unable to find directory to add to classpath.");
							}
							
							ClassLoader loader = loadLoader(null, dir.toURL());
							
							botInfo.bot = loader.loadClass(botInfo.getClassName()).newInstance();
							//botInfo.bot = Class.forName(botInfo.getClassName()).newInstance();
						}
					}
					
					tournCache.put(name, botInfo);
				}
				bots.add(botInfo);				
			}
		}
		
		return bots;
	}
	
	private static ClassLoader loadLoader(GameBase base, URL url)
	{
		ClassLoader origLoader;
		
		if(base != null)
		{
			origLoader = base.getClass().getClassLoader();
		}
		else
		{
			try
			{
				origLoader = Class.forName("com.gamebotarena.framework.core.Misc").getClassLoader();
			}
			catch(Exception e)
			{
				D.out(e);
				origLoader = ClassLoader.getSystemClassLoader();
			}
		}
		
		ClassLoader loader;
		try
		{
			loader = new URLClassLoader(new URL[]{ url }, origLoader);
		}
		catch(Exception e)
		{
			loader = origLoader;
		}
		
		return loader;
	}

	public static Color darken(Color c, int i) 
	{
		return new Color(
				Math.max(0, c.getRed() - i),
				Math.max(0, c.getGreen() - i),
				Math.max(0, c.getBlue() - i));
	}
	
	public static boolean isTournament(String[] args)
	{
		if(args != null)
		{
			for(int i=0; i < args.length; i++)
			{
				if(args[i].equals("-tourn"))
				{
					return true;
				}
			}
		}
		return false;
	}
}
