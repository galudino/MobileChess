package com.rutgers.chess22;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlaybackGame extends ChessActivity implements View.OnClickListener {

    private TextView playersTurn;

    private Button btnBackward;
    private Button btnForward;
    private Button btnMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_game);

        playersTurn = findViewById(R.id.playersTurn);
        playersTurn.setText(game.isWhitesMove() ? "Whites turn || ": "Blacks turn || ");

        btnBackward = findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(this);

        btnForward = findViewById(R.id.btnForward);
        btnForward.setOnClickListener(this);

        btnMainMenu = findViewById(R.id.btnMainMenu);
        btnMainMenu.setOnClickListener(this);


        initializeChessboard(this);
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
