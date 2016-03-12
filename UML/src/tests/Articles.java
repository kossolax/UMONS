package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import framework.*;
import framework.RawMaterial.*;
import framework.modules.*;

public class Articles {
	
	
	@Test
	public void testIsAvalaible() {
		Stockage stock = new Classic(8, 25);
		Collection<RawMaterial> recette = new ArrayList<RawMaterial>();
		recette.add(new RawMaterial(5, TypeOfRawMaterial.gram, stock));
		Article cafe = new Article("Café", 1.0, new Recipe(recette));
		
		assertTrue(cafe.isAvalaible());
		
		Collection<RawMaterial> recette2 = new ArrayList<RawMaterial>();
		recette2.add(new RawMaterial(10, TypeOfRawMaterial.gram, stock));
		Article cafe2 = new Article("Café fort", 1.0, new Recipe(recette2));
		
		assertFalse(cafe2.isAvalaible());
	}

	@Test
	public void testDelivery() {
		
		Stockage stock = new Classic(8, 25);
		Collection<RawMaterial> recette = new ArrayList<RawMaterial>();
		recette.add(new RawMaterial(5, TypeOfRawMaterial.gram, stock));
		Article cafe = new Article("Café", 1.0, new Recipe(recette));

		assertTrue(cafe.delivery() != null);
		assertTrue(stock.getAmount() == 3);
		assertTrue(cafe.delivery() == null);
		
	}
}
