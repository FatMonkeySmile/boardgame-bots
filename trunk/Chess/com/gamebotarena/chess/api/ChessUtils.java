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

package com.gamebotarena.chess.api;

import java.util.*;

public class ChessUtils
{
	static Random rand = new Random();
	
	//todo: Make random move faster?
	
	public static Move findEnPassentMove(MoveContext context)
	{
		if(context.enpassentPossibleTarget == null)
		{
			return null;
		}

		HashMap h = getAllValidMoves(context);
		
		Set keys = h.keySet();
		
		int r = keys.size() - 1;
		
		Iterator iter = keys.iterator();
		Location from = null;
		while (r >= 0)
		{
			from = (Location) iter.next();
			ArrayList a = (ArrayList) h.get(from);
			if(Character.toLowerCase(context.board.b[from.x][from.y]) == 'p' && context.enpassentPossibleTarget.y == from.y)
			{
				for(int i2=0; i2 < a.size(); i2++)
				{
					Location to = (Location) a.get(i2);
					if(to.x == context.enpassentPossibleTarget.x)
					{
						return new Move(from.x, from.y, to.x, to.y);
					}
				}
			}
			r--;
		}
		
		return null;
	}

	public static Move findCastleMove(MoveContext context)
	{
		if(context.castleChecks[context.getPlayerNum()][0] == true || (context.castleChecks[context.getPlayerNum()][2] == true && context.castleChecks[context.getPlayerNum()][1] == true))
		{
			return null;
		}

		HashMap h = getAllValidMoves(context);
		
		Set keys = h.keySet();
		
		int r = keys.size() - 1;
		
		Iterator iter = keys.iterator();
		Location from = null;
		while (r >= 0)
		{
			from = (Location) iter.next();
			ArrayList a = (ArrayList) h.get(from);
			if(Character.toLowerCase(context.board.b[from.x][from.y]) == 'k')
			{
				for(int i2=0; i2 < a.size(); i2++)
				{
					Location to = (Location) a.get(i2);
					if(Math.abs(to.x - from.x) > 1)
					{
						return new Move(from.x, from.y, to.x, to.y);
					}
				}
			}
			r--;
		}
		
		return null;
	}

	//todo: Got a draw here, but shouldn't have
	public static Move getRandomMove(MoveContext context)
	{
		Move move = findEnPassentMove(context);
		if(move != null)
		{
			return move;
		}

		move = findCastleMove(context);
		if(move != null)
		{
			return move;
		}

		HashMap h = getAllValidMoves(context);
		
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
		HashMap h = getAllValidMoves(context, false, true);
		
		if ((h.size() == 0))
		{
			return false;
		}
		
		return true;
	}
	
	public static HashMap getAllValidMoves(MoveContext context)
	{
		return getAllValidMoves(context, true, true);
	}
	
	public static HashMap getAllValidMoves(MoveContext context, boolean checkForCheck)
	{
		return getAllValidMoves(context, true, checkForCheck);
	}

	public static HashMap getAllValidMoves(MoveContext context, boolean fill, boolean checkForCheck)
	{
		HashMap h = new HashMap();
		
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				Location loc = new Location(x, y);
				//todo: pass fill into getValidMoves?
				ArrayList a = ChessUtils.getValidMoves(context, loc, checkForCheck);
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
		return (x >= 0 && x <= 7 && y >= 0 && y <= 7);
	}
	
	private static boolean isPlayersPieceImpl(int owner, char p)
	{
		if (owner == 0)
		{
			if (p == 'r' || p == 'n' || p == 'b' || p == 'k' || p == 'q' || p == 'p')
			{
				return true;
			}
		}
		else if (p == 'R' || p == 'N' || p == 'B' || p == 'K' || p == 'Q' || p == 'P')
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
		if (!isValidLocation(x, y))
		{
			return false;
		}
		char p = context.board.b[x][y];
		if (isPlayersPiece(context, p))
		{
			return false;
		}
		
		boolean squareOccupied = isOpponentsPiece(context, p);
		if (attackAllowed || !squareOccupied)
		{
			a.add(new Location(x, y));
		}
		
		return !squareOccupied;
	}
	
	public static void checkQueensCastle(MoveContext context, ArrayList a)
	{
		//todo: implement check for "check" on every square king passes through
		if(context.getCastleChecks()[context.getPlayerNum()][0] || context.getCastleChecks()[context.getPlayerNum()][2])
		{
			return;
		}

		int yLine = 0;
		if(context.getPlayerNum() == 1)
		{
			yLine = 7;
		}

		Board board = context.board;
		if(board.b[4][yLine] != ' ' || board.b[5][yLine] != ' ' || board.b[6][yLine] != ' ')
		{
			return;
		}

		a.add(new Location(6, yLine));
	}

	public static void checkKingsCastle(MoveContext context, ArrayList a)
	{
		//todo: implement check for "check" on every square king passes through
		if(context.getCastleChecks()[context.getPlayerNum()][0] || context.getCastleChecks()[context.getPlayerNum()][1])
		{
			return;
		}

		int yLine = 0;
		if(context.getPlayerNum() == 1)
		{
			yLine = 7;
		}

		Board board = context.board;
		if(board.b[1][yLine] != ' ' || board.b[2][yLine] != ' ')
		{
			return;
		}

		a.add(new Location(1, yLine));
	}

