package com.guardianangel.sentiment;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * This object is used to store all of the data from the sentiment analysis once it has been complete.
 * Getter methods are called by other classes to use data.
 */

public class SentimentClassification {
	
	/**
	 * Stores the very positive percent
	 */
	protected double veryPositive;
	
	/**
	 * Stores the positive percent
	 */
	protected double positive;
	
	/**
	 * Stores the neutral percent
	 */
	protected double neutral;
	
	/**
	 * Stores the negative percent
	 */
	protected double negative;
	
	/**
	 * Stores the very negative percent
	 */
	protected double veryNegative;
	
	/**
	 * Returns the very positive percent
	 * 
	 * @return the very positive percent
	 */
	public double getVeryPositive() {
		
		return veryPositive;
		
	}
	
	/**
	 * Sets the very positive percent
	 * 
	 * @param veryPositive the percent very positive to set
	 */
	public void setVeryPositive(double veryPositive) {
		
		this.veryPositive = veryPositive;
		
	}
	
	/**
	 * Gets the positive percent
	 * 
	 * @return the positive percent
	 */
	public double getPositive() {
		
		return positive;
		
	}
	
	/**
	 * Sets the positive percent
	 * 
	 * @param positive the percent positive to set
	 */
	public void setPositive(double positive) {
		
		this.positive = positive;
		
	}
	
	/**
	 * Get the neutral percent
	 * 
	 * @return the neutral percent
	 */
	public double getNeutral() {
		
		return neutral;
		
	}
	
	/**
	 * Set the neutral percent
	 * 
	 * @param neutral the percent neutral to set
	 */
	public void setNeutral(double neutral) {
		
		this.neutral = neutral;
		
	}
	
	/**
	 * Get the negative percent
	 * 
	 * @return the negative percent
	 */
	public double getNegative() {
		
		return negative;
		
	}
	
	/**
	 * Set the negative percent
	 * 
	 * @param negative the percent negative to set
	 */
	public void setNegative(double negative) {
		
		this.negative = negative;
		
	}
	
	/**
	 * Get the very negative percent
	 * 
	 * @return the very negative percent
	 */
	public double getVeryNegative() {
		
		return veryNegative;
		
	}
	
	/**
	 * Set the very negative percent
	 * 
	 * @param veryNegative the percent very negative to set
	 */
	public void setVeryNegative(double veryNegative) {
		
		this.veryNegative = veryNegative;
		
	}

}
