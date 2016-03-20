package framework.payement;

import framework.stockage.Stockage;

public abstract class aCoin extends Payment {
	public class Coin extends Stockage {
		public Coin(int a) {
			super(0, a);
		}

		public boolean pay() {
			return false;
		}
	}
}