package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Motus extends Controle {
	Label[][] lettres = new Label[8][8];	// grille de jeu (label (affichage des mots au fil du jeu))
	String secretWord;						// Mot secret
	int j=0;								// Tour de jeu
	
	public Motus(Stage s) {
		Pane p = new Pane();
		Scene scene2 = new Scene(p);
		
		try {
			secretWord = xLettres(8);			// Initialisation d'un mot de 8 lettres
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Bouton des règles //
		Button rules = creerBouton("Règles","rules",1250,680);
		rules(rules,"rulesMotus");
		p.getChildren().add(rules);
		
		newPage(s,p,scene2,"motus");	// nouvelle page
		addReturn(p,s);					// retour au choix du jeu
		
		TextField zoneTxt = new TextField();				// zone de saisie utilisateur
		zoneTxt = saisie(p,zoneTxt,"saisie",520,50,70,650);
		zoneTxt.setFont(Font.font("Verdana", FontWeight.BOLD, 30)); // définition de la police
		Button ok = creerBouton("OK","case",620,645);		// bouton ok de la saisie
		
		// fond bleu ciel de la grille
		Label fond = creerLabel(p,"","ff",74,75);
		fond.setMaxWidth(512);
		fond.setMaxHeight(560);
		fond.setMinWidth(512);
		fond.setMinHeight(560);
		
		Label info = creerLabel(p,"Bienvenue au motus !","info",750,100); // informations de jeu
		
		ok = addAction(ok,zoneTxt,info);	// Action du bouton ok
		
		p.getChildren().addAll(ok);			// ajout au pane
		
		lettres = genererGrille(p);			// Generation de la grille de jeu avec la première lettre s'affichant
	}
	
	
	// Génération de la grille de jeu //
	public Label[][] genererGrille(Pane p){
		Label grille[][] = new Label[8][8];
		for(int i=0; i<8; i++) {
			for(int j=0;j<8;j++) {
				grille[i][j] = creerLabel(p,"  ","mot",(64*(i+1))+10,(70*(j+1))+5);		// cases vides
				grille[i][j].setMinWidth(64);
				grille[i][j].setMaxWidth(64);
			}
		}
		grille[0][0].setText(Character.toString(secretWord.charAt(0)));					// première lettre
		return grille;
	}
	
	
	// Action du Bouton OK //
	Button addAction(Button ok, TextField zoneTxt, Label info) {
		ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent e) {
		    	// Tant que la partie n'est pas finie
		    	if(!info.getText().equals("Vous avez perdu !") && !info.getText().equals("Vous avez gagner !")) {
		    		try {
		    			// Si on a encore des essaies disponibles ET le mot saisie est de la bonne longueur ET le mot saisie est dans le dico
						if(j<7 && zoneTxt.getText().length()==8 && appartientMot(zoneTxt.getText().toUpperCase())) {
							if(secretWord.equals(zoneTxt.getText().toUpperCase())) {	// Si le mot saisie est le mot chercher
								info.setText("Vous avez gagner !");						// vous avez gagner
								points = (8-j)^2 *10;									// Gain de points
								setPts("motus");										// mise à jour des points du joueur dans la base de donnée
							}
							Mot(zoneTxt,lettres);		// lancement de la fonction de traitement de la saisie
						}
						// Sinon vous perdez
						else { info.setText("Vous avez perdu !");
						for(int i=0; i< secretWord.length(); i++)		// Affichage du mot rechercher
						{
							lettres[i][j].setText(Character.toString(secretWord.charAt(i)));
							}
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
			    	j++;	// incrémentation du tour de jeu
		    	}	
		    }
		});
		return ok;
	}
	
	
	// renvoie TRUE si le caractère c est dans le mot s
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
	
	
	static String replaceCharAt(String mot,int i,int n) {
		if(n==mot.length()-2 && n+1==i) {
			return Character.toString(mot.charAt(n)) + '0';
		}
		if(n==i) {
			return '0' + replaceCharAt(mot,i,n+1);
		}
		else if(n==mot.length()-1) {
			return Character.toString(mot.charAt(n));
		}
		else {
			return Character.toString(mot.charAt(n)) + replaceCharAt(mot,i,n+1);
		}
	}
	
	
	// Fonction d'affichage du mot saisie par l'utilisateur avec les couleurs //
	// Entrées : zone de saisie utilisateur, grille de jeu
	public void Mot(TextField zoneTxt,Label[][] lettres)
	{
		String rest = secretWord;								// initilisation d'un mot = au mot secret
		ArrayList<String> couleur = new ArrayList<String>();	// liste String mémoire des couleurs
		// Traitement des lettres rouges //
			for(int i=0; i< secretWord.length(); i++)			// parcours caractère par caractère du mot secret
			{
				lettres[i][j].setText(Character.toString(zoneTxt.getText().toUpperCase().charAt(i))); // affichage du mot saisie à la ligne du tour de jeu j
				// si les caractères à la place i du mot saisie et du mot secret sont les mêmes, on l'affiche en rouge
				if (secretWord.charAt(i) == zoneTxt.getText().toUpperCase().charAt(i)){
					lettres[i][j].setStyle("-fx-background-color: DarkRed;"
							+ "-fx-text-fill: white;");
					rest = replaceCharAt(rest,i,0);		// remplacant des lettres rouge par des 0 dans le mot rest, on remplace par des 0 plutôt
														// que de les supprimer afin de préserver les bons indices des lettres restantes
					couleur.add("red");					// On enregistre le fait que la lettre  d'indice i est rouge		
				}										// le reste et la liste de couleur sera utile pour la gestion de la couleur jaune
				else {
					couleur.add("none");				// On enregistre le fait que la lettre  d'indice i n'est pas coloré
				}
				
				/*
				 *  Le mot rest et la liste de couleur va nous permettre de gérer les problèmes de doublons,
				 *  par exemple si une lettre déjà coloré en rouge est encore présente dans le mot, elle sera pas coloré en jaune
				 *  autre exemple: si le mot possède deux fois la même lettre mais présente une seule fois dans le mot secret, une seule
				 *  de ces deux lettres sera coloré.
				 */
			}
			// Traitement des lettres jaunes //
			for(int i=0; i< secretWord.length(); i++)
			{	// Si la lettre d'indice i n'a pas été colorer et est présente dans le reste des lettres, on la colore en jaune
				if(isInString(rest,zoneTxt.getText().toUpperCase().charAt(i)) && couleur.get(i).equals("none")) {
							lettres[i][j].setStyle("-fx-background-color: gold;"
								+ "-fx-text-fill: black;"
								+ "-fx-background-radius: 180;"
								+ "-fx-background-fill: #B0E0E6");						
							rest = replaceCharAt(rest,i,0);				// On remplace la lettre
					}
			}
	}

	
// Renvoie TRUE si le mot en entrée est dans le dictionnaire	
public boolean appartientMot(String unMot) throws FileNotFoundException {
		File f = new File("Dictionnaire.txt");  // url fichier
		FileReader fr = new FileReader (f);		// lecteur de fichier
		final BufferedReader br = new BufferedReader (fr);
		// BufferedReader permet de lire ligne par ligne un fichier
		// c'est utile car ici 1 mot = 1 ligne

		try 
		{
		String line = br.readLine();
		while (line!= null)
		{
			// Tant que le fichier n'est pas parcouru
			// c'est a dire tant qu'il y a des mots
			if (line.equals(unMot))		
				{
					line= null;
					return true;		// si le mot du dico est égal au mot d'entré on renvoie TRUE
				}
			line=br.readLine();			// passage à la prochaine ligne
		}
		br.close();						// fermeture des lecteurs
		fr.close();
	}
		 catch (IOException exception)
		    {
		        System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		    }
		return false;					// on renvoie FALSE si on a parcouru tout le fichier sans avoir trouver le mot saisie
}
	

}
	
	

