package application;

import javafx.application.Application;
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

			Label presentation = new Label("Bonjour et bienvenue, cette application vous propose de jouer au choix parmis 3 mini-jeux: \n Sudoku , SudokuLettre , MotsFleches , Pendu et Motus");
			presentation.getStyleClass().add("presentation");
		
			Button h = c.creerBouton("Jouer","h",360,370);	// bouton jouer hors ligne
            // au clic du bouton lance le choix du jeu
            h.setOnAction(_ -> c.choixJeu(primaryStage));

			pageDeGarde.getChildren().addAll(presentation,h);
			
			primaryStage.setTitle("JavaFX Project");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.fillInStackTrace();
		}
	}
	
}
