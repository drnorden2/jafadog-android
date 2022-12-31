package jafadog.desktop;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import jafadog.AbstractApp;

/**
 * <p>
 * Title: AboutBox
 * </p>
 * <p>
 * Description: Splash Screen with Info appears on rightclick on gui
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * @author Tim Pohle & Andre Fischer
 * @version 1.7
 */

public class DesktopAboutBox extends java.awt.Frame {

	// Used to identify the windows platform.
	private static final String WIN_ID = "Windows";
	// The default system browser under windows.
	private static final String WIN_PATH = "rundll32";

	// The flag to display a url.
	private static final String WIN_FLAG = "url.dll,FileProtocolHandler";

	// The default browser under unix.
	private static final String UNIX_PATH = "netscape";

	// The flag to display a url.
	private static final String UNIX_FLAG = "-remote openURL";

	/**
	 * Display a file in the system browser. If you want to display a file, you must
	 * include the absolute path name.
	 *
	 * @param url
	 *            the file's url (the url must start with either "http://" or
	 *            "file://").
	 */
	private static void displayURL(java.net.URL url) {
		displayURL(url.toString());
	}

	private static void displayURL(String url) {
		boolean windows = isWindowsPlatform();
		String cmd = null;

		try {
			if (windows) {
				// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
				cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
				Process p = Runtime.getRuntime().exec(cmd);
			} else {
				// Under Unix, Netscape has to be running for the "-remote"
				// command to work. So, we try sending the command and
				// check for an exit value. If the exit command is 0,
				// it worked, otherwise we need to start the browser.

				// cmd = 'netscape -remote openURL(http://www.javaworld.com)'
				cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
				Process p = Runtime.getRuntime().exec(cmd);
				try {
					// wait for exit code -- if it's 0, command worked,
					// otherwise we need to start the browser up.
					int exitCode = p.waitFor();
					if (exitCode != 0) {
						System.out.println("Netscape say:" + exitCode);
						// Command failed, start up the browser

						// cmd = 'netscape http://www.javaworld.com'
						// cmd = UNIX_PATH + " " + url;

						String[] cmdArgs = { "/bin/sh", "-c", "LANG=en_US;export LANG;" + UNIX_PATH + " " + url };

						p = Runtime.getRuntime().exec(cmdArgs);

						/*
						 * This code is stalling the Netscape Process
						 *
						 *
						 * exitCode = p.waitFor(); InputStream in = p.getErrorStream(); int i=in.read();
						 * while(i >0){ System.out.print((char)i); i=in.read(); } in =
						 * p.getInputStream(); i=in.read(); while(i >0){ System.out.print((char)i);
						 * i=in.read(); }
						 */
					}
				} catch (InterruptedException x) {
					System.err.println("Error bringing up browser, cmd='" + cmd + "'");
					System.err.println("Caught: " + x);
				}
			}
		} catch (IOException x) {
			// couldn't exec browser
			System.err.println("Could not invoke browser, command=" + cmd);
			System.err.println("Caught: " + x);
		}
	}

	/**
	 * Try to determine whether this application is running under Windows or some
	 * other platform by examing the "os.name" property.
	 *
	 * @return true if this application is running under a Windows OS
	 */
	private static boolean isWindowsPlatform() {
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith(WIN_ID))
			return true;
		else
			return false;
	}

	// FOR APPLICATION

	private DesktopUI a;

	private boolean isStandalone;

	private Panel panel1 = new Panel();

	private Button button1 = new Button();

	private BorderLayout borderLayout1 = new BorderLayout();

	private Panel panel2 = new Panel();

	private GridLayout gridLayout1 = new GridLayout();

	private Label label1 = new Label();
	private Label label2 = new Label();
	private Label label3 = new Label();
	private Label label4 = new Label();
	private Label label5 = new Label();
	public DesktopAboutBox(DesktopUI a) {
		this.show(false);
		this.a = a;
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void button1_actionPerformed(ActionEvent e) {

	}
	// Close the dialog
	void cancel() {
		dispose();
	}
	// Component initialization
	private void jbInit() throws Exception {
		this.setBackground(SystemColor.menuText);
		this.setTitle("About JAFA DOG");
		setResizable(true);
		button1.setForeground(Color.blue);
		button1.setLabel("http://kickme.to/computerag");
		button1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showWebPage();
			}
		});
		panel1.setLayout(borderLayout1);
		panel2.setLayout(gridLayout1);
		gridLayout1.setRows(5);
		label1.setAlignment(label1.CENTER);
		label1.setBackground(Color.black);
		label1.setForeground(Color.green);
		label1.setText("this game was developed with ");
		label2.setAlignment(label1.CENTER);
		label2.setBackground(SystemColor.desktop);
		label2.setFont(new java.awt.Font("Dialog", 1, 25));
		label2.setForeground(Color.red);
		label2.setText("JAFA DOG  " + AbstractApp.VERSION);
		label3.setAlignment(label1.CENTER);
		label3.setBackground(Color.black);
		label3.setForeground(Color.green);
		label3.setText("JAVA API FOR ACCELERATED");
		label4.setAlignment(label1.CENTER);
		label4.setBackground(Color.black);
		label4.setForeground(Color.green);
		label4.setText("DEVELOPMENT OF GAMES");
		label5.setAlignment(label1.CENTER);
		label5.setBackground(Color.black);
		label5.setForeground(Color.green);
		label5.setText("(c) 2002 by T.Pohle & A.Fischer");
		this.add(panel1, BorderLayout.CENTER);
		panel1.add(button1, BorderLayout.SOUTH);
		panel1.add(panel2, BorderLayout.CENTER);
		panel2.add(label1, null);
		panel2.add(label2, null);
		panel2.add(label3, null);
		panel2.add(label4, null);
		panel2.add(label5, null);

	}
	// Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			cancel();
		}
		super.processWindowEvent(e);
	}
	public void show() {
		this.setSize(400, 300);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
		super.show();
	}

	void showWebPage() {
		try {
			URL url = new URL("http://kickme.to/computerag");
			this.displayURL(url);
		}

		catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}

}