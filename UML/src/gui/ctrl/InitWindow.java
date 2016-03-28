package gui.ctrl;

import java.io.IOException;
import framework.Machine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
			e.printStackTrace();
		}
    }

	@SuppressWarnings("unchecked")
	@FXML
    private void OnClick_Create() {
    	Machine machine = CreateNewMachine.getNewMachine(mainApp);
    	if( machine != null ) {
    		((ComboBox<Machine>)scene.lookup("#CBMachineList")).getItems().add(machine);
    	}
    }
	@SuppressWarnings("unchecked")
	@FXML
    private void OnClick_Load() {
		Machine machine = ((ComboBox<Machine>)scene.lookup("#CBMachineList")).getValue();
    	if( machine != null ) {
    		VueFournisseur f = new VueFournisseur(mainApp, machine);
    	}
    }
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
}