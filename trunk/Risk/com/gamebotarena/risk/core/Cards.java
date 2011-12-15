package com.gamebotarena.risk.core;

import java.util.ArrayList;
import java.util.Random;

import com.gamebotarena.framework.api.D;
import com.gamebotarena.risk.api.Board;
import com.gamebotarena.risk.api.Card;

public class Cards
{
	private void  InitBlock()
	{
		deck = new ArrayList<Card>();
		random = new Random();
	}

	private int tradeValueIndex;
	
	public static final int[] TRADE_VALUES = new int[]{4, 6, 8, 10, 12, 15};
	
	private ArrayList<Card> deck;
	
	ArrayList<Card>[] hands; //todo: make private?
	
	private Random random;
	
    public Cards(int numPlayers)
	{
		InitBlock();
		hands = new ArrayList[numPlayers + 1];
		for (int i = 1; i < hands.length; i++)
		{
			hands[i] = new ArrayList();
		}
		
		tradeValueIndex = 0;
		ArrayList<Card> v = new ArrayList<Card>(Card.NUM_CARDS);
		for (int i = 0; i < Card.NUM_CARDS; i++)
		{
			v.add(new Card(i, cardTypes[i]));
		}
		
		int x = 0;
		while ((x = v.size()) > 0)
		{
			int num = random.nextInt(x);
			//D.out("num = " + num + " x = " + x);
			
			deck.add(v.get(num));
			v.remove(num);
		}
	}
	
	public static String getCardCountry(int cardNumber)
	{
		if (cardNumber >= Board.NUM_COUNTRIES)
		{
			return "Wild";
		}
		return Board.getCountries()[cardNumber];
	}
	
	public static int getCardType(int cardNumber)
	{
		return cardTypes[cardNumber];
	}
	
	public static String getCardCountry(Card card)
	{
		int cardNumber = card.getTerritory();
		if (cardNumber >= Board.NUM_COUNTRIES)
		{
			return "Wild";
		}
		return Board.getCountries()[cardNumber];
	}
	
	public static int getCardType(Card card)
	{
		return card.getType();
	}
	
	public void draw(int player)
	{
		hands[player].add(deck.get(0));
		deck.remove(0);
	}

	private int getNextTradeInValue(int type)
	{
		/*
		if(type == TYPE_INFANTRY)
		{
			return 4;
		}
		
		if(type == TYPE_CALVARY)
		{
			return 6;
		}
		
		if(type == TYPE_CANNON)
		{
			return 8;
		}
		
		if(type == TYPE_WILD)
		{
			return 10;
		}
		
		return 0;
		*/
		
		int tradeValue;

		if (tradeValueIndex < 6)
		{
			tradeValue = TRADE_VALUES[tradeValueIndex];
		}
		else
		{
			tradeValue = (tradeValueIndex - 2) * 5;
		}

		return tradeValue;
	}
	
