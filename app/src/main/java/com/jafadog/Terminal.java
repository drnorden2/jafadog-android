package com.jafadog;

import java.util.HashMap;
import java.util.Map;

import com.jafadog.util.Point;


public class Terminal  {
	
	public static final int COLOR_BLACK = 0; // Color.black,
	public static final int COLOR_BLUE = 1; // Color.blue,
	public static final int COLOR_CYAN = 2; // Color.cyan,
	public static final int COLOR_ORANGE = 3; // Color.orange,
	public static final int COLOR_RED = 4; // Color.red,

	public static final int COLOR_PINK = 5; // Color.pink,

	public static final int COLOR_GRAY = 6; // Color.gray,

	public static final int COLOR_DARK_GRAY = 7; // Color.darkGray,

	public static final int COLOR_GREEN = 8; // Color.green,

	public static final int COLOR_LIGHT_GRAY = 9; // Color.lightGray,

	public static final int COLOR_MAGENTA = 10; // Color.magenta,

	public static final int COLOR_YELLOW = 11; // Color.yellow,
	public static final int COLOR_WHITE = 12; // Color.white

	


	
	private SoundPlayer soundPlayer;

	private Graphics gm;
	
	private static final int CURSOR_OFF = 0;
	private int resX;
	private int resY;
	private int scaleX;
	private int scaleY;

	private boolean repainting = true;
	private char[][] screen;
	private int[][] bgColors;
	private int[][] fgColors;
	private boolean[][] changed;

	private int[][] cursors;
	private int x = 0;
	private int y = 0;
	private int bufferStop = 0;
	private int bgColor = 0;
	private int[] buffer = new int[10];
	
	private int fgColor = 12;

	private int bufferStart = 0;	
	private int key;
	private long lastTime = System.currentTimeMillis();
	private long periode = 10;

	private boolean isOn = false;
	
	private boolean isBufferedKey = false;
	
	/**
	 * Internal Constructor
	 * 
	 * @param resX
	 * @param resY
	 * @param scaleX
	 * @param scaleY
	 */
	 public void create( Graphics gm,  SoundPlayer soundPlayer, int resX, int resY, int scaleX, int scaleY) {
		this.resX = resX;
		this.resY = resY;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		screen = new char[resX][resY];
		fgColors = new int[resX][resY];
		bgColors = new int[resX][resY];
		this.cursors = new int[resX][resY];
		changed = new boolean[resX][resY];
		for (int i = 0; i < resX; i++) {
			for (int j = 0; j < resY; j++) {
				screen[i][j] = 32;
				fgColors[i][j] = this.fgColor;
				bgColors[i][j] = this.bgColor;
				changed[i][j] = true;
				cursors[i][j] = CURSOR_OFF;
			}
		}

		this.gm = gm;
		this.soundPlayer = soundPlayer ;
	};
	
	public final synchronized boolean bindGraphics(char c, String graphicName) {
		return gm.bindGraphics(c, graphicName);
	};

	
	
	public final synchronized char[][] bindGraphicsBanner(String graphicName, int beginAscii, int xSquares,
			int ySquares) {
		return gm.bindGraphicsBanner(graphicName, beginAscii, xSquares, ySquares);
	};

	
	
	public final synchronized void clearScreen() {
		this.clearScreen(false);
	};


	
	public final synchronized int getBackgroundColor() {
		return this.bgColor;
	}
	
	
	public final synchronized void clearScreen(boolean isDirty) {
		for (int i = 0; i < this.resX; i++) {
			for (int j = 0; j < this.resY; j++) {
				if (!isDirty || this.screen[i][j] != ' ' || this.bgColors[i][j] != bgColor) {
					this.changed[i][j] = true;
					this.screen[i][j] = ' ';
					fgColors[i][j] = this.fgColor;
					bgColors[i][j] = this.bgColor;
					this.cursors[i][j] = CURSOR_OFF;
				}
			}
		}
		x = 0;
		y = 0;
		if (this.repainting)
			repaint();
	}

	
	
	public final void continueRepainting() {
		repainting = true;
		repaint();
	}



	
	
	public final synchronized void cr() {
		y++;
		x = 0;
		if (y == resY) {
			scrollUp();
			y--;
		}
	}
	
	
	public final synchronized int getBackgroundColor(int x, int y) {
		return this.bgColors[x][y];
	}

	
	
	public final synchronized char getChar(int x, int y) {
		if (screen[x][y] > Graphics.OFFSET) {
			return (char) (screen[x][y] - Graphics.OFFSET);
		} else {
			return screen[x][y];
		}
	}

	
	
	synchronized public final int getCursor(int x, int y) {
		return this.cursors[x][y];
	}

	
	
	
	public final synchronized int getForegroundColor() {
		return fgColor;
	}

	
	
