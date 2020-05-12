package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 11/21/2019
 * 
 * Used to control the administrator page functionality of the Guardian Angel Application.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.auth0.jwt.interfaces.Claim;
import com.guardianangel.security.TokenAuthentication;
import com.guardianangel.sql.SQLConnection;
import com.guardianangel.twitter.*;
import com.guardianangel.xchart.likes.*;
import com.guardianangel.xchart.likes.admin.*;
import com.guardianangel.xchart.timeline.*;
import com.guardianangel.xchart.timeline.admin.*;
import com.guardianangel.xchart.retweets.*;
import com.guardianangel.xchart.retweets.admin.*;
import com.guardianangel.xchart.mentions.*;
import com.guardianangel.xchart.mentions.admin.*;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

@SuppressWarnings("unused")
@Controller
public class AdminController {
	
	/**
	 * The connection to the SQL server
	 */
	private Connection sqlConnection = null;
	
	/**
	 * Variable used to store the data received from an SQL query
	 */
	private ResultSet data = null;
	
	/**
	 * The SQL statement that will be sent to query the SQL server
	 */
	private Statement statement = null;
	
	/**
	 * The logger for this class
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Used to load the administration page from the thymeleaf template
	 * 
	 * @param model the model for the thymeleaf template to be filled
	 * @param authentication logged in user authentication data
	 * @param sortOptions to show the selected sort option
	 * @return
	 */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    private String admin(final Model model, final Authentication authentication, @ModelAttribute("sortOptions") ViewTweetsSortOptions sortOptions) {
    	
        ArrayList<AdminViewTweetsTable> tableData = null;
        ArrayList<String> chartData = null;
    	
    	PullOtherUsername otherUsername = new PullOtherUsername();
    	model.addAttribute("otherUsername", otherUsername);
    	
    	AddAdmin adminUsername = new AddAdmin();
    	model.addAttribute("adminUsername", adminUsername);
    	
		//Assign the authentication data from the passed parameters
		//This will be used to get user data
		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		
		//All the data is stored within a map from a JSON file, set the "claims" of all the data to a map
		//This will make the user data really easy to get from the map
        Map<String, Claim> AuthMap = tokenAuthentication.getClaims();
        
        //The twitter id is stored in the string "sub" so we can get the full thing and assign it to a string
        String fullSub = AuthMap.get("sub").asString();
        
        //Remove the part of the string "twitter|" and just have a string with the user id
        String twitterId = fullSub.replaceAll("[twitter|]", "");
        
        //If the user is an administrator then go to the administrator page
        //Else return back to the index of the site
        if(checkAdmin(twitterId)) {
        	
        	tableData = displayData("timeline");
        	chartData = timelineChartDataAsList();
        	
    		model.addAttribute("tableData", tableData);
    		model.addAttribute("chartData", chartData);
        	
        	return "admin";
        	
        }else {
        	
        	return "redirect:/";
        	
        }
    	
    }
    
	/**
	 * Used to load the administration page from the thymeleaf template
	 * 
	 * @param model the model for the thymeleaf template to be filled
	 * @param authentication logged in user authentication data
	 * @param sortOptions to show the selected sort option
	 * @return
	 */
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    private String sortedAdmin(final Model model, final Authentication authentication, @ModelAttribute("sortOptions") ViewTweetsSortOptions sortOptions) {
    	
    	ArrayList<AdminViewTweetsTable> tableData = null;
        ArrayList<String> chartData = null;
    	
    	PullOtherUsername otherUsername = new PullOtherUsername();
    	model.addAttribute("otherUsername", otherUsername);
    	
    	AddAdmin adminUsername = new AddAdmin();
    	model.addAttribute("adminUsername", adminUsername);
    	
		//Assign the authentication data from the passed parameters
		//This will be used to get user data
		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		
		//All the data is stored within a map from a JSON file, set the "claims" of all the data to a map
		//This will make the user data really easy to get from the map
        Map<String, Claim> AuthMap = tokenAuthentication.getClaims();
        
        //The twitter id is stored in the string "sub" so we can get the full thing and assign it to a string
        String fullSub = AuthMap.get("sub").asString();
        
        //Remove the part of the string "twitter|" and just have a string with the user id
        String twitterId = fullSub.replaceAll("[twitter|]", "");
        
        //If the user is an administrator then go to the administrator page
        //Else return back to the index of the site
        if(checkAdmin(twitterId)) {
        	
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
        	
        	return "admin";
        	
        }else {
        	
        	return "redirect:/";
        	
        }
    	
    }
    
