package jafadog.desktop;

import jafadog.*;
import jafadog.util.Point;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class DesktopInput implements Input {

	private AbstractApp app;
	private int scaleX;
	private int scaleY;
	
	// fuer mouse Events:
	private boolean mouseClicked = false;
	private Point clickedPoint = new Point(0, 0);
	private Point currentMousePoint = new Point(0, 0);
	private DesktopUI ui;
	
	public void create (AbstractApp app, Object ui, int scaleX, int scaleY) {
		System.out.print("Create DesktopInput");
		
		this.ui = (DesktopUI)ui;
		this.app = app;
		// fuer Maus
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	
		// mouse
		this.ui.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				this_mouseClicked(e);
			}
		});
		this.ui.addMouseMotionListener(new java.awt.event.MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				this_mouseMoved(e);
			}

			public void mouseMoved(MouseEvent e) {
				this_mouseMoved(e);
			}
		});
		// keyboard
		this.ui.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				this_keyPressed(e);
			}
		});		
		System.out.println("... All the listeners added!");
		
	}

	@Override
	public Point readMouseClickPos() {
		if (mouseClicked) {
			mouseClicked = false;
			Point result = new Point(clickedPoint.getX(), clickedPoint.getY());
			return result;
		} else
			return null;
	}

	
	@Override
	public Point readMousePos() {
		return new Point(currentMousePoint.getX(), this.currentMousePoint.getY());
	}

	
	@Override
	public void reportKey(int key) {
		app.terminal.setKey(key);
	}
	
	final private synchronized void this_keyPressed(KeyEvent e) {
		//System.out.println("this_keyPressed");
		
		int key = (int) e.getKeyChar();
		if (key > 255 || key == 0) {
			key = e.getKeyCode() + 1000;
		}
		reportKey(key);
	}

	final private void this_mouseClicked(MouseEvent e) {
		// right click
		//System.out.println("this_mouseClicked");
		
		if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
			this.ui.showBox();

		}

		// left click
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {

			mouseClicked = true;
			int feldX = e.getX() / scaleX;
			int feldY = e.getY() / scaleY;
			this.clickedPoint = new Point(feldX, feldY);

		}

	}

	final private void this_mouseMoved(MouseEvent e) {
		//System.out.println("this_mouseMoved");
		currentMousePoint = new Point(e.getX() / scaleX, e.getY() / scaleY);
	}


}
