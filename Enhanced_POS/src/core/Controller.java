package core;

import java.text.DecimalFormat;
import java.util.HashMap;
import conf.GlobalConfiguration;

import core.entities.Item;
import core.entities.ItemList;

public class Controller {
	private HashMap<String, Integer> orderList;
	private static Controller instance;
	private DecimalFormat numberFormat;
	
	private Controller() {
		orderList = new HashMap<String, Integer>();
		numberFormat = new DecimalFormat("##.0");
	}
	
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	public void addToCart(Item item, int amount) {
		orderList.put(item.getItemID(), amount);
	}
	
	public void removeFromCart(Item item) {
		orderList.remove(item.getItemID());
	}
	
	public HashMap<String, Integer> getOrderList() {
		return orderList;
	}
	
	public DecimalFormat getNumberFormat() {
		return numberFormat;
	}
	
	public float getTotalPrice() {
		float total = 0f;
		for (String itemID : orderList.keySet()) {
			Item item = ItemList.getInstance().getItemById(itemID);
			float subTotal = item.getPrice() * orderList.get(itemID);
			total += subTotal;
		}
		return total;
	}
	
	public float getTotalSum(boolean isVIP) {
		float total = 0f;
		for (String itemID : orderList.keySet()) {
			Item item = ItemList.getInstance().getItemById(itemID);
			float subTotal = item.getPrice() * orderList.get(itemID) * (1 - item.getDiscount().discount());
			System.out.println(item); 
			System.out.println(item.getDiscount().discountMessage());
			total += subTotal;
		}
		if (getTotalPrice() > GlobalConfiguration.getInstance().getSalesRequirement()) {
			total *= (1 - GlobalConfiguration.getInstance().getSalesDiscount().discount());
			System.out.println(GlobalConfiguration.getInstance().getSalesDiscount().discountMessage());
		}
		if (isVIP) {
			total *= (1 - GlobalConfiguration.getInstance().getCustomerDiscount().discount());
			System.out.println(GlobalConfiguration.getInstance().getCustomerDiscount().discountMessage());
		}
		total *= (1 - GlobalConfiguration.getInstance().getGlobalDiscount().discount());
		System.out.println(GlobalConfiguration.getInstance().getGlobalDiscount().discountMessage());
		return total;
	}
	
	public float getDiscounted(boolean isVIP) {
		return getTotalPrice() - getTotalSum(isVIP);
	}
}
