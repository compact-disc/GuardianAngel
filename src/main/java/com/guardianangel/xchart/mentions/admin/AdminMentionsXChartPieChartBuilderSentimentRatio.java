package com.guardianangel.xchart.mentions.admin;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/23/2020
 * 
 * Used to create Pie Charts from mentions data using XChart
 */

import java.awt.Color;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.PieStyler;
import com.guardianangel.cloudinary.CloudinaryUpload;
import com.guardianangel.sql.SQLConnection;

public class AdminMentionsXChartPieChartBuilderSentimentRatio {
	
	private int veryNegativeCount = 0;
	private int negativeCount = 0;
	private int neutralCount = 0;
	private int positiveCount = 0;
	private int verypositiveCount = 0;
	
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
	 * Constructor to create the image for the administrator graph for the pie chart
	 */
	public AdminMentionsXChartPieChartBuilderSentimentRatio() {
		
		countTweets();
		
		final PieChart chart = new PieChartBuilder().width(800).height(700).title("Tweet Sentiment Ratio").build();
		
		chart.getStyler().setCircular(false);
		
		int colorPos = 0;
		Color[] sliceColors = new Color[5];
		chart.getStyler().setSeriesColors(sliceColors);
		
		if(this.veryNegativeCount != 0) {
			chart.addSeries("Very Negative", this.veryNegativeCount);
			sliceColors[colorPos] = new Color(255, 51, 51);
			colorPos++;
		}
		
		if(this.negativeCount != 0) {
			chart.addSeries("Negative", this.negativeCount);
			sliceColors[colorPos] = new Color(255, 255, 102);
			colorPos++;
		}
		
		if(this.neutralCount != 0) {
			chart.addSeries("Neutral", this.neutralCount);
			sliceColors[colorPos] = new Color(177, 156, 217);
			colorPos++;
		}
		
		if(this.positiveCount != 0) {
			chart.addSeries("Positive", this.positiveCount);
			sliceColors[colorPos] = new Color(102, 204, 102);
			colorPos++;
		}
		
		if(this.verypositiveCount != 0) {
			chart.addSeries("Very Positive", this.verypositiveCount);
			sliceColors[colorPos] = new Color(102, 204, 255);
			colorPos++;
		}
		
		chart.getStyler().setAnnotationType(PieStyler.AnnotationType.LabelAndPercentage);
		chart.getStyler().setAnnotationDistance(0.8);
		chart.getStyler().setDrawAllAnnotations(true);
		
		try {
			
			BitmapEncoder.saveBitmap(chart, "./GuardianAngelUserData/UserXCharts/Admin_PieChart_Sentiment_Ratio_Mentions", BitmapFormat.PNG);
			
			CloudinaryUpload upload = new CloudinaryUpload("Admin_PieChart_Sentiment_Ratio_Mentions");
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * Count the tweets for the graph
	 */
	private void countTweets() {
		
		String sentimentType = null;
		
		try {
		
			sqlConnection = SQLConnection.getSQLConnection();
		
			statement = sqlConnection.createStatement();
			
			data = statement.executeQuery("SELECT * FROM mentions");
			
			while(data.next()) {
				
				sentimentType = data.getString("sentimenttype");
				
				if(sentimentType.equals("Very negative")) {
					
					this.veryNegativeCount++;
					
				}else if(sentimentType.equals("Negative")) {
					
					this.negativeCount++;
					
				}else if(sentimentType.equals("Neutral")) {
					
					this.neutralCount++;
					
				}else if(sentimentType.equals("Positive")) {
					
					this.positiveCount++;
					
				}else if(sentimentType.equals("Very positive")) {
					
					this.verypositiveCount++;
					
				}
				
			}
		
		} catch (SQLException se) { //catch for any error while trying to connect to the SQL server
			
			se.printStackTrace();
			
		}
		
	}

}
