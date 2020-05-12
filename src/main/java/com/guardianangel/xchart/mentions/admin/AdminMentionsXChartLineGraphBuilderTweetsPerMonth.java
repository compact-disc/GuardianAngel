package com.guardianangel.xchart.mentions.admin;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/23/2020
 * 
 * Used to create Line Graphs from mentions data using XChart
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
import com.guardianangel.cloudinary.CloudinaryUpload;
import com.guardianangel.sql.SQLConnection;

public class AdminMentionsXChartLineGraphBuilderTweetsPerMonth {
	
	private int jan = 0;
	private int feb = 0;
	private int mar = 0;
	private int apr = 0;
	private int may = 0;
	private int jun = 0;
	private int jul = 0;
	private int aug = 0;
	private int sep = 0;
	private int oct = 0;
	private int nov = 0;
	private int dec = 0;
	
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
	 * Constructor to create the image to create the administration tweets per month
	 */
	public AdminMentionsXChartLineGraphBuilderTweetsPerMonth() {
		
		countTweetsByMonth();
		
		final XYChart chart = new XYChartBuilder().width(800).height(650).title("Tweets Per Month - Original Tweet Date").build();
		
		chart.setXAxisTitle("Month");
		chart.setYAxisTitle("Number of Tweets");
		chart.getStyler().setLegendVisible(false);
		chart.getStyler().setXAxisTickMarkSpacingHint(50);
		chart.getStyler().setYAxisTickMarkSpacingHint(100);
		
		Color[] sliceColors = new Color[] {new Color(0, 0, 0)};
		chart.getStyler().setSeriesColors(sliceColors);
		
		chart.addSeries("Tweets", new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[] {jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec});
		
		try {
			
			BitmapEncoder.saveBitmap(chart, "./GuardianAngelUserData/UserXCharts/Admin_LineGraph_Tweets_Per_Month_Mentions", BitmapFormat.PNG);
			
			CloudinaryUpload upload = new CloudinaryUpload("Admin_LineGraph_Tweets_Per_Month_Mentions");
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	private void countTweetsByMonth() {
		
		String month = null;
		
		try {
		
			sqlConnection = SQLConnection.getSQLConnection();
		
			statement = sqlConnection.createStatement();
			
			data = statement.executeQuery("SELECT * FROM mentions");
			
			while(data.next()) {
				
				month = data.getString("date");
				
				if(month.contains("Jan")) {
					
					this.jan++;
					
				}else if(month.contains("Feb")) {
					
					this.feb++;
					
				}else if(month.contains("Mar")) {
					
					this.mar++;
					
				}else if(month.contains("Apr")) {
					
					this.apr++;
					
				}else if(month.contains("May")) {
					
					this.may++;
					
				}else if(month.contains("Jun")) {
					
					this.jun++;
					
				}else if(month.contains("Jul")) {
					
					this.jul++;
					
				}else if(month.contains("Aug")) {
					
					this.aug++;
					
				}else if(month.contains("Sep")) { 
					
					this.sep++;
					
				}else if(month.contains("Oct")) {
					
					this.oct++;
					
				}else if(month.contains("Nov")) {
					
					this.nov++;
					
				}else if(month.contains("Dec")) {
					
					this.dec++;
					
				}
				
			}
		
		} catch (SQLException se) { //catch for any error while trying to connect to the SQL server
			
			se.printStackTrace();
			
		}
		
	}

}
