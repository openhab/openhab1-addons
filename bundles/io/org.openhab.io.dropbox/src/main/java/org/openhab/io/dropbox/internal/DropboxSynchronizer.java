/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.dropbox.internal;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
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
import org.apache.commons.lang.StringUtils;
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
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxDelta;
import com.dropbox.core.DbxDelta.Entry;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.WithChildren;
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

	private static final Logger logger = LoggerFactory.getLogger(DropboxSynchronizer.class);
	
	private static final String DROPBOX_SCHEDULER_GROUP = "Dropbox";

	private static final String FIELD_DELIMITER = "@@";
	
	private static final String LINE_DELIMITER = System.getProperty("line.separator");
	

	private static final String DELTA_CURSOR_FILE_NAME = File.separator + "deltacursor.dbx";

	private static final String DROPBOX_ENTRIES_FILE_NAME = File.separator + "dropbox-entries.dbx";

	private static final String AUTH_FILE_NAME = File.separator + "authfile.dbx";
	
	/** holds the id of the last synchronisation cursor. This is needed to define the delta to download from Dropbox. */
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
	
	private static boolean isProperlyConfigured = false;
	
	private static DropboxSynchronizer instance = null;
	
	private static final DbxAppInfo appInfo =
		new DbxAppInfo(DropboxSynchronizer.appKey, DropboxSynchronizer.appSecret);
	
	private final static DbxRequestConfig requestConfig = 
		new DbxRequestConfig("openHAB/1.0", Locale.getDefault().toString());
	

	public void activate() {
		DropboxSynchronizer.instance = this;
	}
	
	public void deactivate() {
		logger.debug("about to shut down DropboxSynchronizer ...");
		
		cancelAllJobs();
		isProperlyConfigured = false;
		
		lastCursor = null;
		uploadFilterElements = DEFAULT_UPLOAD_FILE_FILTER;
		downloadFilterElements = DEFAULT_DOWNLOAD_FILE_FILTER;
		
		DropboxSynchronizer.instance = null;
	}
	
	private void activateSynchronizer() {
		if (isAuthenticated()) {
			startSynchronizationJobs();
		} else {
			try {
				startAuthentication();
			} catch (DbxException e) {
				logger.warn("Couldn't start authentication process: {}", e.getMessage());;
			}
		}
	}
	
	/**
	 * Starts the OAuth authorization process with Dropbox. The authorization
	 * process is a multi step process which is described in the Wiki in detail.
	 * 
	 * @throws DbxException if there are technical or application level errors 
	 * in the Dropbox communication
	 * 
	 * @see <a href="http://code.google.com/p/openhab/wiki/DropboxIOBundle">openHAB Dropbox Wiki</a>
	 */
	public void startAuthentication() throws DbxException {
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(requestConfig, appInfo);
		String authUrl = webAuth.start();

		logger.info("#########################################################################################");
		logger.info("# Dropbox-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!");
		logger.info("# 1. Open URL '{}'", authUrl);
		logger.info("# 2. Allow openHAB to access Dropbox");
		logger.info("# 3. Paste the authorisation code here using the command 'finishAuthentication \"<token>\"'");
		logger.info("#########################################################################################");
	}
		
	/**
	 * Finishes the OAuth authorization process by taking the given {@code token} and creating 
	 * an accessToken out of it. The authorization process is a multi step process which is 
	 * described in the Wiki in detail.
	 * 
	 * @throws DbxException if there are technical or application level errors 
	 * in the Dropbox communication
	 * 
	 * @see <a href="http://code.google.com/p/openhab/wiki/DropboxIOBundle">openHAB Dropbox Wiki</a>
	 */
	public void finishAuthentication(String code) throws DbxException {
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(requestConfig, appInfo);
        String accessToken = webAuth.finish(code).accessToken;
		writeAccessToken(accessToken);
		
		logger.info("#########################################################################################");
		logger.info("# OAuth2 authentication flow has been finished successfully ");
		logger.info("#########################################################################################");
		
		startSynchronizationJobs();
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
	 * @throws DbxException if there are technical or application level 
	 * errors in the Dropbox communication
	 * @throws IOException 
	 */
	public void syncDropboxToLocal(DbxClient client) throws DbxException, IOException {
		logger.debug("Started synchronization from Dropbox to local ...");
				
		lastCursor = readDeltaCursor();
		if (StringUtils.isBlank(lastCursor)) {
			logger.trace("Last cursor was NULL and has now been recreated from the filesystem '{}'", lastCursor);
		}

		DbxDelta<DbxEntry> deltaPage = client.getDelta(lastCursor);
		if (deltaPage.entries != null && deltaPage.entries.size() == 0) {
			logger.debug("There are no deltas to download from Dropbox ...");
		} else {
			do {
				logger.debug("There are '{}' deltas to process ...", deltaPage.entries.size());
				int processedDelta = 0;
				
				for (Entry<DbxEntry> entry : deltaPage.entries) {
					boolean matches = false;
					for (String filter : downloadFilterElements) {
						matches |= entry.lcPath.matches(filter);
					}
					
					if (matches) {
						if (entry.metadata != null) {
							downloadFile(client, entry);
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
				deltaPage = client.getDelta(lastCursor);
			} while (deltaPage.hasMore);
		}

		writeDeltaCursor(deltaPage.cursor);
	}

	/**
	 * Synchronizes all changes from the local filesystem into Dropbox. Changes
	 * are identified by the files' <code>lastModified</code> attribut. If there
	 * are less files locally the additional files will be deleted from the
	 * Dropbox. New files will be uploaded or overwritten if they exist already.
	 * 
	 * @throws DbxException if there are technical or application level 
	 * errors in the Dropbox communication
	 * @throws IOException 
	 */
	public void syncLocalToDropbox(DbxClient client) throws DbxException, IOException {
		logger.debug("Started synchronization from local to Dropbox ...");
		
		Map<String, Long> dropboxEntries = new HashMap<String, Long>();

		WithChildren metadata = client.getMetadataWithChildren("/");
		File dropboxEntryFile = new File(contentDir + DROPBOX_ENTRIES_FILE_NAME);
		if (!dropboxEntryFile.exists() || !metadata.hash.equals(lastHash)) {
			collectDropboxEntries(client, dropboxEntries, "/");
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
						uploadFile(client, entry.getKey(), true);
					}
					isChanged = true;
				}
			} else {
				logger.trace("Local file '{}' doesn't exist in Dropbox - upload to Dropbox!", entry.getKey());
				if (!fakeMode) {
					uploadFile(client, entry.getKey(), false);
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
						client.delete(path);
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
			DbxDelta<DbxEntry> delta = client.getDelta(lastCursor);
			writeDeltaCursor(delta.cursor);
		} else {
			logger.debug("No files changed locally > no deltas to upload to Dropbox ...");
		}
	}
	
	
	private void downloadFile(DbxClient client, Entry<DbxEntry> entry) throws DbxException, IOException {
		String fqPath = contentDir + entry.metadata.path;
		File newLocalFile = new File(fqPath);

		if (entry.metadata.isFolder()) {
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
					client.getFile(entry.metadata.path, null, os);
				}
				logger.debug("Successfully downloaded file '{}'", fqPath);
			} catch (FileNotFoundException fnfe) {
				throw new DbxException("Couldn't write file '" + fqPath + "'", fnfe);
			}
			
			long lastModified = entry.metadata.asFile().lastModified.getTime();
			boolean success = newLocalFile.setLastModified(lastModified);
			if (!success) {
				logger.debug("Couldn't change attribute 'lastModified' of file '{}'", fqPath);
			}		
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
				String normalizedPath = StringUtils.substringAfter(file.getPath(), contentDir);
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
			String normalizedPath = StringUtils.substringAfter(file.getPath(), contentDir);
			if (file.isDirectory()) {
				collectLocalEntries(localEntries, file.getPath());
			} else {
				localEntries.put(normalizedPath, file.lastModified());
			}
		}
	}

	private void collectDropboxEntries(DbxClient client, Map<String, Long> dropboxEntries, String path) throws DbxException {
		WithChildren entries = client.getMetadataWithChildren(path);
		for (DbxEntry entry : entries.children) {
			if (entry.isFolder()) {
				collectDropboxEntries(client, dropboxEntries, entry.path);
			} else {
				dropboxEntries.put(entry.path, entry.asFile().lastModified.getTime());
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
	
	private void writeAccessToken(String content) {
		File tokenFile = new File(contentDir + AUTH_FILE_NAME);
		writeLocalFile(tokenFile, content);
	}
	
	private String readAccessToken() {
		File tokenFile = new File(contentDir + AUTH_FILE_NAME);
		return readFile(tokenFile);
	}
	
	private boolean isAuthenticated() {
		return StringUtils.isNotBlank(readAccessToken());
	}

	private void writeDeltaCursor(String deltaCursor) {
		if (!deltaCursor.equals(lastCursor)) {
			logger.trace("Delta-Cursor changed (lastCursor '{}', newCursor '{}')", lastCursor, deltaCursor);
			File cursorFile = new File(contentDir + DELTA_CURSOR_FILE_NAME);
			writeLocalFile(cursorFile, deltaCursor);
			lastCursor = deltaCursor;
		}
	}

	private String readDeltaCursor() {
		File cursorFile = new File(contentDir + DELTA_CURSOR_FILE_NAME);
		return readFile(cursorFile);
	}

	private String readFile(File file) {
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
			activateSynchronizer();
		}
	}
	
	
	// ****************************************************************************
	// Synchronisation Jobs 
	// ****************************************************************************
	
	private void startSynchronizationJobs() {
		if (isProperlyConfigured) {
			cancelAllJobs();
			if (isAuthenticated()) {
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
				try {
					DbxClient client = getClient(synchronizer);
					if (client != null) {
						if (isUpload) {
							synchronizer.syncLocalToDropbox(client);
						} else {
							synchronizer.syncDropboxToLocal(client);
						}
					} else {
						logger.info("Couldn't create Dropbox client. Most likely there has been no "
							+ "access token found. Please restart authentication process by typing "
							+ "'startAuthentication' on the OSGi console");
					}
				} catch (Exception e) {
					logger.warn("Synchronizing data with Dropbox throws an exception: {}", e.getMessage());
				}
			} else {
				logger.debug("DropboxSynchronizer instance hasn't been initialized properly!");
			}
		}
		
		/**
		 * Creates and returns a new {@link DbxClient} initialized with the store access token.
		 * Returns {@code null} if no access token has been found.
		 * 
		 * @return a new {@link DbxClient} or <code>null</code> if no access token has been found.
		 */
		private DbxClient getClient(DropboxSynchronizer synchronizer) {
			String accessToken = synchronizer.readAccessToken();
		 	if (StringUtils.isNotBlank(accessToken)) {
		 		return new DbxClient(requestConfig, accessToken);
		 	}
			return null;
		}
		
	}
	

}
