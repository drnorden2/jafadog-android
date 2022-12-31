package com.jafadog.games;

import com.jafadog.Terminal;

import com.jafadog.qbasic.QbasicApp;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class Nibbles extends QbasicApp {
	public static final int KEY_LEFT = 1037;
	public static final int KEY_UP = 1038;
	public static final int KEY_RIGHT = 1039;
	public static final int KEY_DOWN = 1040;
	Terminal terminal;

	/*
	 * ' Q B a s i c N i b b l e s ' ' Copyright (C) Microsoft Corporation 1990 ' '
	 * jafadog.games.Nibbles is a game for one or two players. Navigate your snakes ' around the
	 * game board trying to eat up numbers while avoiding ' running into walls or
	 * other snakes. The more numbers you eat up, ' the more points you gain and the
	 * longer your snake becomes. ' ' To run this game, press Shift+F5. ' ' To exit
	 * QBasic, press Alt, F, X. ' ' To get help on a BASIC keyword, move the cursor
	 * to the keyword and press ' F1 or click the right mouse button. '
	 * 
	 * 'Set default data type to integer for faster game play
	 */
	// DEFINT A-Z

	// 'User-defined TYPEs
	/*
	 * TYPE snakeBody row AS INTEGER col AS INTEGER END TYPE
	 */
	// 'This type defines the player's snake
	/*
	 * TYPE snaketype head AS INTEGER length AS INTEGER row AS INTEGER col AS
	 * INTEGER direction AS INTEGER lives AS INTEGER score AS INTEGER scolor AS
	 * INTEGER alive AS INTEGER END TYPE
	 */
	/*
	 * 'This type is used to represent the playing screen in memory 'It is used to
	 * simulate graphics in text mode, and has some interesting, 'and slightly
	 * advanced methods to increasing the speed of operation. 'Instead of the normal
	 * 80x25 text graphics using chr$(219) "?", we will be 'using chr$(220)"ue" and
	 * chr$(223) "ss" and chr$(219) "?" to mimic an 80x50 'pixel screen. 'Check out
	 * sub-programs SET and POINTISTHERE to see how this is implemented 'feel free
	 * to copy these (as well as arenaType and the DIM ARENA stmt and the
	 * 'initialization code in the DrawScreen subprogram) and use them in your own
	 * 'programs
	 * 
	 * TYPE arenaType realRow AS INTEGER 'Maps the 80x50 point into the real 80x25
	 * acolor AS INTEGER 'Stores the current color of the point sister AS INTEGER
	 * 'Each char has 2 points in it. .SISTER is END TYPE '-1 if sister point is
	 * above, +1 if below
	 */
	// 'Sub Declarations

	public class ArenaType {
		// realRow AS INTEGER 'Maps the 80x50 point into the real 80x25
		// acolor AS INTEGER 'Stores the current color of the point
		// sister AS INTEGER 'Each char has 2 points in it. .SISTER is
		public int realRow;
		public int acolor;
		public int sister;
	}
	public class SnakeBody {
		public int row;
		public int col;
	}
	public class SnakeType {
		public int head;
		public int length;
		public int row;
		public int col;
		public int direction;
		public int lives;
		public int score;
		public int scolor;
		public int alive;
	}
	
	// 'Constants
	final int TRUE = -1;
	final int FALSE = 1;// NOT TRUE

	final int MAXSNAKELENGTH = 1000;
	final int STARTOVER = 1; // ' Parameters to 'Level' SUB
	final int SAMELEVEL = 2;

	final int NEXTLEVEL = 3;
	int numberRow = 0;
	int NumberCol = 0;

	int sisterRow = 0;

	// 'Global Variables
	// SHARED arena(1 TO 50, 1 TO 80) AS arenaType
	ArenaType[][] arena = new ArenaType[51][81];
	int curLevel;
	int[] colorTable = new int[10];
	// have to be global :)
	int NumPlayers = 1;

	int speed = 1;

	String diff$ = "y";

	String monitor$ = "c";

	public Nibbles() {
		super();
		this.terminal = this.getTerminal();
		for (int i = 0; i < 51; i++) {
			for (int j = 0; j < 81; j++) {

				// System.out.println("i" + i+" , "+j);
				arena[i][j] = new ArenaType();
			}
		}
		// System.out.println("DEBUG: DON CONSTRUCTOR!");

	}

	// 'Center:
	// ' Centers text on given row
	void Center(int row, String text$) {
		LOCATE(row, 40 - LEN(text$) / 2);
		PRINT(text$);
	}

	// 'DrawScreen:
	// ' Draws playing field
	void DrawScreen() {

		// 'initialize screen
		// VIEW PRINT ?????????????
		COLOR(colorTable[1], colorTable[4]);
		CLS();
		// 'Print title & message
		Center(1, "jafadog.games.Nibbles!");
		Center(11, "Initializing Playing Field...");
		// 'Initialize arena array
		for (int row = 1; row <= 50; row++) {
			for (int col = 1; col <= 80; col++) {
				arena[row][col].realRow = (int) ((row + 1) / 2);
				arena[row][col].sister = ((row % 2) * 2) - 1;// 1 or -1
			}
		}
	}

	// 'EraseSnake:
	// ' Erases snake to facilitate moving through playing field
	void EraseSnake(SnakeType snake[], SnakeBody[][] snakeBod, int snakeNum) {

		for (int c = 0; c <= 9; c++) {
			for (int b = snake[snakeNum].length - c; b >= 0; b -= 10) {
				int tail = (snake[snakeNum].head + MAXSNAKELENGTH - b) % MAXSNAKELENGTH;
				Set(snakeBod[tail][snakeNum].row, snakeBod[tail][snakeNum].col, colorTable[4]);
			}
		}
	}

	// 'GetInputs:
	// ' Gets player inputs
	void GetInputs() {

		COLOR(7, 0);
		CLS();
		String num$ = "";
		do {
			LOCATE(5, 40);
			PRINT(SPACE$(41));
			LOCATE(5, 20);
			PRINT("How many players (1 or 2)");
			num$ = INPUT();
		} while (!(VAL(num$) == 1 || VAL(num$) == 2));
		NumPlayers = VAL(num$);

		LOCATE(8, 21);
		PRINTLN("Skill level (1 to 100)");
		LOCATE(9, 22);
		PRINT("1   = Novice");
		LOCATE(10, 22);
		PRINT("90  = Expert");
		LOCATE(11, 22);
		PRINT("100 = Twiddle Fingers");
		LOCATE(12, 15);
		PRINT("(Computer speed may affect your skill level)");
		String gamespeed$ = "";
		do {
			LOCATE(8, 44);
			PRINT(SPACE$(35));
			LOCATE(8, 43);
			gamespeed$ = INPUT();
		} while (!(VAL(gamespeed$) >= 1 && VAL(gamespeed$) <= 100));
		speed = VAL(gamespeed$);

		speed = (100 - speed) * 2 + 1;
		do {
			LOCATE(15, 56);
			PRINT(SPACE$(25));
			LOCATE(15, 15);
			PRINT("Increase game speed during play (Y or N)");
			diff$ = INPUT();
			diff$ = UCASE$(diff$);
		} while (!("Y".equals(diff$) || "N".equals(diff$)));

		do {
			LOCATE(17, 46);
			PRINT(SPACE$(34));
			LOCATE(17, 17);
			PRINT("Monochrome or color monitor (M or C)");
			monitor$ = INPUT();
			monitor$ = UCASE$(monitor$);
		} while (!("M".equals(monitor$) || "C".equals(monitor$)));

		/*
		 * TODO!! startTime# = TIMER ' Calculate speed of system for (int i# = 1 TO
		 * 10000: NEXT i# ' and do some compensation stopTime# = TIMER speed = speed * 3
		 * / (stopTime# - startTime#)
		 */
	}

	// RANDOMIZE TIMER
	// GOSUB ClearKeyLocks
	public void go() {
		// TODO: 30/12/2022
		//terminal.setIsKeyBuffered(true);
		Intro();

		GetInputs();
		SetColors();// goSub
		DrawScreen();

		do {
			PlayNibbles();
		} while (StillWantsToPlay() == TRUE);

		// RestoreKeyLocks(); // gosub
		COLOR(15, 0);
		CLS();
	}


	// 'InitColors:
	// 'Initializes playing field colors
	void InitColors() {

		for (int row = 1; row <= 50; row++) {
			for (int col = 1; col <= 80; col++) {
				arena[row][col].acolor = colorTable[4];
			}
		}

		CLS();

		// 'Set (turn on) pixels for screen border
		for (int col = 1; col <= 80; col++) {
			Set(3, col, colorTable[3]);
			Set(50, col, colorTable[3]);
		}

		for (int row = 4; row <= 49; row++) {
			Set(row, 1, colorTable[3]);
			Set(row, 80, colorTable[3]);
		}

	}

	// 'Intro:
	// ' Displays game introduction
	void Intro() {
		// SCREEN 0
		// WIDTH 80, 25
		COLOR(15, 0);
		CLS();

		Center(3, "Q B a s i c   N i b b l e s");
		COLOR(7);
		Center(5, "Copyright (C) Microsoft Corporation 1990");

		COLOR(14);
		Center(6, "Java port 2003 by Andre Fischer");
		COLOR(7);

		Center(8, "Nibbles is a game for one or two players.  Navigate your snakes");
		Center(9, "around the game board trying to eat up numbers while avoiding");
		Center(10, "running into walls or other snakes.  The more numbers you eat up,");
		Center(11, "the more points you gain and the longer your snake becomes.");
		Center(13, " Game Controls ");
		Center(15, "  General             Player 1               Player 2    ");
		Center(16, "                        (Up)                   (Up)      ");
		Center(17, "P - Pause                " + CHR$(24) + "                      W       ");
		Center(18, "                     (Left) " + CHR$(27) + "   " + CHR$(26) + " (Right)   (Left) A   D (Right)  ");
		Center(19, "                         " + CHR$(25) + "                      S       ");
		Center(20, "                       (Down)                 (Down)     ");
		Center(24, "Press any key to continue");
		// no Sound!
		// PLAY "MBT160O1L8CDEDCDL4ECC"
		SparklePause();

	}

	// 'Level:
	// 'Sets game level
	void Level(int WhatToDO, SnakeType[] sammy) {

		switch (WhatToDO) {

		case STARTOVER:
			curLevel = 1;
			break;
		case NEXTLEVEL:
			curLevel = curLevel + 1;
		}

		sammy[1].head = 1;// 'Initialize Snakes
		sammy[1].length = 2;
		sammy[1].alive = TRUE;
		sammy[2].head = 1;
		sammy[2].length = 2;
		sammy[2].alive = TRUE;

		InitColors();

		switch (curLevel) {
		case 1:
			sammy[1].row = 25;
			sammy[2].row = 25;
			sammy[1].col = 50;
			sammy[2].col = 30;
			sammy[1].direction = 4;
			sammy[2].direction = 3;
			break;

		case 2:
			for (int i = 20; i <= 60; i++) {
				Set(25, i, colorTable[3]);
			}
			sammy[1].row = 7;
			sammy[2].row = 43;
			sammy[1].col = 60;
			sammy[2].col = 20;
			sammy[1].direction = 3;
			sammy[2].direction = 4;
			break;
		case 3:
			for (int i = 10; i <= 40; i++) {
				Set(i, 20, colorTable[3]);
				Set(i, 60, colorTable[3]);
			}
			sammy[1].row = 25;
			sammy[2].row = 25;
			sammy[1].col = 50;
			sammy[2].col = 30;
			sammy[1].direction = 1;
			sammy[2].direction = 2;
			break;
		case 4:
			for (int i = 4; i <= 30; i++) {
				Set(i, 20, colorTable[3]);
				Set(53 - i, 60, colorTable[3]);
			}
			for (int i = 2; i < 40; i++) {
				Set(38, i, colorTable[3]);
				Set(15, 81 - i, colorTable[3]);
			}
			sammy[1].row = 7;
			sammy[2].row = 43;
			sammy[1].col = 60;
			sammy[2].col = 20;
			sammy[1].direction = 3;
			sammy[2].direction = 4;
			break;
		case 5:
			for (int i = 13; i <= 39; i++) {
				Set(i, 21, colorTable[3]);
				Set(i, 59, colorTable[3]);
			}
			for (int i = 23; i <= 57; i++) {
				Set(11, i, colorTable[3]);
				Set(41, i, colorTable[3]);
			}
			sammy[1].row = 25;
			sammy[2].row = 25;
			sammy[1].col = 50;
			sammy[2].col = 30;
			sammy[1].direction = 1;
			sammy[2].direction = 2;
			break;
		case 6:
			for (int i = 4; i <= 49; i++) {
				if (i > 30) {
					Set(i, 10, colorTable[3]);
					Set(i, 20, colorTable[3]);
					Set(i, 30, colorTable[3]);
					Set(i, 40, colorTable[3]);
					Set(i, 50, colorTable[3]);
					Set(i, 60, colorTable[3]);
					Set(i, 70, colorTable[3]);
				}
			}
			sammy[1].row = 7;
			sammy[2].row = 43;
			sammy[1].col = 65;
			sammy[2].col = 15;
			sammy[1].direction = 2;
			sammy[2].direction = 1;
			break;
		case 7:
			for (int i = 4; i <= 49; i = i + 2) {
				Set(i, 40, colorTable[3]);
			}
			sammy[1].row = 7;
			sammy[2].row = 43;
			sammy[1].col = 65;
			sammy[2].col = 15;
			sammy[1].direction = 2;
			sammy[2].direction = 1;
			break;
		case 8:
			for (int i = 4; i <= 40; i++) {
				Set(i, 10, colorTable[3]);
				Set(53 - i, 20, colorTable[3]);
				Set(i, 30, colorTable[3]);
				Set(53 - i, 40, colorTable[3]);
				Set(i, 50, colorTable[3]);
				Set(53 - i, 60, colorTable[3]);
				Set(i, 70, colorTable[3]);
			}
			sammy[1].row = 7;
			sammy[2].row = 43;
			sammy[1].col = 65;
			sammy[2].col = 15;
			sammy[1].direction = 2;
			sammy[2].direction = 1;
			break;
		case 9:
			for (int i = 6; i <= 47; i++) {
				Set(i, i, colorTable[3]);
				Set(i, i + 28, colorTable[3]);
			}
			sammy[1].row = 40;
			sammy[2].row = 15;
			sammy[1].col = 75;
			sammy[2].col = 5;
			sammy[1].direction = 1;
			sammy[2].direction = 2;
			break;
		default:
			for (int i = 4; i <= 49; i = i + 2) {
				Set(i, 10, colorTable[3]);
				Set(i + 1, 20, colorTable[3]);
				Set(i, 30, colorTable[3]);
				Set(i + 1, 40, colorTable[3]);
				Set(i, 50, colorTable[3]);
				Set(i + 1, 60, colorTable[3]);
				Set(i, 70, colorTable[3]);
			}
			sammy[1].row = 7;
			sammy[2].row = 43;
			sammy[1].col = 65;
			sammy[2].col = 15;
			sammy[1].direction = 2;
			sammy[2].direction = 1;
		}
	}

	// 'PlayNibbles:
	// ' Main routine that controls game play
	void PlayNibbles() {
		// 'Initialize Snakes
		SnakeBody[][] sammyBody = new SnakeBody[MAXSNAKELENGTH][3];
		for (int i = 0; i < MAXSNAKELENGTH; i++) {
			sammyBody[i][1] = new SnakeBody();
			sammyBody[i][2] = new SnakeBody();
		}

		SnakeType[] sammy = new SnakeType[3];
		sammy[1] = new SnakeType();
		sammy[2] = new SnakeType();

		sammy[1].lives = 5;
		sammy[1].score = 0;
		sammy[1].scolor = colorTable[1];
		sammy[2].lives = 5;
		sammy[2].score = 0;
		sammy[2].scolor = colorTable[2];

		Level(STARTOVER, sammy);
		int startRow1 = sammy[1].row;
		int startCol1 = sammy[1].col;
		int startRow2 = sammy[2].row;
		int startCol2 = sammy[2].col;

		int curSpeed = speed;

		// 'play jafadog.games.Nibbles until finished

		SpacePause("     Level" + STR$(curLevel) + ",  Push Space");
		int gameOver = FALSE;
		do {
			if (NumPlayers == 1) {
				sammy[2].row = 0;
			}
			int number = 1; // 'Current number that snakes are trying to run into
			int nonum = TRUE; // 'nonum = TRUE if a number is not on the screen

			int playerDied = FALSE;
			PrintScore(NumPlayers, sammy[1].score, sammy[2].score, sammy[1].lives, sammy[2].lives);//
			// PLAY "T160O1>L20CDEDCDL10ECC" // no Sound!

			do {
				// 'Print number if no number exists

				if (nonum == TRUE) {

					do {
						numberRow = ((int) (Math.random() * 47.0) + 3); // INT(RND(1) * 47 + 3);
						NumberCol = ((int) (Math.random() * 78.0) + 2); // INT(RND(1) * 78 + 2);
						sisterRow = numberRow + arena[numberRow][NumberCol].sister;
						// LOOP UNTIL NOT PointIsThere(numberRow, NumberCol, colorTable(4)) AND NOT
						// PointIsThere(sisterRow, NumberCol, colorTable(4))
					} while (!((PointIsThere(numberRow, NumberCol, colorTable[4]) != TRUE)
							&& (PointIsThere(sisterRow, NumberCol, colorTable[4]) != TRUE)));

					numberRow = arena[numberRow][NumberCol].realRow;
					nonum = FALSE;
					COLOR(colorTable[1], colorTable[4]);
					LOCATE(numberRow, NumberCol);
					PRINT(RIGHT$(STR$(number), 1));
					// count = 0; ?? never used
				}

				// 'Get keyboard input & Change direction accordingly
				// String kbd$ = INKEY$();
				// int k = ( int)kbd$.toLowerCase().charAt(0);
				int k = terminal.read();
				// System.out.println("key_:" + k);
				switch (k) {
				case 'w':
					if (sammy[2].direction != 2) {
						sammy[2].direction = 1;
					}
					break;
				case 's':
					if (sammy[2].direction != 1) {
						sammy[2].direction = 2;
					}
					break;
				case 'a':
					if (sammy[2].direction != 4) {
						sammy[2].direction = 3;
					}
					break;
				case 'd':
					if (sammy[2].direction != 3) {
						sammy[2].direction = 4;
					}
					break;
				case KEY_UP:
					if (sammy[1].direction != 2) {
						sammy[1].direction = 1;
					}
					break;
				case KEY_DOWN:
					if (sammy[1].direction != 1) {
						sammy[1].direction = 2;
					}
					break;
				case KEY_LEFT:
					if (sammy[1].direction != 4) {
						sammy[1].direction = 3;
					}
					break;
				case KEY_RIGHT:
					if (sammy[1].direction != 3) {
						sammy[1].direction = 4;
					}
					break;

				/*
				 * case CHR$(0) + "P": if sammy[1].direction <> 1 THEN sammy[1].direction = 2
				 * case CHR$(0) + "K": if sammy[1].direction <> 4 THEN sammy[1].direction = 3
				 * case CHR$(0) + "M": if sammy[1].direction <> 3 THEN sammy[1].direction = 4
				 */
				case 'p':
					SpacePause(" Game Paused ... Push Space  ");
					break;
				}

				// System.out.println("dir="+sammy[1].direction);

				for (int a = 1; a <= NumPlayers; a++) {
					// 'Move Snake
					int x = sammy[a].direction;
					switch (x) {
					case 1:
						sammy[a].row = sammy[a].row - 1;
						break;
					case 2:
						sammy[a].row = sammy[a].row + 1;
						break;
					case 3:
						sammy[a].col = sammy[a].col - 1;
						break;
					case 4:
						sammy[a].col = sammy[a].col + 1;
						break;
					}

					// 'If snake hits number, respond accordingly
					// if(numberRow == INT((sammy[a].row + 1) / 2) && NumberCol == sammy[a].col){
					// System.out.println("nrrow =" + ((sammy[a].row + 1) / 2) +"==" +numberRow);
					// System.out.println("nrcol =" + sammy[a].col +"==" +NumberCol);

					if (numberRow == ((sammy[a].row + 1) / 2) && NumberCol == sammy[a].col) {
						// PLAY "MBO0L16>CCCE"
						// System.out.println("HIT A NR!");
						if (sammy[a].length < (MAXSNAKELENGTH - 30)) {
							sammy[a].length = sammy[a].length + number * 4;
						}
						sammy[a].score = sammy[a].score + number;
						PrintScore(NumPlayers, sammy[1].score, sammy[2].score, sammy[1].lives, sammy[2].lives);
						number = number + 1;
						if (number == 10) {
							EraseSnake(sammy, sammyBody, 1);
							EraseSnake(sammy, sammyBody, 2);
							LOCATE(numberRow, NumberCol);
							PRINTLN(" ");
							Level(NEXTLEVEL, sammy);
							PrintScore(NumPlayers, sammy[1].score, sammy[2].score, sammy[1].lives, sammy[2].lives);
							SpacePause("     Level" + STR$(curLevel) + ",  Push Space");
							if (NumPlayers == 1) {
								sammy[2].row = 0;
							}
							number = 1;
							if ("P".equals(diff$)) {
								speed = speed - 10;
								curSpeed = speed;
							}
						}
						nonum = TRUE;
						if (curSpeed < 1) {
							curSpeed = 1;
						}
						// 'Delay game

					}
				}
				for (int a = 1; a <= NumPlayers; a++) {
					// 'If player runs into any point, or the head of the other snake, it dies.
					if ((PointIsThere(sammy[a].row, sammy[a].col, colorTable[4]) == TRUE)
							|| (sammy[1].row == sammy[2].row && sammy[1].col == sammy[2].col)) {
						// System.out.println("cOOL sound!");

						// PLAY "MBO0L32EFGEFDC"
						terminal.sleep(600);
						COLOR(colorTable[4]); // -1 dummy
						LOCATE(numberRow, NumberCol);
						PRINTLN(" ");

						playerDied = TRUE;
						sammy[a].alive = FALSE;
						sammy[a].lives = sammy[a].lives - 1;

						// 'Otherwise, move the snake, and erase the tail
					} else {
						sammy[a].head = (sammy[a].head + 1) % MAXSNAKELENGTH;
						sammyBody[sammy[a].head][a].row = sammy[a].row;
						sammyBody[sammy[a].head][a].col = sammy[a].col;
						int tail = (sammy[a].head + MAXSNAKELENGTH - sammy[a].length) % MAXSNAKELENGTH;
						Set(sammyBody[tail][a].row, sammyBody[tail][a].col, colorTable[4]);
						sammyBody[tail][a].row = 0;

						Set(sammy[a].row, sammy[a].col, sammy[a].scolor);
					}
				}
				terminal.sleep(curSpeed);
			} while (playerDied == FALSE);

			curSpeed = speed;// ' reset speed to initial value

			for (int a = 1; a <= NumPlayers; a++) {
				EraseSnake(sammy, sammyBody, a);

				// 'If dead, then erase snake in really cool way
				if (sammy[a].alive == FALSE) {
					// 'Update score
					sammy[a].score = sammy[a].score - 10;
					PrintScore(NumPlayers, sammy[1].score, sammy[2].score, sammy[1].lives, sammy[2].lives);
					if (a == 1) {
						SpacePause(" Sammy Dies! Push Space! --->");
					} else {
						SpacePause(" <---- Jake Dies! Push Space ");
					}
				}
			}

			Level(SAMELEVEL, sammy);
			PrintScore(NumPlayers, sammy[1].score, sammy[2].score, sammy[1].lives, sammy[2].lives);

			// 'Play next round, until either of snake's lives have run out.

		} while (!(sammy[1].lives == 0 || sammy[2].lives == 0));
	}

	// 'Set:
	// ' Sets row and column on playing field to given color to facilitate moving
	// ' of snakes around the field.

	// 'PointIsThere:
	// ' Checks the global arena array to see if the boolean flag is set
	int PointIsThere(int row, int col, int acolor) {
		int p = 0;
		if (row != 0) {
			if (arena[row][col].acolor != acolor) {
				p = TRUE;
			} else {
				p = FALSE;
			}
		}
		return p;
	}

	// 'PrintScore:
	// ' Prints players scores and number of lives remaining
	void PrintScore(int NumPlayers, int score1, int score2, int lives1, int lives2) {
		COLOR(15, colorTable[4]);

		if (NumPlayers == 2) {
			LOCATE(1, 1);
			// PRINT USING "#,###,#00 Lives: # <--JAKE"; score2; lives2
			PRINTLN(this.getFormated("  #####", score2) + "00  Lives: " + this.getFormated("#", lives2) + "  <--JAKE");
		}

		LOCATE(1, 49);
		// PRINT USING "SAMMY--> Lives: # #,###,#00"; lives1; score1
		PRINTLN("SAMMY-->  Lives: " + this.getFormated("#", lives1) + "       " + this.getFormated("#####", score1)
				+ "00");
	}

	void Set(int row, int col, int acolor) {
		// System.out.println("print in:"+row);
		if (row != 0) {
			arena[row][col].acolor = acolor; // 'assign color to arena
			int realRow = arena[row][col].realRow; // 'Get real row of pixel
			int topFlag = (arena[row][col].sister + 1) / 2; // 'Deduce whether pixel
			// 'is on topss, or bottomue
			int sisterRow = row + arena[row][col].sister; // 'Get arena row of sister
			int sisterColor = arena[sisterRow][col].acolor; // 'Determine sister's color

			LOCATE(realRow, col);
			// System.out.println("DEBUG" + realRow +", " + col);
			if (acolor == sisterColor) { // 'If both points are same
				COLOR(acolor, acolor); // 'Print chr$(219) "?"
				// PRINTX (CHR$(219));
				this.terminal.setChar(col - 1, realRow - 1, (char) 219);
			} else {

				if (topFlag == FALSE) { /// ?? //'Since you cannot have
					if (acolor > 7) { // 'bright backgrounds
						COLOR(acolor, sisterColor); // 'determine best combo
						// PRINTX (CHR$(223)); //'to use.
						this.terminal.setChar(col - 1, realRow - 1, (char) 223);

					} else {
						COLOR(sisterColor, acolor);
						// PRINTX (CHR$(220));
						this.terminal.setChar(col - 1, realRow - 1, (char) 220);

					}
				} else {
					if (acolor > 7) {
						COLOR(acolor, sisterColor);
						// PRINTX (CHR$(220));
						this.terminal.setChar(col - 1, realRow - 1, (char) 220);

					} else {
						COLOR(sisterColor, acolor);
						// PRINTX (CHR$(223));
						this.terminal.setChar(col - 1, realRow - 1, (char) 223);

					}
				}
			}
		}
	}

	void SetColors() {
		/*
		 * if monitor$ = "M" THEN RESTORE mono else RESTORE normal END if
		 */
		// for(int a = 1;a <= 6;a++){// Achtung a war 1!!
		// READ colorTable[a]

		if ("M".equals(monitor$)) {
			int[] tbl = { 0, 15, 7, 7, 0, 15, 0 };
			colorTable = tbl;
		} else {
			int[] tbl = { 0, 14, 13, 12, 1, 15, 4 };
			colorTable = tbl;
		}

	}
	/*
	 * 'snake1 snake2 Walls Background Dialogs-Fore Back mono: DATA 15, 7, 7, 0, 15,
	 * 0 normal: DATA 14, 13, 12, 1, 15, 4 END
	 */

	// 'SpacePause:
	// ' Pauses game play and waits for space bar to be pressed before continuing
	void SpacePause(String text$) {

		COLOR(colorTable[5], colorTable[6]);



		Center(11, "█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█");
		Center(12, "█ " + LEFT$(text$ + SPACE$(29), 29) + " █");
		// System.out.println("" + "? " + LEFT$(text$ + SPACE$(29), 29) + " ?");
		Center(13, "█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█");
		while (!"".equals(INKEY$()))
			;
		while (!" ".equals(INKEY$()))
			;
		COLOR(15, colorTable[4]);
		terminal.stopRepainting();
		for (int i = 21; i <= 26; i++) { // ' Restore the screen background
			for (int j = 24; j <= 56; j++) {
				Set(i, j, arena[i][j].acolor);
			}
		}
		terminal.continueRepainting();
	}

	// 'SparklePause:
	// ' Creates flashing border for intro screen
	void SparklePause() {
		COLOR(4, 0);
		String a$ = "*    *    *    *    *    *    *    *    *    *    *    *    *    *    *    *    *    ";
		// while INKEY$ <> "": WEND //'Clear keyboard buffer

		while ("".equals(INKEY$())) {
			for (int a = 1; a <= 5; a++) {
				terminal.stopRepainting();
				LOCATE(1, 1); // 'print horizontal sparkles
				PRINT(MID$(a$, a, 80));
				LOCATE(22, 1);
				PRINT(MID$(a$, 6 - a, 80));

				for (int b = 2; b <= 21; b++) { // 'Print Vertical sparkles
					int c = (a + b) % 5;
					if (c == 1) {
						LOCATE(b, 80);
						PRINT("*");
						LOCATE(23 - b, 1);
						PRINT("*");
					} else {
						LOCATE(b, 80);
						PRINT(" ");
						LOCATE(23 - b, 1);
						PRINT(" ");
					}
				}
				terminal.continueRepainting();
				terminal.sleep(50);
			}
		}

	}

	// 'StillWantsToPlay:
	// ' Determines if users want to play game again.
	int StillWantsToPlay() {
		int val = 0;
		COLOR(colorTable[5], colorTable[6]);
		Center(10, "█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█");
		Center(11, "█       G A M E   O V E R       █");
		Center(12, "█                               █");
		Center(13, "█      Play Again?   (Y/N)      █");
		Center(14, "█▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄█");


		String kbd$ = "";
		while (!"".equals(INKEY$()))
			;
		do {
			kbd$ = UCASE$(INKEY$());
		} while (!("Y".equals(kbd$) || "N".equals(kbd$)));

		COLOR(15, colorTable[4]);
		Center(10, "                                 ");
		Center(11, "                                 ");
		Center(12, "                                 ");
		Center(13, "                                 ");
		Center(14, "                                 ");

		if ("Y".equals(kbd$)) {
			val = TRUE;
		} else {
			val = FALSE;
			COLOR(7, 0);
			CLS();
		}

		// END FUNCTION*/
		return val;
	}

}