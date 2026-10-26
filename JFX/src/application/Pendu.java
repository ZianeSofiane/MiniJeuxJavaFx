package application;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Pendu extends Controle {
	String secretWord; 	// Mot secret
	String word;		// Mot afficher
	int nbvies; 		// declaration d'une variable pour le nombre de vie restante
	ImageView advanc;	// Image d'avancement du jeu
	String essaieC = ""; 						// Chaine avec les caract�res jou�es
	ArrayList<String> essaieS = new ArrayList<>(); 	// Chaine avec les mots jou�es
	
	
	public Pendu(Stage s)  {
		Pane p = new Pane();
		Scene scene = new Scene(p);
		try {
			secretWord = secretWord();	// Initialisation du mot secret
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.fillInStackTrace();
		} catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Bouton de regles //
		Button rules = creerBouton("Regles","rules",1250,680);
		rules(rules,"rulesPendu");
		p.getChildren().add(rules);
		
		word = word();	// Initialisation du mot vide
		nbvies=7;					// Initialisation du nombres de vies
		
		advanc= creerImage("/project/image/Etape1.png",600,350,60,180);		// Premi�re image du jeu
		
		newPage(s,p,scene,"Pendu");											// Nouvelle page
		Label cache = creerLabel(p,word,"lettres",60,40);					// Label du mot vide
		Label info = creerLabel(p,"Bienvenue au pendu !","info",750,100);	// Label donnant des informations lors de la partie
		TextField zoneTxt = new TextField();								// Cr�ation zone de saisie utilisateur
		zoneTxt = saisie(p,zoneTxt, 600, 60,600);
		zoneTxt.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		Button ok = creerBouton("OK","case",680,595);						// bouton ok de la zone de saisie
		ok = addAction(p,zoneTxt,cache,ok,info);							// ajout de l'effet du bouton ok
		
		addReturn(p,s);		// retour au choix du jeu
		
		p.getChildren().addAll(ok,advanc);	// ajout des �l�ments du pane
	}
	
	
	String secretWord() throws FileNotFoundException, URISyntaxException {		// retourne un mots d'une taille al�atoire compris entre 5 et 15 lettres
		int i = (int)(5+(Math.random()*10));
		return xLettres(i);
	}
	
	
	String word() {					// Initialisation du mot vide
        return "_".repeat(secretWord.length());
	}
	
	
	void setAdvanc(Pane p) {	// Avancement du jeu selon le nombre de vies restants, se d�clenche lors d'une erreur
		advanc = null;
		nbvies--;	// d�cr�mentation du nombre de vies
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
	
	
	// Fonction renvoyant TRUE si un caract�re est dans un mot
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
	
	// Fonction renvoyant TRUE si un mot ou un caract�re a d�j� �t� jouer
	boolean alreadyTry(String s) {
		if(s.length()==1) {				// si s est un caract�re
            // on regarde si il est dans la chaine de caract�res jou�s
            return essaieC.contains(s);
        }
        for (String essay : essaieS) {    // si s est un mot
            if (s.equals(essay)) {    // on regarde si il est pr�sent dans la liste de mots jou�s
                return true;
            }
        }
		return false;
	}
	
	
	/*
	 * Fonction se d�clenchant quand on joue un caract�re
	 * Entr�es : Pane, zone de saisie utilisateur,
	 * 			 Deux tableaux de caract�res: c : texte de la zone de saisie utilisateur
	 * 										  n : texte du mot Afficher � l'�cran
	 * 			 Label du mot cache, Label des infos
	*/
	void playChar(Pane p,TextField zoneTxt,char[] c,char[] n,Label cache,Label info) {
		if(alreadyTry(zoneTxt.getText().toUpperCase())){		// Si le caract�re a d�j� �t� jouer
			info.setText("Vous avez d�j� jouer ce caract�re");	// Texte informatif
		}
		else{
			essaieC = essaieC + c[0];							// ajout � la chaine des essaies
			if(!(isInString(secretWord,c[0]))) {				// Si mauvais caract�re
				setAdvanc(p);									// Avancement du jeu
			}
			else {
				for(int i=0;i<secretWord.length();i++) {
		    		if(c[0] == secretWord.charAt(i)) {			// Comparaison caract�re saisie au caract�re i de secretWord
		    			n[i] = secretWord.charAt(i);			// Evolution du texte du mot � l'�cran
		    		}
		    	}
				// Evolution du texte du mot � l'�cran
				StringBuilder nouveau = new StringBuilder();
		    	for(int i=0;i<secretWord.length();i++) {
		    		nouveau.append(n[i]);
		    	}
		    	word = nouveau.toString();
		    	cache.setText(nouveau.toString());
		    	if(word.equals(secretWord)) {					// Quand le mot obtenu est le mot Secret, le jeu se termine
					info.setText("Vous avez gagner !");			
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
	 * Fonction se d�clenchant quand on joue un mot
	 * Entr�es : Pane, zone de saisie utilisateur,
	 * 			 Label du mot cache, Label des infos
	*/
	void playString(Pane p,TextField zoneTxt,Label cache,Label info) {
		if(alreadyTry(zoneTxt.getText().toUpperCase())){	// Si mot d�j� jouer
			info.setText("Vous avez d�j� jouer ce mot");	// Texte informatif
		}
		else if(!alreadyTry(zoneTxt.getText())){
			essaieS.add(zoneTxt.getText().toUpperCase());				// Ajout du mot saisie � la liste des essaies
			if(zoneTxt.getText().toUpperCase().equals(secretWord)) {	// Si le mot saisie est �gale au mot secret
				word = secretWord;
				cache.setText(secretWord);
				info.setText("Vous avez gagner !");
			}
			else {
				setAdvanc(p);											// Avancement du jeu
			}
		}
	}
	
	
	// Bouton d'action du bouton ok li� � la zone de saisie utilisateur //
	Button addAction(Pane p,TextField zoneTxt,Label cache,Button ok,Label info) {
			ok.setOnMouseClicked(_ -> {
                if(!secretWord.equals(word) && nbvies!=0) {
                    char[] n;
                    n = cache.getText().toCharArray();
                    char[] c;
                    c = (zoneTxt.getText().toUpperCase()).toCharArray();
                    if(zoneTxt.getText().length()==1) {
                        playChar(p,zoneTxt,c,n,cache,info);			// Traitement de jeu d'un caract�re
                    }
                    else if(zoneTxt.getText().length()>1) {
                        playString(p,zoneTxt,cache,info);			// Traitement de jeu d'un mot
                    }
                }
                if(nbvies==0){						// Affichage du mot cherch� quand on a perdu
                    word = secretWord;
                    cache.setText(secretWord);
                }
            });
		return ok;
	}
	
}
