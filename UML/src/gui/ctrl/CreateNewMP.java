package gui.ctrl;

import java.io.IOException;

import framework.Category;
import framework.Machine;
import framework.RawMaterial;
import framework.modules.Boiler;
import framework.modules.Module;
import framework.modules.Water;
import framework.payement.Carte;
import framework.payement.Token;
import framework.stockage.Classic;
import framework.stockage.Cooling;
import framework.stockage.Freeze;
import framework.stockage.Stockage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateNewMP  {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	private static Stockage returnValue;
	
	public static Stockage getNewMP(Stage mainApp, Machine machine) {
		returnValue = null;
		CreateNewMP form = new CreateNewMP(mainApp, machine);
		return returnValue;
	}
	
	private CreateNewMP(Stage mainApp, Machine machine) {
        this.mainApp = mainApp;
        this.machine = machine;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/CreateNewMP.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle("Ajouter une matière première");
        	stage.initModality(Modality.APPLICATION_MODAL);
        	stage.setResizable(false);
        	
        	initialize(stage);
        	
        	stage.showAndWait();
        	
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private void initialize(Stage stage) {
		ComboBox<RawMaterial.TypeOfRawMaterial> cb = (ComboBox<RawMaterial.TypeOfRawMaterial>)scene.lookup("#type");
		cb.getItems().add(RawMaterial.TypeOfRawMaterial.centiliter);
		cb.getItems().add(RawMaterial.TypeOfRawMaterial.gram);
		cb.getItems().add(RawMaterial.TypeOfRawMaterial.unity);
		
		ComboBox<Stockage> cb2 = (ComboBox<Stockage>)scene.lookup("#storage");
		for( Module m : machine.getModules() ) {
			if( m instanceof Stockage ) {
				cb2.getItems().add((Stockage)m);
			}
		}
	}
    @FXML
    private void OnClick_Validate(ActionEvent event) {
    	Stockage s;
    	String name = ((TextField)scene.lookup("#name")).getText().trim();
    	
    }
    @FXML
    private void handleExit() {
    	stage.close();
    }
}