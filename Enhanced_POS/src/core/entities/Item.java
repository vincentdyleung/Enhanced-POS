package core.entities;


import conf.discount.Discount;

//a Item class storing the itemname, price, and number
/**
 * @author Nick
 * Modified at 25, March
 * property ID and getters added 
 */
public class Item {
	private String itemID;
	private String itemName;
	private Integer number;
	private Float price;
	private Discount discount;
	
	public Item(String id, String iName, Integer _number, Float _price){
		this.itemID = id;
		this.itemName = iName;
		this.number = _number;
		this.price = _price;
		this.discount = null;
	}
	
	public Item(Item item){
		this.itemID = item.getItemID();
		this.itemName = item.getItemName();
		this.number = item.getNumber();
		this.price = item.getPrice();
		this.discount = item.getDiscount();
	}
	
	public void setDiscount(Discount _discount) {
		this.discount = _discount;
	}
	
	
	
	public String getItemID() {
		return itemID;
	}
	public String getItemName() {
		return itemName;
	}
	public Integer getNumber() {
		return number;
	}
	public Float getPrice() {
		return price;
	}
	public Discount getDiscount() {
		return discount;
	}
	public String toString(){
		return this.itemName + "  " + number;
	}
}
