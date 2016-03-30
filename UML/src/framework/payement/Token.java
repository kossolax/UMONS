package framework.payement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Token extends Payment {
	
	private static final Logger logger = LogManager.getLogger(Token.class);
	
	public Token() {
		setName("Jeton");
	}
	public Object pay(int price) {
		logger.debug("Payement validated using tokken");
		return Boolean.TRUE;
	}
}