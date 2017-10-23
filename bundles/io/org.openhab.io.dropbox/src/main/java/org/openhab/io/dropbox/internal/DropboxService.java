/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.dropbox.internal;

import static org.apache.commons.lang.StringUtils.*;
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
import java.util.Objects;
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
import org.quartz.SchedulerContext;
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
 * The {@link DropboxService} is responsible for managing the
 * {@link DropboxSynchronizer}.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Carman - split from original DropboxSynchronizer
 * @since 1.11.0
 */
public class DropboxService implements ManagedService {

    private Logger logger;

    private final String AUTH_FILE_NAME = File.separator + "authfile.dbx";
    private static final String DROPBOX_SCHEDULER_GROUP = "Dropbox";

    //// Authentication: user must configure either the personalAccessToken or
    //// BOTH the AppKey AND the AppSecret; if all 3 are defined, then the
    //// personalAccessToken will be used and the others ignored.

    /**
     * A user's personal access token retrieved from configuration only;
     * null by default
     */
    private String personalAccessToken;

    /// AppKey and AppSecret:
    /// These are legacy attributes from when there was an official openHAB Dropbox app.
    /// These should not generally be used, as they are more difficult to set up than
    /// the newer method via personalAccessToken.
    ///
    /// Note: When using these two attributes, the user must watch the logfile during
    /// the startup of OpenHAB to get the URL to open in a Browser to allow the plugin
    /// to connect to a predefined App-Folder (see
    /// <a href="https://www.dropbox.com/developers/apps">Dropbox Documentation</a>
    /// for more information).

    /** The configured AppKey (optional; see notes above) */
    private String appKey;

    /** The configured AppSecret (optional; see notes above) */
    private String appSecret;

    /** The base directory for the .dbx files */
    public final String DBX_FOLDER = DropboxUtils.getUserDbxDataFolder();

    /** The configured synchronization mode (defaults to LOCAL_TO_DROPBOX) */
    private DropboxSyncMode syncMode = DropboxSyncMode.LOCAL_TO_DROPBOX;

    /**
     * The upload interval as a Cron Expression (optional; defaults to
     * '0 0 2 * * ?', which means once a day at 2 a.m.)
     */
    private String uploadInterval = "0 0 2 * * ?";

    /**
     * The download interval as a Cron Expression (optional; defaults to
     * '0 0/5 * * * ?', which means every 5 minutes)
     */
    private String downloadInterval = "0 0/5 * * * ?";

    private boolean isProperlyConfigured;
    private DbxAppInfo appInfo;

    private DbxRequestConfig requestConfig = new DbxRequestConfig("openHAB/1.0", Locale.getDefault().toString());

    private DropboxSynchronizer instance;

    public DropboxService() {
        logger = LoggerFactory.getLogger(DropboxService.class);
    }

    public DropboxSynchronizer getDropboxSynchronizer() {
        if (instance == null) {
            instance = new DropboxSynchronizer();
        }

        return instance;
    }

    public void activate() {
    }

    public void deactivate() {
        logger.debug("About to shut down Dropbox Synchronizer ...");

        cancelAllJobs();
        isProperlyConfigured = false;

        instance = null;

        logger.debug("Shutdown completed.");
    }

    private void activateSynchronizer() {
        if (isAuthenticated()) {
            startSynchronizationJobs();
        } else {
            try {
                startAuthentication();
            } catch (DbxException e) {
                logger.warn("Couldn't start authentication process: {}", e.getMessage());
            }
        }
    }

    /**
     * Starts the OAuth authorization process with Dropbox. This is a
     * multi-step process which is described in the Wiki.
     * 
     * @throws DbxException if there are technical or application level errors
     *             in the Dropbox communication
     * 
     * @see <a href="https://github.com/openhab/openhab/wiki/Dropbox-IO">openHAB Dropbox IO Wiki</a>
     */
    public void startAuthentication() throws DbxException {
        if (personalAccessToken == null) {
            DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(requestConfig, appInfo);
            String authUrl = webAuth.start();

            logger.info("#########################################################################################");
            logger.info("# Dropbox Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!");
            logger.info("# 1. Open URL '{}'", authUrl);
            logger.info("# 2. Allow openHAB to access Dropbox");
            logger.info("# 3. Paste the authorisation code here using the command 'finishAuthentication \"<token>\"'");
            logger.info("#########################################################################################");
        } else {
            logger.info("#########################################################################################");
            logger.info("# Starting auth using personal access token");
            logger.info("#########################################################################################");
            writeAccessToken(personalAccessToken);
            startSynchronizationJobs();
        }
    }

