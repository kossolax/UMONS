package gui.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;

import framework.Article;
import framework.Category;
import framework.Machine;
import framework.RawMaterial;
import framework.stockage.Classic;
import framework.stockage.Stockage;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class GestionFournisseur extends Pane {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	private Category focusCategory;
	
    public GestionFournisseur(Stage parent, Machine machine) {
        this.mainApp = parent;
        this.machine = machine;
        this.focusCategory = machine.getCategory();
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/GestionFournisseur.fxml"));
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
    private Pane addNewArticle(FlowPane p, Article a) {
    	Pane n = null;
		try {
			n = (Pane)FXMLLoader.load(getClass().getResource("/gui/views/Article.fxml"));
			((ImageView)n.lookup("#img")).setImage(new Image(a.getImage().toURI().toString()));
			((Label)n.lookup("#txt")).setText(a.getName());		
			p.getChildren().add(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return n;
    }
    private Pane addNewArticle(FlowPane p, String str) {
    	Pane n = null;
		try {
			n = (Pane)FXMLLoader.load(getClass().getResource("/gui/views/Article.fxml"));
			((ImageView)n.lookup("#img")).setImage(new Image(""+getClass().getResource("/data/img/add.png")));
			((Label)n.lookup("#txt")).setText(str);		
			p.getChildren().add(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return n;
    }
    private void initialize(Stage stage) {
    	TabPane tp = (TabPane)scene.lookup("#tabCategory");
    	tp.getTabs().clear();
    	
    	Category c = focusCategory;
    	do {
    		final Category tmpC = c;
    		Tab tab = new Tab(c.toString());
        	FlowPane p = new FlowPane();
        	
        	if( c.getCategories() != null ) {
        		for( Category c2 : c.getCategories() ) {
        			Pane n = addNewArticle(p, c2.toString());
        			n.setOnMousePressed(new EventHandler<MouseEvent>() {
                	    public void handle(MouseEvent e) {
                	    	focusCategory = c2;
                	    	initialize(stage);
                	    }
        			});
        		}
        	}
        	
        	Pane n = addNewArticle(p, "Catégorie");
    		n.setOnMousePressed(new EventHandler<MouseEvent>() {
        	    public void handle(MouseEvent e) {
        	    	TextInputDialog dialog = new TextInputDialog();
        	    	dialog.setTitle("Création d'une nouvelle catégorie");
        	    	dialog.setContentText("Comment doit s'appeller la nouvelle catégorie?");
        	    	Optional<String> result = dialog.showAndWait();
        	    	if( result.isPresent() && result.get().trim().length() > 0 ) {      	    		
        	    		focusCategory = tmpC.addCategory(new Category(result.get().trim()));
        	    		initialize(stage);
        	    	}
        	    }
        	});
    		
        	if( c.getArticles() != null ) {
        		for( Article a : c.getArticles() ) {
        			addNewArticle(p, a);
        		}
    		}
        	n = addNewArticle(p, "Article");
        	n.setOnMousePressed(new EventHandler<MouseEvent>() {
        	    public void handle(MouseEvent e) {
        	    	Article a = CreateNewArticle.getNewArticle(stage, machine);
        	    	if( a != null ) {
        	    		focusCategory.addArticle(a);
        	    		initialize(stage);
        	    	}
        	    }
        	});
        	tab.setContent(p);
        	tp.getTabs().add( tab );
        	
        	c = c.getParent();
    	} while( c != null );    	
    }
    @FXML
    private void handleExit() {
    	stage.close();
    }
    
    @FXML
    private void OnClick_NewMP() {
    	CreateNewMP mp = new CreateNewMP(stage, machine);
    }
    
    
}