	public int trade(int player, ArrayList<Card> cards)
	{
		if (cards == null)
		{
			return 0;
		}
		
		//todo: check for 0 from caller
		
		if (!isValidTrade(player, cards))
		{
			return 0;
		}
		
		for (int i = 0; i < 3; i++)
		{
			hands[player].remove(cards.get(i));
		}
		
		//todo: shuffle?
		deck.addAll(cards);
		
		int type = getCardType((Card) cards.get(0));
		
		if(getCardType(cards.get(1)) != type)
		{
			type = Card.TYPE_WILD;
		}
		
		int tradeValue = getNextTradeInValue(type);
		
		tradeValueIndex++;
		D.out("trade-in:" + tradeValue);
		return tradeValue;
	}
	
	
	public boolean isValidTrade(int player, ArrayList<Card> cards)
	{
		if (cards.size() != 3)
		{
			//todo: Output invalid trade
			D.out("Invalid trade...");
			return false;
		}
		
		for (int i = 0; i < 3; i++)
		{
			Card in_Renamed = cards.get(i);
			boolean found = false;
			for (int j = 0; j < hands[player].size(); j++)
			{
				Card card = hands[player].get(j);
				if (card.getTerritory() == in_Renamed.getTerritory())
				{
					for (int k = 0; k < i; k++)
					{
						if (cards.get(k).getTerritory() == card.getTerritory())
						{
							return false;
						}
					}
					
					found = true;
					break;
				}
			}
			
			if (!found)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public boolean canTrade(int player)
	{
		return suggestTrade(player) != null;
	}
	
	public boolean mustTrade(int player)
	{
		return (hands[player].size() > 4);
	}
	
	public ArrayList<Card> suggestTrade(int player)
	{
		//todo: Check for null in caller
		ArrayList<Card> cards = hands[player];
		
		if (cards.size() < 3)
		{
			return null;
		}
		
		for (int i = 0; i < cards.size() - 2; i++)
		{
			Card c1 = cards.get(i);
			for (int j = i + 1; j < cards.size(); j++)
			{
				Card c2 = cards.get(j);
				
				for (int k = i + 1; k < cards.size(); k++)
				{
					if (k == j)
					{
						continue;
					}
					
					Card c3 = cards.get(k);
					
					if (isSet(c1, c2, c3))
					{
						ArrayList<Card> ret = new ArrayList<Card>();
						ret.add(c1);
						ret.add(c2);
						ret.add(c3);
						return ret;
					}
				}
			}
		}
		
		return null;
	}
	
	public void transferCards(int from, int to)
	{
		for(Card o : hands[from])
		{
			hands[to].add(o);
		}
		hands[from].clear();
	}
	
	boolean isSet(Card c1, Card c2, Card c3)
	{
		int t1 = c1.getType();
		int t2 = c2.getType();
		int t3 = c3.getType();
		
		if (t1 == t2 && t2 == t3)
		{
			return true;
		}
		
		if (t1 == Card.TYPE_WILD || t2 == Card.TYPE_WILD || t3 == Card.TYPE_WILD)
		{
			return true;
		}
		
		if (t1 != t2 && t2 != t3 && t1 != t3)
		{
			return true;
		}
		
		return false;
	}
	
	private static int[] cardTypes = new int[]{Card.TYPE_INFANTRY, Card.TYPE_CANNON, Card.TYPE_CALVARY, Card.TYPE_CALVARY, Card.TYPE_CALVARY, Card.TYPE_CANNON, Card.TYPE_CANNON, Card.TYPE_CALVARY, Card.TYPE_CANNON, Card.TYPE_INFANTRY, Card.TYPE_CALVARY, Card.TYPE_CANNON, Card.TYPE_CANNON, Card.TYPE_CANNON, Card.TYPE_CANNON, Card.TYPE_CALVARY, Card.TYPE_CALVARY, Card.TYPE_CALVARY, Card.TYPE_INFANTRY, Card.TYPE_CALVARY, Card.TYPE_CALVARY, Card.TYPE_CALVARY, Card.TYPE_INFANTRY, Card.TYPE_INFANTRY, Card.TYPE_CANNON, Card.TYPE_INFANTRY, Card.TYPE_CALVARY, Card.TYPE_INFANTRY, Card.TYPE_CALVARY, Card.TYPE_INFANTRY, Card.TYPE_INFANTRY, Card.TYPE_INFANTRY, Card.TYPE_CANNON, Card.TYPE_CALVARY, Card.TYPE_CANNON, Card.TYPE_INFANTRY, Card.TYPE_CANNON, Card.TYPE_CANNON, Card.TYPE_INFANTRY, Card.TYPE_INFANTRY, Card.TYPE_CANNON, Card.TYPE_INFANTRY, Card.TYPE_WILD, Card.TYPE_WILD};

	public ArrayList<Card> getHand(int player)
	{
		return hands[player];
	}
}