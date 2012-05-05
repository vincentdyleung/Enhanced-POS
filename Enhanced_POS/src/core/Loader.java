package core;

import gui.LoaderDialog;
import conf.GlobalConfiguration;

public class Loader {
	
	private GlobalConfiguration allConf;
	
	public Loader() {
		allConf = GlobalConfiguration.getInstance();
		if (!allConf.load(null, null, null, null)) {
			System.err.println("Configuration load error!");
			System.exit(-1);
		}
		LoaderDialog loaderDialog = new LoaderDialog();
		loaderDialog.setLocationRelativeTo(null);
		loaderDialog.setVisible(true);
	}
}
