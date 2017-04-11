package Composants.Models;

public class Partie {

	private Plateau plateau;
	private Joueur joueur1;
	private Joueur joueur2;
	
	
	public Partie(Plateau plateau, Joueur joueur1, Joueur joueur2) {
		this.plateau = plateau;
		this.joueur1 = joueur1;
		this.joueur2 = joueur2;
	}
	
	public Plateau getPlateau() {
		return plateau;
	}
	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}
	public Joueur getJoueur1() {
		return joueur1;
	}
	public void setJoueur1(Joueur joueur1) {
		this.joueur1 = joueur1;
	}
	public Joueur getJoueur2() {
		return joueur2;
	}
	public void setJoueur2(Joueur joueur2) {
		this.joueur2 = joueur2;
	}

	public Joueur getJoueurById(int idJoueur){
		if(this.joueur1.getIdJoueur().equals(idJoueur)){
			return this.joueur1;
		}
		return this.joueur2;
	}
	
	public boolean addPion(int numJoueur, int x, int y) {
		this.plateau.getCases()[x][y] = numJoueur;	
		return true;
	}
	
	
}
