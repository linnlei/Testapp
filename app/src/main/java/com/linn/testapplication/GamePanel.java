package com.linn.testapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.linn.testapplication.model.Player;
import com.linn.testapplication.model.Speed;

/**
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */

public class GamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = GamePanel.class.getSimpleName();

    private GameThread thread;
    private Player player;

    public GamePanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create avatar and load bitmap
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.plupp), 500, 500);

        // create the game loop thread
        thread = new GameThread(getHolder(), this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop

        thread.setRunning(true);

        //kolla om tråden redan kör, annars starta ny
        if ( thread.getState() == Thread.State.NEW )
            thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");

        /*thread.setRunning(false);
        Log.d(TAG, "Stopped thread from running");
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        thread.setRunning(false);
        Log.d(TAG, "Stopped thread from running");
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        Log.d(TAG, "Thread was shut down cleanly");
    }

    /** Exempelvariant för att flytta avataren med drag-and-drop */
    /*@Override
    public boolean onTouchEventold(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // delegating event handling to the player avatar
            player.handleActionDown((int) event.getX(), (int) event.getY());

            Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());

        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // the gestures
            if (player.isTouched()) {
                // the droid was picked up and is being dragged
                player.setX((int)event.getX());
                player.setY((int)event.getY());
            }
        } if (event.getAction() == MotionEvent.ACTION_UP) {
            // touch was released
            if (player.isTouched()) {
                player.setTouched(false);
            }
        }
        return true;
    }*/

    /** Egen variant för att flytta på avataren */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //Varianten med att sätta en riktning
            /*if((int)event.getX() < player.getX())
                player.getSpeed().setxDirection(-1);
            else if((int)event.getX() > player.getX())
                player.getSpeed().setxDirection(1);

            if((int)event.getY() < player.getY())
                player.getSpeed().setyDirection(-1);
            else if((int)event.getY() > player.getY())
                player.getSpeed().setyDirection(1);*/

            //Varianten med att använda en mål-koordinat
            player.xGoal = (int)event.getX();
            player.yGoal = (int)event.getY();

            player.setScreenTouch(true);

        } if (event.getAction() == MotionEvent.ACTION_UP) {
            // touch was released
                player.setScreenTouch(false);
        }
        return true;
    }


    public void render(Canvas canvas) {

        canvas.drawColor(Color.rgb(197, 255, 187));
        player.draw(canvas);
    }

    /**
     * This is the game update method. It iterates through all the objects
     * and calls their update method if they have one or calls specific
     * engine's update method.
     */
    public void update() {
        // check collision with right wall if heading right
        if (player.getSpeed().getxDirection() == Speed.DIRECTION_RIGHT
                && player.getX() + player.getBitmap().getWidth() / 2 >= getWidth()) {
            player.getSpeed().toggleXDirection();
        }
        // check collision with left wall if heading left
        if (player.getSpeed().getxDirection() == Speed.DIRECTION_LEFT
                && player.getX() - player.getBitmap().getWidth() / 2 <= 0) {
            player.getSpeed().toggleXDirection();
        }
        // check collision with bottom wall if heading down
        if (player.getSpeed().getyDirection() == Speed.DIRECTION_DOWN
                && player.getY() + player.getBitmap().getHeight() / 2 >= getHeight()) {
            player.getSpeed().toggleYDirection();
        }
        // check collision with top wall if heading up
        if (player.getSpeed().getyDirection() == Speed.DIRECTION_UP
                && player.getY() - player.getBitmap().getHeight() / 2 <= 0) {
            player.getSpeed().toggleYDirection();
        }
        // Update the lone droid
        player.update();
    }

    public void runThread(boolean b){
        thread.setRunning(b);
    }

}




