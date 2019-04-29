/**
 * Pawn.java
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
import model.game.Move;
import model.game.Position;

/**
 * @version Apr 27, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Pawn extends Piece {

    private boolean firstMove;
    private boolean checkEnpassant;

    /**
     * Parameterized constructor
     *
     * @param pieceType the PieceType to assign
     * @param color     the Color of a Player's PieceSet
     */
    public Pawn(PieceType pieceType, PieceType.Color color) {
        super(color);

        firstMove = true;
        checkEnpassant = false;

        switch (pieceType) {
            case PAWN_0:
            case PAWN_1:
            case PAWN_2:
            case PAWN_3:
            case PAWN_4:
            case PAWN_5:
            case PAWN_6:
            case PAWN_7:
                this.pieceType = pieceType;
                identifier += "Pawn";
                break;
            default:
                this.pieceType = null;
                identifier += " (invalid)";

                System.err.println("ERROR: Set this piece to either "
                        + "PieceType.PAWN_n, n being [0 - 7]!");

                break;
        }

        switch (this.pieceType) {
            case PAWN_0:
                identifier += "   (1)";
                break;
            case PAWN_1:
                identifier += "   (2)";
                break;
            case PAWN_2:
                identifier += "   (3)";
                break;
            case PAWN_3:
                identifier += "   (4)";
                break;
            case PAWN_4:
                identifier += "   (5)";
                break;
            case PAWN_5:
                identifier += "   (6)";
                break;
            case PAWN_6:
                identifier += "   (7)";
                break;
            case PAWN_7:
                identifier += "   (8)";
                break;
            default:
                break;
        }
    }

    /*
     * (non-Javadoc)
     *
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

        boolean deltaFileOne = deltaFile == 1;
        boolean deltaRankOne = deltaRank == 1;
        boolean deltaRankTwo = deltaRank == 2;

        boolean deltaRankTwoOrOne = deltaRankTwo || deltaRankOne;
        boolean bothDeltasOne = deltaRankOne && deltaFileOne;

        boolean equalFile = thisPosRefFile == posRefFile;

        Piece pieceAtCell = board.getCell(posRef).getPiece();

        boolean matchesColor = pieceAtCell != null ? this.matchesColor(pieceAtCell) : false;
        boolean oppositeColor = matchesColor == true ? false : true;

        boolean whiteMoveForward = thisPosRefRank < posRefRank;
        boolean blackMoveForward = posRefRank < thisPosRefRank;

        if (whiteMoveForward || blackMoveForward) {
            boolean firstCondition =
                    deltaRankTwoOrOne
                            && equalFile
                            && pieceAtCell == null;

            boolean secondCondition =
                    bothDeltasOne
                            && pieceAtCell != null
                            && oppositeColor;

            boolean firstOrSecondCondition = firstCondition || secondCondition;

            if (firstMove && firstOrSecondCondition) {
                result = true;
                firstMove = false;
            } else {
                boolean enpassantCondition =
                        deltaRankOne
                                && deltaFileOne
                                && pieceAtCell == null;

                if (firstOrSecondCondition) {
                    result = true;
                } else if (enpassantCondition) {
                    checkEnpassant = true;

                    if (checkEnpassant) {
                        Move lastMove =
                                board.getCell
                                        (board.getPosition(
                                                posRefFile, posRefRank)).getLastMove();

                        Position lastMoveStartPosition = lastMove
                                .getStartPosition();
                        Position lastMoveEndPosition = lastMove
                                .getEndPosition();

                        Piece pieceFromLastMove = lastMove.getLastPiece();

                        int lastMoveStartRank = lastMoveStartPosition.getRank();
                        int lastMoveEndRank = lastMoveEndPosition.getRank();

                        int deltaRankLastMoveStartEnd =
                                Math.abs(lastMoveStartRank - lastMoveEndRank);

                        if (pieceFromLastMove.isPawn()) {
                            boolean whiteConditionsMet =
                                    (this.isWhite()
                                            && thisPosRefRank == 4
                                            && lastMoveEndRank == 4
                                            && deltaRankLastMoveStartEnd == 2);

                            boolean blackConditionsMet =
                                    (this.isBlack()
                                            && thisPosRefRank == 3
                                            && lastMoveEndRank == 3
                                            && deltaRankLastMoveStartEnd == 2);

                            if (whiteConditionsMet || blackConditionsMet) {
                                pieceFromLastMove.makeDead();

                                board.
                                        setPieceNullAtPosition(lastMoveEndPosition);

                                result = true;
                            }
                        }

                    }
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "P";
    }
}
