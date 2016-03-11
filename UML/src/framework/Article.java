package framework;

import java.util.Iterator;

public class Article {
	private String name;
	private int price;
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

	public void delivery() {
	}
}