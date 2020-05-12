package com.guardianangel.twitter;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 3/12/2020
 * 
 * Driver to pull likes from the specified user.
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.guardianangel.sentiment.SentimentAnalyzerInstance;
import com.guardianangel.sentiment.SentimentDriver;
import com.guardianangel.sql.SQLConnection;
import twitter4j.GeoLocation;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class PullTwitterLikesDriver {
	
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
     * Type of twitter id, either username or id
     */
    private String type;
    
    /**
     * Instance of the sentiment analyzer to make less calls to create objects
     */
    private SentimentAnalyzerInstance sentimentAnalyzerInstance;
	
    /**
     * Constructor for the PullTwitterLikesDriver to pull likes of a user
     * 
     * @param twitterId Either the twitter id or username
     * @param type Either twitter id or username
     */
	public PullTwitterLikesDriver(String twitterId, String type) {
		
		tweetLogger = new TwitterLogger("likes_logger");
		
		//Set the class variable of the twitter id to the passed id
		this.twitterId = twitterId;
		this.type = type;
		
		this.sentimentAnalyzerInstance = new SentimentAnalyzerInstance();
		
		getLikes();
		
		tweetLogger.close();
		
	}
	
	/**
	 * Get the likes from Twitter and send them to the writeDatabase method
	 */
	private void getLikes() {
		
		ResponseList<Status> listOfLikes = null;
		
		try {
			
			switch(this.type) {
			
			case "id":
				listOfLikes = TwitterConnection.getTwitterInstance().getFavorites(Long.parseLong(twitterId));
				break;
			case "username":
				listOfLikes = TwitterConnection.getTwitterInstance().getFavorites("@" + this.twitterId);
				Twitter twitter = TwitterConnection.getTwitterInstance();
				User user = twitter.showUser(this.twitterId);
				this.twitterId = Long.toString(user.getId());
				break;
			}
			
		} catch (TwitterException e) {
			
			e.printStackTrace();
			
		}
		
		for(Status like : listOfLikes) {
			
			//Assign all the values needed to Strings to be written to SQL database
			String id, name, date, content, fullSource, numberOfFavorites, 
			contentNoApostrophe, sourceEdited = "", geoLocationString, sentimentType, sentimentScore, sentimentVeryPositive, sentimentPositive,
			sentimentNeutral, sentimentNegative, sentimentVeryNegative;
			
			GeoLocation geoLocation;
			
			id = String.valueOf(like.getId());
			
			if(!isDuplicate(id)) {
				
				name = like.getUser().getScreenName();
				date = like.getCreatedAt().toString();
				
				//Check if the status gathered is a tweet or retweet
				//This is needed so it will pull the full text of both tweets and retweets per the twitter API
				content = like.getText();
				
				fullSource = like.getSource();
				if (fullSource.contains("Web App")) {
					
					sourceEdited = "Twitter Web App";
					
				}else if (fullSource.contains("iPhone")) {
					
					sourceEdited = "Twitter for iPhone";
					
				}else if (fullSource.contains("Android")) {
					
					sourceEdited = "Twitter for Android";
					
				}else {
					
					sourceEdited = "Unknown Source";
					
				}
				
				numberOfFavorites = Integer.toString(like.getFavoriteCount());
				
				//Having Apostrophes in the strings was giving an SQL error so this is to remove them from the content
				contentNoApostrophe = content.replace("'", "");
				
				geoLocation = like.getGeoLocation();
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
	
	/**
	 * 
	 * Method to write each liked tweet to the SQL database
	 * 
	 * @param id twitter id of the tweet itself
	 * @param userName the username of the twitter user
	 * @param datePosted date the tweet was posted
	 * @param content content of the tweet
	 * @param source device the tweet was tweeted from
	 * @param numberOfFavorites number of favorites to the tweet
	 * @param geoLocation location of the tweet
	 * @param sentimentType sentiment type
	 * @param sentimentScore sentiment score
	 * @param sentimentVeryPositive sentiment very positive percent
	 * @param sentimentPositive sentiment positive percent
	 * @param sentimentNeutral sentiment neutral percent
	 * @param sentimentNegative sentiment negative percent
	 * @param sentimentVeryNegative sentiment very negative percent
	 */
	private void writeToDatabase(String id, String userName, String datePosted, String content, String source, String numberOfFavorites, String geoLocation, 
			String sentimentType, String sentimentScore, String sentimentVeryPositive, String sentimentPositive, String sentimentNeutral, 
			String sentimentNegative, String sentimentVeryNegative) {
		
		//Get the username of the user that has interacted with the tweet rather than the name of the person who's tweet is being liked
		String interactingUser = GetUsernameFromTwitterID.getUsername(this.twitterId);
		
		//String of all the data that will be written to the log file
		String logData = "\nid: " + id + "\nusername: " + userName + "\ndate: " + datePosted + "\ncontent: \n---Start Content---\n" + content + "\n---End Content---" + "\nsource: " + source + "\nnumberOfFavorites: " + numberOfFavorites
		+ "\ntwitterid: " + this.twitterId +"\ngeolocation: " + geoLocation + "\nsentiment type: " + sentimentType + "\nsentiment score: " + sentimentScore + "\nsentiment very positive: " + sentimentVeryPositive
		+ "\nsentiment positive: " + sentimentPositive + "\nsentiment neutral: " + sentimentNeutral + "\nsentiment negaive: " + sentimentNeutral + "\nsentiment negative: " + sentimentNegative
		+ "\nsentiment very negative: " + sentimentVeryNegative + "\ninteractinguser: " + interactingUser;
		
		//String of all the data that will be written to the database
		//This include the SQL commands and database that it will write to
		String tweetSQLData = "INSERT INTO likes (id,name,date,content,source,numberoffavorites,twitterid,geolocation,sentimenttype,sentimentscore,sentimentverypositive,sentimentpositive,sentimentneutral,sentimentnegative,sentimentverynegative,interactinguser) "
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
	 * Method returns a string to check for duplicate data inside database so there is one of each tweet
	 * 
	 * @param tweetId the id of the tweet
	 * @return returns true or false depending if the tweet already exists in the database
	 */
	private Boolean isDuplicate(String tweetId) {
		
		String result = null;
		Connection sqlConnection = null;
		Statement sqlStatement = null;
		ResultSet data = null;
		
		try {
			
			sqlConnection = SQLConnection.getSQLConnection();
			sqlStatement = sqlConnection.createStatement();
			data = sqlStatement.executeQuery("SELECT id FROM likes");

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
