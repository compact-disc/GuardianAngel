package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/25/2019
 * 
 * Used for authentication from Auth0. On callback from the authentication it
 * redirects to (the host).com/callback and information is sent to this object.
 */

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.guardianangel.security.TokenAuthentication;
import com.guardianangel.sql.SQLConnection;
import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.guardianangel.twitter.TwitterConnection;

@SuppressWarnings("unused")
@Controller
public class CallbackController {

	/**
	 * Controller for the authentication process
	 */
    @Autowired
    private AuthenticationController controller;
    
    /**
     * Where to redirect on login failure
     */
    private final String redirectOnFail;
    
    /**
     * Where to redirect on the login success
     */
    private final String redirectOnSuccess;

    /**
     * Set the variables redirectOnFail and redirectOnSuccess to their URLs.
     */
    public CallbackController() {
        this.redirectOnFail = "/login";
        this.redirectOnSuccess = "/";
    }

    /**
     * The mapping for the /callback url
     * 
     * @param req the http request for /callback
     * @param res the http response for /callback
     */
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    /**
     * The mapping for the /callback url
     * 
     * @param req the http request for /callback
     * @param res the http response for /callback
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected void postCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }
    
    /**
     * The handler method that consumes the response and request from the external server
     * 
     * 
     * @param req the http request for /callback
     * @param res the htpp response for /callback
     */
    private void handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Tokens tokens = controller.handle(req);
            TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            res.sendRedirect(redirectOnSuccess);
        } catch (AuthenticationException | IdentityVerificationException e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            res.sendRedirect(redirectOnFail);
        }
    }

}
