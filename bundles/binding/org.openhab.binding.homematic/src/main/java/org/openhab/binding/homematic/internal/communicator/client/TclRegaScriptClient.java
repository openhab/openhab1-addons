/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.homematic.internal.common.HomematicConfig;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.config.binding.DatapointConfig;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.config.binding.VariableConfig;
import org.openhab.binding.homematic.internal.model.CommonUnmarshallerListener;
import org.openhab.binding.homematic.internal.model.HmChannel;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmDevice;
import org.openhab.binding.homematic.internal.model.HmDeviceList;
import org.openhab.binding.homematic.internal.model.HmProgram;
import org.openhab.binding.homematic.internal.model.HmProgramList;
import org.openhab.binding.homematic.internal.model.HmResult;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.binding.homematic.internal.model.HmVariable;
import org.openhab.binding.homematic.internal.model.HmVariableList;
import org.openhab.binding.homematic.internal.model.TclScript;
import org.openhab.binding.homematic.internal.model.TclScripts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client to execute TclRega scripts on the CCU. It's mainly used for
 * metadata/value retrieval, getting and setting variables and executing
 * programs.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class TclRegaScriptClient {
	private static final Logger logger = LoggerFactory.getLogger(TclRegaScriptClient.class);
	private final static boolean TRACE_ENABLED = logger.isTraceEnabled();

	private HomematicConfig config = HomematicContext.getInstance().getConfig();

	private Map<String, String> tclregaScripts;
	private HttpClient httpClient;

	/**
	 * Creates the client.
	 */
	public TclRegaScriptClient() {
		logger.info("Starting {}", TclRegaScriptClient.class.getSimpleName());

		tclregaScripts = loadTclRegaScripts();

		httpClient = new HttpClient(new SimpleHttpConnectionManager(true));
		HttpClientParams params = httpClient.getParams();
		params.setConnectionManagerTimeout(5000);
		params.setSoTimeout(30000);
		params.setContentCharset("ISO-8859-1");
	}

	/**
	 * Destroys the client.
	 */
	public void shutdown() {
		tclregaScripts = null;
		httpClient = null;
	}

	/**
	 * Retrieves all variables from the CCU.
	 */
	public void iterateAllVariables(TclIteratorCallback callback) throws CcuClientException {
		List<HmVariable> variables = sendScriptByName("getAllVariables", HmVariableList.class).getVariables();
		for (HmVariable variable : variables) {
			VariableConfig bindingConfig = new VariableConfig(variable.getName());
			callback.iterate(bindingConfig, variable);
		}
	}

	/**
	 * Retrieves all datapoints from the CCU.
	 */
	public void iterateAllDatapoints(TclIteratorCallback callback) throws CcuClientException {
		List<HmDevice> devices = sendScriptByName("getAllDevices", HmDeviceList.class).getDevices();
		for (HmDevice device : devices) {
			for (HmChannel channel : device.getChannels()) {
				for (HmDatapoint dp : channel.getDatapoints()) {
					DatapointConfig bindingConfig = new DatapointConfig(device.getAddress(), channel.getNumber(),
							dp.getName());
					callback.iterate(bindingConfig, dp);
				}
			}
		}
	}

	/**
	 * Retrieves all programs from the CCU.
	 */
	public List<HmProgram> getAllPrograms() throws CcuClientException {
		return sendScriptByName("getAllPrograms", HmProgramList.class).getPrograms();
	}

	/**
	 * Execute a program on the CCU.
	 */
	public void executeProgram(String programName) throws CcuClientException {
		logger.debug("Executing program on CCU: {}", programName);
		HmResult result = sendScriptByName("executeProgram", HmResult.class, new String[] { "program_name" },
				new String[] { programName });
		if (!result.isValid()) {
			throw new CcuClientException("Unable to start CCU program " + programName);
		}
	}

	/**
	 * Set a variable on the CCU.
	 */
	public void setVariable(HmValueItem hmValueItem, Object value) throws CcuClientException {
		logger.debug("Sending {} with value '{}' to CCU", hmValueItem.getName(), value);
		HmResult result = sendScriptByName("setVariable", HmResult.class, new String[] { "variable_name",
				"variable_state" }, new String[] { hmValueItem.getName(), ObjectUtils.toString(value) });
		if (!result.isValid()) {
			throw new CcuClientException("Unable to set CCU variable " + hmValueItem.getName());
		}
	}

	/**
	 * Sends a message and/or sets properties for the display of a 19 key Homematic remote control.
	 * Used in the Homematic action.
	 */
	public void setRemoteControlDisplay(String remoteDisplayAddress, String text, int unit, int backlight, int beep)
			throws Exception {
		HmResult result = sendScriptByName("setRemoteControl", HmResult.class, new String[] { "remote_address",
				"text_value", "unit_value", "backlight_value", "beep_value" }, new String[] { remoteDisplayAddress,
				text, String.valueOf(unit), String.valueOf(backlight), String.valueOf(beep) });

		if (!result.isValid()) {
			throw new CcuClientException("Failed to set RemoteControl with address " + remoteDisplayAddress);
		}
	}

	private <T> T sendScriptByName(String scriptName, Class<T> clazz) throws CcuClientException {
		return sendScript(tclregaScripts.get(scriptName), clazz);
	}

	private <T> T sendScriptByName(String scriptName, Class<T> clazz, String[] variableNames, String[] values)
			throws CcuClientException {
		String script = tclregaScripts.get(scriptName);
		for (int i = 0; i < variableNames.length; i++) {
			script = StringUtils.replace(script, "{" + variableNames[i] + "}", values[i]);
		}
		return sendScript(script, clazz);
	}

	/**
	 * Main method for sending a TclRega script and parsing the XML result.
	 */
	@SuppressWarnings("unchecked")
	private synchronized <T> T sendScript(String script, Class<T> clazz) throws CcuClientException {
		PostMethod post = null;
		try {
			script = StringUtils.trim(script);
			if (StringUtils.isEmpty(script)) {
				throw new RuntimeException("Homematic TclRegaScript is empty!");
			}
			if (TRACE_ENABLED) {
				logger.trace("TclRegaScript: {}", script);
			}

			post = new PostMethod(config.getTclRegaUrl());
			RequestEntity re = new ByteArrayRequestEntity(script.getBytes("ISO-8859-1"));
			post.setRequestEntity(re);
			httpClient.executeMethod(post);

			String result = post.getResponseBodyAsString();
			result = StringUtils.substringBeforeLast(result, "<xml><exec>");
			if (TRACE_ENABLED) {
				logger.trace("Result TclRegaScript: {}", result);
			}

			Unmarshaller um = JAXBContext.newInstance(clazz).createUnmarshaller();
			um.setListener(new CommonUnmarshallerListener());
			return (T) um.unmarshal(new StringReader(result));
		} catch (Exception ex) {
			throw new CcuClientException(ex.getMessage(), ex);
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}

	/**
	 * Load predefined scripts from an XML file.
	 */
	private Map<String, String> loadTclRegaScripts() {
		try {
			Unmarshaller um = JAXBContext.newInstance(TclScripts.class).createUnmarshaller();
			InputStream stream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("homematic/tclrega-scripts.xml");
			TclScripts scripts = (TclScripts) um.unmarshal(stream);

			Map<String, String> result = new HashMap<String, String>();
			for (TclScript script : scripts.getScripts()) {
				result.put(script.getName(), script.getData());
			}
			return result;
		} catch (JAXBException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * Callback interface to iterate through all Homematic valueItems.
	 */
	public interface TclIteratorCallback {
		/**
		 * Called for every Homematic valueItem after loading from the CCU.
		 */
		public void iterate(HomematicBindingConfig bindingConfig, HmValueItem hmValueItem);
	}
}
