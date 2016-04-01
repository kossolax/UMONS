package gui.ctrl;

import java.io.File;
import java.io.IOException;
import framework.Category;
import framework.Machine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
@SuppressWarnings({"unused"})
public class CreateNewCategory  {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	private Category category;
	private static Category returnValue;
	
	public static Category getNewArticle(Stage mainApp, Machine machine) {
		returnValue = null;
		CreateNewCategory form = new CreateNewCategory(mainApp, machine);
		return returnValue;
	}
	public CreateNewCategory(Stage mainApp, Machine machine) {
        this.mainApp = mainApp;
        this.machine = machine;
        this.category = new Category("");
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/CreateNewCategory.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle("Ajouter une catégorie");
        	stage.initModality(Modality.APPLICATION_MODAL);
        	stage.setResizable(false);
        	
        	stage.showAndWait();
        	
        } catch (IOException e) {
		}
    }
	
	@FXML
    private void OnClick_Image() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selectionnez une image");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));                 
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Toutes les images", "*.jpg", "*.gif", "*.bmp", "*.png"),
			new FileChooser.ExtensionFilter("JPG", "*.jpg"),
			new FileChooser.ExtensionFilter("GIF", "*.gif"),
			new FileChooser.ExtensionFilter("BMP", "*.bmp"),
			new FileChooser.ExtensionFilter("PNG", "*.png")
		);
		File file = fileChooser.showOpenDialog(stage);
		if( file != null ) {
			category.setImage(file);
			((ImageView)scene.lookup("#image")).setImage(new Image(file.toURI().toString()));
		}
    }
	@FXML
    private void OnClick_Save() {
		try {
			String name = ((TextField)scene.lookup("#name")).getText().trim();
	    	if( name.length() == 0 )
	    		throw new Exception("nom de la catégorie");
	    	if( category.getImage() == null )
	    		throw new Exception("Image");
    		
    		category.setName(name);
    		returnValue = category;
			stage.close();
		
    	} catch ( Exception e ) {
    		
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Veuillez compléter le champs "+e.getMessage());
    		alert.show();
    		return;
    	}
    }
    @FXML
    private void handleExit() {
    	stage.close();
    }
}