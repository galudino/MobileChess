package com.rutgers.chess22;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import model.game.Game;

public class PlaybackGame extends ChessActivity implements View.OnClickListener {

    private TextView playersTurn;

    private Button btnBackward;
    private Button btnForward;
    private Button btnMainMenu;

    private List<String> stringMoveList;

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_game);

        playersTurn = findViewById(R.id.playersTurn);
        playersTurn.setText(game.isWhitesMove() ? "Whites turn" : "Blacks turn");

        btnBackward = findViewById(R.id.btnBackward);
        btnForward = findViewById(R.id.btnForward);
        btnMainMenu = findViewById(R.id.btnMainMenu);


        initializeChessboard(this);

        stringMoveList = null;

        Intent intent = getIntent();
        String fileName = intent.getExtras().getString(LoadGame.INTENT_DATA_KEY_FILENAME);

        String fullPath = getFilesDir().getAbsolutePath() + File.separator + fileName;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_game);

        playersTurn = findViewById(R.id.playersTurn);
        playersTurn.setText(game.isWhitesMove() ? "Whites turn" : "Blacks turn");

        btnBackward = findViewById(R.id.btnBackward);
        btnForward = findViewById(R.id.btnForward);
        btnMainMenu = findViewById(R.id.btnMainMenu);

        initializeChessboard(this);

        String input = "";

        Intent intent = getIntent();
        String fileName = intent.getExtras().getString(LoadGame.INTENT_DATA_KEY_FILENAME);
        Object obj = readObjectFromFile(getFilesDir(), fileName);
        if(obj != null) {
            System.out.println("Opened: " + fileName);
            input += (String) obj;
        }

        System.out.println(input);



    }

    public static Object readObjectFromFile(File parentDir, String fileName) {
        String filePath = parentDir.getPath() + File.separator + fileName;
        Object obj = null;
        //
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try {
            fileIn = new FileInputStream(filePath);
            in = new ObjectInputStream(fileIn);
            obj = in.readObject();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    @Override
    public void onClick(View v) {


        if(v instanceof Button) {
            if(v == btnBackward) {

            } else if(v == btnForward) {

            } else if(v == btnMainMenu) {
                Intent mainMenu = new Intent(PlaybackGame.this, MainActivity.class);
                startActivity(mainMenu);
            } else {

            }
        }
    }
}
