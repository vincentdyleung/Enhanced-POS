package core.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import conf.discount.Discount;

/**
 * @author Robin
 * @Modified by Nick
 */
public class ItemList {
	private static ItemList instance;
	private HashMap<String, Item> items;
	private HashMap<String, String> itemKeys;
	private boolean initialized;
	
	private ItemList(){
		this.items = new HashMap<String, Item>();
		this.itemKeys = new HashMap<String, String>();
		this.initialized = false;
	}
	
	public static ItemList getInstance(){
		if(instance == null){
			instance = new ItemList();
		}
		return instance;
	}
	
	public boolean load(String fullPath){
		if(this.initialized){
			Logger.getAnonymousLogger().log(Level.SEVERE, "ItemList already initialized!");
			return false;
		}
		BufferedReader bReader;
		try {
			bReader = new BufferedReader(new FileReader(fullPath));
			String item = bReader.readLine();
			while(item != null) {
				String[] temp = item.split(" ");
				// a line should be formatted like "itemID itemName price number"
				if ((temp.length == 4)){
					String itemID = temp[0];
					String itemName = temp[1];
					Float price = new Float(temp[2]);
					Integer number = new Integer(temp[3]);
					Item _item = new Item(itemID,itemName, number, price);
					items.put(itemID, _item);
					itemKeys.put(_item.getItemName(), itemID);
				}else {
					Logger.getAnonymousLogger().log(Level.SEVERE, "The item list file is wrongly formatted!");
					return false;
				}
				item = bReader.readLine();
			}
			bReader.close();
			initialized = true;
			return true;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Load itemList failed: " + e.getMessage());
			return false;
		}
	}
	
	public void setItemDiscount(String itemId, Discount discount){
		Item item = null;
		if(itemId != null && itemId.length() != 0){
			item = this.items.get(itemId);
		}
		if(item != null){
			item.setDiscount(discount);
		}
	}
	
	public boolean isInitialized(){
		return this.initialized;
	}
	
	public HashMap<String, Item> getItems(){
		return this.items;
	}
	
	public Item getItemById(String itemId){
		Item rtn = new Item(this.items.get(itemId));
		return rtn;
	}
	
	public Item getItemByName(String name){
		String itemId = this.itemKeys.get(name);
		return new Item(this.items.get(itemId));
	}
	
	public String getItemKeyByName(String name){
		String itemId = this.itemKeys.get(name);
		return itemId;
	}
	
}
