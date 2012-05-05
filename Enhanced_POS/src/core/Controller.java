package core;

import java.util.HashMap;

import core.entities.Item;

public class Controller {
	private HashMap<String, Integer> orderList;
	private static Controller instance;
	
	private Controller() {
		orderList = new HashMap<String, Integer>();
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
	
	public HashMap<String, Integer> getOrderList() {
		return orderList;
	}
}
