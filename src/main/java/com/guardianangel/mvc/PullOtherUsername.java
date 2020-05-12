package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * Object used to store and use the username of any twitter user.
 * It is part of administrator functionality to pull tweets of any public user on twitter
 */

public class PullOtherUsername {
	
	/**
	 * Used to store the username of the user
	 */
	public String username;
	
	/**
	 * Sets the username instance variable
	 * 
	 * @param username receives the username from template
	 */
	public void setUsername(String username) {
		
		this.username = username;
		
	}
	
	/**
	 * Returns the username from the object
	 * 
	 * @return username for the object
	 */
	public String getUsername() {
		
		return this.username;
		
	}

}
