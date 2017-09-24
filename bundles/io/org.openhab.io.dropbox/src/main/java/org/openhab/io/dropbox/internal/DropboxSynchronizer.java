/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.dropbox.internal;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxDelta;
import com.dropbox.core.DbxDelta.Entry;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.WithChildren;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;

/**
 * The {@link DropboxSynchronizer} is able to synchronize the contents of Dropbox
 * to the local file system and vice versa. There are three synchronization modes
 * available: local to dropbox (the default mode), dropbox to local, and
 * bidirectional.
 *
 * @author Thomas.Eichstaedt-Engelen - Initial Contribution
 * @author Theo Weiss - add smarthome.userdata support
 * @author Chris Carman - add support for personalAccessToken
 * @since 1.0.0
 */
public class DropboxSynchronizer {

    private Logger logger;

    private final String FIELD_DELIMITER = "@@";
    private final String LINE_DELIMITER = System.getProperty("line.separator");
    private final String DELTA_CURSOR_FILE_NAME = File.separator + "deltacursor.dbx";
    private final String DROPBOX_ENTRIES_FILE_NAME = File.separator + "dropbox-entries.dbx";

    /** The default directory to which to download files from Dropbox */
    private final String DEFAULT_CONTENT_DIR = DropboxUtils.getConfigDirFolder();

    private final List<String> DEFAULT_UPLOAD_FILE_FILTER = Arrays.asList("^([^/]*/){1}[^/]*$", "/configurations.*",
            "/logs/.*", "/etc/.*");
    private final List<String> DEFAULT_DOWNLOAD_FILE_FILTER = Arrays.asList("^([^/]*/){1}[^/]*$", "/configurations.*");

    /**
     * Holds the id of the last synchronisation cursor. This is needed to
     * define the delta to download from Dropbox.
     */
    private String lastCursor = null;
    private String lastHash = null;

    /**
     * The base directory to synchronize with openHAB.
     */
    private String contentDir;

    /** The base directory for the .dbx files */
    public final String DBX_FOLDER = DropboxUtils.getUserDbxDataFolder();

    /**
     * Defines a comma separated list of regular expressions which matches the
     * filenames to upload to Dropbox (optional; defaults to '/configurations.*,
     * /logs/.*, /etc/.*')
     */
    private List<String> uploadFilterElements = DEFAULT_UPLOAD_FILE_FILTER;

    /**
     * Defines a comma separated list of regular expressions which matches the
     * filenames to download from Dropbox (optional; defaults to '/configurations.*')
     */
    private List<String> downloadFilterElements = DEFAULT_DOWNLOAD_FILE_FILTER;

    /**
     * Operates the Synchronizer in fake mode which avoids sending files to or
     * from Dropbox. This is meant as a test mode for the filter settings.
     * (optional; defaults to false)
     */
    private boolean fakeMode;

    protected DropboxSynchronizer() {
        logger = LoggerFactory.getLogger(DropboxSynchronizer.class);
    }

    protected void setFakeMode(boolean value) {
        fakeMode = value;
    }

    protected void setContentDir(String value) {
        if (StringUtils.isBlank(value)) {
            contentDir = DEFAULT_CONTENT_DIR;
        } else {
            contentDir = value;
        }
        logger.debug("contentdir: {}", contentDir);
    }

    protected void setUploadFilterElements(List<String> value) {
        if (value != null) {
            uploadFilterElements = value;
        } else {
            uploadFilterElements = DEFAULT_UPLOAD_FILE_FILTER;
        }
    }

