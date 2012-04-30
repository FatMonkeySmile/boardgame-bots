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

public class Board
{
	Territory[] terr;
	
	public Board()
	{
		terr = new Territory[NUM_TERRITORIES];
		
		armies = new int[NUM_COUNTRIES][];
		for (int i = 0; i < NUM_COUNTRIES; i++)
		{
			armies[i] = new int[2];
		}
		
		for (int i = 0; i < NUM_COUNTRIES; i++)
		{
			terr[i] = new Territory(i, this);
		}
	}
	
	public static final int NUM_TERRITORIES = 42;
	
	/**
	 * @deprecated NUM_TERRITORIES
	 */
	public static final int NUM_COUNTRIES = NUM_TERRITORIES;
	
	public Territory getTerritory(int i)
	{
		return terr[i];
	}
	
	public Territory[] getTerritories()
	{
		return terr;
	}

	/**
	 * @deprecated Territory.getName()
	 */
	public static String[] getCountries()
	{
		return countries;
	}
	
	static String[] countries = new String[]{
			 "Alaska", 
			 "Northwest Territory",
			 "Alberta", 
			 "Greenland",
			 "Ontario", 
			 "Western United States", 
			 "Eastern United States", 
			 "Quebec", 
			 "Central America", 
			 "Iceland", 
			 "Scandinavia", 
			 "Great Britain", 
			 "N. Europe", 
			 "S. Europe", 
			 "W. Europe", 
			 "Ukraine", 
			 "Ural", 
			 "Afghanistan", 
			 "Middle East", 
			 "Siberia", 
			 "Yakursk", 
			 "Irkutsk", 
			 "Mongolia", 
			 "China", 
			 "Japan", 
			 "Kamchatka", 
			 "India", 
			 "Siam", 
			 "North Africa", 
			 "Egypt", 
			 "East Africa", 
			 "Congo", 
			 "South Africa", 
			 "Madagascar", 
			 "Indonesia", 
			 "New Guinea", 
			 "Western Australia", 
			 "Eastern Australia", 
			 "Venezuela", 
			 "Peru", 
			 "Brazil", 
			 "Argentina"};
	
	static int[][] connections = new int[][]{new int[]{1, 2, 25}, new int[]{0, 2, 4, 3}, new int[]{0, 4, 1, 5}, new int[]{1, 4, 7, 9}, new int[]{1, 2, 5, 6, 7, 3}, new int[]{2, 4, 6, 8}, new int[]{7, 4, 5, 8}, new int[]{3, 4, 6}, new int[]{5, 6, 38}, new int[]{3, 11, 10}, new int[]{9, 11, 12, 15}, new int[]{9, 10, 12, 14}, new int[]{11, 10, 15, 13, 14}, new int[]{14, 12, 15, 18, 29, 28}, new int[]{11, 12, 13, 28}, new int[]{10, 12, 13, 18, 17, 16}, new int[]{15, 17, 23, 19}, new int[]{15, 16, 23, 26, 18}, new int[]{13, 15, 17, 26, 29, 30}, new int[]{16, 23, 20, 21, 22}, new int[]{19, 21, 25}, new int[]{19, 20, 25, 22}, new int[]{24, 25, 21, 19, 23}, new int[]{22, 19, 16, 17, 26, 27}, new int[]{25, 22}, new int[]{20, 21, 22, 24, 0}, new int[]{27, 23, 17, 18}, new int[]{26, 23, 34}, new int[]{40, 14, 13, 29, 30, 31}, new int[]{13, 18, 30, 28}, new int[]{18, 29, 28, 31, 32, 33}, new int[]{28, 30, 32}, new int[]{31, 30, 33}, new int[]{32, 30}, new int[]{27, 35, 36}, new int[]{34, 36, 37}, new int[]{34, 35, 37}, new int[]{36, 35}, new int[]{8, 39, 40}, new int[]{38, 40, 41}, new int[]{38, 39, 41, 28}, new int[]{39, 40}};
	
	/**
	 * @deprecated Territory.getNeighbor()
	 */
	public static int[][] getConnections()
	{
		return connections;
	}

	static int[][] countryLocations = new int[][]{new int[]{56, 96}, new int[]{160, 100}, new int[]{110, 128}, new int[]{313, 50}, new int[]{179, 136}, new int[]{149, 179}, new int[]{189, 198}, new int[]{243, 134}, new int[]{145, 232}, new int[]{339, 113}, new int[]{378, 115}, new int[]{346, 165}, new int[]{376, 155}, new int[]{403, 212}, new int[]{337, 218}, new int[]{463, 124}, new int[]{530, 119}, new int[]{512, 197}, new int[]{461, 244}, new int[]{578, 98}, new int[]{684, 86}, new int[]{640, 135}, new int[]{662, 174}, new int[]{632, 226}, new int[]{707, 203}, new int[]{732, 95}, new int[]{542, 261}, new int[]{611, 281}, new int[]{342, 275}, new int[]{409, 265}, new int[]{425, 318}, new int[]{396, 355}, new int[]{393, 416}, new int[]{462, 421}, new int[]{633, 334}, new int[]{712, 351}, new int[]{634, 405}, new int[]{702, 429}, new int[]{167, 291}, new int[]{154, 327}, new int[]{238, 333}, new int[]{188, 440}};
	
	public static int[][] getCountryLocations()
	{
		return countryLocations;
	}

	/**
	 * @deprecated Territory.getArmies() and Territory.getPlayer()
	 */
	public int[][] getArmies()
	{
		return armies;
	}

	int[][] armies;
}
