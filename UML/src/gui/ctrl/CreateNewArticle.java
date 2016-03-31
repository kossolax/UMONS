package gui.ctrl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import framework.Article;
import framework.Machine;
import framework.RawMaterial;
import framework.Recipe;
import framework.modules.Boiler;
import framework.modules.Module;
import framework.modules.Water;
import framework.stockage.Stockage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
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

@SuppressWarnings({"unused", "unchecked"})
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
		TableView<RawMaterial> table = ((TableView<RawMaterial>)scene.lookup("#table"));
		table.setEditable(true);
        
		if( table.getColumns().isEmpty() ) {
			table.setPlaceholder(new Label("Aucune matière première."));
			
			TableColumn<RawMaterial, String> mp = new TableColumn<RawMaterial, String>("Name");
			TableColumn<RawMaterial, Integer> min = new TableColumn<RawMaterial, Integer>("min");
			TableColumn<RawMaterial, Integer> max = new TableColumn<RawMaterial, Integer>("max");
			
			mp.setCellValueFactory(new PropertyValueFactory<>("name"));
			mp.setMinWidth(150.0);
			
			max.setCellValueFactory(new PropertyValueFactory<RawMaterial, Integer>("max"));
			max.setCellFactory(TextFieldTableCell.forTableColumn(Utils.IntToString()));
			max.setOnEditCommit(new EventHandler<CellEditEvent<RawMaterial, Integer>>() {
				public void handle(CellEditEvent<RawMaterial, Integer> t) {
					((RawMaterial) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMax((int)t.getNewValue());
					table.refresh();
				}
			});
			
			min.setCellValueFactory(new PropertyValueFactory<RawMaterial, Integer>("min"));
			min.setCellFactory(TextFieldTableCell.forTableColumn(Utils.IntToString()));
			min.setOnEditCommit(new EventHandler<CellEditEvent<RawMaterial, Integer>>() {
				public void handle(CellEditEvent<RawMaterial, Integer> t) {
					((RawMaterial) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMin((int)t.getNewValue());
					table.refresh();
				}
			});
				
			table.getColumns().addAll(mp, min, max);
		}
		
		ObservableList<RawMaterial> data = FXCollections.observableArrayList(article.getRecipe());
		table.setItems(data);
		table.setEditable(true);
        
        if( article.getImage() != null ) {
        	((ImageView)scene.lookup("#image")).setImage(new Image(article.getImage().toURI().toString()));
        }
        
        ((CheckBox)scene.lookup("#btnEau")).getParent().setVisible(false);
        ((CheckBox)scene.lookup("#btnChauffeEau")).getParent().setVisible(false);
        
        for( Module m : machine.getModules() ) {
        	if( m instanceof Water )
        		((CheckBox)scene.lookup("#btnEau")).getParent().setVisible(true);
        	else if( m instanceof Boiler )
        		((CheckBox)scene.lookup("#btnChauffeEau")).getParent().setVisible(true);
        }
	}
	
	@FXML
    private void OnClick_AddMaterial() {
		Collection<RawMaterial> lst = new ArrayList<RawMaterial>();
		for( Module m : machine.getModules() ) {
			if( m instanceof Stockage ) {
				if( ((Stockage) m).getContains() != null )
					lst.add(((Stockage)m).getContains());
			}
		}
		
		if( lst.size() == 0 ) {
			Alert alert = new Alert(AlertType.ERROR);
	    	alert.setTitle("Erreur");
	    	alert.setHeaderText("Vous n'avez ajouté aucune matière première dans cette machine.");
	    	alert.show();
		}
		else {
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
		double price;
		
		try {
	    	name = ((TextField)scene.lookup("#name")).getText().trim();
	    	if( name.length() == 0 )
	    		throw new Exception("nom de l'article");    	
	    	price = Double.parseDouble(((TextField)scene.lookup("#price")).getText());
    		if( price <= 0.0 )
    			throw new Exception("prix de l'article"); 
    		if( article.getImage() == null )
    			throw new Exception("Image");
    		
    		for( Module m : machine.getModules() ) {
            	if( m instanceof Water && ((CheckBox) scene.lookup("#btnEau")).isSelected() )
            		article.addRequireModule(m);
            	else if( m instanceof Boiler && ((CheckBox) scene.lookup("#btnChauffeEau")).isSelected() )
            		article.addRequireModule(m);
    		}
				
    		
	    	article.setName(name);
			article.setPrice((int)(price*100));
			System.out.println(article.getPrice());
			returnValue = article;
			stage.close();
			
    	} catch ( NumberFormatException e ) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Erreur");
    		alert.setHeaderText("Veuillez corriger le champs prix de l'article");
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