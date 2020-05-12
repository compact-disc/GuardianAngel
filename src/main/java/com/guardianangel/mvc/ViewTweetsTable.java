package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/15/2020
 * 
 * Helper for the View Tweet class. Object used to store data for each tweet to display in a table format.
 */

public class ViewTweetsTable {
	
	/**
	 * String to store the date
	 */
	public String date;
	
	/**
	 * String to store the content
	 */
	public String content;
	
	/**
	 * String to store the sentiment type
	 */
	public String sentimentType;
	
	/**
	 * String to store the sentiment score
	 */
	public String sentimentScore;
	
	/**
	 * 
	 * Setter for the date
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		
		this.date = date;
		
	}
	
	/**
	 * 
	 * Setter for the content
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		
		this.content = content;
		
	}
	
	/**
	 * 
	 * Setter for the sentiment type
	 * 
	 * @param sentimentType
	 */
	public void setSentimentType(String sentimentType) {
		
		this.sentimentType = sentimentType;
		
	}
	
	
	/**
	 * 
	 * Setter for the sentiment score
	 * 
	 * @param sentimentScore
	 */
	public void setSentimentScore(String sentimentScore) {
		
		this.sentimentScore = sentimentScore;
		
	}

}
