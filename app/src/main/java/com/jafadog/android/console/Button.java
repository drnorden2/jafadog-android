package com.jafadog.android.console;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;


import com.jafadog.R;

/***
 * Button Class to manage and render a softbutton on the android activity
 *
 * After Tutorial by Alex Bükk
 * https://www.youtube.com/@alexbukk556
 *
 * Android Game Loop Tutorial series
 * https://www.youtube.com/playlist?list=PL2EfDMM6n_LYJdzaOQ5jZZ3Dj5L4tbAuM
 */
public class Button {

    private final int circleCenterX;
    private final int circleCenterY;
    private final int circleRadius;
    private final Paint circlePaintPressed;
    private final Paint circlePaintReleased;
    private int pointerId;


    private final int buttonKey;


    public int getCircleCenterX() {
        return circleCenterX;
    }

    public int getCircleCenterY() {
        return circleCenterY;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    private double buttonCenterToTouchDistance;
    private boolean isPressed;

    public Button(Context context, int centerPositionX, int centerPositionY, int radius, int buttonKey) {
        this.circleCenterX=centerPositionX;
        this.circleCenterY=centerPositionY;
        this.circleRadius = radius;
        this.buttonKey = buttonKey;
        circlePaintPressed = new Paint();
        circlePaintPressed.setColor(ContextCompat.getColor(context, R.color.white));
        circlePaintPressed.setStyle(Paint.Style.FILL_AND_STROKE);

        circlePaintReleased = new Paint();
        circlePaintReleased.setColor(ContextCompat.getColor(context,R.color.button_released));
        circlePaintReleased.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    public int getButtonKey() {
        return buttonKey;
    }

    final public void setPointerId(int pointerId){
        this.pointerId = pointerId;
    }

    final public int getPointerId(){
        return pointerId;
    }


    public void draw(Canvas canvas) {
        if(this.isPressed) {
            canvas.drawCircle(circleCenterX, circleCenterY, circleRadius, circlePaintPressed);
        }else {
            canvas.drawCircle(circleCenterX, circleCenterY, circleRadius, circlePaintReleased);
        }
    }

    final public boolean isPressed(double touchPositionX, double  touchPositionY) {
        buttonCenterToTouchDistance = distance(circleCenterX,circleCenterY,touchPositionX,touchPositionY);
        return buttonCenterToTouchDistance <circleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    final public boolean getIsPressed() {
        return isPressed;
    }

    final public double distance(double x1,double y1,double x2,double y2){
        return Math.sqrt(
                Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }
}
