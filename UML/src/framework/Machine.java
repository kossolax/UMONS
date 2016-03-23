package framework;

import java.util.ArrayList;
import java.util.Collection;

import framework.modules.Module;
import framework.payement.Payment;

public class Machine {

	private String name;
	private Collection<Module> modules;
	private Category category;
	
	public Machine(String name) {
		this.name = name;
		this.modules = new ArrayList<Module>();
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
	public Collection<Module> getModules() {
		return modules;
	}
	public int countModule() {
		return modules.size();
	}
	public void setMainCategory(Category category) {
		this.category = category;
	}
	public Category getCategory() {
		return category;
	}
	public String toString() {
		return name;
	}

}