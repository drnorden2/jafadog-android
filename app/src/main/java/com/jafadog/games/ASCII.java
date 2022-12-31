package com.jafadog.games;

import com.jafadog.Terminal;
import com.jafadog.android.console.Cartridge;

public class ASCII extends Cartridge {
	@Override
	public void go() {

		Terminal terminal = this.getTerminal();
		terminal.clearScreen();
		terminal.setBackgroundColor(10);
		terminal.clearScreen();
		terminal.setForegroundColor(8);
		terminal.print("All jafadog.games.ASCII");
		for (int i = 0; i < 256; i++) {
			terminal.println("" + ((char) (i)) + " " + (i) + "");
			terminal.readKeyWaiting();
		}
	}
}