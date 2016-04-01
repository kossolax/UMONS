package framework.stockage;
/**
 * 
 * @author Copois Pierre
 * @author Zaretti Steve
 * 
 */
public class Freeze extends Stockage {
	public Freeze(int a) {
		super(0, a);
		setName("Congélateur");
	}
}