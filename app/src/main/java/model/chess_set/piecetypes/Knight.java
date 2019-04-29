/**
 * Knight.java
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
import model.chess_set.Piece;
import model.chess_set.Board;
import model.game.Position;

/**
 * @version Apr 27, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Knight extends Piece {

    /**
     * Parameterized constructor
     *
     * @param pieceType the PieceType to assign
     * @param color     the Color of a Player's PieceSet
     */
    public Knight(PieceType pieceType, PieceType.Color color) {
        super(color);

        this.pieceType = pieceType.equals(PieceType.KNIGHT_R)
                || pieceType.equals(PieceType.KNIGHT_L) ? pieceType : null;

        if (this.pieceType == null) {
            System.err.println("ERROR: Set this piece to either "
                    + "PieceType.KNIGHT_R or PieceType.KNIGHT_L!");
            identifier += " (invalid)";
        } else {
            identifier += "Knight";

            identifier += pieceType.equals(PieceType.KNIGHT_R) ? " (right)"
                    : " (left)";
        }
    }

    /* (non-Javadoc)
     * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board,
     * model.game.Position)
     */
    @Override
    public boolean isMoveLegal(Board board, Position posRef) {
        boolean result = false;

        int thisPosRefFile = this.posRef.getFile();
        int thisPosRefRank = this.posRef.getRank();

        int posRefFile = posRef.getFile();
        int posRefRank = posRef.getRank();

        int deltaFile = Math.abs(posRefFile - thisPosRefFile);
        int deltaRank = Math.abs(posRefRank - thisPosRefRank);

        result = deltaFile == 2 && deltaRank == 1 ? true : result;
        result = deltaFile == 1 && deltaRank == 2 ? true : result;

        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "N";
    }
}
