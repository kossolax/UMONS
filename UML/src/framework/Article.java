package framework;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import framework.modules.Module;

public class Article {
	private String name;
	private int price;
	private Recipe recipe;
	private File image;
	
	
	public Article(String name, int price, Recipe recipe) {
		this.name = name;
		this.price = price;
		this.recipe = recipe;
	}
	public Article(Recipe recipe) {
		this.recipe = recipe;
	}
	
	/**
	 * Checks if Article is available.
	 *
	 * @return true, if is available
	 */
	public boolean isAvailaible() {
		return recipe.IsAvailable();
	}

	/**
	 * Delivery. Please check isAvalaible first.
	 *
	 * @return true, if successful
	 */
	public Article delivery() {
		Iterator<RawMaterial> itr = recipe.getRecipe().iterator();
		while( itr.hasNext() ) {
			RawMaterial materials = itr.next();
			try {
				if( !materials.getStock().isAvalaible() )
					throw new Exception();
				
				materials.getStock().Substract(materials.getAmount());
			} catch ( Exception e ) {
				// TODO: Que faire dans ce cas? Il était sensé avoir la bonne quantité, mais...
				return null;
			}
		}
		return this;
	}
	public Collection<RawMaterial> getRecipe() {
		return recipe.getRecipe();
	}
	public void addRequireModule(Module m) {
		recipe.addRequiredModule(m);
	}
	public void setRecipe(Recipe r) {
		recipe = r;
	}
	public int getPrice() {
		return price;
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
	public void setPrice(int price) {
		this.price = price;
	}
	public String toString() {
		return name;
	}
}