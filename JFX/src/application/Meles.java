package application;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Meles extends Controle{
	Label[][] grille = new Label[9][9];						// grille de jeu
	String motCache = new String();							// mot caché dans la grille
	ArrayList<String> cherche = new ArrayList<String>();	// liste des mots à trouver dans la grille
	ArrayList<Label> lab = new ArrayList<Label>();			// Labels pour affichage des mots sur le coté
	boolean action = false;									// booleen pour gérer l'effet d'un premier puis d'un second clic
	ArrayList<int[]> indices = new ArrayList<int[]>();		// liste d'indices des cases vides restantes après avoir rempli la grille de mots
	int essai=0;											// nombre d'essai pour trouver le mot final
	
	public Meles(Stage s) {
		Pane p = new Pane();
		Scene scene = new Scene(p);
		newPage(s,p,scene,"meles");		// nouvelle page
		addReturn(p,s);					// retour au choix du jeu
		
		Label info = creerLabel(p,"Bienvenue au Mots Meles !","info",900,100);
		
		Button rules = creerBouton("Règles","rules",1250,680);	// bouton des règles
		rules(rules,"rulesMeles");
		p.getChildren().add(rules);
		
		genererGrille(p,scene,info);	// génération de la grille vide
		try {
			remplir();					// remplissage de la grille
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		afficherMots(p);				// affichage des mots à trouver sur le côté
		
	}
	
	// recherche du mot final //
	void motFinal(Pane p, Label info) {
		if(info.getText().equals("VOUS AVEZ GAGNE")) return;
		else {
		TextField zoneTxt = new TextField();
		zoneTxt = saisie(p,zoneTxt,"saisie",300,50,800,600);
		zoneTxt.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		Button ok = creerBouton("OK","case",1120,595);
		p.getChildren().add(ok);
		Label[] lettre2 = new Label[motCache.length()];
		Label[] lettre = new Label[motCache.length()];
		int n=0;
		for(int i=0; i<indices.size(); i++){
			lettre[n] = creerLabel(p,grille[indices.get(n)[0]][indices.get(n)[1]].getText(),"cacheMeles",100+60*i,200);
			lettre2[n] = creerLabel(p,grille[indices.get(n)[0]][indices.get(n)[1]].getText(),"cacheMeles",100+60*i,100);
			n++;
		}
		ok = actionOk(ok,zoneTxt,info,p,lettre);	}
	}
	
	
	Button actionOk(Button ok,TextField zoneTxt, Label info, Pane p,Label[] lettre) {
		ok.setOnMouseClicked(e-> {
			if(!info.getText().equals("VOUS AVEZ GAGNE")) {
			if(zoneTxt.getText().toUpperCase().equals(motCache)) {
				info.setText("VOUS AVEZ GAGNE");
				e=null;
				points = 81 - motCache.length() + (motCache.length() - essai)^2;
				setPts("meles");
			}
			else {
				info.setText("Mauvais mot");
				motusHelp(p,zoneTxt,lettre); essai++;
			}	}
		});
		return ok;
	}
	
	
	// Fonction renvoyant TRUE si le caractère c est dans le mot s
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
	
	
	// Fonction d'aide pour trouver le mot final
	void motusHelp(Pane p, TextField zoneTxt, Label[] lettre) {
		for(int i=0; i< motCache.length(); i++)
		{
			lettre[i].setText(Character.toString(zoneTxt.getText().toUpperCase().charAt(i)));	// affichage du mot saisie dans des Labels
		if (motCache.charAt(i) == zoneTxt.getText().toUpperCase().charAt(i)){					// si la lettre d'indice i de la saisie de l'utilisateur
			lettre[i].setStyle("-fx-background-color: DarkRed;"									// et la même que celle du mot secret
					+ "-fx-text-fill: white;");													// elle s'affiche en rouge
		}
		}
	}
	
	// Actions aux clics sur la grille //
	public Label addAction(Scene scene, Pane p,int i,int j,Label info) {
		grille[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
			//@Override
			public void handle(MouseEvent event) {
				if(!info.getText().equals("VOUS AVEZ GAGNE")) {				// Quand le jeu se termine, les clics n'ont plus d'effets
				if(action==false && cherche.size()!=0) {					// Action du premier clic
					Line l = new Line(109+70*i,109+70*j,109+70*i,109+70*j);	// création d'une ligne
					l.setStrokeWidth(26);
					l.setStroke(Color.web(newColor(cherche.size())));
					l.setOpacity(0.5);										// transparence
					p.getChildren().add(l);
					action=true;											// le prochain clic sera un second clic
				
					// la fin de la ligne l suit la position de la souris sur l'écran //
					scene.setOnMouseMoved(e1 -> {
						// Ces différentes boucles permettent de forcer les directions de la ligne, vertical, horizontal et les 2 diagonales
						if( ( Math.abs( (e1.getX() - (109+70*i)) - (e1.getY() - (109+70*j)) ) ) < 35 
								&& e1.getX() < 110 + 70*8 && e1.getY() < 110 + 70*8
									&& e1.getX() > 20 && e1.getY() > 20) {
							l.setEndX(e1.getX());
							l.setEndY(e1.getY());
						}
						else if( ( Math.abs( (Math.ceil(e1.getX()/70)*70 - (109+70*i)) - ((109+70*j) - Math.floor(e1.getY()/70)*70 )) ) < 35 
								&& e1.getX() < 110 + 70*8 && e1.getY() < 110 + 70*8
									&& e1.getX() > 20 && e1.getY() > 20) {
							l.setEndX(e1.getX());
							l.setEndY(e1.getY());
						}
					
						else if( Math.abs(e1.getY() - (109+70*j)) < 35
							&& e1.getX() < 110 + 70*8 && e1.getY() < 110 + 70*8
								&& e1.getX() > 20 && e1.getY() > 20) {
							l.setEndY(109+70*j);
							l.setEndX(e1.getX());
						}
						else if( Math.abs(e1.getX() - (109+70*i)) < 35
							&& e1.getX() < 110 + 70*8 && e1.getY() < 110 + 70*8
								&& e1.getX() > 20 && e1.getY() > 20) {
							l.setEndX(109+70*i);
							l.setEndY(e1.getY());
						}
						
						e1=null;
						scene.setOnMouseClicked(e2 -> {		// au deuxième clic, si le mot entre les deux clics fait partie de ceux recherchés
															// la ligne reste sur l'écran
							action=false;					// le prochain clic sera un premier clic
							// recupMot permet de récuperer le mot entre les deux clics de l'utilisateur //
							if(cherche.contains(recupMot(i,j,(int)( (l.getEndX() - 85) /70),(int)( (l.getEndY() - 85) /70)))) {
								Line l2 = new Line(109+70*i,109+70*j,l.getEndX(),l.getEndY());
								l2.setStrokeWidth(26);
								l2.setStroke(Color.web(newColor(cherche.size()))); // newColor contient différentes couleurs
								l2.setOpacity(0.4);
								p.getChildren().add(l2);
								delete(recupMot(i,j,(int)((l.getEndX() - 85) /70),(int)((l.getEndY() - 85) /70)));
								p.getChildren().remove(l);
							}
							else {
								p.getChildren().remove(l);	// si le mot est faux, on enlève la ligne créée
							}
							if(cherche.size()==0) {motFinal(p,info);}	// lancement de la recherche du mot final quand tous les mots de la grille sont trouvés
						});
				});	
			}
			else { event=null;} // les clics n'ont plus d'effet quand le jeu se termine
			}
			}
		});
		return grille[i][j]; 
	}

	
	// Fonction passant la couleur d'un mot sur le côté de blanc à noir quand il est trouvé
	void delete(String s) {
		for(int i=0; i<lab.size(); i++) {
			if(lab.get(i).getText().equals(s)) {
				lab.get(i).setStyle("-fx-text-fill:black;");
				cherche.remove(cherche.get(i));
				lab.remove(lab.get(i));
				return;
			}
		}
	}
	
	
	// Fonction permettant de récupérer un mot en colonne des points {i,j} à {i2,j2}
	String recupColonne(int i, int j, int i2, int j2) {
		String recup = new String();
		if(j2>j) {										// de haut en bas
			for(int n=j; n<=j2; n++) {
				recup = recup + grille[i][n].getText();
			}
		}
		else {											// de bas en haut
			for(int n=j; n>=j2; n--) {
				recup = recup + grille[i][n].getText();
			}
		}
		
		return recup;
	}
	
	// Fonction permettant de récépurer un mot en ligne des points {i,j} à {i2,j2}
	String recupLigne(int i, int j, int i2, int j2) {
		String recup = new String();
		if(i2>i) {										// de gauche à droite
			for(int n=i; n<=i2; n++) {
				recup = recup + grille[n][j].getText();
			}
		}
		else {											// de droite à gauche
			for(int n=i; n>=i2; n--) {
				recup = recup + grille[n][j].getText();
			}
		}
		return recup;
	}
	
	// permet de récupérer un mot sur la diagonale \ 
	String recupDiag1(int i, int j, int i2, int j2) {
		String recup = new String();
		if(i2>i) {											// de haut en bas
			for(int n=0; n<=i2-i; n++) {
				recup = recup + grille[i+n][n+j].getText();
			}
		}
		else {												// de bas en haut
			for(int n=0; n<=i-i2; n++) {
				recup = recup + grille[i-n][j-n].getText();
			}
		}
		return recup;
	}
	
	// permet la récupération d'un mot sur la diagonale /
	String recupDiag2(int i, int j, int i2, int j2) {
		String recup = new String();
		if(i2>i) {											// de bas en haut
			for(int n=0; n<=i2-i; n++) {
				recup = recup + grille[i+n][j-n].getText();
			}
		}
		else {												// de haut en bas
			for(int n=0; n<=i-i2; n++) {
				recup = recup + grille[i-n][j+n].getText();
			}
		}
		return recup;
	}
	
	// Fonction permettant la récupération d'un mot des points {i,j} à {i2,j2}
	String recupMot(int i, int j, int i2, int j2) {
		if(i==i2) {
			return recupColonne(i,j,i2,j2);
		}
		else if(j==j2) {
			return recupLigne(i,j,i2,j2);
		}
		else if( (i>i2 && j>j2) || (i<i2 && j<j2) ) {
			return recupDiag1(i,j,i2,j2);
		}
		else if( (i>i2 && j<j2) || (i<i2 && j>j2) ) {
			return recupDiag2(i,j,i2,j2);
		}
		return null;
	}
	
	// Supprime le caractère d'indice i d'un mot
	String deleteCharAt(String m, int i) {
		String m2 = new String();
		for(int j=0; j<m.length(); j++) {
			if(j!=i) {
				m2 = m2 + m.charAt(j);
			}	
		}
		return m2;
	}
	
	// Permet de renvoyer un mot en entrée avec les lettres mélangés
	String melangeLettresMots(String m) {
		String m2 = new String();
		while(m.length()!=0) {
			int i = (int)(Math.random()*m.length());
			m2 = m2 + m.charAt(i);
			m = deleteCharAt(m,i);
		}
		return m2;
	}
	
	// Création et intégration du mots cachés dans la grille //
	void motCache() {
		
		// récupération des indices des cases vides
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				if(grille[i][j].getText().equals("  ")) {
					int[] ind = {i,j};
					indices.add(ind);
				}
			}
		}
		try {
			motCache = xLettres(indices.size());	// selection aléatoire d'un mot du nombre de lettres égale au nombre de cases vides
		} catch (FileNotFoundException e) {			// aprés remplissage de la grille
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String motCacheBis = melangeLettresMots(motCache);	// on mélange les lettres du motCaché
		
		// placement des lettres dans la grille
		for(int i=0 ; i<indices.size() ; i++) {
			grille[indices.get(i)[0]][indices.get(i)[1]].setText(Character.toString(motCacheBis.charAt(i)));
		}
	}
	
	// génération d'un mot aléatoire de 4 à 9 lettres
	String mot_max9() throws FileNotFoundException {
		int j = 4 + (int)(Math.random()*4);
		return xLettres(j);
	}
	
	// renvoie TRUE si on peut placer un mot dans la grille en ligne à partir de la case {i,j} , sens determine le sens du mot
	boolean ligne(String s, int i, int j, int sens) {	
		if(sens==0) s = reverseWord(s);					// on retourne le mot
				if(i+s.length()-1 <= 8) {
					for(int k=i; k<i+s.length(); k++) {
						if( (!grille[k][j].getText().equals(Character.toString(s.charAt(k-i)))  // si la case n'est pas vide et la lettre ne 
								&& !grille[k][j].getText().equals("  ")) ) {					// correspond pas à celle du mot qu'on
							return false;														// essaie d'ajouter, renvoie FALSE
						}
					}
					return true;
				}
				return false;
		}
		
	// ajout du mot en ligne
	void ajoutLigne(String s, int i, int j, int sens) {	
		cherche.add(s);											// ajout du mot à la liste des mots à chercher
		if(sens==0) s = reverseWord(s);							// on retourne le mot
			for(int k=i; k<i+s.length(); k++) {
				grille[k][j].setText(Character.toString(s.charAt(k-i)));
			}
		}
	
	
	// renvoie TRUE si on peut placer un mot dans la grille en colonne à partir de la case{i,j} , sens determine le sens du mot
	boolean colonne(String s, int i, int j, int sens) {
		if(sens==0) s = reverseWord(s);					// on retourne le mot
		if(j+s.length()-1 <= 8) {
			for(int k=j; k<j+s.length(); k++) {
				if( (!grille[i][k].getText().equals(Character.toString(s.charAt(k-j))) 
						&& !grille[i][k].getText().equals("  ")) ) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	// ajout d'un mot en colonne
	void ajoutColonne(String s, int i, int j, int sens) {
		cherche.add(s);
		if(sens==0) s = reverseWord(s);
			for(int k=j; k<j+s.length(); k++) {
					grille[i][k].setText(Character.toString(s.charAt(k-j)));
			}
	}
	
	// possibilité de placement en diagonale \
	boolean diagonale(String s, int i, int j, int sens) {
		if(sens==0) s = reverseWord(s);
		if(i+s.length()-1 <= 8 && j+s.length()-1 <= 8) {
			for(int k=0; k<s.length(); k++) {
					if( (!grille[k+i][k+j].getText().equals(Character.toString(s.charAt(k))) 
						&& !grille[k+i][k+j].getText().equals("  ")) ) {
						return false;
					}
			}
			return true;
		}
		return false;
	}
			
	// placement en diagonale \
	void ajoutDiagonale(String s, int i, int j, int sens) {
		cherche.add(s);
		if(sens==0) s = reverseWord(s);
			for(int k=0; k<s.length(); k++) {
				grille[k+i][k+j].setText(Character.toString(s.charAt(k)));	
			}
	}
	
	// possibilité de placement en /
	boolean diagonale2(String s, int i, int j, int sens) {
		if(sens==0) s = reverseWord(s);
		if(i+s.length()-1 <= 8 && j-s.length()+1 >= 0) {
			for(int k=0; k<s.length(); k++) {
					if( (!grille[k+i][j-k].getText().equals(Character.toString(s.charAt(k))) 
						&& !grille[k+i][j-k].getText().equals("  ")) ) {
						return false;
					}
			}
			return true;
		}
		return false;
	}
			
	// ajout en diagonale /
	void ajoutDiagonale2(String s, int i, int j, int sens) {
		cherche.add(s);
		if(sens==0) s = reverseWord(s);
			for(int k=0; k<s.length(); k++) {
				grille[k+i][j-k].setText(Character.toString(s.charAt(k)));	
			}
	}
	
	// inverse le sens d'un mot //
	String reverseWord(String s) {
		String s1 = new String(Character.toString(s.charAt(s.length()-1)));
		for(int i=s.length()-2; i>=0; i--) {
			s1 =s1 + s.charAt(i);
		}
		return s1;
	}
	
	// choix d'un sens au hasard, si on peut ajouter le mot s on l'ajoute
	void ajCol(String s, int i, int j) {
		int sens = (int)(Math.random()*2);
		if(colonne(s,i,j,sens)) ajoutColonne(s,i,j,sens);
	}
	
	void ajLine(String s, int i, int j) {
		int sens = (int)(Math.random()*2);
		if(ligne(s,i,j,sens)) ajoutLigne(s,i,j,sens);
	}
	
	void ajDiag2(String s, int i, int j) {
		int sens = (int)(Math.random()*2);
		if(diagonale2(s,i,j,sens)) ajoutDiagonale2(s,i,j,sens);
	}
	
	void ajDiag1(String s, int i, int j) {
		int sens = (int)(Math.random()*2);
		if(diagonale(s,i,j,sens)) ajoutDiagonale(s,i,j,sens);
	}
	
	// affiche les mots que le joueur doit chercher
	void afficherMots(Pane p) {
		for(int i=0; i<cherche.size(); i++) {
			lab.add(creerLabel(p,cherche.get(i),"listeMot",750,70+30*i));
		}
	}
	
	// Fonction remplissant une grille de mots
	void remplir() throws FileNotFoundException {
		ArrayList<int[]> ind = new ArrayList<int[]>();
			// on rentre d'abord un maximum de mots de taille > 4 dans la grille
			while(ind.size()!=36) {
				int i = (int)(Math.random()*6);
				int j = (int)(Math.random()*6);
				int[] add = {i,j};
				while(ind.contains(add)) {
					i++;
					if(i==9) i=0;j++;
					if(j==9) j=0;
				}
				ind.add(add);
				ajDiag2(mot_max9(),i,j);
				ajDiag1(mot_max9(),i,j);
				ajCol(mot_max9(),i,j);
				ajLine(mot_max9(),i,j);		
			}
			for(int i=6; i<9; i++) {
				for(int j=0 ; j<6 ; j++) {
					ajCol(mot_max9(),i,j);
				}
			}
			for(int i=0; i<6; i++) {
				for(int j=6 ; j<9 ; j++) {
					ajLine(mot_max9(),i,j);
				}
			}
			// ensuite on complète au mieux la grille en ajoutant des mots de 3 lettres
			for(int k=0; k<9; k++) {
				for(int l=0 ; l<9 ; l++) {
					int al = (int)(Math.random()*4);
					switch(al) {
					case 0:
						ajDiag2(xLettres(3),k,l);
						ajDiag1(xLettres(3),k,l);
						ajCol(xLettres(3),k,l);
						ajLine(xLettres(3),k,l);
					break;
					case 1:
						ajDiag1(xLettres(3),k,l);
						ajCol(xLettres(3),k,l);
						ajLine(xLettres(3),k,l);
						ajDiag2(xLettres(3),k,l);
					break;
					case 2:
						ajCol(xLettres(3),k,l);
						ajLine(xLettres(3),k,l);
						ajDiag2(xLettres(3),k,l);
						ajDiag1(xLettres(3),k,l);
					break;
					case 3:
						ajLine(xLettres(3),k,l);
						ajDiag2(xLettres(3),k,l);
						ajDiag1(xLettres(3),k,l);
						ajCol(xLettres(3),k,l);
					break;
					}
				}
			}
			motCache(); // une fois la grille rempli un maximum on génère le mot caché dans les cases restantes
		}

	
	// fonction contenant une liste de couleur pour un visuel ergonomique des mots surlignés dans la grille //
	String newColor(int x) {
		switch(x) {
			case 0: return "blue";
			case 1: return "#7FFFD4";
			case 2: return "#FF00FF";
			case 3: return "#90EE90";
			case 4: return "#FF7F50";
			case 5: return "#FF0000";
			case 6: return "#FFFF00";
			case 7: return "#FF69B4";
			case 8: return "#ADFF2F";
			case 9: return "#9932CC";
			case 10: return "#8470FF";
			case 11: return "#FFA500";
			case 12: return "#D02090";
			case 13: return "#5F9EA0";
			case 14: return "#A9A9A9";
			case 15: return "#BDB76B";
			case 16: return "#D87093";
			case 17: return "#32CD32";
			case 18: return "#20B2AA";
			case 19: return "#C71585";
			default: return "#C0C0C0";
		}
	}
	
	// fonction générant la grille vide avant remplissage //
	public Label[][] genererGrille(Pane p, Scene s, Label info){
		for(int i=0; i<9; i++) {
			for(int j=0;j<9;j++) {
				grille[i][j] = creerLabel(p,"  ","caseL",(70*(i+1))+5,(70*(j+1))+5);
				
				grille[i][j].setMinWidth(70);
				grille[i][j].setMaxWidth(70);
				
				grille[i][j] = addAction(s,p,i,j,info);
				
			}
		}
		return grille;
	}
	
}
