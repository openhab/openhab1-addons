/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ntp.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.openhab.binding.ntp.NtpBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The NTP Refresh Service polls the configured timeserver with a configurable 
 * interval and posts a new event of type ({@link DateTimeType} to the event bus.
 * The interval is 15 minutes by default and can be changed via openhab.cfg. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.8.0
 */
public class NtpBinding extends AbstractActiveBinding<NtpBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(NtpBinding.class);
	
	/** timeout for requests to the NTP server */
	private static final int NTP_TIMEOUT = 5000;

	// List of time servers: http://tf.nist.gov/service/time-servers.html
	protected String hostname = "ptbtime1.ptb.de";
	
	/** Default refresh interval (currently 15 minutes) */
	private long refreshInterval = 900000L;
	
	/** for logging purposes */
	private final static DateFormat SDF = 
		SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.FULL, SimpleDateFormat.FULL);
	
	
	@Override
	protected String getName() {
		return "NTP Refresh Service";
	}
	
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		
		if (!bindingsExist()) {
			logger.debug("There is no existing NTP binding configuration => refresh cycle aborted!");
			return;
		}
		
		long networkTimeInMillis = getTime(hostname);
		
		logger.debug("Got time from {}: {}", hostname, SDF.format(new Date(networkTimeInMillis)));
				
		for (NtpBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				
				TimeZone timeZone = provider.getTimeZone(itemName);				
				Locale locale = provider.getLocale(itemName);
				
				Calendar calendar = Calendar.getInstance(timeZone, locale);
				calendar.setTimeInMillis(networkTimeInMillis);

				eventPublisher.postUpdate(itemName, new DateTimeType(calendar));
			}
		}
		
	}
	
	/**
	 * Queries the given timeserver <code>hostname</code> and returns the time
	 * in milliseconds.
	 * 
	 * @param hostname the timeserver to query
	 * @return the time in milliseconds or the current time of the system if an
	 * error occurs.
	 */
	protected static long getTime(String hostname) {
		
		try {
			NTPUDPClient timeClient = new NTPUDPClient();
			timeClient.setDefaultTimeout(NTP_TIMEOUT);
			InetAddress inetAddress = InetAddress.getByName(hostname);
			TimeInfo timeInfo = timeClient.getTime(inetAddress);
			
			return timeInfo.getReturnTime();
		} 
		catch (UnknownHostException uhe) {
			logger.warn("the given hostname '{}' of the timeserver is unknown -> returning current sytem time instead", hostname);
		}
		catch (IOException ioe) {
			logger.warn("couldn't establish network connection [host '{}'] -> returning current sytem time instead", hostname);
		}
		
		return System.currentTimeMillis();
	}
		
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		
		if (config != null) {
			String hostnameString = (String) config.get("hostname");
			if (StringUtils.isNotBlank(hostnameString)) {
				hostname = hostnameString;
			}
			
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			setProperlyConfigured(true);
		}

	}
	
}
