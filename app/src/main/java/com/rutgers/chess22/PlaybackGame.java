package com.rutgers.chess22;

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
        playersTurn.setText(game.isWhitesMove() ? "Whites turn" : "Blacks turn");

        btnBackward = findViewById(R.id.btnBackward);
        btnForward = findViewById(R.id.btnForward);
        btnMainMenu = findViewById(R.id.btnMainMenu);


        initializeChessboard(this);
    }

    @Override
    public void onClick(View v) {

    }
}
