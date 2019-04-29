/**
 * Rook.java
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
public final class Rook extends Piece {

    private boolean canCastle;

    /**
     * Parameterized constructor
     *
     * @param pieceType the PieceType to assign
     * @param color     the Color of a Player's PieceSet
     */
    public Rook(PieceType pieceType, PieceType.Color color) {
        super(color);
        canCastle = true;

        this.pieceType = pieceType.equals(PieceType.ROOK_R)
                || pieceType.equals(PieceType.ROOK_L) ? pieceType : null;

        if (this.pieceType == null) {
            System.err.println("ERROR: Set this piece to either "
                    + "PieceType.ROOK_R or PieceType.ROOK_L!");
            identifier += " (invalid)";
        } else {
            identifier += "Rook";

            identifier += pieceType.equals(PieceType.ROOK_R) ? "   (right)"
                    : "   (left)";
        }
    }

    public boolean canCastle() {
        return canCastle;
    }

    /* (non-Javadoc)
     * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board,
     * model.game.Position)
     */
    @Override
    public boolean isMoveLegal(Board board, Position posRef) {
        boolean result = true;

        int thisPosRefFile = this.posRef.getFile();
        int thisPosRefRank = this.posRef.getRank();

        int posRefFile = posRef.getFile();
        int posRefRank = posRef.getRank();

        // This is to check if it is moving on one path, aka not diagonal
        result =
                (posRefRank != thisPosRefRank)
                        && (posRefFile != thisPosRefFile)
                        ? false : result;

        // Utilized to check if next piece will be null
        int offset;

        if (posRefFile != thisPosRefFile) {
            offset = thisPosRefFile < posRefFile ? 1 : -1;

            for (int x = thisPosRefFile + offset;
                 x != posRefFile; x += offset) {
                Piece pieceAtCell =
                        board.getCell(board.getPosition(x, posRefRank)).getPiece();

                if (pieceAtCell != null) {
                    return false;
                }
            }
        }

        if (posRefRank != thisPosRefRank) {
            offset = thisPosRefRank < posRefRank ? 1 : -1;

            for (int x = thisPosRefRank + offset;
                 x != posRefRank; x += offset) {
                Piece pieceAtCell =
                        board.getCell(board.getPosition(posRefFile, x)).getPiece();

                if (pieceAtCell != null) {
                    return false;
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "R";
    }

}
