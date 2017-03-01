import Jama.*;
public class Main {
	/*
	 * build your own if you like, this is just an example of how to start the viewer
	 * ...
	 */
	

	//run with following command:
	//java -classpath .;Jama-1.0.3.jar Main
	public static void main( String[] args) {
		
		/*
		 * generate your own localiser / estimator wrapper here to plug it into the 
		 * graphics class.
		 */
		EstimatorInterface l = new Localizer(4, 4, 4);

		RobotLocalizationViewer viewer = new RobotLocalizationViewer(l);
		//test for jama, clear later
		//Matrix m = new Matrix(4,4,2);
		//m.set(1,1,5);
		//m.set(0,0,1);
		//m.print(2,2);

		/*
		 * this thread controls the continuous update. If it is not started, 
		 * you can only click through your localisation stepwise
		 */
		new LocalizationDriver(500, viewer).start();
	}
}	