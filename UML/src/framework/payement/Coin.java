package framework.payement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import framework.Machine;
import framework.RawMaterial;
import framework.stockage.Stockage;

public class Coin extends Payment {
	private TreeMap<Integer, Stockage> piece;
	private Machine machine;
	
	public Coin(Machine machine) {
		this.piece = new TreeMap<Integer, Stockage>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
		this.machine = machine;
		setName("Pièce");
	}
	public void addModule(int val) {
		
		Stockage s = new Stockage(0, Integer.MAX_VALUE);
		RawMaterial rw = new RawMaterial("Pièce de "+val+"", 0, RawMaterial.TypeOfRawMaterial.money, s);
		//s.Add(rw);
		
		piece.put(val, s);
	}
	public void delModule(int val) {
		piece.remove(piece.get(val));
	}
	public boolean insertPiece(int val) {
		try {
			Stockage s = piece.get(val);
			s.Add(1);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public Object pay(int price) {
		if( price == 0 )
			return Boolean.TRUE;
		else if( price < 0.0 )
			return refund(-price);
		return null;
	}
	public Collection<Integer> getModules() {
		return new ArrayList<Integer>(piece.keySet());
	}
	public ArrayList<Integer> refund(int amount) {
		try {
			Set<Integer> keys = piece.keySet();
			Integer[] val = keys.toArray(new Integer[keys.size()]);
			Integer[] res = new Integer[val.length];
			int length = val.length;
			
			int i = 0;
			int j;
			int tmp;
			
			while( i < length && amount != 0 ) {			
				
				for(j=0; j<length; j++)
					res[j] = 0;
				
				tmp = amount;
				j = i;
				while ( tmp > 0 && j < length ) {				
					while( j < length && (val[j] > tmp || piece.get(val[j]).getAmount()-res[j] <= 0) ) j++;
					if( j >= length )
						break;
					
					
					res[j]++;
					tmp = tmp - val[j];
				}
				
				
				if( tmp == 0 )
					amount = 0;
				i++;
			}
			
			
			ArrayList<Integer> refund = new ArrayList<Integer>();
			if( amount == 0 ) {
				for(i=0; i<length; i++) {
					for(j=0; j<res[i]; j++) {
						refund.add(val[i]);
						piece.get(val[i]).Substract(1);
					}
				}
			}
			else {
				return null;
			}
			
			return refund;
		} catch (Exception e) {
			// Est nécessaire pour la compilation car substract throw une erreur.
		}
		return null;
	}
}