package gui.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;

import framework.Article;
import framework.Category;
import framework.Machine;
import framework.RawMaterial;
import framework.modules.Module;
import framework.payement.Payment;
import framework.stockage.Classic;
import framework.stockage.Stockage;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class VueUtilisateur extends Pane {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	private Category focusCategory;
	private Article focusArticle;
	private double solde;
	
    public VueUtilisateur(Stage parent, Machine machine) {
        this.mainApp = parent;
        this.machine = machine;
        this.focusCategory = machine.getCategory();
        this.focusArticle = null;
        this.solde = 0.0;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/utilisateur.fxml"));
        fxmlLoader.setController(this);
        
        try {
        	stage = new Stage();
            scene = new Scene(fxmlLoader.load()); 
            stage.setTitle("Création d'une nouvelle machine");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

        	initialize(stage);
        	parent.toBack();
        	stage.show();
        	
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void initialize(Stage stage) {
    	TabPane tp = (TabPane)scene.lookup("#tabCategory");
    	tp.getTabs().clear();
    	
    	Category c = focusCategory;
    	do {
    		Tab tab = new Tab(c.toString());
        	FlowPane p = new FlowPane();
        	
        	if( c.getCategories() != null ) {
        		for( Category c2 : c.getCategories() ) {
        			Pane n = Utils.addNewCategory(p, c2);
        			n.setOnMousePressed(new EventHandler<MouseEvent>() {
                	    public void handle(MouseEvent e) {
                	    	focusCategory = c2;
                	    	initialize(stage);
                	    }
        			});
        		}
        	}
    		
        	if( c.getArticles() != null ) {
        		for( Article a : c.getArticles() ) {
        			Pane n = Utils.addNewArticle(p, a);
        			n.setOnMousePressed(new EventHandler<MouseEvent>() {
                	    public void handle(MouseEvent e) {
                	    	focusArticle = a;
                	    	updatePayement();
                	    }
        			});
        		}
    		}
        	
        	tab.setContent(p);
        	tp.getTabs().add( tab );
        	
        	c = c.getParent();
    	} while( c != null );    	
    }
    private void updatePayement() {
    	if( focusArticle != null ) {
    		((ImageView)scene.lookup("#image")).setVisible(true);
    		((Label)scene.lookup("#name")).setVisible(true);
    		((Label)scene.lookup("#price")).setVisible(true);
    		
    		((ImageView)scene.lookup("#image")).setImage(new Image(focusArticle.getImage().toURI().toString()));
    		((Label)scene.lookup("#name")).setText(focusArticle.getName());
    		((Label)scene.lookup("#price")).setText(focusArticle.getPrice()+"€");
    	}
    	else {
    		((ImageView)scene.lookup("#image")).setVisible(false);
    		((Label)scene.lookup("#name")).setVisible(false);
    		((Label)scene.lookup("#price")).setVisible(false);
    	}
    	
    	((Label)scene.lookup("#solde")).setText("Solde: "+solde+"€");
    }
    @FXML
    private void handleExit() {
    	stage.close();
    }
    @FXML
    private void OnClick_Buy() {
    	if( focusArticle != null  ) {
    		
    		ChoiceDialog<Payment> dialog = new ChoiceDialog<Payment>();
    		for( Module m : machine.getModules() ) {
    			if( m instanceof Payment ) 
    				dialog.getItems().add((Payment)m);
    		}
    			
    		dialog.setTitle("Choisissez une matière première");
    		dialog.setHeaderText("Choisissez une matière première");
    		Optional<Payment> result = dialog.showAndWait();
    		if (result.isPresent()){
    			Payment p = result.get();
    			if( machine.Buy(p, focusArticle) ) {
    				Alert alert = new Alert(AlertType.CONFIRMATION);
    	    		alert.setTitle("Achat");
    	    		alert.setHeaderText("Votre achat s'est déroulé avec succès. Vous avez acheté: "+focusArticle);
    	    		alert.show();
    			}
    			else {
    				Alert alert = new Alert(AlertType.ERROR);
    	    		alert.setTitle("Erreur");
    	    		alert.setHeaderText("Le paiement a échoué.");
    	    		alert.show();
    			}
    		}
    	}
    }
    @FXML
    private void OnClick_Cancel() {
    	focusArticle = null;
    	updatePayement();
    }
}