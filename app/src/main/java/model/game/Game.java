/**
 * Game.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj.
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.game;

import java.io.*;
import java.util.Scanner;

import model.PieceType;
import model.chess_set.Board;

/**
 * Represents the state of a Chess game and all of its components. Instances of
 * Game are to be created within the client program.
 *
 * @version Mar 5, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Game {

    private Board board;

    private Player white;
    private Player black;

    private Position whitePlay;
    private Position whiteNewPosition;
    private Position blackPlay;
    private Position blackNewPosition;

    private boolean active;

    private boolean whitesMove;

    private boolean willDraw;
    private boolean willResign;

    private boolean didDraw;
    private boolean drawGranted;
    private boolean didResign;

    private boolean validMoveInput;
    private boolean validMoveInputWithDraw;
    private boolean validMoveInputWithPromotion;

    /**
     * Default constructor
     */
    public Game() {
        board = new Board();

        white = new Player(PieceType.Color.WHITE, board);
        black = new Player(PieceType.Color.BLACK, board);

        active = false;
        whitesMove = true;

        willDraw = false;
        willResign = false;

        didDraw = false;
        drawGranted = false;
        didResign = false;

        validMoveInput = false;
        validMoveInputWithDraw = false;
        validMoveInputWithPromotion = false;
    }

    /**
     * Begins game loop
     */
    public void start() {
        System.out.println(board);

        Scanner scan = new Scanner(System.in);

        active = true;

        while (active) {
            validMoveInput = false;
            validMoveInputWithDraw = false;
            validMoveInputWithPromotion = false;

            drawGranted = false;
            willResign = false;

            String prompt = "";
            String input = "";

            do {
                prompt = whitesMove ? "White's " : "Black's ";
                System.out.print(prompt + "move: ");

                /**
                 * Retrieve input from user
                 */
                input = scan.nextLine();
                System.out.println();

                readInput(input);
            } while (validMoveInput == false);

            whitesMove = whitesMove ? false : true;

            System.out.println(boardToString());

            //@formatter:off
            /**
             * DIAGNOSTICS
             */
			/*
			printMoveLog();

			if (whitesMove == false) {
				white.printPieceSet();
			} else {
				black.printPieceSet();
			}
			*/
            //@formatter:on
        }

        scan.close();
    }

    /**
     * Starts a game from a String inputFilePath to an existing file.
     * Precondition: File to inputFilePath must exist.
     *
     * @param inputFilePath String representing the input file to be read
     * @throws IOException On nonexistent inputFilePath
     *
     * @return true if game can continue, false otherwise
     */
    public void startFromFile(String inputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);

        if (inputFile.exists() == false) {
            System.err.println("Error: " + inputFilePath + " does not exist.");
            System.exit(0);
        }

        System.out.println(board);

        FileReader fileReader = new FileReader(inputFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        active = true;

        while (active) {
            validMoveInput = false;
            validMoveInputWithDraw = false;
            validMoveInputWithPromotion = false;

            drawGranted = false;
            willResign = false;

            String input = "";

            do {
                /**
                 * Retrieve input from file
                 */
                input = bufferedReader.readLine();
                System.out.println();

                if (input == null) {
                    start();
                    break;
                }

                readInput(input);
            } while (validMoveInput == false);

            whitesMove = whitesMove ? false : true;

            System.out.println(boardToString());

            /**
             * DIAGNOSTICS
             */
            printMoveLog();

            if (whitesMove == false) {
                white.printPieceSet();
            } else {
                black.printPieceSet();
            }
        }

        bufferedReader.close();
        fileReader.close();
    }

    /**
     * Prints the current state of the move list
     */
    public void printMoveLog() {
        board.printMoveLog();
    }

    /**
     * Returns the current state of the Game as an ASCII chess board
     *
     * @return string representation of the Game's Board instance
     */
    public String boardToString() {
        return board.toString();
    }

    /**
     * Piece from the white PieceSet will be moved to a new Cell on the Board
     *
     * @param file    x axis coordinate of a requested Piece (0-7 only)
     * @param rank    y axis coordinate of a requested Piece (0-7 only)
     * @param newFile x axis coordinate of the desired move (0-7 only)
     * @param newRank y axis coordinate of the desired move (0-7 only)
     * @param promo   integer that represents the piece to promote to (if != -1)
     *
     * @return true if move executed successfully, false otherwise
     */
    private boolean whitePlayMove(int file, int rank, int newFile, int newRank,
                                  int promo) {
        whitePlay = board.getPosition(file, rank);
        whiteNewPosition = board.getPosition(newFile, newRank);

        return white.playMove(whitePlay, whiteNewPosition, promo);
    }

    /**
     * Piece from the black PieceSet will be moved to a new Cell on the Board
     *
     * @param file    x axis coordinate of a requested Piece (0-7 only)
     * @param rank    y axis coordinate of a requested Piece (0-7 only)
     * @param newFile x axis coordinate of the desired move (0-7 only)
     * @param newRank y axis coordinate of the desired move (0-7 only)
     * @param promo   integer that represents the piece to promote to (if != -1)
     *
     * @return true if move executed successfully, false otherwise
     */
    private boolean blackPlayMove(int file, int rank, int newFile, int newRank,
                                  int promo) {
        blackPlay = board.getPosition(file, rank);
        blackNewPosition = board.getPosition(newFile, newRank);

        return black.playMove(blackPlay, blackNewPosition, promo);
    }

    /**
     * Assesses String input for executing moves per Player
     *
     * @param input String parsed from user input or plaintext file
     */
    private void readInput(String input) {
        final String whiteSpaceRegex = "[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]";
        final String fileRankRegex = "[a-h][1-8]";
        final String validFileRankRegex = String.format("%s%s%s", fileRankRegex,
                whiteSpaceRegex, fileRankRegex);

        final String drawRegex = "draw\\?";
        final String validFileRankWithDrawRegex = String.format("%s%s%s",
                validFileRankRegex, whiteSpaceRegex, drawRegex);

        final String pieceRegex = "[qQbBnNrR]";

        final String validFileRankWithPromotion = String.format("%s%s%s",
                validFileRankRegex, whiteSpaceRegex, pieceRegex);

        validMoveInput = input.matches(validFileRankRegex);

        validMoveInputWithDraw = input.matches(validFileRankWithDrawRegex);

        validMoveInputWithPromotion = input.matches(validFileRankWithPromotion);

        drawGranted = willDraw && input.equals("draw");
        willResign = input.equals("resign");

        String output = "";

        int[] fileRankArray = null;

        if (validMoveInput) {
            willDraw = false;
            fileRankArray = getFileRankArray(input);
        } else if (validMoveInputWithPromotion) {
            willDraw = false;
            fileRankArray = getFileRankArray(input);
        } else if (validMoveInputWithDraw) {
            willDraw = true;
            fileRankArray = getFileRankArray(input);
        } else if (drawGranted) {
            didDraw = true;
            output = "draw";
        } else if (willResign) {
            didResign = true;
            output = whitesMove ? "Black wins" : "White wins";
        } else {
            validMoveInput = false;
            output = "Invalid input, try again\n";
        }

        if (validMoveInput || validMoveInputWithPromotion
                || validMoveInputWithDraw) {
            int file = -1;
            int rank = -1;
            int newFile = -1;
            int newRank = -1;
            int promo = -1;

            file = fileRankArray[0];
            rank = fileRankArray[1];
            newFile = fileRankArray[2];
            newRank = fileRankArray[3];
            promo = fileRankArray[4];

            validMoveInput = whitesMove
                    ? whitePlayMove(file, rank, newFile, newRank, promo)
                    : blackPlayMove(file, rank, newFile, newRank, promo);

            output = validMoveInput ? "" : "Illegal move, try again";
        }

        System.out.println(output);

        if (didDraw || didResign) {
            System.exit(0);
        }
    }

    /**
     * Parses a line of input into an integer array of a Piece's current file
     * and rank, and the desired file and rank for a new Position
     *
     * @param input a line of input
     * @return an integer array representing a Piece's current position and the
     *         desired position to move to
     */
    private int[] getFileRankArray(String input) {
        final String fileRankRegex = "[a-h][1-8]";
        // final String pieceRegex = "[qQbBnNrR]";

        String fileRankStr = "";
        String newFileNewRankStr = "";
        String promoStr = null;

        int file = -1;
        int rank = -1;
        int newFile = -1;
        int newRank = -1;
        int promo = -1;

        int[] result = new int[5];

        Scanner parse = new Scanner(input);
        fileRankStr = parse.next(fileRankRegex).toLowerCase();
        newFileNewRankStr = parse.next(fileRankRegex).toLowerCase();

        if (parse.hasNext()) {
            promoStr = parse.next();
            promoStr = promoStr.toUpperCase();
        }

        char chFile = fileRankStr.charAt(0);
        char chNewFile = newFileNewRankStr.charAt(0);
        char chPromo = promoStr != null ? promoStr.charAt(0) : '!';

        switch (chFile) {
            case 'a':
                file = 0;
                break;
            case 'b':
                file = 1;
                break;
            case 'c':
                file = 2;
                break;
            case 'd':
                file = 3;
                break;
            case 'e':
                file = 4;
                break;
            case 'f':
                file = 5;
                break;
            case 'g':
                file = 6;
                break;
            case 'h':
                file = 7;
                break;
        }

        switch (chNewFile) {
            case 'a':
                newFile = 0;
                break;
            case 'b':
                newFile = 1;
                break;
            case 'c':
                newFile = 2;
                break;
            case 'd':
                newFile = 3;
                break;
            case 'e':
                newFile = 4;
                break;
            case 'f':
                newFile = 5;
                break;
            case 'g':
                newFile = 6;
                break;
            case 'h':
                newFile = 7;
                break;
        }

        switch (chPromo) {
            case 'Q':
                promo = PieceType.QUEEN.ordinal();
                break;
            case 'B':
                promo = PieceType.BISHOP_R.ordinal();
                break;
            case 'N':
                promo = PieceType.KNIGHT_R.ordinal();
                break;
            case 'R':
                promo = PieceType.ROOK_R.ordinal();
                break;
            default:
                promo = PieceType.QUEEN.ordinal();
                break;
        }

        rank = Integer.parseInt(fileRankStr.substring(1)) - 1;
        newRank = Integer.parseInt(newFileNewRankStr.substring(1)) - 1;

        result[0] = file;
        result[1] = rank;
        result[2] = newFile;
        result[3] = newRank;
        result[4] = promo;

        parse.close();
        return result;
    }
}
