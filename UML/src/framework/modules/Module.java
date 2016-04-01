package framework.modules;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
public abstract class Module {

	protected boolean avalaible;
	private String name;
	
	public Module() {
		this.avalaible = true;
		this.name = getClass().getName();
	}
	public Module(String name) {
		this.avalaible = true;
		this.name = name;
	}
	public boolean isAvalaible() {
		return avalaible;
	}
	public void setAvalaible(boolean avalaible) {
		this.avalaible = avalaible;
	}
	public String toString() {
		return name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}