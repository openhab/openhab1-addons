/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.hue.internal;

import org.openhab.action.hue.Rule;
import org.openhab.binding.hue.internal.common.HueContext;
import org.openhab.binding.hue.internal.hardware.HueBridge;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;



/**
 * Provide static Methods for Actions. 
 * 
 * @author Gernot Eger
 * @since 1.7.0
 */
public class Hue {

	private static final Logger logger = LoggerFactory.getLogger(Hue.class);

	
	
	/**
	 * set Group to those id's; if a group with this name doesn't exist, create a new own
	 * TODO: create similar procedure with Items/Groups passed directly!!!
	 * @param name 
	 * @param hueIds a list of hue ID's seperated by ';'
	 * @return id of generated hue group
	 */
	public static String hueSetGroup(String name,String hueIds){
		logger.error("not yet implemented");
		return "0";
	}
	
	
	/*
	 * 
	 * set settings for this scene on the light(s)
	 */
	public static String hueSetSceneSettings(String sceneName,String lightId,String body){
		logger.error("not yet implemented");
		return "0";
	}

	@ActionDoc(text = "Create a new Rule")
	public static Rule createHueRule(
			@ParamDoc(name = "name", text = "Nmae of the Rule") String name){
		return new Rule(name);
	}
	
	
//	/**
//	 * Sends a text to a Homematic remote control display.
//	 */
//	@ActionDoc(text = "Sends a text to a Homematic remote control display")
//	public static boolean sendHomematicDisplay(
//			@ParamDoc(name = "remoteControlAddress", text = "The address of the remote control") String remoteControlAddress,
//			@ParamDoc(name = "text", text = "The text to send to the display") String text) {
//
//		return sendDisplay(remoteControlAddress, text, null);
//	}

//	/**
//	 * Sends a text to a Homematic remote control display with options.
//	 */
//	@ActionDoc(text = "Sends a text to a Homematic remote control display with options")
//	public static boolean sendHomematicDisplay(
//			@ParamDoc(name = "remoteControlAddress", text = "The address of the remote control") String remoteControlAddress,
//			@ParamDoc(name = "text", text = "The text to send to the display") String text,
//			@ParamDoc(name = "options", text = "The beep, backlight, unit and symbol options to send to the display") String options) {
//		return sendMessage(remoteControlAddress, text, options);
//	}

	@ActionDoc(text = "Tests Connection to hue bridge")
	public static boolean pingHue() {
		String settings=getResource("/");
		if(settings==null)return false;
		
		logger.debug("ping success");
		return true;
	}

	/**
	 * get resource 
	 * @param path
	 * @return
	 */
	private static String getResource(String path){
		HueBridge bridge=HueContext.getInstance().getBridge();
		Client client=bridge.getClient();
		
		String url=bridge.getUrl()+path;
		WebResource webResource = client.resource(url);

		try {
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
			String resourceString = response.getEntity(String.class);

			if (response.getStatus() != 200) {
				logger.warn("Failed to connect to Hue bridge at '"+url+"': HTTP error code: "
						+ response.getStatus());
				return null;
			}
			return resourceString;
		} catch(ClientHandlerException e) {
			logger.warn("Failed to connect to Hue bridgeat '"+url+"': HTTP request timed out.");
			return null;
		}
		
	}
	/**
	 * send Message to hue bridge
	 * @param remoteControlAddress
	 * @param text
	 * @param options
	 * @return 
	 */
	private static boolean sendMessage(String url, String body, String options) {
		HueContext context=HueContext.getInstance();
		return false;
//		
//		if (!context.getHomematicClient().isStarted()) {
//			logger.warn("The Homematic client is not started, ignoring action sendHomematicDisplay!");
//			return false;
//		} else {
//			try {
//				context.getHomematicClient().setRemoteControlDisplay(remoteControlAddress, text, options);
//				return true;
//			} catch (HomematicClientException ex) {
//				logger.error(ex.getMessage(), ex);
//				return false;
//			}
//		}
	}

}
