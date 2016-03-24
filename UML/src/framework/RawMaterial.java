package framework;

import framework.stockage.Stockage;

public class RawMaterial {
	
	private String name;
	private int amount, min, max; 
	private boolean variable;
	private Stockage contains;
	private TypeOfRawMaterial kind;
	
	public enum TypeOfRawMaterial {
		unity ("Unité"),  centiliter ("Centilitre"), gram ("Gramme");
		
		private String name;       
	    private TypeOfRawMaterial(String s) {
	        name = s;
	    }
	    public String toString() {
	    	return name;
	    }
	}
	public RawMaterial(int amount, TypeOfRawMaterial kind, Stockage contains) {
		this.amount = amount;
		this.kind = kind;
		this.contains = contains;
		contains.setContains(this);
	}
	public RawMaterial(String name, int amount, TypeOfRawMaterial kind, Stockage contains) {
		this.name = name;
		this.amount = amount;
		this.kind = kind;
		this.contains = contains;
		contains.setContains(this);
	}
	public Stockage getStock() {
		return contains;
	}
	public int getAmount() {
		return amount;
	}
	public String toString() {
		return amount + " " + kind + " de "+ name + " dans "+ contains;
	}

}