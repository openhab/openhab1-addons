/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.exec.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.exec.ExecBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import sun.tools.jar.CommandLine;

/**
 * The swiss army knife binding which executes given commands on the commandline.
 * It could act as the opposite of WoL and sends the shutdown command to servers.
 * Or switches of WLAN connectivity if a scene "sleeping" is activated.
 * <p>
 * <i>Note</i>: when using 'ssh' you should use private key authorization since
 * the password cannot be read from commandline. The given user should have the
 * necessary permissions.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Pauli Anttila  
 * @since 0.6.0
 */
public class ExecBinding extends AbstractActiveBinding<ExecBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(ExecBinding.class);
	
	protected static final Command WILDCARD_COMMAND_KEY = StringType.valueOf("*");

	private static final String CMD_LINE_DELIMITER = "@@";
	
	/** the timeout for executing command (defaults to 60000 milliseconds) */
	private int timeout = 60000;

	/** the interval to find new refresh candidates (defaults to 1000 milliseconds)*/ 
	private int granularity = 1000;

	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

	/** RegEx to extract a parse a function String <code>'(.*?)\((.*)\)'</code> */
	private static final Pattern EXTRACT_FUNCTION_PATTERN = Pattern.compile("(.*?)\\((.*)\\)");
		
	@Override
	protected long getRefreshInterval() {
		return granularity;
	}

	@Override
	protected String getName() {
		return "Exec Refresh Service";
	}

	@Override
	public void activate() {
		super.activate();
		setProperlyConfigured(true);
	}
	
	public void execute() {
		for (ExecBindingProvider provider : providers) {
			for (String itemName : provider.getInBindingItemNames()) {
				
				String commandLine = provider.getCommandLine(itemName);
				
				int refreshInterval = provider.getRefreshInterval(itemName);
				String transformation = provider.getTransformation(itemName);
				
				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}
				
				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = age >= refreshInterval;
				
				if (needsUpdate) {
					
					logger.debug("item '{}' is about to be refreshed now", itemName);
						
					commandLine = String.format(commandLine, Calendar.getInstance().getTime(), "", itemName);

					String response = executeCommandAndWaitResponse(commandLine);
					

					if(response==null) {
						logger.error("No response received from command '{}'", commandLine);
					} else {
						String transformedResponse;
						
						try {
							String[] parts = splitTransformationConfig(transformation);
							String transformationType = parts[0];
							String transformationFunction = parts[1];
							
							TransformationService transformationService = 
								TransformationHelper.getTransformationService(ExecActivator.getContext(), transformationType);
							if (transformationService != null) {
								transformedResponse = transformationService.transform(transformationFunction, response);
							} else {
								transformedResponse = response;
								logger.warn("couldn't transform response because transformationService of type '{}' is unavailable", transformationType);
							}
						}
						catch (TransformationException te) {
							logger.error("transformation throws exception [transformation="
									+ transformation + ", response=" + response + "]", te);
							
							// in case of an error we return the response without any
							// transformation
							transformedResponse = response;
						}
						
						logger.debug("transformed response is '{}'", transformedResponse);
						
						Class<? extends Item> itemType = provider.getItemType(itemName);
						State state = createState(itemType, transformedResponse);
						
						if (state != null) {
							eventPublisher.postUpdate(itemName, state);
						}
					}
					
					lastUpdateMap.put(itemName, System.currentTimeMillis());
				}					
			}
		}
	}
	
	/**
	 * Splits a transformation configuration string into its two parts - the
	 * transformation type and the function/pattern to apply.
	 * 
	 * @param transformation the string to split
	 * @return a string array with exactly two entries for the type and the function
	 */
	protected String[] splitTransformationConfig(String transformation) {
		Matcher matcher = EXTRACT_FUNCTION_PATTERN.matcher(transformation);
		
		if (!matcher.matches()) {
			throw new IllegalArgumentException("given transformation function '" + transformation + "' does not follow the expected pattern '<function>(<pattern>)'");
		}
		matcher.reset();
		
		matcher.find();			
		String type = matcher.group(1);
		String pattern = matcher.group(2);
	
		return new String[] { type, pattern };
	}

	/**
	 * Returns a {@link State} which is inherited from the {@link Item}s
	 * accepted DataTypes. The call is delegated to the  {@link TypeParser}. If
	 * <code>item</code> is <code>null</code> the {@link StringType} is used.
	 *  
	 * @param itemType
	 * @param transformedResponse
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser}
	 * or a {@link StringType} if <code>item</code> is <code>null</code> 
	 */
	private State createState(Class<? extends Item> itemType, String transformedResponse) {
		try {
			if (itemType.isAssignableFrom(NumberItem.class)) {
				return DecimalType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(ContactItem.class)) {
				return OpenClosedType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(SwitchItem.class)) {
				return OnOffType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(RollershutterItem.class)) {
				return PercentType.valueOf(transformedResponse);
			} else {
				return StringType.valueOf(transformedResponse);
			}
		} catch (Exception e) {
			logger.debug("Couldn't create state of type '{}' for value '{}'", itemType, transformedResponse);
			return StringType.valueOf(transformedResponse);
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		
		ExecBindingProvider provider = 
			findFirstMatchingBindingProvider(itemName, command);
		
		if (provider == null) {
			logger.warn("doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
			return;
		}
		
		String commandLine = provider.getCommandLine(itemName, command);
		
		// fallback 
		if (commandLine == null) {
			commandLine = provider.getCommandLine(itemName, WILDCARD_COMMAND_KEY); 
		}
		if (commandLine != null && !commandLine.isEmpty()) {
			
			commandLine = String.format(commandLine, Calendar.getInstance().getTime(), command, itemName);
			
			executeCommand(commandLine);
		}
	}
	
	/**
	 * Find the first matching {@link ExecBindingProvider} according to 
	 * <code>itemName</code> and <code>command</code>. If no direct match is
	 * found, a second match is issued with wilcard-command '*'. 
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 * provider could be found
	 */
	private ExecBindingProvider findFirstMatchingBindingProvider(String itemName, Command command) {
		
		ExecBindingProvider firstMatchingProvider = null;
		
		for (ExecBindingProvider provider : this.providers) {
			
			String commandLine = provider.getCommandLine(itemName, command);
			
			if (commandLine != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		// we didn't find an exact match. probably one configured a fallback
		// command?
		if (firstMatchingProvider == null) {
			for (ExecBindingProvider provider : this.providers) {
				
				String commandLine = provider.getCommandLine(itemName, WILDCARD_COMMAND_KEY);
				if (commandLine != null) {
					firstMatchingProvider = provider;
					break;
				}
			}
		}
		
		return firstMatchingProvider;
	}

	/**
	 * <p>Executes <code>commandLine</code>. Sometimes (especially observed on 
	 * MacOS) the commandLine isn't executed properly. In that cases another 
	 * exec-method is to be used. To accomplish this please use the special 
	 * delimiter '<code>@@</code>'. If <code>commandLine</code> contains this 
	 * delimiter it is split into a String[] array and the special exec-method
	 * is used.</p>
	 * <p>A possible {@link IOException} gets logged but no further processing is
	 * done.</p> 
	 * 
	 * @param commandLine the command line to execute
	 * @see http://www.peterfriese.de/running-applescript-from-java/
	 */
	private void executeCommand(String commandLine) {
		try {
			if (commandLine.contains(CMD_LINE_DELIMITER)) {
				String[] cmdArray = commandLine.split(CMD_LINE_DELIMITER);
				Runtime.getRuntime().exec(cmdArray);
				logger.info("executed commandLine '{}'", Arrays.asList(cmdArray));
			} else {
				Runtime.getRuntime().exec(commandLine);
				logger.info("executed commandLine '{}'", commandLine);
			}
		}
		catch (IOException e) {
			logger.error("couldn't execute commandLine '" + commandLine + "'", e);
		} 
	}
	
	/**
	 * <p>Executes <code>commandLine</code>. Sometimes (especially observed on 
	 * MacOS) the commandLine isn't executed properly. In that cases another 
	 * exec-method is to be used. To accomplish this please use the special 
	 * delimiter '<code>@@</code>'. If <code>commandLine</code> contains this 
	 * delimiter it is split into a String[] array and the special exec-method
	 * is used.</p>
	 * <p>A possible {@link IOException} gets logged but no further processing is
	 * done.</p> 
	 * 
	 * @param commandLine the command line to execute
	 * @return response data from executed command line 
	 */
	private String executeCommandAndWaitResponse(String commandLine) {
		String retval = null;

		CommandLine cmdLine = null;

		if (commandLine.contains(CMD_LINE_DELIMITER)) {
			String[] cmdArray = commandLine.split(CMD_LINE_DELIMITER);
			cmdLine = new CommandLine(cmdArray[0]);

			for (int i = 1; i < cmdArray.length; i++) {
				cmdLine.addArgument(cmdArray[i], false);
			}
		} else {
			cmdLine = CommandLine.parse(commandLine);
		}

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

		ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
		Executor executor = new DefaultExecutor();

		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(stdout);

		executor.setExitValue(1);
		executor.setStreamHandler(streamHandler);
		executor.setWatchdog(watchdog);

		try {
			executor.execute(cmdLine, resultHandler);
			logger.debug("executed commandLine '{}'", commandLine);
		} catch (ExecuteException e) {
			logger.error("couldn't execute commandLine '" + commandLine + "'", e);
		} catch (IOException e) {
			logger.error("couldn't execute commandLine '" + commandLine + "'", e);
		}

		// some time later the result handler callback was invoked so we
		// can safely request the exit code
		try {
			resultHandler.waitFor();
			int exitCode = resultHandler.getExitValue();
			retval = StringUtils.chomp(stdout.toString()); 
			logger.debug("exit code '{}', result '{}'", exitCode, retval);

		} catch (InterruptedException e) {
			logger.error("Timeout occured when executing commandLine '" + commandLine + "'", e);
		}

		return retval;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		
		if (config != null) {
			String timeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(timeoutString)) {
				timeout = Integer.parseInt(timeoutString);
			}
			
			String granularityString = (String) config.get("granularity");
			if (StringUtils.isNotBlank(granularityString)) {
				granularity = Integer.parseInt(granularityString);
			}
		}
		
	}

	@Override
	public void addBindingProvider(ExecBindingProvider provider) {
		super.addBindingProvider(provider);
		
		
		setProperlyConfigured(true);
	}
}
