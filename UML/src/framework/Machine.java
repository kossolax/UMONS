package framework;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import framework.modules.Module;
import framework.payement.Payment;

public class Machine {

	private String name;
	private Collection<Module> modules;
	private Category category;
	private static final Logger logger = LogManager.getLogger(Machine.class);
	
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
	public boolean Buy(Payment payement, Article article, double solde) {
		boolean res = false;
		
		logger.debug("--------------------------------------------------");
		logger.debug("Attempt to buy" + article + " with " + payement);
		
		if( article.isAvailaible() ) {
			
			double price = article.getPrice() - solde;
			logger.debug("Solde is " +solde +" article cost "+ article.getPrice() + " missing "+ price);
			
			if( payement.pay(price) ) {
				logger.debug("payement validated");
				
				res = ( article.delivery() != null );
			}
		}
		if( res )
			logger.debug("Article has been delivered");
		else
			logger.debug("Article has not been delivered");
		
		return res;
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