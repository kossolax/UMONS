package gui.ctrl;

import java.io.IOException;

import framework.Article;
import framework.Category;
import framework.Machine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;


public class GestionFournisseur extends Pane {    
    
	private Stage mainApp;
	private Scene scene;
	private Machine machine;
	
    public GestionFournisseur(Stage stage, Machine machine) {
        this.mainApp = stage;
        this.machine = machine;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/GestionFournisseur.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage.setTitle("Création d'une nouvelle machine");
        	stage.setScene(scene);
        	stage.setResizable(false);
        	
        	initialize(stage);
        	
        	stage.show();
        	
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
    private void initialize(Stage stage) {
    	TabPane tp = (TabPane)scene.lookup("#tabCategory");
    	for( Category c : machine.getCategories() ) {
    		Tab tab = new Tab(c.toString());
    		Pane p = new Pane();
    		
    		if( c.getArticles() != null ) {
	    		for( Article a : c.getArticles() ) {
	    			
	    		}
    		}
    		
    		Pane n = new Pane();
    		
    		Label l1 = new Label("Nouvel article");
    		Label l2 = new Label("+");
    		l1.setAlignment(Pos.CENTER);
    		l2.setFont(new Font("System", 80));
    		n.setOnMousePressed(new EventHandler<MouseEvent>() {
        	    public void handle(MouseEvent e) {
        	        System.out.println(e);
        	    }
        	});
    		
    		n.setPrefWidth(100.0);
    		n.setPrefHeight(100.0);
    		
    		n.getChildren().add(l1);
    		n.getChildren().add(l2);
    		    		
    		p.getChildren().add(n);
    		tab.setContent(p);
    		
    		tp.getTabs().add( tab );
    	}
    	
    	Tab tab = new Tab("Ajouter");
    	tp.getTabs().add( tab );
    	
    	tp.getSelectionModel().selectedItemProperty().addListener(
    		new ChangeListener<Tab>() {
    		@Override
    		public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
    			if( t1 == tab ) {
    				tp.getSelectionModel().select(t);
    				System.out.println("in new tab...");
    			}
    		}
    	});
    	
    }
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
}