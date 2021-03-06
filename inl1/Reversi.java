import java.util.ArrayList;
/**
  * Mapping of general elements of a search problem/game
  * ----------------------------------------------------
  *
  * S0: 				board[][] after constructor
  * PLAYER(s): 			the variable turn
  * ACTION(s): 			one field in the matrix sMatrix[][]
  * RESULT(s, a): 		change in numbers of bricks of own colour?
  * CUTOFF-TEST(S):		the method legalMoves() returns 0 => method gameOn() returns false 					[TERMINAL-TEST(s)]
  * EVAL(s, p):			the method eval(int matrix[][]). evaluates desirability of given board state. 		[UTILITY(s, p)]
  *
  */

public class Reversi {
    public static int[][] board = new int[10][10]; //remove static later on, just for debugging under time pressure /raz
    public static final int STATEEMPTY = 0;
    public static final int STATEWHITE = -1;
    public static final int STATEBLACK = 1;
    public int turn;
    public int[][] legalMovesMatrix = new int[10][10]; 
  	public static final int CUTOFFDEPTH = 3;
  	public int currentNbrBricks = 0;
    
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
        if (findLegalMoves(board).size() != 0) cont = true;
      
      	currentNbrBricks = 64 - counter;
        
        return cont;
    }
    
    public ArrayList<String> findLegalMoves(int[][] currentBoard) {
        ArrayList<String> legMoves = new ArrayList<String>();
        
        for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
                if (currentBoard[i][j] == STATEEMPTY) {
                    boolean[][] matrix = checkNeighbours(i, j);
                    boolean legMove = false;
                    
                    if (matrix[0][0]) if(directionSearch(i-1, j-1, "NW")) legMove = true;
                    if (matrix[1][0]) if(directionSearch(i, j-1, "W")) legMove = true;
                    if (matrix[2][0]) if(directionSearch(i+1, j-1, "SW")) legMove = true;
                    
                    if (matrix[0][1]) if(directionSearch(i-1, j, "N")) legMove = true;
                    if (matrix[2][1]) if(directionSearch(i+1, j, "S")) legMove = true;
                    
                    if (matrix[0][2]) if(directionSearch(i-1, j+1, "NE")) legMove = true;
                    if (matrix[1][2]) if(directionSearch(i, j+1, "E")) legMove = true;
                    if (matrix[2][2]) if(directionSearch(i+1, j+1, "SE")) legMove = true;
                    
                    if (legMove) legMoves.add(Integer.toString(i) + Integer.toString(j));
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
    
    public String miniMaxDecision(int[][] currentBoard) {
		ArrayList<String> legalMoves = findLegalMoves(currentBoard);
		int x = Character.getNumericValue(legalMoves.get(0).charAt(0));
		int y = Character.getNumericValue(legalMoves.get(0).charAt(1));
		String move = legalMoves.get(0);
		int value = minValue(currentBoard, x, y); //<-- need to init x & y before this?
      
		for (int i=1; i<legalMoves.size(); i++) {
			/*int*/ x = Character.getNumericValue(legalMoves.get(i).charAt(0));
			/*int*/ y = Character.getNumericValue(legalMoves.get(i).charAt(1));
			int v = minValue(currentBoard, x, y);
			if (value < v) {
				value =  v;
				move = legalMoves.get(i);
			}
		}
      
		return move;
    }
	
    public int maxValue(int[][] currentBoard, int x, int y) {
		currentBoard[x][y] = turn;
		if (cutoffTest()) return eval(currentBoard);
		ArrayList<String> legalMoves = findLegalMoves(currentBoard);
		int value = Integer.MIN_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			x = Character.getNumericValue(legalMoves.get(i).charAt(0));
			y = Character.getNumericValue(legalMoves.get(i).charAt(1));
			//currentBoard[x][y] = turn;
			value = Math.max(value, minValue(currentBoard, x, y));
		}
		return value;
	}
    
    public int minValue(int[][] currentBoard, int x, int y) {
		currentBoard[x][y] = 0-turn;
		if (cutoffTest()) return eval(currentBoard);
		ArrayList<String> legalMoves = findLegalMoves(currentBoard);
		int value = Integer.MAX_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			x = Character.getNumericValue(legalMoves.get(i).charAt(0));
			y = Character.getNumericValue(legalMoves.get(i).charAt(1));
			//currentBoard[x][y] = 0-turn;
			value = Math.min(value,maxValue(currentBoard, x, y));
		}
		return value;
    }
	
    public String ab_search(int[][] currentBoard) {
		ArrayList<String> legalMoves = findLegalMoves(currentBoard);
		int x = Character.getNumericValue(legalMoves.get(0).charAt(0));
		int y = Character.getNumericValue(legalMoves.get(0).charAt(1));
		String move = legalMoves.get(0);
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
      	int value = ab_minValue(currentBoard, x, y, alpha, beta);
      
      	for (int i=0; i<legalMoves.size(); i++) {
        	/*int*/ x = Character.getNumericValue(legalMoves.get(i).charAt(0));
        	/*int*/ y = Character.getNumericValue(legalMoves.get(i).charAt(1));
        	int v = ab_minValue(currentBoard, x, y, alpha, beta);
        	if (value < v) {
         	   value =  v;
			   move = legalMoves.get(i);
		   	}
      	}
      
      	return move;
    }
	
    public int ab_maxValue(int[][] currentBoard, int x, int y, int alpha, int beta) {
		currentBoard[x][y] = turn;
		if (cutoffTest()) return eval(currentBoard);
		ArrayList<String> legalMoves = findLegalMoves(currentBoard);
		int value = Integer.MIN_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			x = Character.getNumericValue(legalMoves.get(i).charAt(0));
			y = Character.getNumericValue(legalMoves.get(i).charAt(1));
			//currentBoard[x][y] = turn;
			value = Math.max(value, ab_minValue(currentBoard, x, y, alpha, beta));
			if (value >= beta) return value;
			alpha = Math.max(alpha, value);
		}
		return value;
	}
    
    public int ab_minValue(int[][] currentBoard, int x, int y, int alpha, int beta) {
		currentBoard[x][y] = 0-turn;
		if (cutoffTest()) return eval(currentBoard);
		ArrayList<String> legalMoves = findLegalMoves(currentBoard);
		int value = Integer.MAX_VALUE;
		for (int i=0; i<legalMoves.size(); i++) {
			x = Character.getNumericValue(legalMoves.get(i).charAt(0));
			y = Character.getNumericValue(legalMoves.get(i).charAt(1));
			//currentBoard[x][y] = 0-turn;
			value = Math.min(value, ab_maxValue(currentBoard, x, y, alpha, beta));
			if (value <= alpha) return value;
			beta = Math.min(beta, value);
		}
		return value;
    }
  
  	public int eval(int[][] matrix) {
      	int value = 0;
        for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
            	if (matrix[i][j] == turn) value++;
            }
        }
      	return value;
  	}
  
  	public boolean cutoffTest() {
    	//check # of bricks on board, if CUTOFFVALUE more than saved state => return true
      	int bricks = 64;
        
        for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
                if (board[i][j] == STATEEMPTY) bricks--;
            }
        }
      
      	return ((bricks - currentNbrBricks) > CUTOFFDEPTH);
    }
  
    public void play (int color, int x, int y) {
    	//kolla att moven är legal ev. (ja för kommer användas av spelaren också)
      	//
    	//uppdatera boardstate (vänd alla brickor som ska vändas)
      	//
    }
      
    public static void main(String[] args) {
        Reversi r = new Reversi();
		BoardDrawer d = new BoardDrawer();
       // r.board[3][3] = r.STATEWHITE;
        //System.out.println(r.searchNW(6, 6));
      
        char playerColour = 'b';
        int timeLimit = 1000; //milliseconds
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
        System.out.println("\nHi, and welcome to Reversi. You are playing as " +
          ((playerColour == 'b') ? "black" : "white") + ", and I am playing as " +
          ((playerColour == 'w') ? "black" : "white") + ".");
        System.out.println("My move consideration time limit is " +
          timeLimit + " ms. Let's play: \n");
		
		
		//System.out.println(d.draw(board));
        
        /*while (gameOn()) {
          	
            //Do stuff..
            
            turn -= turn;
        }*/
    }
}

