public class Main {

	public static void main(String[] args) {

		Board board = new Board();
		BoardDrawer d = new BoardDrawer();
		GameStrategy strat = new MinMaxStrategy(4);

		char playerColour = 'b';
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
					} 
					else if (args[i].equals("play") || args[i].equals("auto")) {
						playMode = args[i];
					}
					else if (args[i].equals("ab") || args[i].equals("mm")) {
						strategy = args[i];
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
		
		if (playMode.equals("play")) {
			System.out.print("Hi, and welcome to Reversi. You are playing as " +
					((playerColour == 'b') ? "black (X)" : "white (O)") + ", and I am playing as " +
					((playerColour == 'w') ? "black (X)" : "white (O)") + ".");
		} else System.out.print("Hi, and welcome to Reversi. I will demonstrate the game by playing vs. myself. Black (X) starts. ");
		System.out.print("I will be using the MinMax strategy" + ((strategy.equals("ab")) ? " with Alpha-Beta pruning. " : ". "));
		System.out.println("My move consideration time limit is " + timeLimit + " ms. Let's play: ");
		
		System.out.println(d.draw(board));
		
		System.out.println("before loop");

		while (!board.terminalState()) {

			String nextMove = strat.nextMove(board);
			System.out.println(nextMove);
			board.play(nextMove);
			System.out.println(d.draw(board));
			try {
				Thread.sleep(500);
			}
			catch (Exception e) {
				//jahaja
			}
		}
	}
}