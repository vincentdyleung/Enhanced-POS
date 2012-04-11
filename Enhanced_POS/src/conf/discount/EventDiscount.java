package conf.discount;

public class EventDiscount implements Discount {
	private float discount;
	public EventDiscount(float _discount) {
		discount = _discount;
	}
	public float discount() {
		// TODO Auto-generated method stub
		return discount;
	}
	public String discountMessage() {
		String message = "\tEvent Discount: " + discount*100 + "%" + "\n";
		return message;
	}
}
