package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import framework.Article;
import framework.RawMaterial;
import framework.Recipe;
import framework.RawMaterial.TypeOfRawMaterial;
import framework.payement.Token;
import framework.stockage.Classic;
import framework.stockage.Stockage;

public class Machine {

	@Test
	public void testBuy() {
		try {
			Stockage stock = new Classic(100);
			RawMaterial cofeee = new RawMaterial(5, TypeOfRawMaterial.gram, stock);
			stock.Add(cofeee.getAmount());
			Collection<RawMaterial> recette = new ArrayList<RawMaterial>();
			recette.add(cofeee);
			Article cafe = new Article("Café", 1.0, new Recipe(recette));
			
			framework.Machine machine = new framework.Machine("cc");
			assertTrue(machine.Buy(new Token(), cafe));
			assertFalse(machine.Buy(new Token(), cafe));
		} catch (Exception e ) {
			fail(e.getMessage());
		}
	}

}
