package com.gamebotarena.risk.api;

public class RandomRiskGameBot extends RiskGameBot
{
	public SetupMove getSetupMove(MoveContext context)
	{
		return context.getRandomSetupMove();
	}

	public ReinforcementsMove getReinforcementsMove(MoveContext context)
	{
		return context.getRandomReinforcementsMove();
	}
	
	public AttackMove getAttackMove(MoveContext context)
	{
		return context.getRandomAttackMove();
	}
	
	public Move getMove(MoveContext context)
	{
		return context.getRandomMove();
	}
}
