package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RootLayoutController {
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void OnClick_Pay() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("titre");
    	alert.setHeaderText("Euhhh");
    	alert.setContentText("blablala");

    	alert.showAndWait();
    }
    
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}