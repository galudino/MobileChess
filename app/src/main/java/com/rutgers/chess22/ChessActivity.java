/**
 * ChessActivity.java
 *
 * Copyright (c) 2019 Patrick Nogaj, Gemuele Aludino
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package com.rutgers.chess22;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import model.PieceType;
import model.game.Game;
import model.game.Position;

class debug {
    public static final void log(String classname, String message) {
        System.out.println(String.format("[%s] %s", classname, message));
    }
}

/**
 * Represents a handle (GUI representation) for a chess game
 *
 * @version May 1, 2019
 * @author patricknogaj
 * @author gemuelealudino
 */
public class ChessActivity extends AppCompatActivity implements View.OnClickListener {

    private static int MAX_LENGTH_WIDTH = 8;

    public static final String PIECE_BR = "bR";
    public static final String PIECE_BN = "bN";
    public static final String PIECE_BB = "bB";
    public static final String PIECE_BQ = "bQ";
    public static final String PIECE_BK = "bK";
    public static final String PIECE_BP = "bP";
    public static final String PIECE_WR = "wR";
    public static final String PIECE_WN = "wN";
    public static final String PIECE_WB = "wB";
    public static final String PIECE_WQ = "wQ";
    public static final String PIECE_WK = "wK";
    public static final String PIECE_WP = "wP";
    public static final String PIECE_NO = "--";


    private static final String linearLayoutChessboardTag = "linearLayoutChessboard";
    private static final String linearLayoutTopTag = "linearLayoutTop";
    private static final String linearLayoutBottomTag = "linearLayoutBottom";

    private static final String checkBoxDrawTag = "checkBoxDraw";

    private static final String buttonAITag = "buttonAI";
    private static final String buttonUndoTag = "buttonUndo";
    private static final String buttonDrawTag = "buttonDraw";
    private static final String buttonResignTag = "buttonResign";

    private LinearLayout linearLayoutChessboard;
    private LinearLayout linearLayoutTop;
    private LinearLayout linearLayoutBottom;

    private CheckBox checkBoxDraw;

    private TextView displayTurn;

    private Button buttonAI;
    private Button buttonUndo;
    private Button buttonDraw;
    private Button buttonResign;

    private ImageView[][] board;
    private String selectedObject = null;

    private String oldTag;
    private String newTag;

    private boolean drawRequested;      // activated by checkBoxDraw
    private boolean drawAccepted;       // activated by opponent's acceptance of other player's draw

    private boolean resignRequested;    // activated by buttonResign

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        initializeChessboard(this);

        linearLayoutChessboard = findViewById(R.id.chessboard);
        linearLayoutChessboard.setTag(linearLayoutChessboardTag);

        linearLayoutTop = findViewById(R.id.linearLayoutTop);
        linearLayoutTop.setTag(linearLayoutTopTag);

        linearLayoutBottom = findViewById(R.id.linearLayoutBottom);
        linearLayoutBottom.setTag(linearLayoutBottomTag);

        checkBoxDraw = findViewById(R.id.checkDraw);
        checkBoxDraw.setTag(checkBoxDrawTag);
        checkBoxDraw.setText("Draw?");

        buttonAI = findViewById(R.id.btnAI);
        buttonAI.setTag(buttonAITag);

        buttonUndo = findViewById(R.id.btnRollback);
        buttonUndo.setTag(buttonUndoTag);

        buttonDraw = findViewById(R.id.btnDraw);
        buttonDraw.setTag(buttonDrawTag);

        buttonResign = findViewById(R.id.btnResign);
        buttonResign.setTag(buttonResignTag);

        oldTag = "";
        newTag = "";

        drawRequested = false;
        drawAccepted = false;
        resignRequested = false;

        game = new Game();

