import java.util.*;
import java.time.*;

public class MinMaxStrategy implements GameStrategy {
	private int timeLimit;
	private int discs;
	private int cutoffDepth;
	
	public MinMaxStrategy (int cutoff) {
		cutoffDepth = cutoff;
	}
	
	public void setTimeLimit(int millis) {
		timeLimit = millis;
	}
	
	public String nextMove (Board board, Instant timeKeeping) {
		discs = board.getNbrOfDiscs();
		return miniMaxDecision(board, timeKeeping, false);
	}
	
	private boolean cutoffTest (Board b) {
		if (((b.getNbrOfDiscs() - discs) > cutoffDepth) || (b.getNbrOfDiscs() == 64)) return true;
		return false;	
	}

	public String miniMaxDecision(Board board, Instant timeKeeping, boolean panic) {
		Board currentBoard = new Board(board);
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		String move = legalMoves.get(0);
		int value = minValue(new Board(currentBoard), move, timeKeeping, false);
		for (int i=1; i<legalMoves.size(); i++) {
			if (Instant.now().isAfter(timeKeeping.plusMillis(timeLimit-50))) panic = true;
			if (panic) return move;
			else {
				int v = minValue(new Board(currentBoard), legalMoves.get(i), timeKeeping, panic);
				if (value < v) {
					value =  v;
					move = legalMoves.get(i);
				}
			}
		}
		return move;
	}

	public int maxValue(Board board, String move, Instant timeKeeping, boolean panic) {
		Board currentBoard = new Board(board);
		currentBoard.play(move);
		if (cutoffTest(currentBoard)) return currentBoard.eval();
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		int value = Integer.MIN_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			if (Instant.now().isAfter(timeKeeping.plusMillis(timeLimit-50))) panic = true;
			if (panic) return value; //NOTE: this may yield a return value of Integer.MIN_VALUE (not likely w/ reasonable timelimits and depth, though)
			else value = Math.max(value, minValue(currentBoard, legalMoves.get(i), timeKeeping, false));
		}
		return value;
	}

	public int minValue(Board board, String move, Instant timeKeeping, boolean panic) {
		Board currentBoard = new Board(board);
		currentBoard.play(move);
		if (cutoffTest(currentBoard)) return currentBoard.eval();
		ArrayList<String> legalMoves = currentBoard.findLegalMoves();
		int value = Integer.MAX_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			if (Instant.now().isAfter(timeKeeping.plusMillis(timeLimit-50))) panic = true;
			if (panic) return value; //NOTE: as above
			else value = Math.min(value, maxValue(currentBoard, legalMoves.get(i), timeKeeping, false));
		}
		return value;
	}
}