    /**
     * 
     * Model Attribute for thymeleaf for sort selection options
     * 
     * @return
     */
    @ModelAttribute("viewByOptions")
    private String[] getViewBySortOptions() {
    	
    	return new String[] {"Timeline", "Retweets", "Likes", "Mentions"};
    	
    }
    
    /**
     * Used to pull the tweets of any public user on twitter
     * 
     * @param otherUsername the name of the specified user to get tweets from
     * @param authentication data from the authentication of the logged in user
     * @param sortOptions to show the selected sort option
     * @return
     */
	@RequestMapping(value = "/adminPullOtherTweets", method = RequestMethod.POST)
	private String pullOtherTweets(@ModelAttribute(value="otherUsername") PullOtherUsername otherUsername, final Authentication authentication, @ModelAttribute("sortOptions") ViewTweetsSortOptions sortOptions) {
		
		//Assign the authentication data from the passed parameters
		//This will be used to get user data
		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		
		//All the data is stored within a map from a JSON file, set the "claims" of all the data to a map
		//This will make the user data really easy to get from the map
        Map<String, Claim> AuthMap = tokenAuthentication.getClaims();
        
        //The twitter id is stored in the string "sub" so we can get the full thing and assign it to a string
        String fullSub = AuthMap.get("sub").asString();
        
        //Remove the part of the string "twitter|" and just have a string with the user id
        String twitterId = fullSub.replaceAll("[twitter|]", "");
		
        //If the user is an administrator then go to the administrator page
        //Else return back to the index of the site
        if(checkAdmin(twitterId)) {
        	
        	String otherTwitterID = GetUserTwitterIDByScreenName.getID(otherUsername.getUsername());
        	
            PullTwitterTimelineDriver timelineDriverAdmin = new PullTwitterTimelineDriver(otherUsername.getUsername() , "username"); //retweets is included in this call
            PullTwitterLikesDriver likesDriverAdmin = new PullTwitterLikesDriver(otherUsername.getUsername(), "username");
            PullTwitterMentionsDriver mentionsDriverAdmin = new PullTwitterMentionsDriver(otherUsername.getUsername(), "username");
            
            //Create the pie chart of sentiment ratio for timeline data
            TimelineXChartPieChartBuilderSentimentRatio timelineSentimentRatio = new TimelineXChartPieChartBuilderSentimentRatio(otherTwitterID);
            //Create the line graph of sentiment per month for timeline data
            TimelineXChartLineGraphBuilderTweetsPerMonth timelineTweetsPerMonth = new TimelineXChartLineGraphBuilderTweetsPerMonth(otherTwitterID);
            //Create line graph of sentiment over time for timeline data
            TimelineXChartLineGraphBuilderSentimentPerMonth timelineSentimentOverTimeLineGraph = new TimelineXChartLineGraphBuilderSentimentPerMonth(otherTwitterID);
            
            //Create the pie chart of sentiment ratio for likes data
            LikesXChartPieChartBuilderSentimentRatio likesSentimentRatio = new LikesXChartPieChartBuilderSentimentRatio(otherTwitterID);
            //Create the line graph of sentiment per month for likes data
            LikesXChartLineGraphBuilderTweetsPerMonth likesTweetsPerMonth = new LikesXChartLineGraphBuilderTweetsPerMonth(otherTwitterID);
            //Create line graph of sentiment over time for likes data
            LikesXChartLineGraphBuilderSentimentPerMonth likesSentimentOverTimeLineGraph = new LikesXChartLineGraphBuilderSentimentPerMonth(otherTwitterID);
            
            //Create the pie chart of sentiment ratio for retweets data
            RetweetsXChartPieChartBuilderSentimentRatio retweetsSentimentRatio = new RetweetsXChartPieChartBuilderSentimentRatio(otherTwitterID);
            //Create the line graph of sentiment per month for retweets data
            RetweetsXChartLineGraphBuilderTweetsPerMonth retweetsTweetsPerMonth = new RetweetsXChartLineGraphBuilderTweetsPerMonth(otherTwitterID);
            //Create line graph of sentiment over time for retweets data
            RetweetsXChartLineGraphBuilderSentimentPerMonth retweetsSentimentOverTimeLineGraph = new RetweetsXChartLineGraphBuilderSentimentPerMonth(otherTwitterID);
            
            //Create the pie chart of sentiment ratio for mentions data
            MentionsXChartPieChartBuilderSentimentRatio mentionsSentimentRatio = new MentionsXChartPieChartBuilderSentimentRatio(otherTwitterID);
            //Create the line graph of sentiment per month for mentions data
            MentionsXChartLineGraphBuilderTweetsPerMonth mentionsTweetsPerMonth = new MentionsXChartLineGraphBuilderTweetsPerMonth(otherTwitterID);
            //Create line graph of sentiment over time for mentions data
            MentionsXChartLineGraphBuilderSentimentPerMonth mentionsSentimentOverTimeLineGraph = new MentionsXChartLineGraphBuilderSentimentPerMonth(otherTwitterID);
            
            //Create the pie chart of sentiment ratio for timeline data for all data
            AdminTimelineXChartPieChartBuilderSentimentRatio timelineSentimentRatioAdmin = new AdminTimelineXChartPieChartBuilderSentimentRatio();
            //Create the line graph of sentiment per month for timeline data for all data
            AdminTimelineXChartLineGraphBuilderTweetsPerMonth timelineTweetsPerMonthAdmin = new AdminTimelineXChartLineGraphBuilderTweetsPerMonth();
            //Create line graph of sentiment over time for timeline data for all data
            AdminTimelineXChartLineGraphBuilderSentimentPerMonth timelineSentimentOverTimeLineGraphAdmin = new AdminTimelineXChartLineGraphBuilderSentimentPerMonth();
            
            //Create the pie chart of sentiment ratio for likes data for all data
            AdminLikesXChartPieChartBuilderSentimentRatio likesSentimentRatioAdmin = new AdminLikesXChartPieChartBuilderSentimentRatio();
            //Create the line graph of sentiment per month for likes data for all data
            AdminLikesXChartLineGraphBuilderTweetsPerMonth likesTweetsPerMonthAdmin = new AdminLikesXChartLineGraphBuilderTweetsPerMonth();
            //Create line graph of sentiment over time for likes data for all data
            AdminLikesXChartLineGraphBuilderSentimentPerMonth likesSentimentOverTimeLineGraphAdmin = new AdminLikesXChartLineGraphBuilderSentimentPerMonth();
            
            //Create the pie chart of sentiment ratio for retweets data for all data
            AdminRetweetsXChartPieChartBuilderSentimentRatio retweetsSentimentRatioAdmin = new AdminRetweetsXChartPieChartBuilderSentimentRatio();
            //Create the line graph of sentiment per month for retweets data for all data
            AdminRetweetsXChartLineGraphBuilderTweetsPerMonth retweetsTweetsPerMonthAdmin = new AdminRetweetsXChartLineGraphBuilderTweetsPerMonth();
            //Create line graph of sentiment over time for retweets data for all data
            AdminRetweetsXChartLineGraphBuilderSentimentPerMonth retweetsSentimentOverTimeLineGraphAdmin = new AdminRetweetsXChartLineGraphBuilderSentimentPerMonth();
            
            //Create the pie chart of sentiment ratio for mentions data for all data
            AdminMentionsXChartPieChartBuilderSentimentRatio mentionsSentimentRatioAdmin = new AdminMentionsXChartPieChartBuilderSentimentRatio();
            //Create the line graph of sentiment per month for mentions data for all data
            AdminMentionsXChartLineGraphBuilderTweetsPerMonth mentionsTweetsPerMonthAdmin = new AdminMentionsXChartLineGraphBuilderTweetsPerMonth();
            //Create line graph of sentiment over time for mentions data for all data
            AdminMentionsXChartLineGraphBuilderSentimentPerMonth mentionsSentimentOverTimeLineGraphAdmin = new AdminMentionsXChartLineGraphBuilderSentimentPerMonth();
        	
        	return "redirect:/admin";
        	
        }else {
        	
        	return "redirect:/";
        	
        }
		
	}
	
