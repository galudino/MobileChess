/**
 * Bishop.java
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
public final class Bishop extends Piece {

    /**
     * Parameterized constructor
     *
     * @param pieceType the PieceType to assign
     * @param color     the Color of a Player's PieceSet
     */
    public Bishop(PieceType pieceType, PieceType.Color color) {
        super(color);

        this.pieceType = pieceType.equals(PieceType.BISHOP_R)
                || pieceType.equals(PieceType.BISHOP_L) ? pieceType : null;

        if (this.pieceType == null) {
            System.err.println("ERROR: Set this piece to either "
                    + "PieceType.BISHOP_R or PieceType.BISHOP_L!");
            identifier += " (invalid)";
        } else {
            identifier += "Bishop";

            identifier += pieceType.equals(PieceType.BISHOP_R) ? " (right)"
                    : " (left)";
        }
    }

    /* (non-Javadoc)
     * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board,
     * model.game.Position)
     */
    @Override
    public boolean isMoveLegal(Board board, Position pos) {
        boolean result = true;

        int thisPosRefFile = this.posRef.getFile();
        int thisPosRefRank = this.posRef.getRank();

        int posRefFile = posRef.getFile();
        int posRefRank = posRef.getRank();

        int deltaFile = Math.abs(thisPosRefFile - posRefFile);
        int deltaRank = Math.abs(thisPosRefRank - posRefRank);

        result =
                thisPosRefRank == posRefRank
                        || thisPosRefFile == posRefFile ?
                        false : result;

        result = deltaRank != deltaFile ? false : result;

        int rowOffset;
        int colOffset;

        colOffset = thisPosRefFile < posRefFile ? 1 : -1;
        rowOffset = thisPosRefRank < posRefRank ? 1 : -1;

        for (int x = thisPosRefFile + colOffset, y = thisPosRefRank + rowOffset;
             x != posRefFile;
             x += colOffset) {
            Piece pieceAtCell =
                    board.getCell(board.getPosition(x, y)).getPiece();

            result = pieceAtCell != null ? false : result;

            y += rowOffset;
        }

        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "B";
    }

}
