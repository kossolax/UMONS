package framework;

import java.io.File;
import java.util.*;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
public class Category {
	private Collection<Article> belong;
	private Collection<Category> subCategories;
	private Category parent;
	private String name;
	private File image;
	
	public Category(String name) {
		this.name = name;
		this.belong = new ArrayList<Article>();
		this.subCategories = new ArrayList<Category>();
	}
	
	public Category addArticle(Article a) {
		belong.add(a);
		return this;
	}
	public Collection<Article> getArticles() {
		return belong;
	}
	public Category addCategory(Category c) {
		subCategories.add(c);
		c.setParent(this);
		return c;
	}
	public void setParent(Category c) {
		this.parent = c;
	}
	public Category getParent() {
		return this.parent;
	}
	public Collection<Category> getCategories() {
		return subCategories;
	}
	public String toString() {
		return name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}
}