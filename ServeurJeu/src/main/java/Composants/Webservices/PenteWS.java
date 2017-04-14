package Composants.Webservices;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import Composants.Models.GameInfo;
import Composants.Models.Joueur;
import Composants.Models.Partie;
import Composants.Models.Plateau;

@RestController
public class PenteWS {

	Partie currentGame;
	public static final int JOUEUR1_DEFAULT_NUM = 1;
	public static final int JOUEUR2_DEFAULT_NUM = 2;
	
	public static int DERNIER_COUP_JOUE_X = 0;
	public static int DERNIER_COUP_JOUE_Y = 0;
	
	public static int JOUEUR_QUI_DOIT_JOUER = 0;
	public static int NOMBRE_TENAILLE_J_1 = 0;
	public static int NOMBRE_TENAILLE_J_2 = 0;
	
	public static int TOUR_DU_JOUEUR = 0;
	
	public static int NUM_TOUR = 1;
	
	/**
	 * Permet de se connecter au jeu de pente. Le serveur créé une partie pour
	 * le nom du groupe et retourne au joueur son identifiant et son numéro.
	 * 
	 * @param groupName
	 */
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public void ping() {
		
	}

	/**
	 * Permet de se connecter au jeu de pente. Le serveur créé une partie pour
	 * le nom du groupe et retourne au joueur son identifiant et son numéro.
	 * 
	 * @param groupName
	 */
	@RequestMapping(value = "/connect/{joueurName}", method = RequestMethod.GET)
	public ResponseEntity<Joueur> connecter(@PathVariable String joueurName) {
		Joueur joueur = null;
		if(TOUR_DU_JOUEUR == 0 ){
			TOUR_DU_JOUEUR = (int) (1 + Math.random() * (2 - 1 + 1));
		}
		if(currentGame == null){
			joueur = new Joueur(joueurName, JOUEUR1_DEFAULT_NUM);
			Plateau plateau = new Plateau();			
			currentGame = new Partie(plateau , joueur, null);
		}else if (currentGame.getJoueur2() == null) {
			joueur = new Joueur(joueurName, JOUEUR2_DEFAULT_NUM);
			currentGame.setJoueur2(joueur);
		}
		else{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(joueur);		
	}

	/**
	 * Permet de placer un pion sur une position.
	 * 
	 * @param x
	 * @param y
	 * @param idJoueur
	 */
	@RequestMapping(value = "/play/{x}/{y}/{idJoueur}", method = RequestMethod.GET)
	public HttpStatus play(@PathVariable int x, @PathVariable int y, @PathVariable String idJoueur) {
		
		// Si le joueur n'est pas autorisé -> on renvois une 401
		if(! (currentGame.getJoueur1().getIdJoueur().equals(idJoueur) || currentGame.getJoueur2().getIdJoueur().equals(idJoueur))){
			return HttpStatus.UNAUTHORIZED;
		}
		
		// Si le pion peux etre placé on renvoi une 200
		if(currentGame.addPion(currentGame.getJoueurById(idJoueur).getNumJoueur(), x, y, NUM_TOUR)){
			DERNIER_COUP_JOUE_X = x;
			DERNIER_COUP_JOUE_Y = y;
			TOUR_DU_JOUEUR = (currentGame.getJoueur1().getIdJoueur().equals(idJoueur)) ? 2 : 1;
			NUM_TOUR += 1;
			return HttpStatus.OK;
		}else{
			return HttpStatus.NOT_ACCEPTABLE;
		}
		
	}

	/**
	 * Permet de connaître l'état actuel du jeu. Il faut appeler cette methode
	 * toute les 0.5 seconde pour rafraîchir l'IHM.
	 * 
	 * @param idJoueur
	 */
	@RequestMapping(value = "/turn/{idJoueur}", method = RequestMethod.GET)
	public ResponseEntity<GameInfo> turn(@PathVariable String idJoueur) {
		int numJoueur = 0;
		//int status = (currentGame.getJoueur1().getIdJoueur().equals(idJoueur)) ? 1 : 0;
		int status = 0;
		boolean success = false;
		
		if(currentGame.getJoueur1() != null && currentGame.getJoueur1().getIdJoueur().equals(idJoueur)){
			success = true;
		}else if(currentGame.getJoueur2() != null && currentGame.getJoueur2().getIdJoueur().equals(idJoueur)){
			success = true;
		}
		
		if(success){
			
			// MAJ STATUS
			if(TOUR_DU_JOUEUR == 1 && currentGame.getJoueur1().getIdJoueur().equals(idJoueur)){
				status = 1;
			}else if(TOUR_DU_JOUEUR == 2 && currentGame.getJoueur2() != null &&  currentGame.getJoueur2().getIdJoueur().equals(idJoueur)){
				status = 1;
			}
			
			// GET NB TENAILLES
			if(currentGame.getJoueur1().getIdJoueur().equals(idJoueur)){
				numJoueur = 1;
				NOMBRE_TENAILLE_J_1 += currentGame.getNombreTenaille(numJoueur, DERNIER_COUP_JOUE_X, DERNIER_COUP_JOUE_Y);
			}else{
				numJoueur = 2;
				NOMBRE_TENAILLE_J_2 += currentGame.getNombreTenaille(numJoueur, DERNIER_COUP_JOUE_X, DERNIER_COUP_JOUE_Y);
			}
			
			if(NOMBRE_TENAILLE_J_1 == 5 || NOMBRE_TENAILLE_J_2 == 5){
				String gagnant = (NOMBRE_TENAILLE_J_1 == 5) ? "JOUEUR 1 A GAGNE" : "JOUEUR 2 A GAGNE";
				GameInfo gameInfo = new GameInfo(
						0,
						currentGame.getPlateau().getCases(), 
						NOMBRE_TENAILLE_J_1, 
						NOMBRE_TENAILLE_J_2, 
						DERNIER_COUP_JOUE_X, 
						DERNIER_COUP_JOUE_Y, 
						false, 
						true, 
						gagnant, 
						NUM_TOUR);
			}
			
			GameInfo gameInfo = new GameInfo(status, 
											currentGame.getPlateau().getCases(), 
											NOMBRE_TENAILLE_J_1, 
											NOMBRE_TENAILLE_J_2, 
											DERNIER_COUP_JOUE_X, 
											DERNIER_COUP_JOUE_Y, 
											false, 
											false, 
											null, 
											NUM_TOUR);
			return ResponseEntity.status(HttpStatus.OK).body(gameInfo);
		}else{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		
	}
	
	
	
}
