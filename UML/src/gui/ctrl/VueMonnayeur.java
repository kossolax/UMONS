package gui.ctrl;

import java.io.IOException;
import framework.Machine;
import framework.payement.Coin;
import framework.payement.Payment;
import gui.MainApp;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings({"unused"})
public class VueMonnayeur  {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	
	public VueMonnayeur(Stage mainApp, Machine machine) {
        this.mainApp = mainApp;
        this.machine = machine;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/Monnayeur.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle("Monnayeur");
        	stage.initModality(Modality.APPLICATION_MODAL);
        	stage.setResizable(false);
        	
        	initialize(stage);
        	
        	stage.show();
        	
        } catch (IOException e) {
		}
    }
	
	private void initialize(Stage stage) {
		
	}
	@FXML
	private void onClick2Euro(Event e) {
		Payment p = (Payment)((Node)e.getTarget()).getUserData();
		Coin c = (Coin)p;
		
    		if( c.insertPiece(5) ) {
    			MainApp.getState().getSCInterface().setPiece( 5);
    			MainApp.getState().getSCInterface().raiseInsertPiece();
    			MainApp.getState().runCycle();
    		}
    		//.updatePayement();
    }
    @FXML
    private void handleExit() {
    	stage.close();
    }
}