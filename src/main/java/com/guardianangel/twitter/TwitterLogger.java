package com.guardianangel.twitter;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 9/12/2019
 * 
 * Created a TweetLogger class to use java's built in logging framework from java.util
 * This will allow some customization and simplification rather than importing some big logging framework from outside
 */

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.util.logging.FileHandler;

public class TwitterLogger {
	
	/**
	 * Logger for the class
	 */
	private static final Logger logger = Logger.getLogger(TwitterLogger.class.getName());
	
	/**
	 * For writing the logs to file
	 */
	private FileHandler fileHandler;
	
	/**
	 * Constructor to create the system logger of the specified class
	 * 
	 * @param loggedClass takes in any class that needs to be logged
	 */
	protected TwitterLogger(String loggedClass) {
		
		fileHandler = null;
		
		try {
			
			//Create the file handler and set it to the twitter
			fileHandler = new FileHandler("./GuardianAngelLogs/"+ loggedClass +"_logs.txt", true);
			
			//Add a handler to the logger from java.util.logger -- in this case it will be a file handler to write to a log file
			logger.addHandler(fileHandler);
			
			//Create a SimpleFormatter, and this will create a header for each log entry. Giving date/time/method of log
			SimpleFormatter fileFormatter = new SimpleFormatter();
			
			//Set the file handler to format to the simple logger created above
			fileHandler.setFormatter(fileFormatter);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * Warning level logging with log parameter
	 * 
	 * @param warning is the data to be logged
	 */
	protected void warning(String warning) {
		
		logger.warning(warning + "\n");
		
	}
	
	/**
	 * Information level logging with log parameter
	 * 
	 * @param information is the data to be logged
	 */
	protected void info(String information) {
		
		logger.info(information + "\n");
		
	}
	
	/**
	 * Severe level logging with log parameter
	 * 
	 * @param severe is that data to be logged
	 */
	protected void severe(String severe) {
		
		logger.severe(severe + "\n");
		
	}
	
	/**
	 * Method to close the file handler
	 */
	protected void close() {
		
		fileHandler.close();
		
	}

}