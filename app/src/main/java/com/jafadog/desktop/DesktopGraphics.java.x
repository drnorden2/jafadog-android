package jafadog.desktop;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.net.URL;


public class DesktopGraphics extends jafadog.Graphics {
	private final Color[] COLOR = { Color.black, Color.blue, Color.cyan, Color.orange, Color.red, Color.pink,
			Color.gray, Color.darkGray, Color.green, Color.lightGray, Color.magenta, Color.yellow, Color.white,
			new Color(0, 0, 0), /* from here the DOS COLORS with an offset of 12 */
			new Color(0, 0, 128), new Color(0, 128, 0), new Color(0, 128, 128), new Color(128, 0, 0),
			new Color(128, 0, 128), new Color(128, 128, 0), new Color(192, 192, 192), new Color(128, 128, 128),
			new Color(0, 0, 255), new Color(0, 255, 0), new Color(0, 255, 255), new Color(255, 0, 0),
			new Color(255, 0, 255), new Color(255, 255, 0), new Color(255, 255, 255) };	

	@Override
	public char[][] bindGraphicsBanner(String graphicName, int beginAscii, int xSquares, int ySquares) {
		char[][] array = new char[xSquares][ySquares];

		BufferedImage bm = (BufferedImage) getImage(graphicName);

		int scaleX = bm.getWidth()/xSquares;
        int scaleY = bm.getHeight()/ySquares;
        if(scaleX*xSquares<bm.getWidth())scaleX++;
        if(scaleY*ySquares<bm.getHeight())scaleY++;


        for (int i = 0; i < ySquares; i++) {
			for (int j = 0; j < xSquares; j++) {
                try {
                	BufferedImage curBM = bm.getSubimage(j * scaleX, i * scaleY,
                            Math.min(scaleX,(bm.getWidth()-1-(j * scaleX))) ,
                            Math.min(scaleY,(bm.getHeight()-1-(i * scaleY))));
                  	char key = (char) beginAscii++;
                    array[j][i] = key;
                    this.bindGraphics(key, curBM);
                }catch(Exception e){
                    e.printStackTrace();
                }
			}
		}

		return array;
	}

	@Override
	public Object getImage(String path) {
		BufferedImage bm = null;
		

        try {

    		path = "../" + path;
    		Image image = null;
    		URL url = getClass().getResource(path);
    		if(url == null) {
    			throw new RuntimeException("Not found:"+path);
    		}
        	bm = ImageIO.read(url);
    		
        } catch (Exception e) {
            e.printStackTrace();
        }

		return bm;
	}
	
	@Override
	public Object getUncachedCharAsImage(char c, int bg, int fg) {
		BufferedImage bm = null;
		/*
		if (c == (char) 1) {
			bm= new BufferedImage(scaleX, scaleY,BufferedImage.TYPE_INT_ARGB);
	        Graphics2D graph = bm.createGraphics();
	        graph.setColor(new Color(fg));
	        graph.fill(new Rectangle(0, 0, scaleX, scaleY));
	        graph.dispose();
		} else {
		    int[] pixel = this.getCharToArray(c, COLOR[fg].getRGB(), COLOR[bg].getRGB());
		    bm = getTransformedBM(pixel, 8, 8, scaleX, scaleY);
		}*/
	    int[] pixel = this.getCharToArray(c, COLOR[fg].getRGB(), COLOR[bg].getRGB());
	    bm = getTransformedBM(pixel, 8, 8, scaleX, scaleY);
	
		return bm;
	}
	

	@Override
	public boolean bindGraphics(char c, Object image) {
		BufferedImage bm = (BufferedImage)image;
		try {
			if (bm.getWidth() != scaleX || bm.getHeight() != scaleY ) {
				int[] pixel = this.getBMToArray(bm, bm.getWidth(), bm.getHeight());
				bm = this.getTransformedBM(pixel, bm.getWidth(), bm.getHeight(), scaleX, scaleY);
			}

			binding.put("" + c, bm);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	

	@Override
	public void setColor(Object graphics, int color) {
		((Graphics) graphics).setColor(COLOR[color]);
	}




	@Override
	public void drawImage(Object graphics, Object im, int posX, int posY, int width, int height, int color) {
		((Graphics) graphics).drawImage((BufferedImage)im, posX, posY, width, height, new Color(color), null);
	}




	@Override
	public void fillRect(Object graphics, int posX, int posY, int w, int h) {
		((Graphics) graphics).fillRect(posX, posY, w, h);
		
	}

	@Override
	public void drawRect(Object graphics, int posX, int posY, int w, int h) {
		((Graphics) graphics).drawRect(posX, posY, w, h);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	




	

	private int[] getBMToArray(BufferedImage bitmap, int scaleX, int scaleY) {
		int[] pixel = null;
		try {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            pixel = new int[w*h];
            
            pixel = bitmap.getRGB(0, 0, w, h, pixel, 0, w);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		return pixel;
	}

	private BufferedImage getTransformedBM(int[] pixel, int origX, int origY, int scaleX, int scaleY) {
		BufferedImage curBM = null;
		int[] array = new int[scaleY * scaleX];

		double fx = ((double) origX) / (double) scaleX;
		double fy = ((double) origY) / (double) scaleY;
		for (int i = 0; i < scaleX; i++) {
			for (int j = 0; j < scaleY; j++) {
				int x = (int) (((double) i) * fx);
				int y = (int) (((double) j) * fy);
				int index = (y * (origX)) + x;

				array[i + scaleX * j] = pixel[index];


			}

		}

		curBM = new BufferedImage(scaleX,scaleY,BufferedImage.TYPE_INT_ARGB);
		try {
			curBM.setRGB(0, 0, scaleX,scaleY,array, 0, scaleX);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return curBM;
	}

	

	
}

