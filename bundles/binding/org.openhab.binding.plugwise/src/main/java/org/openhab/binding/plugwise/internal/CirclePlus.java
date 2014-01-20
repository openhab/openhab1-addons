/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.binding.plugwise.protocol.AcknowledgeMessage;
import org.openhab.binding.plugwise.protocol.Message;
import org.openhab.binding.plugwise.protocol.RealTimeClockGetRequestMessage;
import org.openhab.binding.plugwise.protocol.RealTimeClockGetResponseMessage;
import org.openhab.binding.plugwise.protocol.RoleCallRequestMessage;
import org.openhab.binding.plugwise.protocol.RoleCallResponseMessage;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that represents a Plugwise Circle+ device
 * 
 * Circle+ are special Circles. Typically there is one Circle+ in a Plugwise network, and it serves as a master
 * controller in the network, providing Clock data to the other Circles, relay information to the Stick and so forth
 * 
 * Every 24h the Clock of the Circle+ is set identical to the hosts' Clock
 * 
 * The Circle+ also does "RoleCall"s, e.g. polling the Plugwise network in order to make an inventory of all the 
 * availble Circles
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class CirclePlus extends Circle {

	private static final Logger logger = LoggerFactory.getLogger(CirclePlus.class);

	protected DateTime realtimeClock;
	
	public CirclePlus(String mac,Stick stick) {
		super(mac, stick, "circleplus");
		type = DeviceType.CirclePlus;
		
		// set up the Quartz job to set the clock every 24h
		
		Scheduler sched = null;
		try {
			sched = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			logger.error("Error getting a reference to the Quarz Scheduler");
		}

		JobDataMap map = new JobDataMap();
		map.put("CirclePlus", this);
		
		JobDetail job = newJob(SetClockJob.class)
		    .withIdentity(MAC+"-SetCirclePlusClock", "Plugwise")
		    .usingJobData(map)
		    .build();
		
		CronTrigger trigger = newTrigger()
			    .withIdentity(MAC+"-SetCirclePlusClock", "Plugwise")
			    .startNow()
			    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
			    .build();
		
		try {
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			logger.error("Error scheduling a Quartz Job");
		}	
	}

	/**
	 * Role calling is basically asking the Circle+ to return all the devices known to it. Up to 64 devices
	 * are supported in a PW network, and role calling is done by sequentially sendng RoleCallMessages for all
	 * possible IDs in the network (ID = number from 1 to 63)
	 * 
	 * @param id of the device to rolecall
	 */
	public void roleCall(int id) {
		if(id>=0 && id < 64) {
			RoleCallRequestMessage request = new RoleCallRequestMessage(MAC, id);
			stick.sendMessage(request);
		}
	}
	
	public DateTime getRealTimeClock() {
		if(realtimeClock!= null) {
			return realtimeClock;
		} else {
			updateRealTimeClock();
			return null;
		}
	}
	
	public void updateRealTimeClock() {
		RealTimeClockGetRequestMessage message = new RealTimeClockGetRequestMessage(MAC);
		stick.sendMessage(message);
	}
	
	@Override
	public boolean processMessage(Message message) {
		if(message!=null) {
			switch(message.getType()){	
			case DEVICE_ROLECALL_RESPONSE:
				if( ((RoleCallResponseMessage)message).getNodeID() < 63 && !((RoleCallResponseMessage)message).getNodeMAC().equals("FFFFFFFFFFFFFFFF") ) {
					// add e new node
					Circle newCircle1 = (Circle) stick.getDeviceByMAC(((RoleCallResponseMessage)message).getNodeMAC());
					if(	newCircle1 ==null) {
						newCircle1 = new Circle(((RoleCallResponseMessage)message).getNodeMAC(), stick,((RoleCallResponseMessage)message).getNodeMAC());
						stick.plugwiseDeviceCache.add(newCircle1);
						logger.debug("Added a Circle with MAC {} to the cache",newCircle1.getMAC());
					}
					newCircle1.updateInformation();
					newCircle1.calibrate();
					// check if there is any other on the network
					roleCall(((RoleCallResponseMessage)message).getNodeID() + 1);	
				}
				return true;
				
			case REALTIMECLOCK_GET_RESPONSE:
				
				realtimeClock = ((RealTimeClockGetResponseMessage)message).getTime();
				
				DateTimeFormatter rc = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
				postUpdate(MAC,PlugwiseCommandType.REALTIMECLOCK,rc.print(realtimeClock));
				
				return true;
				
			case ACKNOWLEDGEMENT:
				if(((AcknowledgeMessage)message).isExtended()) {
					switch(((AcknowledgeMessage)message).getExtensionCode()) {

					case CLOCKSET:
						logger.debug("Circle+ Clock is set");
						break;
						
					default:
						return stick.processMessage(message);
					}
				}

			default:
				return super.processMessage(message);	
			}
		} else {
			return false;
		}	
	}
	
	public static class SetClockJob implements Job {
		
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			
			// get the reference to the Stick
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			CirclePlus circlePlus = (CirclePlus) dataMap.get("CirclePlus");
			circlePlus.setClock();
		}
	}
}
