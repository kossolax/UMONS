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
	
	public CreateNewMP(Stage mainApp, Machine machine) {
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
        	
        	stage.show();
        	
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
		ComboBox<RawMaterial> cb3 = (ComboBox<RawMaterial>)scene.lookup("#stockage");
		
		for( Module m : machine.getModules() ) {
			if( m instanceof Stockage ) {
				if( ((Stockage) m).getContains() == null )
					cb2.getItems().add((Stockage)m);
				else
					cb3.getItems().add(((Stockage)m).getContains());
			}
		}
	}
	@FXML
    private void OnClick_Add(ActionEvent event) {
    	String name;
    	RawMaterial.TypeOfRawMaterial type = null;
    	Stockage s = null;
    	int qt;
    	
    	try {
	    	name = ((TextField)scene.lookup("#name")).getText().trim();
	    	if( name.length() == 0 )
	    		throw new Exception("nom de la matière première");
	    	
	    	type = ((ComboBox<RawMaterial.TypeOfRawMaterial>)scene.lookup("#type")).getValue();
	    	if( type == null )
	    		throw new Exception("type de matière première");
	    	
	    	s = ((ComboBox<Stockage>)scene.lookup("#storage")).getValue();
	    	if( s == null )
	    		throw new Exception("type de stockage");
    	
    		qt = Integer.parseInt(((TextField)scene.lookup("#amount")).getText());
    		
    		Stockage ns = s.getClass().newInstance();

    		ns.setAvalaible(true);    		
    		RawMaterial rw = new RawMaterial(name, qt, type, ns);
    		ns.Add(rw.getAmount());
        	machine.addModule((Module)ns);
        	
        	ComboBox<RawMaterial> cb2 = (ComboBox<RawMaterial>)scene.lookup("#stockage");
    		cb2.getItems().add(ns.getContains());
    		
    		((TextField)scene.lookup("#name")).setText("");
        	
    	} catch ( NumberFormatException e ) {
    		
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Veuillez corriger le champs quantité");
    		alert.show();
    		return;
    	} catch ( Exception e ) {
    		
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Veuillez compléter le champs "+e.getMessage());
    		alert.show();
    		return;
    	}
    	
    }
	@FXML
    private void OnClick_Remove(ActionEvent event) {
		ComboBox<RawMaterial> cb = (ComboBox<RawMaterial>)scene.lookup("#stockage");
		RawMaterial rw = cb.getValue();
		if( rw != null && rw.getStock() != null ) {
			machine.getModules().remove((Module)rw.getStock());
			cb.getItems().remove(rw);
		}
    	
    }
    @FXML
    private void handleExit() {
    	stage.close();
    }
}