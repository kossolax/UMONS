package gui;


import org.yakindu.scr.RuntimeService;
import org.yakindu.scr.TimerService;
import org.yakindu.scr.vendingmachine.VendingMachineStatemachine;

import gui.ctrl.InitWindow;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application {
	
	private static VendingMachineStatemachine state;
	
    public static void main(String[] args) {
    	
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {		
		
    	state = new VendingMachineStatemachine();
    	state.setTimer(new TimerService());
    	state.init();
    	state.enter();
    	RuntimeService.getInstance().registerStatemachine(state, 100);
    	state.runCycle();
    	
    	InitWindow w = new InitWindow(stage);
    }
    public static VendingMachineStatemachine getState() {
    	return state;
    }
}