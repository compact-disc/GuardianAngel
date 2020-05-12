package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * Used to build the ArrayList of all of the current users Twitter data that has been collected.
 * All of the data is injected into the thymeleaf template from the SQL server.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.auth0.jwt.interfaces.Claim;
import com.guardianangel.security.TokenAuthentication;
import com.guardianangel.sql.SQLConnection;
import com.guardianangel.util.TokenUtils;

@SuppressWarnings("unused")
@Controller
public class ViewTweetsController {
	
	/**
	 * Logger for the class
	 */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
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
	 * Stores the name of the current twitter user for display on the page
	 */
	private String name;

	/**
	 * Thymeleaf mapping for /viewtweets and method that controls the functionality on load, GET mapping
	 * 
	 * @param model the html model that is served to thymeleaf and is filled with the twitter data
	 * @param authentication the current user authenciation data
	 * @param sortOptions to show the selected sort option
	 * @return the value of the thymeleaf html page
	 */
    @RequestMapping(value = "/viewtweets", method = RequestMethod.GET)
    private String viewTweets(final Model model, final Authentication authentication, @ModelAttribute("sortOptions") ViewTweetsSortOptions sortOptions) {
        
		//Assign the authentication data from the passed parameters
		//This will be used to get user data
		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		
		//All the data is stored within a map from a JSON file, set the "claims" of all the data to a map
		//This will make the user data really easy to get from the map
        Map<String, Claim> AuthMap = tokenAuthentication.getClaims();
        
        //Get the name of the twitter user to display the view header at the top of the page
        name = AuthMap.get("name").asString();
        String viewHeader = "Viewing Twitter Timeline data for " + name;
        
        //The twitter id is stored in the string "sub" so we can get the full thing and assign it to a string
        String fullSub = AuthMap.get("sub").asString();
        
        //Remove the part of the string "twitter|" and just have a string with the user id
        this.twitterId = fullSub.replaceAll("[twitter|]", "");

        ArrayList<String> chartData = timelineChartDataAsList();
        ArrayList<ViewTweetsTable> tableData = displayData("timeline");
		
        model.addAttribute("tableData", tableData);
		model.addAttribute("chartData", chartData);
        model.addAttribute("viewHeader", viewHeader);
		
        return "viewtweets";
        
    }
    
	/**
	 * Thymeleaf mapping for /viewtweets and method that controls the functionality on load, POST mapping
	 * 
	 * @param model the html model that is served to thymeleaf and is filled with the twitter data
	 * @param authentication the current user authenciation data
	 * @param sortOptions to show the selected sort option
	 * @return the value of the thymeleaf html page
	 */
    @RequestMapping(value = "/viewtweets", method = RequestMethod.POST)
    private String sortedViewTweets(final Model model, final Authentication authentication, @ModelAttribute("sortOptions") ViewTweetsSortOptions sortOptions) {
    	
        ArrayList<String> chartData = null;
        ArrayList<ViewTweetsTable> tableData = null;
        
		//Assign the authentication data from the passed parameters
		//This will be used to get user data
		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		
		//All the data is stored within a map from a JSON file, set the "claims" of all the data to a map
		//This will make the user data really easy to get from the map
        Map<String, Claim> AuthMap = tokenAuthentication.getClaims();
        
        //Get the name of the twitter user to display the view header at the top of the page
        name = AuthMap.get("name").asString();
        String viewHeader = "Viewing Twitter " + sortOptions.getViewBy() + " data for " + name;
        
        //The twitter id is stored in the string "sub" so we can get the full thing and assign it to a string
        String fullSub = AuthMap.get("sub").asString();
        
        //Remove the part of the string "twitter|" and just have a string with the user id
        this.twitterId = fullSub.replaceAll("[twitter|]", "");
        
        if(sortOptions.getViewBy().equalsIgnoreCase("timeline")) {

            chartData = timelineChartDataAsList();
        	
        }else if(sortOptions.getViewBy().equalsIgnoreCase("retweets")) {
        	
        	chartData = retweetsChartDataAsList();
        	
        }else if(sortOptions.getViewBy().equalsIgnoreCase("likes")) {
        	
            chartData = likesChartDataAsList();
        	
        }else if(sortOptions.getViewBy().equalsIgnoreCase("mentions")) {
    
        	chartData = mentionsChartDataAsList();
        	
        }
        
        tableData = displayData(sortOptions.getViewBy().toLowerCase());
        
        model.addAttribute("tableData", tableData);
		model.addAttribute("chartData", chartData);
        model.addAttribute("viewHeader", viewHeader);
		
        return "viewtweets";
        
    }
    
    @ModelAttribute("viewByOptions")
    private String[] getViewBySortOptions() {
    	
    	return new String[] {"Timeline", "Retweets", "Likes", "Mentions"};
    	
    }
    
