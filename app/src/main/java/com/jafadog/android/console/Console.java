package com.jafadog.android.console;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.jafadog.R;

/***
 * Console manages all Console Objects in the game
 *
 * After Tutorial by Alex B?kk
 * https://www.youtube.com/@alexbukk556
 *
 * Android Game Loop Tutorial series
 * https://www.youtube.com/playlist?list=PL2EfDMM6n_LYJdzaOQ5jZZ3Dj5L4tbAuM
 */

public class Console extends SurfaceView implements SurfaceHolder.Callback{
    private final Cartridge cartridge;
    private final Joystick joystick;
    private final Button buttonA;
    private final Button buttonB;
    private Button[] all;
    private ConsoleLoop gameLoop;


    public Console(Context context, Cartridge cartrigde) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new ConsoleLoop(this,surfaceHolder);
        joystick = new Joystick(context,275,700,70,40);
        buttonA = new Button(context,2000,150,75,' ');
        buttonB = new Button(context,2000,350,75,27);

        Button button_S = new Button(context,200,1000,35,'1');
        Button button_P = new Button(context,400,1000,35,'\n');
        Button button_A = new Button(context,600,1000,35,'N');
        Button button_M = new Button(context,800,1000,35,'M');
        Button button_Q = new Button(context,1000,1000,35,'C');

        all = new Button[]{joystick,buttonA,buttonB,button_S,button_P,button_A,button_M,button_Q};

        this.cartridge =cartrigde;
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                int pointerIndex = event.findPointerIndex(event.getActionIndex());

                for(Button ctrl:all){
                    if (ctrl.isPressed((double) event.getX(pointerIndex), (double) event.getY(pointerIndex))) {
                        ctrl.setPointerId(event.getPointerId(event.getActionIndex()));
                        ctrl.setIsPressed(true);
                        if(ctrl.getButtonKey()!=0){
                            try {
                                cartridge.terminal.setKey(ctrl.getButtonKey());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double)event.getX(),(double)event.getY());
                    cartridge.terminal.setKey(joystick.getDirectionalKey());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                int pointerId = event.getPointerId(event.getActionIndex());
                for(Button ctrl:all){
                    if(ctrl.getPointerId()==pointerId) {
                        ctrl.setIsPressed(false);
                    }
                }
                return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop = new ConsoleLoop(this,surfaceHolder);
        gameLoop.startLoop();
        new Thread() {
            public void run() {
                cartridge.go();
            }
        }.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        cartridge.draw(canvas);
        for(Button ctrl:all) {
            ctrl.draw(canvas);
        }
        //this.drawFPS(canvas);
        //this.drawUPS(canvas);
        //System.out.println("!!!!!!!!!!!!!!█:"+(int)"█".charAt(0)+" "+(int)getAscii((int)"█".charAt(0)));
    }
    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: "+ averageUPS,100,100,paint);
    }
    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: "+ averageFPS,100,40,paint);
    }

    public void update() {
        joystick.update();
        cartridge.update();
    }

    public void pause() {
        if(gameLoop!=null) {
            gameLoop.stopLoop();
        }
    }


}
