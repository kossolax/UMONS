package framework;

import java.util.Iterator;

public class Article {
	private String name;
	private double price;
	private Recipe recipe;
	
	public Article(String name, double price, Recipe recipe) {
		this.name = name;
		this.price = price;
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
				
				materials.getStock().Substract(materials);
			} catch ( Exception e ) {
				// TODO: Que faire dans ce cas? Il était sensé avoir la bonne quantité, mais...
				return null;
			}
		}
		return this;
	}

	public double getPrice() {
		return price;
	}
}