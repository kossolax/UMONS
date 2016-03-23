package gui.ctrl;

import java.io.IOException;

import framework.Category;
import framework.Machine;
import framework.RawMaterial;
import framework.modules.Boiler;
import framework.modules.Water;
import framework.payement.Carte;
import framework.payement.Token;
import framework.stockage.Classic;
import framework.stockage.Cooling;
import framework.stockage.Freeze;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateNewMP  {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private static RawMaterial returnValue;
	
	public static RawMaterial getNewMachine(Stage mainApp) {
		returnValue = null;
		CreateNewMP form = new CreateNewMP(mainApp);
		return returnValue;
	}
	
	private CreateNewMP(Stage mainApp) {
        this.mainApp = mainApp;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/CreateNewMP.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle("Ajouter une matière première");
        	stage.initModality(Modality.APPLICATION_MODAL);
        	stage.setResizable(false);
        	stage.showAndWait();
        	
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
    @FXML
    private void OnClick_Validate(ActionEvent event) {
    	
    	
    }
    
    @FXML
    private void handleExit() {
    	stage.close();
    }
}