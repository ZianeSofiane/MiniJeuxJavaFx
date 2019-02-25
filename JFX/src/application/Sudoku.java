package application;

/*
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
*/
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Sudoku extends Controle {
	Button[][] grille = new Button[9][9];	// grille de jeu
	Button[][] solution = new Button[9][9];	// solution de la grille
	Button[][] saisie = new Button[4][3];	// Padd de saisie des valeurs
	ArrayList<String> nums = valPadd();		// liste des caractères de jeu 
	ArrayList<ArrayList<Integer>> l = new ArrayList<ArrayList<Integer>>();	// liste d'indices

	
	public Sudoku(Stage stage, int difficulty) {
		super();
		Pane p = new Pane();
		Scene scene2 = new Scene(p);
		
		// bouton des règles
		Button rules = creerBouton("Règles","rules",1250,680);
		rules(rules,"rulesLettre");
		p.getChildren().add(rules);
		
		Label info = creerLabel(p,"Bienvenue au sudoku !","info",825,400);
		saisie = genererSaisie(p,nums,info);	//génération du padd de saisie
		newPage(stage,p,scene2,"sudoku");		// nouvelle page
		// affichage du padd
		for(int i=0; i<9; i++) {
			for(int j=0;j<9;j++) {
				solution[i][j] = creerBouton("  ","case",(64*(i+1))+10,(70*(j+1))+5);
			}
		}
		addReturn(p,stage);						// retour au choix du jeu
		
		// ajout des indices de la grille sauf la diagonale dans la liste l ( va servir pour la génération de la grille )
		for(int i=0; i<9 ; i++) {
			for(int j=0; j<9 ; j++) {
				if(i!=j) {
				ArrayList<Integer> indices = new ArrayList<Integer>();
				indices.add(i);indices.add(j);
				l.add(indices);	}
			}
		}
		
		grille = genererGrille(p, info, difficulty);	// génération de la grille
	}
	
	
	// suppression des élément de la diagonal \ de la grille
	void suppDiag() {
		for(int i=0; i<9; i++) {
			solution[i][i].setText("  ");
		}
	}
	
	// Définition des Valeurs du padd
	public ArrayList<String> valPadd(){
		ArrayList<String> val = new ArrayList<String>(9);
		val.add("1"); 
		val.add("2"); 
		val.add("3"); 
		val.add("4"); 
		val.add("5");
		val.add("6");
		val.add("7");
		val.add("8");
		val.add("9");
		
		return val;
	}
	
	// Génération de la grille de jeu //
	public Button[][] genererGrille(Pane p, Label info, int difficulty){
		solution();				// on génère une solution aléatoire
		suppDiag();				// on supprime les valeurs de la diagonale
		delete(0,difficulty,l);	// on supprime un certain nombre de valeurs de la grille selon la difficulté choisie
		for(int i=0; i<9; i++) {
			for(int j=0;j<9;j++) {
				if(i==2 || i==5) {
					if(j==2 || j==5) {	// Gestion des styles des cases pour l'affichage des bordures
						// gestion du style css selon si c'est une case jouable ou non, les chiffres de départs de la grille sont en gras
						if(solution[i][j].getText().equals("  ")) {
							grille[i][j] = creerBouton(solution[i][j].getText(),"caseV1",(64*(i+1))+10,(70*(j+1))+5);
							grille[i][j] = addAction(i,j,saisie,info,p);	// ajout de l'action au clic des cases jouables
						}
						else {
							grille[i][j] = creerBouton(solution[i][j].getText(),"case1",(64*(i+1))+10,(70*(j+1))+5);
						}
					}
					else {
						if(solution[i][j].getText().equals("  ")) {	
							grille[i][j] = creerBouton(solution[i][j].getText(),"caseV2",(64*(i+1))+10,(70*(j+1))+5);
							grille[i][j] = addAction(i,j,saisie,info,p);	// ajout de l'action au clic des cases jouables
						}
						else {
							grille[i][j] = creerBouton(solution[i][j].getText(),"case2",(64*(i+1))+10,(70*(j+1))+5);
						}
					}
				}
				else if(j==2 || j==5) {
					if(solution[i][j].getText().equals("  ")) {
						grille[i][j] = creerBouton(solution[i][j].getText(),"caseV3",(64*(i+1))+10,(70*(j+1))+5);
						grille[i][j] = addAction(i,j,saisie,info,p);	// ajout de l'action au clic des cases jouables
					}
					else {
						grille[i][j] = creerBouton(solution[i][j].getText(),"case3",(64*(i+1))+10,(70*(j+1))+5);
					}
				}
				else {
					if(solution[i][j].getText().equals("  ")) {
						grille[i][j] = creerBouton(solution[i][j].getText(),"caseV",(64*(i+1))+10,(70*(j+1))+5);
						grille[i][j] = addAction(i,j,saisie,info,p);	// ajout de l'action au clic des cases jouables
					}
					else {
						grille[i][j] = creerBouton(solution[i][j].getText(),"case",(64*(i+1))+10,(70*(j+1))+5);
					}
				}
				// dimension de la case
				grille[i][j].setMinWidth(64);
				grille[i][j].setMaxWidth(64);
				grille[i][j].setMinHeight(70);
				grille[i][j].setMaxHeight(70);
				p.getChildren().addAll(grille[i][j]);
			}
		}
		return grille;
	}
	
	// génération du padd de saisie //
	public Button[][] genererSaisie(Pane p, ArrayList<String> liste,Label info) {
		Button saisie[][] = new Button[4][3];
		for(int i=0; i<3; i++) {
			for(int j=0;j<3;j++) {
				saisie[i][j] = creerBouton(liste.get((j*3)+(i)),"case",(64*(i+1))+800,(70*(j+1))+5);
				p.getChildren().addAll(saisie[i][j]);
				saisie[i][j].setMinWidth(64);
				saisie[i][j].setMaxWidth(64);
				saisie[i][j].setMinHeight(70);
				saisie[i][j].setMaxHeight(70);
			}
		}
		saisie[3][1] = creerBouton("x","case",(64*(1+1))+800,(72*(3+1))+5);
		p.getChildren().addAll(saisie[3][1]);
		return saisie;
	}	
	
	// FIN //
	void fin(Pane p,Label info) {
		info.setText("VOUS AVEZ GAGNER !");
	}
	
	// renvoie TRUE si la valeur d'une case est déjà dans la ligne
	boolean estDansLaLigne(int i,int ligne, Button[][] grille)
	{
		for(int colonne=0; colonne < 9; colonne++)
		{
			if (grille[colonne][ligne].getText().equals(grille[i][ligne].getText()) && colonne!=i)
			{
				return true;
			}
		}
		return false;
	}
	
	// renvoie TRUE si la valeur d'une case est déjà dans la colonne
	boolean estDansLaColonne (int colonne, int j, Button[][] grille)
	{
		for(int ligne=0; ligne < 9; ligne ++)
		{
			if (grille[colonne][ligne].getText().equals(grille[colonne][j].getText()) && ligne!=j)
			{
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 	renvoie TRUE si la valeur d'une case est déjà dans le carré
	 * 	Entrées: i et j, indices de la case à comparer, les min et max définissant la zone du carré à étudier
	 */
	boolean verifCarre(int i,int j,int minC,int minL,int maxC,int maxL,Button[][] grille) {
		for(int colonne=minC; colonne<=maxC; colonne++) {
			for(int ligne=minL; ligne<=maxL; ligne++) {
				if (grille[colonne][ligne].getText().equals(grille[i][j].getText()) && (ligne!=j || colonne!=i))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	// Renvoie TRUE si la grille est pleine //
	boolean estPleine(Button[][] grille) {
		for(int i=0; i<9; i++) {
			for(int j=0;j<9;j++) {
				if(grille[i][j].getText().equals("  ")){
					return false;
				}
			}
		}
		return true;
	}
	
	
	// Renvoie TRUE si la grille ne présente pas de contradiction et mais à jour les couleurs selon les erreurs
	boolean verif(Button[][] grille) {
		boolean b = true;
		for(int i=0; i<9; i++) {
			for(int j=0;j<9;j++) {
				if(grille[i][j].getText().equals("  ")){				 // si la case est vide, elle ne peut pas être une erreur
					grille[i][j].setStyle("-fx-background-color: white;"
							+ "-fx-text-fill:black");
				}
				// si la valeur de la case est déjà présente dans le carré, la ligne, ou la colonne, on la met en rouge
				else if(estDansLeCarre(i,j,grille) || estDansLaLigne(i,j,grille) || estDansLaColonne(i,j,grille)) {
					grille[i][j].setStyle("-fx-background-color: #FEB7B7;"
							+ "-fx-text-fill:red");
					b = false;
				}
				// si la valeur n'est pas en contradiction avec une autre, elle garde la couleur initiale
				else if(!(estDansLeCarre(i,j,grille) || estDansLaLigne(i,j,grille) || estDansLaColonne(i,j,grille))){
					grille[i][j].setStyle("-fx-background-color: white;"
							+ "-fx-text-fill:black");
				}
			}
		}
		return b;
	}
	
	
	/*
	 *  renvoie TRUE si la valeur à l'indice i,j est déjà présente dans le carré
	 */
	boolean estDansLeCarre(int i,int j,Button[][] grille) {
		boolean b = false;
		// définition de dans quel carré on se trouve selon les indices i,j
		switch(i%3) {
		case 0:
			switch(j%3) {
			case 0: b = verifCarre(i,j,i,j,i+2,j+2,grille); break;
			case 1: b = verifCarre(i,j,i,j-1,i+2,j+1,grille); break;
			case 2: b = verifCarre(i,j,i,j-2,i+2,j,grille); break;
			}
			break;
		case 1:
			switch(j%3) {
			case 0: b = verifCarre(i,j,i-1,j,i+1,j+2,grille); break;
			case 1: b = verifCarre(i,j,i-1,j-1,i+1,j+1,grille); break;
			case 2: b = verifCarre(i,j,i-1,j-2,i+1,j,grille); break;
			}
			break;
		case 2:
			switch(j%3) {
			case 0: b = verifCarre(i,j,i-2,j,i,j+2,grille); break;
			case 1: b = verifCarre(i,j,i-2,j-1,i,j+1,grille); break;
			case 2: b = verifCarre(i,j,i-2,j-2,i,j,grille); break;
			}
			break;
		}
		return b;
	}
	
	// remplissage de la première ligne de la grille aléatoirement 		 //
	// liste l des possibilités, n pour parcourir la ligne récursivement //
	Button[][] ligne1(ArrayList<String> l, int n) {
		if(l.size()==0) {
			return solution;
		}
		else {
			int i = (int)(Math.random()*(l.size()-1));
			solution[n][0].setText(l.get(i));
			l.remove(i);
			return ligne1(l,n+1);
		}
	}
	
	// Generation d'une sous-solution basique
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
	}
	
	
	// Inverse la valeur de 2 cases de la grille solution //
	Button[][] reverse(int i1, int i2, int j1, int j2) {
		String s = solution[i1][j1].getText();
		solution[i1][j1].setText(solution[i2][j2].getText());
		solution[i2][j2].setText(s);
		
		return solution;
	}
	
	/*
	 * 	Suppression de plusieurs valeurs aléatoirement
	 * 	n : variable de boucle, x : condition d'arrêt, l : liste des indices modifiables (on a oter la diagonale pour sudoku Lettre)
	 */
	ArrayList<ArrayList<Integer>> delete(int n, int x, ArrayList<ArrayList<Integer>> indices) {
		ArrayList<ArrayList<Integer>> reste = new ArrayList<ArrayList<Integer>>();
		while(n!=x) {
			if(indices.size()!=0) {
				int i = (int)(Math.random()*(indices.size()-1));
				solution[indices.get(i).get(0)][indices.get(i).get(1)].setText("  ");
				reste.add(indices.get(i));
				n++;
			}	
		}
		return reste;
	}
	
	// Re Rempli les vides de la solution avec une valeur possible aléatoire //
	void reRemplir() {
		for(int i=0 ; i<l.size(); i++) {
			ArrayList<String> poss = valPadd();
			int val = (int)(Math.random()*(poss.size()));
			solution[l.get(i).get(0)][l.get(i).get(1)].setText(poss.get(val));
			while((estDansLeCarre(l.get(i).get(0),l.get(i).get(1),solution))) {
				poss.remove(val);
				val = (int)(Math.random()*(poss.size()));
				solution[l.get(i).get(0)][l.get(i).get(1)].setText(poss.get(val));
			}
		}
	}
	
	/*
	 * 	Compare la valeur de la case d'indice i,j avec les autres valeurs du carré
	 * 	si les deux valeurs sont mals placées, on les échanges
	 * 	Les min et max définissent le carré dans lequel on est 
	 */
	void pivot(int i, int j, int maxi, int mini, int maxj, int minj) {
		if((estDansLaLigne(i,j,solution) || estDansLaColonne(i,j,solution))) {
			for(int k=mini; k<=maxi ; k++) {
			for(int l=minj ; l<=maxj ; l++) {
				if((estDansLaLigne(i,j,solution) || estDansLaColonne(i,j,solution)) 
						&& (estDansLaLigne(k,l,solution) || estDansLaColonne(k,l,solution))) {
					
					solution = reverse(i,k,j,l);
				}
			}
			}
		}	
	}
	
	// Défini on est dans quel carré selon i,j et applique la fonction pivot
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
				pivot(k,l, maxi, mini, maxj, minj);
			}
		}
	}
	
	// 3 légers mélanges des valeurs de la solution de façon quelle soit réalisable facilement par pivot
	void melange1() {
		for(int j=0 ; j<3 ; j++) {
			solution = reverse(3,4,j,j);
			solution = reverse(3,5,j,j);
			solution = reverse(j,j,3,4);
			solution = reverse(j,j,3,5);
		}
		for(int j=6 ; j<9 ; j++) {
			solution = reverse(4,5,j,j);
			solution = reverse(3,5,j,j);
			solution = reverse(j,j,4,5);
			solution = reverse(j,j,3,5);
		}
	}
	
	void melange2() {
		solution = reverse(0,2,1,1);
		solution = reverse(1,1,2,0);
		solution = reverse(6,8,8,6);
		solution = reverse(8,6,7,7);
	}
	
	void melange3() {
		for(int i=0; i<9 ; i++) {
			for(int j=0; j<9 ; j++) {
				solution = reverse(i,j,j,i);
			}
		}
	}
	
	
	// réalise les pivots pour tous les carrés de la grille
	void pivotGrille() {
			for(int n=0 ; n<10 ;n++) {
			pivotCarre(0,0);
			pivotCarre(3,0);
			pivotCarre(6,0);
			pivotCarre(0,3);
			pivotCarre(3,3);
			pivotCarre(6,3);
			pivotCarre(0,6);
			pivotCarre(3,6);
			pivotCarre(6,6);
			}
	}
	
	
	// on génère une solution basique aléatoire puis on effectue des mélanges pour complexifié la grille
	void solution() {
		sous_solution();
		for(int i=0; i<200 ; i++) {
			melange2();
			melange1();
			melange3();
			delete(0,8,l);
			reRemplir();
			pivotGrille();}
		while(!verif(solution)) {
			sous_solution();
			for(int i=0; i<200 ; i++) {
				melange2();
				melange1();
				melange3();
				for(int k=0; k<9 ; k++) {
					for(int m=0; m<9 ; m++) {
						ArrayList<Integer> indices = new ArrayList<Integer>();
						indices.add(k);indices.add(m);
						if(!l.contains(indices)) {
							l.add(indices);
						}	
					}
				}
				delete(0,8,l);
				reRemplir();
				pivotGrille();}
		}
	}
	
	// ajout d'une chaine à une autre pour les hypothèse , on passe une ligne toutes les 3 valeurs
	String hypothese(String m, String add) {
		if(m.contains(add))  m = deleteHyp(m,add);
		else {
			m = m + add;
		if(m.length()%3==0 && m.length()==3) m = m + "\n";
		else if(m.length()%7==0) m = m + "\n";
		}
		
		return m;
	}
	
	// suppression d'une hypothèse
	String deleteHyp(String m,String add) {
		String m2 = new String();
		for(int i=0; i<m.length(); i++) {
			if(!add.equals(Character.toString(m.charAt(i))) && m.charAt(i)!='\n') {
				m2 = hypothese(m2, Character.toString(m.charAt(i)));
			}
		}
		return m2;
	}
	
	
	/*
	 * 	Action au clic sur une des valeurs du padd
	 * 	Entrées: un bouton du padd, les indices i et j de la case de la grille sur laquelle on a cliqué avant celui sur le padd, le pane
	 */
			// bouton de suppression
			// suppression
			// mise à jour des couleurs de la grille, couleur rouge
			// ou il y a des contradiction
			// bouton de saisie
			// mise à jour des couleurs et si la grille est correcte et pleine, partie terminée
			// lancement de la fonction de fin
	public Button clicPad2(Button s,int i,int j,Label info, Pane p) {
		s.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				grille[i][j].setStyle("-fx-background-color: white;");
				if(s.getText().equals("x")) {
					grille[i][j].setText("  ");
					verif(grille);
				}
				else if(grille[i][j].getText().equals("  ")) {
					grille[i][j].setText(s.getText());
					if(verif(grille) && estPleine(grille)) {
						fin(p,info);
					}
				}
				else {
					grille[i][j].setText(hypothese(grille[i][j].getText(),s.getText()));
					grille[i][j].setStyle("-fx-background-color: lightgrey;"
							+ "-fx-text-fill: green;"
							+ "-fx-font-size:10;"
							+ "-fx-padding: 5 5 5 5");
				}
			}
		});
		return s;
	}
	
	
	// Action au clic d'une case de la grille d'indice i,j
	public Button addAction(int i,int j,Button[][] s,Label info,Pane p) {
		grille[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				grille[i][j].setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 ); \r\n" + 
						"    -fx-effect: innershadow( gaussian , rgba(0,0,0,0.7) , 20,0,0,0 );");
				for(int k=0; k<3; k++) {
					for(int l=0;l<3;l++) {
						s[k][l] = clicPad2(s[k][l],i,j, info,p);
					}
				}
				s[3][1] = clicPad2(s[3][1],i,j,info,p);
			}
		});
		return grille[i][j];
	}
	
	
	
	////// PAS FINI, Travail sur une sauvegarde pour le Sudoku //////
	
	/* 
	void playSave() throws FileNotFoundException {
		File f = new File("C:\\Users\\Ziane\\Desktop\\Dictionnaire.txt");
		FileReader fr = new FileReader (f);
		BufferedReader br = new BufferedReader (fr);	
		try 
		{
			String line = br.readLine();
			while (line!= null)
				{
				for(int i=0; i<9; i++) {
            		for(int j=0; j<9; j++) {
            			solution[i][j].setText(Character.toString(line.charAt(j)));
            		}
            		line = br.readLine();
            	}
				for(int i=0; i<9; i++) {
            		for(int j=0; j<9; j++) {
            			solution[i][j].setText(Character.toString(line.charAt(j)));
            		}
            		line = br.readLine();
            	}
				
				for(int i=0; i<9; i++) {
            		for(int j=0; j<9; j++) {
            			grille[i][j].setText(line);
            			if(!grille[i][j].getText().equals("  ") && grille[i][j].getText().length()!=1) {
        					
        				}
            		}
            		line = br.readLine();
            	}
				
				
				
				}
		br.close();
		fr.close();
		}
		 catch (IOException exception)
	    {
	        System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
	    }
	}
	
	
	void save(Button[][] solution, Button[][] grille, Button[][] save) {
		final String chemin = "C:\\Users\\Ziane\\Desktop";
        final File fichier =new File(chemin); 
        try {
            // Creation du fichier
            fichier .createNewFile();
            // creation d'un writer (un écrivain)
            final FileWriter writer = new FileWriter(fichier);
            try {
            	for(int i=0; i<9; i++) {
            		for(int j=0; j<9; j++) {
            			writer.write(solution[i][j].getText());
            		}
            		writer.write("\n");
            	}
            	for(int i=0; i<9; i++) {
            		for(int j=0; j<9; j++) {
            			writer.write(save[i][j].getText());
            		}
            		writer.write("\n");
            	}
            	for(int i=0; i<9; i++) {
            		for(int j=0; j<9; j++) {
            			writer.write(grille[i][j].getText());
            			writer.write("\n");
            		}
            	}
                
                writer.write("encore et encore");
            } finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier");
        }
	}
	
	*/
	
}
