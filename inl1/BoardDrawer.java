public class BoardDrawer {

	public String draw (int[][] matrix) {
		StringBuilder s = new StringBuilder();

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
			return "X";
			case 1:
			return "O";
			default:
			return " ";
		}
	}

	/*public static void main (String[]args) {
		BoardDrawer d = new BoardDrawer();
		int[][] matrix = new int[10][10];
		matrix[4][4] = 1;
		matrix[4][6] = 4;
		matrix[4][5] = -1;
		System.out.println(d.draw(matrix));
	}*/
}