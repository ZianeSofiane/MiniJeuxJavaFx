package application;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Pendu extends Controle {
	String secretWord; 	// Mot secret
	String word;		// Mot afficher
	int nbvies; 		// déclaration d'une variable pour le nombre de vie restante
	ImageView advanc;	// Image d'avancement du jeu
	String essaieC =  new String(); 						// Chaine avec les caractères jouées
	ArrayList<String> essaieS = new ArrayList<String>(); 	// Chaine avec les mots jouées
	
	
	public Pendu(Stage s)  {
		Pane p = new Pane();
		Scene scene = new Scene(p);
		try {
			secretWord = secretWord();	// Initialisation du mot secret
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Bouton de règles //
		Button rules = creerBouton("Règles","rules",1250,680);
		rules(rules,"rulesPendu");
		p.getChildren().add(rules);
		
		word = word();	// Initialisation du mot vide
		nbvies=7;					// Initialisation du nombres de vies
		
		advanc= creerImage("/project/image/Etape1.png",600,350,60,180);		// Première image du jeu
		
		newPage(s,p,scene,"Pendu");											// Nouvelle page
		Label cache = creerLabel(p,word,"lettres",60,40);					// Label du mot vide
		Label info = creerLabel(p,"Bienvenue au pendu !","info",750,100);	// Label donnant des informations lors de la partie
		TextField zoneTxt = new TextField();								// Création zone de saisie utilisateur
		zoneTxt = saisie(p,zoneTxt,"saisie",600,50,60,600);
		zoneTxt.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		Button ok = creerBouton("OK","case",680,595);						// bouton ok de la zone de saisie
		ok = addAction(p,zoneTxt,cache,ok,info);							// ajout de l'effet du bouton ok
		
		addReturn(p,s);		// retour au choix du jeu
		
		p.getChildren().addAll(ok,advanc);	// ajout des éléments du pane
	}
	
	
	String secretWord() throws FileNotFoundException {		// retourne un mots d'une taille aléatoire compris entre 5 et 15 lettres
		int i = (int)(5+(Math.random()*10));
		return xLettres(i);
	}
	
	
	String word() {					// Initialisation du mot vide
		String word = new String();
		for(int i=0; i<secretWord.length(); i++) {
			word = word + '_';
		}
		
		return word;
	}
	
	
	void setAdvanc(Pane p) {	// Avancement du jeu selon le nombre de vies restants, se déclenche lors d'une erreur
		advanc = null;
		nbvies--;	// décrémentation du nombre de vies
		// Changement de l'image d'avancement //
		switch(nbvies) {
		case 6:
			advanc = creerImage("/project/image/Etape2.png",600,350,60,180);
			break;
		case 5:
			advanc = creerImage("/project/image/Etape3.png",600,350,60,180);
			break;
		case 4:
			advanc = creerImage("/project/image/Etape4.png",600,350,60,180); 
			break;
		case 3:
			advanc = creerImage("/project/image/Etape5.png",600,350,60,180);
			break;
		case 2:
			advanc = creerImage("/project/image/Etape6.png",600,350,60,180); 
			break;
		case 1:
			advanc = creerImage("/project/image/Etape7.png",600,350,60,180);
			break;
		case 0:
			advanc = creerImage("/project/image/Etape8.png",600,350,60,180);
			break;
		}
		p.getChildren().addAll(advanc);	// ajout de la nouvelle image au Pane
	}
	
	
	// Fonction renvoyant TRUE si un caractère est dans un mot
	boolean isInString(String s,char c) {
		char[] m;
		m = s.toCharArray();
		for(int i=0;i<s.length();i++) {
	    	if(c == m[i]) {
	    		return true;	
	    	}
		}
		return false;
	}
	
	// Fonction renvoyant TRUE si un mot ou un caractère a déjà été jouer
	boolean alreadyTry(String s) {
		if(s.length()==1) {				// si s est un caractère
	    	if(essaieC.contains(s)) {	// on regarde si il est dans la chaine de caractères joués
	    		return true;	
	    	}
	    	return false;	
	    }
		for(int i=0;i<essaieS.size();i++) {	// si s est un mot
			if(s.equals(essaieS.get(i))) {	// on regarde si il est présent dans la liste de mots joués
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * Fonction se déclenchant quand on joue un caractère
	 * Entrées : Pane, zone de saisie utilisateur,
	 * 			 Deux tableaux de caractères: c : texte de la zone de saisie utilisateur
	 * 										  n : texte du mot Afficher à l'écran
	 * 			 Label du mot cache, Label des infos
	*/
	void playChar(Pane p,TextField zoneTxt,char[] c,char[] n,Label cache,Label info) {
		if(alreadyTry(zoneTxt.getText().toUpperCase())){		// Si le caractère a déjà été jouer
			info.setText("Vous avez déjà jouer ce caractère");	// Texte informatif
		}
		else{
			essaieC = essaieC + c[0];							// ajout à la chaine des essaies
			if(!(isInString(secretWord,c[0]))) {				// Si mauvais caractère
				setAdvanc(p);									// Avancement du jeu
				points = points - 5;							// Perte de points
			}
			else {
				points = points + 10;							// Gain de point
				for(int i=0;i<secretWord.length();i++) {		
		    		if(c[0] == secretWord.charAt(i)) {			// Comparaison caractère saisie au caractère i de secretWord
		    			n[i] = secretWord.charAt(i);			// Evolution du texte du mot à l'écran
		    		}
		    	}
				// Evolution du texte du mot à l'écran
				String nouveau = new String();
		    	for(int i=0;i<secretWord.length();i++) {
		    		nouveau = nouveau + n[i];
		    	}
		    	word = nouveau;
		    	cache.setText(nouveau);	
		    	if(word.equals(secretWord)) {					// Quand le mot obtenu est le mot Secret, le jeu se termine
					info.setText("Vous avez gagner !");			
					points = points + secretWord.length()^2;	// Gain de points
					setPts("pendu");							// Mise à jour des points dans la bdd
				}
			}
		}
	}
	
	
	// fonction retournant le nombre de vide dans le mot afficher du jeu
	int countVoid(String word) {
		int res=0;
		for(int i=0; i<word.length() ; i++) {
			if(word.charAt(i)!='_') {
				res++;
			}
		}
		return res;
	}
	
	
	/*
	 * Fonction se déclenchant quand on joue un mot
	 * Entrées : Pane, zone de saisie utilisateur,
	 * 			 Label du mot cache, Label des infos
	*/
	void playString(Pane p,TextField zoneTxt,Label cache,Label info) {
		if(alreadyTry(zoneTxt.getText().toUpperCase())){	// Si mot déjà jouer
			info.setText("Vous avez déjà jouer ce mot");	// Texte informatif
		}
		else if(!alreadyTry(zoneTxt.getText())){
			essaieS.add(zoneTxt.getText().toUpperCase());				// Ajout du mot saisie à la liste des essaies
			if(zoneTxt.getText().toUpperCase().equals(secretWord)) {	// Si le mot saisie est égale au mot secret
				points = points + ((countVoid(word))^2 *10);			// Gain de points
				points = points + secretWord.length()^2;
				word = secretWord;
				cache.setText(secretWord);
				info.setText("Vous avez gagner !");
				setPts("pendu");										// mise à jour des points du joueur dans la bdd
			}
			else {
				setAdvanc(p);											// Avancement du jeu
				points = points - 5;									// Perte de points
			}
		}
	}
	
	
	// Bouton d'action du bouton ok lié à la zone de saisie utilisateur //
	Button addAction(Pane p,TextField zoneTxt,Label cache,Button ok,Label info) {
			ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent e) {
			    	if(!secretWord.equals(word) && nbvies!=0) {
			    		char[] n;
						n = cache.getText().toCharArray();
				    	char[] c;
						c = (zoneTxt.getText().toUpperCase()).toCharArray();
						if(zoneTxt.getText().length()==1) {
							playChar(p,zoneTxt,c,n,cache,info);			// Traitement de jeu d'un caractère
						}
						else if(zoneTxt.getText().length()>1) {
							playString(p,zoneTxt,cache,info);			// Traitement de jeu d'un mot
						}
			    	}
			    	if(nbvies==0){						// Affichage du mot cherché quand on a perdu
			    		word = secretWord;
			    		cache.setText(secretWord);
			    	}
			    }
		});
		return ok;
	}
	
}
