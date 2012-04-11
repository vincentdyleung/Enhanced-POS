package core;


import java.util.logging.Level;
import java.util.logging.Logger;

import conf.GlobalConfiguration;


public class LoadConfExample {

	public static void main(String [] args) {
		boolean finished;
		
		GlobalConfiguration conf = GlobalConfiguration.getInstance();
		finished = conf.load(null, null,null,null);
		if(!finished){
			Logger.getAnonymousLogger().log(Level.SEVERE, "Loading application configuration failed!");
			return;
		}
		
		
	}
	
}
