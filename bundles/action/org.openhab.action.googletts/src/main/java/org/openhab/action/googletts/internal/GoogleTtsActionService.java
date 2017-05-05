/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.googletts.internal;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Dictionary;
import java.util.HashSet;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This class registers an OSGi service for the GoogleTts action.
 * 
 * @author goldman.vlad
 * @since 1.4.0
 */
public class GoogleTtsActionService implements ActionService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(GoogleTtsActionService.class);

	public static final String TOKEN_PATH = "etc/googletts";
	
	public static HashSet<String> textAudioFiles;
	
	public static MessageDigest md;
	/**
	 * Indicates whether this action is properly configured which means all
	 * necessary configurations are set. This flag can be checked by the
	 * action methods before executing code.
	 */
	/* default */ static boolean isProperlyConfigured = true;
	
	static {
		try {
			md=MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Failed to initialize MD5 hashing", e);
		}
	}
	
	public GoogleTtsActionService() {
		
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return GoogleTts.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return GoogleTts.class;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("Google TTS config updated");
		
		if (config != null) {
			GoogleTts.defaultLang = (String) config.get("defaultLang");
			GoogleTts.isCacheEnabled=Boolean.valueOf((String) config.get("cacheEnabled"));
		}
		if (GoogleTts.defaultLang == null || GoogleTts.defaultLang == "") 
			GoogleTts.defaultLang="en";
		
		textAudioFiles=new HashSet<String>(10);
		File dir = new File(GoogleTts.GOOGLETTS_CACHE_PATH);
		if (!dir.exists()) {
			logger.debug("GoogleTts creating directory: " + GoogleTts.GOOGLETTS_CACHE_PATH);
		    boolean result = dir.mkdir();  

		     if(result) {    
		    	 logger.debug(GoogleTts.GOOGLETTS_CACHE_PATH+" dir created");  
		     }
		  }
		if(dir.isDirectory())
	      {
	        File afile[] = dir.listFiles();
	         if(afile.length > 0)
	         {
	            for(int i = 0; i < afile.length; i++)
	            {
	               if(afile[i].isFile())
	                  logger.debug("Adding text audio: " + afile[i].getName());
	                  textAudioFiles.add(afile[i].getName());
	            }
	         }
	      } else {
	    	  logger.debug("Text audio directory not found: " + dir.getAbsolutePath());
	      }
		logger.debug("GoogleTtsActionService completed loading cached files hashes");
		
		isProperlyConfigured = true;
		logger.debug("Google TTS is properly configured now with defaultLang "+GoogleTts.defaultLang+" and cache enabled:"+GoogleTts.isCacheEnabled);

	}
	
	public static String MD5(String md5) {

		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		}
	
}
