package gui.ctrl;

import java.io.IOException;
import java.lang.reflect.Constructor;

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

public class GestionStock  {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	
	public GestionStock(Stage mainApp, Machine machine) {
        this.mainApp = mainApp;
        this.machine = machine;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/GestionStock.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle("Gestion des stocks");
        	stage.initModality(Modality.APPLICATION_MODAL);
        	stage.setResizable(false);
        	
        	initialize(stage);
        	
        	stage.show();
        	
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private void initialize(Stage stage) {
		ComboBox<Stockage> cb = (ComboBox<Stockage>)scene.lookup("#stockage");
		
		for( Module m : machine.getModules() ) {
			if( m instanceof Stockage ) {
				if( ((Stockage) m).getContains() != null )
					cb.getItems().add((Stockage)m);
			}
		}
	}
	@FXML
    private void OnClick_Save(ActionEvent event) {
    	try {	    	
    		Stockage s = ((ComboBox<Stockage>)scene.lookup("#stockage")).getValue();
	    	if( s == null )
	    		throw new Exception("type de stockage");
    	
	    	int qt = Integer.parseInt(((TextField)scene.lookup("#amount")).getText());
	    	if( qt > 0 )
	    		s.Add(qt);
	    	else
	    		s.Substract(-qt);
	    	
    		((TextField)scene.lookup("#amount")).setText("");
        	
    	} catch ( NumberFormatException e ) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Veuillez corriger le champs quantité");
    		alert.show();
    		return;
    	} catch ( Exception e ) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Le stock dépasse les limites autorisées");
    		alert.show();
    		return;
    	}
    	
    }
    @FXML
    private void handleExit() {
    	stage.close();
    }
}