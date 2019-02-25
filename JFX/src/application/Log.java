package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Log extends Controle{

	public Log(Stage s) {
		Pane p = new Pane();
		Scene scene = new Scene(p);
		newPage(s,p,scene,"Log in");	// nouvelle page
		addReturn2(p,s);				// retour au premier écran
		
		Label info = creerLabel(p,"Entrez votre identifiant et votre mot de passe","info",175,150);
		info.setStyle("-fx-font-size: 50;");
		creerLabel(p,"Identifiant : ","info",300,300);
		creerLabel(p,"Mot de passe : ","info",256,400);
		
		// saisie id
		TextField id = new TextField();
		id = saisie(p,id,"mdp",400,50,500,300);
		id.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		
		// saisie mot de passe
		PasswordField mdp = new PasswordField();
		mdp.setPrefWidth(400);
		mdp.setPrefHeight(50);
		mdp.setLayoutX(500);
		mdp.setLayoutY(400);
		mdp.getStyleClass().add("mdp");
		p.getChildren().addAll(mdp);
		mdp.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		
		// bouton valider
		Button valider = creerBouton("Valider","log",530,520);
		// ajout de l'action au bouton
		addAction(valider,id,mdp,info, s);
		
		p.getChildren().add(valider);
	}
	
	// au clic du bouton action, lancement de la fonction connection
	void addAction(Button b, TextField id, PasswordField mdp, Label info, Stage s) {
		b.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				connection(id.getText(),mdp.getText(),info,s);
			}
		});
	}
	
	// fonction de connection
	void connection(String id, String mdp, Label info,Stage s) {
		try {
		      Class.forName("org.postgresql.Driver");		// driver
		      System.out.println("Driver O.K.");

		      String url = "jdbc:postgresql://localhost:5432/Projet";		// url projet du pc
		      String user = "postgres";
		      String passwd = "K7se9mkc";

		      Connection conn = DriverManager.getConnection(url, user, passwd);	// connexion à la bdd
		      System.out.println("Connexion effective !");         
		     
		      // outils de requêtes
		      Statement statement = conn.createStatement();
		      ResultSet resultat;
		      
		      String id2 = new String();	// mot ou on enregistrera les selections de la requête
		      // selctionne l'id de Joueur ou l'id est égal à celui saisie et le mot de passe est égal au mot de passe saisie
		      resultat = statement.executeQuery("SELECT id FROM \"Joueur\" WHERE id='"+id+"' AND mdp='"+mdp+"';");
		      while ( resultat.next() ) {	// si les conditions sont vérifier, on est connecté
		    	  id2 = resultat.getString("id");
		    	  info.setText("Bienvenue à toi " +id);
		    	  Controle.id = id;			// l'id du controle du jeu est l'id de celui qui se connecte
		    	  menu(s,id);				// lancement de la fonction menu, affichant l'écran personnel du joueur connecter
		      }
		      if(id2.length()==0) {			// si on a pas trouver de ligne avec l'id et le mdp, on regarde si l'id saisie existe
		    	  resultat = statement.executeQuery("SELECT id FROM \"Joueur\" WHERE id='"+id+"';"); 
		    	  while ( resultat.next() ) {
		    		  id2 = resultat.getString("id");	// si l'id n'est pas trouvé c'est que l'id saisie n'existe pas
				  }
		    	  if(id2.length()==0)	info.setText("Cet identifiant n'existe pas.");
		    	  else	info.setText("Mot de passe incorrect.");	// sinon c'est que le mot de passe est incorrect
		      }
		      
		    } catch (Exception e) {
		      e.printStackTrace();
		      info.setText("Echec connexion");
		    } 
	}
	
	// fonction affichant la page perso de l'utilisateur connecté
	void menu(Stage s,String id) {
		Pane p = new Pane();
		Scene scene = new Scene(p);
		newPage(s,p,scene,"Welcome");	// nouvelle page
		showStats(id,p);				// fonction d'affichage des statistiques
		
		Label info = creerLabel(p,"Bienvenue à toi "+id+ " !","info",30,25);
		info.setStyle("-fx-font-size: 40;");
		
		// Bouton jouer
		Button play = creerBouton("PLAY !","play",900,30);
		play.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent e) {
		    	choixJeu(s);	// lancement de la fonction du choix du jeu
		    }
		});
		// Bouton de déconnexion
		Button out = creerBouton("Log out","out",1250,30);
		out.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
		    public void handle(MouseEvent e) {
				Controle.id = null;			// on remet l'id à null
				(new Main()).start(s);		// on relance la page d'acceuil
			}
		});
		
		p.getChildren().addAll(out,play);
		
	}
	
	// fonction d'affichage des stats perso et des top 5 des jeux //
	void showStats(String id,Pane p) {
		try {
			Class.forName("org.postgresql.Driver");		// driver
		      System.out.println("Driver O.K.");

		      String url = "jdbc:postgresql://localhost:5432/Projet";	// url
		      String user = "postgres";
		      String passwd = "K7se9mkc";

		      Connection conn = DriverManager.getConnection(url, user, passwd);	// connexion
		      System.out.println("Connexion effective !");         
		     
		      ResultSet resultat;	// outil de requête
		      
		      // on selectionne la ligne de l'id de l'utilisateur de la table Jeu
		      resultat = conn.createStatement().executeQuery("SELECT * FROM \"Jeu\" WHERE id='"+id+"';");
		      
		      while ( resultat.next() ) {	// affichages des statistiques personnels
		    	  String ident = resultat.getString("id");
		    	  
		    	  int easy = resultat.getInt("easy");		// get des points du sudoku easy du resultat de la requête
		    	  int medium = resultat.getInt("medium");	// get des points du sudoku medium
		    	  int hard = resultat.getInt("hard");		// get des points du sudoku hard
		    	  int meles = resultat.getInt("meles");		// get des points du mots meles
		    	  int motus = resultat.getInt("motus");		// get des points du motus
		    	  int pendu = resultat.getInt("pendu");		// get des points du pendu
		    	  
		    	  Label i1 = creerLabel(p,"Identifiant","stat2",30,110);
		    	  i1.setStyle("-fx-max-width:220; -fx-min-width:220;");
		    	  creerLabel(p,"SDK Easy","stat2",250,110);
		    	  creerLabel(p,"SDK Medium","stat2",420,110);
		    	  creerLabel(p,"SDK Hard","stat2",590,110);
		    	  creerLabel(p,"Meles","stat2",760,110);
		    	  creerLabel(p,"Motus","stat2",930,110);
		    	  creerLabel(p,"Pendu","stat2",1100,110);
		    	  
		    	  Label i = creerLabel(p,ident,"stat3",30,110+30);
		    	  i.setStyle("-fx-max-width:220; -fx-min-width:220;");
		    	  creerLabel(p,String.valueOf(easy) + " win","stat3",250,110+30);
		    	  creerLabel(p,String.valueOf(medium) + " win","stat3",420,110+30);
		    	  creerLabel(p,String.valueOf(hard) + " win","stat3",590,110+30);
		    	  creerLabel(p,String.valueOf(meles) + " pts","stat3",760,110+30);
		    	  creerLabel(p,String.valueOf(motus) + " pts","stat3",930,110+30);
		    	  creerLabel(p,String.valueOf(pendu) + " pts","stat3",1100,110+30);
		      }
		      
		      
		      
		      ArrayList<String> liste = new ArrayList<String>();	// Liste des noms de colonnes de la table Jeu
		      liste.add("easy"); liste.add("medium"); liste.add("hard"); liste.add("meles"); liste.add("motus"); liste.add("pendu");
		      ArrayList<String> liste2 = new ArrayList<String>();	// Liste des noms des Labels d'affichage
		      liste2.add("Sudoku Easy"); liste2.add("Sudoku Medium"); liste2.add("Sudoku Hard"); liste2.add("Meles"); liste2.add("Motus"); liste2.add("Pendu");
		     
		      // variables pour la gestion de l'affichage des stats
		      int j = 1;
		      int k2;
		      int j2;
		      int j3=0;
		      
		      for(int k=0; k<liste.size(); k++) {
		    	  
		      // selection des stats du jeu k pour chaque id ordonner par ordre decroissant
		      resultat = conn.createStatement().executeQuery("SELECT id,"+liste.get(k)+" FROM \"Jeu\" ORDER BY "+liste.get(k)+" DESC;");
		      while ( resultat.next() && j<6) {		// tant qu'on a pas parcouru la table ou qu'on a déjà pris 5 joueurs
		    	  
		    	  // variables pour la gestion de l'affichage des stats
		    	  if(k>2)	{k2=k-3; j2=j+8; j3=240;}
		    	  else {k2=k; j2=j;}
		    	  
		    	  String ident = resultat.getString("id");
		    	  int val = resultat.getInt(liste.get(k));
		    	  
		    	  Label n = creerLabel(p,liste2.get(k),"stat2",40+390*k2+27*k2,210+j3);
		    	  n.setStyle("-fx-max-width:390; -fx-min-width:390;");
		    	  
		    	  Label i = creerLabel(p,ident,"stat",40+390*k2+27*k2,210+30*j2);
		    	  i.setStyle("-fx-max-width:220; -fx-min-width:220;");
		    	  Label m = creerLabel(p,val + " pts","stat",260+390*k2+27*k2,210+30*j2);
		    	  if(ident.equals(id)) {
		    		  i.setStyle("-fx-background-color:#00FF7F; -fx-max-width:220; -fx-min-width:220;");
		    		  m.setStyle("-fx-background-color:#00FF7F;");
		    	  }
		    	  
		    	  j++;
		      }
		      j=1;
		      
		      }
		      
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
	}
	
}
