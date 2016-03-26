package framework.payement;


public class Carte extends Payment {

	public boolean pay() {
		return false;
	}
	public String toString() {
		return "Carte";
	}
}