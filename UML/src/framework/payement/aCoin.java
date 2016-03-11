package framework.payement;

import framework.Stockage;

public abstract class aCoin extends Payment {
	public class Coin extends Stockage {
		public boolean pay() {
			return false;
		}
	}
}