/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oceanic.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.oceanic.OceanicBindingProvider;
import org.openhab.binding.oceanic.OceanicValueSelector;
import org.openhab.binding.oceanic.OceanicValueSelector.ValueSelectorType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.BindingConfigParseException;
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
 * 
 * Binding to support the Oceanic Watersoftener. 
 * 
 * @author Karel Goderis
 * @since 1.5.0
 * 
 */
public class OceanicBinding extends AbstractActiveBinding<OceanicBindingProvider>
implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(OceanicBinding.class);

	/** stores information about serial devices / pump gateways in use  */ 
	private Map<String, SerialDevice> serialDevices = new HashMap<String, SerialDevice>();

	/** stores information about the context of items. The map has this content structure: context -> Set of itemNames */ 
	private Map<String, Set<String>> contextMap = new HashMap<String, Set<String>>();

	/** the refresh interval which is used to check for changes in the binding configurations */
	private static long refreshInterval = 5000;

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;

		for(SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.setEventPublisher(eventPublisher);
		}
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;

		for(SerialDevice serialDevice : serialDevices.values()) {
			serialDevice.setEventPublisher(null);
		}
	}

	public void activate() {
		// Nothing to do here. 
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		OceanicBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		String serialPort = ((OceanicBindingProvider)provider).getSerialPort(itemName);
		OceanicValueSelector valueSelector = OceanicValueSelector.getValueSelector(((OceanicBindingProvider)provider).getValueSelector(itemName),ValueSelectorType.SET);
		SerialDevice serialDevice = serialDevices.get(serialPort);

		if(valueSelector.name().contains("set")) {
			String commandAsString = command.toString();
			switch(valueSelector) {
			case setSV1:
				commandAsString = valueSelector.name()+commandAsString;
			default:
				commandAsString = valueSelector.name();
				break;
			}
			String response = serialDevice.requestResponse(commandAsString);
			if(response.equals("ERR")) {
				logger.error("An error occurred while setting '{}' to {}",valueSelector.toString(),command.toString());
			}
		} else {
			// can not set the value of a read-only "get" variable
			logger.warn("An error occurred while trying to set the read-only variable '{}' to {}",valueSelector.toString(),command.toString());
		}	
	}



	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		setProperlyConfigured(true);
	}

	@Override
	protected void execute() {
		
		//TODO Change binding to AbstractActive type and move code in execute() to 
		//TODO bindingChanged()
		
		if(isProperlyConfigured()) {

			Scheduler sched = null;
			try {
				sched =  StdSchedulerFactory.getDefaultScheduler();
			} catch (SchedulerException e) {
				logger.error("An exception occurred while getting a reference to the Quartz Scheduler");
			}

			//reset the contextMap before rebuilding it
			for(String serialPort : serialDevices.keySet()) {
				Set<String> itemNames = contextMap.get(serialPort);
				if(itemNames != null ) {
					contextMap.clear();					
				}
			}

			for(OceanicBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {

					String serialPort = ((OceanicBindingProvider)provider).getSerialPort(itemName);

					SerialDevice serialDevice = serialDevices.get(serialPort);
					boolean serialDeviceReady = true;
					if (serialDevice == null) {
						serialDevice = new SerialDevice(serialPort);
						serialDevice.setEventPublisher(eventPublisher);
						try {
							serialDevice.initialize();
						} catch (InitializationException e) {
							logger.error("Could not open serial port " + serialPort + ": "
									+ e.getMessage());
							serialDeviceReady = false;

						} catch (Throwable e) {
							logger.error("Could not open serial port " + serialPort + ": "
									+ e.getMessage());
							serialDeviceReady = false;
						}
						serialDevices.put(serialPort, serialDevice);
					}

					Set<String> itemNames = contextMap.get(serialPort);
					if (itemNames == null) {
						itemNames = new HashSet<String>();
						contextMap.put(serialPort, itemNames);
					}
					itemNames.add(itemName);


					if(serialDeviceReady) {
						//set up the polling jobs
						boolean jobExists = false;

						// enumerate each job group
						try {
							for(String group: sched.getJobGroupNames()) {
								// enumerate each job in group
								for(JobKey jobKey : sched.getJobKeys(jobGroupEquals(group))) {
									if(jobKey.getName().equals(itemName+"-"+provider.getValueSelector(itemName).toString())) {
										jobExists = true;
										break;
									}
								}
							}
						} catch (SchedulerException e1) {
							logger.error("An exception occurred while quering the Quartz Scheduler ({})",e1.getMessage());
						}

						if(!jobExists && OceanicValueSelector.getValueSelector(provider.getValueSelector(itemName),ValueSelectorType.GET)!=null  ) {
							// set up the Quartz jobs
							JobDataMap map = new JobDataMap();
							map.put("SerialPort", serialPort);
							map.put("ValueSelector",OceanicValueSelector.getValueSelector(provider.getValueSelector(itemName),ValueSelectorType.GET));
							map.put("Binding",this);

							JobDetail job = newJob(OceanicBinding.PollJob.class)
									.withIdentity(itemName+"-"+provider.getValueSelector(itemName).toString(), "Oceanic-"+provider.toString())
									.usingJobData(map)
									.build();

							Trigger trigger = newTrigger()
									.withIdentity(itemName+"-"+provider.getValueSelector(itemName).toString(), "Oceanic-"+provider.toString())
									.startNow()
									.withSchedule(simpleSchedule()
											.repeatForever()
											.withIntervalInSeconds(provider.getPollingInterval(itemName)))            
											.build();

							try {
								sched.scheduleJob(job, trigger);
							} catch (SchedulerException e) {
								logger.error("An exception occurred while scheduling a Quartz Job");
							}
						}

						// kill the Quartz jobs that we do not need anymore
						try {
							for(String group: sched.getJobGroupNames()) {
								// enumerate each job in group
								for(JobKey jobKey : sched.getJobKeys(jobGroupEquals(group))) {
									if(findFirstMatchingBindingProvider(jobKey.getName().split("-")[0]) == null) {
										sched.deleteJob(jobKey);
									}
								}
							}
						} catch (SchedulerException e1) {
							logger.error("An exception occurred while quering the Quartz Scheduler ({})",e1.getMessage());
						}
					}

				}

			}

			// close down the serial ports that do not have any Items anymore associated to them
			for(String serialPort : serialDevices.keySet()) {
				SerialDevice serialDevice = serialDevices.get(serialPort);
				Set<String> itemNames = contextMap.get(serialPort);
				if(itemNames == null || itemNames.size()==0 ) {
					contextMap.remove(serialPort);
					serialDevice.close();
					serialDevices.remove(serialPort);						
				}
			}		

		}
	}

	protected OceanicBindingProvider findFirstMatchingBindingProvider(String itemName) {
		OceanicBindingProvider firstMatchingProvider = null;
		for (OceanicBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Oceanic Refresh Service";
	}

	protected class SerialDevice implements SerialPortEventListener {

		private String port;
		private int baud = 19200;

		/** we store the previous value of a status variable, and only publish Updates on the bus
		 * if the value differs. 
		 */
		private HashMap<OceanicValueSelector,String> cachedValues = new HashMap<OceanicValueSelector,String>();

		private EventPublisher eventPublisher;

		private CommPortIdentifier portId;
		private SerialPort serialPort;

		private InputStream inputStream;
		private OutputStream outputStream;

		private String response = "";

		public SerialDevice(String port) {
			this.port = port;
		}

		public SerialDevice(String port, int baud) {
			this.port = port;
			this.baud = baud;
		}

		public void setEventPublisher(EventPublisher eventPublisher) {
			this.eventPublisher = eventPublisher;
		}

		public void unsetEventPublisher(EventPublisher eventPublisher) {
			this.eventPublisher = null;
		}

		public String getPort() {
			return port;
		}

		public void clearResponse() {
			response ="";
		}

		public String getResponse() {
			return response;
		}

		/**
		 * Initialize this device and open the serial port
		 * 
		 * @throws InitializationException if port can not be opened
		 */
		@SuppressWarnings("rawtypes")
		public void initialize() throws InitializationException {
			// parse ports and if the default port is found, initialized the reader
			Enumeration portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					if (id.getName().equals(port)) {
						logger.debug("Serial port '{}' has been found.", port);
						portId = id;
					}
				}
			}
			if (portId != null) {
				// initialize serial port
				try {
					serialPort = (SerialPort) portId.open("openHAB", 2000);
				} catch (PortInUseException e) {
					throw new InitializationException(e);
				}

				try {
					inputStream = serialPort.getInputStream();
				} catch (IOException e) {
					throw new InitializationException(e);
				}

				try {
					serialPort.addEventListener(this);
				} catch (TooManyListenersException e) {
					throw new InitializationException(e);
				}

				// activate the DATA_AVAILABLE notifier
				serialPort.notifyOnDataAvailable(true);

				try {
					// set port parameters
					serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
					throw new InitializationException(e);
				}

				try {
					// get the output stream
					outputStream = serialPort.getOutputStream();
				} catch (IOException e) {
					throw new InitializationException(e);
				}
			} else {
				StringBuilder sb = new StringBuilder();
				portList = CommPortIdentifier.getPortIdentifiers();
				while (portList.hasMoreElements()) {
					CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
					if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
						sb.append(id.getName() + "\n");
					}
				}
				throw new InitializationException("Serial port '" + port + "' could not be found. Available ports are:\n" + sb.toString());
			}
		}

		public void serialEvent(SerialPortEvent event) {
			switch (event.getEventType()) {
			case SerialPortEvent.BI:
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), 32*1024*1024);
					if(br.ready()) {
						String line = br.readLine();
						line = StringUtils.chomp(line);
						line = line.replace(",",".");
						response = line.trim();
					}
				} catch (IOException e) {
					logger.debug("Error receiving data on serial port {}: {}", new Object[] { port, e.getMessage() });
				}
				break;
			}
		}


		/**
		 * Sends a string to the serial port of this device
		 * 
		 * @param msg the string to send
		 */
		public void writeString(String msg) {
			try {
				outputStream.write(msg.getBytes());
				outputStream.flush();
			} catch (IOException e) {
				logger.error("Error writing '{}' to serial port {}: {}", new Object[] { msg, port, e.getMessage() });
			}
		}

		public String requestResponse(String msg) {
			synchronized(this) {
				writeString(msg+"\r");
				String response = "";
				while(response.equals("")) {
					response = getResponse();
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						logger.debug("An exception occurred while putting the thread to sleep: {}",e.getMessage());
						e.printStackTrace();
					}
				}
				clearResponse();
				return response;
			}
		}


		/**
		 * Close this serial device
		 */
		public void close() {
			serialPort.removeEventListener();
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
			serialPort.close();
		}
	}


	public static class PollJob implements Job {


		@SuppressWarnings("unchecked")
		private State createStateForType(OceanicValueSelector selector, String value) throws BindingConfigParseException {

			Class<? extends Type> typeClass  = selector.getTypeClass();
			List<Class<? extends State>> stateTypeList = new ArrayList<Class<? extends State>>();

			stateTypeList.add((Class<? extends State>) typeClass);

			State state = TypeParser.parseState(stateTypeList, selector.convertValue(value));

			return state;	
		}

		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			// get the reference to the Stick
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			String serialPort = (String) dataMap.get("SerialPort");
			OceanicValueSelector valueSelector = (OceanicValueSelector) dataMap.get("ValueSelector");
			OceanicBinding theBinding = (OceanicBinding) dataMap.get("Binding");

			SerialDevice serialDevice = theBinding.serialDevices.get(serialPort);
			String response = serialDevice.requestResponse(valueSelector.name());

			// process response etc

			for (OceanicBindingProvider provider : theBinding.providers) {
				for (String itemName : provider.getItemNames()) {
					String itemSerialPort = provider.getSerialPort(itemName);
					OceanicValueSelector itemSelector = OceanicValueSelector.getValueSelector(provider.getValueSelector(itemName),ValueSelectorType.GET);

					if (itemSerialPort.equals(serialPort) && itemSelector.equals(valueSelector)) {
						if(serialDevice.cachedValues.get(valueSelector)==null || !serialDevice.cachedValues.get(valueSelector).equals(response)) {
							serialDevice.cachedValues.put(valueSelector, response);
							State value;
							try {
								value = createStateForType(valueSelector,response);
							} catch (BindingConfigParseException e) {
								logger.error("An exception occured while converting {} to a valide state : {}",response,e.getMessage());
								return;
							}

							serialDevice.eventPublisher.postUpdate(itemName, value);
						}
					}
				}
			}
		}
	}
}