	public final synchronized int getForegroundColor(int x, int y) {
		return this.fgColors[x][y];
	}

	
	
	public final int getResX() {
		return resX;
	}

	
	
	public final int getResY() {
		return resY;
	}
	
	
	public final int getScaleX() {
		return scaleX;
	}

	
	public final int getScaleY() {
		return scaleY;
	}


	
	
	public final synchronized void print(String text) {
		print(text, true);
	}

	
	
	public final synchronized void print(String text, boolean flush) {
		this.printNoRepaint(text, false);
		if (flush && this.repainting)
			repaint();
	}

	
	
	public final synchronized void printGraphics(String text) {
		printGraphics(text, true);
	}

	
	
	public final synchronized void printGraphics(String text, boolean flush) {
		this.printNoRepaint(text, true);
		if (flush && this.repainting)
			repaint();
	}


	
	public final synchronized void println(String text) {
		println(text, true);
	}

	
	
	public final synchronized void println(String text, boolean flush) {
		this.printNoRepaint(text, false);
		cr();
		if (flush && this.repainting)
			repaint();
	}

	
	
	public final synchronized void printlnGraphics(String text) {
		printlnGraphics(text, true);
	}

	
	
	public final synchronized void printlnGraphics(String text, boolean flush) {
		this.printNoRepaint(text, true);
		cr();
		if (flush && this.repainting)
			repaint();
	}

	/**
	 * Internal method
	 * 
	 * @param text
	 * @param isGraphics
	 */
	private final synchronized void printNoRepaint(String text, boolean isGraphics) {

		for (int i = 0; i < text.length(); i++) {
			char c;
			if (isGraphics && gm.isBound(text.charAt(i))) {
				c = (char) (text.charAt(i) + Graphics.OFFSET);
			} else {
				c = (char)getExtendedAscii(text.charAt(i));
			}
			changed[x][y] = ((screen[x][y] != c) || (bgColors[x][y] != bgColor) || (fgColors[x][y] != fgColor)
					|| changed[x][y]);
			screen[x][y] = c;
			fgColors[x][y] = fgColor;
			bgColors[x][y] = bgColor;
			x++;
			if (x == resX)
				cr();
		}
	}

	
	


	
	

	/**
	 * Internal Method!
	 */
	private final synchronized void scrollUp() {
		for (int j = 0; j < this.resY - 1; j++) {
			for (int i = 0; i < this.resX; i++) {
				boolean changed = ((this.screen[i][j] != this.screen[i][j + 1]) || (this.changed[i][j + 1]));
				this.screen[i][j] = this.screen[i][j + 1];
				if (!changed)
					changed = (this.fgColors[i][j] != this.fgColors[i][j + 1]);
				if (!changed)
					changed = (this.bgColors[i][j] != this.bgColors[i][j + 1]);
				this.fgColors[i][j] = this.fgColors[i][j + 1];
				this.bgColors[i][j] = this.bgColors[i][j + 1];

				this.changed[i][j] = changed;
			}
		}
		for (int i = 0; i < this.resX; i++) {
			this.screen[i][this.resY - 1] = ' ';
			this.changed[i][this.resY - 1] = true;
		}
		repaint();
	}

	
	
