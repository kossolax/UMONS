package gui;


import org.yakindu.scr.RuntimeService;
import org.yakindu.scr.TimerService;
import org.yakindu.scr.vendingmachine.VendingMachineStatemachine;

import gui.ctrl.InitWindow;
import javafx.application.Application;
import javafx.stage.Stage;

@SuppressWarnings({"unused"})
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
    	RuntimeService.getInstance().registerStatemachine(state, 100);
    	
    	
    	state.enter();
    	state.runCycle();
    	
    	InitWindow w = new InitWindow(stage);
    }
    @Override
    public void stop() {
    	RuntimeService.getInstance().cancelTimer();
    	state.exit();
    	state = null;
    	System.exit(0);
    }
    public static VendingMachineStatemachine getState() {
    	return state;
    }
}