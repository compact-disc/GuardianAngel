package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/30/2019
 * 
 * Used to pull the remove current users data
 */

import com.auth0.jwt.interfaces.Claim;
import com.cloudinary.utils.ObjectUtils;
import com.guardianangel.cloudinary.CloudinaryConfiguration;
import com.guardianangel.security.TokenAuthentication;
import com.guardianangel.sql.SQLConnection;
import com.guardianangel.xchart.likes.admin.*;
import com.guardianangel.xchart.timeline.admin.*;
import com.guardianangel.xchart.retweets.admin.*;
import com.guardianangel.xchart.mentions.admin.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("unused")
@Controller
public class DeleteDataController {
	
	/**
	 * Logger for the class
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * The connection to the SQL server
	 */
	private Connection sqlConnection = null;
	
	/**
	 * The SQL statement that will be sent to query the SQL server
	 */
	private Statement statement = null;
	
	/**
	 * Thymeleaf mapping for the /deletedata functionality
	 * 
	 * @param authentication data for the currently logged in user
	 * @return a redirect back viewtweets where the user was to show no data
	 */
	@RequestMapping(value = "/deletedata", method = RequestMethod.POST)
	private String deletedata(final Authentication authentication) {
		
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
        
        //Code to delete the data from the current user
        String sqlDeleteTimeline = "DELETE FROM timeline WHERE twitterId='" + twitterId + "'";
        String sqlDeleteRetweets = "DELETE FROM retweets WHERE twitterId='" + twitterId + "'";
        String sqlDeleteMentions = "DELETE FROM mentions WHERE twitterId='" + twitterId + "'";
        String sqlDeleteLikes = "DELETE FROM likes WHERE twitterId='" + twitterId + "'";
        
        try {
        	
        	//Get the connection from the SQL Connection class
        	sqlConnection = SQLConnection.getSQLConnection();
        	
        	//Create an SQL statement object to send
			statement = sqlConnection.createStatement();
			
			//Send the statement created with the twitterId to the SQL server
			statement.executeQuery(sqlDeleteTimeline);
			statement.executeQuery(sqlDeleteRetweets);
			statement.executeQuery(sqlDeleteMentions);
			statement.executeQuery(sqlDeleteLikes);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
        
        File folder = new File("./GuardianAngelUserData/UserXCharts/");
        
        for (File file : folder.listFiles()) {
        	
        	if(file.getName().startsWith(twitterId)) {
        		
        		file.delete();
        		
        	}
        	
        }
        
        
        //Destroy all the images for the user in the Cloudinary CDN database
        try {
        	
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_LineGraph_Sentiment_Per_Month_Likes", ObjectUtils.emptyMap());
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_LineGraph_Tweets_Per_Month_Likes", ObjectUtils.emptyMap());
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_PieChart_Sentiment_Ratio_Likes", ObjectUtils.emptyMap());
			
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_LineGraph_Sentiment_Per_Month_Mentions", ObjectUtils.emptyMap());
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_LineGraph_Tweets_Per_Month_Mentions", ObjectUtils.emptyMap());
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_PieChart_Sentiment_Ratio_Mentions", ObjectUtils.emptyMap());
			
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_LineGraph_Sentiment_Per_Month_Retweets", ObjectUtils.emptyMap());
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_LineGraph_Tweets_Per_Month_Retweets", ObjectUtils.emptyMap());
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_PieChart_Sentiment_Ratio_Retweets", ObjectUtils.emptyMap());
			
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_LineGraph_Sentiment_Per_Month_Timeline", ObjectUtils.emptyMap());
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_LineGraph_Tweets_Per_Month_Timeline", ObjectUtils.emptyMap());
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().destroy("GuardianAngelUserData/UserXCharts/" + twitterId + "_PieChart_Sentiment_Ratio_Timeline", ObjectUtils.emptyMap());

			
		} catch (IOException e) {

			e.printStackTrace();
			
		}
        
        /*
         * Below is code to generate new Administration graphs without the current users data included
         */
        
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
		
        //put user back to the viewtweets to show blank database
		return "redirect:viewtweets";
		
	}

}
