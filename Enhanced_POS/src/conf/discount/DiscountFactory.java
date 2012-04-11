package conf.discount;

public class DiscountFactory {

	public static Discount createDiscount(String type, float ratio){
		if("event".equals(type)){
			return new EventDiscount(ratio);
		}
		if("customer".equals(type)){
			return new CustomerDiscount(ratio);
		}
		if("product".equals(type)){
			return new ProductDiscount(ratio);
		}
		if("sales".equals(type)){
			return new SalesDiscount(ratio);
		}
		return null;
	}
}
