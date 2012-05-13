package core;

import java.util.HashMap;
import java.util.Observable;

import gui.LoaderDialog;
import conf.GlobalConfiguration;
import core.entities.ItemList;

/**
 * Load the GlobalConfiguration and create a loader dialog for adding POS
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
public class Loader extends Observable {
	
	private GlobalConfiguration allConf;
	private HashMap<String, Integer> stockLevels;
	private static Loader instance;
	/**
	 * Constructor of Loader
	 */
	private Loader() {
		allConf = GlobalConfiguration.getInstance();
		if (!allConf.load(null, null, null, null)) {
			System.err.println("Configuration load error!");
			System.exit(-1);
		}
		stockLevels = new HashMap<String, Integer>();
		for (String itemid : ItemList.getInstance().getItems().keySet()) {
			stockLevels.put(itemid, ItemList.getInstance().getItemById(itemid).getNumber());
		}
		LoaderDialog loaderDialog = new LoaderDialog();
		loaderDialog.setLocationRelativeTo(null);
		loaderDialog.setVisible(true);
	}
	
	public static Loader getInstance() {
		if (instance == null) {
			instance = new Loader();
		}
		return instance;
	}
	
	public synchronized void deductStockLevel(String itemID, int purchaseAmount) throws OutOfStockException {
		int prevLevel = stockLevels.get(itemID);
		if (purchaseAmount > prevLevel) {
			throw new OutOfStockException();
		}
		stockLevels.put(itemID, prevLevel - purchaseAmount);
		setChanged();
		notifyObservers(stockLevels);
	}
	
	public int getStockLevel(String itemID) {
		return stockLevels.get(itemID);
	}
	
	public HashMap<String, Integer> getStockLevels() {
		return stockLevels;
	}
	
}
