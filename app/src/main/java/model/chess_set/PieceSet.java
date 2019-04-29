/**
 * PieceSet.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj.
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.chess_set;

import java.util.Iterator;

import model.PieceType;
import model.chess_set.piecetypes.*;
import model.game.Position;

/**
 * Represents a complete 'set' of Pieces for the chess Board. Two instances of
 * PieceSet are owned by Board and referred to by two Players.
 *
 * @version Apr 27, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class PieceSet implements Iterable<Piece> {

    private final int PIECE_COUNT = 16;

    private PieceType.Color color;
    private Piece[] pieceArray; // all instances of Piece reside here

    /**
     * Parameterized constructor
     *
     * @param color the Color of a Player's PieceSet
     */
    PieceSet(PieceType.Color color) {
        this.color = color;

        pieceArray = new Piece[PIECE_COUNT];

        Piece king = new King(color);
        Piece queen = new Queen(color);
        Piece bishop_l = new Bishop(PieceType.BISHOP_L, color);
        Piece bishop_r = new Bishop(PieceType.BISHOP_R, color);
        Piece knight_l = new Knight(PieceType.KNIGHT_L, color);
        Piece knight_r = new Knight(PieceType.KNIGHT_R, color);
        Piece rook_l = new Rook(PieceType.ROOK_L, color);
        Piece rook_r = new Rook(PieceType.ROOK_R, color);
        Piece pawn_0 = new Pawn(PieceType.PAWN_0, color);
        Piece pawn_1 = new Pawn(PieceType.PAWN_1, color);
        Piece pawn_2 = new Pawn(PieceType.PAWN_2, color);
        Piece pawn_3 = new Pawn(PieceType.PAWN_3, color);
        Piece pawn_4 = new Pawn(PieceType.PAWN_4, color);
        Piece pawn_5 = new Pawn(PieceType.PAWN_5, color);
        Piece pawn_6 = new Pawn(PieceType.PAWN_6, color);
        Piece pawn_7 = new Pawn(PieceType.PAWN_7, color);

        pieceArray[PieceType.KING.ordinal()] = king;
        pieceArray[PieceType.QUEEN.ordinal()] = queen;
        pieceArray[PieceType.BISHOP_L.ordinal()] = bishop_l;
        pieceArray[PieceType.BISHOP_R.ordinal()] = bishop_r;
        pieceArray[PieceType.KNIGHT_L.ordinal()] = knight_l;
        pieceArray[PieceType.KNIGHT_R.ordinal()] = knight_r;
        pieceArray[PieceType.ROOK_L.ordinal()] = rook_l;
        pieceArray[PieceType.ROOK_R.ordinal()] = rook_r;
        pieceArray[PieceType.PAWN_0.ordinal()] = pawn_0;
        pieceArray[PieceType.PAWN_1.ordinal()] = pawn_1;
        pieceArray[PieceType.PAWN_2.ordinal()] = pawn_2;
        pieceArray[PieceType.PAWN_3.ordinal()] = pawn_3;
        pieceArray[PieceType.PAWN_4.ordinal()] = pawn_4;
        pieceArray[PieceType.PAWN_5.ordinal()] = pawn_5;
        pieceArray[PieceType.PAWN_6.ordinal()] = pawn_6;
        pieceArray[PieceType.PAWN_7.ordinal()] = pawn_7;
    }

    /**
     * Accessor to retrieve the Color of a Player's PieceSet
     *
     * @return the Color of a Player's PieceSet
     */
    public PieceType.Color getPieceSetColor() {
        return color;
    }

    /**
     * Accessor to retrieve a Piece using the file and rank of a Position
     *
     * @param pos The Position of the desired Piece
     *
     * @return if found, the desired Piece, otherwise null
     */
    public Piece getPieceByPosition(Position pos) {
        for (Piece piece : this) {
            if (piece.posRef.equals(pos)) {
                return piece;
            }
        }

        return null;
    }

    /**
     * Accessor to retrieve a Piece by PieceType
     *
     * @param pieceType the PieceType of a desired Piece
     *
     * @return A Piece within the PieceSet
     */
    Piece getPieceByType(PieceType pieceType) {
        return pieceArray[pieceType.ordinal()];
    }

    /**
     * Promotes a Pawn to a Queen, Bishop, Knight, or Rook. Precondition: @see
     * Board.java, line 276 promoType must be QUEEN, BISHOP_R/L, KNIGHT_R/L, or
     * ROOK_R/L.
     *
     * @param pawnPiece the Pawn to promote
     * @param promoType Queen, Bishop, Knight, or Rook
     * @param color     White, or Black
     *
     * @return a promoted Pawn -- a new Queen, Bishop, Knight, or Rook
     */
    Piece promotePawn(Pawn pawnPiece, PieceType promoType,
                      PieceType.Color color) {
        Piece promo = null;

        /**
         * If the pawnPiece to be promoted is queen-side (L),
         * it will be promoted to a piece from queen-side (L) --
         * and the same goes for a pawnPiece from king-side (R).
         */

        PieceType pt = null;

        switch (promoType) {
            case QUEEN:
                promo = new Queen(color);
                break;
            case BISHOP_R:
                switch (pawnPiece.pieceType) {
                    case PAWN_0:
                    case PAWN_1:
                    case PAWN_2:
                    case PAWN_3:
                        pt = PieceType.BISHOP_L;
                        break;
                    case PAWN_4:
                    case PAWN_5:
                    case PAWN_6:
                    case PAWN_7:
                        pt = PieceType.BISHOP_R;
                        break;
                    default:
                        break;
                }

                promo = new Bishop(pt, color);
                break;
            case KNIGHT_R:
                switch (pawnPiece.pieceType) {
                    case PAWN_0:
                    case PAWN_1:
                    case PAWN_2:
                    case PAWN_3:
                        pt = PieceType.KNIGHT_L;
                        break;
                    case PAWN_4:
                    case PAWN_5:
                    case PAWN_6:
                    case PAWN_7:
                        pt = PieceType.KNIGHT_R;
                        break;
                    default:
                        break;
                }

                promo = new Knight(pt, color);
                break;
            case ROOK_R:
                switch (pawnPiece.pieceType) {
                    case PAWN_0:
                    case PAWN_1:
                    case PAWN_2:
                    case PAWN_3:
                        pt = PieceType.ROOK_L;
                        break;
                    case PAWN_4:
                    case PAWN_5:
                    case PAWN_6:
                    case PAWN_7:
                        pt = PieceType.ROOK_R;
                        break;
                    default:
                        break;
                }

                promo = new Rook(pt, color);
                break;
            default:
                System.err.println(
                        "Can only promote to QUEEN, BISHOP, " + "KNIGHT, or ROOK.");
                break;
        }

        if (promo != null) {
            promo.makeAlive();				// bring the new Piece to life
            promo.posRef = pawnPiece.posRef;// assign the Pawn's Position

            // assign the promo Piece to the pieceArray within the PieceSet
            pieceArray[pawnPiece.pieceType.ordinal()] = promo;
        }

        return promo == null ? pawnPiece : promo;
    }

    /**
     * Returns a string representation of the PieceSet (used for debugging)
     */
    @Override
    public String toString() {
        String str = "";
        str += "PIECESET LOG (" + color + ") --------------------------------\n";
        str += "Symbol\tIdentifier\t\tPosition\tAlive\n";
        str += "-----------------------------------------------------\n";

        Iterator<String> iter = pieceSetLogIterator();

        while (iter.hasNext()) {
            str += iter.next();
        }

        return str;
    }

    /**
     * Returns an iterator that assists in producing the string representation
     * of the PieceSet
     *
     * @return an iterator representing the fields of the pieceSet
     */
    private Iterator<String> pieceSetLogIterator() {
        return new Iterator<String>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < PIECE_COUNT;
            }

            @Override
            public String next() {
                if (i < PIECE_COUNT) {
                    return String.format("%s\t" + "%s\t" + "%s\t\t" + "%s\n",
                            pieceArray[i].toString(), pieceArray[i].identifier,
                            pieceArray[i].posRef, pieceArray[i++].alive);
                } else {
                    return null;
                }
            }
        };
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Piece> iterator() {
        return new Iterator<Piece>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < PIECE_COUNT;
            }

            @Override
            public Piece next() {
                return i < PIECE_COUNT ? pieceArray[i++] : null;
            }

        };
    }
}
