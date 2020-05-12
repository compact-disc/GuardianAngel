package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 2/19/2020
 * 
 * Used to show the FAQ page.
 */

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.auth0.jwt.interfaces.Claim;
import com.guardianangel.security.TokenAuthentication;

@Controller
@SuppressWarnings("unused")
public class FAQController {
	
	/**
	 * Logger for the class
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Thymeleaf mapping for /faq to run the privacy checkup
	 * 
	 * @param model thymeleaf html model to be served content
	 * @param authentication data for the currently logged in user
	 * @return "redirect:faq" will redirect to the faq model
	 */
    @RequestMapping(value = "/faq", method = RequestMethod.GET)
    private String faq(final Model model, final Authentication authentication) {
    	
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
        
        //set to redirect to the faq page
        return "faq";
    	
    }

}