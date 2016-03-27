package framework.payement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import framework.Machine;
import framework.RawMaterial;
import framework.stockage.Stockage;

public class Coin extends Payment {
	private Hashtable<Double, Stockage> piece;
	private Machine machine;
	
	public Coin(Machine machine) {
		this.piece = new Hashtable<Double, Stockage>();
		this.machine = machine;
	}
	public void addModule(double val) {
		
		Stockage s = new Stockage(0, Integer.MAX_VALUE);
		RawMaterial rw = new RawMaterial("Pièce de "+val+"", 0, RawMaterial.TypeOfRawMaterial.money, s);
		//s.Add(rw);
		
		piece.put(val, s);
	}
	public void delModule(double val) {
		piece.remove(piece.get(val));
	}
	public double insertPiece(double val) {
		Stockage s = piece.get(val);
		try {
			s.Add(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return val;
	}
	public boolean pay(double price) {
		if( price <= 0.0 )
			return true;
		return false;
	}
	public String toString() {
		return "Pièce";
	}
	public Collection<Double> getModules() {
		ArrayList<Double>val = new ArrayList<Double>();
		val.addAll(piece.keySet());
		return val;
	}
}