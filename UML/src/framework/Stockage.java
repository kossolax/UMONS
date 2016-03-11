package framework;

public class Stockage extends Module {

	private int amount;
	private int max;
	public int getAmount() {
		return amount;
	}
	public void changeAmount(int value) throws Exception {
		if( amount+value > max || amount-value < 0 )
			throw new Exception();
		
		amount += value;
	}

}