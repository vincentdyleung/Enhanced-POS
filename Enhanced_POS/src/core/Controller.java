package core;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import conf.GlobalConfiguration;
import conf.discount.Discount;

import core.entities.Item;
import core.entities.ItemList;

/**
 * Controller for managing the shopping cart
 * One controller instance for one POSDialog
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
public class Controller implements Observer {
	private HashMap<String, Integer> orderList;
	private HashMap<String, Integer> stockLevels;
	private DecimalFormat numberFormat;
	
	/**
	 * Constructor for Controller
	 * The default format for number is one decimal place
	 */
	public Controller() {
		orderList = new HashMap<String, Integer>();
		numberFormat = new DecimalFormat("##.0");
		stockLevels = (HashMap<String, Integer>) Loader.getInstance().getStockLevels().clone();
		Loader.getInstance().addObserver(this);
	}
	
	/**
	 * Add an item to the shopping cart with the amount specified
	 * @param item
	 * @param amount
	 * @throws OutOfStockException
	 */
	public void addToCart(Item item, int amount) throws OutOfStockException {
		int prevLevel = stockLevels.get(item.getItemID());
		if (amount > prevLevel) {
			throw new OutOfStockException();
		}
		orderList.put(item.getItemID(), amount);
	}
	
	/**
	 * Remove the item from shopping cart
	 * @param item
	 */
	public void removeFromCart(Item item) {
		orderList.remove(item.getItemID());
	}
	
	/**
	 * Clear the whole shopping cart
	 */
	public void clearCart() {
		orderList.clear();
	}
	
	/**
	 * Getter of orderList
	 * @return
	 */
	public HashMap<String, Integer> getOrderList() {
		return orderList;
	}
	
	/**
	 * Getter of numberFormat
	 * @return
	 */
	public DecimalFormat getNumberFormat() {
		return numberFormat;
	}
	
	/**
	 * Calculate the total price (without discounts) of the whole shopping cart
	 * @return
	 */
	public float getTotalPrice() {
		float total = 0f;
		for (String itemID : orderList.keySet()) {
			Item item = ItemList.getInstance().getItemById(itemID);
			float subTotal = item.getPrice() * orderList.get(itemID);
			total += subTotal;
		}
		return total;
	}
	
	/**
	 * Calculate the price to be paid of the whole shopping cart after discounts
	 * Discount strategy is read from GlobalConfiguration
	 * @param isVIP
	 * @param isEventDiscount
	 * @return
	 */
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
	
	/**
	 * Calculate the difference between the total price without discounts and with discounts
	 * @param isVIP
	 * @param isEventDiscount
	 * @return
	 */
	public float getDiscounted(boolean isVIP, boolean isEventDiscount) {
		return getTotalPrice() - getTotalSum(isVIP, isEventDiscount);
	}
	
	/**
	 * Calculate the change to be given to customer with the specified amount to be paid
	 * @param paid
	 * @param isVIP
	 * @param isEventDiscount
	 * @return
	 */
	public float getRefund(float paid, boolean isVIP, boolean isEventDiscount) {
		return paid - getTotalSum(isVIP, isEventDiscount);
	}
	
	/**
	 * Add a sales record to the log file
	 * @param msg
	 */
	public void addLog(String msg) {
		Logger.getInstance().addLog(msg);
	}

	public void update(Observable o, Object arg) {
		if (arg instanceof HashMap) {
			stockLevels = (HashMap<String, Integer>) ((HashMap) arg).clone();
		}
	}
	
	public void updateGlobalStockLevels() throws OutOfStockException {
		for (String itemID : orderList.keySet()) {
			Loader.getInstance().deductStockLevel(itemID, orderList.get(itemID));
		}
	}
}
