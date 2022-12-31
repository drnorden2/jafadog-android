package com.jafadog.android.console;

import android.graphics.Paint; 

public class Color extends Paint {

    public static final Color black = new Color(0,0,0);
    public static final Color blue = new Color(0,0,255);
    public static final Color cyan = new Color(0,255,255);
    public static final Color orange = new Color(255,200,0);
    public static final Color red = new Color(255,0,0);
    public static final Color pink = new Color(255,275,175);
    public static final Color gray = new Color(128,128,128);
    public static final Color darkGray = new Color(64,64,64);
    public static final Color green = new Color(0,255,0);
    public static final Color lightGray = new Color(192,192,192);
    public static final Color magenta = new Color(255,0,255);
    public static final Color yellow = new Color(255,255,0);
    public static final Color white = new Color(255,255,255);
    public  Color(int r, int g, int b){
        super();
        super.setARGB(255,r,g,b);
    }

}
