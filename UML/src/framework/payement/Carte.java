package framework.payement;


public class Carte extends Payment {

	public boolean pay(double price) {
		return false;
	}
	public String toString() {
		return "Carte";
	}
}