package com.jafadog.android.console;

import android.graphics.Canvas;

import com.jafadog.Graphics;
import com.jafadog.Terminal;

abstract public class Cartridge {
    Terminal terminal;
    Graphics graphics;
    public  Cartridge(){
        this.terminal = new Terminal();
        this.graphics = new AndroidGraphics();
        this.graphics.create(20,40);
        this.terminal.create(graphics,null,81,25,20,40);
    }

    public abstract void go();


    void update(){
        //terminal.notify();
    }


    public Terminal getTerminal(){
        return terminal;
    }

    public void draw (Canvas canvas){
        if(terminal!=null) {
            terminal.draw(canvas);
        }else{
            System.out.println("By god there is no terminal");
        }
    }
}
