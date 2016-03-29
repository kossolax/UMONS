package gui.ctrl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.security.auth.callback.Callback;

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
import javafx.event.EventHandler;
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
import javafx.util.StringConverter;

public class CreateNewArticle  {    
    
	private Stage mainApp, stage;
	private Scene scene;
	private Machine machine;
	private Article article;
	private static Article returnValue;
	
	public static Article getNewArticle(Stage mainApp, Machine machine) {
		returnValue = null;
		CreateNewArticle form = new CreateNewArticle(mainApp, machine);
		return returnValue;
	}
	public CreateNewArticle(Stage mainApp, Machine machine) {
        this.mainApp = mainApp;
        this.machine = machine;
        this.article = new Article(new Recipe(new ArrayList<RawMaterial>()));
        
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
        	
        	stage.showAndWait();
        	
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private void initialize(Stage stage) {
		TableView<RawMaterial> table = ((TableView)scene.lookup("#table"));
	
		table.setEditable(true);

        
		if( table.getColumns().isEmpty() ) {
			table.setPlaceholder(new Label("Double clique pour ajouter une matière première"));
			TableColumn mp = new TableColumn("Name");
			TableColumn max = new TableColumn("max");
			max.setCellValueFactory(
		            new PropertyValueFactory<RawMaterial, Integer>("max"));
			max.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>(){

			        @Override
			        public String toString(Integer object) {
			            return object.toString();
			        }

			        @Override
			        public Integer fromString(String string) {
			            return Integer.parseInt(string);
			        }

			    }));
				max.setOnEditCommit(
	                     new EventHandler<CellEditEvent<RawMaterial, Integer>>() {
	                         public void handle(CellEditEvent<RawMaterial, Integer> t) {
	                             ((RawMaterial) t.getTableView().getItems().get(
	                                     t.getTablePosition().getRow())
	                                     ).setMax((int)t.getNewValue());
	                         }
	                     });
			
			TableColumn min = new TableColumn("min");
			min.setCellValueFactory(
	            new PropertyValueFactory<RawMaterial, Integer>("min"));
			min.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>(){

		        @Override
		        public String toString(Integer object) {
		            return object.toString();
		        }

		        @Override
		        public Integer fromString(String string) {
		            return Integer.parseInt(string);
		        }

		    }));
			 min.setOnEditCommit(
                     new EventHandler<CellEditEvent<RawMaterial, Integer>>() {
                         public void handle(CellEditEvent<RawMaterial, Integer> t) {
                             ((RawMaterial) t.getTableView().getItems().get(
                                     t.getTablePosition().getRow())
                                     ).setMin((int)t.getNewValue());
                         }
                     });
                     
			mp.setCellValueFactory(new PropertyValueFactory<>("name"));
			//min.setCellValueFactory(new PropertyValueFactory<>("min"));	
			//max.setCellValueFactory(new PropertyValueFactory<>("max"));
			
			table.getColumns().addAll(mp, min, max);
		}
		
		ObservableList<RawMaterial> data = FXCollections.observableArrayList(article.getRecipe());
		table.setItems(data);
table.setEditable(true);
        
        if( article.getImage() != null ) {
        	((ImageView)scene.lookup("#image")).setImage(new Image(article.getImage().toURI().toString()));
        }
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
				article.getRecipe().add(new RawMaterial(result.get()));
				initialize(stage);
			}
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
			article.setImage(file);
			initialize(stage);
		}
    }
	@FXML
    private void OnClick_Save() {
		String name;
		int price;
		
		try {
	    	name = ((TextField)scene.lookup("#name")).getText().trim();
	    	if( name.length() == 0 )
	    		throw new Exception("nom de l'article");    	
	    	price = Integer.parseInt(((TextField)scene.lookup("#price")).getText());
    		if( price <= 0.0 )
    			throw new Exception("prix de l'article"); 
    		
	    	article.setName(name);
			article.setPrice(price);
			returnValue = article;
			stage.close();
			
    	} catch ( NumberFormatException e ) {
    		
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Veuillez corriger le champs quantité");
    		alert.show();
    		return;
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