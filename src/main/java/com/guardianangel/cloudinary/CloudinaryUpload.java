package com.guardianangel.cloudinary;

/**
 * @author 	Christopher DeRoche cderoche@iu.edu
 * @version	1.0.0
 * @since	1.0.0
 * 
 * Date Created: 4/27/2020
 * 
 * Cloudinary CDN upload files
 * 
 */

import java.io.IOException;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryUpload {
	
	public CloudinaryUpload(String filename) {
		
		try {
			
			CloudinaryConfiguration.getCloudinaryConfiguration().uploader().upload("./GuardianAngelUserData/UserXCharts/"+ filename +".png", ObjectUtils.asMap(
					"use_filename", "true",
					"invalidate", "true",
					"version", "1",
					"unique_filename", "false",
					"public_id", filename,
					"folder", "GuardianAngelUserData/UserXCharts/"));
			
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		
	}

}
