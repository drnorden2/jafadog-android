package com.jafadog.qbasic;

import com.jafadog.android.console.Cartridge;

import com.jafadog.Terminal;

abstract public class QbasicApp extends Cartridge {
	// int[] colMap ={0,1,2,2,4,5,1,2,7,1,8,9,4,10,11,12};
	Terminal terminal;
	public QbasicApp() {
		this.terminal = this.getTerminal();
	}

	public String CHR$(int ascii) {
		return "" + (char) ascii;
	}

	public void CLS() {
		terminal.clearScreen();
	}

	public void COLOR(int fg) {
		terminal.setForegroundColor(fg + 13);
	}

	public void COLOR(int fg, int bg) {
		// if(fg>=0&&fg<=15)
		terminal.setLocalBackgroundColor(bg + 13);
		// if(bg>=0&&bg<=15)
		terminal.setForegroundColor(fg + 13);
	}

	public String getFormated(String format, int v) {
		String out = "";
		String val = "" + v;
		for (int i = val.length() - 1; i >= 0; i--) {
			for (int j = format.length() - 1; j >= 0; j--) {
				if (format.charAt(j) == '#' || val.charAt(i) == '-') {
					out = val.charAt(i) + out;
					format = format.substring(0, format.length() - 1);
					break;

				} else {
					out = out + format.charAt(j);
					format = format.substring(0, format.length() - 1);
				}

			}
		}
		return SPACE$(format.length()) + out;
	}

	public String INKEY$() {
		char k = terminal.read();
		if (k == 0)
			return "";
		return "" + k;
	}

	public String INPUT() {
		return terminal.readln();
	}

	public String LEFT$(String txt, int len) {
		return txt.substring(0, len);
	}

	public int LEN(String text) {
		return text.length();
	}

	public void LOCATE(int y, int x) {
		terminal.setPos(x - 1, y - 1);
	}

	public String MID$(String txt, int start, int len) {
		return txt.substring(start, start + len);
	}

	public void PRINT(String s) {
		terminal.print(s);
	}

	public void PRINTLN(String s) {
		terminal.println(s);
	}

	public String RIGHT$(String txt, int len) {
		return txt.substring(txt.length() - len);
	}

	public String SPACE$(int amount) {
		String val = "";
		for (int i = 0; i < amount; i++) {
			val += " ";
		}
		return val;
	}

	public String STR$(int val) {
		return "" + val;
	}

	public String UCASE$(String txt) {
		return txt.toUpperCase();
	}

	public int VAL(String c) {
		int val = 0;
		try {
			val = Integer.parseInt(c);
		} catch (Exception ex) {

		}
		return val;
	}
}