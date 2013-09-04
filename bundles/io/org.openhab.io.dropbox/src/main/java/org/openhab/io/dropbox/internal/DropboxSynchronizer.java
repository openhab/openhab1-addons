/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.io.dropbox.internal;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.BooleanUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxDelta;
import com.dropbox.core.DbxDelta.Entry;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;


/**
 * The {@link DropboxSynchronizer} is able to synchronize contents of your Dropbox
 * to the local file system and vice versa. There three synchronization modes
 * available: local to dropbox (which is the default mode), dropbox to local and
 * bidirectional.
 * 
 * Note: The {@link DropboxSynchronizer} must be authorized against Dropbox one
 * time. Watch the logfile for the URL to open in your Browser and allow openHAB
 * to connect to a predefined App-Folder (see <a 
 * href="https://www.dropbox.com/developers/apps">Dropbox Documentation</a> for more information).
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class DropboxSynchronizer implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(DropboxSynchronizer.class);
	
	
	private static final String DROPBOX_SCHEDULER_GROUP = "Dropbox";

	private static final String FIELD_DELIMITER = "@@";

	private static final String LINE_DELIMITER = System.getProperty("line.separator");

	private static final String DELTA_CURSOR_FILE_NAME = File.separator + "deltacursor.dbx";

	private static final String DROPBOX_ENTRIES_FILE_NAME = File.separator + "dropbox-entries.dbx";

	private static final String AUTH_FILE_NAME = File.separator + "authfile.dbx";

	
	private static String lastCursor = null;
	
	private static String lastHash = null;
	

	/** the configured AppKey (optional, defaults to the official Dropbox-App key 'gbrwwfzvrw6a9uv') */
	private static String appKey = "gbrwwfzvrw6a9uv";

	/** the configured AppSecret (optional, defaults to official Dropbox-App secret 'gu5v7lp1f5bbs07') */
	private static String appSecret = "gu5v7lp1f5bbs07";

	/** The default directory to download files from Dropbox to (currently '.') */
	private static final String DEFAULT_CONTENT_DIR = ".";
	
	/** the base directory to synchronize with openHAB, configure 'filter' to select files (defaults to DEFAULT_CONTENT_DIR) */
	private static String contentDir = DEFAULT_CONTENT_DIR;

	/** the configured synchronization mode (defaults to LOCAL_TO_DROPBOX) */
	private static DropboxSyncMode syncMode = DropboxSyncMode.LOCAL_TO_DROPBOX;

	/** the upload interval as Cron-Expression (optional, defaults to '0 0 2 * * ?' which means once a day at 2am) */
	private static String uploadInterval = "0 0 2 * * ?";

	/** the download interval as Cron-Expression (optional, defaults to '0 0/5 * * * ?' which means every 5 minutes) */
	private static String downloadInterval = "0 0/5 * * * ?";
	
	private static final List<String> DEFAULT_UPLOAD_FILE_FILTER = Arrays.asList("^([^/]*/){1}[^/]*$", "/configurations.*", "/logs/.*", "/etc/.*");
	private static final List<String> DEFAULT_DOWNLOAD_FILE_FILTER = Arrays.asList("^([^/]*/){1}[^/]*$", "/configurations.*");
	
	/** defines a comma separated list of regular expressions which matches the filenames to upload to Dropbox (optional, defaults to '/configurations.*, /logs/.*, /etc/.*') */
	private static List<String> uploadFilterElements = DEFAULT_UPLOAD_FILE_FILTER;
	
	/** defines a comma separated list of regular expressions which matches the filenames to download from Dropbox (optional, defaults to '/configurations.*') */
	private static List<String> downloadFilterElements = DEFAULT_DOWNLOAD_FILE_FILTER;
	
	/** operates the Synchronizer in fake mode which avoids up- or downloading files to and from Dropbox. This is meant as testMode for the filter settings (optional, defaults to false) */
	private static boolean fakeMode = false;
	
	/** indicates whether the Dropbox authorization Thread should be interrupted or not (defaults to <code>false</code>)*/
	private static boolean authorizationThreadInterrupted = false;
	
	private DbxAuthFinish authFinish = null;

	private static boolean isProperlyConfigured = false;
	
	private static DropboxSynchronizer instance = null;
	

	public void activate() {
		DropboxSynchronizer.instance = this;
		startSynchronizationJobs();
	}
	
	public void deactivate() {
		logger.debug("about to shut down DropboxSynchronizer ...");
		
		cancelAllJobs();
		authorizationThreadInterrupted = true;
		isProperlyConfigured = false;
		
		lastCursor = null;
		lastHash = null;
		uploadFilterElements = DEFAULT_UPLOAD_FILE_FILTER;
		downloadFilterElements = DEFAULT_DOWNLOAD_FILE_FILTER;
		
		DropboxSynchronizer.instance = null;
	}
	
	
	private boolean isAuthorized() {
		try {
			WebAuthSession authSession = getSession();
			return authSession != null; 
		} catch (DropboxException de) {
			logger.debug("creating Dropbox session throws an exception", de);
			return false;
		}
	}

	/**
	 * Creates and returns a new {@link WebAuthSession}. The Session is either
	 * created with an {@link AccessTokenPair} restored from a previously created
	 * file 'authfile.dbx' or with a new {@link AccessTokenPair} returned by
	 * the manual Dropbox authorization process (via OAuth). 
	 * 
	 * @return a new {@link WebAuthSession} or <code>null</code> if the 
	 * authorization process couldn't be finished successfully.
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	private synchronized DbxWebAuthNoRedirect getSession() {
		DbxAppInfo appInfo = new DbxAppInfo(DropboxSynchronizer.appKey, DropboxSynchronizer.appSecret);
		DbxRequestConfig config = new DbxRequestConfig("openHAB/1.0", Locale.getDefault().toString());
		
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		
		if (webAuth == null) {
			File authFile = new File(contentDir + AUTH_FILE_NAME);
			if (authFile.exists()) {
				accessToken = extractAccessTokenFrom(authFile);
			} else {
				accessToken = authorizeOpenHAB(webAuth, authFile);
			}
		}

		if (accessToken != null) {
			session.setAccessTokenPair(accessToken);
		} else {
			session = null;
		}
		
		return session;
	}
	
	/**
	 * Initiates the OAuth authorization process with Dropbox. The authorization
	 * process is a multi step process which is described <a href="
	 * https://www.dropbox.com/developers/start/authentication#android">here</a>
	 * in more detail.
	 * 
	 * @param session the party initialized {@link WebAuthSession}.
	 * @param authFile the {@link File} to write an {@link AccessTokenPair} to
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	public void startAuthentication() {
		
		// start athorization process
		DbxWebAuthNoRedirect webAuth = getSession();
		String authUrl = webAuth.start();

		logger.info("#########################################################################################");
		logger.info("# Dropbox-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!");
		logger.info("# 1. Open URL '{}'", authUrl);
		logger.info("# 2. Paste the returned authorisation code here using the command 'dropbox token <token>'");
		logger.info("#########################################################################################");
	}
		
	public void finishAuthentication(String code) throws DbxException {
		DbxWebAuthNoRedirect webAuth = getSession();
        authFinish = webAuth.finish(code);
        
		if (authFinish != null) {
			logger.debug("Got token pair from Dropbox (key={}, secret={}) -> serialize to file for later use!", accessToken.key, accessToken.secret);
			writeLocalFile(authFile, accessToken.key + FIELD_DELIMITER + accessToken.secret);
		} else {
			logger.debug("AccessToken hasn't been updated by Dropbox. This is likely because the authorization timeslot timed out. Thus we didn't write an authfile for later use.");
			accessToken = null;
		}
	}
	
	
	private String readTokenFile(File file) {
		String content = null;
		if (file.exists()) {
			try {
				List<String> lines = FileUtils.readLines(file);
				if (lines.size() > 0) {
					content = lines.get(0);
				}
			} catch (IOException ioe) {
				logger.debug("Handling of cursor file throws an Exception", ioe);
			}
		}
		return content;
	}



	/**
	 * Synchronizes all changes from Dropbox to the local file system. Changes are
	 * identified by the Dropbox delta mechanism which takes the <code>lastCursor</code>
	 * field into account. If <code>lastCursor</code> is <code>null</code> it
	 * tries to recreate it from the file <code>deltacursor.dbx</code>. If
	 * it is still <code>null</code> all files are downloaded from the specified
	 * location.
	 * 
	 * Note: Since we define Dropbox as data master we do not care about local
	 * changes while downloading files!
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	public void syncDropboxToLocal() {
		logger.debug("Started synchronization from Dropbox to local ...");
		
		DbxWebAuthNoRedirect session = getSession();
		if (session == null) {
			return;
		}
		
		DropboxAPI<WebAuthSession> dropbox = new new DbxCDropboxAPI<WebAuthSession>(session);
		File cursorFile = new File(contentDir + DELTA_CURSOR_FILE_NAME);
		
		if (lastCursor == null) {
			lastCursor = extractDeltaCursor(cursorFile);
			logger.trace("Last cursor was NULL and has now been recreated from the filesystem (file='{}', cursor='{}')", cursorFile.getAbsolutePath(), lastCursor);
		}

		DbxDelta<Entry> deltaPage = dropbox.delta(lastCursor);
		if (deltaPage.entries != null && deltaPage.entries.size() == 0) {
			logger.debug("There are no deltas to download from Dropbox ...");
		} else {
			do {
				logger.debug("There are '{}' deltas to process ...", deltaPage.entries.size());
				int processedDelta = 0;
				
				for (DbxDelta<Entry> entry : deltaPage.entries) {
					boolean matches = false;
					for (String filter : downloadFilterElements) {
						matches |= entry.lcPath.matches(filter);
					}
					
					if (matches) {
						if (entry.metadata != null) {
							downloadFile(dropbox, entry);
						} else {
							String fqPath = contentDir + entry.lcPath;
							deleteLocalFile(fqPath);
						}
						processedDelta++;
					} else {
						logger.trace("skipped file '{}' since it doesn't match the given filter arguments.", entry.lcPath);
					}
				}
				logger.debug("'{}' deltas met the given downloadFilter {}", processedDelta, downloadFilterElements);
				
				// query again to check if there more entries to process!
				deltaPage = dropbox.delta(lastCursor);
			} while (deltaPage.hasMore);
		}

		writeLastCursorFile(deltaPage, cursorFile);
	}

	/**
	 * Synchronizes all changes from the local filesystem into Dropbox. Changes
	 * are identified by the files' <code>lastModified</code> attribut. If there
	 * are less files locally the additional files will be deleted from the
	 * Dropbox. New files will be uploaded or overwritten if they exist already.
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	public void syncLocalToDropbox() {
		logger.debug("Started synchronization from local to Dropbox ...");
		
		DbxWebAuthNoRedirect session = getSession();
		if (session == null) {
			return;
		}
		
		DropboxAPI<WebAuthSession> dropbox = new DropboxAPI<WebAuthSession>(session);
		Map<String, Long> dropboxEntries = new HashMap<String, Long>();

		Entry metadata = dropbox.metadata("/", -1, null, true, null);
		File dropboxEntryFile = new File(contentDir + DROPBOX_ENTRIES_FILE_NAME);
		if (!dropboxEntryFile.exists() || !metadata.hash.equals(lastHash)) {
			collectDropboxEntries(dropbox, dropboxEntries, "/");
			serializeDropboxEntries(dropboxEntryFile, dropboxEntries);
			lastHash = metadata.hash;
			
			// TODO: TEE: we could think about writing the 'lastHash' to a file?
			// let's see what daily use brings whether this a necessary feature!
		} else {
			logger.trace("Dropbox entry file '{}' exists -> extract content", dropboxEntryFile.getPath());
			dropboxEntries = extractDropboxEntries(dropboxEntryFile);
		}

		Map<String, Long> localEntries = new HashMap<String, Long>();
		collectLocalEntries(localEntries, contentDir);
		logger.debug("There are '{}' local entries that met the upload filters ...", localEntries.size());

		boolean isChanged = false;

		for (java.util.Map.Entry<String, Long> entry : localEntries.entrySet()) {
			if (dropboxEntries.containsKey(entry.getKey())) {
				if (entry.getValue().compareTo(dropboxEntries.get(entry.getKey())) > 0) {
					logger.trace("Local file '{}' is newer - upload to Dropbox!", entry.getKey());
					if (!fakeMode) {
						uploadOverwriteFile(dropbox, entry.getKey());
					}
					isChanged = true;
				}
			} else {
				logger.trace("Local file '{}' doesn't exist in Dropbox - upload to Dropbox!", entry.getKey());
				if (!fakeMode) {
					uploadFile(dropbox, entry.getKey());
				}
				isChanged = true;
			}

			dropboxEntries.remove(entry.getKey());
		}

		// all left dropboxEntries are only present in Dropbox and not locally (anymore)
		// so delete them from Dropbox!
		for (String path : dropboxEntries.keySet()) {
			for (String filter : uploadFilterElements) {
				if (path.matches(filter)) {
					if (!fakeMode) {
						dropbox.delete(path);
					}
					isChanged = true;
					logger.debug("Successfully deleted file '{}' from Dropbox", path);
				} else {
					logger.trace("skipped file '{}' since it doesn't match the given filter arguments.", path);
				}
			}
		}

		// when something changed we will remove the entry file 
		// which causes a new generation during the next sync
		if (isChanged) {
			boolean success = FileUtils.deleteQuietly(dropboxEntryFile);
			if (!success) {
				logger.warn("Couldn't delete file '{}'", dropboxEntryFile.getPath());
			} else {
				logger.debug("Deleted cache file '{}' since there are changes. It will be recreated while next synchronization loop.", dropboxEntryFile.getPath());
			}
			
			// since there are changes we have to update the lastCursor (and
			// the corresponding file) to have the right starting point for the
			// next synchronization loop
			DbxDelta<Entry> deltaPage = dropbox.delta(lastCursor);
			writeLastCursorFile(deltaPage, new File(contentDir + DELTA_CURSOR_FILE_NAME));
		} else {
			logger.debug("No files changed locally > no deltas to upload to Dropbox ...");
		}
	}
	
	
	private void downloadFile(DropboxAPI<WebAuthSession> dropbox, DeltaEntry<Entry> entry) throws DropboxException {
		String fqPath = contentDir + entry.metadata.path;
		File newLocalFile = new File(fqPath);

		if (entry.metadata.isDir) {
			// create intermediary directories
			boolean success = newLocalFile.mkdirs();
			if (!success) {
				logger.debug("Didn't create any intermediary directories for '{}'", fqPath);
			}
		} else {
			// if the parent directory doesn't exist create all intermediary
			// directorys ...
			if (!newLocalFile.getParentFile().exists()) {
				newLocalFile.getParentFile().mkdirs();
			}
			
			try {
				FileOutputStream os = new FileOutputStream(newLocalFile);
				if (!fakeMode) {
					dropbox.getFile(entry.metadata.path, null, os, null);
				}
				logger.debug("Successfully downloaded file '{}'", fqPath);
			} catch (FileNotFoundException fnfe) {
				throw new DbxException("Couldn't write file '" + fqPath + "'", fnfe);
			}
		}
		
		long lastModified = RESTUtility.parseDate(entry.metadata.modified).getTime();
		boolean success = newLocalFile.setLastModified(lastModified);
		if (!success) {
			logger.debug("Couldn't change attribute 'lastModified' of file '{}'", fqPath);
		}		
	}

	private Map<String, Long> extractDropboxEntries(File dropboxEntryFile) {
		Map<String, Long> dropboxEntries = new HashMap<String, Long>();
		try {
			List<String> lines = FileUtils.readLines(dropboxEntryFile);
			for (String line : lines) {
				String[] lineComponents = line.split(FIELD_DELIMITER);
				if (lineComponents.length == 2) {
					dropboxEntries.put(lineComponents[0], Long.valueOf(lineComponents[1]));
				} else {
					logger.trace("Couldn't parse line '{}' - it does not contain to elements delimited by '{}'", line, FIELD_DELIMITER);
				}
			}
		} catch (IOException ioe) {
			logger.warn("Couldn't read lines from file '{}'", dropboxEntryFile.getPath());
		}
		return dropboxEntries;
	}

	private void serializeDropboxEntries(File file, Map<String, Long> dropboxEntries) {
		try {
			StringBuffer sb = new StringBuffer();
			for (java.util.Map.Entry<String, Long> line : dropboxEntries.entrySet()) {
				sb.append(line.getKey()).append(FIELD_DELIMITER).append(line.getValue()).append(LINE_DELIMITER);
			}
			FileUtils.writeStringToFile(file, sb.toString());
		} catch (IOException e) {
			logger.warn("Couldn't write file '{}'", file.getPath());
		}
	}

	private void collectLocalEntries(Map<String, Long> localEntries, String path) {
		File[] files = new File(path).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				String normalizedPath = substringAfter(file.getPath(), contentDir);
				for (String filter : uploadFilterElements) {
					if (FilenameUtils.getName(normalizedPath).startsWith(".")) {
						return false;
					} else if (FilenameUtils.getName(normalizedPath).endsWith(".dbx")) {
						return false;
					} else if (normalizedPath.matches(filter)) {
						return true;
					} 
				}
				
				logger.trace("skipped file '{}' since it doesn't match the given filter arguments.", file.getAbsolutePath());
				return false;
			}
		});
		
		for (File file : files) {
			String normalizedPath = substringAfter(file.getPath(), contentDir);
			if (file.isDirectory()) {
				collectLocalEntries(localEntries, file.getPath());
			} else {
				localEntries.put(normalizedPath, file.lastModified());
			}
		}
	}

	private void collectDropboxEntries(DropboxAPI<WebAuthSession> dropbox, Map<String, Long> dropboxEntries, String path) throws DropboxException {
		Entry entries = dropbox.metadata(path, -1 /* unlimited */, null, true /* children */, null /* latest */);
		for (Entry entry : entries.contents) {
			if (entry.isDir) {
				collectDropboxEntries(dropbox, dropboxEntries, entry.path);
			} else {
				dropboxEntries.put(entry.path, RESTUtility.parseDate(entry.modified).getTime());
			}
		}
	}

	/*
	 *  TODO: TEE: Currently there is now way to change the attribute 
	 *  'lastModified' of the files to upload via Dropbox API. See the 
	 *  discussion below for  more details.
	 *  
	 *  Since this is a missing feature (from my point of view) we should
	 *  check the improvements of the API development on regular basis.
	 *  
	 *  @see http://forums.dropbox.com/topic.php?id=22347
	 */
	private void uploadFile(DbxClient client, String dropboxPath, boolean overwrite) throws DbxException, IOException {
		File file = new File(contentDir + File.separator + dropboxPath);
        FileInputStream inputStream = new FileInputStream(file);
        try {
        	DbxWriteMode mode = overwrite ? DbxWriteMode.force() : DbxWriteMode.add();
            DbxEntry.File uploadedFile = 
            	client.uploadFile(dropboxPath, mode, file.length(), inputStream);
			logger.debug("successfully uploaded file '{}'. New revision is '{}'", uploadedFile.toString(), uploadedFile.rev);
        } finally {
            inputStream.close();
        }
	}
	
	private static void writeLastCursorFile(DbxDelta<Entry> deltaPage, File cursorFile) {
		String newCursor = deltaPage.cursor;
		if (!newCursor.equals(lastCursor)) {
			logger.trace("Delta-Cursor changed (lastCursor '{}', newCursor '{}')", lastCursor, newCursor);
			writeLocalFile(cursorFile, newCursor);
			lastCursor = newCursor;
		}
	}

	private static void writeLocalFile(File file, String content) {
		try {
			FileUtils.writeStringToFile(file, content);
			logger.debug("Created file '{}' with content '{}'", file.getAbsolutePath(), content);
		} catch (IOException e) {
			logger.error("Couldn't write to file '" + file.getPath() + "'.", e);
		}
	}

	private static void deleteLocalFile(String fqPath) {
		File fileToDelete = new File(fqPath);
		if (!fileToDelete.isDirectory()) {
			boolean success = true;
			if (!fakeMode) {
				FileUtils.deleteQuietly(fileToDelete);
			}
			
			if (success) {
				logger.debug("Successfully deleted local file '{}'", fqPath);
			} else {
				logger.debug("Local file '{}' couldn't be deleted", fqPath);
			}
		} else {
			logger.trace("Local file '{}' isn't deleted because it is a directory");
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			isProperlyConfigured = false;
						
			String appKeyString = (String) config.get("appkey");
			if (isNotBlank(appKeyString)) {
				DropboxSynchronizer.appKey = appKeyString;
			}
			
			String appSecretString = (String) config.get("appsecret");
			if (isNotBlank(appSecretString)) {
				DropboxSynchronizer.appSecret = appSecretString;
			}
			
			if (isBlank(DropboxSynchronizer.appKey) || isBlank(DropboxSynchronizer.appSecret)) {
				throw new ConfigurationException("dropbox:appkey",
					"The parameters 'appkey' or 'appsecret' are missing! Please refer to your 'openhab.cfg'");
			}
			
			String fakeModeString = (String) config.get("fakemode");
			if (isNotBlank(fakeModeString)) {
				DropboxSynchronizer.fakeMode = BooleanUtils.toBoolean(fakeModeString);
			}
			
			String contentDirString = (String) config.get("contentdir");
			if (isNotBlank(contentDirString)) {
				DropboxSynchronizer.contentDir = contentDirString;
			}

			String uploadIntervalString = (String) config.get("uploadInterval");
			if (isNotBlank(uploadIntervalString)) {
				DropboxSynchronizer.uploadInterval = uploadIntervalString;
			}

			String downloadIntervalString = (String) config.get("downloadInterval");
			if (isNotBlank(downloadIntervalString)) {
				DropboxSynchronizer.downloadInterval = downloadIntervalString;
			}

			String syncModeString = (String) config.get("syncmode");
			if (isNotBlank(syncModeString)) {
				try {
					DropboxSynchronizer.syncMode = DropboxSyncMode.valueOf(syncModeString.toUpperCase());
				} catch (IllegalArgumentException iae) {
					throw new ConfigurationException(
						"dropbox:syncmode", "Unknown SyncMode '" + syncModeString
						+ "'. Valid SyncModes are 'DROPBOX_TO_LOCAL', 'LOCAL_TO_DROPBOX' and 'BIDIRECTIONAL'.");
				}
			}
			
			String uploadFilterString = (String) config.get("uploadfilter");
			if (isNotBlank(uploadFilterString)) {
				String[] newFilterElements = uploadFilterString.split(",");
				uploadFilterElements = Arrays.asList(newFilterElements);
			}

			String downloadFilterString = (String) config.get("downloadfilter");
			if (isNotBlank(downloadFilterString)) {
				String[] newFilterElements = downloadFilterString.split(",");
				downloadFilterElements = Arrays.asList(newFilterElements);
			}
			
			// we got thus far, so we define this synchronizer as properly configured ...
			isProperlyConfigured = true;
			activate();
		}
	}
	
	
	// ****************************************************************************
	// Synchronisation Jobs 
	// ****************************************************************************
	
	private void startSynchronizationJobs() {
		if (isProperlyConfigured) {
			cancelAllJobs();
			if (isAuthorized()) {
				scheduleJobs();
			} else {
				logger.debug("Dropbox-Bundle isn't authorized properly, so the synchronization jobs " +
					"won't be started! Please re-initiate the authorization process by restarting the " +
					"Dropbox-Bundle through OSGi console.");
			}
		}
	}

	/**
	 * Schedules the quartz synchronization according to the synchronization mode
	 */
	private void scheduleJobs() {
		switch (syncMode) {
			case DROPBOX_TO_LOCAL:
				schedule(DropboxSynchronizer.downloadInterval, false);
				break;
			case LOCAL_TO_DROPBOX:
				schedule(DropboxSynchronizer.uploadInterval, true);
				break;
			case BIDIRECTIONAL:
				schedule(DropboxSynchronizer.downloadInterval, false);
				schedule(DropboxSynchronizer.uploadInterval, true);
				break;
			default:
				throw new IllegalArgumentException("Unknown SyncMode '" + syncMode.toString() + "'");
		}
	}

	/**
	 * Schedules either a job handling the Upload (<code>LOCAL_TO_DROPBOX</code>)
	 * or Download (<code>DROPBOX_TO_LOCAL</code>) direction depending on
	 * <code>isUpload</code>.
	 * 
	 * @param interval the Trigger interval as cron expression
	 * @param isUpload
	 */
	private void schedule(String interval, boolean isUpload) {
		String direction = isUpload ? "Upload" : "Download";
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			JobDetail job = newJob(SynchronizationJob.class)
				.withIdentity(direction, DROPBOX_SCHEDULER_GROUP)
			    .build();

			CronTrigger trigger = newTrigger()
			    .withIdentity(direction, DROPBOX_SCHEDULER_GROUP)
			    .withSchedule(CronScheduleBuilder.cronSchedule(interval))
			    .build();

			sched.scheduleJob(job, trigger);
			logger.debug("Scheduled synchronization job (direction={}) with cron expression '{}'", direction, interval);
		} catch (SchedulerException e) {
			logger.warn("Could not create synchronization job: {}", e.getMessage());
		}		
	}
	
	/**
	 * Delete all quartz scheduler jobs of the group <code>Dropbox</code>.
	 */
	private void cancelAllJobs() {
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			Set<JobKey> jobKeys = sched.getJobKeys(jobGroupEquals(DROPBOX_SCHEDULER_GROUP));
			if (jobKeys.size() > 0) {
				sched.deleteJobs(new ArrayList<JobKey>(jobKeys));
				logger.debug("Found {} synchronization jobs to delete from DefaulScheduler (keys={})", jobKeys.size(), jobKeys);
			}
		} catch (SchedulerException e) {
			logger.warn("Couldn't remove synchronization job: {}", e.getMessage());
		}		
	}
	
	/**
	 * A quartz scheduler job to execute the synchronization. There can be only
	 * one instance of a specific job type running at the same time.
	 */
	@DisallowConcurrentExecution
	public static class SynchronizationJob implements Job {
		
		private final static JobKey UPLOAD_JOB_KEY = new JobKey("Upload", DROPBOX_SCHEDULER_GROUP);
		
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			boolean isUpload = 
				UPLOAD_JOB_KEY.compareTo(context.getJobDetail().getKey()) == 0;
			
			DropboxSynchronizer synchronizer = DropboxSynchronizer.instance; 
			if (synchronizer != null) {
				if (isUpload) {
					synchronizer.syncLocalToDropbox();
				} else {
					synchronizer.syncDropboxToLocal();
				}
			} else {
				logger.debug("DropboxSynchronizer instance hasn't been initialized properly!");
			}
		}
		
	}


}
