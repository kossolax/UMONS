package framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import framework.modules.Module;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
public class Recipe {
	private Collection<RawMaterial> requireStock;
	private Collection<Module> requireModule;
	private static final Logger logger = LogManager.getLogger(Recipe.class);
	
	public Recipe(Collection<RawMaterial> require) {
		this.requireStock = require;
		requireModule = new ArrayList<Module>();
	}
	public Collection<RawMaterial> getRecipe() {
		return requireStock;
	}
	public void add(RawMaterial rw) {
		requireStock.add(rw);
	}
	public void addRequiredModule(Module requireModule) {
		this.requireModule.add(requireModule);
	}
	public boolean IsAvailable() {
		boolean avaialable = true;
		logger.debug("--------------------------------------------------");
		logger.debug("Checking IsAvailable");
		
		Iterator<RawMaterial> itr = requireStock.iterator();
		while( itr.hasNext() && avaialable ) {
			RawMaterial materials = itr.next();
			
			logger.debug("Checking stock of "+materials.getName()+" : "+ materials.getStock() + ". It has: " + materials.getStock().getAmount() + ", but we need "+ materials.getAmount());
			
			if( !materials.getStock().isAvalaible() || materials.getStock().getAmount() < materials.getAmount() ) {
				logger.debug("Something is wrong. The article will not be available.");
				avaialable = false;
			}
		}
		
		
		for( Module m : requireModule ) {
			
			logger.debug("Checking module "+m+" : "+ m.isAvalaible()  );
			
			if( !m.isAvalaible() ) {
				logger.debug("Something is wrong. The article will not be available.");
				avaialable = false;
				break;
			}
		}
		
		if( avaialable )
			logger.debug("Article is available.");
		else
			logger.debug("Article is not available.");
		
		return avaialable;
	}
}