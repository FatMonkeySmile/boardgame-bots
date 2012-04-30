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

public class Territory 
{
	Territory[] neighbors;
	Board b;
	int i;
	
	public Territory(int i, Board b)
	{
		this.i = i;
		this.b = b;
	}
	
	public String getName()
	{
		return Board.getCountries()[i];
	}
	
	public int getTerritoryNumber()
	{
		return i;
	}
	
	public int getArmies()
	{
		return b.getArmies()[i][1];
	}
	
	public int getPlayer()
	{
		return b.getArmies()[i][0];
	}
	
	public Territory[] getNeighbors()
	{
		if(neighbors == null)
		{
			neighbors = new Territory[Board.getConnections()[i].length];
			
			for(int j=0; j < Board.getConnections()[i].length; j++)
			{
				neighbors[j] = b.getTerritory(Board.getConnections()[i][j]);
			}
		}
		return neighbors;
	}
}
