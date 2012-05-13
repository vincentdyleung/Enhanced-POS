package core;

import java.util.HashMap;
import java.util.Observable;

import gui.LoaderDialog;
import conf.GlobalConfiguration;
import core.entities.ItemList;

/**
 * Load the GlobalConfiguration and create a loader dialog for adding POS
 * Enforced observer pattern, Loader is the subject for all Controller instances
 * Enforced Singleton pattern, guarantee that only one Loader instance will be created for each run
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
	
	/**
	 * Get a Loader instance. Enforced Singleton pattern
	 * @return
	 */
	public static Loader getInstance() {
		if (instance == null) {
			instance = new Loader();
		}
		return instance;
	}
	
	/**
	 * Thread safe method for updating the stock levels accross all POS Dialogs
	 * @param itemID
	 * @param purchaseAmount
	 * @throws OutOfStockException
	 */
	public synchronized void deductStockLevel(String itemID, int purchaseAmount) throws OutOfStockException {
		int prevLevel = stockLevels.get(itemID);
		if (purchaseAmount > prevLevel) {
			throw new OutOfStockException();
		}
		stockLevels.put(itemID, prevLevel - purchaseAmount);
		setChanged();
		notifyObservers(stockLevels);
	}
	
	/**
	 * Getter for stockLevels
	 * @return
	 */
	public HashMap<String, Integer> getStockLevels() {
		return stockLevels;
	}
	
}
