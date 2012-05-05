package core;

import gui.POSDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JDialog;

import core.entities.Item;

public class Controller {
	private HashMap<String, Integer> orderList;
	private static Controller instance;
	private ArrayList<POSDialog> posDialogStack;
	
	private Controller() {
		orderList = new HashMap<String, Integer>();
		posDialogStack = new ArrayList<POSDialog>();
	}
	
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	public void addToCart(Item item, int amount, POSDialog pos) {
	
	}
	
	public HashMap<String, Integer> getOrderList() {
		return orderList;
	}
	
	public void addPOSDialog(POSDialog pos) {
		posDialogStack.add(pos);
	}
}
