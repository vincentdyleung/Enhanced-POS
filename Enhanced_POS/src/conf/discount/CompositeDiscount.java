package conf.discount;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Robin
 * @modified by Nick
 */
public class CompositeDiscount implements Discount {
	private List<Discount> mDiscount;
	private String discountMessage;
	
	public CompositeDiscount() {
		mDiscount = new ArrayList<Discount>();
		discountMessage = "";
	}
	
	/* 
	 * it is the value AFTER all discount without 1-totalDiscount
	 */
	public float discount() {
		float totalDiscount = 1;
		for (Discount d : mDiscount) {
			totalDiscount *= 1 - d.discount();
		}
		return 1-totalDiscount;
	}
	
	public void add(Discount _discount) {
		mDiscount.add(_discount);
	}
	
	public void remove(Discount _discount) {
		mDiscount.remove(_discount);
	}
	
	
	/**
	 * @return: current discount message, no global discount included
	 * @author: Nick
	 * @since: Mar 25, 2009
	 */
	public String getDiscountMessage() {
		return discountMessage;
	}

	public void addDiscountMessage(String discountMessage) {
		this.discountMessage += discountMessage;
	}
	
	
	/**
	 * get OVERALL discount message
	 * @author: Nick
	 * @since: Mar 25, 2009
	 */
	public String discountMessage() {
		String message = this.discountMessage;
		for (Discount d : mDiscount) {
			message += d.discountMessage();
		}
		return message;
	}

}
