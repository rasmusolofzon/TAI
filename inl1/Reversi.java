

public class Reversi {
	public int[][] board = new int[10][10];
	public static final int STATEEMPTY = 0;
	public static final int STATEWHITE = -1;
	public static final int STATEBLACK = 1;
	public int turn;
	public int[][] legalMovesMatrix = new int[10][10];
	
	public Reversi() {
		for (int i=1;i<9;i++) {
			for (int j=1;j<9;j++) {
				if ((i == 4 && j == 4) || (i == 5 && j == 5)) board[i][j] = STATEWHITE;
				else if ((i == 4 && j == 5) || (i == 5 && j == 4)) board[i][j] = STATEBLACK;
				else board[i][j] = STATEEMPTY;
			}
		}
		turn = STATEBLACK;
	}
	
	public boolean gameOn() {
		int counter = 0;
		boolean cont = false;
		
		for (int i=1;i<9;i++) {
			for (int j=1;j<9;j++) {
				if (board[i][j] == STATEEMPTY) {
					counter++;
				}
			}
		}
		
		if (counter != 0) cont = true;
		
		if (legalMoves() != 0) cont = true;
		
		return cont;
	}
	
	public int legalMoves() {
		int legMoves = 0;
		
		for (int i=1;i<9;i++) {
			for (int j=1;j<9;j++) {
				if (board[i][j] == STATEEMPTY) {
					boolean[][] matrix = checkNeighbours(i, j);
					boolean legMove = false;
					
					if (matrix[0][0]) if(searchNW(i-1, j-1)) legMove = true;
					if (matrix[1][0]) if(searchW(i, j-1)) legMove = true;
					if (matrix[2][0]) if(searchSW(i+1, j-1)) legMove = true;
					
					if (matrix[0][1]) if(searchN(i-1, j)) legMove = true;
					if (matrix[2][1]) if(searchS(i+1, j)) legMove = true;
					
					if (matrix[0][2]) if(searchNE(i-1, j+1)) legMove = true;
					if (matrix[1][2]) if(searchE(i, j+1)) legMove = true;
					if (matrix[2][2]) if(searchSE(i+1, j+1)) legMove = true;
					
					if (legMove) legMoves++;
				} 
			}
		}
		
		return legMoves;
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
	
	public boolean searchNW(int x, int y) {
		if (x == 0 || x == 9 || y == 0 || y == 9) return false;
		
		if (board[x-1][y-1] == (0-turn)) return searchNW(x-1, y-1);
		else if (board[x-1][y-1] == turn) return true;
		else return false;
	}
	
	public boolean searchN(int x, int y) {
		if (x == 0 || x == 9 || y == 0 || y == 9) return false;
		
		if (board[x-1][y] == (0-turn)) return searchNW(x-1, y);
		else if (board[x-1][y] == turn) return true;
		else return false;
	}
	
	public static void main(String[] args) {
		Reversi r = new Reversi();
		r.board[3][3] = r.STATEWHITE;
		System.out.println(r.searchNW(6, 6));
		
		while (gameOn()) {
			//Do stuff..
			
			turn -= turn;
		}
	}
}

