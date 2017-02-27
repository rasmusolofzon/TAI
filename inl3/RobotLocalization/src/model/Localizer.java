package model;

public class Localizer implements EstimatorInterface {



	/*
	 * return the number of assumed rows, columns and possible headings for the grid
	 * a number of four headings makes obviously most sense... the viewer can handle 
	 * four headings at maximum in a useful way.
	 */
	public int getNumRows();
	public int getNumCols();
	public int getNumHead();
	
	/*
	 * should trigger one step of the estimation, i.e., true position, sensor reading and 
	 * the probability distribution for the position estimate should be updated one step
	 * after the method has been called once.
	 */
	public void update();
	
	/*
	 * returns the currently known true position i.e., after one simulation step
	 * of the robot as (x,y)-pair.
	 */
	public int[] getCurrentTruePosition();
	
	/*
	 * returns the currently available sensor reading obtained for the true position 
	 * after the simulation step 
	 */
	public int[] getCurrentReading();
	
	/*
	 * returns the currently estimated (summed) probability for the robot to be in position
	 * (x,y) in the grid. The different headings are not considered, as it makes the 
	 * view somewhat unclear.
	 */
	public double getCurrentProb( int x, int y);

	/*
	 * returns the probability entry of the sensor matrices O to get reading r corresponding 
	 * to position (rX, rY) when actually in position (x, y) (note that you have to take 
	 * care of potentially necessary transformations from states i = <x, y, h> to 
	 * positions (x, y)). If rX or rY (or both) are -1, the method should return the probability for 
	 * the sensor to return "nothing" given the robot is in position (x, y).
	 */
	public double getOrXY( int rX, int rY, int x, int y);

	/*
	 * returns the probability entry (Tij) of the transition matrix T to go from pose 
	 * i = (x, y, h) to pose j = (nX, nY, nH)
	 */	
	public double getTProb( int x, int y, int h, int nX, int nY, int nH);


}