        displayTurn = (TextView) findViewById(R.id.displayTurn);
        displayTurn.setText(game.isWhitesMove() ? "White players turn" : "Black players turn");
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ImageView) {
            ImageView image = (ImageView) view;
            String tempTag = (String) image.getTag();

            int file = tempTag.charAt(0) - '0';
            int rank = tempTag.charAt(1) - '0';

            String currentObj = getSelectedObject();

            if (currentObj != null) {
                if (isSelected(tempTag)) {
                    deselectPiece();
                }

                int fileSelected = currentObj.charAt(0) - '0';
                int rankSelected = currentObj.charAt(1) - '0';

                char pieceSelected = currentObj.charAt(3);

                /**
                 * Check here to see checkBoxDraw is checked.
                 */
                drawRequested = checkBoxDraw.isChecked();

                movePiece(fileSelected, rankSelected, file, rank);
                deselectPiece();
            } else {
                selectPiece(tempTag);
            }
        } else if (view instanceof Button) {
            Button button = (Button)(view);
            String buttonTag = (String)(button.getTag());

            switch (buttonTag) {
                case buttonAITag:
                    debug.log("ChessActivity::onClick", "Clicked buttonAI");
                    break;
                case buttonUndoTag:
                    debug.log("ChessActivity::onClick", "Clicked buttonUndo");
                    break;
                case buttonDrawTag:
                    debug.log("ChessActivity::onClick", "Clicked buttonDraw");

                    /**
                     * If on previous turn,
                     * checkBoxDraw was checked (which sets drawRequested == true)
                     * and buttonDraw was pressed -- drawGranted = true.
                     */

                    drawAccepted = drawRequested;
                    if (drawAccepted) {
                        game.readInput("draw");
                        System.out.println(game.output());
                        /**
                         * At this point, the game has now ended in a draw.
                         * It is now up to the GUI to show notifications
                         * and do cleanup.
                         */

                        if (game.isDidDraw()) {
                            debug.log("[ChessActivity::onClick]",
                                    "*** END GAME HERE BY DRAW ***");
                        }
                    }

                    break;
                case buttonResignTag:
                    debug.log("ChessActivity::onClick", "Clicked buttonResign");

                    resignRequested = true;
                    if (resignRequested) {
                        game.readInput("resign");
                        System.out.println(game.output());
                        /**
                         * At this point, the game has now ended in a draw.
                         * It is now up to the GUI to show notifications
                         * and do cleanup.
                         */

                        if (game.isDidResign()) {
                            debug.log("[ChessActivity::onClick]",
                                    "*** END GAME HERE BY RESIGNATION ***");
                        }
                    }

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Activates/deactivates highlighting for a selected piece/cell
     *
     * @param file  the file to set
     * @param rank  the rank to set
     * @param selected  true if to select/highlight a graphical cell, false to deselect it
     */
    protected void setBackground(int file, int rank, boolean selected) {
        if (selected) {
            board[file][rank].setBackgroundColor(Color.GREEN);
        } else {
            if ((file + rank) % 2 == 0) {
                board[file][rank].setBackgroundColor(getResources().getColor(R.color.boardDark));
            } else {
                board[file][rank].setBackgroundColor(getResources().getColor(R.color.boardLight));
            }
        }
    }

    /**
     * Used to select a piece/cell
     *
     * @param tag string representation of a graphical piece
     */
    protected void selectPiece(String tag) {
        int file = tag.charAt(0) - '0';
        int rank = tag.charAt(1) - '0';

        deselectPiece();

        selectedObject = tag;
        setBackground(file, rank, true);
    }

    /**
     * Used to deselect a piece/cell
     */
    protected void deselectPiece() {
        if (selectedObject != null) {
            int file = selectedObject.charAt(0) - '0';
            int rank = selectedObject.charAt(1) - '0';

            selectedObject = null;

            setBackground(file, rank, false);
        }
    }

    /**
     * Determines if a piece/cell is selected, or not
     *
     * @param tag string representation of a graphical piece
     *
     * @return true if selected, false otherwise
     */
    protected boolean isSelected(String tag) {
        if (selectedObject == null) {
            return false;
        } else {
            return selectedObject.charAt(0) == tag.charAt(0)
                    && selectedObject.charAt(1) == tag.charAt(1);
        }
    }

    /**
     *
     * @param oldFile
     * @param oldRank
     * @param newFile
     * @param newRank
     */
    protected void movePiece(int oldFile, int oldRank, int newFile, int newRank) {
        if (game.isActive() == false) {
            debug.log("ChessActivity::movePiece", "Game is no longer active.");
            return;
        }

        oldTag = (String) board[oldFile][oldRank].getTag();
        newTag = (String) board[newFile][newRank].getTag();

        String inputRequest =
                translateTags(oldFile, oldRank,
                        newFile, newRank,
                        drawRequested, drawAccepted, resignRequested);

        game.readInput(inputRequest.trim());

        if (game.isValidMoveInput()) {
            /**
             * DEBUG MESSAGES
             */
            System.out.println(game.boardToString());

            game.printPostMoveLog();
            game.printMoveLog();

            if (game.isWhitesMove()) {
                game.printBlackSet();
            } else {
                game.printWhiteSet();
            }
            /**
             * END DEBUG MESSAGES
             */

            moveGraphicalPieces(oldFile, oldRank, newFile, newRank);

            if (game.didPromoteWhite() || game.didPromoteBlack()) {
                promotion(newFile, newRank);
            }

            if (game.isWillDraw()) {
                checkBoxDraw.setChecked(false);
                checkBoxDraw.setText("Draw.");

                // TODO graphical popup notifying the other player that their opponent
                // has request
                // ed a draw

                debug.log("ChessActivity::movePiece", "Draw requested");
            } else {
                checkBoxDraw.setText("Draw?");
            }

            if (game.isDidDraw()) {
                debug.log("ChessActivity::movePiece", "Game has ended in a draw.");
            }

            /**
             * If checkmate has occurred...
             */
            if (game.isActive() == false) {
                debug.log("ChessActivity::movePiece", game.getGameWinner() + " has won!");
            }



            displayTurn.setText(game.isWhitesMove() ? "White player's turn" : "Black player's turn");
        }
    }



    /**
     * Precondition: game.didPromoteWhite() or game.didPromoteBlack() must be true.
     * (it is meant to be called within an if block, hence the access modifier private)
     *
     * @param newFile file where a promotable pawn resides
     * @param newRank rank where a promotable pawn resides
     */
    private void promotion(int newFile, int newRank) {

        // TODO doPromotion() -- will default to QUEEN for now.
        PieceType pieceType = doPromotion();

        /**
         * Once the user makes a selection of their promotion preference
         * in doPromotion(), the default promotion performed within Game.java,
         * Board.java, and PieceSet.java will be overridden.
         */
        game.overridePawnPromotion(new Position(newFile, newRank), pieceType);

        /**
         *  The string representation of a Piece implies its PieceType,
         *  and its PieceType.color, and is identical in form
         *  to the public static String constants declared at the top of this class.
         */
        String tagSuffix = game.getLastMove().getLastPiece().toString();

        int imageResourceID = -1;

        switch (tagSuffix) {
            case PIECE_BR:
                imageResourceID = R.drawable.blackrook;
                break;
            case PIECE_BN:
                imageResourceID = R.drawable.blackknight;
                break;
            case PIECE_BB:
                imageResourceID = R.drawable.blackbishop;
                break;
            case PIECE_BQ:
                imageResourceID = R.drawable.blackqueen;
                break;
            case PIECE_WR:
                imageResourceID = R.drawable.whiterook;
                break;
            case PIECE_WN:
                imageResourceID = R.drawable.whiteknight;
                break;
            case PIECE_WB:
                imageResourceID = R.drawable.whitebishop;
                break;
            case PIECE_WQ:
                imageResourceID = R.drawable.whitequeen;
                break;
            default:
                /**
                 * tagSuffix should not allow this switch
                 * to hit the default case, but is here nonetheless.
                 */
                imageResourceID = R.drawable.whitequeen;
                break;
        }

        board[newFile][newRank].setImageResource(imageResourceID);
        board[newFile][newRank].setTag(newTag.substring(0, 2) + tagSuffix);
    }

    /**
     * TODO GUI NOTIFICATION OF PROMOTION
     *
     * @return
     */
    private PieceType doPromotion() {
        PieceType pieceType = PieceType.QUEEN;


        /**
         * USER makes decision here.
         */


        /**
         * Let the user choose their desired pieceType.
         * pieceType will then be assigned to
         *  PieceType.QUEEN,
         *  PieceType.BISHOP_R,
         *  PieceType.ROOK_R,
         *  PieceType.KNIGHT_R.
         *
         * If the PAWN is queen-side,
         * The PieceType varieties with a _L (B, R, or K) will be used instead.
         */

        switch (game.getPawnPromoteType()) {
            case PAWN_0:
            case PAWN_1:
            case PAWN_2:
            case PAWN_3:
                if (pieceType.equals(PieceType.BISHOP_R)) {
                    pieceType = PieceType.BISHOP_L;
                } else if (pieceType.equals(PieceType.ROOK_R)) {
                    pieceType = PieceType.ROOK_R;
                } else if (pieceType.equals(PieceType.KNIGHT_R)) {
                    pieceType = PieceType.KNIGHT_L;
                }
                break;
            default:
                break;
        }

        return pieceType;
    }

    /**
     *
     * @param oldFile
     * @param oldRank
     * @param newFile
     * @param newRank
     * @param drawRequested
     * @param drawAccepted
     * @param resignRequested
     *
     * @return
     */
    private String translateTags(int oldFile,
                                 int oldRank,
                                 int newFile,
                                 int newRank,
                                 boolean drawRequested,
                                 boolean drawAccepted,
                                 boolean resignRequested) {
        char oFile = intToChar(oldFile);
        int oRank = oldRank + 1;

        char nFile = intToChar(newFile);
        int nRank = newRank + 1;

        String result = String.format("%c%d %c%d", oFile, oRank, nFile, nRank);

        if (drawRequested) {
            result += " draw?";
        } else if (drawAccepted) {
            result = "draw";
        } else if (resignRequested) {
            result = "resign";
        }

        return result;
    }

    /**
     *
     * @param file
     *
     * @return
     */
    private char intToChar(int file) {
        char chFile;

        switch (file) {
            case 0:
                chFile = 'a';
                break;
            case 1:
                chFile = 'b';
                break;
            case 2:
                chFile = 'c';
                break;
            case 3:
                chFile = 'd';
                break;
            case 4:
                chFile = 'e';
                break;
            case 5:
                chFile = 'f';
                break;
            case 6:
                chFile = 'g';
                break;
            case 7:
                chFile = 'h';
                break;
            default:
                chFile = '-';
                break;
        }

        return chFile;
    }

    /**
     *
     * @param oldFile
     * @param oldRank
     * @param newFile
     * @param newRank
     */
    private void moveGraphicalPieces(int oldFile, int oldRank, int newFile, int newRank) {
        if (oldTag.indexOf(PIECE_BR) >= 0) {
            // set the cell of oldFile, oldRank transparent
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);

            // set the tag of within oldFile, oldRank to be "<oldFile><oldRank>PIECE_NO"
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //

            // set the cell of newFile, newRank to have the blackrook
            board[newFile][newRank].setImageResource(R.drawable.blackrook);

            // set the tag within newFile, newRank to be "<newFile><newRank>PIECE_BR"
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_BN) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackknight);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_BB) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackbishop);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_BQ) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackqueen);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_BK) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackking);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_BP) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.blackpawn);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_WR) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whiterook);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_WN) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whiteknight);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_WB) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whitebishop);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_WQ) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whitequeen);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_WK) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whiteking);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_WP) >= 0) {
            board[oldFile][oldRank].setImageResource(R.drawable.transparent);
            board[oldFile][oldRank].setTag(oldTag.substring(0, 2) + PIECE_NO);
            //
            board[newFile][newRank].setImageResource(R.drawable.whitepawn);
            board[newFile][newRank].setTag(newTag.substring(0, 2) + oldTag.substring(2));
        } else if (oldTag.indexOf(PIECE_NO) >= 0) {
        } else {
        }
    }

    /**
     *
     * @param listener
     */
    public void initializeChessboard(View.OnClickListener listener) {
        board = new ImageView[MAX_LENGTH_WIDTH][MAX_LENGTH_WIDTH];

        board[0][0] = (ImageView) findViewById(R.id.iv00);
        board[0][0].setTag("00" + PIECE_WR);
        board[1][0] = (ImageView) findViewById(R.id.iv10);
        board[1][0].setTag("10" + PIECE_WN);
        board[2][0] = (ImageView) findViewById(R.id.iv20);
        board[2][0].setTag("20" + PIECE_WB);
        board[3][0] = (ImageView) findViewById(R.id.iv30);
        board[3][0].setTag("30" + PIECE_WQ);
        board[4][0] = (ImageView) findViewById(R.id.iv40);
        board[4][0].setTag("40" + PIECE_WK);
        board[5][0] = (ImageView) findViewById(R.id.iv50);
        board[5][0].setTag("50" + PIECE_WB);
        board[6][0] = (ImageView) findViewById(R.id.iv60);
        board[6][0].setTag("60" + PIECE_WN);
        board[7][0] = (ImageView) findViewById(R.id.iv70);
        board[7][0].setTag("70" + PIECE_WR);

        board[0][1] = (ImageView) findViewById(R.id.iv01);
        board[0][1].setTag("01" + PIECE_WP);
        board[1][1] = (ImageView) findViewById(R.id.iv11);
        board[1][1].setTag("11" + PIECE_WP);
        board[2][1] = (ImageView) findViewById(R.id.iv21);
        board[2][1].setTag("21" + PIECE_WP);
        board[3][1] = (ImageView) findViewById(R.id.iv31);
        board[3][1].setTag("31" + PIECE_WP);
        board[4][1] = (ImageView) findViewById(R.id.iv41);
        board[4][1].setTag("41" + PIECE_WP);
        board[5][1] = (ImageView) findViewById(R.id.iv51);
        board[5][1].setTag("51" + PIECE_WP);
        board[6][1] = (ImageView) findViewById(R.id.iv61);
        board[6][1].setTag("61" + PIECE_WP);
        board[7][1] = (ImageView) findViewById(R.id.iv71);
        board[7][1].setTag("71" + PIECE_WP);

        board[0][2] = (ImageView) findViewById(R.id.iv02);
        board[0][2].setTag("02" + PIECE_NO);
        board[1][2] = (ImageView) findViewById(R.id.iv12);
        board[1][2].setTag("12" + PIECE_NO);
        board[2][2] = (ImageView) findViewById(R.id.iv22);
        board[2][2].setTag("22" + PIECE_NO);
        board[3][2] = (ImageView) findViewById(R.id.iv32);
        board[3][2].setTag("32" + PIECE_NO);
        board[4][2] = (ImageView) findViewById(R.id.iv42);
        board[4][2].setTag("42" + PIECE_NO);
        board[5][2] = (ImageView) findViewById(R.id.iv52);
        board[5][2].setTag("52" + PIECE_NO);
        board[6][2] = (ImageView) findViewById(R.id.iv62);
        board[6][2].setTag("62" + PIECE_NO);
        board[7][2] = (ImageView) findViewById(R.id.iv72);
        board[7][2].setTag("72" + PIECE_NO);

        board[0][3] = (ImageView) findViewById(R.id.iv03);
        board[0][3].setTag("03" + PIECE_NO);
        board[1][3] = (ImageView) findViewById(R.id.iv13);
        board[1][3].setTag("13" + PIECE_NO);
        board[2][3] = (ImageView) findViewById(R.id.iv23);
        board[2][3].setTag("23" + PIECE_NO);
        board[3][3] = (ImageView) findViewById(R.id.iv33);
        board[3][3].setTag("33" + PIECE_NO);
        board[4][3] = (ImageView) findViewById(R.id.iv43);
        board[4][3].setTag("43" + PIECE_NO);
        board[5][3] = (ImageView) findViewById(R.id.iv53);
        board[5][3].setTag("53" + PIECE_NO);
        board[6][3] = (ImageView) findViewById(R.id.iv63);
        board[6][3].setTag("63" + PIECE_NO);
        board[7][3] = (ImageView) findViewById(R.id.iv73);
        board[7][3].setTag("73" + PIECE_NO);

        board[0][4] = (ImageView) findViewById(R.id.iv04);
        board[0][4].setTag("04" + PIECE_NO);
        board[1][4] = (ImageView) findViewById(R.id.iv14);
        board[1][4].setTag("14" + PIECE_NO);
        board[2][4] = (ImageView) findViewById(R.id.iv24);
        board[2][4].setTag("24" + PIECE_NO);
        board[3][4] = (ImageView) findViewById(R.id.iv34);
        board[3][4].setTag("34" + PIECE_NO);
        board[4][4] = (ImageView) findViewById(R.id.iv44);
        board[4][4].setTag("44" + PIECE_NO);
        board[5][4] = (ImageView) findViewById(R.id.iv54);
        board[5][4].setTag("54" + PIECE_NO);
        board[6][4] = (ImageView) findViewById(R.id.iv64);
        board[6][4].setTag("64" + PIECE_NO);
        board[7][4] = (ImageView) findViewById(R.id.iv74);
        board[7][4].setTag("74" + PIECE_NO);

        board[0][5] = (ImageView) findViewById(R.id.iv05);
        board[0][5].setTag("05" + PIECE_NO);
        board[1][5] = (ImageView) findViewById(R.id.iv15);
        board[1][5].setTag("15" + PIECE_NO);
        board[2][5] = (ImageView) findViewById(R.id.iv25);
        board[2][5].setTag("25" + PIECE_NO);
        board[3][5] = (ImageView) findViewById(R.id.iv35);
        board[3][5].setTag("35" + PIECE_NO);
        board[4][5] = (ImageView) findViewById(R.id.iv45);
        board[4][5].setTag("45" + PIECE_NO);
        board[5][5] = (ImageView) findViewById(R.id.iv55);
        board[5][5].setTag("55" + PIECE_NO);
        board[6][5] = (ImageView) findViewById(R.id.iv65);
        board[6][5].setTag("65" + PIECE_NO);
        board[7][5] = (ImageView) findViewById(R.id.iv75);
        board[7][5].setTag("75" + PIECE_NO);

        board[0][6] = (ImageView) findViewById(R.id.iv06);
        board[0][6].setTag("06" + PIECE_BP);
        board[1][6] = (ImageView) findViewById(R.id.iv16);
        board[1][6].setTag("16" + PIECE_BP);
        board[2][6] = (ImageView) findViewById(R.id.iv26);
        board[2][6].setTag("26" + PIECE_BP);
        board[3][6] = (ImageView) findViewById(R.id.iv36);
        board[3][6].setTag("36" + PIECE_BP);
        board[4][6] = (ImageView) findViewById(R.id.iv46);
        board[4][6].setTag("46" + PIECE_BP);
        board[5][6] = (ImageView) findViewById(R.id.iv56);
        board[5][6].setTag("56" + PIECE_BP);
        board[6][6] = (ImageView) findViewById(R.id.iv66);
        board[6][6].setTag("66" + PIECE_BP);
        board[7][6] = (ImageView) findViewById(R.id.iv76);
        board[7][6].setTag("76" + PIECE_BP);

        board[0][7] = (ImageView) findViewById(R.id.iv07);
        board[0][7].setTag("07" + PIECE_BR);
        board[1][7] = (ImageView) findViewById(R.id.iv17);
        board[1][7].setTag("17" + PIECE_BN);
        board[2][7] = (ImageView) findViewById(R.id.iv27);
        board[2][7].setTag("27" + PIECE_BB);
        board[3][7] = (ImageView) findViewById(R.id.iv37);
        board[3][7].setTag("37" + PIECE_BQ);
        board[4][7] = (ImageView) findViewById(R.id.iv47);
        board[4][7].setTag("47" + PIECE_BK);
        board[5][7] = (ImageView) findViewById(R.id.iv57);
        board[5][7].setTag("57" + PIECE_BB);
        board[6][7] = (ImageView) findViewById(R.id.iv67);
        board[6][7].setTag("67" + PIECE_BN);
        board[7][7] = (ImageView) findViewById(R.id.iv77);
        board[7][7].setTag("77" + PIECE_BR);

        for (int file = 0; file <= 7; file++) {
            for (int rank = 0; rank <= 7; rank++) {
                setBackground(file, rank, false);

                if (listener != null) {
                    board[file][rank].setOnClickListener(listener);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public String getSelectedObject() {
        return selectedObject;
    }
}