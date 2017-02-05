import java.util.*;
public class AlfaBetaStrategy implements GameStrategy {

	private int discs;
	private int cutoffDepth;

	public AlfaBetaStrategy (int cutoff) {
		cutoffDepth = cutoff;
	}
	
	public String nextMove (Board board) {
		discs = board.getNbrOfDiscs();
		return ab_search(board);
	}
	
	private boolean cutoffTest (Board b) {
		if ((b.getNbrOfDiscs() - discs) > cutoffDepth) return true;
		return false;	
	}

	    public String ab_search(Board board) {
	    Board currentBoard = new Board(board);
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		String move = legalMoves.get(0);
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
      	int value = ab_minValue(currentBoard, move, alpha, beta);
      	for (int i=1; i<legalMoves.size(); i++) {
        	int v = ab_minValue(currentBoard, legalMoves.get(i), alpha, beta);
        	if (value < v) {
         	   value =  v;
			   move = legalMoves.get(i);
		   	}
      	}
      	return move;
    }
	
    public int ab_maxValue(Board board, String move, int alpha, int beta) {
    	Board currentBoard = new Board(board);
    	currentBoard.play(move);
		if (cutoffTest(currentBoard)) return currentBoard.eval();
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		int value = Integer.MIN_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			value = Math.max(value, ab_minValue(currentBoard, legalMoves.get(i), alpha, beta));
			if (value >= beta) return value;
			alpha = Math.max(alpha, value);
		}
		return value;
	}
    
    public int ab_minValue(Board board, String move, int alpha, int beta) {
    	Board currentBoard = new Board(board);
		currentBoard.play(move);
		if (cutoffTest(currentBoard)) return currentBoard.eval();
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		int value = Integer.MAX_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			value = Math.min(value, ab_maxValue(currentBoard, legalMoves.get(i), alpha, beta));
			if (value <= alpha) return value;
			beta = Math.min(beta, value);
		}
		return value;
    }
}