	public static ArrayList getValidMoves(MoveContext context, Location loc, boolean checkForCheck)
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
			checkValidMove(context, x - 1, y - 1, a);
			checkValidMove(context, x - 1, y, a);
			checkValidMove(context, x - 1, y + 1, a);
			checkValidMove(context, x, y - 1, a);
			checkValidMove(context, x, y + 1, a);
			checkValidMove(context, x + 1, y - 1, a);
			checkValidMove(context, x + 1, y, a);
			checkValidMove(context, x + 1, y + 1, a);
			checkQueensCastle(context, a);
			checkKingsCastle(context, a);
		}
		else if (p2 == 'n')
		{
			checkValidMove(context, x - 2, y + 1, a);
			checkValidMove(context, x - 2, y - 1, a);
			checkValidMove(context, x + 2, y + 1, a);
			checkValidMove(context, x + 2, y - 1, a);
			checkValidMove(context, x - 1, y + 2, a);
			checkValidMove(context, x + 1, y + 2, a);
			checkValidMove(context, x - 1, y - 2, a);
			checkValidMove(context, x + 1, y - 2, a);
		}
		else if (p2 == 'b')
		{
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x - i, y - i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x + i, y + i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x - i, y + i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x + i, y - i, a))
				{
					break;
				}
			}
		}
		else if (p2 == 'q')
		{
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x - i, y - i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x + i, y + i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x - i, y + i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x + i, y - i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x, y - i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x, y + i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x - i, y, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x + i, y, a))
				{
					break;
				}
			}
		}
		else if (p2 == 'r')
		{
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x, y - i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x, y + i, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x - i, y, a))
				{
					break;
				}
			}
			for (int i = 1; i < 8; i++)
			{
				if (!checkValidMove(context, x + i, y, a))
				{
					break;
				}
			}
		}
		else if (p2 == 'p')
		{
			if (context.getPlayerNum() == 0)
			{
				if (checkValidMove(context, x, y + 1, a, false))
				{
					if (y == 1)
					{
						checkValidMove(context, x, y + 2, a, false);
					}
				}
				
				if(context.enpassentPossibleTarget != null)
				{
					if(y == 4)
					{
						if(isEnpassentTarget(context, x - 1, 4))
						{
							checkValidMove(context, x - 1, 5, a, false); 
						}
						else if(isEnpassentTarget(context, x + 1, 4))
						{
							checkValidMove(context, x + 1, 5, a, false);
						}
					}
				}

				int possibleAttackX = x - 1;
				int possibleAttackY = y + 1;
				if (isValidLocation(possibleAttackX, possibleAttackY))
				{
					char p3 = board.b[possibleAttackX][possibleAttackY];
					if (isOpponentsPiece(context, p3))
					{
						a.add(new Location(possibleAttackX, possibleAttackY));
					}
				}
				possibleAttackX = x + 1;
				possibleAttackY = y + 1;
				if(isValidLocation(possibleAttackX, possibleAttackY))
				{
					char p3 = board.b[possibleAttackX][possibleAttackY];
					if (isOpponentsPiece(context, p3))
					{
						a.add(new Location(possibleAttackX, possibleAttackY));
					}
				}
			}
			else
			{
				if (checkValidMove(context, x, y - 1, a, false))
				{
					if (y == 6)
					{
						checkValidMove(context, x, y - 2, a, false);
					}
				}
				
				if(context.enpassentPossibleTarget != null)
				{
					if(y == 3)
					{
						if(isEnpassentTarget(context, x - 1, 3))
						{
							checkValidMove(context, x - 1, 2, a, false); 
						}
						else if(isEnpassentTarget(context, x + 1, 3))
						{
							checkValidMove(context, x + 1, 2, a, false);
						}
					}
				}

				int possibleAttackX = x - 1;
				int possibleAttackY = y - 1;
				if (isValidLocation(possibleAttackX, possibleAttackY))
				{
					char p3 = board.b[possibleAttackX][possibleAttackY];
					if (isOpponentsPiece(context, p3))
					{
						a.add(new Location(possibleAttackX, possibleAttackY));
					}
				}
				possibleAttackX = x + 1;
				possibleAttackY = y - 1;
				if (isValidLocation(possibleAttackX, possibleAttackY))
				{
					char p3 = board.b[possibleAttackX][possibleAttackY];
					if (isOpponentsPiece(context, p3))
					{
						a.add(new Location(possibleAttackX, possibleAttackY));
					}
				}
			}
		}
		
		if (checkForCheck)
		{
			validateMovesForCheck(context, loc, a);
		}
		
		return a;
	}

	public static boolean isEnpassentTarget(MoveContext context, int x, int y)
	{
		if(context.enpassentPossibleTarget == null)
		{
			return false;
		}

		if(context.enpassentPossibleTarget.x == x && context.enpassentPossibleTarget.y == y)
		{
			return true;
		}

		return false;
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

		ArrayList a = getValidMoves(context, new Location(move.fromx, move.fromy), true);
		
		for (int i = 0; i < a.size(); i++)
		{
			Location loc = (Location) a.get(i);
			if (loc.x == move.tox && loc.y == move.toy)
			{
				if (!wouldPutInCheck(context, move))
				{
					return true;
				}
				
				return false;
			}
		}
		
		return false;
	}

	public static void performCastle(Board board, char piece, Move move)
	{
		if(move.tox == 1)
		{
			board.b[move.tox][move.toy] = piece;
			board.b[move.fromx][move.fromy] = ' ';
			board.b[2][move.toy] = board.b[0][move.toy];
			board.b[0][move.toy] = ' ';
		}
		else
		{
			board.b[move.tox][move.toy] = piece;
			board.b[move.fromx][move.fromy] = ' ';
			board.b[5][move.toy] = board.b[7][move.toy];
			board.b[7][move.toy] = ' ';
		}
	}

	public static void reverseCastle(Board board, Move move)
	{
		char piece = board.b[move.tox][move.toy];
		if(move.tox == 1)
		{
			board.b[move.tox][move.toy] = ' ';
			board.b[move.fromx][move.fromy] = piece;
			board.b[0][move.toy] = board.b[2][move.toy];
			board.b[2][move.toy] = ' ';
		}
		else
		{
			board.b[move.tox][move.toy] = ' ';
			board.b[move.fromx][move.fromy] = piece;
			board.b[7][move.toy] = board.b[5][move.toy];
			board.b[5][move.toy] = ' ';
		}
	}

	public static char performMove(MoveContext context, Move move)
	{
		Board board = context.board;
		char oldPiece = board.b[move.tox][move.toy];
		char piece = board.b[move.fromx][move.fromy];

		if(ChessUtils.isCastle(piece, move))
		{
			performCastle(board, piece, move);
		}
		else
		{
			board.b[move.tox][move.toy] = piece;
			board.b[move.fromx][move.fromy] = ' ';
		}

		if(oldPiece == ' ' && Character.toLowerCase(piece) == 'p' && Math.abs(move.tox - move.fromx) == 1 && context.enpassentPossibleTarget != null)
		{
			if(context.enpassentPossibleTarget.x == move.tox && context.enpassentPossibleTarget.y == move.fromy)
			{
				board.b[context.enpassentPossibleTarget.x][context.enpassentPossibleTarget.y] = ' ';
				oldPiece = 'x';
			}
		}

		return oldPiece;
	}

	public static void reverseMove(Board board, Move move, char oldPiece)
	{
		char piece = board.b[move.tox][move.toy];

		if(ChessUtils.isCastle(piece, move))
		{
			reverseCastle(board, move);
		}
		else if(oldPiece == 'x')
		{
			reverseEnPassent(board, move, oldPiece);
		}
		else
		{
			board.b[move.fromx][move.fromy] = board.b[move.tox][move.toy];
			board.b[move.tox][move.toy] = oldPiece;
		}
	}

	public static void reverseEnPassent(Board board, Move move, char oldPiece)
	{
		board.b[move.fromx][move.fromy] = board.b[move.tox][move.toy];
		board.b[move.tox][move.toy] = ' ';
		if(move.toy == 2)
		{
			board.b[move.tox][3] = 'p';
		}
		else
		{
			board.b[move.tox][4] = 'P';
		}
	}
	
	public static boolean wouldPutInCheck(MoveContext context, Move move)
	{
		Board board = context.board;
		char oldPiece = performMove(context, move);
		
		MoveContext oppContext = getOpponentsContext(context);
		HashMap h = ChessUtils.getAllValidMoves(oppContext, false);
		
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
				if (Character.toLowerCase(board.b[to.x][to.y]) == 'k' && ChessUtils.isOpponentsPiece(oppContext, board.b[to.x][to.y]))
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
		
		return new MoveContext(context.board, opponent, context.getCastleChecks(), context.enpassentPossibleTarget);
	}

	public static boolean isCastle(char piece, Move move)
	{
		if(Character.toLowerCase(piece) == 'k' && move.fromx == 3 && (move.tox == 6 || move.tox == 1))
		{
			return true;
		}

		return false;
	}

	public static boolean isUpgradePawnMoveValid(MoveContext context, UpgradePawnMove move)
	{
		char p = move.piece;
		if(!isPlayersPiece(context, p))
		{
			return false;
		}

		char p2 = Character.toLowerCase(p);
		if(p2 == 'k' || p2 == 'p')
		{
			return false;
		}

		return true;
	}

	public static UpgradePawnMove getRandomUpgradePawnMove(MoveContext context)
	{
		int r = rand.nextInt(4);
		char p = ' ';
		if(r == 0)
		{
			p = 'q';
		}
		else if(r == 1)
		{
			p = 'r';
		}
		else if(r == 2)
		{
			p = 'b';
		}
		else if(r == 3)
		{
			p = 'n';
		}

		return new UpgradePawnMove(p);
	}

	public static Move swapMove(Move move)
	{
		return new Move(7 - move.fromx, 7 - move.fromy, 7 - move.tox, 7 - move.toy);
	}
}
