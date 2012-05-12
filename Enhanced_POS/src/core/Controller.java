package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import conf.GlobalConfiguration;
import conf.discount.Discount;

import core.entities.Item;
import core.entities.ItemList;

public class Controller {
	private HashMap<String, Integer> orderList;
	private DecimalFormat numberFormat;
	
	public Controller() {
		orderList = new HashMap<String, Integer>();
		numberFormat = new DecimalFormat("##.0");
	}
	
	public void addToCart(Item item, int amount) throws OutOfStockException {
		if (amount > item.getNumber()) {
			throw new OutOfStockException();
		} else {
			orderList.put(item.getItemID(), amount);
			return;
		}
	}
	
	public void removeFromCart(Item item) {
		orderList.remove(item.getItemID());
	}
	
	public void clearCart() {
		orderList.clear();
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
	
	public float getTotalSum(boolean isVIP, boolean isEventDiscount) {
		float total = 0f;
		for (String itemID : orderList.keySet()) {
			Item item = ItemList.getInstance().getItemById(itemID);
			Discount productDiscount = item.getDiscount();
			float subTotal;
			if (productDiscount != null) {
				subTotal = item.getPrice() * orderList.get(itemID) * (1 - productDiscount.discount());
			} else {
				subTotal = item.getPrice() * orderList.get(itemID);
			}	
			total += subTotal;
		}
		if (getTotalPrice() > GlobalConfiguration.getInstance().getSalesRequirement()) {
			total *= (1 - GlobalConfiguration.getInstance().getSalesDiscount().discount());
		}
		if (isVIP) {
			total *= (1 - GlobalConfiguration.getInstance().getCustomerDiscount().discount());
		}
		if (isEventDiscount) {
			total *= (1 - GlobalConfiguration.getInstance().getGlobalDiscount().discount());
		}
		return total;
	}
	
	public float getDiscounted(boolean isVIP, boolean isEventDiscount) {
		return getTotalPrice() - getTotalSum(isVIP, isEventDiscount);
	}
	
	public float getRefund(float paid, boolean isVIP, boolean isEventDiscount) {
		return paid - getTotalSum(isVIP, isEventDiscount);
	}
	
	public void addLog(String msg) {
		Logger.getInstance().addLog(msg);
	}
}
