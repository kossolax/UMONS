package gui.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateNewArticle  {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	private Category category;
	private Recipe recipe;
	
	public CreateNewArticle(Stage mainApp, Machine machine, Category category) {
        this.mainApp = mainApp;
        this.machine = machine;
        this.category = category;
        this.recipe = new Recipe(new ArrayList<RawMaterial>());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/views/CreateNewArticle.fxml"));
        fxmlLoader.setController(this);
        
        try {
            scene = new Scene(fxmlLoader.load()); 
        	stage = new Stage();
        	stage.setScene(scene);
        	stage.setTitle("Ajouter un article");
        	stage.initModality(Modality.APPLICATION_MODAL);
        	stage.setResizable(false);
        	
        	initialize(stage);
        	
        	stage.show();
        	
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private void initialize(Stage stage) {
		TableView<RawMaterial> table = ((TableView)scene.lookup("#table"));
		table.setPlaceholder(new Label("Double clique pour ajouter une matière première"));
		table.setEditable(true);
		TableColumn mp = new TableColumn("Matière première");
		TableColumn min = new TableColumn("min");
		TableColumn max = new TableColumn("max");
		mp.setCellValueFactory(new PropertyValueFactory<>("name"));
		min.setCellValueFactory(new PropertyValueFactory<>("min"));	
		max.setCellValueFactory(new PropertyValueFactory<>("max"));
		
		
		ObservableList<RawMaterial> data = FXCollections.observableArrayList(recipe.getRecipe());
		table.setItems(data);
        table.getColumns().addAll(mp, min, max);

	}
	
	@FXML
    private void OnClick_Table(MouseEvent e) {
		Node node = ((Node) e.getTarget()).getParent();
		if(e.getButton().equals(MouseButton.PRIMARY) && !(node instanceof TableRow) ) {
			
			Collection<RawMaterial> lst = new ArrayList<RawMaterial>();
			for( Module m : machine.getModules() ) {
				if( m instanceof Stockage ) {
					if( ((Stockage) m).getContains() != null )
						lst.add(((Stockage)m).getContains());
				}
			}

			ChoiceDialog<RawMaterial> dialog = new ChoiceDialog<RawMaterial>(lst.iterator().next(), lst);
			dialog.setTitle("Choisissez une matière première");
			dialog.setHeaderText("Choisissez une matière première");
			Optional<RawMaterial> result = dialog.showAndWait();
			if (result.isPresent()){
			    TableView<RawMaterial> table = ((TableView)scene.lookup("#table"));
			    table.getItems().add(result.get());
			}
		}
	}
    @FXML
    private void handleExit() {
    	stage.close();
    }
}