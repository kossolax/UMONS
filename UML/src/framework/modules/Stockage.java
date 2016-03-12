package framework.modules;


public class Stockage extends Module {

	private int amount;
	private int max;
	
	public Stockage(int amount, int max) {
		this.avalaible = true;
		this.amount = amount;
		this.max = max;
	}
	public int getAmount() {
		return amount;
	}
	public void changeAmount(int value) throws Exception {
		if( amount+value > max || amount-value < 0 )
			throw new Exception();
		
		amount += value;
	}

}