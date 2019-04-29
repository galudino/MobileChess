/**
 * Move.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj.
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.game;

import java.time.LocalTime;

import model.chess_set.Piece;

/**
 * Represents a move within a Chess game, for logging purposes. Instances of
 * Move are created within Board and stored in a List<Move> within Board.
 *
 * @version Apr 27, 2019
 * @author gemuelealudino
 */
public class Move {

    private Piece pieceRef;			// ref to Piece from Player's PieceSet

    private Position startPosRef;	// ref to a Cell's Position field
    private Position endPosRef;		// ref to a Cell's Position field

    private boolean alive;

    private LocalTime localTime;

    private int moveNumber;			// Board initializes
    // its moveCounter field to 1.

    /**
     * Parameterized constructor
     *
     * @param piece      the Piece participating in a Move
     * @param startPos   the starting Position of piece
     * @param endPos     the ending Position of piece
     * @param moveNumber the sequential move number within the game
     * @param alive		 status after a move is completed
     */
    public Move(Piece piece, Position startPos, Position endPos,
                int moveNumber, boolean alive) {
        this.pieceRef = piece;
        this.startPosRef = startPos;
        this.endPosRef = endPos;

        this.moveNumber = moveNumber;
        localTime = LocalTime.now();
    }

    /**
     * Accessor method to retrieve a Piece associated with a Move
     *
     * @return the Piece corresponding to a Move
     */
    public Piece getLastPiece() {
        return pieceRef;
    }

    /**
     * Accessor method to retrieve the starting Position of a Move
     *
     * @return the Position object associated with a Move
     */
    public Position getStartPosition() {
        return startPosRef;
    }

    /**
     * Accessor method to retrieve the ending Position of a Move
     *
     * @return the Position object associated with a Move
     */
    public Position getEndPosition() {
        return endPosRef;
    }

    @Override
    public String toString() {
        return localTime.toString() + "\t" + moveNumber + "\t" + pieceRef
                + "\t" + startPosRef + "\t" + endPosRef + "\t" + alive;
    }
}

