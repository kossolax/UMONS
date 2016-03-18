package framework;

import java.util.ArrayList;
import java.util.Collection;

import framework.modules.Module;
import framework.payement.Payment;

public class Machine {

	private String name;
	private Collection<Module> modules;
	private Collection<Category> categories;
	
	public Machine(String name) {
		this.name = name;
		this.modules = new ArrayList<Module>();
		this.categories = new ArrayList<Category>();
	}
	/**
	 * Buy.
	 *
	 * @param payement the kind of payement
	 * @param article the article
	 */
	public boolean Buy(Payment payement, Article article) {
		if( article.isAvailaible() ) {
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
	public int countModule() {
		return modules.size();
	}
	public void addCategory(Category category) {
		categories.add(category);
	}
	public String toString() {
		return name;
	}

}