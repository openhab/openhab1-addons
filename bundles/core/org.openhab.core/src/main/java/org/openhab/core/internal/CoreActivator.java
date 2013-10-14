/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;


/**
 * @author Kai Kreuzer
 * @author Thomas.Eichstaedt-Engelen
 * 
 * @since 0.1.0
 */
public class CoreActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(CoreActivator.class);
	
	
	private static final String STATIC_CONTENT_DIR = "webapps" + File.separator + "static";

	private static final String UUID_FILE_NAME = "uuid";

	private static final String VERSION_FILE_NAME = "version";

	private static final String VERSION_URL = "http://version.openhab.org/";
	

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		String uuidString = createUUIDFile();
		
		String versionString = context.getBundle().getVersion().toString();
		// if the version string contains a qualifier, remove it!
		if (StringUtils.countMatches(versionString, ".") == 3) {
			versionString = StringUtils.substringBeforeLast(versionString, ".");
		}
		checkVersion(uuidString, versionString);
		createVersionFile(versionString);
		
		logger.info("openHAB runtime has been started (v{}).", versionString);

		java.util.logging.Logger rootLogger = java.util.logging.LogManager.getLogManager().getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		for (Handler handler : handlers) {
			rootLogger.removeHandler(handler);
		}
		
		SLF4JBridgeHandler.install();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		logger.info("openHAB runtime has been terminated.");
	}
	
	/**
	 * Creates a unified unique id and writes it to the <code>webapps/static</code>
	 * directory. An existing <code>uuid</code> file won't be overwritten.
	 */
	private String createUUIDFile() {
		File file = new File(STATIC_CONTENT_DIR + File.separator + UUID_FILE_NAME);
		String uuidString = "";
		
		if (!file.exists()) {
			uuidString = UUID.randomUUID().toString();
			writeFile(file, uuidString);
		} else {
			uuidString = readFirstLine(file);
			logger.debug("UUID file already exists at '{}' with content '{}'", file.getAbsolutePath(), uuidString);
		}
		
		return uuidString;
	}
	
	/**
	 * Creates a file with given <code>version</code>. The file will be overwritten
	 * each time openHAB has been started.
	 * 
	 * @param version the version number to write to the version file
	 */
	private void createVersionFile(String version) {
		File file = new File(STATIC_CONTENT_DIR + File.separator + VERSION_FILE_NAME);
		writeFile(file, version);
	}
	
	private void writeFile(File file, String content) {
		// create intermediary directories
		file.getParentFile().mkdirs();
		try {
			IOUtils.write(content, new FileOutputStream(file));
			logger.debug("Created file '{}' with content '{}'", file.getAbsolutePath(), content);
		} catch (FileNotFoundException e) {
			logger.error("Couldn't create file '" + file.getPath() + "'.", e);
		} catch (IOException e) {
			logger.error("Couldn't write to file '" + file.getPath() + "'.", e);
		}
	}

	private String readFirstLine(File file) {
		List<String> lines = null;
		try {
			lines = IOUtils.readLines(new FileInputStream(file));
		} catch (IOException ioe) {
			// no exception handling - we just return the empty String
		}
		return lines != null && lines.size() > 0 ? lines.get(0) : "";
	}
	
	/**
	 * Checks the current version of openHAB and logs the result.
	 * 
	 * @param uuidString the uuid this openHAB instance
	 * @param versionString the Bundle version without qualifier
	 */
	private void checkVersion(String uuidString, String versionString) {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(VERSION_URL + "?uuid=" + uuidString + "&" + "version=" + versionString);
		method.getParams().setSoTimeout(3000);
		method.getParams().setParameter(
			HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler(3, false));

		try {
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				logger.debug("Executing url failed: " + method.getStatusLine());
			}

			String versionFromWeb = StringUtils.trimToEmpty(
				IOUtils.toString(method.getResponseBodyAsStream()));
			if (versionFromWeb.matches("\\d\\.\\d\\.\\d")) {
				if (Collator.getInstance().compare(versionFromWeb, versionString) > 0) {
					logger.info("A newer version of openHAB is available 'v{}'. Please check http://www.openhab.org for further information.", versionFromWeb);
				} else if (Collator.getInstance().compare(versionFromWeb, versionString) < 0) {
					logger.debug("You are running 'v{}' a potentially unstable version of openHAB. The current stable version is 'v{}'.", versionString, versionFromWeb);
				}
 			} else {
 				logger.debug("Received version number from '{}' which doesn't match the required format '#.#.#' ({})", VERSION_URL, versionFromWeb);
 			}
		}
		catch (HttpException he) {
			logger.debug("Fatal protocol violation: {}", he.getMessage());
		}
		catch (IOException ioe) {
			logger.debug("Fatal transport error: {}", ioe.getMessage());
		}
		finally {
			method.releaseConnection();
		}
	}
	

}
