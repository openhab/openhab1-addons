/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.io.dropbox.DropboxSynchronizer;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DeltaEntry;
import com.dropbox.client2.DropboxAPI.DeltaPage;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.RESTUtility;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

/**
 * The {@link DropboxSynchronizerImpl} is able to synchronize contents of your Dropbox
 * to the local file system and vice versa. There three synchronization modes
 * available: local to dropbox (which is the default mode), dropbox to local and
 * bidirectional.
 * 
 * Note: The {@link DropboxSynchronizerImpl} must be authorized against Dropbox one
 * time. Watch the logfile for the URL to open in your Browser and allow openHAB
 * to connect to a predefined App-Folder (see <a 
 * href="https://www.dropbox.com/developers/apps">Dropbox Documentation</a> for more information).
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class DropboxSynchronizerImpl extends AbstractActiveService implements DropboxSynchronizer, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(DropboxSynchronizerImpl.class);
	

	private static final String FIELD_DELIMITER = "@@";

	private static final String LINE_DELIMITER = System.getProperty("line.separator");

	private static final String DELTA_CURSOR_FILE_NAME = File.separator + "deltacursor.dbox";

	private static final String DROPBOX_ENTRIES_FILE_NAME = File.separator + "dropbox-entries.dbox";

	private static final String AUTH_FILE_NAME = File.separator + "authfile.dbox";

	
	private static String lastCursor = null;
	
	private static String lastHash = null;
	

	/** the configured AppKey (can be obtained from the <a href="https://www.dropbox.com/developers/apps">Dropbox Website</a>) */
	private static String appKey;

	/** the configured AppSecret (can be obtained from the <a href="https://www.dropbox.com/developers/apps">Dropbox Website</a>) */
	private static String appSecret;

	/** The default directory to download files from Dropbox to (currently '.') */
	private static final String DEFAULT_CONTENT_DIR = ".";
	
	/** the base directory to synchronize with openHAB, configure 'filter' to select files (defaults to DEFAULT_CONTENT_DIR) */
	private static String contentDir = DEFAULT_CONTENT_DIR;

	/** the configured synchronization mode (defaults to LOCAL_TO_DROPBOX) */
	private static DropboxSyncMode syncMode = DropboxSyncMode.LOCAL_TO_DROPBOX;

	/** the configured refresh interval (defaults to 5 minutes) */
	private static long refreshInterval = 300000L;
	
	private static final List<String> DEFAULT_FILE_FILTER = Arrays.asList("^([^/]*/){1}[^/]*$", "/configurations.*", "/logs.*", "/etc.*");
	
	/** defines a comma separated list of regular expressions to filter files which won't be uploaded to Dropbox (optional, defaults to '/configurations.*, /logs.*, /etc.*') */
	private static List<String> filterElements = DEFAULT_FILE_FILTER;
	

	private boolean isProperlyConfigured = false;
	
	private static DropboxAPI<WebAuthSession> dropbox;

	private static AccessTokenPair accessToken;
	

	/**
	 * @{inheritDoc}
	 * 
	 * Additionally some static members are being reset.
	 */
	@Override
	public void deactivate() {
		logger.debug("about to shut down Dropbox Synchronizer ...");
		
		lastCursor = null;
		lastHash = null;
		filterElements = DEFAULT_FILE_FILTER;
		super.deactivate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeSyncMode(DropboxSyncMode newSyncMode) {
		syncMode = newSyncMode;
		logger.debug("changed synchronization to '{}'", newSyncMode);
	}
	

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Dropbox Synchronizer";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isProperlyConfigured() {
		return isProperlyConfigured;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}
	

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		try {
			dropbox = new DropboxAPI<WebAuthSession>(getSession());
			if (dropbox != null) {
				switch (syncMode) {
				case DROPBOX_TO_LOCAL:
					syncDropboxToLocal(dropbox);
					break;
				case LOCAL_TO_DROPBOX:
					syncLocalToDropbox(dropbox);
					break;
				case BIDIRECTIONAL:
					syncBidirectional(dropbox);
					break;
				default:
					throw new IllegalArgumentException("Unknown SyncMode '" + syncMode.toString() + "'");
				}
			} else {
				logger.debug("DropboxAPI hasn't been initialized properly!");
			}
		} catch (DropboxException de) {
			logger.error("Interaction with Dropbox API throws an exception", de);
		}
	}
	

	/**
	 * Creates and returns a new {@link WebAuthSession}. The Session is either
	 * created with an {@link AccessTokenPair} restored from a previously created
	 * file 'authfile.dbox' or with a new {@link AccessTokenPair} returned by
	 * a manual authorization process. 
	 * 
	 * @return a new {@link WebAuthSession} which might be unauthorized.
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	private WebAuthSession getSession() throws DropboxException {
		AppKeyPair appKeys = new AppKeyPair(DropboxSynchronizerImpl.appKey, DropboxSynchronizerImpl.appSecret);
		WebAuthSession session = new WebAuthSession(appKeys, AccessType.APP_FOLDER);

		if (accessToken == null) {
			File authFile = new File(contentDir + AUTH_FILE_NAME);
			if (authFile.exists()) {
				accessToken = extractAccessTokenFrom(authFile);
			} else {
				accessToken = authorizeOpenhab(session, authFile);
			}
		}

		if (accessToken != null) {
			session.setAccessTokenPair(accessToken);
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
	 * @return an {@link AccessTokenPair} which might be <code>null</code>
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	private AccessTokenPair authorizeOpenhab(WebAuthSession session, File authFile) throws DropboxException {
		WebAuthInfo authInfo = session.getAuthInfo();
		RequestTokenPair pair = authInfo.requestTokenPair;

		logger.info("################################################################################################");
		logger.info("# Dropbox-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!");
		logger.info("# 1. Open URL '{}'", authInfo.url);
		logger.info("# 2. Allow openHAB to access Dropbox. Note: The given URL is only valid for THE NEXT 5 MINUTES!");
		logger.info("################################################################################################");

		boolean interrupted = false;
		int waitedFor = 0;
		while (!interrupted) {
			try {
				int interval = 5000;
				Thread.sleep(interval);
				waitedFor += interval;
				try {
					session.retrieveWebAccessToken(pair);
					interrupted = true;
				} catch (DropboxUnlinkedException due) {
					// ignore this Exception since we expect it! It will occur
					// as long as the user has not allowed access to dropbox 
				}
			} catch (InterruptedException e) {
				// ignore
			}

			// if we already waited for more than five minutes we have to cancel
			// the authorization process since the token has been invalidated by
			// dropbox now
			if (waitedFor > 300000) {
				interrupted = true;
				logger.info("Authorization timeslot is closed now! Please use OSGi "
						+ "console to restart the Dropbox-Bundle and re-initiate the authorization process!");
			}
		}

		AccessTokenPair accessToken = session.getAccessTokenPair();
		if (accessToken != null) {
			logger.debug("Got token pair from Dropbox (key={}, secret={}) -> serialize to file for later use!", accessToken.key, accessToken.secret);
			writeLocalFile(authFile, accessToken.key + FIELD_DELIMITER + accessToken.secret);
		} else {
			logger.warn("AccessToken is null, so we can't write it to a file for later use.");
		}

		return accessToken;
	}

	/**
	 * Synchronizes all changes from Dropbox to the local file system. Changes are
	 * identified by the Dropbox delta mechanism which takes the <code>lastCursor</code>
	 * field into account. If <code>lastCursor</code> is <code>null</code> it
	 * tries to recreate it from the file <code>deltacursor.dbox</code>. If
	 * it is still <code>null</code> all files are downloaded from the specified
	 * location.
	 * 
	 * Note: Since we define Dropbox as data master we do not care about local
	 * changes while downloading files!
	 * 
	 * @param dropbox a handle to the {@link DropboxAPI}
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	public void syncDropboxToLocal(DropboxAPI<WebAuthSession> dropbox) throws DropboxException {
		File cursorFile = new File(contentDir + DELTA_CURSOR_FILE_NAME);
		if (lastCursor == null) {
			lastCursor = extractDeltaCursor(cursorFile);
			logger.trace("Last cursor was NULL and has now been recreated from the filesystem (file='{}', cursor='{}')", cursorFile.getAbsolutePath(), lastCursor);
		}

		DeltaPage<Entry> deltaPage = dropbox.delta(lastCursor);

		if (deltaPage.entries != null && deltaPage.entries.size() == 0) {
			logger.debug("There are no deltas to download from Dropbox ...");
		} else {
			for (DeltaEntry<Entry> entry : deltaPage.entries) {
				if (entry.metadata != null) {
					downloadFile(entry);
				} else {
					deleteLocalFile(entry.lcPath);
				}
			}
		}

		writeLastCursorFile(deltaPage, cursorFile);
	}

	/**
	 * Synchronizes all changes from the local filesystem into Dropbox. Changes
	 * are identified by the files' <code>lastModified</code> attribut. If there
	 * are less files locally the additional files will be deleted from the
	 * Dropbox. New files will be uploaded or overwritten if they exist already.
	 * 
	 * @param dropbox a handle to the {@link DropboxAPI}
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	public void syncLocalToDropbox(DropboxAPI<WebAuthSession> dropbox) throws DropboxException {
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

		boolean isChanged = false;

		for (java.util.Map.Entry<String, Long> entry : localEntries.entrySet()) {
			if (dropboxEntries.containsKey(entry.getKey())) {
				if (entry.getValue().compareTo(dropboxEntries.get(entry.getKey())) > 0) {
					logger.trace("Local file '{}' is newer - upload into Dropbox!", entry.getKey());
					uploadOverwriteFile(entry.getKey());
					isChanged = true;
				}
			} else {
				logger.trace("Local file '{}' doesn't exist in Dropbox - upload into Dropbox!", entry.getKey());
				uploadFile(entry.getKey());
				isChanged = true;
			}

			dropboxEntries.remove(entry.getKey());
		}

		for (String path : dropboxEntries.keySet()) {
			for (String filter : filterElements) {
				if (path.matches(filter)) {
					dropbox.delete(path);
					isChanged = true;
					logger.debug("Successfully deleted file '{}' from Dropbox", path);
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
			DeltaPage<Entry> deltaPage = dropbox.delta(lastCursor);
			writeLastCursorFile(deltaPage, new File(contentDir + DELTA_CURSOR_FILE_NAME));
		} else {
			logger.debug("There are no deltas to upload into Dropbox ...");
		}
	}

	/**
	 * Synchronizes all changes in both directions starting with 'Dropbox to local'
	 * and ending with 'Local to Dropbox' so Dropbox can be seen as data master
	 * since we do not care about local changes in this case.
	 * 
	 * @param dropbox a handle to the {@link DropboxAPI}
	 * 
	 * @throws DropboxException if there are technical or application level 
	 * errors in the Dropbox communication
	 */
	public void syncBidirectional(DropboxAPI<WebAuthSession> dropbox) throws DropboxException {
		syncDropboxToLocal(dropbox);
		syncLocalToDropbox(dropbox);
	}
	
	
	private AccessTokenPair extractAccessTokenFrom(File authFile) {
		AccessTokenPair tokenPair = null;
		try {
			List<String> lines = FileUtils.readLines(authFile);
			if (lines.size() > 0) {
				String line = lines.get(0);
				String[] tokenPairArray = line.split(FIELD_DELIMITER);
				if (tokenPairArray.length == 2) {
					tokenPair = new AccessTokenPair(tokenPairArray[0], tokenPairArray[1]);
				}
			}
		} catch (IOException ioe) {
			logger.debug("Handling of authentication file throws an Exception", ioe);
		}
		return tokenPair;
	}
	
	private String extractDeltaCursor(File cursorFile) {
		String cursor = null;
		if (cursorFile.exists()) {
			try {
				List<String> lines = FileUtils.readLines(cursorFile);
				if (lines.size() > 0) {
					cursor = lines.get(0);
				}
			} catch (IOException ioe) {
				logger.debug("Handling of cursor file throws an Exception", ioe);
			}
		}
		return cursor;
	}

	private void downloadFile(DeltaEntry<Entry> entry) throws DropboxException {
		String fqPath = contentDir + entry.metadata.path;
		File newLocalFile = new File(fqPath);

		if (entry.metadata.isDir) {
			// create intermediary directories
			boolean success = newLocalFile.mkdirs();
			if (!success) {
				logger.debug("Didn't create any intermediary directories for '{}'", fqPath);
			}
		} else {
			try {
				dropbox.getFile(entry.metadata.path, null, new FileOutputStream(newLocalFile), null);
			} catch (FileNotFoundException fnfe) {
				logger.warn("Couldn't download file '" + fqPath + "'", fnfe);
			}
		}
		
		long lastModified = RESTUtility.parseDate(entry.metadata.modified).getTime();
		boolean success = newLocalFile.setLastModified(lastModified);
		if (!success) {
			logger.debug("Couldn't change attribute 'lastModified' of file '{}'", fqPath);
		}
		
		logger.debug("Successfully downloaded file '{}'", fqPath);
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
				for (String filter : filterElements) {
					if (FilenameUtils.getName(normalizedPath).startsWith(".")) {
						return false;
					} else if (normalizedPath.matches(filter)) {
						return true;
					} 
				}
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
	 *  TODO: TEE: Currenty there is now way to change the attribut 
	 *  'lastModified' of the files to upload via Dropbox API. See the 
	 *  discussion below for  more details.
	 *  
	 *  Since this is a missing feature (from my point of view) we should
	 *  check the improvements of the API development on regular basis.
	 *  
	 *  @see http://forums.dropbox.com/topic.php?id=22347
	 */
	private void uploadFile(String dropboxPath) throws DropboxException {
		try {
			File file = new File(contentDir + File.separator + dropboxPath);
			Entry newEntry = dropbox.putFile(dropboxPath, new FileInputStream(file), file.length(), null, null);
			logger.debug("successfully uploaded file '{}'. New revision is '{}'", file.getPath(), newEntry.rev);
		} catch (FileNotFoundException fnfe) {
			logger.error("Errors while uploading file '" + dropboxPath + "'", fnfe);
		}
	}

	/*
	 *  TODO: TEE: Currenty there is now way to change the attribut 
	 *  'lastModified' of the files to upload via Dropbox API. See the 
	 *  discussion below for  more details.
	 *  
	 *  Since this is a missing feature (from my point of view) we should
	 *  check the improvements of the API development on regular basis.
	 *  
	 *  @see http://forums.dropbox.com/topic.php?id=22347
	 */
	private void uploadOverwriteFile(String dropboxPath) throws DropboxException {
		try {
			File file = new File(contentDir + File.separator + dropboxPath);
			Entry newEntry = dropbox.putFileOverwrite(dropboxPath, new FileInputStream(file), file.length(), null);
			logger.debug("successfully overwritten file '{}'. New revision is '{}'", file.getPath(), newEntry.rev);
		} catch (FileNotFoundException fnfe) {
			logger.error("Errors while uploading file '" + dropboxPath + "'", fnfe);
		}
	}
	
	private static void writeLastCursorFile(DeltaPage<Entry> deltaPage, File cursorFile) {
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

	private static void deleteLocalFile(String dropboxPath) {
		String fqPath = contentDir + dropboxPath;
		File fileToDelete = new File(fqPath);
		boolean success = FileUtils.deleteQuietly(fileToDelete);
		if (success) {
			logger.debug("Successfully deleted local file '{}'", fqPath);
		} else {
			logger.debug("Local file '{}' couldn't be deleted", fqPath);
		}
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			DropboxSynchronizerImpl.appKey = (String) config.get("appkey");
			DropboxSynchronizerImpl.appSecret = (String) config.get("appsecret");

			String contentDirString = (String) config.get("contentdir");
			if (StringUtils.isNotBlank(contentDirString)) {
				DropboxSynchronizerImpl.contentDir = contentDirString;
			}

			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				DropboxSynchronizerImpl.refreshInterval = Long
						.parseLong(refreshIntervalString);
			}

			String syncModeString = (String) config.get("syncmode");
			if (StringUtils.isNotBlank(syncModeString)) {
				try {
					DropboxSynchronizerImpl.syncMode = DropboxSyncMode.valueOf(syncModeString.toUpperCase());
				} catch (IllegalArgumentException iae) {
					throw new ConfigurationException(
						"dropbox:syncmode", "Unknown SyncMode '" + syncModeString
						+ "'. Valid SyncModes are 'DROPBOX_TO_LOCAL', 'LOCAL_TO_DROPBOX' and 'BIDIRECTIONAL'.");
				}
			}
			
			String filterString = (String) config.get("filter");
			if (StringUtils.isNotBlank(filterString)) {
				String[] newFilterElements = filterString.split(",");
				filterElements.addAll(Arrays.asList(newFilterElements));
			}

			if (StringUtils.isBlank(DropboxSynchronizerImpl.appKey) || StringUtils.isBlank(DropboxSynchronizerImpl.appSecret)) {
				throw new ConfigurationException("dropbox:appkey",
					"The parameters 'appkey' or 'appsecret' are missing! Please refer to your 'openhab.cfg'");
			}

			isProperlyConfigured = true;
			start();
		}
	}
	

}
