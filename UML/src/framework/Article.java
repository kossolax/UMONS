package framework;

import java.util.Iterator;

public class Article {
	private String name;
	private double price;
	private Recipe recipe;
	
	/**
	 * Checks if Article is avalaible.
	 *
	 * @return true, if is avalaible
	 */
	public boolean isAvalaible() {
		boolean avaialable = true;
		Iterator<RawMaterial> itr = recipe.getRecipe().iterator();
		while( itr.hasNext() && avaialable ) {
			RawMaterial materials = itr.next();
			
			if( materials.getStock().getAmount() < materials.getAmount() ) {
				avaialable = false;
			}
		}
		
		return avaialable;
	}

	/**
	 * Delivery.
	 *
	 * @return true, if successful
	 */
	public Article delivery() {
		Iterator<RawMaterial> itr = recipe.getRecipe().iterator();
		while( itr.hasNext() ) {
			RawMaterial materials = itr.next();
			try {
				materials.getStock().changeAmount( -materials.getAmount() );
				
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