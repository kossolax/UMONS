package framework;

import java.util.Collection;
public class Recipe {
	private Collection<RawMaterial> require;
	
	public Recipe(Collection<RawMaterial> require) {
		this.require = require;
	}
	public Collection<RawMaterial> getRecipe() {
		return require;
	}
	
}