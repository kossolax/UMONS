package framework;

import framework.modules.Stockage;

public class RawMaterial {
	
	private int amount, min, max;
	private boolean variable;
	private Stockage contains;
	private TypeOfRawMaterial kind;
	
	public enum TypeOfRawMaterial {
		unity,  centiliter, gram
	}
	
	public Stockage getStock() {
		return contains;
	}
	public int getAmount() {
		return amount;
	}

}