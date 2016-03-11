package framework.payement;

import framework.modules.Stockage;

public abstract class aCoin extends Payment {
	public class Coin extends Stockage {
		public boolean pay() {
			return false;
		}
	}
}