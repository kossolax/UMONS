package gui.ctrl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import framework.Article;
import framework.Category;
import framework.Machine;
import framework.RawMaterial;
import framework.Recipe;
import framework.modules.Boiler;
import framework.modules.Module;
import framework.modules.Water;
import framework.payement.Carte;
import framework.payement.Token;
import framework.stockage.Classic;
import framework.stockage.Cooling;
import framework.stockage.Freeze;
import framework.stockage.Stockage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
        	stage.setTitle("Ajouter un article");
        	stage.initModality(Modality.APPLICATION_MODAL);
        	stage.setResizable(false);
        	
        	stage.showAndWait();
        	
        } catch (IOException e) {
			e.printStackTrace();
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