package framework;

import java.util.Collection;

import framework.modules.Module;
import framework.payement.Payment;

public class Machine {

	private String name;
	private Collection<Module> modules;
	

	/**
	 * Buy.
	 *
	 * @param payement the kind of payement
	 * @param article the article
	 */
	public boolean Buy(Payment payement, Article article) {
		if( article.isAvalaible() ) {
			double price = article.getPrice();
			if( payement.pay() ) {
				return ( article.delivery() != null );
			}
		}
		return false;
	}
	
	public void addModule(Module module) {
		modules.add(module);
	}

}