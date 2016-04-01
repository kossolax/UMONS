package gui.ctrl;

import java.io.IOException;
import java.util.Optional;
import framework.Article;
import framework.Category;
import framework.Machine;
import framework.modules.Module;
import framework.stockage.Stockage;
import gui.MainApp;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
@SuppressWarnings({"unused"})
public class VueFournisseur extends Pane {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	private Category focusCategory;
	
    public VueFournisseur(Stage parent, Machine machine) {
        this.mainApp = parent;
        this.machine = machine;
        this.focusCategory = machine.getCategory();
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/VueFournisseur.fxml"));
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
        	
        	stage.setOnCloseRequest((WindowEvent event) -> {
        		MainApp.getState().getSCInterface().raiseLogout();
        		MainApp.getState().runCycle();
        		stage.close();
        	});
        	
        } catch (IOException e) {
		}
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
        			Pane n = Utils.addNewArticle(p, c2);
        			n.setOnMousePressed((MouseEvent e) -> { focusCategory = c2; initialize(stage); });
        		}
        	}
        	
        	Pane n = Utils.addNewArticle(p, "Catégorie");
    		n.setOnMousePressed((MouseEvent e) -> {
    			Category cc = CreateNewCategory.getNewArticle(stage, machine);
    			if( cc != null ) {      	    		
    				focusCategory = tmpC.addCategory(cc);
    				initialize(stage);
    			}
    		});
    		
        	if( c.getArticles() != null ) {
        		for( Article a : c.getArticles() ) {
        			Utils.addNewArticle(p, a);
        		}
    		}
        	n = Utils.addNewArticle(p, "Article");
        	n.setOnMousePressed((MouseEvent e) -> {
        		Article a = CreateNewArticle.getNewArticle(stage, machine);
        		if( a != null ) {
        			focusCategory =  tmpC.addArticle(a);
        			initialize(stage);
        		}
        	});
        	tab.setContent(p);
        	tp.getTabs().add( tab );
        	
        	c = c.getParent();
    	} while( c != null );    	
    }
    @FXML
    private void handleExit() {
    	MainApp.getState().getSCInterface().raiseLogout();
		MainApp.getState().runCycle();
		stage.close();
    }
    

    @FXML
    private void OnClick_NewMP() {
    	CreateNewMP mp = new CreateNewMP(stage, machine);
    }
    @FXML
    private void OnClick_SwitchModule(Event e) {
    	boolean enable = ((Node)e.getTarget()).getId().equals("moduleEnable") ;
    	
    	ChoiceDialog<Module> dialog = new ChoiceDialog<Module>();
    	
    	for( Module m : machine.getModules() ) {
    		if( !m.isAvalaible() && enable ) 
    			dialog.getItems().add(m);
    		else if( m.isAvalaible() && !enable ) 
    			dialog.getItems().add(m);
    	}
    	
    	dialog.setTitle("Choisissez un module");
    	dialog.setHeaderText("Choisissez un module");
    	Optional<Module> result = dialog.showAndWait();
    	if (result.isPresent()){
    		Module res = result.get();
    		
    		if( res instanceof Stockage && ((Stockage) res).getContains() == null ) {
    			// Type de stockage générique -> On désactive tout les modules identiques.
    			for( Module m : machine.getModules() ) {
    				if( res.getClass().equals(m.getClass()) ) {
    					m.setAvalaible( enable );
    				}
    			}
    		}
    		else {
    			res.setAvalaible( enable );
    		}
    	}
    }
    @FXML
    private void OnClick_AddMP() {
    	GestionStock s = new GestionStock(stage, machine);
    }
    @FXML
    private void OnClick_VueUtilisateur() {
    	MainApp.getState().getSCInterface().raiseMaintenance();
		MainApp.getState().runCycle();
		
    	VueUtilisateur v = new VueUtilisateur(stage, machine);
    }
    
}