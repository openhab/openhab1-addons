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
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.IllegalClassException;
import org.openhab.binding.plugwise.PlugwiseBindingProvider;
import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.binding.plugwise.internal.PlugwiseGenericBindingProvider.PlugwiseBindingConfigElement;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main binding class
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PlugwiseBinding extends AbstractActiveBinding<PlugwiseBindingProvider> implements ManagedService  {

	private static final Logger logger = LoggerFactory.getLogger(PlugwiseBinding.class);
	private static final Pattern EXTRACT_PLUGWISE_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(mac|port)$");
	
	/** the refresh interval which is used to check for changes in the binding configurations */
	private static long refreshInterval = 5000;

	private Stick stick;

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {

		if (config != null) {

			// First of all make sure the Stick gets set up
			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {

				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_PLUGWISE_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					logger.error("given plugwise-config-key '"
							+ key
							+ "' does not follow the expected pattern '<PlugwiseId>.<mac|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String plugwiseID = matcher.group(1);

				if(plugwiseID.equals("stick")) {
					if (stick == null) {

						String configKey = matcher.group(2);
						String value = (String) config.get(key);

						if ("port".equals(configKey)) {
							stick = new Stick(value,this);
							logger.info("Plugwise added Stick connected to serial port {}",value);
						}
						else {
							throw new ConfigurationException(configKey,
									"the given configKey '" + configKey + "' is unknown");
						}
					}
				}


			}

			if(stick != null) {
				// re-run through the configuration and setup the remaining devices
				keys = config.keys();
				while (keys.hasMoreElements()) {

					String key = (String) keys.nextElement();

					// the config-key enumeration contains additional keys that we
					// don't want to process here ...
					if ("service.pid".equals(key)) {
						continue;
					}

					Matcher matcher = EXTRACT_PLUGWISE_CONFIG_PATTERN.matcher(key);
					if (!matcher.matches()) {
						logger.error("given plugwise-config-key '"
								+ key
								+ "' does not follow the expected pattern '<PlugwiseId>.<mac|port>'");
						continue;
					}

					matcher.reset();
					matcher.find();

					String plugwiseID = matcher.group(1);

					PlugwiseDevice device = stick.getDeviceByName(plugwiseID);
					if (device == null && !plugwiseID.equals("stick")) {

						String configKey = matcher.group(2);
						String value = (String) config.get(key);
						String MAC = null;

						if ("mac".equals(configKey)) {
							MAC = value;
						}
						else {
							throw new ConfigurationException(configKey,
									"the given configKey '" + configKey + "' is unknown");
						}

						if(!MAC.equals("")) {
							if(plugwiseID.equals("circleplus")) {
								if(stick.getDeviceByMAC(MAC)==null) {
									device = new CirclePlus(MAC,stick);
									logger.info("Plugwise added Circle+ with MAC address: {}",MAC);
								}
							} else {
								if(stick.getDeviceByMAC(MAC)==null) {
									device = new Circle(MAC,stick,plugwiseID);
									logger.info("Plugwise added Circle with MAC address: {}",MAC);
								}
							}
							stick.plugwiseDeviceCache.add(device);	
						}
					}
				}	

				setProperlyConfigured(true);

			} else {
				logger.error("Plugwise needs at least one Stick in order to operate");
			}	
		}	
	}

	public void activate() {
		// Nothing to do here. We start the binding when the first item bindigconfig is processed
	}

	public void deactivate() {

		if(stick!=null) {

			//unschedule all the quartz jobs

			Scheduler sched = null;
			try {
				sched = StdSchedulerFactory.getDefaultScheduler();
			} catch (SchedulerException e) {
				logger.error("An exception occurred while getting a reference to the Quarz Scheduler");
			}

			for (PlugwiseBindingProvider provider : providers) {
				try {
					for(JobKey jobKey : sched.getJobKeys(jobGroupEquals("Plugwise-"+provider.toString()))) {
						sched.deleteJob(jobKey);
					}
				} catch (SchedulerException e) {
					logger.error("An exception occurred while deleting the Plugwise Quartz jobs ({})",e.getMessage());
				}
			}

			stick.close();

		}


	}

	@Override
	protected void internalReceiveCommand(String itemName,
			Command command) {

		PlugwiseBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		String commandAsString = command.toString();

		if(command != null){

			List<Command> commands = new ArrayList<Command>();

			// check if the command is valid for this item by checking if a pw ID exists
			String checkID = provider.getPlugwiseID(itemName,command);

			if(checkID != null) {
				commands.add(command);
			} else {
				// ooops - command is not defined, but maybe we have something of the same Type (e.g Decimal, String types)
				//commands = provider.getCommandsByType(itemName, command.getClass());
				commands = provider.getAllCommands(itemName);
			}

			for(Command someCommand : commands) {

				String plugwiseID = provider.getPlugwiseID(itemName,someCommand);
				PlugwiseCommandType plugwiseCommandType = provider.getPlugwiseCommandType(itemName,someCommand);

				if(plugwiseID != null) {
					if(plugwiseCommandType != null){
						@SuppressWarnings("unused")
						boolean result = executeCommand(plugwiseID,plugwiseCommandType,commandAsString);

						// Each command is responsible to make sure that a result value for the action is polled from the device
						// which then will be used to do a postUpdate

						// if new commands would be added later on that do not have this possibility, then a kind of 
						// auto-update has to be performed here below

					} else {
						logger.error(
								"wrong command type for binding [Item={}, command={}]",
								itemName, commandAsString);
					}
				}
				else {
					logger.error("{} is an unrecognised command for Item {}",commandAsString,itemName);
				}
			}		
		}
	}

	private boolean executeCommand(String plugwiseID,
			PlugwiseCommandType plugwiseCommandType, String commandAsString) {

		boolean result = false;

		if(plugwiseID != null) {
			PlugwiseDevice plug = stick.getDeviceByMAC(plugwiseID);
			if(plug != null) {
				switch (plugwiseCommandType) {
				case CURRENTSTATE:
					if(plug instanceof Circle || plug instanceof CirclePlus) {
						result = ((Circle)plug).setPowerState(commandAsString);
						((Circle)plug).updateInformation();
					}
				default:
					break;
				};

			} else {
				logger.error(
						"Plugwise device is not defined for device with ID {}",plugwiseID);
			}
		}
		return result;
	}

	/**
	 * Method to post updates to the OH runtime. 
	 * 
	 * 
	 * @param MAC of the Plugwise device concerned
	 * @param ctype is the Plugwise Command type 
	 * @param value is the value (to be converted) to post
	 */
	public void postUpdate(String MAC, PlugwiseCommandType ctype, Object value) {

		if(MAC != null && ctype != null && value != null) {

			for(PlugwiseBindingProvider provider : providers) {

				Set<String> qualifiedItems = provider.getItemNames(MAC, ctype);
				// Make sure we also capture those devices that were pre-defined with a friendly name in a .cfg or alike
				Set<String> qualifiedItemsFriendly = provider.getItemNames(stick.getDevice(MAC).getFriendlyName(), ctype);
				qualifiedItems.addAll(qualifiedItemsFriendly);

				Type type = null;
				try {
					type = createStateForType(ctype,value.toString());
				} catch (BindingConfigParseException e) {
					logger.error("Error parsing a value {} to a state variable of type {}",value.toString(),ctype.getTypeClass().toString());
				}

				for(String anItem : qualifiedItems) {
					if (type instanceof State) {
						eventPublisher.postUpdate(anItem, (State) type);
					} else {
						throw new IllegalClassException("Cannot process update of type " + type.toString());
					}
				}
			}
		}		
	}


	@SuppressWarnings("unchecked")
	private Type createStateForType(PlugwiseCommandType ctype, String value) throws BindingConfigParseException {

		Class<? extends Type> typeClass  = ctype.getTypeClass();
		List<Class<? extends State>> stateTypeList = new ArrayList<Class<? extends State>>();

		stateTypeList.add((Class<? extends State>) typeClass);

		State state = TypeParser.parseState(stateTypeList, value);

		return state;	
	}


	/**
	 * Find the first matching {@link PlugwiseBindingProvider}
	 * according to <code>itemName</code>
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	protected PlugwiseBindingProvider findFirstMatchingBindingProvider(String itemName) {
		PlugwiseBindingProvider firstMatchingProvider = null;
		for (PlugwiseBindingProvider provider : providers) {
			List<String> plugwiseIDs = provider.getPlugwiseID(itemName);
			if (plugwiseIDs != null && plugwiseIDs.size() > 0) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}

	@Override
	protected void execute() {
		if(isProperlyConfigured()) {

			Scheduler sched = null;
			try {
				sched =  StdSchedulerFactory.getDefaultScheduler();
			} catch (SchedulerException e) {
				logger.error("An exception occurred while getting a reference to the Quartz Scheduler");
			}

			for (PlugwiseBindingProvider provider : providers) {

				List<PlugwiseBindingConfigElement> compiledList = ((PlugwiseBindingProvider)provider).getIntervalList();

				Iterator<PlugwiseBindingConfigElement> pbcIterator = compiledList.iterator();
				while(pbcIterator.hasNext()) {
					PlugwiseBindingConfigElement anElement = pbcIterator.next();
					PlugwiseCommandType type = anElement.getCommandType();

					// check if the device already exists (via cfg definition of Role Call)

					if(stick.getDevice(anElement.getId())==null) {
						logger.info("The Plugwise device with id {} is not yet defined",anElement.getId());

						// check if the config string really contains a MAC address
						Pattern MAC_PATTERN = Pattern.compile("(\\w{16})");
						Matcher matcher = MAC_PATTERN.matcher(anElement.getId());
						if(matcher.matches()){
							CirclePlus cp = (CirclePlus) stick.getDeviceByName("circleplus");
							if(cp!=null) {
								if(!cp.getMAC().equals(anElement.getId())) {
									//a circleplus has been added/detected and it is not what is in the binding config
									PlugwiseDevice device = new Circle(anElement.getId(),stick,anElement.getId());
									stick.plugwiseDeviceCache.add(device);	
									logger.info("Plugwise added Circle with MAC address: {}",anElement.getId());
								}
							} else {
								logger.warn("Plugwise can not guess the device that should be added. Consider defining it in the openHAB configuration file");
							}
						} else {
							logger.warn("Plugwise can not add a valid device without a proper MAC address. {} can not be used",anElement.getId());
						}
					}

					if(stick.getDevice(anElement.getId())!=null) {

						boolean jobExists = false;

						// enumerate each job group
						try {
							for(String group: sched.getJobGroupNames()) {
								// enumerate each job in group
								for(JobKey jobKey : sched.getJobKeys(jobGroupEquals(group))) {
									if(jobKey.getName().equals(anElement.getId()+"-"+type.getJobClass().toString())) {
										jobExists = true;
										break;
									}
								}
							}
						} catch (SchedulerException e1) {
							logger.error("An exception occurred while quering the Quartz Scheduler ({})",e1.getMessage());
						}

						if(!jobExists) {
							// set up the Quartz jobs
							JobDataMap map = new JobDataMap();
							map.put("Stick", stick);
							map.put("MAC",stick.getDevice(anElement.getId()).MAC);

							JobDetail job = newJob(type.getJobClass())
									.withIdentity(anElement.getId()+"-"+type.getJobClass().toString(), "Plugwise-"+provider.toString())
									.usingJobData(map)
									.build();

							Trigger trigger = newTrigger()
									.withIdentity(anElement.getId()+"-"+type.getJobClass().toString(), "Plugwise-"+provider.toString())
									.startNow()
									.withSchedule(simpleSchedule()
											.repeatForever()
											.withIntervalInSeconds(anElement.getInterval()))            
											.build();

							try {
								sched.scheduleJob(job, trigger);
							} catch (SchedulerException e) {
								logger.error("An exception occurred while scheduling a Quartz Job");
							}
						}
					} else {
						logger.error("Error scheduling a Quartz Job for a non-defined Plugwise device");
					}
				}		
			} 
		}
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Plugwise Refresh Service";
	}
}
