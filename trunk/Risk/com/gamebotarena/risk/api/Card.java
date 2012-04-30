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

package com.gamebotarena.risk.api;

public class Card 
{
	public static int TYPE_WILD = 0;
	public static int TYPE_INFANTRY = 1;
	public static int TYPE_CALVARY = 2;
	public static int TYPE_CANNON = 3;
	
	public static final int NUM_CARDS = 44;
	
	int territory;
	int type;

	public Card(int territory, int type)
	{
		this.territory = territory;
		this.type = type;
	}
	
	public int getTerritory()
	{
		return territory;
	}
	
	public int getType()
	{
		return type;
	}
}
