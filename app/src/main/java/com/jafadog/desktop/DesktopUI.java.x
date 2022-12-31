package jafadog.desktop;

import jafadog.*;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;




public class DesktopUI extends Panel implements UserInterface{

	private Thread innerThread;	
	
	private static int OFF_X = 10;
	private static int OFF_Y = 30;
	private DesktopAboutBox box;
	private Image offScreenBuffer;
	private int resX; 
	private int resY;
	private int scaleX; 
	private int scaleY;
	private AbstractApp app;
	
	
	
	@Override
	public void create(AbstractApp app,String title, int resX, int resY, int scaleX, int scaleY) {
		System.out.println("Create DesktopUI");
		
		this.app = app;
		this.resX = resX;
		this.resY = resY;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		
		Frame frame = new Frame() {
			protected void processWindowEvent(WindowEvent e) {
				super.processWindowEvent(e);
				if (e.getID() == WindowEvent.WINDOW_CLOSING) {
					System.exit(0);
				}
			}

			public void setTitle(String title) {
				super.setTitle(title);
				enableEvents(AWTEvent.WINDOW_EVENT_MASK);
			}

		};
		
		frame.setTitle(title);
		frame.add(this, BorderLayout.CENTER);
		
		frame.setSize(resX * scaleX + OFF_X, resY * scaleY + OFF_Y);
		
		frame.setBackground(Color.black);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
		frame.setVisible(true);
		
	}
	


	public final synchronized void paint(Graphics g) {
		//System.out.println("Called paint in DesktopUI");
		if (g != null && offScreenBuffer != null ) {
			//System.out.println("...and drew an Image :)");
			g.drawImage(offScreenBuffer, 0, 0, this);
		}
	}
	
	public void showBox() {
		if (box == null) {
			box = new DesktopAboutBox(this);
		}
		box.show();
		box.validate();
	}
	
	@Override
	public final void update() {
	//	System.out.println("Called update() in DesktopUI");
		update(getGraphics());
	}
	int counter =0;
	
	public final synchronized void update(Graphics g) {
		try {
			while (g == null || app == null || !this.isVisible()) {
				wait(100);
				g = getGraphics();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		Graphics gr;

		// Will hold the graphics context from the offScreenBuffer.
		// We need to make sure we keep our offscreen buffer the same size
		// as the graphics context we're working with.

		if (offScreenBuffer == null) {
			offScreenBuffer = this.createImage(resX  * scaleX,
						resY *scaleY);
			requestFocus();
		}
		// only request focus if no About win!

		// We need to use our buffer Image as a Graphics object:
		gr = offScreenBuffer.getGraphics();
		app.paint(gr);
		// Passes our off-screen buffer to our paint method, which,
		// unsuspecting, paints on it just as it would on the Graphics
		// passed by the browser or applet viewer.
		g.drawImage(offScreenBuffer, 0, 0, this);
		// And now we transfer the info in the buffer onto the
		// graphics context we got from the browser in one smoothmotion.
		
	}

	public final void start() {
		innerThread.resume();

	}

	public final void stop() {
		innerThread.suspend();

	}


}