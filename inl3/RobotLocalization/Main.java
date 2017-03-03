import Jama.*;
public class Main {
	/*
	 * build your own if you like, this is just an example of how to start the viewer
	 * ...
	 */
	
	//compile/run with:
	//javac -classpath .;Jama-1.0.3.jar *.java
	//java -classpath .;Jama-1.0.3.jar Main
	//javac -classpath .;Jama-1.0.3.jar *.java && java -classpath .;Jama-1.0.3.jar Main //windows version
	//javac -classpath .:Jama-1.0.3.jar *.java && java -classpath .:Jama-1.0.3.jar Main //mac version

	public static void main( String[] args) {

		/*
		 * generate your own localiser / estimator wrapper here to plug it into the 
		 * graphics class.
		 */
		EstimatorInterface l;
		if (args.length == 2) {
			l = new Localizer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 4);	
		}
		else {
			l = new Localizer(4, 4, 4);
		}

		RobotLocalizationViewer viewer = new RobotLocalizationViewer(l);

		/*
		 * this thread controls the continuous update. If it is not started, 
		 * you can only click through your localisation stepwise
		 */
		new LocalizationDriver(500, viewer).start();
	}
}	