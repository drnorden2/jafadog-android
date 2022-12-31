package com.jafadog.android.console;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jafadog.Graphics;

public class AndroidGraphics extends Graphics {
    private final Color[] COLOR = { Color.black, Color.blue, Color.cyan, Color.orange, Color.red, Color.pink,
            Color.gray, Color.darkGray, Color.green, Color.lightGray, Color.magenta, Color.yellow, Color.white,
            new Color(0, 0, 0), /* from here the DOS COLORS with an offset of 12 */
            new Color(0, 0, 128), new Color(0, 128, 0), new Color(0, 128, 128), new Color(128, 0, 0),
            new Color(128, 0, 128), new Color(128, 128, 0), new Color(192, 192, 192), new Color(128, 128, 128),
            new Color(0, 0, 255), new Color(0, 255, 0), new Color(0, 255, 255), new Color(255, 0, 0),
            new Color(255, 0, 255), new Color(255, 255, 0), new Color(255, 255, 255) };


    Paint foregroundColor;
    @Override
    public char[][] bindGraphicsBanner(String graphicName, int beginAscii, int xSquares, int ySquares) {
        return new char[0][];
    }

    @Override
    public boolean bindGraphics(char c, Object o) {
        return false;
    }

    @Override
    public Object getImage(String path) {
        return null;
    }

    @Override
    public Object getUncachedCharAsImage(char c, int bg, int fg) {
        Bitmap bm = null;
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
        int[] pixel = this.getCharToArray(c, COLOR[fg].getColor(), COLOR[bg].getColor());
        bm = getTransformedBM(pixel, 8, 8, scaleX, scaleY);

        return bm;
}

    private Bitmap  getTransformedBM(int[] pixel, int origX, int origY, int scaleX, int scaleY) {
        Bitmap curBM = null;
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
        //GPT
        curBM = Bitmap.createBitmap(scaleX,scaleY, Bitmap.Config.ARGB_8888);
        //curBM = new BufferedImage(scaleX,scaleY,BufferedImage.TYPE_INT_ARGB);
        try {
            //curBM.setRGB(0, 0, scaleX,scaleY,array, 0, scaleX);
            //GPT
            curBM.setPixels(array, 0, scaleX, 0, 0, scaleX,scaleY);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return curBM;
    }

    @Override
    public void setColor(Object graphics, int color) {
        this.foregroundColor = COLOR[color];
    }

    @Override
    public void drawImage(Object graphics, Object im, int posX, int posY, int width, int height, int color) {
        //gpt
        Canvas canvas= (Canvas)graphics;
        canvas.drawBitmap((Bitmap) im, posX, posY, null);

    }

    @Override
    public void fillRect(Object graphics, int posX, int posY, int w, int h) {

    }

    @Override
    public void drawRect(Object graphics, int posX, int posY, int w, int h) {
        Canvas canvas = (Canvas)graphics;
        canvas.drawRect(posX,posY,posX+w, posY+h,foregroundColor);
    }
}