/**
 * LoadGame.java
 *
 * Copyright (c) 2019 Patrick Nogaj, Gemuele Aludino
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package com.rutgers.chess22;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Represents a handle to load the state of a chess game
 */
public class LoadGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
    }
}
