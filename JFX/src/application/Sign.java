package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Sign extends Controle{
	
	public Sign(Stage s) {
		Pane p = new Pane();
		Scene scene = new Scene(p);
		newPage(s,p,scene,"meles");		// nouvelle page
		addReturn2(p,s);				// retour à l'écran de départ
		
		Label info = creerLabel(p,"Choisir votre identifiant et votre mot de passe","info",175,150);
		info.setStyle("-fx-font-size: 50;");
		creerLabel(p,"Identifiant : ","info",300,300);
		creerLabel(p,"Mot de passe : ","info",256,400);
		creerLabel(p,"Confirmer mdp : ","info",233,500);
		
		// zone de saisie de l'id //
		TextField id = new TextField();
		id = saisie(p,id,"saisie",400,50,500,300);
		id.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		
		// zone de saisie du mot de passe //
		PasswordField mdp = new PasswordField();
		mdp.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		mdp.setPrefWidth(400);
		mdp.setPrefHeight(50);
		mdp.setLayoutX(500);
		mdp.setLayoutY(400);
		mdp.getStyleClass().add("mdp");
		p.getChildren().addAll(mdp);
		
		// confirmation du mot de passe //
		PasswordField confirm = new PasswordField();
		confirm.setPrefWidth(400);
		confirm.setPrefHeight(50);
		confirm.setLayoutX(500);
		confirm.setLayoutY(500);
		confirm.getStyleClass().add("mdp");
		p.getChildren().addAll(confirm);
		confirm.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		
		Button valider = creerBouton("Valider","log",530,620);		// bouton de validation
		addAction(valider,id,mdp,confirm,info);						// ajout de son action
		
		p.getChildren().add(valider);
	}
	
	// Fonction définissant l'action au clic du bouton valider //
	void addAction(Button b, TextField id, PasswordField mdp, PasswordField confirm, Label info) {
		b.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(id.getText().length()<4) {
					info.setText("L'identifiant doit faire au moins 4 lettres");
				}
				else if(mdp.getText().length()<4) {
					info.setText("Le mot de passe doit faire au moins 4 lettres");
				}
				else if(!confirm.getText().equals(mdp.getText())) {
					info.setText("Mauvaise confirmation");
				}
				else {
					createAccount(id.getText(),mdp.getText(),info);			// lancement de la fonction de création de compte
				}
			}
		});
	}
	
	// Fonction de création du compte //
	void createAccount(String id, String mdp, Label info) {
		try {
		      Class.forName("org.postgresql.Driver");		// driver permettant le lien entre eclipse et la bdd
		      System.out.println("Driver O.K.");

		      String url = "jdbc:postgresql://localhost:5432/Projet";	// url du projet dans l'ordinateur
		      String user = "postgres";
		      String passwd = "K7se9mkc";

		      Connection conn = DriverManager.getConnection(url, user, passwd);		// connexion à la bdd
		      System.out.println("Connexion effective !");         
		     
		      Statement statement = conn.createStatement();		// outil de requête
		      
		      // Création de la ligne du nouveau joueur dans la table Joueur de la bdd //
		      statement.executeUpdate("INSERT INTO \"Joueur\" (id,mdp) VALUES ('"+id+"','"+mdp+"');");
		      info.setText("Compte créer avec succès");
		      
		    } catch (Exception e) {
		      e.printStackTrace();
		      info.setText("Cet identifiant existe déjà");
		    } 
	}
	
}
