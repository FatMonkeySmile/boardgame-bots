/*
 * Copyright (C) 2011 Boardgame Bots (http://code.google.com/p/boardgame-bots)
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

package com.gamebotarena.xiangqi.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.gamebotarena.xiangqi.core.Game;

//todo: no jump means even over own pieces, right?
public class XiangqiUtils
{
	static Random rand = new Random();
	
	//todo: Make random move faster?
	
	public static Move getRandomMove(MoveContext context)
	{
		HashMap h = getAllValidMoves(context, true, true, false);
		
		Set keys = h.keySet();
		
		int r = rand.nextInt(keys.size());
		
		Iterator iter = keys.iterator();
		Location from = null;
		while (r >= 0)
		{
			from = (Location) iter.next();
			r--;
		}
		
		ArrayList a = (ArrayList) h.get(from);
		r = rand.nextInt(a.size());
		Location to = (Location) a.get(r);
		
		return new Move(from.x, from.y, to.x, to.y);
	}
	
	public static boolean isAnyMoveValid(MoveContext context)
	{
		HashMap h = getAllValidMoves(context, false, true, false);
		
		if ((h.size() == 0))
		{
			return false;
		}
		
		return true;
	}
	
	public static HashMap getAllValidMoves(MoveContext context, boolean fill, boolean checkForCheck, boolean checkingForCheck)
	{
		HashMap h = new HashMap();
		
		for (int y = 0; y < 10; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				Location loc = new Location(x, y);
				//todo: pass fill into getValidMoves?
				ArrayList a = XiangqiUtils.getValidMoves(context, loc, checkForCheck, checkingForCheck);
				if (a.size() > 0)
				{
					h.put(loc, a);
					if (!fill)
					{
						return h;
					}
				}
			}
		}
		
		return h;
	}
	
	public static boolean isValidLocation(Location loc)
	{
		return isValidLocation(loc.x, loc.y);
	}
	
	public static boolean isValidLocation(int x, int y)
	{
		return (x >= 0 && x < 9 && y >= 0 && y < 10);
	}
	
	private static boolean isPlayersPieceImpl(int owner, char p)
	{
		if (owner == 0)
		{
			if (p == 'r' || p == 'h' || p == 'e' || p == 'a' || p == 'k' || p == 'c' || p == 'p')
			{
				return true;
			}
		}
		else if (p == 'R' || p == 'H' || p == 'E' || p == 'A' || p == 'K' || p == 'C' || p == 'P')
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isPlayersPiece(MoveContext context, char p)
	{
		return isPlayersPieceImpl(context.getPlayerNum(), p);
	}
	
	public static boolean isOpponentsPiece(MoveContext context, char p)
	{
		int opponent = 0;
		if (context.getPlayerNum() == 0)
		{
			opponent = 1;
		}
		
		return isPlayersPieceImpl(opponent, p);
	}
	
	public static boolean checkValidMove(MoveContext context, int x, int y, ArrayList a)
	{
		return checkValidMove(context, x, y, a, true);
	}
	
	public static boolean checkValidMove(MoveContext context, int x, int y, ArrayList a, boolean attackAllowed)
	{
		return checkValidMove(context, x, y, a, attackAllowed, ' ');
	}
	
	public static boolean checkValidMove(MoveContext context, int x, int y, ArrayList a, boolean attackAllowed, char piece)
	{
		if (!isValidLocation(x, y))
		{
			return false;
		}
		
		if(piece == 'e')
		{
			if(y >= 5)
			{
				return false;
			}
		}
		else if(piece == 'E')
		{
			if(y < 5)
			{
				return false;
			}
		}
		
		if(piece == 'a' || piece == 'k')
		{
			if(x < 3 || x > 5 || y > 2)
			{
				return false;
			}
		}
		else if(piece == 'A' || piece == 'K')
		{
			if(x < 3 || x > 5 || y < 7)
			{
				return false;
			}
		}
		
		char p = context.board.b[x][y];
		if (isPlayersPiece(context, p))
		{
			return false;
		}
		
		boolean squareOccupied = isOpponentsPiece(context, p);
		if (attackAllowed || !squareOccupied)
		{ 
			if(a != null)
			{
				a.add(new Location(x, y));
			}
		}
		
		return !squareOccupied;
	}
	
	public static boolean isValidPiece(MoveContext context, int x, int y)
	{
		if (!isValidLocation(x, y))
		{
			return false;
		}
		
		char p = context.board.b[x][y];
		
		if(p != ' ')
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isValidOpponent(MoveContext context, int x, int y)
	{
		if (!isValidLocation(x, y))
		{
			return false;
		}
		
		char p = context.board.b[x][y];
		
		if (isOpponentsPiece(context, p))
		{
			return true;
		}
		
		return false;
	}
	
	public static ArrayList getValidMoves(MoveContext context, Location loc, boolean checkForCheck, boolean checkingForCheck)
	{
		ArrayList a = new ArrayList();
		if (!isValidLocation(loc))
		{
			return a;
		}
		
		Board board = context.board;
		char p = board.b[loc.x][loc.y];
		if (p == ' ')
		{
			return a;
		}
		
		if (!isPlayersPiece(context, p))
		{
			return a;
		}
		
		char p2 = Character.toLowerCase(p);
		
		int x = loc.x;
		int y = loc.y;
		
		if (p2 == 'k')
		{
			checkValidMove(context, x - 1, y, a, true, p);
			checkValidMove(context, x + 1, y, a, true, p);
			
			if(checkingForCheck)
			{
				for (int i = 1; i < 10; i++)
				{
					if (!checkValidMove(context, x, y - i, a, true))
					{
						break;
					}
				}
				for (int i = 1; i < 10; i++)
				{
					if (!checkValidMove(context, x, y + i, a, true))
					{
						break;
					}
				}
			}
			else
			{
				checkValidMove(context, x, y + 1, a, true, p);
				checkValidMove(context, x, y - 1, a, true, p);
			}
		}
		else if (p2 == 'a')
		{
			checkValidMove(context, x - 1, y - 1, a, true, p);
			checkValidMove(context, x + 1, y - 1, a, true, p);
			checkValidMove(context, x - 1, y + 1, a, true, p);
			checkValidMove(context, x + 1, y + 1, a, true, p);
		}
		else if (p2 == 'r')
		{
			for (int i = 1; i < 10; i++)
			{
				if (!checkValidMove(context, x, y - i, a, true))
				{
					break;
				}
			}
			for (int i = 1; i < 10; i++)
			{
				if (!checkValidMove(context, x, y + i, a, true))
				{
					break;
				}
			}
			for (int i = 1; i < 10; i++)
			{
				if (!checkValidMove(context, x - i, y, a, true))
				{
					break;
				}
			}
			for (int i = 1; i < 10; i++)
			{
				if (!checkValidMove(context, x + i, y, a, true))
				{
					break;
				}
			}
		}
		else if (p2 == 'h')
		{
			if (checkValidMove(context, x - 1, y, null, false))
			{
				checkValidMove(context, x - 2, y + 1, a);
				checkValidMove(context, x - 2, y - 1, a);
			}
			if (checkValidMove(context, x + 1, y, null, false))
			{
				checkValidMove(context, x + 2, y + 1, a);
				checkValidMove(context, x + 2, y - 1, a);
			}
			if (checkValidMove(context, x, y + 1, null, false))
			{
				checkValidMove(context, x - 1, y + 2, a);
				checkValidMove(context, x + 1, y + 2, a);
			}
			if (checkValidMove(context, x, y - 1, null, false))
			{
				checkValidMove(context, x - 1, y - 2, a);
				checkValidMove(context, x + 1, y - 2, a);
			}
		}
		else if (p2 == 'e')
		{
			if (checkValidMove(context, x - 1, y - 1, null, false))
			{
				checkValidMove(context, x - 2, y - 2, a, true, p);
			}
			
			if (checkValidMove(context, x + 1, y + 1, null, false))
			{
				checkValidMove(context, x + 2, y + 2, a, true, p);
			}

			if (checkValidMove(context, x - 1, y + 1, null, false))
			{
				checkValidMove(context, x - 2, y + 2, a, true, p);
			}
			
			if (checkValidMove(context, x + 1, y - 1, null, false))
			{
				checkValidMove(context, x + 2, y - 2, a, true, p);
			}
		}
		else if (p2 == 'c')
		{
			boolean platform = false;
			
			for (int i = 1; i < 10; i++)
			{
				if (!platform)
				{
					if(isValidPiece(context, x, y - i))
					{
						platform = true;
					}
				}
				else
				{
					if(isValidOpponent(context, x, y - i))
					{
						a.add(new Location(x, y - i));
						break;
					}
				}
			}
			platform = false;
			
			for (int i = 1; i < 10; i++)
			{
				if (!platform)
				{
					if(isValidPiece(context, x, y + i))
					{
						platform = true;
					}
				}
				else
				{
					if(isValidOpponent(context, x, y + i))
					{
						a.add(new Location(x, y + i));
						break;
					}
				}
			}
			platform = false;
			
			for (int i = 1; i < 10; i++)
			{
				if (!platform)
				{
					if(isValidPiece(context, x - i, y))
					{
						platform = true;
					}
				}
				else
				{
					if(isValidOpponent(context, x - i, y))
					{
						a.add(new Location(x - i, y));
						break;
					}
				}
			}
			platform = false;
			
			for (int i = 1; i < 10; i++)
			{
				if (!platform)
				{
					if(isValidPiece(context, x + i, y))
					{
						platform = true;
					}
				}
				else
				{
					if(isValidOpponent(context, x + i, y))
					{
						a.add(new Location(x + i, y));
						break;
					}
				}
			}
		}
		else if (p2 == 'p')
		{
			boolean acrossRiver = false;
			if(context.getPlayerNum() == 0)
			{
				checkValidMove(context, x, y + 1, a, true, p);
				if(y > 4)
				{
					acrossRiver = true;
				}
			}
			else
			{
				checkValidMove(context, x, y - 1, a, true, p);
				if(y < 5)
				{
					acrossRiver = true;
				}
			}
			
			if(acrossRiver)
			{
				checkValidMove(context, x - 1, y, a, true, p);
				checkValidMove(context, x + 1, y, a, true, p);
			}
		}
		
		if (checkForCheck)
		{
			validateMovesForCheck(context, loc, a);
		}
		
		return a;
	}

	public static void  validateMovesForCheck(MoveContext context, Location loc, ArrayList a)
	{
		Board board = context.board;
		ArrayList toRemove = new ArrayList();
		
		for(int i=0; i < a.size(); i++)
		{
			Location toLoc = (Location) a.get(i);
			char oldPiece = board.b[toLoc.x][toLoc.y];
			if (wouldPutInCheck(context, new Move(loc.x, loc.y, toLoc.x, toLoc.y)))
			{
				toRemove.add(toLoc);
			}
		}
		
		for (int i=0; i < toRemove.size(); i++)
		{
			Location toLoc = (Location) toRemove.get(i);
			a.remove(toLoc);
		}
	}
	
	public static boolean isMoveValid(MoveContext context, Move move)
	{
		if(move == null)
		{
			return false;
		}
		
		if(!isValidLocation(move.fromx, move.fromy))
		{
			return false;
		}
		
		if(!isPlayersPiece(context, context.getBoard().b[move.fromx][move.fromy]))
		{
			return false;
		}

		ArrayList a = getValidMoves(context, new Location(move.fromx, move.fromy), true, false);
		
		for (int i = 0; i < a.size(); i++)
		{
			Location loc = (Location) a.get(i);
			if (loc.x == move.tox && loc.y == move.toy)
			{
				//todo: right for chinese chess?
				if (!wouldPutInCheck(context, move))
				{
					return true;
				}
				
				return false;
			}
		}
		
		return false;
	}

	public static void reverseMove(Board board, Move move, char oldPiece)
	{
		board.b[move.fromx][move.fromy] = board.b[move.tox][move.toy];
		board.b[move.tox][move.toy] = oldPiece;
	}

	public static boolean wouldPutInCheck(MoveContext context, Move move)
	{
		Board board = context.board;
		char oldPiece = Game.performMove(context, move);
		
		MoveContext oppContext = getOpponentsContext(context);
		HashMap h = XiangqiUtils.getAllValidMoves(oppContext, true, false, true);
		
		boolean check = false;
		
		Set set = h.keySet();
		Iterator iter = set.iterator();
		while (iter.hasNext() && !check)
		{
			Location from = (Location) iter.next();
			ArrayList a = (ArrayList) h.get(from);
			for (int i=0; i < a.size(); i++)
			{
				Location to = (Location) a.get(i);
				if (Character.toLowerCase(board.b[to.x][to.y]) == 'k' && XiangqiUtils.isOpponentsPiece(oppContext, board.b[to.x][to.y]))
				{
					check = true;
					break;
				}
			}
		}
		
		reverseMove(board, move, oldPiece);
		
		return check;
	}
	
	public static MoveContext getOpponentsContext(MoveContext context)
	{
		int opponent = 1;
		
		if (context.getPlayerNum() == 1)
		{
			opponent = 0;
		}
		
		return new MoveContext(context.board, opponent);
	}
}
