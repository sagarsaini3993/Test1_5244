package com.example.sparrow;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    private GameEngine gameEngine;

    // screen size variables
    Display display;
    Point size;
    int screenHeight;
    int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();

        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        gameEngine = new GameEngine(this, screenWidth, screenHeight);
        setContentView(gameEngine);


    }


    @Override
    protected void onPause() {
        super.onPause();
        gameEngine.pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.resumeGame();
    }

}

