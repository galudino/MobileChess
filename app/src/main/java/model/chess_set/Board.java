/**
 * Board.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj.
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.chess_set;

import java.util.ArrayList;
import java.util.List;

import model.PieceType;
import model.chess_set.piecetypes.King;
import model.chess_set.piecetypes.Pawn;
import model.game.Move;
import model.game.Player;
import model.game.Position;

/**
 * Represents a Chess board for a Chess game. Instances of Board are owned by
 * Game.
 *
 * @version Apr 27, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Board {

    /**
     * Represents a 'square' within a Chess board
     *
     * @version Mar 3, 2019
     * @author gemuelealudino
     */
    public final class Cell {
        private Position loc; // 'true' instance of Position
        private Piece pieceRef; // ref to a Piece in a Player's PieceSet

        /**
         * Parameterized constructor
         *
         * @param file x axis coordinate (0-7 only)
         * @param rank y axis coordinate (0-7 only)
         */
        Cell(int file, int rank) {
            loc = new Position(file, rank);
            pieceRef = null;
        }

        /**
         * Accessor method to retrieve a Piece within a Cell
         *
         * @return the Piece occupying a Cell within the Board
         */
        public Piece getPiece() {
            return pieceRef;
        }

        /**
         * Accessor method to retrieve the last Move executed during gameplay
         *
         * @return the most recent Move that was logged by Board
         */
        public Move getLastMove() {
            return moveList.get(moveList.size() - 1);
        }

        /**
         * Returns a string representation of the Piece occupying the cell, if
         * there is a Piece present
         */
        @Override
        public String toString() {
            return pieceRef == null ? "--" : pieceRef.toString();
        }
    }

    private static final int MAX_LENGTH_WIDTH = 8;

    private Cell[][] cell;

    private List<Move> moveList;
    private int moveCounter;

    private PieceSet whiteSet;
    private PieceSet blackSet;

    private Position[] kingMoves;
    private boolean kingChecked;
    private boolean kingSafe;

    /**
     * Default constructor
     */
    public Board() {
        cell = new Cell[MAX_LENGTH_WIDTH][MAX_LENGTH_WIDTH];

        for (int file = 0; file < MAX_LENGTH_WIDTH; file++) {
            for (int rank = 0; rank < MAX_LENGTH_WIDTH; rank++) {
                cell[file][rank] = new Cell(file, rank);
            }
        }

        whiteSet = new PieceSet(PieceType.Color.WHITE);
        blackSet = new PieceSet(PieceType.Color.BLACK);

        moveList = new ArrayList<Move>();
        moveCounter = 0;

        assignWhitePieces();
        assignBlackPieces();

        kingMoves = new Position[MAX_LENGTH_WIDTH];
        kingSafe = true;
    }

    /**
     * Returns the position object associated with the cell corresponding to the
     * file and rank provided
     *
     * @param file the file desired
     * @param rank the rank desired
     * @return a Position object from a Cell[][] that corresponds to a place on
     *         the chess Board
     */
    public Position getPosition(int file, int rank) {
        if ((file < 0 || rank < 0) || (file > 7 || rank > 7)) {
            return null;
        } else {
            return cell[file][rank].loc;
        }
    }

    /**
     * Retrieves the respective PieceSet for a given Player
     *
     * @param player a Player, that is either white or black
     * @return whiteSet for the white player, blackSet for the black player
     */
    public PieceSet getPieceSet(Player player) {
        PieceSet result = null;

        if (player.isWhite()) {
            result = whiteSet;
        } else if (player.isBlack()) {
            result = blackSet;
        }

        return result;
    }

    /**
     * Accessor to retrieve a particular Cell within the Board
     *
     * @param pos the Position of the desired Cell
     *
     * @return the Cell with the given file and rank of a Position
     */
    public Cell getCell(Position pos) {
        return cell[pos.getFile()][pos.getRank()];
    }

    /**
     * Board fulfills the request to move a Piece, provided it is legal to do so
     *
     * @param piece       a Piece, that should be from pieceSetRef
     * @param pieceSetRef a PieceSet, that refers to whiteSet or blackSet
     * @param newPosition the desired Position to move to
     * @param promoType   the desired PieceType to promote to
     *
     * @return true if successful, false otherwise
     */
    public boolean movePiece(Piece piece, PieceSet pieceSetRef,
                             Position newPosition, PieceType promoType) {
        boolean result = false;

        boolean verifiedWhite = piece.isWhite()
                && pieceSetRef.getPieceSetColor().equals(PieceType.Color.WHITE)
                && pieceSetRef.equals(whiteSet)
                && piece.equals(
                pieceSetRef.getPieceByType(piece.getPieceType()))
                && piece.equals(whiteSet.getPieceByType(piece.getPieceType()));

        boolean verifiedBlack = piece.isBlack()
                && pieceSetRef.getPieceSetColor().equals(PieceType.Color.BLACK)
                && pieceSetRef.equals(blackSet)
                && piece.equals(
                pieceSetRef.getPieceByType(piece.getPieceType()))
                && piece.equals(blackSet.getPieceByType(piece.getPieceType()));

        //@formatter:off
        /**
         * Integrity check:
         *	- If white piece:
         *		- is piece.isWhite() true?
         *		- is pieceSetRef a PieceType.Color.WHITE?
         *		- does pieceSetRef refer to whiteSet?
         *		- is piece the same Piece within pieceSetRef?
         *		- is piece the same Piece within whiteSet?
         *	- If black piece:
         *		- is piece.isBlack() true?
         *		- is pieceSetRef a PieceType.Color.BLACK?
         *		- does pieceSetRef refer to blackSet?
         *		- is piece the same Piece within pieceSetRef?
         *		- is piece the same Piece within blackSet?
         */
        //@formatter:on
        if (verifiedWhite || verifiedBlack) {
            // System.out.println("Passed integrity check");
        } else {
            System.err.println("*** ILLEGAL OPERATION DETECTED ***");
        }

        Cell oldPositionCell = getCell(piece.posRef);
        Cell newPositionCell = getCell(newPosition);

        boolean pieceMoveLegal = piece.isMoveLegal(this, newPosition);

        King king = null;

        if (piece.isBlack()) {
            king = (King) whiteSet.getPieceByType(PieceType.KING);
        } else {
            king = (King) blackSet.getPieceByType(PieceType.KING);
        }

        kingSafe = isKingSafe(king, king.posRef);

        if (kingChecked) {
            if (piece.isKing()) {
                for (int i = 0; i < kingMoves.length; i++) {
                    if (newPosition.equals(kingMoves[i])
                            && isKingSafe(king, kingMoves[i])) {
                        kingChecked = false;
                        pieceMoveLegal = true;
                        result = true;

                        break;
                    } else {
                        pieceMoveLegal = false;
                        result = false;
                    }
                }
            } else {
                pieceMoveLegal = false;
                result = false;
            }
        }

        if (pieceMoveLegal) {
            Piece pieceAtNewPosition = newPositionCell.pieceRef;
            boolean pieceFoundAtNewPosition = pieceAtNewPosition != null;

            if (pieceFoundAtNewPosition) {
                boolean allyPieceFound = piece.matchesColor(pieceAtNewPosition);

                if (allyPieceFound) {
                    // This prevents a Piece of the same color as piece
                    // from being taken.
                    result = false;
                } else {
                    PieceType.Color pieceSetColor = pieceSetRef
                            .getPieceSetColor();
                    PieceSet pieceSetOpponent = pieceSetColor.equals(
                            PieceType.Color.WHITE) ? blackSet : whiteSet;

                    Piece opponentsPiece = pieceSetOpponent
                            .getPieceByPosition(newPosition);

                    if (opponentsPiece != null) {
                        // This changes the opponentsPiece object's
                        // alive field to false, denoting that it is 'dead'.
                        opponentsPiece.alive = false;
                    }

                    // This nullifies the pieceRef field so that
                    // newPositionCell does not have a Piece to refer to
                    // (meaning it is now 'unoccupied').
                    newPositionCell.pieceRef = null;

                    // result = true means we will allow our requested
                    // piece to move to newPositionCell.
                    result = true;
                }
            } else {
                // result = true means we will allow our requested
                // piece to move to the cell with newPosition.
                result = true;
            }

            // Since the Piece moved from the old location to the new
            // location,
            // the Cell will no longer have a reference to that Piece.

            /**
             * If a successful move is made, piece will be evaluated by pieceSet
             * to determine if piece is a promotable Pawn.
             */
            if (result) {
                boolean promoteWhite = promoType != null && piece.isWhite()
                        && piece.isPawn() && newPosition.getRank() == 7;

                boolean promoteBlack = promoType != null && piece.isBlack()
                        && piece.isPawn() && newPosition.getRank() == 0;

                if (promoteWhite || promoteBlack) {
                    PieceSet pieceSet = promoteWhite ? whiteSet : blackSet;

                    PieceType.Color color = promoteWhite ? PieceType.Color.WHITE
                            : PieceType.Color.BLACK;

                    piece = pieceSet.promotePawn((Pawn) piece, promoType,
                            color);
                }

                if (kingSafe) {
                    // This statement nullifies any reference to a Piece
                    // for this Cell object. (Next line: piece will be
                    // reassigned
                    // to the newPositionCell.piece field).
                    oldPositionCell.pieceRef = null;

                    // This statement affects what Pieces print
                    // at which cells when board.toString() is called.
                    newPositionCell.pieceRef = piece;

                    // This statement affects the internal position
                    // data within a Piece object.
                    piece.posRef = newPosition;

                    // System.out.println(this);
                    // need to figure out how to prompt user to enter a
                    // valid legal move to make sure King is safe...
                } else {
                    return false;
                }
            }

            ++moveCounter;

            Move newestMove = new Move(piece, oldPositionCell.loc, piece.posRef,
                    ++moveCounter, piece.alive);

            moveList.add(newestMove);
        }

        if (canCheck(piece)) {
            if (hasValidMoves(king)) {
                kingChecked = true;
            } else {
                checkmate(king);
            }

            System.out.println("Check");
        }

        return result;
    }

    /**
     * When provided with a Position posRef that refers to a Position object
     * within a Cell from this Board, that Cell's pieceRef will be set null.
     *
     * @param posRef the Position reference that refers to a Position within a
     *               Cell
     *
     * @return true if pieceRef was set null, false otherwise
     */
    public boolean setPieceNullAtPosition(Position posRef) {
        boolean result = false;

        Cell cell = getCell(posRef);

        cell.pieceRef = cell.pieceRef.posRef == posRef ? null : cell.pieceRef;
        result = cell.pieceRef == null ? true : result;

        return result;
    }

    /**
     * Prints the log of moves as per the moveList field (ArrayList)
     */
    public void printMoveLog() {
        System.out.println(
                "MOVE LOG (ALL PIECES) -------------------------------");

        String str = "";

        str += "Time\t\tMove #\tPiece\tStart\tEnd\tAlive\n";
        str += "-----------------------------------------------------\n";

        System.out.print(str);

        for (Move mp : moveList) {
            System.out.println(mp);
        }

        System.out.println();
    }

    /**
     * This checks to see if the King on opposing side is checked
     *
     * @param piece the current piece
     *
     * @return true if opponent King is checked || false is not.
     */
    private boolean canCheck(Piece piece) {
        boolean result = false;

        Position oppositeKingPosRef = null;
        Piece oppositeKing = null;

        if (piece.isWhite()) {
            oppositeKing = blackSet.getPieceByType(PieceType.KING);
            oppositeKingPosRef = oppositeKing.posRef;
        } else if (piece.isBlack()) {
            oppositeKing = whiteSet.getPieceByType(PieceType.KING);
            oppositeKingPosRef = oppositeKing.posRef;
        }

        boolean pieceMoveLegal = piece.isMoveLegal(this, oppositeKingPosRef);

        if (pieceMoveLegal) {
            result = true;
            kingChecked = true;
        }

        return result;
    }

    /**
     * This checks to see if the King is safe.
     *
     * @param king   - king object
     * @param posRef - position object
     *
     * @return true if any piece on opponent can move to the King's position
     */
    private boolean isKingSafe(King king, Position posRef) {
        boolean result = true;

        PieceSet opponent = king.isWhite() ? blackSet : whiteSet;

        Piece BISH_L = opponent.getPieceByType(PieceType.BISHOP_L);
        Piece BISH_R = opponent.getPieceByType(PieceType.BISHOP_R);
        Piece KNIGHT_L = opponent.getPieceByType(PieceType.KNIGHT_L);
        Piece KNIGHT_R = opponent.getPieceByType(PieceType.KNIGHT_R);
        Piece ROOK_R = opponent.getPieceByType(PieceType.ROOK_R);
        Piece ROOK_L = opponent.getPieceByType(PieceType.ROOK_L);
        Piece PAWN_0 = opponent.getPieceByType(PieceType.PAWN_0);
        Piece PAWN_1 = opponent.getPieceByType(PieceType.PAWN_1);
        Piece PAWN_2 = opponent.getPieceByType(PieceType.PAWN_2);
        Piece PAWN_3 = opponent.getPieceByType(PieceType.PAWN_3);
        Piece PAWN_4 = opponent.getPieceByType(PieceType.PAWN_4);
        Piece PAWN_5 = opponent.getPieceByType(PieceType.PAWN_5);
        Piece PAWN_6 = opponent.getPieceByType(PieceType.PAWN_6);
        Piece PAWN_7 = opponent.getPieceByType(PieceType.PAWN_7);
        Piece QUEEN = opponent.getPieceByType(PieceType.QUEEN);

        //@formatter:off
        if (ROOK_R.isMoveLegal(this, posRef)
                || ROOK_L.isMoveLegal(this, posRef)
                || BISH_L.isMoveLegal(this, posRef)
                || BISH_R.isMoveLegal(this, posRef)
                || PAWN_0.isMoveLegal(this, posRef)
                || PAWN_1.isMoveLegal(this, posRef)
                || PAWN_2.isMoveLegal(this, posRef)
                || PAWN_3.isMoveLegal(this, posRef)
                || PAWN_4.isMoveLegal(this, posRef)
                || PAWN_5.isMoveLegal(this, posRef)
                || PAWN_6.isMoveLegal(this, posRef)
                || PAWN_7.isMoveLegal(this, posRef)
                || QUEEN.isMoveLegal(this, posRef)
                || KNIGHT_L.isMoveLegal(this, posRef)
                || KNIGHT_R.isMoveLegal(this, posRef)) {
            result = false;
        }
        //@formatter:on

        return result;
    }

    /**
     * Checks to see the valid moves a King can make to decide if he will be
     * checked or checkmated.
     *
     * @param king - king object
     *
     * @return true if King is able to move somewhere without being in risk of
     *         check
     */
    private boolean hasValidMoves(King king) {
        // caching away possible valid moves for a king
        int posRefFile = king.posRef.getFile();
        int posRefRank = king.posRef.getRank();

        kingMoves[0] = getPosition(posRefFile + 1, posRefRank);
        kingMoves[1] = getPosition(posRefFile - 1, posRefRank);
        kingMoves[2] = getPosition(posRefFile, posRefRank + 1);
        kingMoves[3] = getPosition(posRefFile, posRefRank - 1);

        kingMoves[4] = getPosition(posRefFile + 1, posRefRank - 1);
        kingMoves[5] = getPosition(posRefFile - 1, posRefRank - 1);
        kingMoves[6] = getPosition(posRefFile + 1, posRefRank + 1);
        kingMoves[7] = getPosition(posRefFile - 1, posRefRank + 1);

        // if all proposed moves are legal and king is safe with said moves,
        // the king has valid moves
        return ((king.isMoveLegal(this, kingMoves[0])
                && isKingSafe(king, kingMoves[0])
                || (king.isMoveLegal(this, kingMoves[1])
                && isKingSafe(king, kingMoves[1])
                || (king.isMoveLegal(this, kingMoves[2])
                && isKingSafe(king, kingMoves[2]))
                || (king.isMoveLegal(this, kingMoves[3])
                && isKingSafe(king, kingMoves[3]))
                || (king.isMoveLegal(this, kingMoves[4])
                && isKingSafe(king, kingMoves[4]))
                || (king.isMoveLegal(this, kingMoves[5])
                && isKingSafe(king, kingMoves[5]))
                || (king.isMoveLegal(this, kingMoves[6])
                && isKingSafe(king, kingMoves[6]))
                || (king.isMoveLegal(this, kingMoves[7])
                && isKingSafe(king, kingMoves[7])))));
    }

    /**
     * Executes upon checkmate, and game ends
     *
     * @param king king that was checked
     */
    private void checkmate(King king) {
        String output = "Checkmate";

        System.out.println(output);

        output = king.isWhite() ? "Black" : "White";

        System.out.println(output + " wins");
        System.exit(0);
    }

    /**
     * Used during Board instantiation: initialize all Piece and Cell instances
     * to their default starting positions prior to beginning a Chess match
     */
    private void assignWhitePieces() {
        Piece king = whiteSet.getPieceByType(PieceType.KING);
        king.alive = true;
        king.posRef = cell[4][0].loc;
        cell[4][0].pieceRef = king;

        Piece queen = whiteSet.getPieceByType(PieceType.QUEEN);
        queen.posRef = cell[3][0].loc;
        queen.alive = true;
        cell[3][0].pieceRef = queen;
        cell[3][0].pieceRef.alive = true;

        Piece bishop_r = whiteSet.getPieceByType(PieceType.BISHOP_R);
        bishop_r.alive = true;
        bishop_r.posRef = cell[5][0].loc;
        cell[5][0].pieceRef = bishop_r;

        Piece bishop_l = whiteSet.getPieceByType(PieceType.BISHOP_L);
        bishop_l.alive = true;
        bishop_l.posRef = cell[2][0].loc;
        cell[2][0].pieceRef = bishop_l;

        Piece knight_r = whiteSet.getPieceByType(PieceType.KNIGHT_R);
        knight_r.alive = true;
        knight_r.posRef = cell[6][0].loc;
        cell[6][0].pieceRef = knight_r;

        Piece knight_l = whiteSet.getPieceByType(PieceType.KNIGHT_L);
        knight_l.alive = true;
        knight_l.posRef = cell[1][0].loc;
        cell[1][0].pieceRef = knight_r;

        Piece rook_r = whiteSet.getPieceByType(PieceType.ROOK_R);
        rook_r.alive = true;
        rook_r.posRef = cell[7][0].loc;
        cell[7][0].pieceRef = rook_r;

        Piece rook_l = whiteSet.getPieceByType(PieceType.ROOK_L);
        rook_l.alive = true;
        rook_l.posRef = cell[0][0].loc;
        cell[0][0].pieceRef = rook_l;

        Piece pawn_0 = whiteSet.getPieceByType(PieceType.PAWN_0);
        pawn_0.alive = true;
        pawn_0.posRef = cell[0][1].loc;
        cell[0][1].pieceRef = pawn_0;

        Piece pawn_1 = whiteSet.getPieceByType(PieceType.PAWN_1);
        pawn_1.alive = true;
        pawn_1.posRef = cell[1][1].loc;
        cell[1][1].pieceRef = pawn_1;

        Piece pawn_2 = whiteSet.getPieceByType(PieceType.PAWN_2);
        pawn_2.alive = true;
        pawn_2.posRef = cell[2][1].loc;
        cell[2][1].pieceRef = pawn_2;

        Piece pawn_3 = whiteSet.getPieceByType(PieceType.PAWN_3);
        pawn_3.alive = true;
        pawn_3.posRef = cell[3][1].loc;
        cell[3][1].pieceRef = pawn_3;

        Piece pawn_4 = whiteSet.getPieceByType(PieceType.PAWN_4);
        pawn_4.alive = true;
        pawn_4.posRef = cell[4][1].loc;
        cell[4][1].pieceRef = pawn_4;

        Piece pawn_5 = whiteSet.getPieceByType(PieceType.PAWN_5);
        pawn_5.alive = true;
        pawn_5.posRef = cell[5][1].loc;
        cell[5][1].pieceRef = pawn_5;

        Piece pawn_6 = whiteSet.getPieceByType(PieceType.PAWN_6);
        pawn_6.alive = true;
        pawn_6.posRef = cell[6][1].loc;
        cell[6][1].pieceRef = pawn_6;

        Piece pawn_7 = whiteSet.getPieceByType(PieceType.PAWN_7);
        pawn_7.alive = true;
        pawn_7.posRef = cell[7][1].loc;
        cell[7][1].pieceRef = pawn_7;
    }

    /**
     * Used during Board instantiation: initialize all Piece and Cell instances
     * to their default starting positions prior to beginning a Chess match
     */
    private void assignBlackPieces() {
        Piece king = blackSet.getPieceByType(PieceType.KING);
        king.alive = true;
        king.posRef = cell[4][7].loc;
        cell[4][7].pieceRef = king;

        Piece queen = blackSet.getPieceByType(PieceType.QUEEN);
        queen.alive = true;
        queen.posRef = cell[3][7].loc;
        cell[3][7].pieceRef = queen;

        Piece bishop_r = blackSet.getPieceByType(PieceType.BISHOP_R);
        bishop_r.alive = true;
        bishop_r.posRef = cell[5][7].loc;
        cell[5][7].pieceRef = bishop_r;

        Piece bishop_l = blackSet.getPieceByType(PieceType.BISHOP_L);
        bishop_l.alive = true;
        bishop_l.posRef = cell[2][7].loc;
        cell[2][7].pieceRef = bishop_l;

        Piece knight_r = blackSet.getPieceByType(PieceType.KNIGHT_R);
        knight_r.alive = true;
        knight_r.posRef = cell[6][7].loc;
        cell[6][7].pieceRef = knight_r;

        Piece knight_l = blackSet.getPieceByType(PieceType.KNIGHT_L);
        knight_l.alive = true;
        knight_l.posRef = cell[1][7].loc;
        cell[1][7].pieceRef = knight_r;

        Piece rook_r = blackSet.getPieceByType(PieceType.ROOK_R);
        rook_r.alive = true;
        rook_r.posRef = cell[7][7].loc;
        cell[7][7].pieceRef = rook_r;

        Piece rook_l = blackSet.getPieceByType(PieceType.ROOK_L);
        rook_l.alive = true;
        rook_l.posRef = cell[0][7].loc;
        cell[0][7].pieceRef = rook_l;

        Piece pawn_0 = blackSet.getPieceByType(PieceType.PAWN_0);
        pawn_0.alive = true;
        pawn_0.posRef = cell[0][6].loc;
        cell[0][6].pieceRef = pawn_0;

        Piece pawn_1 = blackSet.getPieceByType(PieceType.PAWN_1);
        pawn_1.alive = true;
        pawn_1.posRef = cell[1][6].loc;
        cell[1][6].pieceRef = pawn_1;

        Piece pawn_2 = blackSet.getPieceByType(PieceType.PAWN_2);
        pawn_2.alive = true;
        pawn_2.posRef = cell[2][6].loc;
        cell[2][6].pieceRef = pawn_2;

        Piece pawn_3 = blackSet.getPieceByType(PieceType.PAWN_3);
        pawn_3.alive = true;
        pawn_3.posRef = cell[3][6].loc;
        cell[3][6].pieceRef = pawn_3;

        Piece pawn_4 = blackSet.getPieceByType(PieceType.PAWN_4);
        pawn_4.alive = true;
        pawn_4.posRef = cell[4][6].loc;
        cell[4][6].pieceRef = pawn_4;

        Piece pawn_5 = blackSet.getPieceByType(PieceType.PAWN_5);
        pawn_5.alive = true;
        pawn_5.posRef = cell[5][6].loc;
        cell[5][6].pieceRef = pawn_5;

        Piece pawn_6 = blackSet.getPieceByType(PieceType.PAWN_6);
        pawn_6.posRef = cell[6][6].loc;
        pawn_6.alive = true;
        cell[6][6].pieceRef = pawn_6;

        Piece pawn_7 = blackSet.getPieceByType(PieceType.PAWN_7);
        pawn_7.alive = true;
        pawn_7.posRef = cell[7][6].loc;
        cell[7][6].pieceRef = pawn_7;
    }

    /**
     * Returns a string representation of the Board's state
     */
    @Override
    public String toString() {
        String str = "";

        for (int rank = cell.length - 1; rank >= 0; rank--) {
            for (int file = 0; file < cell[rank].length; file++) {
                // FOR RELEASE: Print piece at cell
                if (cell[file][rank].pieceRef == null) {
                    if (rank % 2 != 0) {
                        if (file % 2 != 0) {
                            str += "##";
                        } else {
                            str += "  ";
                        }
                    } else {
                        if (file % 2 == 0) {
                            str += "##";
                        } else {
                            str += "  ";
                        }
                    }
                } else {
                    // FOR RELEASE: Print Pieces
                    str += cell[file][rank];

                    // FOR DEBUGGING: See positions instead of Pieces
                    // str += cell[file][rank].getPosition();
                }

                str += " "; // Two spaces in between cells

                if (file == cell[rank].length - 1) {
                    // FOR RELEASE: Print real rank enumerations
                    str += String.format("%d", rank + 1); // FOR RELEASE

                    // FOR DEBUGGING: Print rank indices as per an array
                    // str += String.format("%d", rank);
                }
            }

            str += "\n";
        }

        // FOR RELEASE: Print real file characters
        str += String.format(" a  b  c  d  e  f  g  h\n");

        // FOR DEBUGGING: Print file indices as per an array
        // str += String.format(" 0 1 2 3 4 5 6 7\n");

        return str;
    }
}
