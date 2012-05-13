package conf;

import java.io.File;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import conf.currency.Currency;
import conf.currency.CurrencyFactory;
import conf.currency.CurrencyFactory.Country;
import conf.discount.CompositeDiscount;
import conf.discount.CustomerDiscount;
import conf.discount.DiscountFactory;
import conf.discount.ProductDiscount;
import conf.discount.SalesDiscount;
import core.entities.ItemList;
import core.entities.UserList;


/**
 * there should be only one instance of GlobalConfiguration
 * for each POS system instance
 * @author Nick
 *
 */
public class GlobalConfiguration {
	private static GlobalConfiguration instance;
	private String confPath = "./configure/";
	private String fileName = "conf.xml";
	private String productFileName = "itemListFile.txt";
	private String userFileName = "userPasswordFile.txt";
	private File configFile;
	private Document confDoc;
	private SAXReader saxReader;
	//configurable features
	private Currency currency;
	private CompositeDiscount globalDiscount;
	private CustomerDiscount customerDiscount;
	private SalesDiscount salesDiscount;
	private float salesRequirement;
	private boolean initialized;
	private static Object syncObject = new Object();
	
	private GlobalConfiguration(){
		this.saxReader = new SAXReader();
		this.globalDiscount = new CompositeDiscount();
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		Logger.getAnonymousLogger().addHandler(handler);
		Logger.getAnonymousLogger().setLevel(Level.ALL);
		initialized = false;
	}
	
	public static GlobalConfiguration getInstance(){
		if(instance == null){
			synchronized(syncObject) {
				if (instance == null) {
					instance = new GlobalConfiguration();
				}
			}
		}
		return instance;
	}
	
	//initialization from the files: itemListFile, userPasswordFile
	private boolean loadEntities(String pathName, String productFile, String userFile){
		boolean rtn = false;
		String productPath = confPath + productFileName;
		String userPath = confPath + userFileName;
		if(pathName != null && pathName.length() != 0){
			if(productFile != null && productFile.length() != 0){
				productPath = pathName + productFile;
			}
			if(userFile != null && userFile.length() != 0){
				userPath = pathName + userFile;
			}
		}
		rtn = ItemList.getInstance().load(productPath);
		if(rtn){
			rtn = UserList.getInstance().loadUsers(userPath);
		}
		return rtn;
	}
	
	private boolean loadConf(String pathName, String fileName){
		//entities should be initialized before configuration
		if(!ItemList.getInstance().isInitialized() || !UserList.getInstance().isInitialized()){
			Logger.getAnonymousLogger().log(Level.SEVERE, "Entities not initialized yet!");
			return false;
		}
		if(pathName != null && pathName.length() != 0){
			this.setConfPath(pathName);
		}
		if(fileName != null && fileName.length() != 0){
			this.setFileName(fileName);
		}
		this.configFile = new File(this.confPath + this.fileName);
		try {
    		this.confDoc = saxReader.read(this.configFile);
	    } catch (DocumentException e) {
	    	e.printStackTrace();
			Logger.getAnonymousLogger().log(Level.SEVERE, "Loading config file failed!");
			return false;
	    }
		boolean loaded = this.loadModels(confDoc);
		if(!loaded){
			Logger.getAnonymousLogger().log(Level.SEVERE, "Loading models failed!");
			return false;
		}
		return true;
	}
	
	//initialization from conf
	private boolean loadModels(Document confDoc){
		try{
			Element root = confDoc.getRootElement();
			//set currency
			Element currentElement = root.element("currency");
			String unit  = currentElement.getTextTrim();
			this.currency = CurrencyFactory.createCurrency(Country.valueOf(unit));
			//set discount strategy
			currentElement = root.element("discount");
			List<?> strategies = currentElement.elements();
			for(int i = 0; i < strategies.size(); i++){
				Element strategy = (Element)strategies.get(i);
				String type = strategy.attributeValue("type");
				if("product".equals(type)){
					//attach discount info to item instance
					List<?> items = strategy.elements();
					for(int idx = 0; idx < items.size(); idx++){
						Element itemDisc = (Element)items.get(idx);
						String id = itemDisc.attributeValue("id");
						String ratio = itemDisc.attributeValue("value");
						//the discount ratio should be between 0 and 1
						if(Double.valueOf(ratio) > 1.0 || Double.valueOf(ratio) < 0.0){
							continue;
						}
						ProductDiscount disc = new ProductDiscount(Float.valueOf(ratio));
						ItemList.getInstance().setItemDiscount(id, disc);
					}
					continue;
				}else if("customer".equals(type)){
					//add customer discounts
					String ratio = strategy.attributeValue("value");
					//the discount ratio should be between 0 and 1
					if(Double.valueOf(ratio) > 1.0 || Double.valueOf(ratio) < 0.0){
						continue;
					}
					this.customerDiscount = new CustomerDiscount(Float.valueOf(ratio));
				}else if("sales".equals(type)){
					//add sales discounts
					String ratio = strategy.attributeValue("value");
					//add sales requirements
					String salesNumber = strategy.attributeValue("value1");
					//the discount ratio should be between 0 and 1
					if(Double.valueOf(ratio) > 1.0 || Double.valueOf(ratio) < 0.0){
						continue;
					}
					//the sales requirement should be larger than 0
					if(Double.valueOf(salesNumber) < 0.0){
						continue;
					}
					this.salesDiscount = new SalesDiscount(Float.valueOf(ratio));
					this.salesRequirement = Float.valueOf(salesNumber);
				}
				else{
					//add global discounts
					String ratio = strategy.attributeValue("value");
					//the discount ratio should be between 0 and 1
					if(Double.valueOf(ratio) > 1.0 || Double.valueOf(ratio) < 0.0){
						continue;
					}
                	this.globalDiscount.add(DiscountFactory.createDiscount(type, Float.valueOf(ratio)));
					continue;
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			Logger.getAnonymousLogger().log(Level.SEVERE, "Fatal error: MVC configuration is failed!");
		}
		return false;
	}
	
	public boolean load(String pathName, String confFile, String productFile, String userFile){
		initialized = this.loadEntities(pathName, productFile, userFile);
		if(initialized){
			Logger.getAnonymousLogger().log(Level.INFO,"Entities Loaded");
			initialized = this.loadConf(pathName, confFile);
		}
		if(initialized){
			Logger.getAnonymousLogger().log(Level.INFO,"Configuration Loaded");
		}
		return initialized;
	}
	
	/*
	 * getters and setters 
	 */
	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Currency getCurrency() {
		return currency;
	}

	public CompositeDiscount getGlobalDiscount() {
		return globalDiscount;
	}
	
	public CustomerDiscount getCustomerDiscount(){
		return customerDiscount;
	}
	
	public SalesDiscount getSalesDiscount(){
		return salesDiscount;
	}
	
	public float getSalesRequirement(){
		return salesRequirement;
	}

	public boolean isInitialized() {
		return initialized;
	}	
	
}
