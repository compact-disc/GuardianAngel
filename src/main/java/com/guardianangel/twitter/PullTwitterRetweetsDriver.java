package com.guardianangel.twitter;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 3/12/2020
 * 
 * Driver to pull retweets of a user.
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.guardianangel.sentiment.SentimentAnalyzerInstance;
import com.guardianangel.sentiment.SentimentDriver;
import com.guardianangel.sql.SQLConnection;
import twitter4j.GeoLocation;
import twitter4j.Status;

public class PullTwitterRetweetsDriver {
	
	/**
	 * The tweet logger
	 */
	private TwitterLogger tweetLogger;
	
	/**
	 * Statement to store SQL queries
	 */
	private Statement sqlStatement = null;
    
	/**
	 * Twitter ID or username to gather tweets from
	 */
    private String twitterId;
    
    /**
     * Sentiment analyzer instance so less objects are having to be created
     */
    private SentimentAnalyzerInstance sentimentAnalyzerInstance;
	
    /**
     * 
     * Constructor for the pull retweets driver to start the pulling process. The timelineList is passed because they are pull from the timeline. This creates less calls.
     * Each tweet is checked and passed to the writeToDatabase method.
     * 
     * @param timelineList The entire timelineList data pull to check for the retweets are write them to the database.
     * @param twitterId the twitter id of the user 
     */
	public PullTwitterRetweetsDriver(List<Status> timelineList, String twitterId) {
		
		this.twitterId = twitterId;
		
		tweetLogger = new TwitterLogger("retweets_logger");
		
		this.sentimentAnalyzerInstance = new SentimentAnalyzerInstance();
		
		for (Status status : timelineList) {
			
			//Assign all the values needed to Strings to be written to SQL database
			String id, name, date, content, fullSource, numberOfFavorites, 
			contentNoApostrophe, sourceEdited = "", geoLocationString, sentimentType, sentimentScore, sentimentVeryPositive, sentimentPositive,
			sentimentNeutral, sentimentNegative, sentimentVeryNegative;
			
			GeoLocation geoLocation;
			
			id = String.valueOf(status.getId());
			
			if(!isDuplicate(id)) {
				
				if(status.getText().contains("RT @") || status.isRetweeted()) {
				
					name = status.getUser().getScreenName();
					date = status.getCreatedAt().toString();
				
					//Check if the status gathered is a tweet or retweet
					//This is needed so it will pull the full text of both tweets and retweets per the twitter API
					if(status.isRetweeted()) {
						content = status.getRetweetedStatus().getText();
					}else {
						content = status.getText();
					}
				
					fullSource = status.getSource();
					if (fullSource.contains("Web App")) {
					
						sourceEdited = "Twitter Web App";
					
					}else if (fullSource.contains("iPhone")) {
					
						sourceEdited = "Twitter for iPhone";
					
					}else if (fullSource.contains("Android")) {
					
						sourceEdited = "Twitter for Android";
					
					}else {
					
						sourceEdited = "Unknown Source";
					
					}
				
					numberOfFavorites = Integer.toString(status.getFavoriteCount());
				
					//Having Apostrophes in the strings was giving an SQL error so this is to remove them from the content
					contentNoApostrophe = content.replace("'", "");
				
					geoLocation = status.getGeoLocation();
					geoLocationString = "null"; //TEST VALUE OF GEOLOCATION .toString does not work...find another solution to add
				
					//Create the sentiment object for the Sentiment analysis, passing the content of the status to the analyzer
					SentimentDriver sentiment = new SentimentDriver(contentNoApostrophe, sentimentAnalyzerInstance.getSentimentAnalyzer());
					sentimentType = sentiment.getSentimentType();
					sentimentScore = sentiment.getSentimentScore();
				
					sentimentVeryPositive = sentiment.getVeryPositive();
					sentimentPositive = sentiment.getPositive();
					sentimentNeutral = sentiment.getNeutral();
					sentimentNegative = sentiment.getNegative();
					sentimentVeryNegative = sentiment.getVeryNegative();
			
					writeToDatabase(id, name, date, contentNoApostrophe, sourceEdited, numberOfFavorites, geoLocationString, 
						sentimentType, sentimentScore, sentimentVeryPositive, sentimentPositive, sentimentNeutral, sentimentNegative, sentimentVeryNegative);
				
				}
				
			}
			
		}
		
		tweetLogger.close();
		
	}
	
