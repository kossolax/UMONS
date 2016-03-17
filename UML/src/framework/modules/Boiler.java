package framework.modules;

public class Boiler extends Module {
	public Boiler(boolean status) {
		super("Boiler");
		this.avalaible = status;
	}
}