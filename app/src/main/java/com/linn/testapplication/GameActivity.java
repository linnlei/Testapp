package com.linn.testapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;


public class GameActivity extends Activity {
    /** Called when the activity is first created. */
    //Should work now

    private static final String TAG = GameActivity.class.getSimpleName();
    private FrameLayout gameFrame;
    private Button quitButton;
    private GamePanel gamePanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        //--- setContentView(new GrassGamePanel(this));
        setContentView(R.layout.activity_game);

        Log.d(TAG, "Creating gameframe");
        gameFrame = (FrameLayout) findViewById(R.id.game_frame);
        gamePanel = new GamePanel(this);
        gameFrame.addView(gamePanel);

        quitButton = (Button) findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View dialog) {
                Log.d(TAG, "Finishing activity");
                finish();
            }
        });

        Log.d(TAG, "View added");
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

    @Override
    protected void onPause(){
        Log.d(TAG, "Pausing!");
        super.onPause();
    }

    @Override
    protected void onResume(){
        Log.d(TAG, "Resuming!");
        gamePanel.runThread(true);
        super.onResume();
    }


}
