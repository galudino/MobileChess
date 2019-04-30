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
 * Move are created within Board and stored in a List within Board.
 * 
 * @version Mar 24, 2019
 * @author gemuelealudino
 */
public class Move {
	private Piece piece;

	private Position startPos;
	private Position endPos;

	private LocalTime localTime;

	private int moveNumber;
	
	/**
	 * Parameterized constructor
	 * 
	 * @param piece      the Piece participating in a Move
	 * @param startPos   the starting Position of piece
	 * @param endPos     the ending Position of piece
	 * @param moveNumber the sequential move number within the game
	 */
	public Move(Piece piece, Position startPos, Position endPos, int moveNumber) {
		this.piece = piece;
		this.startPos = startPos;
		this.endPos = endPos;

		this.moveNumber = moveNumber;
		localTime = LocalTime.now();
	}

	/**
	 * Accessor method to retrieve a Piece associated with a Move
	 * 
	 * @return the Piece corresponding to a Move
	 */
	public Piece getLastPiece() {
		return piece;
	}

	/**
	 * Accessor method to retrieve the starting Position of a Move
	 * 
	 * @return the Position object associated with a Move
	 */
	public Position getStartPosition() {
		return startPos;
	}

	/**
	 * Accessor method to retrieve the ending Position of a Move
	 * 
	 * @return the Position object associated with a Move
	 */
	public Position getEndPosition() {
		return endPos;
	}
	
	/**
	 * Accessor method to retrieve the LocalTime of a Move
	 * 
	 * @return the LocalTime object associated with a Move
	 */
	public LocalTime getLocalTime() {
		return localTime;
	}
	
	@Override
	public String toString() {
		return localTime.toString() + "\t" + moveNumber + "\t" + piece
				+ "\t" + startPos + "\t" + endPos;
	}
}

