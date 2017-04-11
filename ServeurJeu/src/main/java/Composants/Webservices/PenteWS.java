package Composants.Webservices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import Composants.Models.Joueur;
import Composants.Models.Partie;
import Composants.Models.Plateau;

@RestController
public class PenteWS {

	Partie currentGame;
	public static final int JOUEUR1_DEFAULT_NUM = 0;
	public static final int JOUEUR2_DEFAULT_NUM = 1;
	
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
	public HttpStatus play(@PathVariable int x, @PathVariable int y, @PathVariable int idJoueur) {

		if(currentGame.addPion(currentGame.getJoueurById(idJoueur).getNumJoueur() + 1, x, y)){
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_ACCEPTABLE;
	}

	/**
	 * Permet de connaître l'état actuel du jeu. Il faut appeler cette methode
	 * toute les 0.5 seconde pour rafraîchir l'IHM.
	 * 
	 * @param idJoueur
	 */
	@RequestMapping(value = "/turn/{idJoueur}", method = RequestMethod.GET)
	public void turn(@PathVariable int idJoueur) {

	}
}
