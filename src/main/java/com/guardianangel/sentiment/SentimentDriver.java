package com.guardianangel.sentiment;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * Main driver class for the sentiment analysis. This calls method from SentimentClassification and returns strings
 * with percentages for the SQL server.
 */

public class SentimentDriver {
	
	/**
	 * Stores the sentiment result
	 */
	private SentimentResult sentimentResult;
	
	/**
	 * Constructor for getting the sentiment result by passing the analyzer and String text
	 * 
	 * @param contentInput
	 * @param sentimentAnalyzer
	 */
	public SentimentDriver(String contentInput, SentimentAnalyzer sentimentAnalyzer) {
		
		this.sentimentResult = sentimentAnalyzer.getSentimentResult(contentInput);
		
	}
	
	/**
	 * Get the sentiment type
	 * 
	 * @return the sentiment type
	 */
	public String getSentimentType() {
		
		return sentimentResult.getSentimentType();
		
	}
	
	/**
	 * Get the sentiment score
	 * 
	 * @return the sentiment score
	 */
	public String getSentimentScore() {
		
		String score = Double.toString(sentimentResult.getSentimentScore());
		
		return score;
		
	}
	
	/**
	 * Get the very positive percent
	 * 
	 * @return the very positive percent
	 */
	public String getVeryPositive() {
		
		String score = Double.toString(sentimentResult.getSentimentClass().getVeryPositive());
		
		return score + "%";
		
	}
	
	/**
	 * Get the positive percent
	 * 
	 * @return the positive percent
	 */
	public String getPositive() {
		
		String score = Double.toString(sentimentResult.getSentimentClass().getPositive());
		
		return score + "%";
		
	}
	
	/**
	 * Get the neutral percent
	 * 
	 * @return the neutral percent
	 */
	public String getNeutral() {
		
		String score = Double.toString(sentimentResult.getSentimentClass().getNeutral());
		
		return score + "%";
		
	}
	
	/**
	 * Get the negative percent
	 * 
	 * @return the negative percent
	 */
	public String getNegative() {
		
		String score = Double.toString(sentimentResult.getSentimentClass().getNegative());
		
		return score + "%";
		
	}
	
	/**
	 * Get the very negative percent
	 * 
	 * @return the very negative percent
	 */
	public String getVeryNegative() {
		
		String score = Double.toString(sentimentResult.getSentimentClass().getVeryNegative());
		
		return score + "%";
		
	}

}
