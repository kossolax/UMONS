package gui.ctrl;

import java.io.IOException;

import framework.Machine;
import gui.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
@SuppressWarnings({"unused", "unchecked"})
public class InitWindow extends Pane {    
    
	private Stage mainApp;
	private Scene scene;
	
    public InitWindow(Stage stage) {
        this.mainApp = stage;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/InitWindow.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage.setTitle("UMONS - Framework Distributeur");
        	stage.setScene(scene);
        	stage.setResizable(false);
        	
        	
        	stage.show();
        } catch (IOException e) {

        }
    }

	@FXML
    private void OnClick_Create() {
		
		MainApp.getState().getSCInterface().setLoginType(1);
		MainApp.getState().getSCInterface().raiseLogin();
		MainApp.getState().runCycle();
		
    	Machine machine = CreateNewMachine.getNewMachine(mainApp);
    	if( machine != null ) {
    		((ComboBox<Machine>)scene.lookup("#CBMachineList")).getItems().add(machine);
    		((ComboBox<Machine>)scene.lookup("#CBMachineList")).setValue(machine);
    	}
    }
	@FXML
    private void OnClick_Load() {
		
		MainApp.getState().getSCInterface().setLoginType(0);
		MainApp.getState().getSCInterface().raiseLogin();
		MainApp.getState().runCycle();
		
		Machine machine = ((ComboBox<Machine>)scene.lookup("#CBMachineList")).getValue();
    	if( machine != null ) {
    		VueFournisseur f = new VueFournisseur(mainApp, machine);
    	}
    }
    
}