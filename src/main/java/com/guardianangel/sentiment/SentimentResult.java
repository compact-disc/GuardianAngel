package com.guardianangel.sentiment;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * This object stores the results of the sentiment analysis of a specific tweets.
 */

public class SentimentResult {
	
	/**
	 * Sentiment score of given content
	 */
	double sentimentScore;
	
	/**
	 * Sentiment type of given content
	 */
	String sentimentType;
	
	/**
	 * Object for sentiment classificiation of content
	 */
	SentimentClassification sentimentClass;

	/**
	 * Get the sentiment score
	 * 
	 * @return the sentiment score
	 */
	public double getSentiment() {
		
		return sentimentScore;
		
	}

	/**
	 * Get the sentiment score
	 * 
	 * @return the sentiment score
	 */
	public double getSentimentScore() {
		
		return sentimentScore;
		
	}

	/**
	 * Set the sentiment score
	 * 
	 * @param sentimentScore of a given sentiment score
	 */
	public void setSentimentScore(double sentimentScore) {
		
		this.sentimentScore = sentimentScore;
		
	}

	/**
	 * Get the sentiment type
	 * 
	 * @return the sentiment type
	 */
	public String getSentimentType() {
		
		return sentimentType;
		
	}

	/**
	 * Set the sentiment type
	 * 
	 * @param sentimentType from a given sentiment type
	 */
	public void setSentimentType(String sentimentType) {
		
		this.sentimentType = sentimentType;
		
	}

	/**
	 * Get the sentiment class
	 * 
	 * @return the sentiment classification
	 */
	public SentimentClassification getSentimentClass() {
		
		return sentimentClass;
		
	}

	/**
	 * Set the sentiment class
	 * 
	 * @param sentimentClass given a sentiment classification
	 */
	public void setSentimentClass(SentimentClassification sentimentClass) {
		
		this.sentimentClass = sentimentClass;
		
	}

}
