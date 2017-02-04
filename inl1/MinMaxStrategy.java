import java.util.*;

public class MinMaxStrategy implements GameStrategy {

	private int discs;
	private int cutoffDepth;
	
	public MinMaxStrategy (int cutoff) {
		cutoffDepth = cutoff;
	}
	
	public String nextMove (Board board) {
		discs = board.getNbrOfDiscs();
		return miniMaxDecision(board);
	}
	
	private boolean cutoffTest (Board b) {
		if ((b.getNbrOfDiscs() - discs) > cutoffDepth) return true;
		return false;	
	}

	public String miniMaxDecision(Board board) {
		Board currentBoard = new Board(board);
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		String move = legalMoves.get(0);
		int value = minValue(new Board(currentBoard), move);
		for (int i=1; i<legalMoves.size(); i++) {
			int v = minValue(new Board(currentBoard), legalMoves.get(i));
			if (value < v) {
				value =  v;
				move = legalMoves.get(i);
			}
		}
		return move;
	}

	public int maxValue(Board board, String move) {
		Board currentBoard = new Board(board);
		currentBoard.play(move);
		if (cutoffTest(currentBoard)) return currentBoard.eval();
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		int value = Integer.MIN_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			value = Math.max(value, minValue(currentBoard, legalMoves.get(i)));
		}
		return value;
	}

	public int minValue(Board board, String move) {
		Board currentBoard = new Board(board);
		currentBoard.play(move);
		if (cutoffTest(currentBoard)) return currentBoard.eval();
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		int value = Integer.MAX_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			value = Math.min(value,maxValue(currentBoard, legalMoves.get(i)));
		}
		return value;
	}
}
