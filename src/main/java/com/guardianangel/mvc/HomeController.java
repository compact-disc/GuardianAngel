package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/25/2019
 * 
 * Used to control the home or index page of the Guardian Angel Application.
 * It creates the initial connections to twitter and the SQL server.
 * It is also the default landing page after login, but if the user is not logged in they are redirected to /login.
 */

import com.guardianangel.security.TokenAuthentication;
import com.guardianangel.sql.SQLConnection;
import com.guardianangel.twitter.TwitterConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("unused")
@Controller
public class HomeController {

	/**
	 * Logger for the class
	 */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Mapping for /index or / for thymleaf template
     * 
     * @param authentication
     * @return template for the index or login if there is not a user logged in
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    protected String home(final Authentication authentication) {

        if (authentication instanceof TokenAuthentication) {
        	
            TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
            return "index";
            
        }
        
        return "login";
        
    }
    
}
