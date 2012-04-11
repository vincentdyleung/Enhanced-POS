package conf.discount;

public class SalesDiscount implements Discount {
	private float discount;
	public SalesDiscount(float _discount) {
		this.discount = _discount;
	}
	public float discount() {
		// TODO Auto-generated method stub
		return discount;
	}
	public String discountMessage() {
		String message = "\tSales Discount: " + discount*100 + "%" + "\n";
		return message;
	}
}
