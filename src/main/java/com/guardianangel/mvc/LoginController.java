package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/25/2019
 * 
 * Used to start the login process with Auth0. After login is complete it calls /callback for redirect.
 */

import com.auth0.AuthenticationController;
import com.guardianangel.security.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Controller
public class LoginController {

	/**
	 * The authentication controller
	 */
    @Autowired
    private AuthenticationController controller;

    /**
     * Configuration options for Spring on launch
     */
    @Autowired
    private AppConfig appConfig;

    /**
     * Logger for the class
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Runs the /login mapping for thymeleaf to start the login process with auth0
     * 
     * @param req httpserverlet request
     * @return redirect:authorizationUrl for when login is complete 
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest req) {
        logger.debug("Performing login");
        String redirectUri = req.getScheme() + "://" + req.getServerName();
        if ((req.getScheme().equals("http") && req.getServerPort() != 80) || (req.getScheme().equals("https") && req.getServerPort() != 443)) {
            redirectUri += ":" + req.getServerPort();
        }
        redirectUri += "/callback";
        String authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri)
                .withAudience(String.format("https://%s/userinfo", appConfig.getDomain()))
                .withScope("openid profile email")
                .build();
        
        return "redirect:" + authorizeUrl;
    }

}