	public final synchronized void setBackgroundColor(int c) {
		this.bgColor = c;
		for (int i = 0; i < this.resX; i++) {
			for (int j = 0; j < this.resY; j++) {
				this.bgColors[i][j] = c;
				this.changed[i][j] = true;
			}
		}
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see jafadog.screen.Terminal#setBufferSize(int)
	 */
	/* (non-Javadoc)
	 * @see jafadog.screen.Terminal#setBufferSize(int)
	 */
	
	public final void setBufferSize(int amountKeys) {
		buffer = new int[amountKeys];

	}

	/* (non-Javadoc)
	 * @see jafadog.screen.Terminal#setChar(int, int, char)
	 */
	/* (non-Javadoc)
	 * @see jafadog.screen.Terminal#setChar(int, int, char)
	 */
	
	public final synchronized void setChar(int x, int y, char c) {
		setChar(x, y, c, true);
	}

	/* (non-Javadoc)
	 * @see jafadog.screen.Terminal#setChar(int, int, char, boolean)
	 */
	/* (non-Javadoc)
	 * @see jafadog.screen.Terminal#setChar(int, int, char, boolean)
	 */
	
	public final synchronized void setChar(int x, int y, char c, boolean flush) {
		changed[x][y] = ((screen[x][y] != c) || (bgColors[x][y] != bgColor) || (fgColors[x][y] != fgColor)
				|| changed[x][y]);
		screen[x][y] = c;
		fgColors[x][y] = this.fgColor;
		bgColors[x][y] = this.bgColor;

		if (flush && this.repainting)
			repaint();
	}

	
	
	public final synchronized void setCharGraphics(int x, int y, char c) {
		setCharGraphics(x, y, c, true);
	}


	
	public final synchronized void setCharGraphics(int x, int y, char c, boolean flush) {

		if (gm.isBound((char) (c))) {
			changed[x][y] = ((screen[x][y] != c + Graphics.OFFSET) || (bgColors[x][y] != bgColor)
					|| (fgColors[x][y] != fgColor) || changed[x][y]);
			screen[x][y] = (char) (c + Graphics.OFFSET);

		} else {
			changed[x][y] = ((screen[x][y] != c) || (bgColors[x][y] != bgColor) || (fgColors[x][y] != fgColor)
					|| changed[x][y]);
			screen[x][y] = (char) (c);
		}
		fgColors[x][y] = this.fgColor;
		bgColors[x][y] = this.bgColor;
		if (flush && this.repainting)
			repaint();
	}

	
	
	synchronized public final void setCursor(int x, int y, int color) {
		setCursor(x, y, color, true);
	}

	
	
	synchronized public final void setCursor(int x, int y, int color, boolean flush) {
		if (color != this.getCursor(x, y)) {
			changed[x][y] = true;
		}
		this.cursors[x][y] = color;
		if (flush && this.repainting)
			repaint();
	}

	

	
	
	public final synchronized  void setForegroundColor(int c) {
		fgColor = c;
	}

	
	
	public void setIsKeyBuffered(boolean val) {
		this.setBufferSize(10);
		isBufferedKey = val;
	}


	
	public  final void setKey(int key) {
		if (isBufferedKey) {
			if (buffer[(bufferStop) % buffer.length] == 0) {
				buffer[bufferStop] = key;
				bufferStop = (bufferStop + 1) % buffer.length;
				// System.out.print(key);
			}
		} else {
			this.key = key;
		}
	}

	
	
	public final  synchronized void setKeyboardDelay(long delay) {
		this.periode = delay + 10; // to avoid cpu down!
	}

	
	
	public final synchronized void setLocalBackgroundColor(int c) {
		this.bgColor = c;
	}

	
	
	public final synchronized void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	

	
	public final void sleep() {
		try {
			while (read() == 0) {
				Thread.sleep(100);
			}
		} catch (Exception e) {
		}
	}
	
	
	public final void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
		}
	}


	


	
	public final void stopRepainting() {
		repainting = false;
	}

	
	public final boolean isMidiPlaying() {
		return soundPlayer.isMidiPlaying();
	}

	
	
	public final void loadMidi(String fileName) {
		soundPlayer.loadMidi("../"+fileName);
	}

	
	
	public final void loadSound(String str, String fileName) {
		System.out.println(fileName);
		soundPlayer.loadSound(str, "../"+fileName);
	}

	
	
	
	
	public final void playMidi(boolean looped) {
		soundPlayer.playMidi(looped);
	}


	
	public final void playSound(String str) {
		soundPlayer.playSound(str);
	}


	
	public void stopSound(String str) {
		soundPlayer.stopSound(str);
	}
	
	
	public final void stopMidi() {
		soundPlayer.stopMidi();
	}
	
	
	public final Point readMouseClickPos() {
		return null;//input.readMouseClickPos();
	}



	public final Point readMousePos() {
		return null;//input.readMousePos();
	}

	

	
	
	
	
	public final void draw(Object g) {

		if (!repainting) {
			return;
		}
		for (int j = 0; j < this.resY; j++) {
			for (int i = 0; i < this.resX; i++) {
				if (changed[i][j]) {
					changed[i][j] = true;// TODO: 28/12/2022
					int cursor = this.getCursor(i, j);
					Object im = null;
					if (screen[i][j] < Graphics.OFFSET) {
						im = gm.getCharAsImage(screen[i][j], bgColors[i][j], fgColors[i][j]);
					} else {
						im = gm.getGraphics((char) (screen[i][j] - gm.OFFSET));
					}
					int posX = i * scaleX;
					int posY = j * scaleY;
					int w = scaleX;
					int h = scaleY;
					int corrX = 0;
					int corrY = 0;
					int dx = 0;
					int dy = 0;


					dx = corrX;
					dy = corrY;


					if (im != null) {
						gm.drawImage(g, im, posX, posY, w - dx, h - dy, this.bgColor);
					} else {
						gm.setColor(g, COLOR_RED);
						gm.fillRect(g, posX, posY, w, h);
					}
					if (cursor != CURSOR_OFF) {
						gm.setColor(g, cursor);
						int strength = Math.max(1, Math.min(w, h) / 8);
						for (int ii = 0; ii < strength; ii++) {
							gm.drawRect(g, posX + ii, posY + ii, w - 1 - ii * 2, h - 1 - ii * 2);
						}
					}
					continue;
				}
			}
		}
	}
	/**
	 * Internal Method
	 */
	public final synchronized void repaint() {
	    // TODO: 28/12/2022
		//gm.update();
	}

	public final synchronized char read() {
		int val = 0;
		if (isBufferedKey) {
			if (buffer[bufferStart] != 0) {
				val = buffer[bufferStart];
				buffer[bufferStart] = 0;
				bufferStart = (bufferStart + 1) % buffer.length;
			}
		} else {
			val = key;
			key = 0;

		}

		try {
			wait(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			//Thread.sleep(500);
		} catch (Exception ex) {
			ex.printStackTrace();
		}


		if (val == 0)
			return 0;
		else
			return (char) val;
	}



	public int readKeyWaiting() {
		boolean done = false;
		int key = 0;
		while (!done) {
			key = read();
			if (key != 0) {
				done = true;
			}
		}
		return key;
	}

	


	public final synchronized String readln() {
		String val = "";
		long time = System.currentTimeMillis();
		boolean isOn = false;
		while (true) {
			// cursor
			if (System.currentTimeMillis() > (time + 400)) {
				time = System.currentTimeMillis();
				if (isOn) {
					this.setChar(x, y, '_');
				} else {
					this.setChar(x, y, ' ');
				}
				isOn = !isOn;
				repaint();
			}

			char c = read();
			if (c != 0) {
				// enter
				// System.out.println("got"+c);
				if ('\n' == c) {
					break;
				}
				// backspace
				if (((char) 8) == c) {
					if (val.length() <= 0)
						continue;
					this.setChar(x, y, ' ');
					x--;
					if (x == -1) {
						x = this.resX - 1;
						y--; // no test for -1 because val.lenght is limit -> evry printed key is deletable
					}
					this.setChar(x, y, ' ');
					val = val.substring(0, val.length() - 1);
					repaint();
				} else if (c < 32 || c > 256) {
					continue;
				} else {
					val = val + c;
					this.print("" + c);
				}
			}
		}
		this.setChar(x, y, ' ');
		cr();
		repaint();
		return val;
	}
	public static final char[] EXTENDED = { 0x00C7, 0x00FC, 0x00E9, 0x00E2,
			0x00E4, 0x00E0, 0x00E5, 0x00E7, 0x00EA, 0x00EB, 0x00E8, 0x00EF,
			0x00EE, 0x00EC, 0x00C4, 0x00C5, 0x00C9, 0x00E6, 0x00C6, 0x00F4,
			0x00F6, 0x00F2, 0x00FB, 0x00F9, 0x00FF, 0x00D6, 0x00DC, 0x00A2,
			0x00A3, 0x00A5, 0x20A7, 0x0192, 0x00E1, 0x00ED, 0x00F3, 0x00FA,
			0x00F1, 0x00D1, 0x00AA, 0x00BA, 0x00BF, 0x2310, 0x00AC, 0x00BD,
			0x00BC, 0x00A1, 0x00AB, 0x00BB, 0x2591, 0x2592, 0x2593, 0x2502,
			0x2524, 0x2561, 0x2562, 0x2556, 0x2555, 0x2563, 0x2551, 0x2557,
			0x255D, 0x255C, 0x255B, 0x2510, 0x2514, 0x2534, 0x252C, 0x251C,
			0x2500, 0x253C, 0x255E, 0x255F, 0x255A, 0x2554, 0x2569, 0x2566,
			0x2560, 0x2550, 0x256C, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559,
			0x2558, 0x2552, 0x2553, 0x256B, 0x256A, 0x2518, 0x250C, 0x2588,
			0x2584, 0x258C, 0x2590, 0x2580, 0x03B1, 0x00DF, 0x0393, 0x03C0,
			0x03A3, 0x03C3, 0x00B5, 0x03C4, 0x03A6, 0x0398, 0x03A9, 0x03B4,
			0x221E, 0x03C6, 0x03B5, 0x2229, 0x2261, 0x00B1, 0x2265, 0x2264,
			0x2320, 0x2321, 0x00F7, 0x2248, 0x00B0, 0x2219, 0x00B7, 0x221A,
			0x207F, 0x00B2, 0x25A0, 0x00A0 };
	private final static Map<Integer, Integer> toASCII = new HashMap<Integer,Integer>();
	static{
		int code = 128;
		for(int cur:EXTENDED){
			toASCII.put(cur,code++);
		}
	}

	public static final int getExtendedAscii(int code) {
		Integer extended = toASCII.get(code);
		if(extended!=null){
			return extended;
		}else{
			return code;
		}
	}

}