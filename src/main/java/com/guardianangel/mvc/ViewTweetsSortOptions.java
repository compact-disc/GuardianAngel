package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 3/10/2019
 * 
 * Used to store sorting options for view tweets controller
 */

public class ViewTweetsSortOptions {

	/**
	 * String to store the variable to tell what we are viewing (timeline, mentions, likes, or retweets)
	 */
	public String viewBy;
	
	public void setViewBy(String viewBy) {
		
		this.viewBy = viewBy;
		
	}
	
	public String getViewBy() {
		
		return this.viewBy;
		
	}

}
