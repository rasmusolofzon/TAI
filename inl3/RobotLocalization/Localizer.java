import java.util.ArrayList;
import Jama.*;
public class Localizer implements EstimatorInterface {
	private int rows, cols, head;
	private int robot[];
	private Matrix tMat;
	private Matrix oMat;
	private Matrix fVect;

	//probability constants calculated beforehand
	private final double S1PROB = 0.05, S2PROB = 0.025;
	private final double MIDDLE = 0.1, CORNER = 0.625, INNERCORNER = 0.325,
						NEXTTOCORNER = 0.5, EDGE = 0.425, INNEREDGE = 0.315;



	public Localizer(int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		int s = rows*cols*4;
		tMat = new Matrix(s,s,1.0/(s*s));
		oMat = new Matrix(s,s);
		fVect = new Matrix(s,1,1.0/s);

		robot = new int[]{(int) (Math.random()*rows), (int) (Math.random()*cols), (int) (Math.random()*head)};
	}

	/*
	 * return the number of assumed rows, columns and possible headings for the grid
	 * a number of four headings makes obviously most sense... the viewer can handle 
	 * four headings at maximum in a useful way.
	 */
	public int getNumRows() {
		return rows; //takes 1-n, not 0-(n-1)
	}
	
	public int getNumCols() {
		return cols; //takes 1-n, not 0-(n-1)
	}
	
	public int getNumHead() {
		return head; //takes 1-n, not 0-(n-1)
	}
	
	/*
	 * should trigger one step of the estimation, i.e., true position, sensor reading and 
	 * the probability distribution for the position estimate should be updated one step
	 * after the method has been called once.
	 */
	public void update() {
		//walk robot one step, t+1
		walk(getLegalHeading()); 
		//get sensor reading and update = matrix
		int[] sVals = getCurrentReading();
		if (sVals==null) updateO(-1,-1);
		else updateO(sVals[0],sVals[1]);
		//update forward message
		updateF(); 
	}
	
	/*
	 * returns the currently known true position i.e., after one simulation step
	 * of the robot as (x,y)-pair. 
	 */
	public int[] getCurrentTruePosition() {
		return new int[]{robot[0], robot[1]}; //Takes index 0-(n-1).
	}
	
	/*
	 * returns the currently available sensor reading obtained for the true position 
	 * after the simulation step 
	 */

	//needs testing
	 public int[] getCurrentReading() {
		
		int[] reading = getCurrentTruePosition();
		int[] innerLapX = new int[]{-1,-1,-1,0,0,1,1,1};
		int[] innerLapY = new int[]{-1,0,1,-1,1,-1,0,1};
		int[] outerLapX = new int[]{-2,-2,-2,-2,-2,-1,-1,-1,-1,-1,0,0,0,0,0,1,1,1,1,1,2,2,2,2,2};
		int[] outerLapY = new int[]{-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2};

		int sRand = (int) (Math.random()*8);
		int s2Rand = (int) (Math.random()*16);
		int dX = innerLapX[sRand];
		int dY = innerLapY[sRand];
		int dX2 = outerLapX[s2Rand];
		int dY2 = outerLapY[s2Rand];

		double r = Math.random();
		if (r<0.1 &&inGrid(reading[0],reading[1])) return reading;
		else if (0.1<=r && r<0.5 && inGrid(reading[0]+dX,reading[1]+dY)) return new int[]{reading[0]+dX,reading[1]+dY};
		else if (0.5<=r && r<0.9 && inGrid(reading[0]+dX2, reading[1]+dY2)) return new int[]{reading[0]+dX2,reading[1]+dY2};

		reading = null;
		return reading;
	}

	/*
	 * returns the currently estimated (summed) probability for the robot to be in position
	 * (x,y) in the grid. The different headings are not considered, as it makes the 
	 * view somewhat unclear.
	 */
	public double getCurrentProb( int x, int y) {
		double prob = 0.0;
		int start = x*cols+y*4;
		for (int i=start; i<start+4; i++) {
			prob += fVect.get(i,0);
		}
		return prob;
	}

	/*
	 * returns the probability entry of the sensor matrices O to get reading r corresponding 
	 * to position (rX, rY) when actually in position (x, y) (note that you have to take 
	 * care of potentially necessary transformations from states i = <x, y, h> to 
	 * positions (x, y)). If rX or rY (or both) are -1, the method should return the probability for 
	 * the sensor to return "nothing" given the robot is in position (x, y).
	 * e_t = (rX, rY), X_t = i = (x, y), P(e_t | X_t)
	 */
	public double getOrXY( int rX, int rY, int x, int y) {
		return 0;
	}

	/*
	 * returns the probability entry (Tij) of the transition matrix T to go from pose 
	 * i = (x, y, h) to pose j = (nX, nY, nH)
	 */	
	public double getTProb( int x, int y, int h, int nX, int nY, int nH) {
		int i = x*rows + y*cols + h;
		int j = nX*rows + nY*cols + nH;
		//Jama matrix uses java indexing (0,1,2....n-1)!
		return tMat.get(i,j);
	}
	
