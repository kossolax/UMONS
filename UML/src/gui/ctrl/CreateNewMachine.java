package gui.ctrl;

import java.io.IOException;

import framework.Category;
import framework.Machine;
import framework.modules.Boiler;
import framework.modules.Module;
import framework.modules.Water;
import framework.payement.Carte;
import framework.payement.Coin;
import framework.payement.Payment;
import framework.payement.Token;
import framework.stockage.Classic;
import framework.stockage.Cooling;
import framework.stockage.Freeze;
import framework.stockage.Stockage;
import gui.MainApp;
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
import javafx.stage.WindowEvent;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
@SuppressWarnings({"unused"})
public class CreateNewMachine  {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private static Machine returnValue;
	
	public static Machine getNewMachine(Stage mainApp) {
		returnValue = null;
		CreateNewMachine form = new CreateNewMachine(mainApp);
		
		
		MainApp.getState().getSCInterface().raiseLogout();
		MainApp.getState().runCycle();
		return returnValue;
	}
	
	private CreateNewMachine(Stage mainApp) {
        this.mainApp = mainApp;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/CreateNewMachine.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle("Cr�ation d'une nouvelle machine");
        	stage.initModality(Modality.APPLICATION_MODAL);
        	stage.setResizable(false);        	
        	stage.showAndWait();
        	
        } catch (IOException e) {
		}
    }
    @FXML
    private void OnClick_CheckBox(ActionEvent event) {
    	
    	String ID = ((Control)event.getSource()).getId();
    	boolean selected = ((CheckBox)event.getSource()).isSelected();
    	
    	if( ID.equals("btnCafe") ) {
    		((CheckBox)scene.lookup("#btnEau")).setSelected(selected);
    		((CheckBox)scene.lookup("#btnChauffeEau")).setSelected(selected);
    		((CheckBox)scene.lookup("#btnClassic")).setSelected(selected);
    		
    		((CheckBox)scene.lookup("#btnEau")).setDisable(selected);
    		((CheckBox)scene.lookup("#btnChauffeEau")).setDisable(selected);
    		((CheckBox)scene.lookup("#btnClassic")).setDisable(selected);
	        
    	}
    	else if( (ID.equals("btnConfiserie") && !((CheckBox)scene.lookup("#btnBoisson")).isSelected()) || 
    			(ID.equals("btnBoisson") && !((CheckBox)scene.lookup("#btnConfiserie")).isSelected()) 
    			) {
    		
    		((CheckBox)scene.lookup("#btnFrigo")).setSelected(selected);
    		((CheckBox)scene.lookup("#btnFrigo")).setDisable(selected);
    		
    	}
    }
    @FXML
    private void OnClick_Validate(ActionEvent event) {
    	String name = ((TextField)scene.lookup("#txtNomMachine")).getText().trim();
    	if( name.length() <= 3 ) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Le nom de la machine doit faire plus de 3 caract�res");
    		alert.show();
    		return;
    	}
    	
    	Machine machine = new Machine(name);
    	
    	if( ((CheckBox)scene.lookup("#btnEau")).isSelected() )
    		machine.addModule( new Water() );
    	if( ((CheckBox)scene.lookup("#btnChauffeEau")).isSelected() )
    		machine.addModule( new Boiler() );
    	
    	if( ((CheckBox)scene.lookup("#btnCongelateur")).isSelected() )
			machine.addModule( new Freeze(Integer.MAX_VALUE) );
		if( ((CheckBox)scene.lookup("#btnFrigo")).isSelected() )
			machine.addModule( new Cooling(Integer.MAX_VALUE) );
		if( ((CheckBox)scene.lookup("#btnClassic")).isSelected() )
			machine.addModule( new Classic(Integer.MAX_VALUE) );
		
		if( ((CheckBox)scene.lookup("#btnPiece")).isSelected() ) {
			Coin c = new Coin(machine);
			c.addModule(1);
			c.addModule(2);
			c.addModule(5);
			c.addModule(10);
			c.addModule(20);
			c.addModule(50);
			c.addModule(100);
			c.addModule(200);
			machine.addModule( c );
		}
		if( ((CheckBox)scene.lookup("#btnCarte")).isSelected() )
			machine.addModule( new Carte(1000) );
		if( ((CheckBox)scene.lookup("#btnToken")).isSelected() )
			machine.addModule( new Token() );
    	
		int stock = 0, pay = 0;
		for( Module m : machine.getModules() ) {
			if( m instanceof Stockage )
				stock++;
			if( m instanceof Payment )
				pay++;
		}
		if( stock == 0 ) {
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Aucun module de stockage n'a �t� s�lectionn�");
    		alert.show();
    		return;
		}
		if( pay == 0 ) {
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Aucun module de paiement n'a �t� s�lectionn�");
    		alert.show();
    		return;
		}
		
		machine.setMainCategory(new Category("Pr�conis�"));
		returnValue = machine;
		stage.close();
    }
    
    @FXML
    private void handleExit() {
    	stage.close();
    }
}