public class Main {

	public static void main(String[] args) {

		Board board = new Board();
		BoardDrawer d = new BoardDrawer();
		GameStrategy strat = new MinMaxStrategy(3);

		char playerColour = 'b';
		int timeLimit = 10000; //milliseconds
		
		//CLI program setup
		if (args.length > 0) {
			try {
				for (int i=0;i<args.length;i++) {
					if (args[i].charAt(0) == '-') {
						timeLimit = Integer.parseInt(args[i].substring(2, args[i].length()));
					}
					else {
						playerColour = args[i].charAt(0);
						if (playerColour != 'b' && playerColour != 'w') throw new Exception();
					}
				}
			} catch (Exception e) {
				System.err.println("\nTwo possible CLI arguments:");
				System.err.println("'b' for user to play as black, 'w' to play as white. No arg defaults to 'b'.");
				System.err.println("'-tXXXX' to specify the time limit. XXXX is measured in milliseconds.");
				System.err.println("Example: Reversi b -t10500\n");
				System.exit(1);
			}
		}
		System.out.println("Hi, and welcome to Reversi. You are playing as " +
				((playerColour == 'b') ? "black" : "white") + ", and I am playing as " +
				((playerColour == 'w') ? "black" : "white") + ".");
		System.out.println("My move consideration time limit is " +
				timeLimit + " ms. Let's play: ");
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