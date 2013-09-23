package org.syncany.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Logging {
	private static boolean loggingInitialized = false;
	
	public synchronized static void init() {
		if (loggingInitialized) {
			return;
		}
		
    	try {
    		// Use file if exists, else use file embedded in JAR
    		File logConfig = new File("logging.properties");
    		InputStream logConfigInputStream;
    		
    		if (logConfig.exists() && logConfig.canRead()) {
    			logConfigInputStream = new FileInputStream(new File("logging.properties"));
    		}
    		else {
    			logConfigInputStream = Config.class.getResourceAsStream("/logging.properties");
    		}
    		
    	    LogManager.getLogManager().readConfiguration(logConfigInputStream);
    	    loggingInitialized = true;
    	}
    	catch (Exception e) {
    	    Logger.getAnonymousLogger().severe("Could not load logging.properties file from file system or JAR.");
    	    Logger.getAnonymousLogger().severe(e.getMessage());
    	    
    	    e.printStackTrace();
    	}
		
	}	
	
	public static void setGlobalLogLevel(Level targetLogLevel) {
		Logger.getLogger("").setLevel(targetLogLevel);
	}
	
	public static void addGlobalHandler(Handler targetHandler) {
		Logger.getLogger("").addHandler(targetHandler);
	}
		
	public static void disableLogging() {
		setGlobalLogLevel(Level.OFF);
		
		while (Logger.getLogger("").getHandlers().length > 0) {
			Logger.getLogger("").removeHandler(Logger.getLogger("").getHandlers()[0]);
		}
	}
}
