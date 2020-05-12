package com.guardianangel.twitter;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/16/2020
 * 
 * Helper for the View Tweet class. Object used to store data for each tweet to display in a table format.
 */

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class GetUsernameFromTwitterID {
	
	/**
	 * 
	 * Method to get the username of a twitter user by their id and return it in a string format
	 * 
	 * @param id twitter id number in a string format
	 * @return
	 */
	public static String getUsername(String id) {
		
		Twitter twitterInstance = TwitterConnection.getTwitterInstance();
		User user;
		String username = null;
		
		try {
			
			user = twitterInstance.showUser(Long.parseLong(id));
			username = user.getScreenName();
			
		} catch (TwitterException e) {

			e.printStackTrace();
			
		}
		
		return username;
		
	}

}