	/**
	 * 
	 * Used to add an administrator to allowed SQL table
	 * 
	 * @param adminUsername the username of the admin that is being added
	 * @param authentication the authentication data for the user
	 * @param sortOptions to show the selected sort option
	 * @return String which loads the thymeleaf html page, can also accept redirects
	 */
	@RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
	private String addAdmin(@ModelAttribute(value="adminUsername") AddAdmin adminUsername, final Authentication authentication, @ModelAttribute("sortOptions") ViewTweetsSortOptions sortOptions) {
		
		//Assign the authentication data from the passed parameters
		//This will be used to get user data
		TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
		
		//All the data is stored within a map from a JSON file, set the "claims" of all the data to a map
		//This will make the user data really easy to get from the map
        Map<String, Claim> AuthMap = tokenAuthentication.getClaims();
        
        //The twitter id is stored in the string "sub" so we can get the full thing and assign it to a string
        String fullSub = AuthMap.get("sub").asString();
        
        //Remove the part of the string "twitter|" and just have a string with the user id
        String twitterId = fullSub.replaceAll("[twitter|]", "");
		
        //If the user is an administrator then go to the administrator page
        //Else return back to the index of the site
        if(checkAdmin(twitterId)) {
        	
        	addAdmin(adminUsername.getUsername());
        	
        	return "redirect:/admin";
        	
        }else {
        	
        	return "redirect:/";
        	
        }
		
	}
    
