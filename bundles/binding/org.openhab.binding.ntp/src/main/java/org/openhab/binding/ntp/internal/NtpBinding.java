/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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
	
	private boolean isProperlyConfigured = false;

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
	public boolean isProperlyConfigured() {
		return isProperlyConfigured;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void execute() {
		
		if (!bindingsExist()) {
			logger.debug("There is no existing NTP binding configuration -> execution aborted");
			return;
		}
		
		long networkTimeInMillis = getTime(hostname);
		
		logger.info("Got time from {}: {}", hostname, SDF.format(new Date(networkTimeInMillis)));
				
		for (NtpBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				
				TimeZone timeZone = provider.getTimeZone(itemName);				
				Locale locale = provider.getLocale(itemName);
				
				Calendar calendar = Calendar.getInstance(timeZone, locale);
				calendar.setTimeInMillis(networkTimeInMillis);

				eventPublisher.postUpdate(itemName, new DateTimeType(calendar)); break;
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
			
			isProperlyConfigured = true;
			start();
		}

	}
	
	
}