    /**
     * Finishes the OAuth authorization process by taking the given {@code token} and creating
     * an accessToken out of it. The authorization process is a multi-step process which is
     * described in the Wiki in detail.
     * 
     * @throws DbxException if there are technical or application level errors
     *             in the Dropbox communication
     * 
     * @see <a href="https://github.com/openhab/openhab/wiki/Dropbox-IO">openHAB Dropbox IO Wiki</a>
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

    private void writeAccessToken(String content) {
        // create folder for .dbx files if it does not exist
        File folder = new File(DBX_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File tokenFile = new File(DBX_FOLDER + AUTH_FILE_NAME);
        DropboxUtils.writeLocalFile(tokenFile, content);
    }

    private String readAccessToken() {
        File tokenFile = new File(DBX_FOLDER + AUTH_FILE_NAME);
        return DropboxUtils.readFile(tokenFile);
    }

    private boolean isAuthenticated() {
        return StringUtils.isNotBlank(readAccessToken());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void updated(Dictionary config) throws ConfigurationException {
        if (config == null) {
            logger.debug("Updated() was called with a null config!");
            return;
        }

        DropboxSynchronizer synchronizer = getDropboxSynchronizer();

        String appKeyString = Objects.toString(config.get("appkey"), null);
        if (isNotBlank(appKeyString)) {
            appKey = appKeyString;
        }

        String appSecretString = Objects.toString(config.get("appsecret"), null);
        if (isNotBlank(appSecretString)) {
            appSecret = appSecretString;
        }

        String pat = Objects.toString(config.get("personalAccessToken"), null);
        if (isNotBlank(pat)) {
            personalAccessToken = pat;
        }

        if (logger.isDebugEnabled()) {
            StringBuffer message = new StringBuffer();
            message.append("Authentication parameters to be used:\r\n");
            if (isNotBlank(personalAccessToken)) {
                message.append("     Personal access token = " + personalAccessToken + "\r\n");
            } else {
                message.append("     appkey = " + appKey + "\r\n");
                message.append("  appsecret = " + appSecret + "\r\n");
            }
            logger.debug(message.toString());
        }

        if (isBlank(personalAccessToken) && (isBlank(appKey) || isBlank(appSecret))) {
            throw new ConfigurationException("dropbox:authentication",
                    "The Dropbox authentication parameters are incorrect!  "
                            + "The parameter 'personalAccesstoken' must be set, or both of"
                            + " the parameters 'appkey' and 'appsecret' must be set. Please"
                            + " check your configuration.");
        } else if (isNotBlank(appKey) && isNotBlank(appSecret)) {
            appInfo = new DbxAppInfo(appKey, appSecret);
        }

        String fakeModeString = Objects.toString(config.get("fakemode"), null);
        if (isNotBlank(fakeModeString)) {
            synchronizer.setFakeMode(BooleanUtils.toBoolean(fakeModeString));
        }

        String contentDirString = Objects.toString(config.get("contentdir"), null);
        synchronizer.setContentDir(contentDirString);

        String uploadIntervalString = Objects.toString(config.get("uploadInterval"), null);
        if (isNotBlank(uploadIntervalString)) {
            uploadInterval = uploadIntervalString;
        }

        String downloadIntervalString = Objects.toString(config.get("downloadInterval"), null);
        if (isNotBlank(downloadIntervalString)) {
            downloadInterval = downloadIntervalString;
        }

        String syncModeString = Objects.toString(config.get("syncmode"), null);
        if (isNotBlank(syncModeString)) {
            try {
                syncMode = DropboxSyncMode.valueOf(syncModeString.toUpperCase());
            } catch (IllegalArgumentException iae) {
                throw new ConfigurationException("dropbox:syncmode", "Unknown SyncMode '" + syncModeString
                        + "'. Valid SyncModes are 'DROPBOX_TO_LOCAL', 'LOCAL_TO_DROPBOX' and 'BIDIRECTIONAL'.");
            }
        }

        String uploadFilterString = Objects.toString(config.get("uploadfilter"), null);
        if (isNotBlank(uploadFilterString)) {
            String[] newFilterElements = uploadFilterString.split(",");
            synchronizer.setUploadFilterElements(Arrays.asList(newFilterElements));
        }

        String downloadFilterString = Objects.toString(config.get("downloadfilter"), null);
        if (isNotBlank(downloadFilterString)) {
            String[] newFilterElements = downloadFilterString.split(",");
            synchronizer.setDownloadFilterElements(Arrays.asList(newFilterElements));
        }

        // we got this far, so we define this synchronizer as properly configured ...
        isProperlyConfigured = true;
        logger.debug("Dropbox I/O is properly configured. Activating synchronizer.");
        activateSynchronizer();
    }

    // ****************************************************************************
    // Synchronisation Jobs
    // ****************************************************************************

    private void startSynchronizationJobs() {
        if (isProperlyConfigured) {
            cancelAllJobs();
            if (isAuthenticated()) {
                logger.debug("Authenticated. Scheduling jobs.");
                scheduleJobs();
            } else {
                logger.debug("Dropbox bundle isn't authorized properly, so the synchronization jobs "
                        + "won't be started! Please re-initiate the authorization process by restarting the "
                        + "Dropbox bundle through the OSGi console.");
            }
        }
    }

    /**
     * Schedules the quartz synchronization according to the synchronization mode
     */
    private void scheduleJobs() {
        switch (syncMode) {
            case DROPBOX_TO_LOCAL:
                logger.debug("Scheduling DROPBOX_TO_LOCAL download interval: {}", downloadInterval);
                schedule(downloadInterval, false);
                break;
            case LOCAL_TO_DROPBOX:
                logger.debug("Scheduling LOCAL_TO_DROPBOX upload interval: {}", uploadInterval);
                schedule(uploadInterval, true);
                break;
            case BIDIRECTIONAL:
                logger.debug("Scheduling BIDIRECTIONAL download interval: {}, upload interval: {}", downloadInterval,
                        uploadInterval);
                schedule(downloadInterval, false);
                schedule(uploadInterval, true);
                break;
            default:
                throw new IllegalArgumentException("Unknown SyncMode '" + syncMode + "'");
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
            sched.getContext().put("synchronizer", getDropboxSynchronizer());
            sched.getContext().put("accessToken", readAccessToken());
            sched.getContext().put("requestConfig", requestConfig);

            JobDetail job = newJob(SynchronizationJob.class).withIdentity(direction, DROPBOX_SCHEDULER_GROUP).build();

            CronTrigger trigger = newTrigger().withIdentity(direction, DROPBOX_SCHEDULER_GROUP)
                    .withSchedule(CronScheduleBuilder.cronSchedule(interval)).build();

            logger.debug("Scheduled synchronization job (direction={}) with cron expression '{}'", direction, interval);
            sched.scheduleJob(job, trigger);
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
                logger.debug("Found {} synchronization jobs to delete from DefaultScheduler (keys={})", jobKeys.size(),
                        jobKeys);
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

        private final JobKey UPLOAD_JOB_KEY = new JobKey("Upload", DROPBOX_SCHEDULER_GROUP);
        private static Logger logger = LoggerFactory.getLogger(DropboxService.SynchronizationJob.class);
        private DropboxSynchronizer synchronizer = null;
        private String accessToken = null;
        private DbxRequestConfig requestConfig = null;
        private DbxClient client = null;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            if (synchronizer == null) {
                try {
                    SchedulerContext schedulerContext = context.getScheduler().getContext();
                    synchronizer = (DropboxSynchronizer) schedulerContext.get("synchronizer");
                    accessToken = schedulerContext.getString("accessToken");
                    requestConfig = (DbxRequestConfig) schedulerContext.get("requestConfig");
                    client = (DbxClient) schedulerContext.get("dbxClient");
                    if (client == null) {
                        client = getClient(synchronizer);
                        schedulerContext.put("dbxClient", client);
                    }
                } catch (SchedulerException e) {
                    logger.warn("Failed to get the scheduler context. Unable to execute!", e);
                    return;
                }
            }

            boolean isUpload = UPLOAD_JOB_KEY.compareTo(context.getJobDetail().getKey()) == 0;

            if (synchronizer != null && client != null) {
                try {
                    if (isUpload) {
                        synchronizer.syncLocalToDropbox(client);
                    } else {
                        synchronizer.syncDropboxToLocal(client);
                    }
                } catch (Exception e) {
                    logger.warn("Synchronizing data with Dropbox threw an exception", e);
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
            if (StringUtils.isNotBlank(accessToken)) {
                logger.debug("Creating new DbxClient");
                return new DbxClient(requestConfig, accessToken);
            }
            return null;
        }
    }
}
