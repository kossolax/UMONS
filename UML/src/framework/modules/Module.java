package framework.modules;

public abstract class Module {

	protected boolean avalaible;
	private String name;
	
	public Module() {
		this.avalaible = true;
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

}