/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hdanywhere.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openhab.binding.hdanywhere.HDanywhereBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main binding class for the HDanywhere HDMI Matrix. (www.hdanywhere.com)
 * This binding does not use the UDP control or RS232 control channels provided by HDanywhere but
 * instead uses the output from the built-in webserver to steer the matrix. The main advantage is
 * that actual input/output port mappings can be polled, which is not provided by the "standard"
 * integration interfaces. 
 * 
 * The binding is developed for matrixes with firmware version V1.2(20131222)
 *
 * @author Karel Goderis
 * @since 1.4.0
 */
public class HDanywhereBinding extends AbstractActiveBinding<HDanywhereBindingProvider> implements ManagedService  {

	private static final Logger logger = LoggerFactory.getLogger(HDanywhereBinding.class);

	/** the refresh interval which is used to check for changes in the binding configurations */
	private static long refreshInterval = 5000;
	/** the timeout to use for connecting to a given host (defaults to 5000 milliseconds) */
	private static int timeout = 5000;

	private static final Pattern EXTRACT_HDANYWHERE_CONFIG_PATTERN = Pattern.compile("(.*)\\.(.*)\\.(.*)\\.(.*)\\.(ports)$");

	/** structure to track configured matrices */
	private HashMap<String, Integer> portMappingCache = new HashMap<String, Integer>();
	/** structure to store actual input/output states */
	private HashMap<String, Integer> matrixCache = new HashMap<String, Integer>();

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {

		if (config != null) {

			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {

				String key = (String) keys.nextElement();

				Matcher matcher = EXTRACT_HDANYWHERE_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					logger.debug("given hdanywhere-config-key '"
							+ key + "' does not follow the expected pattern '<host_IP_Address>.ports'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String hostIP = matcher.group(1)+"."+matcher.group(2)+"."+matcher.group(3)+"."+matcher.group(4);	
				String configKey = matcher.group(5);
				String value = (String) config.get(key);

				if ("ports".equals(configKey)) {
					matrixCache.put(hostIP, Integer.valueOf(value));
				} else {
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey + "' is unknown");
				}
			}
		}

		setProperlyConfigured(true);

	}	


	public void activate() {
		// Nothing to do here. We start the binding when the first item bindigconfig is processed
	}

	public void deactivate() {
		//unschedule all the quartz jobs

		Scheduler sched = null;
		try {
			sched = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			logger.error("An exception occurred while getting a reference to the Quarz Scheduler");
		}

		for (HDanywhereBindingProvider provider : providers) {
			try {
				for(JobKey jobKey : sched.getJobKeys(jobGroupEquals("HDanywhere-"+provider.toString()))) {
					sched.deleteJob(jobKey);
				}
			} catch (SchedulerException e) {
				logger.error("An exception occurred while deleting the HDanywhere Quartz jobs ({})",e.getMessage());
			}
		}
	}

