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
import model.chess_set.Board.Cell;
import model.game.Position;

/**
 * @version Mar 3, 2019
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board.Cell[][],
	 * model.game.Position)
	 */
	@Override
	public boolean isMoveLegal(Cell[][] cell, Position pos) {
		boolean result = true;

		if (this.posRef.getRank() == pos.getRank()
				|| this.posRef.getFile() == pos.getFile()) {
			result = false;
		}

		if (Math.abs(this.posRef.getRank() - pos.getRank()) != Math
				.abs(this.posRef.getFile() - pos.getFile())) {
			result = false;
		}

		int rowOffset, colOffset;
		boolean movingRight = false;
		
		if (this.posRef.getFile() < pos.getFile()) {
			// 1 to the right
			colOffset = 1;
			movingRight = true;
		} else {
			// 1 to the left
			colOffset = -1;
		}

		if (this.posRef.getRank() < pos.getRank()) {
			// Moving 1 up
			rowOffset = 1;
		} else {
			// Moving 1 down
			rowOffset = -1;
		}
		
		if (movingRight) {
			for (int x = this.posRef.getFile() + colOffset, 
					y = this.posRef.getRank() + rowOffset;
					x < pos.getFile(); x += colOffset) {
				if (cell[x][y].getPiece() != null) {
					result = false;
					break;
				}
				
				y += rowOffset;
			}
		} else {
			for (int x =  this.posRef.getFile() + colOffset, 
					y = this.posRef.getRank() + rowOffset;
					x > pos.getFile(); x += colOffset) {
				if (cell[x][y].getPiece() != null) {
					result = false;
					break;
				}
				
				y += rowOffset;
			}
		}

		/* ORIGINAL CODE
		for (int x = this.posRef.getFile() + colOffset, 
					y = this.posRef.getRank() + rowOffset; 
				x != pos.getFile(); 
				x += colOffset) {			
			if (cell[x][y].getPiece() != null) {
				result = false;
				break;
			}

			y += rowOffset;
		}
		*/

		return result;
	}

	@Override
	public String toString() {
		return super.toString() + "B";
	}

}
