package com.guardianangel.twitter;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * Object to store and create the connection to Twitter.
 * Getter method can be statically called to return the Twitter object for use.
 */

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConnection {

	/**
	 * Configuration builder to store api keys
	 */
    private static ConfigurationBuilder configurationBuilder;
    
    /**
     * TwitterFactory object that takes the builder
     */
    private static TwitterFactory twitterFactory;
    
    /**
     * Twitter object that is used for all twitter operation, takes twitter factory
     */
    private static Twitter twitter;
    
    /**
     * Check for connection to twitter
     */
    public static Boolean connected = false;
    
    /**
     * Method to connect/create the twitter object
     */
    private static void connectToTwitter() {
    	
		configurationBuilder = new ConfigurationBuilder();
		
		//Get full length tweets instead of tweets cut off
		configurationBuilder.setTweetModeExtended(true);
		
		//Set the configuration to the guardian angel application on twitter
		configurationBuilder.setDebugEnabled(true)
        .setOAuthConsumerKey("API KEY GOES HERE")
        .setOAuthConsumerSecret("API KEY GOES HERE")
        .setOAuthAccessToken("API KEY GOES HERE")
        .setOAuthAccessTokenSecret("API KEY GOES HERE");

		twitterFactory = new TwitterFactory(configurationBuilder.build());

	    twitter = twitterFactory.getInstance();
	    
	    connected = true;
    	
    }
    
    /**
     * Get the twitter object for use
     * 
     * @return the twitter object to use
     */
    public static Twitter getTwitterInstance() {
    	
    	if(connected) {
    		
    		return twitter;
    		
    	}else{
    		
    		connectToTwitter();
    		return twitter;
    		
    	}
    	
    }

}
