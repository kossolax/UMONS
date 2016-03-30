package gui.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import framework.Article;
import framework.Category;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class Utils {
	
	public static Pane addNewArticle(FlowPane p, Object a) {
    	Pane n = null;
		try {
			n = (Pane)FXMLLoader.load(Utils.class.getResource("/gui/views/Article.fxml"));
			
			if( a instanceof Article ) {
				((ImageView)n.lookup("#img")).setImage(new Image(((Article)a).getImage().toURI().toString()));
				((Label)n.lookup("#txt")).setText(((Article)a).getName());
			}
			else if( a instanceof Category ) {
				((ImageView)n.lookup("#img")).setImage(new Image(((Category)a).getImage().toURI().toString()));
				((Label)n.lookup("#txt")).setText(((Category)a).getName());
			}
			else if( a instanceof String ) {
				((ImageView)n.lookup("#img")).setImage(new Image(""+Utils.class.getResource("/data/img/add.png")));
				((Label)n.lookup("#txt")).setText((String)a);	
			}
			
			p.getChildren().add(n);
			
		} catch (IOException e) {
			
		}
		
		return n;
    }
	@SuppressWarnings("unchecked")
	public static void showPopUp(Object collection) {
		if( collection == null || collection instanceof Boolean || collection instanceof String )
			return;
		
		if( !(collection instanceof Collection) ) {
			ArrayList<Object> c = new ArrayList<Object>();
			c.add(collection);
			collection = c;
		}
		
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.setTitle("Distributeur");
		dialog.setHeaderText("Vous avez reçu...:");
		FlowPane p = new FlowPane();
		p.setMaxWidth( dialog.getDialogPane().getMaxWidth() );
		
		for(Object o : (Collection<Object>)collection ) {

			
			ImageView n = new ImageView();
			Image m = null;
			
			if( o instanceof Integer )
				m = new Image(""+Utils.class.getResource("/data/img/"+o+".gif"));
			else if( o instanceof Article )
				m = new Image(((Article)o).getImage().toURI().toString());
			else
				continue;
			
			n.maxWidth(20.0);
			n.maxHeight(20.0);
			
			n.setImage(m);
			p.getChildren().add(n);
		}
		
		if( p.getChildren().size() == 0 )
			return;
		
		
		dialog.getDialogPane().setContent(p);
		dialog.showAndWait();
	}
}
