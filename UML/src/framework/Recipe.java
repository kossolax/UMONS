package framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import framework.modules.Module;
public class Recipe {
	private Collection<RawMaterial> requireStock;
	private Collection<Module> requireModule;
	
	
	public Recipe(Collection<RawMaterial> require) {
		this.requireStock = require;
		requireModule = new ArrayList<Module>();
	}
	public Collection<RawMaterial> getRecipe() {
		return requireStock;
	}
	public void addRequiredModule(Module requireModule) {
		this.requireModule.add(requireModule);
	}
	public boolean IsAvailable() {
		boolean avaialable = true;
		Iterator<RawMaterial> itr = requireStock.iterator();
		while( itr.hasNext() && avaialable ) {
			RawMaterial materials = itr.next();
			
			if( !materials.getStock().isAvalaible() || materials.getStock().getAmount() < materials.getAmount() ) {
				avaialable = false;
			}
		}
		for( Module m : requireModule ) {
			if( !m.isAvalaible() ) {
				avaialable = false;
				break;
			}
		}
		return avaialable;
	}
}