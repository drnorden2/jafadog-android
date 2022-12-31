package jafadog.desktop;

import java.applet.Applet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;

import javax.sound.sampled.LineUnavailableException;

import jafadog.AbstractApp;
import jafadog.SoundPlayer;

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

public class DesktopSoundPlayer implements Runnable, SoundPlayer {
	// Hintergrundmusik im Midi-Format:
	private Sequence sequence;
	private Sequencer sequencer;
	private boolean sequencerLoaded = false;

	private Looping looping;

	// zur Speicherung von (geladenen) gesampleten Einzelsounds:
	private Hashtable clips = new Hashtable(4, 0.5f);
	private Hashtable audioInputStreams = new Hashtable(); // die clip-Daten werden im Arbeitsspeicher als
															// ByteArrayInputStreams abgelegt
	// welche Sounds werden aktuell abgespielt?
	private Vector playingSounds = new Vector();
	private Thread cleaningThread;
	private final int CLEANING_THREAD_SLEEPTIME = 1000; // ueberpruefungsintervall fuer gestoppte Sounds
	
	public void create() {
		System.out.println("Create DesktopSoundPlayer");
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencerLoaded = true;
		} catch (Exception ex) {
			System.err.println("Midi wird von diesem Computer scheinbar nicht unsterstuetzt: Fehlermeldung: ");
			ex.printStackTrace();
		}
	}

	
	/**
	 * loopt das Midi-File
	 */
	private class Looping extends TimerTask {
		private Timer loopTimer = new Timer(true);
		Sequencer sequencer;

		public Looping(Sequencer sequencer) {
			this.sequencer = sequencer;
		}

		public void destroy() {
			sequencer.stop();
			loopTimer.cancel();
		}

		public void run() {
			sequencer.stop();
			sequencer.setMicrosecondPosition(0);
			sequencer.start();
		}

		public void start() {
			try {
				loopTimer.schedule(this, 0, sequencer.getSequence().getMicrosecondLength() / 1000 - 100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	
	private URL fileNameToUrl(String fileName) {
		URL url = null;
		try {
			url = getClass().getResource(fileName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("SoundManager: Lade Sounddatei " + url);
		return url;
	}

	public boolean isMidiPlaying() {
		return sequencer.isRunning();
	}

	public void loadMidi(String fileName) {
		loadMidi(fileNameToUrl(fileName));
	}

	/**
	 * laedt eine Midi-Datei aus einer URL
	 */
	private void loadMidi(URL fileUrl) {
		try {
			sequence = MidiSystem.getSequence(fileUrl);
			sequencer.setSequence(sequence);
			sequencer.setMicrosecondPosition(2000);

		} catch (Exception ex) {
			System.err.println("Fehler beim Laden der Midi-Datei " + fileUrl + ":");
			ex.printStackTrace();
		}
	}

	public void loadSound(String c, String fileName) {
		loadSound(c, fileNameToUrl(fileName));
	}

	/**
	 * laedt einen gesampleten Sound aus einer URL
	 */
	private void loadSound(String c, URL fileUrl) {
		// intern wird der neu geladene Sound als Clip in das Hashtable 'clips'
		// geschrieben.
		try {

			// AudioFormat ermitteln:
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileUrl);
			AudioFormat audioFormat = audioInputStream.getFormat();

			/**
			 * we can't yet open the device for ALAW/ULAW playback, convert ALAW/ULAW to PCM
			 */
			if ((audioFormat.getEncoding() == AudioFormat.Encoding.ULAW)
					|| (audioFormat.getEncoding() == AudioFormat.Encoding.ALAW)) {
				AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(),
						audioFormat.getSampleSizeInBits() * 2, audioFormat.getChannels(),
						audioFormat.getFrameSize() * 2, audioFormat.getFrameRate(), true);
				audioInputStream = AudioSystem.getAudioInputStream(tmp, audioInputStream);
				audioFormat = tmp;
			}

			DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat(),
					((int) audioInputStream.getFrameLength() * audioFormat.getFrameSize()));

			// hashing der Clip-Objekte:
			Clip newClip = (Clip) AudioSystem.getLine(info);
			clips.put(c, newClip);

			// Audiodaten kopieren und hashen:

			// zuerst in byte[] speichern:
			ByteArrayOutputStream str = new ByteArrayOutputStream();
			byte[] readVal = new byte[(int) audioInputStream.getFrameLength()];
			while ((audioInputStream.read(readVal)) != -1) {
				str.write(readVal);
			}

			// dann aus den byte[]-Daten einen AudioInputStream konstruieren und diesen
			// Hashen:
			byte[] buf = str.toByteArray();
			AudioInputStream audioInputStreamFromByteArray = new AudioInputStream(new ByteArrayInputStream(buf),
					audioFormat, audioInputStream.getFrameLength());
			audioInputStreams.put(c, audioInputStreamFromByteArray);

		} catch (Exception ex) {
			System.err.println("SoundManager: Fehler beim oeffnen der Sound-Datei " + fileUrl);
			// ex.printStackTrace();
		}
	}

	/**
	 * startet die Hintergrundmusik
	 */
	public void playMidi(boolean looped) {
		if (!looped) {
			if (looping != null) {
				looping.destroy();
				looping = null;
			}
			sequencer.start();
		} else {
			looping = new Looping(sequencer);
			looping.start();
		}
	}

	public void playSound(String str) {
		try {
			Clip currClip = (Clip) clips.get(str);

			// this.setClipVolume(0, str);

			// wenn der Clip bereits geoeffnet ist, spiele ihn ab:
			if (currClip.isOpen()) {
				currClip.stop();
				currClip.setMicrosecondPosition(0);
				currClip.start();
			}
			// wenn der Clip noch nicht geoeffnet ist, oeffne ihn
			else {
				try {
					currClip.open((AudioInputStream) audioInputStreams.get(str));
				} catch (LineUnavailableException ex) { // nur falls die Systemressourcen erschoepft sind, schliesse ggf.
														// andere Clips:
					removeOldClips();
					// startCleaningThread();
					currClip.open((AudioInputStream) audioInputStreams.get(str)); // todo: nicht gewaehrleistet, dass
																					// Ressourcen frei sind
				}
				currClip.setMicrosecondPosition(0);
				currClip.start();
			}

			if (!playingSounds.contains(currClip))
				playingSounds.add(currClip);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ueberprueft, ob die gestarteten (gesampleten) Audiodateien noch laufen,
	 * ansonsten werden die Ressourcen freigegeben
	 */
	private synchronized void removeOldClips() {
		for (int i = playingSounds.size() - 1; i > 0; i--) { // rueckwaerts, da Objekte aus Vektor entfernt werden koennen
			Clip testClip = (Clip) playingSounds.get(i);
			// Clips, die fertig gespielt haben, werden gestoppt und aus dem Vektor mit
			// klingenden Clips entfernt
			if (!testClip.isActive()) {
				testClip.stop();
				testClip.close();
				playingSounds.remove(i);
			}
		}
	}

	/**
	 * Thread, der ggf. Systemressourcen freigibt (momentan nicht verwendet)
	 */
	public void run() {
		while ((playingSounds.size() > 0) && (cleaningThread != null)) {
			removeOldClips();
			try {
				cleaningThread.sleep(CLEANING_THREAD_SLEEPTIME);
			} catch (Exception e) {
				break;
			}
			;
		}
		cleaningThread = null;
	}

	public void setClipVolume(int vol, String str) {
		Clip clip = (Clip) clips.get(str);
		Control[] controls = clip.getControls();
		for (int i = 0; i < controls.length; i++)
			System.out.println(controls[i]);
	}

	/**
	 * startet ggf. den Thread, der Systemressourcen freigibt
	 */
	private void startCleaningThread() {
		if ((cleaningThread == null) || (!cleaningThread.isAlive())) {
			cleaningThread = new Thread(this);
			cleaningThread.start();
		}
	}

	public void stopMidi() {
		if (looping != null) {
			looping.destroy();
			looping = null;
		} else {
			sequencer.stop();
		}
	}

	public void stopSound(String str) {
		Clip currClip = (Clip) clips.get(str);
		currClip.stop();
	}

}