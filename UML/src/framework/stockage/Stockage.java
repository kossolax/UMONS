package framework.stockage;

import framework.RawMaterial;
import framework.modules.Module;

public class Stockage extends Module implements Cloneable {

	private int amount;
	private int max;
	private RawMaterial contains;
	
	public Stockage() {
		avalaible = true;
		amount = 0;
		max = Integer.MAX_VALUE;
	}
	public Stockage(int amount, int max) {
		this.avalaible = true;
		this.amount = amount;
		this.max = max;
	}
	public int getAmount() {
		return amount;
	}
	public void Substract(int val) throws Exception {
		if( amount-val < 0 )
			throw new Exception();
		
		amount -= val;
	}
	public void Add(int val) throws Exception {
		if( amount+val > max )
			throw new Exception();
		
		amount += val;
	}
	public void setContains(RawMaterial rw) {
		contains = rw;
	}
	public RawMaterial getContains() {
		return contains;
	}
	public String toString() {
		return "Stockage";
	}
}