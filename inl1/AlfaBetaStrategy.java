import java.util.*;
import java.time.*;

public class AlfaBetaStrategy implements GameStrategy {
	private int timeLimit;
	private int discs;
	private int cutoffDepth;

	public AlfaBetaStrategy (int cutoff) {
		cutoffDepth = cutoff;
	}
	
	public void setTimeLimit(int millis) {
		timeLimit = millis;
	}
	
	public String nextMove (Board board, Instant timeKeeping) {
		discs = board.getNbrOfDiscs();
		return ab_search(board, timeKeeping, false);
	}
	
	private boolean cutoffTest (Board b) {
		if (((b.getNbrOfDiscs() - discs) > cutoffDepth) || (b.getNbrOfDiscs() == 64)) return true;
		return false;	
	}

	public String ab_search(Board board, Instant timeKeeping, boolean panic) {
		Board currentBoard = new Board(board);
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		String move = legalMoves.get(0);
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int value = ab_minValue(currentBoard, move, alpha, beta, timeKeeping, false);
		for (int i=1; i<legalMoves.size(); i++) {
			if (Instant.now().isAfter(timeKeeping.plusMillis(timeLimit-50))) panic = true;
			if (panic) return move;
			else {
				int v = ab_minValue(currentBoard, legalMoves.get(i), alpha, beta, timeKeeping, panic);
				if (value < v) {
				   value =  v;
				   move = legalMoves.get(i);
				}
			}
		}
		return move;
    }
	
    public int ab_maxValue(Board board, String move, int alpha, int beta, Instant timeKeeping, boolean panic) {
    	Board currentBoard = new Board(board);
    	currentBoard.play(move);
		if (cutoffTest(currentBoard)) return currentBoard.eval();
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		int value = Integer.MIN_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			if (Instant.now().isAfter(timeKeeping.plusMillis(timeLimit-50))) panic = true;
			if (panic) return value; //NOTE: this may yield a return value of Integer.MIN_VALUE (not likely w/ reasonable timelimits and depth, though)
			else {
				value = Math.max(value, ab_minValue(currentBoard, legalMoves.get(i), alpha, beta, timeKeeping, panic));
				if (value >= beta) return value;
				alpha = Math.max(alpha, value);
			}
		}
		return value;
	}
    
    public int ab_minValue(Board board, String move, int alpha, int beta, Instant timeKeeping, boolean panic) {
    	Board currentBoard = new Board(board);
		currentBoard.play(move);
		if (cutoffTest(currentBoard)) return currentBoard.eval();
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		int value = Integer.MAX_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			if (Instant.now().isAfter(timeKeeping.plusMillis(timeLimit-50))) panic = true;
			if (panic) return value; //NOTE: as above
			else {
				value = Math.min(value, ab_maxValue(currentBoard, legalMoves.get(i), alpha, beta, timeKeeping, panic));
				if (value <= alpha) return value;
				beta = Math.min(beta, value);
			}
		}
		return value;
    }
}