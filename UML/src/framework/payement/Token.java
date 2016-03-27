package framework.payement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import framework.Machine;

public class Token extends Payment {
	
	private static final Logger logger = LogManager.getLogger(Token.class);
	
	public boolean pay(double price) {
		logger.debug("Payement validated using tokken");
		return true;
	}
	public String toString() {
		return "Jeton";
	}
}