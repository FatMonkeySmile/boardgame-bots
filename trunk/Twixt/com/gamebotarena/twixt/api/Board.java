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

package com.gamebotarena.twixt.api;

public class Board
{
	public int[][] b;
	public boolean[][][] l;

	public void Init()
	{
		b = new int[SIZE][];
		l = new boolean[SIZE][][];
		for(int i=0; i < SIZE; i++)
		{
			b[i] = new int[SIZE];
			l[i] = new boolean[SIZE][];
			for(int j=0; j < SIZE; j++)
			{
				l[i][j] = new boolean[9];
			}
		}
	}

	public Board()
	{
		Init();
	}

	public Board(Board board)
	{
		Init();
		for(int y=0; y < SIZE; y++)
		{
			for(int x=0; x < SIZE; x++)
			{
				b[x][y] = board.b[x][y];
				for(int i=0; i < 9; i++)
				{
					l[x][y][i] = board.l[x][y][i];
				}
			}
		}
	}

	public static int SIZE = 24;
	
	public static int getSize()
	{
		return SIZE;
	}

	public static int[][] DIFFERENCE;
	public static int[] INVERSE;
	public static CheckTable[][] CHECKTABLE;

	static
	{
		//todo: move to static initializer
		DIFFERENCE = new int[9][];
		DIFFERENCE[1] = new int[]{1, -2};
		DIFFERENCE[2] = new int[]{2, -1};
		DIFFERENCE[3] = new int[]{2, 1};
		DIFFERENCE[4] = new int[]{1, 2};
		DIFFERENCE[5] = new int[]{-1, 2};
		DIFFERENCE[6] = new int[]{-2, 1};
		DIFFERENCE[7] = new int[]{-2, -1};
		DIFFERENCE[8] = new int[]{-1, -2};

		INVERSE = new int[9];
		INVERSE[1] = 5;
		INVERSE[2] = 6;
		INVERSE[3] = 7;
		INVERSE[4] = 8;
		INVERSE[5] = 1;
		INVERSE[6] = 2;
		INVERSE[7] = 3;
		INVERSE[8] = 4;

		CHECKTABLE = new CheckTable[5][];

		CHECKTABLE[1] = new CheckTable[4];
		CHECKTABLE[1][0] = new CheckTable(0, -1, new int[]{2, 3, 4});
		CHECKTABLE[1][1] = new CheckTable(0, -2, new int[]{3, 4});
		CHECKTABLE[1][2] = new CheckTable(1, 0, new int[]{7, 8});
		CHECKTABLE[1][3] = new CheckTable(1, -1, new int[]{6, 7, 8});

		CHECKTABLE[2] = new CheckTable[4];
		CHECKTABLE[2][0] = new CheckTable(0, -1, new int[]{4, 3});
		CHECKTABLE[2][1] = new CheckTable(1, -1, new int[]{5, 4, 3});
		CHECKTABLE[2][2] = new CheckTable(1, 0, new int[]{7, 8, 1});
		CHECKTABLE[2][3] = new CheckTable(2, 0, new int[]{7, 8});

		CHECKTABLE[3] = new CheckTable[4];
		CHECKTABLE[3][0] = new CheckTable(0, 1, new int[]{1, 2});
		CHECKTABLE[3][1] = new CheckTable(1, 1, new int[]{8, 1, 2});
		CHECKTABLE[3][2] = new CheckTable(1, 0, new int[]{4, 5, 6});
		CHECKTABLE[3][3] = new CheckTable(2, 0, new int[]{5, 6});
	
		CHECKTABLE[4] = new CheckTable[4];
		CHECKTABLE[4][0] = new CheckTable(0, 1, new int[]{1, 2, 3});
		CHECKTABLE[4][1] = new CheckTable(0, 2, new int[]{1, 2});
		CHECKTABLE[4][2] = new CheckTable(1, 0, new int[]{6, 5});
		CHECKTABLE[4][3] = new CheckTable(1, 1, new int[]{6, 7, 5});
	}

	public static int[] getDifference(int i)
	{
		return DIFFERENCE[i];
	}

	public static int inverse(int i)
	{
		return INVERSE[i];
	}
}
