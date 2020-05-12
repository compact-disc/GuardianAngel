package com.guardianangel.sentiment;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * Used to create an instance of the sentiment analyzer to use for the analysis of tweets passed.
 */

public class SentimentAnalyzerInstance {
	
	/**
	 * Object of the SentimentAnalyzer
	 */
	private SentimentAnalyzer sentimentAnalyzer = null;
	
	/**
	 * Constructor that creates the SentimentAnalyzer object and initializes it
	 */
	public SentimentAnalyzerInstance() {
		
		this.sentimentAnalyzer = new SentimentAnalyzer();
		sentimentAnalyzer.initialize();
		
	}
	
	/**
	 * Gets the SentimentAnalyzer
	 * 
	 * @return the SentimentAnalyzer object for use
	 */
	public SentimentAnalyzer getSentimentAnalyzer() {
		
		return this.sentimentAnalyzer;
		
	}

}
