package com.rutgers.chess22;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import model.chess_set.Board;
import model.game.Position;

public class ChessActivity extends AppCompatActivity implements View.OnClickListener {

    public final String PIECE_BR = "BR";
    public final String PIECE_BN = "BN";
    public final String PIECE_BB = "BB";
    public final String PIECE_BQ = "BQ";
    public final String PIECE_BK = "BK";
    public final String PIECE_BP = "BP";
    public final String PIECE_WR = "WR";
    public final String PIECE_WN = "WN";
    public final String PIECE_WB = "WB";
    public final String PIECE_WQ = "WQ";
    public final String PIECE_WK = "WK";
    public final String PIECE_WP = "WP";
    public final String PIECE_NO = "--";

    ImageView player;

    private ImageView[][] board = new ImageView[8][8];
    private String selectedObject = null;

    Board boardobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        boardobj = new Board();
        initializeChessboard(this);
    }

    protected void setBackground(int file, int rank, boolean selected) {
        if(selected) {
            board[file][rank].setBackgroundColor(Color.GREEN);
        } else {
            if((file + rank) % 2 == 0) {
                board[file][rank].setBackgroundColor(getResources().getColor(R.color.boardDark));
            } else {
                board[file][rank].setBackgroundColor(getResources().getColor(R.color.boardLight));
            }
        }
    }

    protected boolean isSelected(String tag) {
        if(selectedObject == null)
            return false;
        else
            return selectedObject.charAt(0) == tag.charAt(0) && selectedObject.charAt(1)== tag.charAt(1);
    }

    protected void selectPiece(String tag) {
        int file = tag.charAt(0) - '0';
        int rank = tag.charAt(1) - '0';
        deselectPiece();
        selectedObject = tag;
        setBackground(file, rank, true);
    }

    protected void deselectPiece() {
        if(selectedObject != null) {
            int file = selectedObject.charAt(0) - '0';
            int rank = selectedObject.charAt(1) - '0';
            selectedObject = null;
            setBackground(file, rank, false);
        }
    }

    protected void movePiece(int oldFile, int oldRank, int newFile, int newRank) {
        String oldTag = (String) board[oldFile][oldRank].getTag();
        String newTag = (String) board[newFile][newRank].getTag();

        if (oldTag.indexOf(PIECE_BR)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackrook);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_BN)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackknight);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_BB)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackbishop);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_BQ)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackqueen);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_BK)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackking);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_BP)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackpawn);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_WR)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whiterook);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_WN)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whiteknight);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_WB)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whitebishop);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_WQ)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whitequeen);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_WK)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whiteking);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_WP)>=0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0,2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whitepawn);
            board[newFile][newRank].setTag(newTag.substring(0,2) + oldTag.substring(2));
        }
        else if (oldTag.indexOf(PIECE_NO)>=0) {
        } else {
        }
    }

    @Override
    public void onClick(View view) {
        if(view instanceof ImageView) {
            ImageView image = (ImageView) view;
            String tempTag = (String) image.getTag();
            int file = tempTag.charAt(0) - '0';
            int rank = tempTag.charAt(1) - '0';
            String currentObj = getSelectedObject();

            if(currentObj != null) {
                if(isSelected(tempTag))
                    deselectPiece();
                int fileSelected = currentObj.charAt(0) - '0';
                int rankSelected = currentObj.charAt(1) - '0';
                char pieceSelected = currentObj.charAt(3);
                movePiece(fileSelected, rankSelected, file, rank);
                deselectPiece();
            } else {
                selectPiece(tempTag);
            }
        }
    }

    public void initializeChessboard(View.OnClickListener listener) {
        board[0][0] = (ImageView) findViewById(R.id.iv00);
        board[0][0].setTag("00"+PIECE_WR);
        board[1][0] = (ImageView) findViewById(R.id.iv10);
        board[1][0].setTag("10"+PIECE_WN);
        board[2][0] = (ImageView) findViewById(R.id.iv20);
        board[2][0].setTag("20"+PIECE_WB);
        board[3][0] = (ImageView) findViewById(R.id.iv30);
        board[3][0].setTag("30"+PIECE_WQ);
        board[4][0] = (ImageView) findViewById(R.id.iv40);
        board[4][0].setTag("40"+PIECE_WK);
        board[5][0] = (ImageView) findViewById(R.id.iv50);
        board[5][0].setTag("50"+PIECE_WB);
        board[6][0] = (ImageView) findViewById(R.id.iv60);
        board[6][0].setTag("60"+PIECE_WN);
        board[7][0] = (ImageView) findViewById(R.id.iv70);
        board[7][0].setTag("70"+PIECE_WR);

        board[0][1] = (ImageView) findViewById(R.id.iv01);
        board[0][1].setTag("01"+PIECE_WP);
        board[1][1] = (ImageView) findViewById(R.id.iv11);
        board[1][1].setTag("11"+PIECE_WP);
        board[2][1] = (ImageView) findViewById(R.id.iv21);
        board[2][1].setTag("21"+PIECE_WP);
        board[3][1] = (ImageView) findViewById(R.id.iv31);
        board[3][1].setTag("31"+PIECE_WP);
        board[4][1] = (ImageView) findViewById(R.id.iv41);
        board[4][1].setTag("41"+PIECE_WP);
        board[5][1] = (ImageView) findViewById(R.id.iv51);
        board[5][1].setTag("51"+PIECE_WP);
        board[6][1] = (ImageView) findViewById(R.id.iv61);
        board[6][1].setTag("61"+PIECE_WP);
        board[7][1] = (ImageView) findViewById(R.id.iv71);
        board[7][1].setTag("71"+PIECE_WP);

        board[0][2] = (ImageView) findViewById(R.id.iv02);
        board[0][2].setTag("02"+PIECE_NO);
        board[1][2] = (ImageView) findViewById(R.id.iv12);
        board[1][2].setTag("12"+PIECE_NO);
        board[2][2] = (ImageView) findViewById(R.id.iv22);
        board[2][2].setTag("22"+PIECE_NO);
        board[3][2] = (ImageView) findViewById(R.id.iv32);
        board[3][2].setTag("32"+PIECE_NO);
        board[4][2] = (ImageView) findViewById(R.id.iv42);
        board[4][2].setTag("42"+PIECE_NO);
        board[5][2] = (ImageView) findViewById(R.id.iv52);
        board[5][2].setTag("52"+PIECE_NO);
        board[6][2] = (ImageView) findViewById(R.id.iv62);
        board[6][2].setTag("62"+PIECE_NO);
        board[7][2] = (ImageView) findViewById(R.id.iv72);
        board[7][2].setTag("72"+PIECE_NO);

        board[0][3] = (ImageView) findViewById(R.id.iv03);
        board[0][3].setTag("03"+PIECE_NO);
        board[1][3] = (ImageView) findViewById(R.id.iv13);
        board[1][3].setTag("13"+PIECE_NO);
        board[2][3] = (ImageView) findViewById(R.id.iv23);
        board[2][3].setTag("23"+PIECE_NO);
        board[3][3] = (ImageView) findViewById(R.id.iv33);
        board[3][3].setTag("33"+PIECE_NO);
        board[4][3] = (ImageView) findViewById(R.id.iv43);
        board[4][3].setTag("43"+PIECE_NO);
        board[5][3] = (ImageView) findViewById(R.id.iv53);
        board[5][3].setTag("53"+PIECE_NO);
        board[6][3] = (ImageView) findViewById(R.id.iv63);
        board[6][3].setTag("63"+PIECE_NO);
        board[7][3] = (ImageView) findViewById(R.id.iv73);
        board[7][3].setTag("73"+PIECE_NO);

        board[0][4] = (ImageView) findViewById(R.id.iv04);
        board[0][4].setTag("04"+PIECE_NO);
        board[1][4] = (ImageView) findViewById(R.id.iv14);
        board[1][4].setTag("14"+PIECE_NO);
        board[2][4] = (ImageView) findViewById(R.id.iv24);
        board[2][4].setTag("24"+PIECE_NO);
        board[3][4] = (ImageView) findViewById(R.id.iv34);
        board[3][4].setTag("34"+PIECE_NO);
        board[4][4] = (ImageView) findViewById(R.id.iv44);
        board[4][4].setTag("44"+PIECE_NO);
        board[5][4] = (ImageView) findViewById(R.id.iv54);
        board[5][4].setTag("54"+PIECE_NO);
        board[6][4] = (ImageView) findViewById(R.id.iv64);
        board[6][4].setTag("64"+PIECE_NO);
        board[7][4] = (ImageView) findViewById(R.id.iv74);
        board[7][4].setTag("74"+PIECE_NO);

        board[0][5] = (ImageView) findViewById(R.id.iv05);
        board[0][5].setTag("05"+PIECE_NO);
        board[1][5] = (ImageView) findViewById(R.id.iv15);
        board[1][5].setTag("15"+PIECE_NO);
        board[2][5] = (ImageView) findViewById(R.id.iv25);
        board[2][5].setTag("25"+PIECE_NO);
        board[3][5] = (ImageView) findViewById(R.id.iv35);
        board[3][5].setTag("35"+PIECE_NO);
        board[4][5] = (ImageView) findViewById(R.id.iv45);
        board[4][5].setTag("45"+PIECE_NO);
        board[5][5] = (ImageView) findViewById(R.id.iv55);
        board[5][5].setTag("55"+PIECE_NO);
        board[6][5] = (ImageView) findViewById(R.id.iv65);
        board[6][5].setTag("65"+PIECE_NO);
        board[7][5] = (ImageView) findViewById(R.id.iv75);
        board[7][5].setTag("75"+PIECE_NO);

        board[0][6] = (ImageView) findViewById(R.id.iv06);
        board[0][6].setTag("06"+PIECE_BP);
        board[1][6] = (ImageView) findViewById(R.id.iv16);
        board[1][6].setTag("16"+PIECE_BP);
        board[2][6] = (ImageView) findViewById(R.id.iv26);
        board[2][6].setTag("26"+PIECE_BP);
        board[3][6] = (ImageView) findViewById(R.id.iv36);
        board[3][6].setTag("36"+PIECE_BP);
        board[4][6] = (ImageView) findViewById(R.id.iv46);
        board[4][6].setTag("46"+PIECE_BP);
        board[5][6] = (ImageView) findViewById(R.id.iv56);
        board[5][6].setTag("56"+PIECE_BP);
        board[6][6] = (ImageView) findViewById(R.id.iv66);
        board[6][6].setTag("66"+PIECE_BP);
        board[7][6] = (ImageView) findViewById(R.id.iv76);
        board[7][6].setTag("76"+PIECE_BP);

        board[0][7] = (ImageView) findViewById(R.id.iv07);
        board[0][7].setTag("07"+PIECE_BR);
        board[1][7] = (ImageView) findViewById(R.id.iv17);
        board[1][7].setTag("17"+PIECE_BN);
        board[2][7] = (ImageView) findViewById(R.id.iv27);
        board[2][7].setTag("27"+PIECE_BB);
        board[3][7] = (ImageView) findViewById(R.id.iv37);
        board[3][7].setTag("37"+PIECE_BQ);
        board[4][7] = (ImageView) findViewById(R.id.iv47);
        board[4][7].setTag("47"+PIECE_BK);
        board[5][7] = (ImageView) findViewById(R.id.iv57);
        board[5][7].setTag("57"+PIECE_BB);
        board[6][7] = (ImageView) findViewById(R.id.iv67);
        board[6][7].setTag("67"+PIECE_BN);
        board[7][7] = (ImageView) findViewById(R.id.iv77);
        board[7][7].setTag("77"+PIECE_BR);

        for(int file = 0; file <= 7; file++) {
            for(int rank = 0; rank <= 7; rank++) {
                setBackground(file, rank, false);
                if(listener != null)
                    board[file][rank].setOnClickListener(listener);
            }
        }
    }

    public String getSelectedObject() {
        return selectedObject;
    }
}
