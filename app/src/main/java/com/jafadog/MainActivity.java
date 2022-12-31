package com.jafadog;

import android.app.Activity;
import android.os.Bundle;

import com.jafadog.android.console.Cartridge;
import com.jafadog.android.console.Console;
import com.jafadog.games.ASCII;
import com.jafadog.games.Kroz;
import com.jafadog.games.Nibbles;

/**
 * Entry point to the application
 *
 * After Tutorial by Alex Bükk
 * https://www.youtube.com/@alexbukk556
 *
 * Android Game Loop Tutorial series
 * https://www.youtube.com/playlist?list=PL2EfDMM6n_LYJdzaOQ5jZZ3Dj5L4tbAuM
 */
public class MainActivity extends Activity {

    private Console console;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set content view to the game, so that objects in the game class can be rendered to the screen
        Cartridge cardridge = new Nibbles();
        console = new Console(this,cardridge);
        setContentView(console);
    }

    @Override
    protected void onPause() {
        console.pause();
        super.onPause();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    public void onBackPressed() {
        //do nothing
    }
}