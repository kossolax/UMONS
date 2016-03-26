package gui.ctrl;

import java.io.IOException;
import framework.Article;
import framework.Category;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class Utils {
	// TODO: Fusionner Article et Category
	public static Pane addNewArticle(FlowPane p, Article a) {
    	Pane n = null;
		try {
			n = (Pane)FXMLLoader.load(Utils.class.getResource("/gui/views/Article.fxml"));
			((ImageView)n.lookup("#img")).setImage(new Image(a.getImage().toURI().toString()));
			((Label)n.lookup("#txt")).setText(a.getName());		
			p.getChildren().add(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return n;
    }
	public static Pane addNewCategory(FlowPane p, Category c) {
    	Pane n = null;
		try {
			n = (Pane)FXMLLoader.load(Utils.class.getResource("/gui/views/Article.fxml"));
			((ImageView)n.lookup("#img")).setImage(new Image(""+Utils.class.getResource("/data/img/add.png")));
			((Label)n.lookup("#txt")).setText(c.getName());		
			p.getChildren().add(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return n;
    }
	
	
	public static Pane addNewString(FlowPane p, String str) {
    	Pane n = null;
		try {
			n = (Pane)FXMLLoader.load(Utils.class.getResource("/gui/views/Article.fxml"));
			((ImageView)n.lookup("#img")).setImage(new Image(""+Utils.class.getResource("/data/img/add.png")));
			((Label)n.lookup("#txt")).setText(str);		
			p.getChildren().add(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return n;
    }
}
