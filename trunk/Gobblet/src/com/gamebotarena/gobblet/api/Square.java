package com.gamebotarena.gobblet.api;

import java.util.Stack;

public class Square 
{
	Stack<Piece> pieces = new Stack<Piece>();
	
	public Square()
	{
	}
	
	public void play(Piece piece)
	{
		if(pieces.size() != 0 && pieces.peek().getSize() >= piece.getSize())
		{
			//todo: report correctly...
			System.out.println("Invalid play: " + piece.getSize());
			return;
		}
		pieces.add(piece);
	}
	
	public Piece getPiece()
	{
		if(pieces.size() > 0)
		{
			return pieces.peek();
		}
		
		return null;
	}
	
	//internal...
	public Piece pop()
	{
		if(pieces.size() == 0)
		{
			return null;
		}
		
		return pieces.pop();
	}
	
	public Piece peek()
	{
		if(pieces.size() == 0)
		{
			return null;
		}
		
		return pieces.peek();
	}
}
