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

public class ReinforcementsStruct
{
	int armies;
	int territory;
	
	public ReinforcementsStruct(int armies, int territory)
	{
		this.armies = armies;
		this.territory = territory;
	}

	public ReinforcementsStruct(int armies, Territory territory)
	{
		this.armies = armies;
		this.territory = territory.getTerritoryNumber();
	}
	
	public int getArmies()
	{
		return armies;
	}

	public void setArmies(int value)
	{
		this.armies = value;
	}
	
	public int getTerritory()
	{
		return territory;
	}
	
	public void setTerritory(int value)
	{
		this.territory = value;
	}
}
