package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 2/26/2020
 * 
 * Used as a test controller for testing new features in Guardian Angel.
 */

import com.auth0.jwt.interfaces.Claim;
import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import com.guardianangel.cloudinary.CloudinaryConfiguration;
import com.guardianangel.cloudinary.CloudinaryUpload;
import com.guardianangel.security.TokenAuthentication;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.guardianangel.xchart.*;
import com.guardianangel.xchart.timeline.TimelineXChartLineGraphBuilderSentimentPerMonth;
import com.guardianangel.xchart.timeline.TimelineXChartPieChartBuilderSentimentRatio;
import com.guardianangel.twitter.GetUserTwitterIDByScreenName;
import com.guardianangel.twitter.GetUsernameFromTwitterID;
import com.guardianangel.twitter.PullTwitterLikesDriver;
import com.guardianangel.twitter.PullTwitterMentionsDriver;
import org.cloudinary.json.*;


@SuppressWarnings("unused")
@Controller
public class TestController {
	
	/**
	 * Logger for the class
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Thymeleaf mapping for the /test functionality
	 * 
	 * @param authentication data for the currently logged in user
	 * @return a redirect to the admin page
	 */
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	private String test(final Authentication authentication) {
		
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
		
        //redirect back to the admin page after running the test function
		return "redirect:admin";
		
	}

}
