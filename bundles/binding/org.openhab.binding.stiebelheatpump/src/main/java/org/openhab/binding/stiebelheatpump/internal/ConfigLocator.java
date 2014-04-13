/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.internal;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.stiebelheatpump.protocol.Request;
import org.openhab.binding.stiebelheatpump.protocol.Requests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLocator {

	private static final Logger logger = LoggerFactory
			.getLogger(ConfigLocator.class);
	
	private String file;
	private ConfigParser configParser = new ConfigParser();
	
	public ConfigLocator() {
	}

	public ConfigLocator(String file) {
        this.file = file;
        
		String[] searchFiles = null;

		try {
			searchFiles = getResourceListing(Requests.class, "");
	
			String[] match = getMatchingStrings(searchFiles, file);
			if (match.length == 0){
				logger.warn("Could not find heat pump configuration file for {}!", file);
				this.file = "";
			}
		} catch (URISyntaxException e) {
			logger.warn("URISyntaxException " +  e.toString());
		} catch (IOException e) {
			logger.warn("IOException " +  e.toString());
		}
    }
	
    /**
     * Searches for the given files in the
     * class path.
     * 
     * @return All found Configurations
     */
    public List<Request> getConfig() {
    	List<Request> config = configParser.parseConfig(file);
        return config;
    }
    
    /**
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     * 
     * @param clazz Any java class that lives in the same place as the resources you want.
     * @param path Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException 
     * @throws IOException 
     */
    public String[] getResourceListing(Class<?> clazz, String path) throws URISyntaxException, IOException {

    	String[] resources = null ;
    	URL dirURL = clazz.getClassLoader().getResource(path);
        
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
          /* A file path: easy enough */
        	resources =  new File(dirURL.toURI()).list();
        } 

        if (dirURL == null) {
          /* 
           * In case of a jar file, we can't actually find a directory.
           * Have to assume the same jar as clazz.
           */
          String me = clazz.getName().replace(".", "/")+".class";
          dirURL = clazz.getClassLoader().getResource(me);
        }
        
        if (dirURL.getProtocol().equals("jar")) {
          /* A JAR path */
          String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
          JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
          Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
          Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a sub directory
          while(entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.startsWith(path)) { //filter according to the path
              String entry = name.substring(path.length());
              int checkSubdir = entry.indexOf("/");
              if (checkSubdir >= 0) {
                // if it is a sub directory, we just return the directory name
                entry = entry.substring(0, checkSubdir);
              }
              result.add(entry);
            }
          }
          jar.close();
          resources = result.toArray(new String[result.size()]);
        } 
        resources = getMatchingStrings(resources, "xml$");
        return resources;
    }
    
    /**
     * Finds the index of all entries in the list that matches the regex
     * @param list The list of strings to check
     * @param regex The regular expression to use
     * @return list containing the indexes of all matching entries
     */
    private String[] getMatchingStrings(String[] list, String regex) {

      ArrayList<String> matches = new ArrayList<String>();

      Pattern p = Pattern.compile(regex);

      for (String s:list) {
    	Matcher m = p.matcher(s); 
    	boolean b = m.find();
        if (b) {
          matches.add(s);
        }
      }
      String[] result = new String[matches.size()];
      result = matches.toArray(result);

      return result;
    }

}
