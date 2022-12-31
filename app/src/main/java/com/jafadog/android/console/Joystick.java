package com.jafadog.android.console;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;


import com.jafadog.R;

/***
 * Joystick implements a directonal controller for the touchscreen
 *
 * After Tutorial by Alex Bükk
 * https://www.youtube.com/@alexbukk556
 *
 * Android Game Loop Tutorial series
 * https://www.youtube.com/playlist?list=PL2EfDMM6n_LYJdzaOQ5jZZ3Dj5L4tbAuM
 */

public class Joystick extends Button{
    public static final int KEY_LEFT = 1037;
    public static final int KEY_UP = 1038;
    public static final int KEY_RIGHT = 1039;
    public static final int KEY_DOWN = 1040;

    private int innerCircleCenterX;
    private int innerCircleCenterY;
    private final int innerCircleRadius;

    private final Paint innerCirclePaint;

    private double joystickCenterToTouchDistance;
    private double actuatorX;
    private double actuatorY;


    public Joystick(Context context, int centerPositionX, int centerPositionY, int outerCircleRadius, int innerCircleRadius) {
        super(context,centerPositionX,centerPositionY,outerCircleRadius,0);
        this.innerCircleCenterX=centerPositionX;
        this.innerCircleCenterY=centerPositionY;
        this.innerCircleRadius = innerCircleRadius;


        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(ContextCompat.getColor(context,R.color.inner_circle));
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterX = (int)(getCircleCenterX()+actuatorX*getCircleRadius());
        innerCircleCenterY = (int)(getCircleCenterY()+actuatorY*getCircleRadius());

    }
    public int getDirectionalKey() {
        if (this.actuatorX== 0 &&this.actuatorY==0) {
            return 0;
        }
        //GPT
        double angleRad  = Math.atan2(this.actuatorX,this.actuatorY);

        double angle = Math.toDegrees(angleRad/2);
        if(angle<0){
            angle = 180+angle;
        }
        angle =angle*2;


        System.err.println("!!Angle:"+angle);
        if (angle >= 315 || angle < 45) {
            return KEY_DOWN;
        } else if (angle >= 45 & angle < 115) {
            return KEY_RIGHT;
        } else if (angle >= 135 & angle < 225) {
            return KEY_UP;
        } else  {
            return KEY_LEFT;
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle(innerCircleCenterX,innerCircleCenterY,innerCircleRadius,innerCirclePaint);
    }

    public void resetActuator() {
        actuatorX = 0;
        actuatorY = 0;
    }
    public void setIsPressed(boolean isPressed) {
        super.setIsPressed(isPressed);
        if(isPressed==false){
            resetActuator();
        }
    }


        public void setActuator(double touchPositionX, double  touchPositionY) {
        double deltaX = touchPositionX - getCircleCenterX();
        double deltaY = touchPositionY - getCircleCenterY();
        double deltaDistance = distance(getCircleCenterX(),getCircleCenterY(),touchPositionX,touchPositionY);
        if(deltaDistance < getCircleRadius()){
            actuatorX = deltaX/getCircleRadius();
            actuatorY = deltaY/getCircleRadius();
        }else{
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }
    }


    public double getActuatorX() {
        return actuatorX;
    }
    public double getActuatorY() {
        return actuatorY;
    }
}
