import java.util.*;
import java.lang.IllegalArgumentException;

public class Board {

	public  int[][] board = new int[10][10];
	private int turn;
	private int nbrOfDiscs;
	public static final int STATEEMPTY = 0;
	public static final int STATEWHITE = -1;
	public static final int STATEBLACK = 1;

	public Board () {
		board[4][4] = STATEWHITE;
		board[5][5] = STATEWHITE;
		board[4][5] = STATEBLACK;
		board[5][4] = STATEBLACK;
		nbrOfDiscs = 4;
		turn = STATEBLACK;
	}
	
	public Board (Board another) {
		for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
            	this.board[i][j] = another.board[i][j];
            }
        }
		this.turn = another.turn;
		this.nbrOfDiscs = another.nbrOfDiscs;
	}

	public int[][] getModel() {
		return board;
	}

	public int getNbrOfDiscs () {
		return nbrOfDiscs;
	}

	public int getNbrOfWhites () {
		int whites = 0;
		for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
            	if (board[i][j]==STATEWHITE) whites++;
            }
        }
        return whites;
	}

	public int getNbrOfBlacks () {
		int blacks = 0;
		for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
            	if (board[i][j]==STATEBLACK) blacks++;
            }
        }
        return blacks;
	}

	public String whosTurn () {
		if (turn==STATEWHITE) return "white";
		else return "black";
	}

	public void swapTurn () {
		turn = 0-turn;
	}

	public void play (String move) throws IllegalArgumentException {

		//place disk
		int x = Character.getNumericValue(move.charAt(0));
		int y = Character.getNumericValue(move.charAt(1));
		
		if (!isLegalMove(x,y)) throw new IllegalArgumentException("Illegl move, try again: ");

		board[x][y] = turn;
		
		boolean[][] matrix = checkNeighbours(x, y);
		boolean legMove = false;
		if (matrix[0][0]) if(directionSearch(x-1, y-1, "NW", false)) directionSearch(x-1, y-1,"NW", true);
		if (matrix[1][0]) if(directionSearch(x, y-1, "W", false)) directionSearch(x, y-1, "W", true);
		if (matrix[2][0]) if(directionSearch(x+1, y-1, "SW", false)) directionSearch(x+1, y-1, "SW", true);

		if (matrix[0][1]) if(directionSearch(x-1, y, "N", false)) directionSearch(x-1, y, "N", true);
		if (matrix[2][1]) if(directionSearch(x+1, y, "S", false)) directionSearch(x+1, y, "S", true);

		if (matrix[0][2]) if(directionSearch(x-1, y+1, "NE", false)) directionSearch(x-1, y+1, "NE", true);
		if (matrix[1][2]) if(directionSearch(x, y+1, "E", false)) directionSearch(x, y+1, "E", true);
		if (matrix[2][2]) if(directionSearch(x+1, y+1, "SE", false)) directionSearch(x+1, y+1, "SE", true);

		turn = 0-turn;
		nbrOfDiscs++;
	}

	public int eval() {
		int value = 0;
		for (int i=1;i<9;i++) {
			for (int j=1;j<9;j++) {
				if (board[i][j] == turn) value++;
			}
		}
		return value;
	}

	//checks if player who's currently playing can play, not terminal state altogether
	public boolean terminalState () {
		if (findLegalMoves().size() != 0) return false;
		else return true;
	}

	public ArrayList<String> findLegalMoves() {
		ArrayList<String> legMoves = new ArrayList<String>();

		for (int i=1;i<9;i++) {
			for (int j=1;j<9;j++) {
				if (board[i][j] == STATEEMPTY && isLegalMove(i,j)) {
					legMoves.add(Integer.toString(i) + Integer.toString(j));
				} 
			}
		}

		return legMoves;
	}

	private boolean isLegalMove (int x, int y) {
		//gives all neighbours of opposing color
		boolean[][] matrix = checkNeighbours(x, y);
		
		boolean legMove = false;
		if (matrix[0][0]) if(directionSearch(x-1, y-1, "NW", false)) legMove = true;
		if (matrix[1][0]) if(directionSearch(x, y-1, "W", false)) legMove = true;
		if (matrix[2][0]) if(directionSearch(x+1, y-1, "SW", false)) legMove = true;

		if (matrix[0][1]) if(directionSearch(x-1, y, "N", false)) legMove = true;
		if (matrix[2][1]) if(directionSearch(x+1, y, "S", false)) legMove = true;

		if (matrix[0][2]) if(directionSearch(x-1, y+1, "NE", false)) legMove = true;
		if (matrix[1][2]) if(directionSearch(x, y+1, "E", false)) legMove = true;
		if (matrix[2][2]) if(directionSearch(x+1, y+1, "SE", false)) legMove = true;
		return legMove;
	}
	

	public boolean[][] checkNeighbours(int x, int y) {
		boolean[][] interestingNeighbours = new boolean[3][3];
		if (board[x-1][y-1] == (0-turn)) interestingNeighbours[0][0] = true;
		if (board[x][y-1] == (0-turn)) interestingNeighbours[1][0] = true;
		if (board[x+1][y-1] == (0-turn)) interestingNeighbours[2][0] = true;

		if (board[x-1][y] == (0-turn)) interestingNeighbours[0][1] = true;
		if (board[x+1][y] == (0-turn)) interestingNeighbours[2][1] = true;

		if (board[x-1][y+1] == (0-turn)) interestingNeighbours[0][2] = true;
		if (board[x][y+1] == (0-turn)) interestingNeighbours[1][2] = true;
		if (board[x+1][y+1] == (0-turn)) interestingNeighbours[2][2] = true;

		return interestingNeighbours;
	}

	public boolean directionSearch(int x, int y, String direction, boolean flip) {
		if (x == 0 || x == 9 || y == 0 || y == 9) return false;
		
		if (flip) board[x][y] = turn;

		int xCoord = x;
		int yCoord = y;

		switch (direction) {
		case "NW":
			xCoord--;
			yCoord--;
			break;
		case "N":
			xCoord--;
			break;
		case "NE":
			xCoord--;
			yCoord++;
			break;
		case "E":
			yCoord++;
			break;
		case "SE":
			xCoord++;
			yCoord++;
			break;
		case "S":
			xCoord++;
			break;
		case "SW":
			xCoord++;
			yCoord--;
			break;
		case "W":
			yCoord--;
			break;
		}
		if (board[xCoord][yCoord] == (0-turn)) return directionSearch(xCoord, yCoord, direction, flip);
		else if (board[xCoord][yCoord] == turn) return !flip;
		else return false;
	}
}
