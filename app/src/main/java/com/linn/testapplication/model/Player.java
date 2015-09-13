package com.linn.testapplication.model;

/**
 * Created by Linn on 2015-09-13.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

public class Player {

    private static final String TAG = Player.class.getSimpleName();
    private Bitmap bitmap;	// the actual bitmap
    private int x;			// the X coordinate
    private int y;			// the Y coordinate
    private boolean touched;	// if droid is touched/picked up
    private boolean screenTouch;
    private Speed speed;	// the speed with its directions
    public int xGoal;
    public int yGoal;

    public Player(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = new Speed();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setScreenTouch(boolean b) {
        this.screenTouch = b;
    }

    public boolean isScreenTouch(){
        return screenTouch;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }

    /**
     * Method which updates the droid's internal state every tick
     */
    public void update() {
        /*if (!touched) {
            x += (speed.getXv() * speed.getxDirection());
            y += (speed.getYv() * speed.getyDirection());
        }*/

        if (screenTouch) {
            Log.d(TAG, "Screen touched");
        }

        //Variant där avataren rör sig i den satta riktningen
        /*if (screenTouch) {
            x += ( 10 * speed.getxDirection() );
            y += ( 10 * speed.getyDirection() );
        }*/

        //Variant där avataren rör sig mot mål-koordinaten
        if (screenTouch) {

            if(x < xGoal) x += 10;
            if(x > xGoal) x -= 10;
            if(y < yGoal) y += 10;
            if(y > yGoal) y -= 10;

            /*
            x += ( 10 * speed.getxDirection() );
            y += ( 10 * speed.getyDirection() );*/
        }


    }


    /**
     * Handles the {@link MotionEvent.ACTION_DOWN} event. If the event happens on the
     * bitmap surface then the touched state is set to <code>true</code> otherwise to <code>false</code>
     * @param eventX - the event's X coordinate
     * @param eventY - the event's Y coordinate
     *
     *               --ANVÄNDS EJ I DENNA VERSION
     */
    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
            if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
                // avatar touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }

    }
}


