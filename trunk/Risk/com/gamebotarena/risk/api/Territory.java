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
