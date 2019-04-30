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
	@Override
	public boolean isMoveLegal(Cell[][] cell, Position pos) {
		boolean result = true;

		if (Math.abs(this.posRef.getRank() - pos.getRank()) == Math
				.abs(this.posRef.getFile() - pos.getFile())) {
			int rowOffset, colOffset;

			if (this.posRef.getFile() < pos.getFile()) {
				colOffset = 1;
			} else {
				colOffset = -1;
			}

			if (this.posRef.getRank() < pos.getRank()) {
				rowOffset = 1;
			} else {
				rowOffset = -1;
			}

			for (int x = this.posRef.getFile() + colOffset, y = this.posRef.getRank()
					+ rowOffset; x != pos.getFile(); x += colOffset) {
				if (cell[x][y].getPiece() != null) {
					result = false;
				}

				y += rowOffset;
			}
		} else {
			// This is to check if it is moving on one path aka not diagonal
			if (pos.getRank() != this.posRef.getRank()
					&& pos.getFile() != this.posRef.getFile()) {
				result = false;
			}

			int offset;

			if (pos.getFile() != this.posRef.getFile()) {
				if (this.posRef.getFile() < pos.getFile()) {
					offset = 1;
				} else {
					offset = -1;
				}

				for (int x = this.posRef.getFile() + offset; x != pos
						.getFile(); x += offset) {
					if (cell[x][this.posRef.getRank()].getPiece() != null) {
						return false;
					}
				}
			}

			if (pos.getRank() != this.posRef.getRank()) {
				if (this.posRef.getRank() < pos.getRank()) {
					offset = 1;
				} else {
					offset = -1;
				}

				for (int x = this.posRef.getRank() + offset; x != pos
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
	public String toString() {
		return super.toString() + "Q";
	}
}
