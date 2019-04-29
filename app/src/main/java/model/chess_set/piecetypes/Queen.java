/**
 * Queen.java
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
import model.chess_set.Board.Cell;
import model.game.Position;

/**
 * @version Mar 3, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Queen extends Piece {

    /**
     * Parameterized constructor
     *
     * @param color the Color of a Player's PieceSet
     */
    public Queen(PieceType.Color color) {
        super(color);
        pieceType = PieceType.QUEEN;

        identifier += "Queen     ";
    }

    /*
     * (non-Javadoc)
     *
     * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board.Cell[][],
     * model.game.Position)
     */
    @Deprecated
    public boolean isMoveLegal(Cell[][] cell, Position posRef) {
        boolean result = true;

        if (Math.abs(this.posRef.getRank() - posRef.getRank()) == Math
                .abs(this.posRef.getFile() - posRef.getFile())) {
            int rowOffset, colOffset;

            if (this.posRef.getFile() < posRef.getFile()) {
                colOffset = 1;
            } else {
                colOffset = -1;
            }

            if (this.posRef.getRank() < posRef.getRank()) {
                rowOffset = 1;
            } else {
                rowOffset = -1;
            }

            for (int x = this.posRef.getFile() + colOffset, y = this.posRef.getRank()
                    + rowOffset; x != posRef.getFile(); x += colOffset) {
                if (cell[x][y].getPiece() != null) {
                    result = false;
                }

                y += rowOffset;
            }
        } else {
            // This is to check if it is moving on one path aka not diagonal
            if (posRef.getRank() != this.posRef.getRank()
                    && posRef.getFile() != this.posRef.getFile()) {
                result = false;
            }

            int offset;

            if (posRef.getFile() != this.posRef.getFile()) {
                if (this.posRef.getFile() < posRef.getFile()) {
                    offset = 1;
                } else {
                    offset = -1;
                }

                for (int x = this.posRef.getFile() + offset; x != posRef
                        .getFile(); x += offset) {
                    if (cell[x][this.posRef.getRank()].getPiece() != null) {
                        return false;
                    }
                }
            }

            if (posRef.getRank() != this.posRef.getRank()) {
                if (this.posRef.getRank() < posRef.getRank()) {
                    offset = 1;
                } else {
                    offset = -1;
                }

                for (int x = this.posRef.getRank() + offset; x != posRef
                        .getRank(); x += offset) {
                    if (cell[this.posRef.getFile()][x].getPiece() != null) {
                        return false;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean isMoveLegal(Board board, Position posRef) {
        boolean result = true;

        int thisPosRefFile = this.posRef.getFile();
        int thisPosRefRank = this.posRef.getRank();

        int posRefFile = posRef.getFile();
        int posRefRank = posRef.getRank();

        int deltaFile = Math.abs(thisPosRefFile - posRefFile);
        int deltaRank = Math.abs(thisPosRefRank - posRefRank);

        if (deltaRank == deltaFile) {
            int rowOffset;
            int colOffset;

            colOffset = thisPosRefFile < posRefFile ? 1 : -1;
            rowOffset = thisPosRefRank < posRefRank ? 1 : -1;

            for (int x = thisPosRefFile + colOffset, y = thisPosRefRank + rowOffset;
                 x != posRefFile;
                 x += colOffset) {

                Piece pieceAtCell = board.getCell(board.getPosition(x, y)).getPiece();

                result = pieceAtCell != null ? false : result;

                y += colOffset;
            }
        } else {
            // This is to check if it is moving on one path aka not diagonal
            result = (posRefRank != thisPosRefRank
                    && posRefFile != thisPosRefFile)
                    ? false : result;

            int offset;

            if (posRefFile != thisPosRefFile) {
                offset = thisPosRefFile < posRefFile ? 1 : -1;

                for (int x = thisPosRefFile + offset; x != posRefFile; x += offset) {
                    Piece pieceAtCell = board.getCell(board.getPosition(x, thisPosRefRank)).getPiece();

                    if (pieceAtCell != null) {
                        return false;
                    }
                }
            }

            if (posRefRank != thisPosRefRank) {
                offset = thisPosRefRank < posRefRank ? 1 : -1;

                for (int x = thisPosRefRank + offset; x != posRefRank; x += offset) {
                    Piece pieceAtCell =
                            board.getCell(board.getPosition(thisPosRefFile, x)).getPiece();

                    if (pieceAtCell != null) {
                        return false;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "Q";
    }
}
