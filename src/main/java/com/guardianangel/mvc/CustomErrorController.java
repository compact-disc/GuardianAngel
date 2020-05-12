package com.guardianangel.mvc;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/25/2019
 * 
 * Used for error handling. If there is an error encountered it will redirect to example.com/error.
 * 
 * This currently just redirects the user back to the login page and then the user should be able to proceed from there.
 * However if the user is logged in they should be redirected back to the index page
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@SuppressWarnings("unused")
@Controller
public class CustomErrorController implements ErrorController {

	/**
	 * Logger for this class
	 */
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Redirect path if there is some kind of error while running the program.
     */
    public static final String PATH = "/error";

    /**
     * The /error mapping method in thymeleaf for errors when running
     * 
     * @param redirectAttributes for any information on the error
     * @return "redirect:/login" sends the user back to the login screen on error for now
     */
    @RequestMapping("/error")
    public String error(final RedirectAttributes redirectAttributes) throws IOException {
        
        return "redirect:/login";
        
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}