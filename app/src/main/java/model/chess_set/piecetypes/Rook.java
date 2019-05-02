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
import model.chess_set.Board.Cell;
import model.game.Position;

/**
 * @version Mar 3, 2019
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board.Cell[][],
	 * model.game.Position)
	 */
	@Override
	public boolean isMoveLegal(Cell[][] cell, Position posRef) {
		boolean result = true;

		boolean movingRight = false;
		boolean movingUp = false;

		int x = 0;
		int y = 0;

		int thisPosRefRank = this.posRef.getRank();
		int thisPosRefFile = this.posRef.getFile();
		int posRefRank = posRef.getRank();
		int posRefFile = posRef.getFile();

		boolean unequalRank = thisPosRefRank != posRefRank;
		boolean unequalFile = thisPosRefFile != posRefFile;

		boolean moveRightXIncreases = false;
		boolean moveLeftXDecreases = false;
		boolean moveUpYIncreases = false;
		boolean moveDownYDecreases = false;

		boolean pieceFoundAlongPath = false;

		/**
		 * "Function" of Rook's movement has a slope m such that
		 * m == 0 (the x-axis)
		 * || m == UNDEFINED (the y-axis) 
		 * 
		 * It cannot "jump" over other pieces.
		 */
		
		if (unequalRank && unequalFile) {
			result = false;
		}

		int offset = 0;

		if (unequalFile) {
			offset = thisPosRefFile < posRefFile ? 1 : -1;
			movingRight = offset == 1 ? true : false;

			x = thisPosRefFile + offset;

			moveRightXIncreases = movingRight && x < posRefFile;
			moveLeftXDecreases = !movingRight && x > posRefFile;

			while (moveRightXIncreases || moveLeftXDecreases) {
				pieceFoundAlongPath = cell[x][thisPosRefRank]
						.getPiece() != null;

				if (pieceFoundAlongPath) {
					result = false;
					break;
				}

				x += offset;
			}
		} else if (unequalRank) {
			offset = thisPosRefRank < posRefRank ? 1 : -1;
			movingUp = offset == 1 ? true : false;

			y = thisPosRefRank + offset;

			moveUpYIncreases = movingUp && y < posRefRank;
			moveDownYDecreases = !movingUp && y > posRefRank;

			while (moveUpYIncreases || moveDownYDecreases) {
				pieceFoundAlongPath = cell[thisPosRefFile][y]
						.getPiece() != null;

				if (pieceFoundAlongPath) {
					result = false;
					break;
				}

				y += offset;
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return super.toString() + "R";
	}

}
