import java.util.Scanner;
import java.io.*;
import java.lang.IllegalArgumentException;
public class Main {

	private static final char[] translationTable = {'A','B','C','D','E','F','G','H'};

	public static void main(String[] args) {

		Board board = new Board();
		BoardDrawer d = new BoardDrawer();
		int depth = 8;
		GameStrategy strat = new AlfaBetaStrategy(depth);

		char playerColour = 'b';
		char aiColour = 'w';
		int timeLimit = 10000; //milliseconds
		String playMode = "auto";
		String strategy = "ab";
		
		//CLI program setup
		if (args.length > 0) {
			try {
				for (int i=0;i<args.length;i++) {
					System.out.println(args[i]);
					if (args[i].charAt(0) == '-') {
						timeLimit = Integer.parseInt(args[i].substring(2, args[i].length()));
					} 
					else if (args[i].charAt(0) == 'b' || args[i].charAt(0) == 'w') {
						playerColour = args[i].charAt(0);
						if (playerColour == 'w') {
							aiColour = 'b';
						}
						
					} 
					else if (args[i].equals("play") || args[i].equals("auto")) {
						playMode = args[i];
					}
					else if (args[i].equals("ab") || args[i].equals("mm")) {
						strategy = args[i];
						if (strategy.equals("mm")) strat = new MinMaxStrategy(depth); 
					} else {
						throw new Exception();
					}
				}
			} catch (Exception e) {
				System.err.println("\nFour possible CLI arguments:");
				System.err.println("Argument #1: 'b' for user to play as black, 'w' to play as white. Default is 'b'.");
				System.err.println("Argument #2: '-tXXXX' to specify the time limit. XXXX is measured in milliseconds. Default is 10000 ms.");
				System.err.println("Argument #3: 'play' to play against the program, 'auto' to let the program play itself. Default is 'auto'.");
				System.err.println("Argument #4: 'mm' to use MinMax strategy, 'ab' to add alpha-beta pruning. Default is 'ab'.");
				System.err.println("Example: Reversi w -t10500 mm play\n");
				System.exit(1);
			}
		}
		
		int passCounter = 0;

		//interactive mode
		if (playMode.equals("play")) {
			System.out.print("\nHi, and welcome to Reversi. You are playing as " +
					((playerColour == 'b') ? "black (X)" : "white (O)") + ", and I am playing as " +
					((playerColour == 'w') ? "black (X)" : "white (O)") + ".\n\n");
			System.out.println(d.draw(board));

			boolean incorrect = false;

			while (board.getNbrOfDiscs() < 64 && passCounter < 2) {				

				if (board.terminalState()) {
					System.out.println("  " + board.whosTurn() + " is forced to pass.\n");
					passCounter++;
					board.swapTurn();
					incorrect = false;
				}

				//player's turn
				if (playerColour == board.whosTurn().charAt(0)) {
					if (!incorrect) {
						System.out.println("\n  " + board.whosTurn() + " to play, please enter a move: ");	
					}
					
					System.out.print("  ");
					try {
						Scanner scan = new Scanner(System.in);
						String playerMove = scan.nextLine().toUpperCase();
						String move = translateInputFormat(playerMove);
						String tempTurn = board.whosTurn();
						board.play(move);
						System.out.println("  " + tempTurn + " plays " + playerMove + ":");
						passCounter = 0;
						incorrect = false;
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
						incorrect  = true;
					}
				}

				//computer's turn
				else {
					String nextMove = strat.nextMove(board);
					String tempTurn = board.whosTurn();
					board.play(nextMove);
					int letter = (int) (nextMove.charAt(1) + 16);
					char c1 = (char) (letter);
					char c2 = nextMove.charAt(0);
					System.out.println("  " + tempTurn + " plays " + c1 + c2 + ":");
					passCounter = 0;
				}


				if (!incorrect) {
					System.out.println(d.draw(board));
				}
			}

		}

		//automatic mode
		else {
			System.out.print("Hi, and welcome to Reversi. I will demonstrate the game by playing vs. myself. Black (X) starts. ");
			System.out.print("I will be using the MinMax strategy" + ((strategy.equals("ab")) ? " with Alpha-Beta pruning. " : ". "));
			System.out.println("My move consideration time limit is " + timeLimit + " ms. Let's play: \n");
		
			System.out.println(d.draw(board));

			
			while (board.getNbrOfDiscs() < 64 && passCounter < 2) {


				if (board.terminalState()) {
					System.out.println(board.whosTurn() + " is forced to pass.\n");
					passCounter++;
					board.swapTurn();
				}

				else {
					String nextMove = strat.nextMove(board);
					System.out.println(nextMove);
					board.play(nextMove);
					System.out.println(d.draw(board));
					passCounter = 0;
				}
			}
		}

		//end statements
		int whites = board.getNbrOfWhites();
		int blacks = board.getNbrOfBlacks();
		System.out.println("The game has ended.");
		System.out.println("White has " + whites + " discs.");
		System.out.println("Black has " + blacks + " discs.");
		if (whites==blacks) System.out.println("The game was a draw.");
		else if (whites<blacks) System.out.println("Black wins!");
		else if (whites>blacks) System.out.println("White wins!");
	}


	//formchecks user input (not legality, only form)
	private static String translateInputFormat (String playerMove) throws IOException {
		if (playerMove.length() == 2) {
			int number = Character.getNumericValue(playerMove.charAt(0));
			if (number >= 1 || number <= 8) {
				for (int i=0; i<8; i++) {
					if (playerMove.charAt(0) == translationTable[i]) {
						int letter = (int) playerMove.charAt(0) - 16;
						StringBuilder move = new StringBuilder("");
						move.append(playerMove.charAt(1));
						move.append((char)(letter));
						return move.toString();
					}
				}
			}
		}
		throw new IOException("  Incorrect format, please try again: ");
	}
}