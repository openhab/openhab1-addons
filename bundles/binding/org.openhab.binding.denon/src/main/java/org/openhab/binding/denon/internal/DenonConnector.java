/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

import java.beans.Introspector;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.denon.internal.communication.entities.Deviceinfo;
import org.openhab.binding.denon.internal.communication.entities.Main;
import org.openhab.binding.denon.internal.communication.entities.ZoneStatus;
import org.openhab.binding.denon.internal.communication.entities.ZoneStatusLite;
import org.openhab.binding.denon.internal.communication.entities.commands.AppCommandRequest;
import org.openhab.binding.denon.internal.communication.entities.commands.AppCommandResponse;
import org.openhab.binding.denon.internal.communication.entities.commands.CommandRx;
import org.openhab.binding.denon.internal.communication.entities.commands.CommandTx;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Response;
import com.ning.http.client.providers.netty.NettyAsyncHttpProvider;

/**
 * This class makes the connection to the receiver and manages it.  
 * It is also responsible for sending commands to the receiver.  
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class DenonConnector {
	
	private static final Logger logger = LoggerFactory.getLogger(DenonConnector.class);
	
	private static final int REQUEST_TIMEOUT_MS = 5000; // 5 seconds

	// All regular commands. Example: PW, SICD, SITV, Z2MU
	private static final Pattern COMMAND_PATTERN = Pattern.compile("^([A-Z0-9]{2})+(.+)$");
	
	// Matches all secondary zone commands with a parameter. Example: Z2TUNER
	private static final Pattern ZONE_SUBCOMMAND_PATTERN = Pattern.compile("^(Z[0-9]{1}((?!ON|OFF|UP|DOWN)([A-Z]){2,}))$");
	
	// Example: E2Counting Crows
	private static final Pattern DISPLAY_PATTERN = Pattern.compile("^(E|A)([0-9]{1})(.+)$");
	
	// Main URL for the receiver
	private static final String URL_MAIN = "formMainZone_MainZoneXml.xml";
	
	// Main Zone Status URL	
	private static final String URL_ZONE_MAIN = "formMainZone_MainZoneXmlStatus.xml";

	// Secondary zone lite status URL (contains less info) 
	private static final String URL_ZONE_SECONDARY_LITE = "formZone%d_Zone%dXmlStatusLite.xml";
	
	// Device info URL
	private static final String URL_DEVICE_INFO = "Deviceinfo.xml";

	// URL to send app commands to 
	private static final String URL_APP_COMMAND = "AppCommand.xml";
	
	private static final BigDecimal NINETYNINE = new BigDecimal("99");
	
	private static final BigDecimal POINTFIVE = new BigDecimal("0.5");
	
	private static final String CONTENT_TYPE_XML = "application/xml";
	
	private final DenonConnectionProperties connection;
	
	private final String cmdUrl;
	
	private final String statusUrl;
	
	private final AsyncHttpClient client;
	
	private final DenonPropertyUpdatedCallback callback;

	private DenonListener listener;
	
	private Map<String,State> stateCache = new HashMap<String, State>();
	
	private ExecutorService executor = Executors.newFixedThreadPool(1); 
	
	private boolean displayNowplaying = false;
	
	public DenonConnector(DenonConnectionProperties connection, DenonPropertyUpdatedCallback callback) {
		this.connection = connection;
		this.callback = callback;
		this.client = new AsyncHttpClient(new NettyAsyncHttpProvider(createAsyncHttpClientConfig()));
		
		this.cmdUrl = String.format("http://%s:%d/goform/formiPhoneAppDirect.xml?", connection.getHost(), connection.getHttpPort());
		this.statusUrl = String.format("http://%s:%d/goform/", connection.getHost(), connection.getHttpPort());
	}
	
	/**
	 * Set up a telnet connection to the receiver AND fetch initial state over HTTP. 
	 */
	public void connect() {
		if (connection.isTelnet()) {
			listener = new DenonListener(connection, new DenonUpdateReceivedCallback() {
				@Override
				public void updateReceived(String command) {
					processUpdate(command);
				}

				@Override
				public void listenerConnected() {
					getInitialState();
				}

				@Override
				public void listenerDisconnected() {
					sendUpdate(DenonProperty.POWER.getCode(), OnOffType.OFF);
				}
			});
			listener.start();
		}
		getInitialState();
	}
	
	/**
	 * Close all connections
	 */
	public void disconnect() {
		executor.shutdown();
		
		if (listener != null) {
			listener.shutdown();
		}
	}
	
	/**
	 * Send a command for a certain property 
	 * 
	 * @param config The property
	 * @param command The command
	 */
	public void sendCommand(DenonBindingConfig config, Command command) {
		String commandToSend = null;
		Class<? extends Command> commandClass = command.getClass();
		
		if (commandClass.equals(OnOffType.class)) {		
			commandToSend = getCommandFor(config, (OnOffType)command);
		} else if (commandClass.equals(IncreaseDecreaseType.class)) {
			commandToSend = getCommandFor(config, (IncreaseDecreaseType)command);
		} else if (commandClass.equals(PercentType.class)) {
			commandToSend = getCommandFor(config, (PercentType)command);
		} else if (commandClass.equals(StringType.class)) {
			commandToSend = getCommandFor(config, (StringType)command);
		}
		
		internalSendCommand(commandToSend);		
	}

	/**
	 * Gets the current state of all properties from the receiver, including
	 * basic configuration info (like the number of zones)  
	 */
	public void getInitialState() {
		setConfigProperties();
		updateState();
	}
	
	/**
	 * Update the value for all properties. Includes fetching it from the receiver.     
	 */
	public void updateState() {
		Date start = new Date();
		logger.trace("Refresh Denon HTTP state");
		refreshHttpProperties();
		
		for (Entry<String, State> state : stateCache.entrySet()) {
			callback.updated(connection.getInstance(), state.getKey(), state.getValue());
		}
		
		logger.trace("Refresh took {}ms", new Date().getTime() - start.getTime());
	}

	/**
	 * Update a single property from the state cache
	 * 
	 * @param property The name of the property
	 */
	public void updateStateFromCache(String property) {
		if (stateCache.containsKey(property)) {
			callback.updated(connection.getInstance(), property, stateCache.get(property));
		}
	}
	
	private String getCommandFor(DenonBindingConfig config, OnOffType onOff) {
		String commandToSend = null;
		String property = config.getActualProperty();

		if (config.isOnOffProperty()) {
			if (property.equals(DenonProperty.POWER.getCode())) {
				if (OnOffType.ON.equals(onOff)) {
					commandToSend = "PWON";
				} else { 
					commandToSend = "PWSTANDBY";
				}
			} else {
				commandToSend = property + onOff.name();
			}
		} else { 
			if (onOff.equals(OnOffType.ON)) { 
				commandToSend = property;
			}
		}
		
		return commandToSend;
	}
	
	private String getCommandFor(DenonBindingConfig config, IncreaseDecreaseType increaseDecreaseType) {
		String commandToSend = null;
		String property = config.getActualProperty();

		if (increaseDecreaseType.equals(IncreaseDecreaseType.INCREASE)) {
			commandToSend = property + "UP";
		} else { 
			commandToSend = property + "DOWN";
		}
		
		return commandToSend;
	}
	
	private String getCommandFor(DenonBindingConfig config, StringType stringType) {
		String commandToSend = null;
		String property = config.getActualProperty();
		
		if (property.equals(DenonProperty.INPUT.getCode())) {
			commandToSend = "SI" + stringType.toString();
		} else if (property.equals(DenonProperty.COMMAND.getCode())) {
			commandToSend = stringType.toString();
		}
		
		return commandToSend;
	}

	private String getCommandFor(DenonBindingConfig config,	PercentType percentType) {
		String property = config.getActualProperty();
		String commandToSend = property + toDenonValue(percentType.toBigDecimal());
		
		return commandToSend;
	}
	
	/**
	 * This method tries to parse information received over the telnet connection. 
	 * It's quite unreliable. Some chars go missing or turn into other chars. That's
	 * why each command is validated using a regex.
	 * 
	 * @param commandString The received command (one line)
	 */
	private void processUpdate(String commandString) {
		if (COMMAND_PATTERN.matcher(commandString).matches()) {
			
			/* 
			 * This splits the commandString into the command and the parameter. SICD 
			 * for example has SI as the command and CD as the parameter. 
			 */
			String command = commandString.substring(0, 2);
			String value = commandString.substring(2, commandString.length()).trim();
			
			// Secondary zone commands with a parameter 
			if (ZONE_SUBCOMMAND_PATTERN.matcher(commandString).matches()) {
				command = commandString.substring(0, 4);
				value = commandString.substring(4, commandString.length()).trim();
			}
			
			logger.debug("Command: {}, value: {}", command, value);
			
			if (value.equals("ON") || value.equals("OFF")) {
				sendUpdate(command, OnOffType.valueOf(value));
			} else if (value.equals("STANDBY")) {
				sendUpdate(command, OnOffType.OFF);
			} else if (StringUtils.isNumeric(value)) {
				PercentType percent = new PercentType(fromDenonValue(value));
				command = translateVolumeCommand(command);
				sendUpdate(command, percent);
			} else if (command.equals("SI")) {
				sendUpdate(DenonProperty.INPUT.getCode(), new StringType(value));
				sendUpdate(commandString, OnOffType.ON);
			} else if (command.equals("MS")) {
				sendUpdate(DenonProperty.SURROUND_MODE.getCode(), new StringType(value));
			} else if (command.equals("NS")) {
				processTitleCommand(command, value);
			} 
		} else {
			logger.debug("Invalid command: " + commandString);
		}
	}

	private void processTitleCommand(String command, String value) {
		if (DISPLAY_PATTERN.matcher(value).matches()) {
			Integer commandNo = Integer.valueOf(value.substring(1, 2));
			String titleValue = value.substring(2);
			
			if (commandNo == 0) {
				displayNowplaying = titleValue.contains("Now Playing");
			}
			
			State state = displayNowplaying ? new StringType(cleanupDisplayInfo(titleValue)) : UnDefType.UNDEF; 
			
			switch (commandNo) {
				case 1:
					sendUpdate(DenonProperty.TRACK.getCode(), state);
				break;
				case 2:
					sendUpdate(DenonProperty.ARTIST.getCode(), state);
				break;
				case 4:
					sendUpdate(DenonProperty.ALBUM.getCode(), state);
				break;
			}
		}
	}

	private void sendUpdate(String property, State state) {
		stateCache.put(property, state);
		callback.updated(connection.getInstance(), property, state);
	}
	
	private String toDenonValue(BigDecimal percent) {
		// Round to nearest number divisible by 0.5 
		percent = percent.divide(POINTFIVE).setScale(0, RoundingMode.UP)
						 .multiply(POINTFIVE)
						 .min(connection.getMainVolumeMax())
						 .max(BigDecimal.ZERO);

		String dbString = String.valueOf(percent.intValue());
		
		if (percent.compareTo(BigDecimal.TEN) == -1) {
			dbString = "0" + dbString;
		} 
		if (percent.remainder(BigDecimal.ONE).equals(POINTFIVE)) {
			dbString = dbString + "5";
		}
		
		return dbString;
	}
	
	private BigDecimal fromDenonValue(String string) {
		/*
		 * 455 = 45,5
		 * 45 = 45
		 * 045 = 4,5
		 * 04 = 4 
		 */
		BigDecimal value = new BigDecimal(string);
		if (value.compareTo(NINETYNINE) == 1 || (string.startsWith("0") && string.length() > 2)) {
			value = value.divide(BigDecimal.TEN);
		}
		
		return value;
	}

	private void internalSendCommand(String command) {
		if (StringUtils.isBlank(command)) {
			logger.warn("Trying to send empty command");
			return;
		}
		
		try {
			String url = cmdUrl + URLEncoder.encode(command, Charset.defaultCharset().displayName());
			logger.trace("Calling url {}", url);
			
			client.prepareGet(url)
				.execute(new AsyncCompletionHandler<Response>() {
					@Override
					public Response onCompleted(Response response) throws Exception {
						if (response.getStatusCode() != 200) {
							logger.warn("Error {} while sending command", response.getStatusText());
						}
						return response;
					}
			
					@Override
					public void onThrowable(Throwable t) {
						logger.warn("Error sending command", t);
					}
				});
		} catch (UnsupportedEncodingException e) {
			logger.warn("Error preparing command", e);
		} catch (IOException e) {
			logger.warn("Error sending command", e);
		}
	}
	
	private void updateMain() {
		String url = statusUrl + URL_MAIN;
		logger.trace("Refreshing URL: {}", url);

		Main statusMain = getDocument(url, Main.class); 
		if (statusMain != null) {
			stateCache.put(DenonProperty.POWER.getCode(), statusMain.getPower().getValue() ? OnOffType.ON : OnOffType.OFF);
		}
	}
	
	private void updateMainZone() {
		String url = statusUrl + URL_ZONE_MAIN;
		logger.trace("Refreshing URL: {}", url);

		ZoneStatus mainZone = getDocument(url, ZoneStatus.class);
		if (mainZone != null) {
			stateCache.put(DenonProperty.INPUT.getCode(), new StringType(mainZone.getInputFuncSelect().getValue()));
			stateCache.put("SI" + mainZone.getInputFuncSelect().getValue(), OnOffType.ON);
			stateCache.put(DenonProperty.MASTER_VOLUME.getCode(), new PercentType(mainZone.getMasterVolume().getValue()));
			stateCache.put(DenonProperty.POWER_MAINZONE.getCode(), mainZone.getPower().getValue() ? OnOffType.ON : OnOffType.OFF);
			stateCache.put(DenonProperty.MUTE.getCode(), mainZone.getMute().getValue() ? OnOffType.ON : OnOffType.OFF);
			stateCache.put(DenonProperty.SURROUND_MODE.getCode(), new StringType(mainZone.getSurrMode().getValue()));
		}
	}
	
	private void updateSecondaryZones() {
		for (int i = 2; i <= connection.getZoneCount(); i++) {
			String url = String.format("%s" + URL_ZONE_SECONDARY_LITE, statusUrl, i, i);
			logger.trace("Refreshing URL: {}", url);
			ZoneStatusLite zoneSecondary = getDocument(url, ZoneStatusLite.class);
			if (zoneSecondary != null) {
				stateCache.put("Z"+i, zoneSecondary.getPower().getValue() ? OnOffType.ON : OnOffType.OFF);
				stateCache.put("Z"+i+DenonProperty.ZONE_VOLUME.getCode(), new PercentType(zoneSecondary.getMasterVolume().getValue()));
				stateCache.put("Z"+i+DenonProperty.MUTE.getCode(), zoneSecondary.getMute().getValue() ? OnOffType.ON : OnOffType.OFF);
			}
		}
	}
	
	private void updateDisplayInfo() {
		String url = statusUrl + URL_APP_COMMAND;
		logger.trace("Refreshing URL: {}", url);

		AppCommandRequest request = AppCommandRequest.of(CommandTx.CMD_NET_STATUS);
		AppCommandResponse response = postDocument(url, AppCommandResponse.class, request);
		
		if (response != null) {
			CommandRx titleInfo = response.getCommands().get(0);
			stateCache.put(DenonProperty.TRACK.getCode(), getStateForValue(titleInfo.getText("track")));
			stateCache.put(DenonProperty.ARTIST.getCode(), getStateForValue(titleInfo.getText("artist")));
			stateCache.put(DenonProperty.ALBUM.getCode(), getStateForValue(titleInfo.getText("album")));
		}
	}
		
	private void setConfigProperties() {
		String url = statusUrl + URL_DEVICE_INFO;
		logger.debug("Refreshing URL: {}", url);
		
		Deviceinfo deviceinfo = getDocument(url, Deviceinfo.class);
		if (deviceinfo != null) {
			connection.setZoneCount(deviceinfo.getDeviceZones());
		}
		
		/**
		 * The maximum volume is received from the telnet connection in the
		 * form of the MVMAX property. It is not always received reliable however,
		 * so we're using a default for now. 
		 */
		connection.setMainVolumeMax(DenonConnectionProperties.MAX_VOLUME);
		
		logger.debug("Denon {} zones: {}", connection.getInstance(), connection.getZoneCount());
	}
	
	private void refreshHttpProperties() {
		logger.trace("Refreshing Denon status");
		stateCache.clear();
		
		updateMain();
		updateMainZone();
		updateSecondaryZones();
		updateDisplayInfo();
	}
	
	/**
	 * Translate the volume command from the receiver to the openHAB property.
	 * 
	 * Z2 -> Z2ZV
	 * Z3 -> Z3ZV, etc
	 * 
	 * @param command The command from the receiver
	 * @return The property name in openHAB 
	 */
	private String translateVolumeCommand(String command) {
		if (command.matches("Z[0-9]")) {
			command = command + DenonProperty.ZONE_VOLUME.getCode();
		}
		
		return command;
	}
	
	private State getStateForValue(String value) {
		if (StringUtils.isBlank(value))
			return UnDefType.UNDEF;
		
		return new StringType(value);
	}
	
	/**
	 * Display info could contain some garbled text, attempt to clean it up.   
	 */
	private String cleanupDisplayInfo(String titleValue) {
		byte firstByteRemoved[] =  Arrays.copyOfRange(titleValue.getBytes(), 1, titleValue.getBytes().length);
		titleValue = new String(firstByteRemoved).replaceAll("[\u0000-\u001f]", "");
		return titleValue;
	}

	private AsyncHttpClientConfig createAsyncHttpClientConfig() {
		Builder builder = new AsyncHttpClientConfig.Builder();
		builder.setRequestTimeoutInMs(REQUEST_TIMEOUT_MS);
		builder.setUseRawUrl(true);
		return builder.build();
	}
	
	private <T> T getDocument(String uri, Class<T> response) {
		try {
			String result = HttpUtil.executeUrl("GET", uri, REQUEST_TIMEOUT_MS);
			
			if (StringUtils.isNotBlank(result)) {
				JAXBContext jc = JAXBContext.newInstance(response);
				XMLInputFactory xif = XMLInputFactory.newInstance();
				XMLStreamReader xsr = xif.createXMLStreamReader(IOUtils.toInputStream(result));
				xsr = new PropertyRenamerDelegate(xsr);
	
				@SuppressWarnings("unchecked")
				T obj = (T) jc.createUnmarshaller().unmarshal(xsr);
				
				return obj;
			}
		} catch (JAXBException e) {
			logger.debug("Encoding error in get", e);
		} catch (XMLStreamException e) {
			logger.debug("Communication error in get", e);
		}
		
		return null;
	}
	
	private <T,S> T postDocument(String uri, Class<T> response, S request) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(request.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(request, sw);
			
			String result = HttpUtil.executeUrl("POST", uri, IOUtils.toInputStream(sw.toString()), CONTENT_TYPE_XML, REQUEST_TIMEOUT_MS);
			
			if (StringUtils.isNotBlank(result)) {
				JAXBContext jcResponse = JAXBContext.newInstance(response);
				
				@SuppressWarnings("unchecked")
				T obj =
				    (T) jcResponse.createUnmarshaller().unmarshal(IOUtils.toInputStream(result));
				
				return obj;
			}
		} catch (JAXBException e) {
			logger.debug("Encoding error in post", e);
		}
		
		return null;
	}
	
	private static class PropertyRenamerDelegate extends StreamReaderDelegate {
		
		public PropertyRenamerDelegate(XMLStreamReader xsr) {
		    super(xsr);
		}
		
		@Override
		public String getAttributeLocalName(int index) {
			return Introspector.decapitalize(super.getAttributeLocalName(index));
		}
		
		@Override
		public String getLocalName() {
			return Introspector.decapitalize(super.getLocalName());
		}
	}
	
}
