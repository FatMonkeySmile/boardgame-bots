package com.gamebotarena.risk.api;

public class ReinforcementsMove
{
	ReinforcementsStruct[] armies;
	
	public ReinforcementsMove(ReinforcementsStruct[] armies)
	{
		this.armies = armies;
	}
	
	public ReinforcementsMove()
	{
	}

	public ReinforcementsStruct[] getArmies()
	{
		return armies;
	}
	
	public void setArmies(ReinforcementsStruct[] value)
	{
		this.armies = value;
	}
	
	public void addReinforcements(ReinforcementsStruct reinf)
	{
		if(this.armies == null)
		{
			this.armies = new ReinforcementsStruct[1];
		}
		else
		{
			ReinforcementsStruct[] old = this.armies;
			this.armies = new ReinforcementsStruct[old.length + 1];
			for(int i=0; i < old.length; i++)
			{
				this.armies[i] = old[i];
			}
		}

		this.armies[this.armies.length - 1] = reinf;
	}
	
	public void addReinforcements(int armies, int country)
	{
		addReinforcements(new ReinforcementsStruct(armies, country));
	}
}
