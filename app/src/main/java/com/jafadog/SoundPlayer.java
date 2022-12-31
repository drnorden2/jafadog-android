package com.jafadog;

/**
 * <p>
 * Sound-Modul fuer Jafadog
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * @author Andre Fischer / Tim Pohle
 * @version 1.0
 */

public interface SoundPlayer {

	public void create();

	public boolean isMidiPlaying();

	public void loadMidi(String fileName);

	public void loadSound(String c, String fileName);

	public void playMidi(boolean looped);

	public void playSound(String str);

	public void stopMidi();

	public void stopSound(String str);

}