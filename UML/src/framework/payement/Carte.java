package framework.payement;


public class Carte extends Payment {
	int argent;
	
	public Carte(int argent) {
		this.argent = argent;
		setName("Carte");
	}
	public Object pay(int price) {
		if( argent-price > 0 ) {
			argent -= price;
			return Boolean.TRUE;
		}
		return null;
	}
}