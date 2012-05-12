package core;

import gui.LoaderDialog;
import conf.GlobalConfiguration;

/**
 * Load the GlobalConfiguration and create a loader dialog for adding POS
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */
public class Loader {
	
	private GlobalConfiguration allConf;
	
	/**
	 * Constructor of Loader
	 */
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
