package core;

import conf.GlobalConfiguration;
import gui.LoginDialog;

public class Loader {
	
	private static GlobalConfiguration allConf;
	
	public static void main(String[] args) {
		allConf = GlobalConfiguration.getInstance();
		if (!allConf.load(null, null, null, null)) {
			System.exit(-1);
		}
		LoginDialog login = new LoginDialog();
		login.setVisible(true);
	}
}
