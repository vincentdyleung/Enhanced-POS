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
	private static Controller instance;
	private DecimalFormat numberFormat;
	private PrintWriter logger;
	
	private Controller() {
		orderList = new HashMap<String, Integer>();
		numberFormat = new DecimalFormat("##.0");
		try {
			logger = new PrintWriter(new File(GlobalConfiguration.getInstance().getConfPath() + "salesRecord.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Log file not found");
			System.exit(-1);
		}
	}
	
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
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
			System.out.println(item); 
			if (productDiscount != null) {
				subTotal = item.getPrice() * orderList.get(itemID) * (1 - productDiscount.discount());
				System.out.println(productDiscount.discountMessage() + productDiscount.discount());
			} else {
				subTotal = item.getPrice();
				System.out.println("No product discount");
			}	
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
		if (isEventDiscount) {
			total *= (1 - GlobalConfiguration.getInstance().getGlobalDiscount().discount());
			System.out.println(GlobalConfiguration.getInstance().getGlobalDiscount().discountMessage());
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
		logger.append(msg);
		logger.close();
	}
}