	/**
	 * 
	 * Write each tweet to the database.
	 * 
	 * @param id twitter id of the tweet itself
	 * @param userName name of the user who created the tweet
	 * @param datePosted original date the tweet was posted
	 * @param content content of the tweet
	 * @param source device the tweet was posted from 
	 * @param numberOfFavorites number of favorites on the tweet
	 * @param geoLocation location where the tweet was posted from
	 * @param sentimentType sentiment type of the tweet
	 * @param sentimentScore sentiment score
	 * @param sentimentVeryPositive sentiment very positive percent
	 * @param sentimentPositive sentiment positive percent
	 * @param sentimentNeutral sentiment neutral percent
	 * @param sentimentNegative sentiment negative percent
	 * @param sentimentVeryNegative sentiment very negative
	 */
	private void writeToDatabase(String id, String userName, String datePosted, String content, String source, String numberOfFavorites, String geoLocation, 
			String sentimentType, String sentimentScore, String sentimentVeryPositive, String sentimentPositive, String sentimentNeutral, String sentimentNegative, 
			String sentimentVeryNegative) {
		
		//Get the username of the user that has interacted with the tweet rather than the name of the person who's tweet is being retweeted
		String interactingUser = GetUsernameFromTwitterID.getUsername(this.twitterId);
		
		//String of all the data that will be written to the log file
		String logData = "\nid: " + id + "\nusername: " + userName + "\ndate: " + datePosted + "\ncontent: \n---Start Content---\n" + content + "\n---End Content---\n" + "\nsource: " + source + "\nnumberOfFavorites: " + numberOfFavorites
		+ "\ntwitterid: " + this.twitterId +"\ngeolocation: " + geoLocation + "\nsentiment type: " + sentimentType + "\nsentiment score: " + sentimentScore + "\nsentiment very positive: " + sentimentVeryPositive
		+ "\nsentiment positive: " + sentimentPositive + "\nsentiment neutral: " + sentimentNeutral + "\nsentiment negaive: " + sentimentNeutral + "\nsentiment negative: " + sentimentNegative
		+ "\nsentiment very negative: " + sentimentVeryNegative + "\ninteractinguser: " + interactingUser;
		
		//String of all the data that will be written to the database
		//This include the SQL commands and database that it will write to
		String tweetSQLData = "INSERT INTO retweets (id,name,date,content,source,numberoffavorites,twitterid,geolocation,sentimenttype,sentimentscore,sentimentverypositive,sentimentpositive,sentimentneutral,sentimentnegative,sentimentverynegative, interactinguser) "
				+ "VALUES ('"+ id +"','"+ userName +"','"+ datePosted +"','"+ content +"','"+ source +"','"+ numberOfFavorites +"','"+ this.twitterId +"','"+ geoLocation +"','"+ sentimentType +"','"+ sentimentScore +"','"+ sentimentVeryPositive +"','"+ sentimentPositive +"','"+ sentimentNeutral +"','"+ sentimentNegative +"','"+ sentimentVeryNegative +"','"+ interactingUser +"')";
		
		//Try/catch statement to try and write the tweet data to the SQL database
		try {
			
			//Within the SQL connect create a new statement to be written
			sqlStatement = SQLConnection.getSQLConnection().createStatement();
			
			//Execute the created statement into the SQL database
			sqlStatement.execute(tweetSQLData);
			
		
		} catch (SQLException se) {
			
			se.printStackTrace();
			
		}
		
		tweetLogger.info(logData);
		
	}
	
	/**
	 * 
	 * Check for duplicates in the retweets database
	 * 
	 * @param tweetId the id of the tweet itself to be checked against the already written tweets in the database
	 * @return
	 */
	private Boolean isDuplicate(String tweetId) {
		
		String result = null;
		Connection sqlConnection = null;
		Statement sqlStatement = null;
		ResultSet data = null;
		
		try {
			
			sqlConnection = SQLConnection.getSQLConnection();
			sqlStatement = sqlConnection.createStatement();
			data = sqlStatement.executeQuery("SELECT id FROM retweets");

			while(data.next()) {
				
				result = data.getString("id");
				
				if(result.equals(tweetId)) {
					
					return true;
					
				}
				
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
			
		}
		
		return false;
		
	}

}
