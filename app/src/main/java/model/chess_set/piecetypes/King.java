/**
 * King.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj.
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.chess_set.piecetypes;

import model.PieceType;
import model.chess_set.Board;
import model.chess_set.Piece;
import model.game.Position;

/**
 * @version Apr 27, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class King extends Piece {

    private boolean castled;

    /**
     * Parameterized constructor
     *
     * @param color the Color of a Player's PieceSet
     */
    public King(PieceType.Color color) {
        super(color);
        pieceType = PieceType.KING;
        this.castled = false;

        identifier += "King      ";
    }

    /**
     * Determines if an instance of King is castled, or not
     *
     * @return true if King is castled, false otherwise
     */
    public boolean isCastled() {
        return castled;
    }

    /* (non-Javadoc)
     * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board,
     * model.game.Position)
     */
    @Override
    public boolean isMoveLegal(Board board, Position posRef) {
        boolean result = false;

        int deltaFile = Math.abs(this.posRef.getFile() - posRef.getFile());
        int deltaRank = Math.abs(this.posRef.getRank() - posRef.getRank());

        if (deltaFile <= 1 && deltaRank <= 1) {
            Piece pieceAtCell = board.getCell(posRef).getPiece();

            boolean matchesColor =
                    pieceAtCell != null ?
                            this.matchesColor(pieceAtCell) : false;
            boolean oppositeColor = matchesColor == true ? false : true;

            if (pieceAtCell == null) {
                result = true;
            } else if (pieceAtCell != null && matchesColor) {
                result = false;
            } else if (pieceAtCell != null && oppositeColor) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = false;
        }

        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "K";
    }
}
