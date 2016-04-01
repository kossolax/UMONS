package framework;

import framework.stockage.Stockage;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
public class RawMaterial {
	
	private String name;
	private int amount, min, max; 
	private boolean variable;
	private Stockage contains;
	private TypeOfRawMaterial kind;
	
	public enum TypeOfRawMaterial {
		unity ("Unité"),  centiliter ("Centilitre"), gram ("Gramme"), money("€");
		
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
	public RawMaterial(RawMaterial r) {
		this.name = r.name;
		this.amount = r.amount;
		this.min = r.min;
		this.max = r.max;
		this.variable = r.variable;
		this.contains = r.contains;
		this.kind = r.kind;
	}
	public Stockage getStock() {
		return contains;
	}
	public int getAmount() {
		return amount;
	}
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	public void setMin(int _min) {
		if(_min > this.max) this.max = _min;
		this.min = _min;
		updateAmount();
	}
	public void setMax(int _max) {
		if(_max < this.min) this.min = _max;
		this.max = _max;
		updateAmount();
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return amount + " " + kind + " de "+ name + " dans "+ contains;
	}
	public void updateAmount() {
		this.amount = (getMax() - getMin())/2 + getMin();
	}
	public void setAmount(int intValue) {
		this.amount = intValue;
	}
}