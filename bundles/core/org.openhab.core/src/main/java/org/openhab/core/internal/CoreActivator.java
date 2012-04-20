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
package org.openhab.core.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Collator;
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
 */
public class CoreActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(CoreActivator.class);
	
	
	private static final String STATIC_CONTENT_DIR = "webapps" + File.separator + "static";
	
	private static final String UUID_REPLACE_PATTERN = "@uuid@";

	private static final String UUID_FILE_NAME = "uuid.html";
	
	private static final String UUID_HTML_TEMPLATE = 
		"<!DOCTYPE html>" +
		"<html>" +
			"<head>" +
				"<meta charset=\"UTF-8\">" +
				"<title>openHAB UUID</title>" +
			"</head>" +
			"<body>" + UUID_REPLACE_PATTERN + "</body>" +
		"</html>";
	
	private static final String VERSION_URL = "http://wiki.openhab.googlecode.com/hg/resources/version";
	

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		createUUID();
		
		String versionString = context.getBundle().getVersion().toString();
		// if the version string contains a qualifier, remove it!
		if (StringUtils.countMatches(versionString, ".") == 3) {
			versionString = StringUtils.substringBeforeLast(versionString, ".");
		}
		checkVersion(versionString);
		
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
	 * directory. An existing <code>uuid.html</code> file won't be overwritten.
	 */
	private void createUUID() {
		File uuidFile = new File(STATIC_CONTENT_DIR + File.separator + UUID_FILE_NAME);
		if (!uuidFile.exists()) {
			// create intermediary directories
			uuidFile.getParentFile().mkdirs();
			String uuidString = UUID.randomUUID().toString();
			String uuidFileContent = UUID_HTML_TEMPLATE.replaceAll(UUID_REPLACE_PATTERN, uuidString);
			try {
				IOUtils.write(uuidFileContent, new FileOutputStream(uuidFile));
				logger.info("Created openHAB UUID '{}' and wrote it to '{}'", uuidString, uuidFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				logger.error("Couldn't create UUID file.", e);
			} catch (IOException e) {
				logger.error("Couldn't write to UUID file.", e);
			}
		} else {
			logger.debug("UUID file already exists at '{}'", uuidFile.getAbsolutePath());
		}
	}
	
	/**
	 * Checks the current version of openHAB and logs the result.
	 * 
	 * @param version the Bundle version without qualifier
	 */
	private void checkVersion(String version) {
		
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(VERSION_URL);
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
				if (Collator.getInstance().compare(versionFromWeb, version) > 1) {
					logger.info("A newer version of openHAB is available 'v{}'. Please check http://www.openhab.org for further information.", versionFromWeb);
				} else if (Collator.getInstance().compare(versionFromWeb, version) < 1) {
					logger.info("You are running a potentially unstable version of openHAB. The current stable version is 'v{}'.", versionFromWeb);
				}
 			} else {
 				logger.debug("Received version number from '{}' which doesn't match the required format '#.#.#' ({})", VERSION_URL, versionFromWeb);
 			}
		}
		catch (HttpException he) {
			logger.debug("Fatal protocol violation: {}", he);
		}
		catch (IOException ioe) {
			logger.debug("Fatal transport error: {}", ioe);
		}
		finally {
			method.releaseConnection();
		}
	}
	

}
