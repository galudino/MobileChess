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
	public boolean isMoveLegal(Cell[][] cell, Position posRef) {
		boolean result = true;
		
		boolean movingRight = false;
		boolean movingUp = false;
		
		int colOffset = 0;
		int rowOffset = 0;
		
		int x = 0;
		int y = 0;
		
		int thisPosRefRank = this.posRef.getRank();
		int thisPosRefFile = this.posRef.getFile();
		int posRefRank = posRef.getRank();
		int posRefFile = posRef.getFile();
		
		int deltaRank = Math.abs(thisPosRefRank - posRefRank);
		int deltaFile = Math.abs(thisPosRefFile - posRefFile);
		
		boolean unequalRank = thisPosRefRank != posRefRank;
		boolean unequalFile = thisPosRefFile != posRefFile;
		
		boolean moveRightXIncreases = false;
		boolean moveLeftXDecreases = false;
		boolean moveUpYIncreases = false;
		boolean moveDownYDecreases = false;
		
		boolean pieceFoundAlongPath = false;
		
		/**
		 * @see Rook.java, Bishop.java
		 * The QUEEN's movement combines both the capabilities
		 * of ROOK and BISHOP.
		 * 
		 * It can move along the x-axis, y-axis, or in any diagonal
		 * such that the "function" of a move has a slope m == 1 
		 * (moving diagonally right) or m == -1 (moving diagonally left)
		 */
		
		if (deltaRank == deltaFile) {
			/**
			 * @see Bishop.java
			 * "Function" of Bishop's movement has a slope m such that
			 * m == 1 (moving diagonally right) || m == -1 (moving diagonally left)
			 * 
			 * It cannot jump over other pieces.
			 */
			
			colOffset = thisPosRefFile < posRefFile ? 1 : -1;
			rowOffset = thisPosRefRank < posRefRank ? -1 : -1;
			
			movingRight = colOffset == 1 ? true : false;
			
			x = thisPosRefFile + colOffset;
			y = thisPosRefRank + rowOffset;
			
			moveRightXIncreases = movingRight && x < posRefFile;
			moveLeftXDecreases = !movingRight && x > posRefFile;
			
			while (moveRightXIncreases || moveLeftXDecreases) {
				pieceFoundAlongPath = cell[x][y].getPiece() != null;
				
				if (pieceFoundAlongPath) {
					result = false;
					break;
				}
			
				x += colOffset;
				y += rowOffset;
			}
			
		} else {
			/**
			 * @see Rook.java
			 * 
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
					pieceFoundAlongPath = 
							cell[x][thisPosRefRank].getPiece() != null;
					
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
					pieceFoundAlongPath = 
							cell[thisPosRefFile][y].getPiece() != null;
					
					if (pieceFoundAlongPath) {
						result = false;
						break;
					}
					
					y += offset;
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
