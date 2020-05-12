package com.guardianangel.twitter;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/9/2020
 * 
 * Helper class to get the Twitter ID number of a user if only given a screen name
 */

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class GetUserTwitterIDByScreenName {
	
	/**
	 * Function that takes in a String of a Twitter user's user name and returns their Twitter ID in a string format
	 * 
	 * @param user name of the Twitter user without the "@"
	 * @return A user's Twitter ID in a String format
	 */
	public static String getID(String username) {
		
		Twitter twitterInstance = TwitterConnection.getTwitterInstance();
		User user = null;
		String id = null;
		
		try {
			
			user = twitterInstance.showUser(username);
			
		} catch (TwitterException e) {
			
			e.printStackTrace();
			
		}
		
		id = Long.toString(user.getId());
		
		return id;
		
	}

}
