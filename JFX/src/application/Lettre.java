package application;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/*
 * 	La classe Lettre du jeu Sudoku lettre h�rite de la classe Sudoku, ayant quasiment les m�mes fonctionnalit�s � part quelques exceptions
 */
public class Lettre extends Sudoku{
	String secretWord;	// mot secret s'affichant en diagonale � saisir apr�s le remplissage de la grille
	
	public Lettre(Stage stage,int difficulty) {
		super(stage,difficulty);
		// TODO Auto-generated constructor stub
	}

	
	// Generation d'un mot de 9 lettres sans doublons
	String motSudokuL(String motS) throws FileNotFoundException, URISyntaxException {
		char [] motP = motS.toCharArray();
		ArrayList<Character> al = new ArrayList<>();
		for(char c : motP)
		{
			al.add(c);
		}	
		HashSet<Character> hs = new HashSet<>(al);
		if(hs.size()==9)
		{
			StringBuilder s = new StringBuilder();
            for (Character character : al) {
                s.append(character);
            }
		return s.toString();
		}
		else return motSudokuL(xLettres(9)); 		
	}
	
	// Generation du mot secret
	String generateSecretWord() {
		if(secretWord == null) {
			try {
				secretWord = motSudokuL(xLettres(9));
			} catch (FileNotFoundException e) {
				e.fillInStackTrace();
			} catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
		return secretWord;
	}
	
	// r�d�finition de la fonction valPadd() ici les valeurs du padd sont les lettres du mot secret
	public ArrayList<String> valPadd(){
		ArrayList<String> val = new ArrayList<>(9);
		String pad = melangeLettresMots(generateSecretWord());	// on genere le mot secret puis m�lange ses lettres
		for(int i=0; i<9; i++) {
			val.add(Character.toString(pad.charAt(i))); 		// ajout des valeurs du padd
		}
		return val;
	}
	
	// supprime le caract�re � l'indice i de la chaine m
	String deleteCharAt(String m, int i) {
		StringBuilder m2 = new StringBuilder();
		for(int j=0; j<m.length(); j++) {
			if(j!=i) {
				m2.append(m.charAt(j));
			}	
		}
		return m2.toString();
	}
	
	// m�lange les lettres d'un mot
	String melangeLettresMots(String m) {
		StringBuilder m2 = new StringBuilder();
		while(!m.isEmpty()) {
			int i = (int)(Math.random()*m.length());
			m2.append(m.charAt(i));
			m = deleteCharAt(m,i);
		}
		return m2.toString();
	}
	
	// inverse le sens du mot en entr�e
	String reverseWord(String s) {
		StringBuilder s1 = new StringBuilder(Character.toString(s.charAt(s.length() - 1)));
		for(int i=s.length()-2; i>=0; i--) {
			s1.append(s.charAt(i));
		}
		return s1.toString();
	}
	
	// ajout du mot secret sur la diagonale
	Button[][] diagonale(){
		int sens = (int)(Math.random()*2);
		if(sens==1) {
			for(int i=0; i<9 ; i++) {
			solution[i][i].setText(Character.toString(secretWord.charAt(i)));
			}
		}
		if(sens==0) {
			String m = reverseWord(secretWord);
			for(int i=0; i<9 ; i++) {
			solution[i][i].setText(Character.toString(m.charAt(i)));
			}
		}
		return solution;
	}
	
	
	// r�d�finition de la fonction ligne 1 vu qu'ici on la determine en fonction de la diagonale
	Button[][] ligne1(ArrayList<String> l, int n) {
		solution = diagonale();
		
			solution[1][0].setText(solution[5][5].getText());
			solution[2][0].setText(solution[7][7].getText());
			solution[3][0].setText(solution[8][8].getText());
			solution[4][0].setText(solution[1][1].getText());
			solution[5][0].setText(solution[3][3].getText());
			solution[6][0].setText(solution[4][4].getText());
			solution[7][0].setText(solution[6][6].getText());
			solution[8][0].setText(solution[2][2].getText());
			
			return solution;
	}
	
	
	// Les fonctions effectuants les modifications de la solution basique de la grille sont red�finis du fait qu'ici on ne souhaite pas modifier la diagonale //
	
	void pivotCarre(int i, int j) {
		int maxi, mini, minj, maxj;
		if(i<3) { maxi=2 ; mini=0 ; }
		else if(i<6) { maxi=5 ; mini=3; }
		else { maxi=8 ; mini=6; }
		if(j<3) { maxj=2 ; minj=0 ; }
		else if(j<6) { maxj=5 ; minj=3; }
		else { maxj=8 ; minj=6; }
		for(int k=mini; k<=maxi ; k++) {
			for(int l=minj ; l<=maxj ; l++) {
				if(k!=l) {
					pivot(k,l, maxi, mini, maxj, minj);
				}
			}
		}
	}
	
	
	void pivot(int i, int j, int maxi, int mini, int maxj, int minj) {
		if((estDansLaLigne(i,j,solution) || estDansLaColonne(i,j,solution))) {
			for(int k=mini; k<=maxi ; k++) {
			for(int l=minj ; l<=maxj ; l++) {
				if((estDansLaLigne(i,j,solution) || estDansLaColonne(i,j,solution)) 
						&& (estDansLaLigne(k,l,solution) || estDansLaColonne(k,l,solution))) {
					if(k!=l) {
						solution = reverse(i,k,j,l);
					}	
				}
			}
			}
		}	
	}
	
	
	void sous_solution() {
		solution = ligne1(nums,0);
		for(int j=1; j<3; j++) {
			for(int i=0; i<9 ; i++) {
				if(i>5) {
					solution[i][j].setText(solution[i-6][j-1].getText());
				}
				else{
					solution[i][j].setText(solution[i+3][j-1].getText());
				}
			}
		}
		for(int j=3; j<9 ; j++) {
			for(int i=0; i<9 ; i++) {
				if(i%3==2) {
					solution[i][j].setText(solution[i-2][j-3].getText());
				}
				else{
					solution[i][j].setText(solution[i+1][j-3].getText());
				}
			}
		}
		for(int i=0; i<9 ;i++) {
			for(int j=3; j<6 ;j++) {
				reverse(i,i,j,j+3);
			}
		}
	}
	
	void melange2() {
		solution = reverse(0,2,2,0);
		solution = reverse(6,8,8,6);
	}
	
	void solution() {
		sous_solution();
		for(int i=0; i<200 ; i++) {
			melange1();
			melange2();
			melange3();
			delete(0,8,l);
			reRemplir();
			pivotGrille();}
		while(!verif(solution)) {
			sous_solution();
			for(int i=0; i<200 ; i++) {
				melange1();
				melange2();
				melange3();
				for(int k=0; k<9 ; k++) {
					for(int m=0; m<9 ; m++) {
						ArrayList<Integer> indices = new ArrayList<>();
						indices.add(k);indices.add(m);
						if(k!=m) {
						if(!l.contains(indices)) {
							l.add(indices);
						}}
					}
				}
				delete(0,8,l);
				reRemplir();
				pivotGrille();}
		}
	}
	
	// red�finition de la foncion fin, ici on doit saisir le mot secret
	void fin(Pane p,Label info) {
		info.setText("Saisissez le mot cache");
		TextField zoneTxt = new TextField();
		zoneTxt = saisie(p,zoneTxt, 300, 800,600);
		zoneTxt.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		Button ok = creerBouton("OK","case",1120,595);
		p.getChildren().add(ok);
		
		ok = actionOk(ok,zoneTxt,info);	
	}
	
	// action au clic du bouton ok associ� � la zone de saisie utilisateur du mot secret apparaissant apr�s avoir fini la grille
	Button actionOk(Button ok,TextField zoneTxt, Label info) {
		ok.setOnMouseClicked(_ -> {
			if(zoneTxt.getText().toUpperCase().equals(secretWord)) {
				info.setText("VOUS AVEZ GAGNE");
            }
		});
		return ok;
	}
	
}
