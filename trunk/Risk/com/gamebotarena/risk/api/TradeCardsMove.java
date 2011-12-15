package com.gamebotarena.risk.api;

import java.util.ArrayList;

public class TradeCardsMove
{
	ArrayList cards;
	
	public TradeCardsMove(ArrayList cards)
	{
		this.cards = cards;
	}
	
	public ArrayList getCards()
	{
		return cards;
	}

	public void setCards(ArrayList cards)
	{
		this.cards = cards;
	}
}
