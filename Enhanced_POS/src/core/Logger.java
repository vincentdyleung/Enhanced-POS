package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import conf.GlobalConfiguration;

/**
 * Logger for sales information
 * Singleton design pattern is applied
 * Enforced singleton pattern, guarantee that only one Logger instance will be created for each run
 * @author Liang Diyu dliang@stu.ust.hk
 *
 */

public class Logger {
	private PrintWriter logger;
	private static final String LOG_FILE = "salesRecord.txt";
	private static Logger instance;
	private static Object syncObject = new Object();
	
	/**
	 * Constructor of Logger
	 */
	private Logger() {
		try {
			String configPath = GlobalConfiguration.getInstance().getConfPath();
			FileWriter fileWriter = new FileWriter(new File(configPath + LOG_FILE), true);
			logger = new PrintWriter(fileWriter, true);
		} catch (FileNotFoundException e) {
			System.err.println("Log file not found");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Getter for the singleton design pattern
	 * @return
	 */
	public static Logger getInstance() {
		if (instance == null) {
			synchronized(syncObject) {
				if (instance == null) {
					instance = new Logger();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Thread safe method to add log message
	 * @param msg
	 */
	public synchronized void addLog(String msg) {
		logger.println(msg);
	}

}