	//####################################################################
	/*
	 * returns a heading randomly chosen from the true position's possible headings
	 */
	private int getLegalHeading() {
		boolean[] possMoves = possibleMoves(robot[0], robot[1]);
		int h = robot[2]; 
		int newh = robot[2];
		
		if (!possMoves[h] || (possMoves[h] && Math.random() < 0.3)) {
			ArrayList<Integer> trueHeadings = new ArrayList<Integer>();
			for (int i=0;i<4;i++) {
				if (possMoves[i]) trueHeadings.add(i);
			}

			newh = trueHeadings.get((int) (Math.random()*trueHeadings.size()));
		}
		return newh;
	}
	
	/*
	 * returns a boolean vector with the headings it is possible to move in 
	 * (i. e. no walls in that heading). [0] = N(orth), [1]=E, [2]=S, [3]=W
	 */
	private boolean[] possibleMoves(int x, int y) {
		return new boolean[]{inGrid(x-1,y),inGrid(x,y+1),inGrid(x+1,y),inGrid(x,y-1)};
	}

	private void updateF() {
		Matrix tMatT = tMat.transpose();
		Matrix temp = tMatT.times(fVect);
		Matrix result = oMat.times(temp);
		fVect = scale(result);
	}

	private void updateO(int x, int y) {
		oMat = new Matrix(rows*cols*4, rows*cols*4);
		int[] xDelta = new int[]{-2,-2,-2,-2,-2,-1,-1,-1,-1,-1,0,0,0,0,0,1,1,1,1,1,2,2,2,2,2};
		int[] yDelta = new int[]{-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2,-2,-1,0,1,2};
		double[] prob = new double[]{S2PROB,S2PROB,S2PROB,S2PROB,S2PROB,
									S2PROB,S1PROB,S1PROB,S1PROB,S2PROB,
									S2PROB,S1PROB,0.1,S1PROB,S2PROB,
									S2PROB,S1PROB,S1PROB,S1PROB,S2PROB,
									S2PROB,S2PROB,S2PROB,S2PROB,S2PROB};
		if (x != -1 && y != -1) {
			for (int lap=0;lap<xDelta.length;lap++) {		
				if (inGrid(x+xDelta[lap], y+yDelta[lap])) {
					int start = (x+xDelta[lap])*cols+(y+yDelta[lap])*4;
					for (int i=start; i<start+4; i++) {
						oMat.set(i,i,prob[lap]/4);
					}
				}
			}
		}
		//uses method nothingProbability to determine probability
		else {
			for (int i=0; i<rows; i++) {
				for (int j=0; j<cols; j++) {
					int start = (i*cols+j)*4;
					for (int s=start; s<start+4; i++) {
						double nonProb = nothingProbability(i,j);
						oMat.set(s,s,nonProb/4);
					}
				}
			}
		}
		oMat = scale(oMat);
	}

	/*
	*this method returns probability that the robot is
	*in square (x,y), given that the sensor returns nothing.
	*/
	//tested, functions correctly
	private double nothingProbability (int x, int y) {
	double prob = 0;
	//there is a reason for not returning straight in if statement!
	if ( wallDistX(x) >= 2 && wallDistY(y) >= 2) prob = MIDDLE;
	if ( wallDistX(x) == 0 && wallDistY(y) == 0) prob = CORNER;
	if ((wallDistX(x) == 0 && wallDistY(y) == 1) || (wallDistX(x) == 1 && wallDistY(y) == 0)) prob = NEXTTOCORNER;
	if ( wallDistX(x) == 1 && wallDistY(y) == 1) prob = INNERCORNER; 
	if ((wallDistX(x) == 0 && wallDistY(y) >= 2) || (wallDistX(x) >= 2 && wallDistY(y) == 0)) prob = EDGE;
	if ((wallDistX(x) == 1 && wallDistY(y) >= 2) || (wallDistX(x) >= 2 && wallDistY(y) == 1)) prob = INNEREDGE;
	return prob;
	}

	//tested, functions correctly
	private Matrix scale (Matrix m) {
		double sum = 0;
		for (int i=0; i<m.getRowDimension(); i++) {
			for (int j=0; j<m.getColumnDimension(); j++) {
				sum += m.get(i,j);
			}
		}
		double alpha = 1.0/sum;
		return m.times(alpha);
	}

	/*
	 * walk the robot one step in direction newh
	 */
	private void walk(int newh) {
		if 		(newh == 0) robot[0]--;
		else if (newh == 1) robot[1]++;
		else if (newh == 2) robot[0]++;
		else if (newh == 3) robot[1]--;	
		robot[2] = newh;
	}

	private boolean inGrid (int x, int y) {
		if (x<0 || x>=rows || y<0 || y>=cols) return false;
		return true;
	}

	private int wallDistX (int x) {
		return Math.min(rows-1-x,x);
	}
	
	private int wallDistY (int y) {
		return Math.min(cols-1-y,y);
	}
}