    protected void setDownloadFilterElements(List<String> value) {
        if (value != null) {
            downloadFilterElements = value;
        } else {
            downloadFilterElements = DEFAULT_DOWNLOAD_FILE_FILTER;
        }
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
     *             errors in the Dropbox communication
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
                        logger.trace("skipped file '{}' since it doesn't match the given filter arguments.",
                                entry.lcPath);
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
     * are identified by the files' <code>lastModified</code> attribute. If there
     * are less files locally, the additional files will be deleted from the
     * Dropbox. New files will be uploaded or overwritten if they exist already.
     *
     * @throws DbxException if there are technical or application level
     *             errors in the Dropbox communication
     * @throws IOException
     */
    public void syncLocalToDropbox(DbxClient client) throws DbxException, IOException {
        logger.debug("Started synchronization from local to Dropbox ...");

        Map<String, Long> dropboxEntries = new HashMap<String, Long>();

        WithChildren metadata = client.getMetadataWithChildren("/");
        File dropboxEntryFile = new File(DBX_FOLDER + DROPBOX_ENTRIES_FILE_NAME);
        if (!dropboxEntryFile.exists() || !metadata.hash.equals(lastHash)) {
            collectDropboxEntries(client, dropboxEntries, "/");
            serializeDropboxEntries(dropboxEntryFile, dropboxEntries);
            lastHash = metadata.hash;

            // TODO: TEE: we could think about writing the 'lastHash' to a file?
            // let's see what daily use brings whether this is a necessary feature!
        } else {
            logger.trace("Dropbox entry file '{}' exists; extract content", dropboxEntryFile.getPath());
            dropboxEntries = extractDropboxEntries(dropboxEntryFile);
        }

        Map<String, Long> localEntries = new HashMap<String, Long>();
        collectLocalEntries(localEntries, contentDir);
        logger.debug("There are '{}' local entries that met the upload filters ...", localEntries.size());

        boolean isChanged = false;

        for (java.util.Map.Entry<String, Long> entry : localEntries.entrySet()) {
            if (dropboxEntries.containsKey(entry.getKey())) {
                if (entry.getValue().compareTo(dropboxEntries.get(entry.getKey())) > 0) {
                    logger.trace("Local file '{}' is newer; upload to Dropbox!", entry.getKey());
                    if (!fakeMode) {
                        uploadFile(client, entry.getKey(), true);
                    }
                    isChanged = true;
                }
            } else {
                logger.trace("Local file '{}' doesn't exist in Dropbox; upload to Dropbox!", entry.getKey());
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
                    logger.trace("Skipped file '{}' since it doesn't match the given filter arguments.", path);
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
                logger.debug(
                        "Deleted cache file '{}' since there are changes. It will be recreated on the next synchronization loop.",
                        dropboxEntryFile.getPath());
            }

            // since there are changes we have to update the lastCursor (and
            // the corresponding file) to have the right starting point for the
            // next synchronization loop
            DbxDelta<DbxEntry> delta = client.getDelta(lastCursor);
            writeDeltaCursor(delta.cursor);
        } else {
            logger.debug("No files changed locally. No deltas to upload to Dropbox ...");
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
                    logger.trace("Couldn't parse line '{}'; it does not contain two elements delimited by '{}'", line,
                            FIELD_DELIMITER);
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

                logger.trace("Skipped file '{}' since it doesn't match the given filter arguments.",
                        file.getAbsolutePath());
                return false;
            }
        });

        for (File file : files) {
            String normalizedPath = StringUtils.substringAfter(file.getPath(), contentDir);
            if (file.isDirectory()) {
                collectLocalEntries(localEntries, file.getPath());
            } else {
                // if we are on a Windows filesystem we need to change the separator for dropbox
                if (DropboxUtils.isWindows()) {
                    normalizedPath = normalizedPath.replace('\\', '/');
                }
                localEntries.put(normalizedPath, file.lastModified());
            }
        }
    }

    private void collectDropboxEntries(DbxClient client, Map<String, Long> dropboxEntries, String path)
            throws DbxException {
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
     * TODO: TEE: Currently there is no way to change the attribute
     * 'lastModified' of the files to upload via Dropbox API. See the
     * discussion below for more details.
     *
     * Since this is a missing feature (from my point of view) we should
     * check the improvements of the API development on a regular basis.
     *
     * @see http://forums.dropbox.com/topic.php?id=22347
     */
    private void uploadFile(DbxClient client, String dropboxPath, boolean overwrite) throws DbxException, IOException {
        File file = new File(contentDir + File.separator + dropboxPath);
        FileInputStream inputStream = new FileInputStream(file);
        try {
            DbxWriteMode mode = overwrite ? DbxWriteMode.force() : DbxWriteMode.add();
            DbxEntry.File uploadedFile = client.uploadFile(dropboxPath, mode, file.length(), inputStream);
            logger.debug("Successfully uploaded file '{}'. New revision is '{}'.", uploadedFile, uploadedFile.rev);
        } finally {
            inputStream.close();
        }
    }

    private void writeDeltaCursor(String deltaCursor) {
        if (!deltaCursor.equals(lastCursor)) {
            logger.trace("Delta-Cursor changed (lastCursor '{}', newCursor '{}')", lastCursor, deltaCursor);
            File cursorFile = new File(DBX_FOLDER + DELTA_CURSOR_FILE_NAME);
            DropboxUtils.writeLocalFile(cursorFile, deltaCursor);
            lastCursor = deltaCursor;
        }
    }

    private String readDeltaCursor() {
        File cursorFile = new File(DBX_FOLDER + DELTA_CURSOR_FILE_NAME);
        return DropboxUtils.readFile(cursorFile);
    }

    private void deleteLocalFile(String fqPath) {
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
            logger.trace("Local item '{}' wasn't deleted because it is a directory.");
        }
    }
}