    /**
     * Check if the user trying to load the page is an administrator
     * 
     * @param twitterId the id of the user trying to use the page
     * @return boolean either true or false to determine if user is an admin
     */
	private boolean checkAdmin(String twitterId) {
		
		//Variable to collect the user id for the Twitter user
		String id;
		
		//try to connect to the server
		try {
			
			//Connect to the SQL server
			sqlConnection = SQLConnection.getSQLConnection();
			
			//Create a statement from the SQL connection
			statement = sqlConnection.createStatement();
			//Query the SQL server and collect all of the entrys for admin
			data = statement.executeQuery("SELECT * FROM admin");
			
			//While there is more data keep looping through
			while(data.next()) {
				
				//Get the id entry
				id = data.getString("id");
				
				//If the id is equal to the currently logged in user return true
				if(id.equals(twitterId)) {
					
					return true;
					
				}
				
			}
			
		} catch (SQLException se) { //catch for any error while trying to connect to the SQL server
			
			se.printStackTrace();
			
		}
		
		//Return false if the user is not in the admin database
		return false;
		
	}
	
	/**
	 * This adds the specified user by twitter id to the admin database
	 * 
	 * @param username which takes in the username of the admin being added
	 */
	private void addAdmin(String username) {
		
		String sqlData;
		Statement checkStatement;
		String id = null;
		String checkId = null;
		
		try {
			
			Twitter twitter = TwitterConnection.getTwitterInstance();
			User user = twitter.showUser(username);
			id = Long.toString(user.getId());
		
		}catch(TwitterException te) {
			
			te.printStackTrace();
			
		}
		
		sqlData = "INSERT INTO admin (name,id) VALUES ('"+ username +"','"+ id +"')";
		
		try {
			
			checkStatement = SQLConnection.getSQLConnection().createStatement();
			data = checkStatement.executeQuery("SELECT * FROM admin");
			
			while(data.next()) {
				
				checkId = data.getString("id");
				
				if(id.equals(checkId)) {
					
					return;
					
				}
				
			}
			
			//Within the SQL connect create a new statement to be written
			statement = SQLConnection.getSQLConnection().createStatement();
			
			//Execute the created statement into the SQL database
			statement.execute(sqlData);
			
		
		} catch (SQLException se) {
			
			se.printStackTrace();
			
		}
		
	}
	
	
	/**
	 * 
	 * displayData to get the data from the respective SQL table for viewing on the administration page
	 * 
	 * @param SQLTable a string that takes in which SQL table is being queried
	 * @return
	 */
    private ArrayList<AdminViewTweetsTable> displayData(String SQLTable) {
    	
    	String userThatInteractedName = "none";
    	
    	ArrayList<AdminViewTweetsTable> tweetData = new ArrayList<>();
    	
		String name = null;
		String content = null;
		String sentimentType = null;
		String sentimentScore = null;
		String date = null;
		String twitterId = null;
		String interactingUser = null;
		
		String backgroundColor = "#ffffff"; //white, default, neutral
		int color = 0;
		
		try {
			
			sqlConnection = SQLConnection.getSQLConnection();
			
			statement = sqlConnection.createStatement();
			
			data = statement.executeQuery("SELECT * FROM " + SQLTable);
			
			while(data.next()) {
				
				AdminViewTweetsTable tableData = new AdminViewTweetsTable();
				
				content = data.getString("content");
				sentimentType = data.getString("sentimenttype");
				sentimentScore = data.getString("sentimentscore");
				date = data.getString("date");
				twitterId = data.getString("twitterId");
				name = data.getString("interactinguser");
				
				
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
				tableData.setName("<div class=\"tweet\" style=\"background-color: "+ backgroundColor +";\"> " + name + "</div>");
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
    	
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_PieChart_Sentiment_Ratio_Timeline.png\" alt=\"Sentiment Ratio\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_LineGraph_Tweets_Per_Month_Timeline.png\" alt=\"Tweets Per Month\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_LineGraph_Sentiment_Per_Month_Timeline.png\" alt=\"Sentiment Per Month\"></img>");
    	
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
    	
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_PieChart_Sentiment_Ratio_Likes.png\" alt=\"Sentiment Ratio\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_LineGraph_Tweets_Per_Month_Likes.png\" alt=\"Tweets Per Month\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_LineGraph_Sentiment_Per_Month_Likes.png\" alt=\"Sentiment Per Month\"></img>");
    	
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
    	
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_PieChart_Sentiment_Ratio_Retweets.png\" alt=\"Sentiment Ratio\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_LineGraph_Tweets_Per_Month_Retweets.png\" alt=\"Tweets Per Month\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_LineGraph_Sentiment_Per_Month_Retweets.png\" alt=\"Sentiment Per Month\"></img>");
    	
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
    	
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_PieChart_Sentiment_Ratio_Mentions.png\" alt=\"Sentiment Ratio\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_LineGraph_Tweets_Per_Month_Mentions.png\" alt=\"Tweets Per Month\"></img>");
    	chartData.add("<img class=\"graphImage\" src=\"https://res.cloudinary.com/dfq1jscuf/image/upload/v"+ v +"/GuardianAngelUserData/UserXCharts/Admin_LineGraph_Sentiment_Per_Month_Mentions.png\" alt=\"Sentiment Per Month\"></img>");
    	
    	return chartData;
    	
    }

}
