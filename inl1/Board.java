import java.util.*;

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
	

	public void play (String move) {
		//place disk
		int x = Character.getNumericValue(move.charAt(0));
		int y = Character.getNumericValue(move.charAt(1));
		board[x][y] = turn;
		
		boolean[][] matrix = checkNeighbours(x, y);
		boolean legMove = false;
		if (matrix[0][0]) if(directionSearch(x-1, y-1, "NW")) flip(x-1, y-1,"NW");
		if (matrix[1][0]) if(directionSearch(x, y-1, "W")) flip(x, y-1, "W");
		if (matrix[2][0]) if(directionSearch(x+1, y-1, "SW")) flip(x+1, y-1, "SW");

		if (matrix[0][1]) if(directionSearch(x-1, y, "N")) flip(x-1, y, "N");
		if (matrix[2][1]) if(directionSearch(x+1, y, "S")) flip(x+1, y, "S");

		if (matrix[0][2]) if(directionSearch(x-1, y+1, "NE")) flip(x-1, y+1, "NE");
		if (matrix[1][2]) if(directionSearch(x, y+1, "E")) flip(x, y+1, "E");
		if (matrix[2][2]) if(directionSearch(x+1, y+1, "SE")) flip(x+1, y+1, "SE");

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

	private boolean flip (int x, int y, String direction) {
		if (x == 0 || x == 9 || y == 0 || y == 9) return false;
		board[x][y] = turn;
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

		if (board[xCoord][yCoord] == (0-turn)) return directionSearch(xCoord, yCoord, direction);
		else if (board[xCoord][yCoord] == turn) return true;
		else return false;
	}
	
	
	private boolean isLegalMove (int x, int y) {
		//gives all neighbours of opposing color
		boolean[][] matrix = checkNeighbours(x, y);
		
		boolean legMove = false;
		if (matrix[0][0]) if(directionSearch(x-1, y-1, "NW")) legMove = true;
		if (matrix[1][0]) if(directionSearch(x, y-1, "W")) legMove = true;
		if (matrix[2][0]) if(directionSearch(x+1, y-1, "SW")) legMove = true;

		if (matrix[0][1]) if(directionSearch(x-1, y, "N")) legMove = true;
		if (matrix[2][1]) if(directionSearch(x+1, y, "S")) legMove = true;

		if (matrix[0][2]) if(directionSearch(x-1, y+1, "NE")) legMove = true;
		if (matrix[1][2]) if(directionSearch(x, y+1, "E")) legMove = true;
		if (matrix[2][2]) if(directionSearch(x+1, y+1, "SE")) legMove = true;
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

	public boolean directionSearch(int x, int y, String direction) {
		if (x == 0 || x == 9 || y == 0 || y == 9) return false;

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

		if (board[xCoord][yCoord] == (0-turn)) return directionSearch(xCoord, yCoord, direction);
		else if (board[xCoord][yCoord] == turn) return true;
		else return false;
	}
}
