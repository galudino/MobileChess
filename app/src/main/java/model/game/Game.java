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
 * Represents the state of a Chess game and all of its components.
 * Instances of Game are to be created within the client program.
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
	
	private static final String whiteSpaceRegex = 
			"[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]";
	private static final String fileRankRegex = "[a-h][1-8]";
	private static final String validFileRankRegex = 
			String.format("%s%s%s", fileRankRegex,
					whiteSpaceRegex, fileRankRegex);

	private static final String drawRegex = "draw\\?";
	private static final String validFileRankWithDrawRegex = 
			String.format("%s%s%s",
			validFileRankRegex, whiteSpaceRegex, drawRegex);

	private static final String pieceRegex = "[qQbBnNrR]";

	private static final String validFileRankWithPromotion = 
			String.format("%s%s%s",
			validFileRankRegex, whiteSpaceRegex, pieceRegex);
	
	/**
	 * Default constructor
	 */
	public Game() {
		board = new Board();

		white = new Player(PieceType.Color.WHITE, board);
		black = new Player(PieceType.Color.BLACK, board);
		
		active = true;

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
	 * Retrieves the last move from the Board's moveList
	 * 
	 * @return the Move that describes the last piece to be successfully moved,
	 * otherwise null if there are no pieces moved
	 */
	public Move getLastMove() {
		return board.getLastMove();
	}
	
	/**
	 * Retrieves the last kill from the Board's moveList
	 * 
	 * @return the Move that describes the last piece to be killed,
	 * otherwise null if there are no pieces killed
	 */
	public Move getLastKill() {
		return board.getLastKill();
	}
	
	/**
	 * Mutator to toggle the game's status.
	 * If false (game not active), active will be switched to true.
	 * Else (game is current active), active will be switched to false.
	 */
	public void toggleActive() {
		active = active ? false : true;
	}
	
	/**
	 * Accessor to retrieve status of game
	 * 
	 * @return true if game is active, false otherwise
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Accessor to determine if it is white player's move, or not
	 * 
	 * @return true if white's move, false otherwise
	 */
	public boolean isWhitesMove() {
		return whitesMove;
	}
	
	/**
	 * Accessor to determine if a player requested a draw to the other player
	 * 
	 * @return true if draw requested, false otherwise
	 */
	public boolean isWillDraw() {
		return willDraw;
	}
	
	/**
	 * Accessor to determine if player requested to resign from the game
	 * 
	 * @return true if resign requested, false otherwise
	 */
	public boolean isWillResign() {
		return willResign;
	}
	
	/**
	 * Accessor to determine if a draw has been mutually agreed upon
	 * and will be done (willDraw and drawGranted must both be true,
	 * and the values of those variables are determined during the game)
	 * 
	 * @return true if the draw has been agreed upon, false otherwise
	 */
	public boolean isDidDraw() {
		return didDraw;
	}
	
	/**
	 * Accessor to determine if a draw requested made by one player
	 * has been accepted by the other player. This is the moment when
	 * the requestee (recipient of the request) agrees to a draw 
	 * after it was proposed by the requestee's opponent.
	 * 
	 * @return true if drawGranted is true, false otherwise
	 */
	public boolean isDrawGranted() {
		return drawGranted;
	}
	
	/**
	 * Accessor to determine if a resignation requested made by one player
	 * has been accepted by the other player. This is the moment when the
	 * requestee (recipient of the request) agrees to let their opponent
	 * resign from the game.
	 * 
	 * @return true if didResign is true, false otherwise
	 */
	public boolean isDidResign() {
		return didResign;
	}
	
	/**
	 * Accessor to determine if a move request results in a valid move and was
	 * completed by a player.
	 * 
	 * @return true if a valid move was made by a player, false otherwise
	 */
	public boolean isValidMoveInput() {
		return validMoveInput;
	}
	
	/**
	 * Accessor to determine if a move request results in a valid move and was
	 * completed by a player -- but has made a request to their opponent
	 * to end the game by draw.
	 * 
	 * @return true if validMoveInputWithDraw is true, false otherwise
	 */
	public boolean isValidMoveInputWithDraw() {
		return validMoveInputWithDraw;
	}
	
	/**
	 * Accessor to determine if a move request results in a valid move and was
	 * completed by a player -- but has made a request to promote their pawn
	 * Piece to a Queen, Bishop, Knight, or Rook.
	 * 
	 * @return true if validMoveInputWithPromotion is true, false otherwise
	 */
	public boolean isValidMoveInputWithPromotion() {
		return validMoveInputWithPromotion;
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
	 * Begins game loop
	 */
	public void start() {				
		Scanner scan = new Scanner(System.in);
		String input = "";

		System.out.println(board);
				
		while (active) {
			String prompt = "";

			validMoveInput = false;
			validMoveInputWithDraw = false;
			validMoveInputWithPromotion = false;

			drawGranted = false;
			willResign = false;
			
			do {
				validMoveInput = false;

				prompt = whitesMove ? "White's " : "Black's ";

				System.out.print(prompt + "move: ");
				input = scan.nextLine();
				System.out.println();
				
				readInput(input);
			} while (validMoveInput == false);

			whitesMove = whitesMove ? false : true;

			System.out.println(boardToString());
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
		
		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String input = "";

		System.out.println(board);
		
		while (active) {
			//String prompt = "";
			
			validMoveInput = false;
			validMoveInputWithDraw = false;
			validMoveInputWithPromotion = false;

			drawGranted = false;
			willResign = false;

			do {
				validMoveInput = false;

				input = bufferedReader.readLine();
				System.out.println();
				
				if (input == null) {
					bufferedReader.close();
					fileReader.close();
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
	
	public void readInput(String input) {
		int[] fileRankArray = null;
		String output = "";
		
		validMoveInput = input.matches(validFileRankRegex);
		validMoveInputWithDraw = input
				.matches(validFileRankWithDrawRegex);
		validMoveInputWithPromotion = input
				.matches(validFileRankWithPromotion);

		drawGranted = willDraw && input.equals("draw");
		willResign = input.equals("resign");

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

			if (whitesMove) {
				validMoveInput = whitePlayMove(file, rank, newFile,
						newRank, promo);
			} else {
				validMoveInput = blackPlayMove(file, rank, newFile,
						newRank, promo);
			}

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
	 * 
	 * @return an integer array representing a Piece's current position and the
	 *         desired position to move to
	 */
	private int[] getFileRankArray(String input) {
		//final String pieceRegex = "[qQbBnNrR]";

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
		whitePlay = new Position(file, rank);
		whiteNewPosition = new Position(newFile, newRank);

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
		blackPlay = new Position(file, rank);
		blackNewPosition = new Position(newFile, newRank);

		return black.playMove(blackPlay, blackNewPosition, promo);
	}
}
