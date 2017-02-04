package reversiGame;

public class BoardDrawer {

	public String draw (Board board) {
		StringBuilder s = new StringBuilder();
		int[][] matrix = board.getModel();
		//OBS kräver för tillfället att encoding är satt till UTF8.
		//Kan man göra geonom att sätta miljövariabel JAVA_TOOL_OPTIONS till -Dfile.encoding=UTF8
		//dock inte optimalt, kanske kan lösa programmatiskt
		//https://stackoverflow.com/questions/361975/setting-the-default-java-character-encoding /mvh Rasmus
		
		s.append("      A   B   C   D   E   F   G   H"+"\n");
		s.append("    ┌───┬───┬───┬───┬───┬───┬───┬───┐"+"\n");
		
		for (int i=1; i<=8; i++) {
			s.append("  ");
			s.append(i);
			for (int j=1; j<=8; j++) {
				s.append(" │ ");
				s.append(marker(matrix[i][j]));
			} 
			s.append(" │ " + "\n"); //end of row
			if (i<8) {
				s.append("    ├───┼───┼───┼───┼───┼───┼───┼───┤" + "\n"); //middle row
			}
		}
		s.append("    └───┴───┴───┴───┴───┴───┴───┴───┘");
		
		return s.toString();
	}

	private String marker(int color) {
		switch (color) {
			case -1:
			return "O";
			case 1:
			return "X";
			default:
			return " ";
		}
	}
}