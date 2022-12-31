package com.jafadog.android.console;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/***
 * Console Loop implements the active thread that runs the game loop
 *
 * After Tutorial by Alex Bükk
 * https://www.youtube.com/@alexbukk556
 *
 * Android Game Loop Tutorial series
 * https://www.youtube.com/playlist?list=PL2EfDMM6n_LYJdzaOQ5jZZ3Dj5L4tbAuM
 */
public class ConsoleLoop extends Thread{
    public static final double MAX_UPS = 60.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;

    private boolean isRunning =false;
    private Console console;
    private SurfaceHolder surfaceHolder;
    private double averageUPS;
    private double averageFPS;



    public double getAverageUPS(){
        return averageUPS;
    }
    public double getAverageFPS() {
        return averageFPS;
    }



    public ConsoleLoop(Console console, SurfaceHolder surfaceHolder) {
        this.console = console;
        this.surfaceHolder = surfaceHolder;
    }


    public void startLoop() {
        this.isRunning=true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount  =0;
        int frameCount =0;
        long startTime;
        long elapsedTime;
        long sleepTime;

        //

        startTime = System.currentTimeMillis();
        while(isRunning){
            //Try to update and render
            Canvas canvas=null;
            boolean locked = true;
            try {
                canvas= surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    console.update();
                    updateCount++;
                    console.draw(canvas);
                }
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    locked =false;
                    frameCount++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }catch (IllegalArgumentException exception){
                exception.printStackTrace();
            }finally {
                if(canvas!=null && locked){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    }catch (Exception e) {
                        e.printStackTrace();
                   }
                }
            }

            //pause game Loop to not exceed target UPS.
            elapsedTime=System.currentTimeMillis()-startTime;
            sleepTime = (long)(updateCount*UPS_PERIOD-elapsedTime);
            if(sleepTime>0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            //Skip frames
            while(sleepTime<0 && updateCount < MAX_UPS-1){
                console.update();
                updateCount++;
                elapsedTime=System.currentTimeMillis()-startTime;
                sleepTime = (long)(updateCount*UPS_PERIOD-elapsedTime);
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime>=1000){
                averageUPS = updateCount / (1E-3 * elapsedTime);
                averageFPS = frameCount / (1E-3 * elapsedTime);
                updateCount=0;
                frameCount=0;
                startTime = System.currentTimeMillis();
            }

        }
    }

    public void stopLoop() {
        this.isRunning=false;
        try{
            join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
