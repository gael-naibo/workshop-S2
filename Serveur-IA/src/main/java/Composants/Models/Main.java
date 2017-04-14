package Composants.Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	public final static int NEUTRE = 0;
	public final static int JOUEUR_1 = 1;
	public final static int JOUEUR_2 = 2;

	public static String URL_TO_CONNECT = "http://localhost:8080";

	public static String ID_JOUEUR;
	public static String NOM_JOUEUR;
	public static int NUM_JOUEUR;

	public static int NB_TENAILLE_J1_POSSIBLE = 0;
	public static int NB_TENAILLE_J2_POSSIBLE = 0;

	public static void main(String[] args) {
		String botName = "BOT_TEST_2";
		try {
			connect(URL_TO_CONNECT + "/connect/" + botName);

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						jouer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(e);
						e.printStackTrace();
					}

				}
			}, 0, 3000);

			jouer();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	private static void jouer() throws MalformedURLException, IOException {
		GameInfo gameinfo = getInfo(URL_TO_CONNECT + "/turn/" + ID_JOUEUR);
		//System.out.println(gameinfo.getTableau()[0][0]);
		System.out.println("status : " + gameinfo.getStatus());
		if(gameinfo.getDetailFinPartie() != null){
			System.out.println(gameinfo.getDetailFinPartie());
			System.exit(1);
		}
		if (gameinfo.getStatus() == 1) {
			System.out.println("au tour du robot :");
			if (gameinfo.getNumTour() == 1) {
				placer_coup(9, 9);
			} else {
				int profondeur = 2;
				if (gameinfo.getNumTour() == 3) {
					placer_coup(9, 11);
				} else {
					IA_ALEATOIRE(gameinfo.getTableau(), NUM_JOUEUR);
					//IA_JOUER(gameinfo.getTableau(), profondeur, NUM_JOUEUR);
				}
			}
		}
	}

	private static void IA_ALEATOIRE(int[][] tableau, int nUM_JOUEUR2) {
		List<Coordonnee> liste = new ArrayList();
		for (int i =0; i<19; i++) {
			for (int j =0; j<19; j++) {
				if(tableau[i][j] == 0){
					liste.add(new Coordonnee(i, j));
				}
			}
		}
		
		Coordonnee coord = liste.get( new Random().nextInt(liste.size()));
		placer_coup(coord.getX(), coord.getY());
		
	}

	private static GameInfo getInfo(String url) throws MalformedURLException,
			IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String jsonText = readAll(rd);
			ObjectMapper mapper = new ObjectMapper();
			GameInfo gameInfo = mapper.readValue(jsonText, GameInfo.class);
			return gameInfo;
		} finally {
			is.close();
		}

	}

	public static void connect(String url) throws IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String jsonText = readAll(rd);

			ObjectMapper mapper = new ObjectMapper();
			// mapper.readValue();
			System.out.println(jsonText);
			Joueur joueur = mapper.readValue(jsonText, Joueur.class);
			ID_JOUEUR = joueur.getIdJoueur();
			NOM_JOUEUR = joueur.getNomJoueur();
			NUM_JOUEUR = joueur.getNumJoueur();
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static void IA_JOUER(int[][] plateau, int profondeur, int numJoueur) {
		int max = -1000;
		int tmp = 0, max_X = 0, max_Y = 0;
		// int _X, _Y;

		for (int i = 0; i < 19; i++) {
			 System.out.println("calcul ligne : " + i);
			for (int j = 0; j < 19; j++) {
				 System.out.println("calcul ligne : " + i + " | collonne : " + j);
				if (plateau[i][j] == 0) {
					plateau[i][j] = numJoueur;
					tmp = Min(plateau, profondeur - 1, (numJoueur == 1) ? 2 : 1, i, j);
					if (tmp > max) {
						max = tmp;
						max_X = i;
						max_Y = j;
					}
					plateau[i][j] = 0;
				}
			}
		}
		if(max == 0){
			System.out.println("valeur max pourri");
		}
		
		placer_coup(max_X, max_Y);

	}

	private static void placer_coup(int max_X, int max_Y) {
		System.out.println("IA PLACE PIONS A : " + max_X + " " + max_Y);
		try {
			InputStream is = new URL(URL_TO_CONNECT + "/play/" + max_X + "/"
					+ max_Y + "/" + ID_JOUEUR).openStream();
		} catch (MalformedURLException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static int Min(int[][] plateau, int profondeur, int numJoueur,
			int dernier_X, int dernier_Y) {

		if (profondeur == 0 || gagnant(plateau, dernier_X, dernier_Y) != 0) {
//		if (profondeur == 0 ) {
		return eval(plateau, numJoueur, dernier_X, dernier_Y);
		}

		int min = 10000;
		int i, j, tmp;

		for (i = 0; i < 19; i++) {
			for (j = 0; j < 19; j++) {
				if (plateau[i][j] == 0) {
					plateau[i][j] = numJoueur;
					tmp = Max(plateau, profondeur - 1, (numJoueur == 1) ? 2 : 1, i, j);

					if (tmp < min) {
						min = tmp;
					}
					plateau[i][j] = 0;
				}
			}
		}

		return min;
	}

	private static int Max(int[][] plateau, int profondeur, int numJoueur,
			int dernier_X, int dernier_Y) {
		if (profondeur == 0 || gagnant(plateau, dernier_X, dernier_Y) != 0) {
//		if (profondeur == 0) {
			return eval(plateau, numJoueur, dernier_X, dernier_Y);
		}

		int max = -10000;
		int i, j, tmp;

		for (i = 0; i < 19; i++) {
			for (j = 0; j < 19; j++) {
				if (plateau[i][j] == 0) {
					plateau[i][j] = numJoueur;
					tmp = Min(plateau, profondeur - 1, (numJoueur == 1) ? 2 : 1, i, j);
					
					if (tmp > max) {
						max = tmp;
					}
					plateau[i][j] = 0;
				}
			}
		}

		return max;

	}

	private static int gagnant(int[][] jeu, int dernier_X, int dernier_Y) {
		int i, j;
		boolean j1 = false;
		boolean j2 = false;

		// nb_series(jeu, j1, j2, 3);
		if (getNombreTenaille(jeu, 1, dernier_X, dernier_Y) > getNombreTenaille(jeu, 2, dernier_X, dernier_Y)) {
			j1 = true;
		} else{
			j2 = true;
		}
		if (j1) {
			return 1;
		} else if (j2) {
			return 2;
		} else {
			// Si le jeu n'est pas fini et que personne n'a gagné, on renvoie 0
			for (i = 0; i < 3; i++) {
				for (j = 0; j < 3; j++) {
					if (jeu[i][j] == 0) {
						return 0;
					}
				}
			}
		}

		// Si le jeu est fini et que personne n'a gagné, on renvoie 3
		return 3;
	}

	private static void nb_series(int[][] jeu, boolean j1, boolean j2, int i) {

	}

	private static int eval(int[][] plateau, int numJoueur, int dernier_X, int dernier_Y) {
			
		int valeur = 0;
		if(gagnant(plateau, dernier_X, dernier_Y) == NUM_JOUEUR){
			valeur = 100 + getNombreTenaille(plateau, numJoueur, dernier_X, dernier_Y);
		}
		if(!(gagnant(plateau, dernier_X, dernier_Y) == NUM_JOUEUR)){
			valeur = -(100 + getNombreTenaille(plateau, numJoueur, dernier_X, dernier_Y));
		}
			
		return valeur;
		
	}

	public static int getNombreTenaille(int[][] plateau, int numJoueur,
			int dernier_coup_joue_x, int dernier_coup_joue_y) {
		int numAutreJoueur = (numJoueur == 1) ? 2 : 1;
		int nombreTenaille = 0;
		try{
			nombreTenaille += getNombreTenailleHorizontale(plateau, numJoueur,
					numAutreJoueur, dernier_coup_joue_x, dernier_coup_joue_y);
			nombreTenaille += getNombreTenailleVerticale(plateau, numJoueur,
					numAutreJoueur, dernier_coup_joue_x, dernier_coup_joue_y);
			nombreTenaille += getNombreTenaillesDiagonale(plateau, numJoueur, numAutreJoueur, dernier_coup_joue_x, dernier_coup_joue_y);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Emplacement du pions choisi : " + dernier_coup_joue_x + " " + dernier_coup_joue_y);
		}
		return nombreTenaille;
	}

	private static int getNombreTenailleHorizontale(int[][] _cases,
			int numJoueur, int numAutreJoueur, int x, int y) {
		// int[][] _cases = this.plateau.getCases();
		int nombreTenailles = 0;
		if (x < 3) {
			if (_cases[x + 1][y] == numAutreJoueur
					&& _cases[x + 2][y] == numAutreJoueur
					&& _cases[x + 3][y] == numJoueur) {
				_cases[x + 1][y] = 0;
				_cases[x + 2][y] = 0;
				nombreTenailles += 1;
			}
		} else if (x > 15) {
			if (_cases[x - 1][y] == numAutreJoueur
					&& _cases[x - 2][y] == numAutreJoueur
					&& _cases[x - 3][y] == numJoueur) {
				_cases[x - 1][y] = 0;
				_cases[x - 2][y] = 0;
				nombreTenailles += 1;
			}
		} else {
			if (_cases[x + 1][y] == numAutreJoueur
					&& _cases[x + 2][y] == numAutreJoueur
					&& _cases[x + 3][y] == numJoueur) {
				_cases[x + 1][y] = 0;
				_cases[x + 2][y] = 0;
				nombreTenailles += 1;
			}
			if (_cases[x - 1][y] == numAutreJoueur
					&& _cases[x - 2][y] == numAutreJoueur
					&& _cases[x - 3][y] == numJoueur) {
				_cases[x - 1][y] = 0;
				_cases[x - 2][y] = 0;
				nombreTenailles += 1;
			}
		}

		return nombreTenailles;
	}

	private static int getNombreTenailleVerticale(int[][] _cases,
			int numJoueur, int numAutreJoueur, int x, int y) {
		// int[][] _cases = this.plateau.getCases();
		int nombreTenailles = 0;
		if (y < 3) {
			if (_cases[x][y + 1] == numAutreJoueur
					&& _cases[x][y + 2] == numAutreJoueur
					&& _cases[x][y + 3] == numJoueur) {
				_cases[x][y + 1] = 0;
				_cases[x][y + 2] = 0;
				nombreTenailles += 1;
			}
		} else if (y > 15) {
			if (_cases[x][y - 1] == numAutreJoueur
					&& _cases[x][y - 2] == numAutreJoueur
					&& _cases[x][y - 3] == numJoueur) {
				_cases[x][y - 1] = 0;
				_cases[x][y - 2] = 0;
				nombreTenailles += 1;
			}
		} else {
			if (_cases[x][y + 1] == numAutreJoueur
					&& _cases[x][y + 2] == numAutreJoueur
					&& _cases[x][y + 3] == numJoueur) {
				_cases[x][y + 1] = 0;
				_cases[x][y + 2] = 0;
				nombreTenailles += 1;
			}
			if (_cases[x][y - 1] == numAutreJoueur
					&& _cases[x][y - 2] == numAutreJoueur
					&& _cases[x][y - 3] == numJoueur) {
				_cases[x][y - 1] = 0;
				_cases[x][y - 2] = 0;
				nombreTenailles += 1;
			}
		}

		return nombreTenailles;
	}

	private static int getNombreTenaillesDiagonale(int[][] _cases,
			int numJoueur, int numAutreJoueur, int x, int y) {
		int nombreTenailles = 0;
		// int[][] _cases = this.plateau.getCases();

		if (x < 3 && y < 3) { // pion en haut à gauche
			if (_cases[x + 1][y + 1] == numAutreJoueur
					&& _cases[x + 2][y + 2] == numAutreJoueur
					&& _cases[x + 3][y + 3] == numJoueur) {
				_cases[x + 1][y + 1] = 0;
				_cases[x + 2][y + 2] = 0;
				nombreTenailles += 1;
			}
		} else if (x < 3 && y > 15) { // pion en haut à droite
			if (_cases[x + 1][y - 1] == numAutreJoueur	&& _cases[x + 2][y - 2] == numAutreJoueur && _cases[x + 3][y - 3] == numJoueur) {
				_cases[x + 1][y - 1] = 0;
				_cases[x + 2][y - 2] = 0;
				nombreTenailles += 1;
			}
		} else if (x > 15 && y < 3) { // pion en bas à gauche
			if (_cases[x - 1][y + 1] == numAutreJoueur	&& _cases[x - 2][y + 2] == numAutreJoueur&& _cases[x - 3][y + 3] == numJoueur) {
				_cases[x - 1][y + 1] = 0;
				_cases[x - 2][y + 2] = 0;
				nombreTenailles += 1;
			}
		} else if (x > 15 && y > 15) { // pion en base à droite
			if (_cases[x - 1][y - 1] == numAutreJoueur
					&& _cases[x - 2][y - 2] == numAutreJoueur
					&& _cases[x - 3][y - 3] == numJoueur) {
				_cases[x - 1][y - 1] = 0;
				_cases[x - 2][y - 2] = 0;
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
		}else { // pion n'importe où ailleurs sur la plateau
			if (_cases[x + 1][y + 1] == numAutreJoueur
					&& _cases[x + 2][y + 2] == numAutreJoueur
					&& _cases[x + 3][y + 3] == numJoueur) {
				_cases[x + 1][y + 1] = 0;
				_cases[x + 2][y + 2] = 0;
				nombreTenailles += 1;
			}
			if (_cases[x + 1][y - 1] == numAutreJoueur
					&& _cases[x + 2][y - 2] == numAutreJoueur
					&& _cases[x + 3][y - 3] == numJoueur) {
				_cases[x + 1][y - 1] = 0;
				_cases[x + 2][y - 2] = 0;
				nombreTenailles += 1;
			}
			if (_cases[x - 1][y + 1] == numAutreJoueur
					&& _cases[x - 2][y + 2] == numAutreJoueur
					&& _cases[x - 3][y + 3] == numJoueur) {
				_cases[x - 1][y + 1] = 0;
				_cases[x - 2][y + 2] = 0;
				nombreTenailles += 1;
			}
			if (_cases[x - 1][y - 1] == numAutreJoueur
					&& _cases[x - 2][y - 2] == numAutreJoueur
					&& _cases[x - 3][y - 3] == numJoueur) {
				_cases[x - 1][y - 1] = 0;
				_cases[x - 2][y - 2] = 0;
				nombreTenailles += 1;
			}
		}

		return nombreTenailles;
	}

}
