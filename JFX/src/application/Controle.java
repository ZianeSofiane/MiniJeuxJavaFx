package application;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
	Cette classe est une classe de Conrole dans laquelle se trouve des fonctions communes
	utiles aux autres classes du projet. Les autres classes héritent donc toute de celle-ci.
*/

public class Controle {
	
	// Fonction permettant de créer une nouvelle page //
	public void newPage(Stage s, Pane p, Scene scene,String titre) {
		s.hide();				// on cache l'ancien
    	s.setFullScreen(true); 	// affichage pleine écran
		p.getStyleClass().add("pageDeGarde");	// Style css (pour affichage fond et image)
		s.setTitle(titre);		// Titre
		// Lien avec le fichier css
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
		s.setScene(scene);
		s.show();
	}
	
	// Permet la création un bouton //
	public Button creerBouton(String nom,String css,double x,double y) {
		Button b = new Button(nom);	// Texte du bouton
		b.getStyleClass().add(css);	// nom style css
		b.setLayoutX(x);			// position x sur l'écran
		b.setLayoutY(y);			// position y sur l'écran
		
		return b;					// retour du bouton créer
	}
	
	// Création d'une image //
	public ImageView creerImage(String url,double dimX,double dimY,double posX,double posY) {
		final java.net.URL imageURL = getClass().getResource(url);  // lien avec URL de l'image
        assert imageURL != null;
        final Image image = new Image(imageURL.toExternalForm());
		final ImageView imageView = new ImageView(image);
		
		imageView.setFitWidth(dimX); 	// dimension X
        imageView.setFitHeight(dimY); 	// dimension Y
        imageView.setLayoutX(posX); 	// position X
        imageView.setLayoutY(posY); 	// position Y
        
        return imageView;				// retour de l'image créer
	}
	
	// Création d'un label //
	Label creerLabel(Pane p,String txt, String css, int posX, int posY) {
		Label label = new Label(txt);	// Texte du label
		label.getStyleClass().add(css);	// nom css
		label.setLayoutX(posX);			// position X
		label.setLayoutY(posY);			// position Y
		p.getChildren().addAll(label);	// ajout de l'image sur le pane (permet d'ajouter l'objet à l'écran)
		
		return label;					// retour du Label
	}
	
	// Ajout d'un bouton retour à l'écran du Choix du Jeu //
	void addReturn(Pane p,Stage s) {
		ImageView retour = creerImage("/project/image/return.png",80,80,1170,20); // image du bouton
        // Action se déclenchant au clic
        retour.setOnMouseClicked(_ -> {
            choixJeu(s);					// retour au choix du jeu
        });
		p.getChildren().addAll(retour);			// ajout au pane
	}
	
	// Ajout d'un bouton retour au premier écran de l'application //
	void addReturn2(Pane p,Stage s) {
		ImageView retour = creerImage("/project/image/return.png",80,80,1170,20);
		retour.setOnMouseClicked(_ -> (new Main()).start(s));
		p.getChildren().addAll(retour);
	}
	
	// Fonction de création d'une zone de texte //
	TextField saisie(Pane p, TextField zoneTxt, int width, int posX, int posY) {
		zoneTxt.setPrefWidth(width); 		// largeur
		zoneTxt.setPrefHeight(50); 		// hauteur
		zoneTxt.setLayoutX(posX);			// position X
		zoneTxt.setLayoutY(posY);			// position Y
		zoneTxt.getStyleClass().add("saisie");	// nom css
		p.getChildren().addAll(zoneTxt);	// ajout au pane
		
		return zoneTxt;						// retour de la zone de texte
	}
	
