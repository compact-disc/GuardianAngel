package com.guardianangel.twitter;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 9/11/2019
 * 
 * Driver to pull the timeline of a user.
 */

import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.GeoLocation;
import com.guardianangel.sentiment.SentimentDriver;
import com.guardianangel.sql.SQLConnection;
import com.guardianangel.sentiment.SentimentAnalyzerInstance;

public class PullTwitterTimelineDriver {
	
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
     * Sentiment analyzer instance to create less objects
     */
    private SentimentAnalyzerInstance sentimentAnalyzerInstance;
	
    /**
     * Constructor for the TweetDriver that collects tweet timeline.
     * 
     * @param twitterId Either the twitter id or username
     * @param type Either twitter id or username
     */
	public PullTwitterTimelineDriver(String twitterId, String type) {
		
		tweetLogger = new TwitterLogger("timeline_logger");
		
		//Set the class variable of the twitter id to the passed id
		this.twitterId = twitterId;
		this.type = type;
		
		this.sentimentAnalyzerInstance = new SentimentAnalyzerInstance();
		
		//Get the home time line of the user that is logged in
		getHomeTimeline();
		
		tweetLogger.close();
		
	}

	
	/**
	 * Gets the home time line of the specified user and write each of the tweets to the database
	 */
	private void getHomeTimeline() {
		
		List<Status> listOfTwitterStatuses = null;
		
		try {
			
			switch(this.type) {
			
			case "id":
				listOfTwitterStatuses = TwitterConnection.getTwitterInstance().getUserTimeline(Long.parseLong(twitterId));
				break;
			case "username":
				listOfTwitterStatuses = TwitterConnection.getTwitterInstance().getUserTimeline("@" + this.twitterId);
				Twitter twitter = TwitterConnection.getTwitterInstance();
				User user = twitter.showUser(this.twitterId);
				this.twitterId = Long.toString(user.getId());
				break;
			}
			
			//Send all of the timeline data pulled over to the retweets data to request less from twitter and separate data for retweets
			PullTwitterRetweetsDriver retweets = new PullTwitterRetweetsDriver(listOfTwitterStatuses, this.twitterId);
			
		} catch (TwitterException e) {
			
			e.printStackTrace();
			
		}
		
		//Enhanced for loop to go through the list of all the tweets from the home timeline
		for (Status status : listOfTwitterStatuses) {
			
			//Assign all the values needed to Strings to be written to SQL database
			String id, name, date, content, isFavorited, isRetweeted, isRetweetedByUser, fullSource, numberOfFavorites, 
			contentNoApostrophe, sourceEdited = "", geoLocationString, sentimentType, sentimentScore, sentimentVeryPositive, sentimentPositive,
			sentimentNeutral, sentimentNegative, sentimentVeryNegative;
			
			GeoLocation geoLocation;
			
			id = String.valueOf(status.getId());
			
			if(!isDuplicate(id)) {
				
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
				
				isFavorited = Boolean.toString(status.isFavorited());
				isRetweeted = Boolean.toString(status.isRetweeted());
				isRetweetedByUser = Boolean.toString(status.isRetweetedByMe());
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
			
				writeToDatabase(id, name, date, contentNoApostrophe, isFavorited, isRetweeted, isRetweetedByUser, sourceEdited, numberOfFavorites, geoLocationString, 
						sentimentType, sentimentScore, sentimentVeryPositive, sentimentPositive, sentimentNeutral, sentimentNegative, sentimentVeryNegative);
				
			}
			
		}
		
	}
	
	/**
	 * 
	 * Method to write each tweet to the database given the parameters.
	 * 
	 * @param id twitter id of the tweet itself
	 * @param userName username of the user who created the tweet
	 * @param datePosted original creation date of the tweet
	 * @param content content of the tweet
	 * @param isFavorited check if the tweet is favorited
	 * @param isRetweeted check if the tweet is retweeted
	 * @param isRetweetedByUser check if the tweet is retweeted by the user
	 * @param source device the tweet was tweeted from 
	 * @param numberOfFavorites number of favorites on the tweet
	 * @param geoLocation location where the tweet was tweeted from 
	 * @param sentimentType sentiment type
	 * @param sentimentScore sentiment score
	 * @param sentimentVeryPositive sentiment very positive percent
	 * @param sentimentPositive sentiment positive percent
	 * @param sentimentNeutral sentiment neutral percent
	 * @param sentimentNegative sentiment negative percent
	 * @param sentimentVeryNegative sentiment very negative percent
	 */
	private void writeToDatabase(String id, String userName, String datePosted, String content, String isFavorited, 
			String isRetweeted, String isRetweetedByUser, String source, String numberOfFavorites, String geoLocation, String sentimentType, String sentimentScore,
			String sentimentVeryPositive, String sentimentPositive, String sentimentNeutral, String sentimentNegative, String sentimentVeryNegative) {
		
		//Get the username of the user that has interacted with the tweet
		String interactingUser = GetUsernameFromTwitterID.getUsername(this.twitterId);
		
		//String of all the data that will be written to the log file
		String logData = "\nid: " + id + "\nusername: " + userName + "\ndate: " + datePosted + "\ncontent: \n---Start Content---\n" + content + "\n---End Content---\nisFavorited: " 
		+ isFavorited + "\nisRetweeted: " + isRetweeted + "\nisRetweetedByUser: " + isRetweetedByUser + "\nsource: " + source + "\nnumberOfFavorites: " + numberOfFavorites
		+ "\ntwitterid: " + this.twitterId +"\ngeolocation: " + geoLocation + "\nsentiment type: " + sentimentType + "\nsentiment score: " + sentimentScore + "\nsentiment very positive: " + sentimentVeryPositive
		+ "\nsentiment positive: " + sentimentPositive + "\nsentiment neutral: " + sentimentNeutral + "\nsentiment negaive: " + sentimentNeutral + "\nsentiment negative: " + sentimentNegative
		+ "\nsentiment very negative: " + sentimentVeryNegative + "\ninteractinguser: " + interactingUser;
		
		//String of all the data that will be written to the database
		//This include the SQL commands and database that it will write to
		String tweetSQLData = "INSERT INTO timeline (id,name,date,content,isfavorited,isretweeted,isretweetedbyuser,source,numberoffavorites,twitterid,geolocation,sentimenttype,sentimentscore,sentimentverypositive,sentimentpositive,sentimentneutral,sentimentnegative,sentimentverynegative,interactinguser) "
				+ "VALUES ('"+ id +"','"+ userName +"','"+ datePosted +"','"+ content +"','"+ isFavorited +"','"+ isRetweeted +"','"+ isRetweetedByUser +"','"+ source +"','"+ numberOfFavorites +"','"+ this.twitterId +"','"+ geoLocation +"','"+ sentimentType +"','"+ sentimentScore +"','"+ sentimentVeryPositive +"','"+ sentimentPositive +"','"+ sentimentNeutral +"','"+ sentimentNegative +"','"+ sentimentVeryNegative +"','"+ interactingUser +"')";
		
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
			data = sqlStatement.executeQuery("SELECT id FROM timeline");

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
