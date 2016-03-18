package gui.ctrl;

import java.io.IOException;
import framework.Machine;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
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
    	Node n = scene.lookup("#btnNewArtlce");
    	n.setOnMousePressed(new EventHandler<MouseEvent>() {
    	    public void handle(MouseEvent e) {
    	        System.out.println(e);
    	    }
    	});
    }
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
}