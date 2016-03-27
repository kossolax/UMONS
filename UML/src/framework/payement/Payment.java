package framework.payement;

import framework.modules.Module;

public abstract class Payment extends Module {
	public boolean pay(double price) {
		return false;
	}
}