	@Override
	protected void internalReceiveCommand(String itemName,
			Command command) {

		HDanywhereBindingProvider provider = findFirstMatchingBindingProvider(itemName);

		if (provider == null) {
			logger.trace("doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
			return;
		}

		List<String> hosts = provider.getHosts(itemName);
		int sourcePort = Integer.valueOf(command.toString());

		for(String aHost : hosts) {

			Integer numberOfPorts = matrixCache.get(aHost);
			if(numberOfPorts == null) {
				// we default to the smallest matrix currently sold by HDanywhere
				numberOfPorts = 4;
			}

			if(sourcePort > numberOfPorts) {
				// nice try - we can switch to a port that does not physically exist
				logger.warn("{} goes beyond the physical number of {} ports available on the matrix {}",new Object[]{sourcePort,numberOfPorts,aHost});
			} else {

				List<Integer> ports = provider.getPorts(aHost,itemName);

				String httpMethod =	"GET";
				String url = "http://"+aHost+"/switch.cgi?command=3&data0=";

				for(Integer aPort : ports) {
					url = url + aPort.toString()+"&data1=";
					url = url + command.toString()+"&checksum=";

					int checksum = 3 + aPort + sourcePort;
					url = url + String.valueOf(checksum);

					if (isNotBlank(httpMethod) && isNotBlank(url)) {
						String response = HttpUtil.executeUrl(httpMethod, url, null, null, null, timeout);

						Pattern p = Pattern.compile("The output "+aPort+" select input (.*).");
						Matcher m = p.matcher(response);
						while (m.find()){
							List<Class<? extends State>> stateTypeList = new ArrayList<Class<? extends State>>();
							stateTypeList.add(DecimalType.class);
							State state = TypeParser.parseState(stateTypeList, m.group(1));

							if(!portMappingCache.containsKey(aHost+":"+aPort)) {
								portMappingCache.put(aHost+":"+aPort, Integer.valueOf(m.group(1)));
								eventPublisher.postUpdate(itemName,(State) state);
							} else {
								int cachedValue = portMappingCache.get(aHost+":"+aPort);
								if(cachedValue != Integer.valueOf(m.group(1))) {
									portMappingCache.put(aHost+":"+aPort, Integer.valueOf(m.group(1)));
									eventPublisher.postUpdate(itemName,(State) state);
								}
							}

						}
					}
				}
			}
		}
	}


	/**
	 * Find the first matching {@link HDanywhereBindingProvider}
	 * according to <code>itemName</code>
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	protected HDanywhereBindingProvider findFirstMatchingBindingProvider(String itemName) {
		HDanywhereBindingProvider firstMatchingProvider = null;
		for (HDanywhereBindingProvider provider : providers) {
			List<String> hosts = provider.getHosts(itemName);
			if (hosts != null && hosts.size() > 0) {
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

			for (HDanywhereBindingProvider provider : providers) {

				HashMap<String, Integer> compiledList = ((HDanywhereBindingProvider)provider).getIntervalList();

				if(compiledList != null) {
					Iterator<String> pbcIterator = compiledList.keySet().iterator();
					while(pbcIterator.hasNext()) {
						String aHost = pbcIterator.next();

						boolean jobExists = false;

						// enumerate each job group
						try {
							for(String group: sched.getJobGroupNames()) {
								// enumerate each job in group
								for(JobKey jobKey : sched.getJobKeys(jobGroupEquals(group))) {
									if(jobKey.getName().equals(aHost)) {
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
							map.put("host", aHost);
							map.put("binding", this);

							JobDetail job = newJob(HDanywhereBinding.PollJob.class)
									.withIdentity(aHost, "HDanywhere-"+provider.toString())
									.usingJobData(map)
									.build();

							Trigger trigger = newTrigger()
									.withIdentity(aHost, "HDanywhere-"+provider.toString())
									.startNow()
									.withSchedule(simpleSchedule()
											.repeatForever()
											.withIntervalInSeconds(compiledList.get(aHost)))            
											.build();

							try {
								sched.scheduleJob(job, trigger);
							} catch (SchedulerException e) {
								logger.error("An exception occurred while scheduling a Quartz Job");
							}
						}
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
		return "HDanywhere Refresh Service";
	}

	/**
	 * Quartz Job that does poll the HDanwywhere matrix and parses the html string returned
	 * by the built-in webserver
	 * 
	 */
	public static class PollJob implements Job {

		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			// get the reference to the Stick
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			String host = (String) dataMap.get("host");
			HDanywhereBinding theBinding = (HDanywhereBinding) dataMap.get("binding");

			String httpMethod =	"GET";
			String url = "http://"+host+"/status_show.shtml";

			if (isNotBlank(httpMethod) && isNotBlank(url)) {
				String response = HttpUtil.executeUrl(httpMethod, url, null, null, null, timeout);

				Integer numberOfPorts = theBinding.matrixCache.get(host);
				if(numberOfPorts == null) {
					// we default to the smallest matrix currently sold by HDanywhere
					numberOfPorts = 4;
				}

				if(response != null) {
					for(int i=1;i<=numberOfPorts;i++) {
						Pattern p = Pattern.compile("var out"+i+"var = (.*);");
						Matcher m = p.matcher(response);

						while (m.find()){
							List<Class<? extends State>> stateTypeList = new ArrayList<Class<? extends State>>();
							stateTypeList.add(DecimalType.class);
							State state = TypeParser.parseState(stateTypeList, m.group(1));

							for (HDanywhereBindingProvider provider : theBinding.providers) {
								Collection<String> theItems = provider.getItemNames();

								for(String anItem : theItems) {
									List<Integer> itemPorts = provider.getPorts(host, anItem);

									for(Integer aPort : itemPorts) {
										if(aPort == i) {

											if(!theBinding.portMappingCache.containsKey(host+":"+aPort)) {
												theBinding.portMappingCache.put(host+":"+aPort, Integer.valueOf(m.group(1)));
												theBinding.eventPublisher.postUpdate(anItem,(State) state);
											} else {
												int cachedValue = theBinding.portMappingCache.get(host+":"+aPort);
												if(cachedValue != Integer.valueOf(m.group(1))) {
													theBinding.portMappingCache.put(host+":"+aPort, Integer.valueOf(m.group(1)));
													theBinding.eventPublisher.postUpdate(anItem,(State) state);
												}
											}
										}
									}								
								}
							}
						}
					}
				}
			}
		}
	}
}


