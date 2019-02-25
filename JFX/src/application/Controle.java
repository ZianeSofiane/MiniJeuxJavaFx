// Ziane Sofiane , Betourne Aurelien //

// L3 Informatique //

package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
	Cette classe est une classe de Conrole dans laquelle se trouve des fonctions communes
	utiles aux autres classes du projet. Les autres classes héritent donc toute de celle-ci.
*/

public class Controle {
	static String id; 	// identifiant de connexion unique
	int points=0; 		// initialisation du nb de points d'un jeu à 0;
	
	
	// Fonction permettant de créer une nouvelle page //
	public void newPage(Stage s, Pane p, Scene scene,String titre) {
		s.hide();				// on cache l'ancien
    	s.setFullScreen(true); 	// affichage pleine écran
		p.getStyleClass().add("pageDeGarde");	// Style css (pour affichage fond et image)
		s.setTitle(titre);		// Titre
		// Lien avec le fichier css
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
		retour.setOnMouseClicked(new EventHandler<MouseEvent>() { // Action se déclenchant au clic
		    @Override
		    public void handle(MouseEvent e) {
		    	choixJeu(s);					// retour au choix du jeu
		    }
		});
		p.getChildren().addAll(retour);			// ajout au pane
	}
	
	// Ajout d'un bouton retour au premier écran de l'application //
	void addReturn2(Pane p,Stage s) {
		ImageView retour = creerImage("/project/image/return.png",80,80,1170,20);
		retour.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent e) {
		    	(new Main()).start(s);
		    }
		});
		p.getChildren().addAll(retour);
	}
	
	// Ajout d'un bouton retour à l'écran post connexion d'un utilisateur (avec Bouton Jouer, Log out et affichage du classement //
	void addReturn3(Pane p,Stage s) {
		ImageView retour = creerImage("/project/image/return.png",80,80,1170,20);
		retour.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent e) {
		    	(new Log(s)).menu(s,id);
		    }
		});
		p.getChildren().addAll(retour);
	}
	
	// Fonction de création d'une zone de texte //
	TextField saisie(Pane p,TextField zoneTxt,String css,int width,int height,int posX,int posY) {
		zoneTxt.setPrefWidth(width); 		// largeur
		zoneTxt.setPrefHeight(height); 		// hauteur
		zoneTxt.setLayoutX(posX);			// position X
		zoneTxt.setLayoutY(posY);			// position Y
		zoneTxt.getStyleClass().add(css);	// nom css
		p.getChildren().addAll(zoneTxt);	// ajout au pane
		
		return zoneTxt;						// retour de la zone de texte
	}
	
	// Fonction permettant de selectionner aléatoirement un mot de x Lettres du dictionnaires //
	String xLettres(int x) throws FileNotFoundException
	{	
		ArrayList<String> xL = new ArrayList<String>();	// Liste dans laquelle on ajoutera les mots de x Lettres du dictionnaires
		File f = new File("Dictionnaire.txt"); // url du fichier texte du dictionnaire
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
	 *  Fonction permettant de mettre à jour les points d'un joueur après avoir fini une partie //
	 * 	Entrée : Nom du jeu joué
	*/
	void setPts(String jeu) {
		try {
		Class.forName("org.postgresql.Driver"); // driver permettant le lien entre l'éditeur et la base de donnée
	      System.out.println("Driver O.K.");

	      String url = "jdbc:postgresql://localhost:5432/Projet"; // url de la bdd
	      String user = "postgres";		// utilisateur
	      String passwd = "K7se9mkc";	// mot de passe

	      Connection conn = DriverManager.getConnection(url, user, passwd); // connection à la bdd
	      System.out.println("Connexion effective !");         
	     
	      // Variables de requètes //
	      Statement statement = conn.createStatement();
	      ResultSet resultat;
	     
	      int points1 = 0; // Variables de mise à jours de points
	      int points2 = 0;
	      
	      // Requête : Selection de jeu (variable entière de points correspondant au jeu) de la table Jeu correspondant à l'id de connexion
	      resultat = conn.createStatement().executeQuery("SELECT "+jeu+" FROM \"Jeu\" WHERE id='"+id+"';");
	      while ( resultat.next() ) { 
	    	  points1 = resultat.getInt(jeu); 	// enregistrements points précédent
	    	  points2 = points + points1 ;		// nouveau points = somme des points précédents et ceux obtenus
	    	  // Requête : Update de la variable de points
	    	  statement.executeUpdate("UPDATE \"Jeu\" SET "+jeu+"="+points2+" WHERE id='"+id+"';");
	      }
	      if(points1==0) { // si le joueur n'a pas encore jouer au jeu , on cré la variable de points
	    	  statement.executeUpdate("INSERT INTO \"Jeu\" (id,"+jeu+") VALUES ('"+id+"','"+points+"');");
	      }
	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	}
	
	
	/*
	 * Ajout d'un bouton affichant les règles d'un jeu
	 * Entrées : un bouton, la liste de nom des images des règles de chaque jeu, un entier pour la selection d'une image
	 */
	void rules(Button b, String image) {
		b.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
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
			}
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
		ArrayList<String> image = new ArrayList<String>();
		image.add("rulesSudoku"); image.add("rulesLettre"); image.add("rulesMotus"); image.add("rulesPendu"); image.add("rulesMeles");
		
		// placement des boutons des règles //
		ArrayList<Button> rules = new ArrayList<Button>();
		for(int i=0; i<5; i++) {
			rules.add(creerBouton("Règles","rules",1000,250+100*i));
			rules(rules.get(i),image.get(i));
			p.getChildren().add(rules.get(i));
		}	
		
		// Si on joue en ligne, bouton retour à la page perso du joueur
		if(id==null) {
			addReturn2(p,stage);
		}
		else { // Sinon retour au premier écran
			addReturn3(p,stage);
		}
		
		// Lancement du jeu Sudoku //
		sudoku.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {	
				Pane p = new Pane();		// pane
				Scene scene2 = new Scene(p);// scene
				newPage(stage,p,scene2,"sudoku");	// nouvelle page
				addReturn(p,stage);					// bouton retour
				
				// Boutons de choix de difficultés du Sudoku //
				
				Button easy = creerBouton("Easy      ","sudoku",200,250);
				easy.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {	
						new Sudoku(stage,40);
					}	
				});
				Button medium = creerBouton("Medium","sudoku",200,350);
				medium.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {	
						new Sudoku(stage,70);
					}	
				});
				Button hard = creerBouton("Hard     ","sudoku",200,450);
				hard.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {	
						new Sudoku(stage,100);
					}	
				});
				p.getChildren().addAll(easy,medium,hard);
			}	
		});
		// Lancement du jeu Sudoku Lettre //
		lettre.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {	
				Pane p = new Pane();
				Scene scene2 = new Scene(p);
				newPage(stage,p,scene2,"lettre");
				addReturn(p,stage);
				Button easy = creerBouton("Easy      ","sudoku",200,250);
				easy.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {	
						new Lettre(stage,40);
					}	
				});
				Button medium = creerBouton("Medium","sudoku",200,350);
				medium.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {	
						new Lettre(stage,70);
					}	
				});
				Button hard = creerBouton("Hard     ","sudoku",200,450);
				hard.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {	
						new Lettre(stage,100);
					}	
				});
				p.getChildren().addAll(easy,medium,hard);
			}	
		});
		// Lancement du jeu Motus //
		motus.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {	
				new Motus(stage);
			}	
		});
		// Lancement du jeu Pendu //
		pendu.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {	
				new Pendu(stage);
			}	
		});
		// Lancement du jeu Mots Meles //
		meles.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {	
				new Meles(stage);
			}	
		});
		// Ajout des boutons au pane //
		p.getChildren().addAll(sudoku,lettre,motus,pendu,meles);
	}
	
}
