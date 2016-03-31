package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import framework.*;
import framework.RawMaterial.*;
import framework.modules.*;
import framework.stockage.Classic;
import framework.stockage.Stockage;

public class Articles {
	
	
	@Test
	public void testIsAvalaible() {
		try {
			Stockage stock = new Classic(1000);
			RawMaterial cafeine = new RawMaterial(5, TypeOfRawMaterial.gram, stock);
			stock.Add(cafeine.getAmount());
			
			
			Collection<RawMaterial> recette = new ArrayList<RawMaterial>();
			Recipe recipe = new Recipe(recette);
			Boiler boil = new Boiler();
			Article cafe = new Article("Café", 100, recipe);
			recipe.addRequiredModule(boil);
			recette.add(cafeine);
			
			assertTrue(cafe.isAvailaible());
			boil.setAvalaible(false);
			assertFalse(cafe.isAvailaible());
			
			Collection<RawMaterial> recette2 = new ArrayList<RawMaterial>();
			recette2.add(new RawMaterial(6, TypeOfRawMaterial.gram, stock));
			Article cafe2 = new Article("Café fort", 100, new Recipe(recette2));
			
			assertFalse(cafe2.isAvailaible());
		} catch ( Exception e ){
			fail(e.getMessage());
		}
	}

	@Test
	public void testDelivery() {
		try {
			Stockage stock = new Classic(1000);
			RawMaterial m = new RawMaterial(8, TypeOfRawMaterial.gram, stock);
			stock.Add(m.getAmount());
			
			Collection<RawMaterial> recette = new ArrayList<RawMaterial>();
			recette.add(new RawMaterial(3, TypeOfRawMaterial.gram, stock));
			Article cafe = new Article("Café", 100, new Recipe(recette));
	
			assertTrue(cafe.delivery() != null);
			assertTrue(stock.getAmount() == 5);
			assertTrue(cafe.delivery() != null);
			assertTrue(stock.getAmount() == 2);
			assertTrue(cafe.delivery() == null);
			
		} catch ( Exception e ){
			fail(e.getMessage());
		}
	}
}
