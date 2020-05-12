package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/30/2019
 * 
 * Used to pull the latest tweets of the currently logged in user.
 * This calls/creates the TweetDriver object and uses a lot of the twitter package functionality.
 */

import com.guardianangel.twitter.PullTwitterTimelineDriver;
import com.guardianangel.twitter.PullTwitterMentionsDriver;
import com.guardianangel.xchart.likes.*;
import com.guardianangel.xchart.likes.admin.*;
import com.guardianangel.xchart.timeline.*;
import com.guardianangel.xchart.timeline.admin.*;
import com.guardianangel.xchart.retweets.*;
import com.guardianangel.xchart.retweets.admin.*;
import com.guardianangel.xchart.mentions.*;
import com.guardianangel.xchart.mentions.admin.*;
import com.auth0.jwt.interfaces.Claim;
import com.guardianangel.security.TokenAuthentication;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.guardianangel.twitter.PullTwitterLikesDriver;

@SuppressWarnings("unused")
@Controller
public class PullDataController {
	
	/**
	 * Logger for the class
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Thymeleaf mapping for the /pullTweets functionality
	 * 
	 * @param authentication data for the currently logged in user
	 * @return a redirect to view tweets to show twitter data
	 */
	@RequestMapping(value = "/pullData", method = {RequestMethod.GET, RequestMethod.POST})
	private String pullData(final Authentication authentication) {
		
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
        
        //Call the tweet driver to pull the users timeline from twitter using their twitter id
        PullTwitterTimelineDriver timelineDriver = new PullTwitterTimelineDriver(twitterId , "id"); //retweets is included in this call
        PullTwitterLikesDriver likesDriver = new PullTwitterLikesDriver(twitterId, "id");
        PullTwitterMentionsDriver mentionsDriver = new PullTwitterMentionsDriver(twitterId, "id");
        
        //Create the pie chart of sentiment ratio for timeline data
        TimelineXChartPieChartBuilderSentimentRatio timelineSentimentRatio = new TimelineXChartPieChartBuilderSentimentRatio(twitterId);
        //Create the line graph of sentiment per month for timeline data
        TimelineXChartLineGraphBuilderTweetsPerMonth timelineTweetsPerMonth = new TimelineXChartLineGraphBuilderTweetsPerMonth(twitterId);
        //Create line graph of sentiment over time for timeline data
        TimelineXChartLineGraphBuilderSentimentPerMonth timelineSentimentOverTimeLineGraph = new TimelineXChartLineGraphBuilderSentimentPerMonth(twitterId);
        
        //Create the pie chart of sentiment ratio for likes data
        LikesXChartPieChartBuilderSentimentRatio likesSentimentRatio = new LikesXChartPieChartBuilderSentimentRatio(twitterId);
        //Create the line graph of sentiment per month for likes data
        LikesXChartLineGraphBuilderTweetsPerMonth likesTweetsPerMonth = new LikesXChartLineGraphBuilderTweetsPerMonth(twitterId);
        //Create line graph of sentiment over time for likes data
        LikesXChartLineGraphBuilderSentimentPerMonth likesSentimentOverTimeLineGraph = new LikesXChartLineGraphBuilderSentimentPerMonth(twitterId);
        
        //Create the pie chart of sentiment ratio for retweets data
        RetweetsXChartPieChartBuilderSentimentRatio retweetsSentimentRatio = new RetweetsXChartPieChartBuilderSentimentRatio(twitterId);
        //Create the line graph of sentiment per month for retweets data
        RetweetsXChartLineGraphBuilderTweetsPerMonth retweetsTweetsPerMonth = new RetweetsXChartLineGraphBuilderTweetsPerMonth(twitterId);
        //Create line graph of sentiment over time for retweets data
        RetweetsXChartLineGraphBuilderSentimentPerMonth retweetsSentimentOverTimeLineGraph = new RetweetsXChartLineGraphBuilderSentimentPerMonth(twitterId);
        
        //Create the pie chart of sentiment ratio for mentions data
        MentionsXChartPieChartBuilderSentimentRatio mentionsSentimentRatio = new MentionsXChartPieChartBuilderSentimentRatio(twitterId);
        //Create the line graph of sentiment per month for mentions data
        MentionsXChartLineGraphBuilderTweetsPerMonth mentionsTweetsPerMonth = new MentionsXChartLineGraphBuilderTweetsPerMonth(twitterId);
        //Create line graph of sentiment over time for mentions data
        MentionsXChartLineGraphBuilderSentimentPerMonth mentionsSentimentOverTimeLineGraph = new MentionsXChartLineGraphBuilderSentimentPerMonth(twitterId);
        
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
		
		return "redirect:viewtweets";
		
	}

}