	// Fonction permettant de selectionner aléatoirement un mot de x Lettres du dictionnaires //
	String xLettres(int x) throws FileNotFoundException, URISyntaxException {
		ArrayList<String> xL = new ArrayList<>();	// Liste dans laquelle on ajoutera les mots de x Lettres du dictionnaires

		File f = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("dictionnaire.txt")).toURI());

		FileReader fr = new FileReader (f); // Lecteur de fichier
		BufferedReader br = new BufferedReader (fr);	
		try 
		{
			String line = br.readLine();	// Lecture d'une ligne
			while (line!= null)				// Tant qu'on a pas fini de parcourir le dictionnaire
				{
					if (line.length()== x)	// si la taille du mot lu est x
						{	
							xL.add(line);	// on l'ajoute à notre liste
						}
					line=br.readLine();		// passage à la prochaine ligne
				}
		br.close();		// fermeture des lecteurs
		fr.close();
		}
		 catch (IOException exception)
	    {
	        System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
	    }
		
		int i = (int)(Math.random()*xL.size()); // variable aléatoire entre 0 et la taille de la liste
		
		return xL.get(i);	// retour du mot à la place i de la liste
	}
	
	/*
	 * Ajout d'un bouton affichant les règles d'un jeu
	 * Entrées : un bouton, la liste de nom des images des règles de chaque jeu, un entier pour la selection d'une image
	 */
	void rules(Button b, String image) {
		b.setOnMouseClicked(_ -> {
            Stage s = new Stage();
            Pane p = new Pane();
            Scene scene2 = new Scene(p);
            s.setScene(scene2);
            final ImageView im = creerImage("/project/image/"+image+".png",0,0,10,10);
            p.getChildren().setAll(im);
            s.setWidth(550);
            s.setHeight(650);
            s.setTitle("Règles");
            s.setScene(scene2);

            s.show();
        });
	}
	
	// Page de choix du jeu //
	public void choixJeu(Stage stage) {
		Pane p = new Pane();
		Scene scene2 = new Scene(p);
		newPage(stage,p,scene2,"ChoixJeu");
        
		final ImageView uphf = creerImage("/project/image/Logo_UPHF.png",600,200,384,20);
        p.getChildren().setAll(uphf); 
		
        // Boutons corespondants à chaque jeu //
		Button sudoku = creerBouton("Sudoku","sudoku",200,250);
		Button lettre = creerBouton("Sudoku Lettre","lettre",200,350);
		Button motus = creerBouton("Motus","motus",200,450);
		Button pendu = creerBouton("Pendu","pendu",200,550);
		Button meles = creerBouton("Mots Meles","meles",200,650);
		
		// Listes pour les règles des jeux //
		ArrayList<String> image = new ArrayList<>();
		image.add("rulesSudoku"); image.add("rulesLettre"); image.add("rulesMotus"); image.add("rulesPendu"); image.add("rulesMeles");
		
		// placement des boutons des règles //
		ArrayList<Button> rules = new ArrayList<>();
		for(int i=0; i<5; i++) {
			rules.add(creerBouton("Règles","rules",1000,250+100*i));
			rules(rules.get(i),image.get(i));
			p.getChildren().add(rules.get(i));
		}	
		
		// Si on joue en ligne, bouton retour à la page perso du joueur
		addReturn2(p,stage);
		
		// Lancement du jeu Sudoku //
		sudoku.setOnMouseClicked(_ -> {
            Pane p1 = new Pane();		// pane
            Scene scene3 = new Scene(p1);// scene
            newPage(stage, p1, scene3,"sudoku");	// nouvelle page
            addReturn(p1,stage);					// bouton retour

            // Boutons de choix de difficultés du Sudoku //

            Button easy = creerBouton("Easy      ","sudoku",200,250);
            easy.setOnMouseClicked(_ -> new Sudoku(stage,40));
            Button medium = creerBouton("Medium","sudoku",200,350);
            medium.setOnMouseClicked(_ -> new Sudoku(stage,70));
            Button hard = creerBouton("Hard     ","sudoku",200,450);
            hard.setOnMouseClicked(_ -> new Sudoku(stage,100));
            p1.getChildren().addAll(easy,medium,hard);
        });
		// Lancement du jeu Sudoku Lettre //
		lettre.setOnMouseClicked(_ -> {
            Pane p2 = new Pane();
            Scene scene4 = new Scene(p2);
            newPage(stage, p2, scene4,"lettre");
            addReturn(p2,stage);
            Button easy = creerBouton("Easy      ","sudoku",200,250);
            easy.setOnMouseClicked(_ -> new Lettre(stage,40));
            Button medium = creerBouton("Medium","sudoku",200,350);
            medium.setOnMouseClicked(_ -> new Lettre(stage,70));
            Button hard = creerBouton("Hard     ","sudoku",200,450);
            hard.setOnMouseClicked(_ -> new Lettre(stage,100));
            p2.getChildren().addAll(easy,medium,hard);
        });
		// Lancement du jeu Motus //
		motus.setOnMouseClicked(_ -> new Motus(stage));
		// Lancement du jeu Pendu //
		pendu.setOnMouseClicked(_ -> new Pendu(stage));
		// Lancement du jeu Mots Meles //
		meles.setOnMouseClicked(_ -> new Meles(stage));
		// Ajout des boutons au pane //
		p.getChildren().addAll(sudoku,lettre,motus,pendu,meles);
	}
	
}
