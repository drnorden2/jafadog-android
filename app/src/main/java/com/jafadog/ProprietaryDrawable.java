package com.jafadog;



public interface ProprietaryDrawable {

	void setColor(Object graphics, int color);
	
	void drawImage(Object graphics, Object im, int posX, int posY, int width, int height, int color);
	
	void fillRect(Object graphics, int posX, int posY, int w, int h);
	
	void drawRect(Object graphics, int posX ,int posY , int w , int h);
	

	/*
	public boolean bindGraphics(char c, String graphicName);
	public char[][] bindGraphicsBanner(String graphicName, int beginAscii, int xSquares, int ySquares);
	public Object getGraphics(char c);
	public boolean isBound(char c);
	int[] getCharToArray(char c, int fgCode, int bgCode );
	*/
}