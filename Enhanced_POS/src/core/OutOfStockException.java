package core;

/**
 * Exception thrown when the requested amount of purchase exceeds the stock level
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
public class OutOfStockException extends Exception {
	
	/**
	 * Constructor for OutOfStockException
	 */
	public OutOfStockException() {
		super();
	}
}
