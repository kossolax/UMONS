package framework.payement;


public class Carte extends Payment {
	
	public Carte() {
		setName("Carte");
	}
	public boolean pay(double price) {
		return false;
	}
}