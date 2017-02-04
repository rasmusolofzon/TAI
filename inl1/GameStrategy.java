

//remake into an abstract class that includes common elements of subclasses
//using strategy pattern allows for runtime decision on ai methodology

public interface GameStrategy {

	String nextMove (Board board);

}
