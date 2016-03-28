package gui.ctrl;

import java.io.IOException;
import framework.Article;
import framework.Category;
import framework.modules.Module;
import javafx.fxml.FXMLLoader;
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
			e.printStackTrace();
		}
		
		return n;
    }
}
