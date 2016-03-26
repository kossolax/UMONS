package gui;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import gui.ctrl.InitWindow;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	InitWindow w = new InitWindow(stage);
    }
}