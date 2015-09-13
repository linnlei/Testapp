package com.linn.testapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener{

    TextView mainTextView;
    Button startButton;
    Button scoreButton;
    Button quitButton;
    EditText mainEditText;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayAdapter sArrayAdapter;
    ArrayList mNameList = new ArrayList();
    ArrayList stationList = new ArrayList();
    String[] stations = new String[]{"1", "2", "3"};

    ShareActionProvider mShareActionProvider;
    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    private static final String LAST_STATION = "station";
    public int station;
    SharedPreferences mSharedPreferences;
    SharedPreferences sSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1. Access the TextView defined in layout XML and then set its text
        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainTextView.setText("Set in Java!");
        //2. Access the Button defined in layout XML and listen for it here
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        scoreButton = (Button) findViewById(R.id.score_button);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View dialog) {
                Toast.makeText(getApplicationContext(), "Här ska highscore finnas senare",
                        Toast.LENGTH_SHORT).show();
            }
        });

        /*
        quitButton = (Button) findViewById(R.id.quit_button);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitGame();
            }
        });*/



        //4. Access the listView
        mainListView = (ListView) findViewById(R.id.main_listview);

        //Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, mNameList);

        sArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, stationList);



        //Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        //5. Set this activity to react to list items being pressed
        mainListView.setOnItemClickListener(this);

        //7. Greet the user, or ask for their name if new user
        //displayWelcome();
        //stationEntry();



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the meny. Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        //Access the object responsible for putting together the sharing submenu
        if(shareItem != null){
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        //Create an Intent to share your content
        setShareIntent();

        return true;
    }

    private void setShareIntent() {
        if (mShareActionProvider != null){
            //Create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());

            //Make sure the provider knows it should work with that intent
            mShareActionProvider.setShareIntent(shareIntent);
        }

    }

    @Override
    public void onClick(View v) {
        //När "starta spelet" klickas på
        stationEntry();


    }

    public void startGame(int station){
        Toast.makeText(getApplicationContext(), "Du spelar nu spelet tråkig gräsplätt " + station + "!",
                Toast.LENGTH_SHORT).show();
        //Starta nytt Intent
        Intent game = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(game);
    }

    public void quitGame(){
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Log the item's position and contents to the console in Debug
        Log.d("omg android", position + ": " + mNameList.get(position));

    }

    public void displayWelcome() {
        //Access the device's key-value storage
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        //Read the user's name, or an empty string if nothing found
        String name = mSharedPreferences.getString(PREF_NAME, "");

        if (name.length() > 0) {
            //If the name is valid, display a Toast welcoming them
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        }
        else {
            //Otherwise, show a dialog to ask for their name
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hello!");
            alert.setMessage("What is your name?");

            //Create EditText for entry
            final EditText input = new EditText(this);
            alert.setView(input);

            //Make an "OK" button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    //Grab the EditText's input
                    String inputName = input.getText().toString();
                    //Put it into memory (don't forget to commit!)
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.commit();
                    //Welcome the new user
                    Toast.makeText(getApplicationContext(), "Welcome, " + inputName + "!",
                            Toast.LENGTH_LONG).show();
                }
            });

            //Make a "Cancel" button that just dismisses the alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {}
            });

            alert.show();

        }

    }



    public void stationEntry() {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Vilken hållplats befinner du dig på? (Använda gps senare?)");


            alert.setItems(stations, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 'which' argumentet innehåller index för valda platsen i listan
                    String stationName = stations[which];
                    Toast.makeText(getApplicationContext(), "Du befinner dig på hållplats " + stationName + "!",
                            Toast.LENGTH_SHORT).show();
                    station = Integer.parseInt(stationName);
                    startGame(station);

                }
            });

            //Avbryt-knapp, kanbske inte behövs
            /*alert.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {}
            });*/

        alert.show();

    }

}
