package Composants.Models;

public class Plateau {

	private int[][] cases;

	
	
	public Plateau() {
		cases = new int [19][19];
	}

	public int[][] getCases() {
		return cases;
	}

	public void setCases(int[][] cases) {
		this.cases = cases;
	}
	
	
}
