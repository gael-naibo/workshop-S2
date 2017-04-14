package Composants.Models;

public class Joueur {

	private String idJoueur;
	private String nomJoueur;
	private int numJoueur;
	
	public Joueur(){
		
	}
	
	public Joueur(String idJoueur, String nomJoueur, int numJoueur) {
		this.idJoueur = idJoueur;
		this.nomJoueur = nomJoueur;
		this.numJoueur = numJoueur;
	}
	public String getIdJoueur() {
		return idJoueur;
	}
	public void setIdJoueur(String idJoueur) {
		this.idJoueur = idJoueur;
	}
	public String getNomJoueur() {
		return nomJoueur;
	}
	public void setNomJoueur(String nomJoueur) {
		this.nomJoueur = nomJoueur;
	}
	public int getNumJoueur() {
		return numJoueur;
	}
	public void setNumJoueur(int numJoueur) {
		this.numJoueur = numJoueur;
	}
	
	
	
}
