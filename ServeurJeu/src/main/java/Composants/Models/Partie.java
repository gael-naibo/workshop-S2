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

	public Joueur getJoueurById(String idJoueur){
		if(this.joueur1.getIdJoueur().equals(idJoueur)){
			return this.joueur1;
		}
		return this.joueur2;
	}
	
	public boolean addPion(int numJoueur, int x, int y, int tour) {
		
		if(x < 0 || x > 18 || y < 0 || y > 18){
			return false;
		}
		
		
		// Si c'est 1er tour, le joueur doit placer son pion au milieu du plateau
		if(tour == 1){
			if(x != 9 && y != 9){
				return false;
			}
		}
		else if(tour == 3) { // Si c'est au 2eme tour du 1er joueur, il doit placer son pion à moins de 3 intersections du milieu
			if(x < 7 || x > 11 || y < 7 || y > 11){
				return false;
			}
		}
		
		// Sinon on place le pion tel quel
		this.plateau.getCases()[x][y] = numJoueur;
		return true;
	}
	
	public int getNombreTenaille(int numJoueur, int dernier_coup_joue_x, int dernier_coup_joue_y){
		int numAutreJoueur = (numJoueur == 1) ? 2 : 1;
		int nombreTenaille = 0;
		try{
			nombreTenaille += getNombreTenailleHorizontale(numJoueur, numAutreJoueur, dernier_coup_joue_x, dernier_coup_joue_y);
			nombreTenaille += getNombreTenailleVerticale(numJoueur, numAutreJoueur, dernier_coup_joue_x, dernier_coup_joue_y);
			nombreTenaille += getNombreTenaillesDiagonale(numJoueur, numAutreJoueur, dernier_coup_joue_x, dernier_coup_joue_y);
			
		}
		catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		return nombreTenaille;
	}
	
	private int getNombreTenailleHorizontale(int numJoueur, int numAutreJoueur, int x, int y){
		int[][] _cases = this.plateau.getCases();
		int nombreTenailles = 0;
		if(x < 3){
			if(_cases[x+1][y] == numAutreJoueur && _cases[x+2][y] == numAutreJoueur && _cases[x+3][y] == numJoueur){
				_cases[x+1][y] = 0;
				_cases[x+2][y] = 0;
				nombreTenailles += 1;
			}
		}
		else if(x > 15){
			if(_cases[x-1][y] == numAutreJoueur && _cases[x-2][y] == numAutreJoueur && _cases[x-3][y] == numJoueur){
				_cases[x-1][y] = 0;
				_cases[x-2][y] = 0;
				nombreTenailles +=1;
			}
		}else{
			if(_cases[x+1][y] == numAutreJoueur && _cases[x+2][y] == numAutreJoueur && _cases[x+3][y] == numJoueur){
				_cases[x+1][y] = 0;
				_cases[x+2][y] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x-1][y] == numAutreJoueur && _cases[x-2][y] == numAutreJoueur && _cases[x-3][y] == numJoueur){
				_cases[x-1][y] = 0;
				_cases[x-2][y] = 0;
				nombreTenailles +=1;
			}
		}
		
		return nombreTenailles;
	}
	

	private int getNombreTenailleVerticale(int numJoueur, int numAutreJoueur, int x, int y){
		int[][] _cases = this.plateau.getCases();
		int nombreTenailles = 0;
		if(y < 3){
			if(_cases[x][y+1] == numAutreJoueur && _cases[x][y+2] == numAutreJoueur && _cases[x][y+3] == numJoueur){
				_cases[x][y+1] = 0;
				_cases[x][y+2] = 0;
				nombreTenailles += 1;
			}
		}
		else if(y > 15){
			if(_cases[x][y-1] == numAutreJoueur && _cases[x][y-2] == numAutreJoueur && _cases[x][y-3] == numJoueur){
				_cases[x][y-1] = 0;
				_cases[x][y-2] = 0;
				nombreTenailles +=1;
			}
		}else{
			if(_cases[x][y+1] == numAutreJoueur && _cases[x][y+2] == numAutreJoueur && _cases[x][y+3] == numJoueur){
				_cases[x][y+1] = 0;
				_cases[x][y+2] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x][y-1] == numAutreJoueur && _cases[x][y-2] == numAutreJoueur && _cases[x][y-3] == numJoueur){
				_cases[x][y-1] = 0;
				_cases[x][y-2] = 0;
				nombreTenailles +=1;
			}
		}
		
		return nombreTenailles;
	} 
	
	private int getNombreTenaillesDiagonale(int numJoueur, int numAutreJoueur, int x, int y){
		int nombreTenailles = 0;
		int[][] _cases = this.plateau.getCases();
		
		if(x < 3 && y < 3){ // pion en haut à gauche
			if(_cases[x+1][y+1] == numAutreJoueur && _cases[x+2][y+2] == numAutreJoueur && _cases[x+3][y+3] == numJoueur){
				_cases[x+1][y+1] = 0;
				_cases[x+2][y+2] = 0;
				nombreTenailles += 1;
			}
		}else if(x < 3 && y > 15){ // pion en haut à droite
			if(_cases[x+1][y-1] == numAutreJoueur && _cases[x+2][y-2] == numAutreJoueur && _cases[x+3][y-3] == numJoueur){
				_cases[x+1][y-1] = 0;
				_cases[x+2][y-2] = 0;
				nombreTenailles += 1;
			}
		}
		else if(x > 15 && y < 3){ // pion en bas à gauche
			if(_cases[x-1][y+1] == numAutreJoueur && _cases[x-2][y+2] == numAutreJoueur && _cases[x-3][y+3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
			
		}
		else if(x > 15 && y > 15){ // pion en bas à droite
			if(_cases[x-1][y-1] == numAutreJoueur && _cases[x-2][y-2] == numAutreJoueur && _cases[x-3][y-3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
		}
		else if(x < 3) {
			if(_cases[x+1][y-1] == numAutreJoueur && _cases[x+2][y-2] == numAutreJoueur && _cases[x+3][y-3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x+1][y+1] == numAutreJoueur && _cases[x+2][y+2] == numAutreJoueur && _cases[x+3][y+3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
		}
		else if(x > 15){
			if(_cases[x-1][y+1] == numAutreJoueur && _cases[x-2][y+2] == numAutreJoueur && _cases[x-3][y+3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x-1][y-1] == numAutreJoueur && _cases[x-2][y-2] == numAutreJoueur && _cases[x-3][y-3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
		}
		else if(y < 3){
			if(_cases[x-1][y+1] == numAutreJoueur && _cases[x-2][y+2] == numAutreJoueur && _cases[x-3][y+3] == numJoueur){
				_cases[x-1][y+1] = 0;
				_cases[x-2][y+2] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x+1][y+1] == numAutreJoueur && _cases[x+2][y+2] == numAutreJoueur && _cases[x+3][y+3] == numJoueur){
				_cases[x-1][y+1] = 0;
				_cases[x-2][y+2] = 0;
				nombreTenailles += 1;
			}
		}
		else if(y > 15){
			if(_cases[x+1][y-1] == numAutreJoueur && _cases[x+2][y-2] == numAutreJoueur && _cases[x+3][y-3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x-1][y-1] == numAutreJoueur && _cases[x-2][y-2] == numAutreJoueur && _cases[x-3][y-3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
		}
		else{ // pion n'importe où ailleurs sur la plateau

			if(_cases[x+1][y+1] == numAutreJoueur && _cases[x+2][y+2] == numAutreJoueur && _cases[x+3][y+3] == numJoueur){
				_cases[x+1][y+1] = 0;
				_cases[x+2][y+2] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x+1][y-1] == numAutreJoueur && _cases[x+2][y-2] == numAutreJoueur && _cases[x+3][y-3] == numJoueur){
				_cases[x+1][y-1] = 0;
				_cases[x+2][y-2] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x-1][y+1] == numAutreJoueur && _cases[x-2][y+2] == numAutreJoueur && _cases[x-3][y+3] == numJoueur){
				_cases[x-1][y+1] = 0;
				_cases[x-2][y+2] = 0;
				nombreTenailles += 1;
			}
			if(_cases[x-1][y-1] == numAutreJoueur && _cases[x-2][y-2] == numAutreJoueur && _cases[x-3][y-3] == numJoueur){
				_cases[x-1][y-1] = 0;
				_cases[x-2][y-2] = 0;
				nombreTenailles += 1;
			}
	}
		
		return nombreTenailles;
	}
}
