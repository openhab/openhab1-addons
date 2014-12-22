/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav.internal;

import static org.quartz.TriggerBuilder.newTrigger;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Dictionary;

import org.openhab.binding.caldav.CalDavBindingProvider;

import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.EventNotifier;
import org.openhab.io.geocoding.GeocodingException;
import org.openhab.io.geocoding.GeocodingProvider;
import org.openhab.library.location.types.LocationType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Robert Delbrück
 * @since 1.6.0
 */
public class CalDavBinding extends AbstractBinding<CalDavBindingProvider> implements ManagedService, EventNotifier {

	private static final Logger logger = 
		LoggerFactory.getLogger(CalDavBinding.class);
	
	private long minimumRefresh = 1000;
	
	private LocationType home;
	private float distanceTimeFactor;
	
	private CalDavLoader calDavLoader;
	
	private GeocodingProvider geocodingProvider;
	
	private Scheduler scheduler;
	
	public CalDavBinding() {
	}
	
	public void setCalDavLoader(CalDavLoader calDavLoader) {
		this.calDavLoader = calDavLoader;
		this.calDavLoader.addListener(this);
	}
	
	public void unsetCalDavLoader() {
		this.calDavLoader.removeListener(this);
		this.calDavLoader = null;
	}
	
	public void setGeocodingProvider(GeocodingProvider geocodingProvider) {
		this.geocodingProvider = geocodingProvider;
	}
	
	public void unsetGeocodingProvider() {
		this.geocodingProvider = null;
	}
	
	public void activate() {
		try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            super.activate();
        }
        catch (SchedulerException se) {
            logger.error("initializing scheduler throws exception", se);
        }
		
		CalDavJob.setEventPublisher(eventPublisher);
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}


	@Override
	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		if (properties == null) {
			logger.warn("no configuration found");
		} else {
			float longitude = Float.parseFloat(properties.get("longitude") + "");
			float latitude = Float.parseFloat(properties.get("latitude") + "");
			this.home = new LocationType(new BigDecimal(longitude), new BigDecimal(latitude));
			
			this.distanceTimeFactor = Float.parseFloat(properties.get("distTimeFactor") + "");
		}
	}

	@Override
	public void eventAdded(CalDavEvent event) {
		CalDavBindingProvider bindingProvider = null;
		for (CalDavBindingProvider bindingProvider_ : this.providers) {
			bindingProvider = bindingProvider_;
		}
		if (bindingProvider == null) {
			logger.error("no binding provider found");
			return;
		}
		
		String item = null;
		for (String item_ : bindingProvider.getItemNames()) {
			CalDavConfig config = bindingProvider.getConfig(item_);
			if (event.getCalendarId().equals(config.getId())) {
				item = item_;
			}
		}
		if (item == null) {
			logger.error("no item found for event '" + event.getId() + "'");
			return;
		}
		
		double timeOffset = 0;
		try {
			LocationType locationType = geocodingProvider.getLocation(event.getLocation());
			double distance = geocodingProvider.distance(this.home, locationType);
			timeOffset = distance * this.distanceTimeFactor;
		} catch (GeocodingException e1) {
			// no problem, then without location
		}
		
		Calendar start = Calendar.getInstance();
		start.setTime(event.getStart());
		if (timeOffset != 0) {
			start.add(Calendar.MINUTE, (int) -timeOffset);
		}
		
		Calendar end = Calendar.getInstance();
		end.setTime(event.getEnd());
		if (timeOffset != 0) {
			end.add(Calendar.MINUTE, (int) timeOffset);
		}
		
		JobDetail jobStart = JobBuilder.newJob(CalDavJob.class)
	        	.usingJobData(CalDavJob.JOB_DATA_CONTENT_KEY, "data")
	            .withIdentity(event.getId() + "_start")
	            .usingJobData("item", item)
	            .usingJobData("state", true)
	            .build();
		
		Trigger triggerStart = newTrigger()
	            .forJob(jobStart)
	            .withIdentity(event.getId() + "_start")
	            .startAt(start.getTime())
	            .build();
		
		JobDetail jobEnd = JobBuilder.newJob(CalDavJob.class)
	        	.usingJobData(CalDavJob.JOB_DATA_CONTENT_KEY, "data")
	            .withIdentity(event.getId() + "_end")
	            .usingJobData("item", item)
	            .usingJobData("state", false)
	            .build();
		
		Trigger triggerEnd = newTrigger()
	            .forJob(jobEnd)
	            .withIdentity(event.getId() + "_end")
	            .startAt(end.getTime())
	            .build();
		
		try {
			this.scheduler.scheduleJob(jobStart, triggerStart);
			this.scheduler.scheduleJob(jobEnd, triggerEnd);
		} catch (SchedulerException e) {
			logger.warn("scheduling Trigger throws an exception.", e);
		}
	}

	@Override
	public void eventRemoved(CalDavEvent event) {
		try {
			this.scheduler.deleteJob(new JobKey(event.getCalendarId() + "_start"));
			this.scheduler.deleteJob(new JobKey(event.getCalendarId() + "_end"));
		} catch (SchedulerException e) {
			logger.warn("deleted trigger throws exception", e);
		}
	}
}
