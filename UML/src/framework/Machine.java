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
	public boolean Buy(Payment payement, Article article) {
		boolean res = false;
		
		logger.debug("--------------------------------------------------");
		logger.debug("Attempt to buy" + article + " with " + payement);
		
		if( article.isAvailaible() ) {
			logger.debug("Article is available");
			double price = article.getPrice();
			
			if( payement.pay() ) {
				logger.debug("payement validated");
				
				res = ( article.delivery() != null );
			}
		}
		if( res )
			logger.debug("Article has been delivered");
		else
			logger.debug("Article is not available");
		
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