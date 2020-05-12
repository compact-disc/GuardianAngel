package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/16/2020
 * 
 * Helper for the View Tweet class. Object used to store data for each tweet to display in a table format.
 */

public class AdminViewTweetsTable {
	
	/**
	 * Stores the date of the Tweet in a string format
	 */
	public String date;
	
	/**
	 * Stores the name of the Tweet in a string format
	 */
	public String name;
	
	/**
	 * Stores the content of the Tweet in a string format
	 */
	public String content;
	
	/**
	 * Stores the sentiment type of the Tweet in a string format
	 */
	public String sentimentType;
	
	/**
	 * Stores the sentiment score of the Tweet in a string format
	 */
	public String sentimentScore;
	
	/**
	 * Setter for the date
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		
		this.date = date;
		
	}
	
	/**
	 * 
	 * Setter for the name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		
		this.name = name;
		
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
