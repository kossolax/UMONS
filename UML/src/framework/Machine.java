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
	
	 public class Delivery {
		 private final Article a;
		 private final Object o;
		 public Delivery(Article a, Object o) {         
		        this.a= a;
		        this.o= o;
		 }
		 public Article getArticle() {
			 return a;
		 }
		 public Object getOther() {
			 return o;
		 }
	}
	/**
	 * Buy.
	 *
	 * @param payement the kind of payement
	 * @param article the article
	 */
	public Delivery Buy(Payment payement, Article article, int solde) {
		Article deliveryArticle = null;
		Object other = null;
		
		logger.debug("--------------------------------------------------");
		logger.debug("Attempt to buy" + article + " with " + payement);
		
		if( article.isAvailaible() ) {
			
			int price = article.getPrice() - solde;
			logger.debug("Solde is " +solde +" article cost "+ article.getPrice() + " missing "+ price);
			
			other = payement.pay(price);
			if( other != null ) {
				logger.debug("payement validated");
				
				deliveryArticle = article.delivery();
			}
		}
		if( deliveryArticle != null )
			logger.debug("Article has been delivered");
		else
			logger.debug("Article has not been delivered");
		
		return new Delivery(deliveryArticle, other);
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