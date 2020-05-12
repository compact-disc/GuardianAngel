package com.guardianangel.xchart.retweets;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/2/2020
 * 
 * Used to create line graphs for sentiment over time for the retweets data
 */

import java.awt.Color;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.Styler.LegendPosition;
import com.guardianangel.cloudinary.CloudinaryUpload;
import com.guardianangel.sql.SQLConnection;

public class RetweetsXChartLineGraphBuilderSentimentPerMonth {
	
    /**
     * Stores the SQL connection for use
     */
    private Connection sqlConnection = null;
    
    /**
     * Result of the SQL query that can be iterated over
     */
	private ResultSet data = null;
	
	/**
	 * Query string that will be sent to the SQL server
	 */
	private Statement statement = null;
	
	/**
	 * Stores the twitterId of the current user
	 */
	private String twitterId;
	
	/**
	 * Constructor to create the image that counts the sentiment per month taking in the twitter id
	 * 
	 * @param twitterId
	 */
	public RetweetsXChartLineGraphBuilderSentimentPerMonth(String twitterId) {
		
		int[] veryNegativeCounts;
		int[] negativeCounts;
		int[] neutralCounts;
		int[] positiveCounts;
		int[] veryPositiveCounts;
		
		this.twitterId = twitterId;
		
		final XYChart chart = new XYChartBuilder().width(800).height(650).title("Sentiment Per Month").build();
		
		chart.setXAxisTitle("Month");
		chart.setYAxisTitle("Number of Tweets");
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setXAxisTickMarkSpacingHint(50);
		chart.getStyler().setYAxisTickMarkSpacingHint(100);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
		
		Color[] sliceColors = new Color[5];
		chart.getStyler().setSeriesColors(sliceColors);
		
		int colorPos = 0;
		
		veryNegativeCounts = countSentimentByMonth("Very negative");
		for(int i = 0; i < veryNegativeCounts.length; i++) {
		
			if(veryNegativeCounts[i] > 0) {
				
				chart.addSeries("Very Negative", new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, veryNegativeCounts);
				sliceColors[colorPos] = new Color(255, 51, 51);
				colorPos++;
				break;
				
			}
			
		}
		
		negativeCounts = countSentimentByMonth("Negative");
		for(int i = 0; i < negativeCounts.length; i++) {
			
			if(negativeCounts[i] > 0) {
				
				chart.addSeries("Negative", new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, negativeCounts);
				sliceColors[colorPos] = new Color(255, 255, 102);
				colorPos++;
				break;
				
			}
			
			
		}
		
		
		neutralCounts = countSentimentByMonth("Neutral");
		for(int i = 0; i < neutralCounts.length; i++) {
			
			if(neutralCounts[i] > 0) {
				
				chart.addSeries("Neutral", new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, neutralCounts);
				sliceColors[colorPos] = new Color(177, 156, 217);
				colorPos++;
				break;
				
			}
			
		}
		
		
		positiveCounts = countSentimentByMonth("Positive");
		for(int i = 0; i < positiveCounts.length; i++) {
			
			if(positiveCounts[i] > 0) {
				
				chart.addSeries("Positive", new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, positiveCounts);
				sliceColors[colorPos] = new Color(102, 204, 102);
				colorPos++;
				break;
				
			}
			
		}
		
		veryPositiveCounts = countSentimentByMonth("Very positive");
		for(int i = 0; i < veryPositiveCounts.length; i++) {
			
			if(veryPositiveCounts[i] > 0) {
				
				chart.addSeries("Very Positive", new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, veryPositiveCounts);
				sliceColors[colorPos] = new Color(102, 204, 255);
				colorPos++;
				break;
				
			}
			
		}
		
		
		try {
			
			BitmapEncoder.saveBitmap(chart, "./GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Sentiment_Per_Month_Retweets", BitmapFormat.PNG);
			
			CloudinaryUpload upload = new CloudinaryUpload(this.twitterId + "_LineGraph_Sentiment_Per_Month_Retweets");
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * Count the sentiment by month and takes in the current sentiment
	 * 
	 * @param currentSentiment takes in the current sentiment to count
	 * @return
	 */
	private int[] countSentimentByMonth(String currentSentiment) {
		
		String month = null;
		String sentimentType = null;
		int[] sentimentValues = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int value;
		
		try {
		
			sqlConnection = SQLConnection.getSQLConnection();
		
			statement = sqlConnection.createStatement();
			
			data = statement.executeQuery("SELECT * FROM retweets WHERE twitterid = " + "'" + this.twitterId + "'");
			
			while(data.next()) {
				
				month = data.getString("date");
				sentimentType = data.getString("sentimenttype");
				
			if(month.contains("Jan") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[0];
					value++;
					sentimentValues[0] = value;
					
				}else if(month.contains("Feb") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[1];
					value++;
					sentimentValues[1] = value;
					
				}else if(month.contains("Mar") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[2];
					value++;
					sentimentValues[2] = value;
					
				}else if(month.contains("Apr") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[3];
					value++;
					sentimentValues[3] = value;
					
				}else if(month.contains("May") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[4];
					value++;
					sentimentValues[4] = value;
					
				}else if(month.contains("Jun") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[5];
					value++;
					sentimentValues[5] = value;
					
				}else if(month.contains("Jul") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[6];
					value++;
					sentimentValues[6] = value;
					
				}else if(month.contains("Aug") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[7];
					value++;
					sentimentValues[7] = value;
					
				}else if(month.contains("Sep") && sentimentType.equals(currentSentiment)) { 
					
					value = sentimentValues[8];
					value++;
					sentimentValues[8] = value;
					
				}else if(month.contains("Oct") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[9];
					value++;
					sentimentValues[9] = value;
					
				}else if(month.contains("Nov") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[10];
					value++;
					sentimentValues[10] = value;
					
				}else if(month.contains("Dec") && sentimentType.equals(currentSentiment)) {
					
					value = sentimentValues[11];
					value++;
					sentimentValues[11] = value;
					
				}
				
			}
		
		} catch (SQLException se) { //catch for any error while trying to connect to the SQL server
			
			se.printStackTrace();
			
		}
		
		return sentimentValues;
		
	}

}
