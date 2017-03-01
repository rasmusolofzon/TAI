import java.util.ArrayList;
import Jama.*;
public class Localizer implements EstimatorInterface {
	private int rows, cols, head;
	private int robot[];
	private Matrix tMat;
	private Matrix oMat;
	private Matrix fVect;

	public Localizer(int rows, int cols, int head) {
		this.rows = rows;
		this.cols = cols;
		this.head = head;
		int s = rows*cols*4;
		tMat = new Matrix(s,s,1000.0/(s*s));
		oMat = new Matrix(s,s,1.0/(s*s));
		fVect = new Matrix(s,1,1.0/(s*s));

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
		walk(getLegalHeading());
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
	public int[] getCurrentReading() {
		double r = Math.random();
		int[] reading = getCurrentTruePosition();
		if (r<0.1) return reading;
		else if (0.10<r && r<=0.15 && inGrid(reading[0]-1,reading[1]+1)) return new int[]{reading[0]-1,reading[1]+1};
		else if (0.15<r && r<=0.20 && inGrid(reading[0]+1,reading[1]+1)) return new int[]{reading[0]+1,reading[1]+1};
		else if (0.20<r && r<=0.25 && inGrid(reading[0],reading[1]-1)) return new int[]{reading[0],reading[1]-1};
		else if (0.25<r && r<=0.275 && inGrid(reading[0]-2,reading[1]+2)) return new int[]{reading[0]-2,reading[1]+2};
		else if (0.275<r && r<=0.30 && inGrid(reading[0]-1,reading[1]+2)) return new int[]{reading[0]-1,reading[1]+2};
		else if (0.30<r && r<=0.325 && inGrid(reading[0],reading[1]+2)) return new int[]{reading[0],reading[1]+2};
		else if (0.325<r && r<=0.35 && inGrid(reading[0]+2,reading[1]+2)) return new int[]{reading[0]+2,reading[1]+2};
		else if (0.35<r && r<=0.375 && inGrid(reading[0]+2,reading[1])) return new int[]{reading[0]+2,reading[1]};
		else if (0.375<r && r<=0.40 && inGrid(reading[0]-1,reading[1]-2)) return new int[]{reading[0]-1,reading[1]-2};

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
		for (int i=((x*cols+y)*4); i<4; i++) {
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
	
	private void walk(int newh) {
		if (newh == 0) robot[0]--;
		else if (newh == 1) robot[1]++;
		else if (newh == 2) robot[0]++;
		else if (newh == 3) robot[1]--;
		
		robot[2] = newh;
	}

	private boolean inGrid (int x, int y) {
		if (x<0 || x>=rows || y<0 || y>=cols) return false;
		return true;
	}
	
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
	
	private boolean[] possibleMoves(int x, int y) {
		return new boolean[]{inGrid(x-1,y),inGrid(x,y+1),inGrid(x+1,y),inGrid(x,y-1)};
		//return new boolean[]{((x-1)<=0)?false:true, ((y+1)>cols)?false:true, ((x+1)>rows)?false:true, ((y-1)<=0)?false:true};
	}

	//scales matrix so elements sums to 1
	private void scale(Matrix m) {
		double sum = 0;

		for (int i=0; i<m.getRowDimension(); i++) {
			for (int j=0; j<m.getColumnDimension(); j++) {
				sum += m.get(i,j);
			}
		}
		double alpha = 1.0/sum;
		m.times(alpha);
	}

	//returns alpha scalar, scaling matrix to 1
	private double alpha(Matrix m) {
		double sum = 0;

		for (int i=0; i<m.getRowDimension(); i++) {
			for (int j=0; j<m.getColumnDimension(); j++) {
				sum += m.get(i,j);
			}
		}
		double alpha = 1.0/sum;
		return alpha;
	}
}