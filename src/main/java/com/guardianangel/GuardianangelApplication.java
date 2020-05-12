package com.guardianangel;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 10/25/2019
 */

import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource("classpath:auth0.properties")
})

public class GuardianangelApplication {

	/**
	 * Main class for Guardian Angel
	 * 
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {
		
		//Check for User Data and Logging Directories before starting the program
		//Create them if they are not there
		File userDataDirectory = new File("./GuardianAngelUserData");
		File userXChartsDirectory = new File("./GuardianAngelUserData/UserXCharts");
		File logsDirectory = new File("./GuardianAngelLogs");
		
		//If the directory is not there, create it
		if(!userDataDirectory.exists()) {
			
			userDataDirectory.mkdir();
			
		}
		
		//If the directory is not there, create it
		if(!logsDirectory.exists()) {
			
			logsDirectory.mkdir();
			
		}
		
		//If the directory is not there, create it
		if(!userXChartsDirectory.exists()) {
			
			userXChartsDirectory.mkdir();
			
		}
		
		//Start the Spring Application
		SpringApplication.run(GuardianangelApplication.class, args);
	
	}

}

