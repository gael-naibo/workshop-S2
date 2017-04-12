package Composants.Models;

public class GameInfo {

	private int status;
	private int[][] tableau;
	private int nbTenaillesJ1;
	private int nbTenaillesJ2;
	private int dernierCoupX;
	private int dernierCoupY;
	private boolean prolongation;
	private boolean finPartie;
	private String detailFinPartie;
	private int numTour;
	
	public GameInfo(int status, int[][] tableau, int nbTenaillesJ1,
			int nbTenaillesJ2, int dernierCoupX, int dernierCoupY,
			boolean prolongation, boolean finPartie, String detailFinPartie,
			int numTour) {
		super();
		this.status = status;
		this.tableau = tableau;
		this.nbTenaillesJ1 = nbTenaillesJ1;
		this.nbTenaillesJ2 = nbTenaillesJ2;
		this.dernierCoupX = dernierCoupX;
		this.dernierCoupY = dernierCoupY;
		this.prolongation = prolongation;
		this.finPartie = finPartie;
		this.detailFinPartie = detailFinPartie;
		this.numTour = numTour;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int[][] getTableau() {
		return tableau;
	}
	public void setTableau(int[][] tableau) {
		this.tableau = tableau;
	}
	public int getNbTenaillesJ1() {
		return nbTenaillesJ1;
	}
	public void setNbTenaillesJ1(int nbTenaillesJ1) {
		this.nbTenaillesJ1 = nbTenaillesJ1;
	}
	public int getNbTenaillesJ2() {
		return nbTenaillesJ2;
	}
	public void setNbTenaillesJ2(int nbTenaillesJ2) {
		this.nbTenaillesJ2 = nbTenaillesJ2;
	}
	public int getDernierCoupX() {
		return dernierCoupX;
	}
	public void setDernierCoupX(int dernierCoupX) {
		this.dernierCoupX = dernierCoupX;
	}
	public int getDernierCoupY() {
		return dernierCoupY;
	}
	public void setDernierCoupY(int dernierCoupY) {
		this.dernierCoupY = dernierCoupY;
	}
	public boolean isProlongation() {
		return prolongation;
	}
	public void setProlongation(boolean prolongation) {
		this.prolongation = prolongation;
	}
	public boolean isFinPartie() {
		return finPartie;
	}
	public void setFinPartie(boolean finPartie) {
		this.finPartie = finPartie;
	}
	public String getDetailFinPartie() {
		return detailFinPartie;
	}
	public void setDetailFinPartie(String detailFinPartie) {
		this.detailFinPartie = detailFinPartie;
	}
	public int getNumTour() {
		return numTour;
	}
	public void setNumTour(int numTour) {
		this.numTour = numTour;
	}
	
	
	
}
