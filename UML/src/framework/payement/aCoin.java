package framework.payement;

import framework.modules.Stockage;

public abstract class aCoin extends Payment {
	public class Coin extends Stockage {
		public Coin(int a, int b) {
			super(a, b);
		}

		public boolean pay() {
			return false;
		}
	}
}