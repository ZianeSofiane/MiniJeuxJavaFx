package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


public class Main extends Application {
	Controle c = new Controle();		// objet contôle pour utiliser ses fonctionnalités
	@Override
	public void start(Stage primaryStage) {
		try {
			Pane pageDeGarde = new Pane();						// nouveau pane
			Scene scene = new Scene(pageDeGarde);				// nouvelle scene
			c.newPage(primaryStage,pageDeGarde,scene,"sudoku");	// nouvelle page
			
			final ImageView uphf = c.creerImage("/project/image/Logo_UPHF.png",600,200,384,20);
	        pageDeGarde.getChildren().setAll(uphf); 
	        
			Label names = new Label("BETOURNE Aurelien \nBERNIER Thomas \nBOUCAUT Lilian \nRANDRIANANDRASANA Harivelo \nZIANE Sofiane");
			names.getStyleClass().add("names");
			Label presentation = new Label("Bonjour et bienvenue, cette application vous propose de jouer au choix parmis 3 mini-jeux: \n Sudoku , SudokuLettre , MotsFleches , Pendu et Motus");
			presentation.getStyleClass().add("presentation");
			
			Button sign = c.creerBouton("S'inscrire","sign",310,400);	// bouton insciption
			Button log = c.creerBouton("Se connecter","log",677,400);	// bouton connexion
		
			Button h = c.creerBouton("Jouer hors ligne","h",310,490);	// bouton jouer hors ligne
			h.setOnAction(new EventHandler<ActionEvent>() {				// au clic du bouton lance le choix du jeu
			    public void handle(ActionEvent e) {
			    	c.choixJeu(primaryStage);							
			    }
			});

			log.setOnAction(new EventHandler<ActionEvent>() {			// au clic lance la page de connnexion
			    public void handle(ActionEvent e) {
			    	new Log(primaryStage);
			    }
			});
			sign.setOnAction(new EventHandler<ActionEvent>() {			// au clic lance la page d'inscription
			    public void handle(ActionEvent e) {
			    	new Sign(primaryStage);
			    }
			});
			
/*Test*/	/*Controle.id = "salut"; c.points = 12; c.setPts("pendu");*/
			
			pageDeGarde.getChildren().addAll(names,presentation,sign,log,h);
			
			primaryStage.setTitle("JavaFX Project");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}

/*
Tooltip toolSign = new Tooltip("Sinscrire");
Tooltip.install(sign, toolSign);*/
/*
final TextField textField = new TextField();
page2.getChildren().addAll(textField);*/
