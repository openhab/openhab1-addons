/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.core.internal.folder;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.openhab.config.core.ConfigDispatcher;
import org.openhab.model.core.ModelCoreConstants;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.internal.util.MathUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is able to observe multiple folders for changes. It checks the
 * last modified date in a configurable frequency and notifies the model repository
 * about every change, so that it can update itself.
 * 
 * This logic is run as a separate thread, so that it can always detect changes.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class FolderObserver extends Thread implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(FolderObserver.class);

	/* map that lists all foldernames that should be observed and the frequency for checks in seconds */
	private final Map<String, Integer> folderRefreshMap = new ConcurrentHashMap<String, Integer>();

	/* map that stores a list of valid file extensions for each folder */
	private final Map<String, String[]> folderFileExtMap = new ConcurrentHashMap<String, String[]>();

	/* map that stores the time of the last check of a filename in milliseconds */
	private Map<String, Long> lastCheckedMap = new ConcurrentHashMap<String, Long>();
	
	/* map that remembers all filenames of the last check, so that it can detect file deletions */
	private Map<String, Set<String>> lastFileNames = new ConcurrentHashMap<String, Set<String>>();

	/* the greatest common divisor of all folder refresh rates */
	private int gcdRefresh = 1;
	
	/* the least common multiple of all folder refresh rates */
	private int lcmRefresh = 1;
	
	/* a counter to know which folders need to be refreshed when waking up */
	private int refreshCount = 0;
	
	/* the model repository is provided as a service */
	private ModelRepository modelRepo = null;
	
	
	public FolderObserver() {
		super("FolderObserver");
	}
	
	public void setModelRepository(ModelRepository modelRepo) {
		this.modelRepo = modelRepo;
	}
	
	public void unsetModelRepository(ModelRepository modelRepo) {
		this.modelRepo = null;
	}

	@Override
	public void run() {
		while(!folderRefreshMap.isEmpty()) { // keep the thread running as long as there are folders to observe
			try {
				for(String foldername : folderRefreshMap.keySet()) {
					// if folder has been checked at least once and it is not time yet to refresh, skip
					if( lastFileNames.get(foldername) != null  && 
							(refreshCount % folderRefreshMap.get(foldername) > 0)) {										
						logger.debug("skipping refresh of folder '{}' folderRefreshMap={}",
								foldername, folderRefreshMap.get(foldername));
						continue;
					} 
					
					logger.debug("Refreshing folder '{}'", foldername);
					checkFolder(foldername);
				}

				// increase the counter and set it to 0, if it reaches the max value
				refreshCount = (refreshCount + gcdRefresh) % lcmRefresh;
			} catch(Throwable e) {
				logger.error("An unexpected exception has occured", e);
			}			
			try {
				if(gcdRefresh <= 0) break;
				synchronized(FolderObserver.this) {
					wait(gcdRefresh * 1000L);
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	private void checkFolder(String foldername) {
		File folder = getFolder(foldername);
		if(!folder.exists()) {
			return;
		}
		String[] extensions = folderFileExtMap.get(foldername);
		
		// check current files and add or refresh them accordingly
		Set<String> currentFileNames = new HashSet<String>();
		for(File file : folder.listFiles()) {
			if(file.isDirectory()) continue;
			if(!file.getName().contains(".")) continue;
			if(file.getName().startsWith(".")) continue;
			
			// if there is an extension filter defined, continue if the file has a different extension
			String fileExt = getExtension(file.getName());
			if(extensions!=null && extensions.length>0 && !ArrayUtils.contains(extensions, fileExt)) continue;
			
			currentFileNames.add(file.getName());
			Long timeLastCheck = lastCheckedMap.get(file.getName());
			if(timeLastCheck==null) timeLastCheck = 0L;
			if(FileUtils.isFileNewer(file, timeLastCheck)) {
				if(modelRepo!=null) {
					try {
						if(modelRepo.addOrRefreshModel(file.getName(), FileUtils.openInputStream(file))) {
							lastCheckedMap.put(file.getName(), new Date().getTime());							
						}
					} catch (IOException e) {
						logger.warn("Cannot open file '"+ file.getAbsolutePath() + "' for reading.", e);
					}
				}
			}
		}
		
		// check for files that have been deleted meanwhile
		if(lastFileNames.get(foldername)!=null) {;
			for(String fileName : lastFileNames.get(foldername)) {
				if(!currentFileNames.contains(fileName)) {
					logger.info("File '{}' has been deleted", fileName);
					if(modelRepo!=null) {
						modelRepo.removeModel(fileName);
					}
				}
			}
		}
		lastFileNames.put(foldername, currentFileNames);
	}

	private String getExtension(String filename) {
		String fileExt = filename.substring(filename.lastIndexOf(".") + 1);
		return fileExt;
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			// make sure to clear the caches first
			lastFileNames.clear();
			lastCheckedMap.clear();
			folderFileExtMap.clear();
			folderRefreshMap.clear();
			
			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {
				String foldername = (String) keys.nextElement();
				if(foldername.equals("service.pid")) continue;
				String[] values = ((String) config.get(foldername)).split(",");
				try {
					Integer refreshValue = Integer.valueOf(values[0]);
					String[] fileExts = (String[]) ArrayUtils.remove(values, 0);
					File folder = getFolder(foldername);
					if (folder.exists() && folder.isDirectory()) {
						folderFileExtMap.put(foldername, fileExts);
						if (refreshValue > 0) {
							folderRefreshMap.put(foldername, refreshValue);
							if(!this.isAlive()) {
								// seems we have the first folder to observe, so let's start the thread
								this.start();
							} else {
								// make sure that we notify the sleeping thread and directly refresh the folders
								synchronized (FolderObserver.this) {
									notify();
									checkFolder(foldername);
								}
							}
						} else {
							// deactivate the refresh for this folder
							folderRefreshMap.remove(foldername);
							checkFolder(foldername);
						}
					} else {
						logger.warn(
								"Directory '{}' does not exist in '{}'. Please check your configuration settings!",
								foldername, ConfigDispatcher.getConfigFolder());
					}
					
					// now update the refresh information for the thread
					Integer[] refreshValues = folderRefreshMap.values().toArray(new Integer[0]);
					if(refreshValues.length>0) {
						gcdRefresh = MathUtils.gcd(refreshValues);
						lcmRefresh = MathUtils.lcm(refreshValues);
					}
					refreshCount = 0;
				} catch (NumberFormatException e) {
					logger.warn(
							"Invalid value '{}' for configuration '{}'. Integer value expected!",
							values[0], ModelCoreConstants.SERVICE_PID + ":"
									+ foldername);
				}

			}
		}
	}

	/**
	 * returns the {@link File} object for a given foldername
	 * @param foldername the foldername to get the {@link File} for
	 * @return the corresponding {@link File}
	 */
	private File getFolder(String foldername) {
		File folder = new File(ConfigDispatcher.getConfigFolder()
				+ File.separator + foldername);
		return folder;
	}

}