	/**
	 * 
	 * displayData to get the data from the respective SQL table for viewing on the view tweets page
	 * 
	 * @param SQLTable a string that takes in which SQL table is being queried
	 * @return
	 */
    private ArrayList<ViewTweetsTable> displayData(String SQLTable) {
    	
    	ArrayList<ViewTweetsTable> tweetData = new ArrayList<>();
    	
		String name = null;
		String content = null;
		String sentimentType = null;
		String sentimentScore = null;
		String date = null;
		
		String backgroundColor = "#ffffff"; //white, default, neutral
		int color = 0;
		
		try {
			
			sqlConnection = SQLConnection.getSQLConnection();
			
			statement = sqlConnection.createStatement();
			
			data = statement.executeQuery("SELECT * FROM " + SQLTable + " WHERE twitterid = " + "'" + this.twitterId + "'");
			
			while(data.next()) {
				
				ViewTweetsTable tableData = new ViewTweetsTable();
				
				name = data.getString("name");
				content = data.getString("content");
				sentimentType = data.getString("sentimenttype");
				sentimentScore = data.getString("sentimentscore");
				date = data.getString("date");
				
				/*
				 * "Very negative" = 0 "Negative" = 1 "Neutral" = 2 "Positive" = 3 "Very positive" = 4
				 */
				
				if(sentimentScore.contains("0.0")) { //red
					color = 0;
				}else if(sentimentScore.contains("1")) { //yellow
					color = 1;
				}else if(sentimentScore.contains("2")) { //purple
					color = 2;
				}else if(sentimentScore.contains("3")) { //green
					color = 3;
				}else if(sentimentScore.contains("4")) { //blue
					color = 4;
				}
				
				switch(color) {
					case 0:
						backgroundColor = "#ff3333"; //red
						break;
					case 1:
						backgroundColor = "#ffff66"; //yellow
						break;
					case 2:
						backgroundColor = "#b19cd9"; //purple
						break;
					case 3:
						backgroundColor = "#66cc66"; //green 
						break;
					case 4:
						backgroundColor = "#66ccff"; //blue
						break;
				}
				
				tableData.setDate("<div class=\"tweet\" style=\"background-color: "+ backgroundColor +";\"> " + date + "</div>");
				tableData.setContent("<div class=\"tweet\" style=\"background-color: "+ backgroundColor +";\"> " + content + "</div>");
				tableData.setSentimentType("<div class=\"tweet\" style=\"background-color: "+ backgroundColor +";\"> " + sentimentType + "</div>");
				tableData.setSentimentScore("<div class=\"tweet\" style=\"background-color: "+ backgroundColor +";\"> " + sentimentScore + "</div>");
				
				tweetData.add(tableData);
				
			}
			
		} catch (SQLException se) { //catch for any error while trying to connect to the SQL server
			
			se.printStackTrace();
			
		}
		
		return tweetData;
    	
    }
    
    /**
     * 
     * Return the address of the graphs in a string format in an HTML format to the template for timelines
     * 
     * @return
     */
    private ArrayList<String> timelineChartDataAsList() {
    	
    	ArrayList<String> chartData = new ArrayList<>();
    	
    	Random random = new Random();
    	int v = random.nextInt(Integer.MAX_VALUE - 1);
    	
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_PieChart_Sentiment_Ratio_Timeline.png\" alt=\"Sentiment Ratio\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Tweets_Per_Month_Timeline.png\" alt=\"Tweets Per Month\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Sentiment_Per_Month_Timeline.png\" alt=\"Sentiment Per Month\"></img>");
    	
    	return chartData;
    	
    }
    
    /**
     * 
     * Return the address of the graphs in a string format in an HTML format to the template for likes
     * 
     * @return
     */
    private ArrayList<String> likesChartDataAsList() {
    	
    	ArrayList<String> chartData = new ArrayList<>();
    	
    	Random random = new Random();
    	int v = random.nextInt(Integer.MAX_VALUE - 1);
    	
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_PieChart_Sentiment_Ratio_Likes.png\" alt=\"Sentiment Ratio\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Tweets_Per_Month_Likes.png\" alt=\"Tweets Per Month\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Sentiment_Per_Month_Likes.png\" alt=\"Sentiment Per Month\"></img>");
    	
    	return chartData;
    	
    }
    
    /**
     * 
     * Return the address of the graphs in a string format in an HTML format to the template for retweets
     * 
     * @return
     */
    private ArrayList<String> retweetsChartDataAsList() {
    	
    	ArrayList<String> chartData = new ArrayList<>();
    	
    	Random random = new Random();
    	int v = random.nextInt(Integer.MAX_VALUE - 1);
    	
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_PieChart_Sentiment_Ratio_Retweets.png\" alt=\"Sentiment Ratio\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Tweets_Per_Month_Retweets.png\" alt=\"Tweets Per Month\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Sentiment_Per_Month_Retweets.png\" alt=\"Sentiment Per Month\"></img>");
    	
    	return chartData;
    	
    }
    
    /**
     * 
     * Return the address of the graphs in a string format in an HTML format to the template for mentions
     * 
     * @return
     */
    private ArrayList<String> mentionsChartDataAsList() {
    	
    	ArrayList<String> chartData = new ArrayList<>();
    	
    	Random random = new Random();
    	int v = random.nextInt(Integer.MAX_VALUE - 1);
    	
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_PieChart_Sentiment_Ratio_Mentions.png\" alt=\"Sentiment Ratio\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Tweets_Per_Month_Mentions.png\" alt=\"Tweets Per Month\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/" + this.twitterId + "_LineGraph_Sentiment_Per_Month_Mentions.png\" alt=\"Sentiment Per Month\"></img>");
    	
    	return chartData;
    	
    }

}
