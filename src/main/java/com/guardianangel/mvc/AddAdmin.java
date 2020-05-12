package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * Used as an object to store data for getting the username for the add administrator function
 */

public class AddAdmin {
	
	/**
	 * Stores the username for adding admins
	 */
	public String username;
	
	/**
	 * Sets the instance variable username to username
	 * 
	 * @param username that takes in the username from the thymeleaf template
	 */
	public void setUsername(String username) {
		
		this.username = username;
		
	}
	
	/**
	 * 
	 * @return username for adding admins
	 */
	public String getUsername() {
		
		return this.username;
		
	}

}
