package com.guardianangel.cloudinary;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/27/2020
 * 
 * Cloudinary CDN Configuaration for connection
 * 
 */

import com.cloudinary.Cloudinary;

public class CloudinaryConfiguration {
	
	/**
	 * Cloudinary Object for configuration setup
	 */
	private static Cloudinary configuration;
	
	/**
	 * Returns the configuration of the cloudinary connection
	 * 
	 * @return
	 */
	public static Cloudinary getCloudinaryConfiguration() {
		
		configuration = new Cloudinary("API KEY GOES HERE");
		
		return configuration;
		
	}

}
