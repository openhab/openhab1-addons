/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.hue.internal;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.action.hue.Rule;
import org.openhab.binding.hue.internal.common.HueContext;
import org.openhab.binding.hue.internal.data.HueSettings;
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
 * Hue Resources will be handled as STrings in Json representation, 
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
	@ActionDoc(text = "not yet implemented")
	public static String hueSetSceneSettings(String sceneName,String lightId,String body){
		logger.error("not yet implemented");
		return "0";
	}

	@ActionDoc(text = "Create a new Rule")
	public static String hueCreateRule(
			@ParamDoc(name = "name", text = "Name of the Rule") String name){
		
		Rule r=new Rule(name);
			
		try {
			return r.toJson();
		} catch (Exception e) {
			logger.warn("failed to serialize to json",e);
			return null;
		}
	}
	
	
	@ActionDoc(text = "Add an action for a group to a rule. Returns the Json representation of the rule")
	public static String hueAddGroupAction(
			@ParamDoc(name = "ruleJson", text = "Json representation of the rule") String ruleJson,
			@ParamDoc(name = "group", text = "Id of the Group")	String group, 
			@ParamDoc(name = "bodyElement", text = "Name of the Body element")	String bodyElement, 
			@ParamDoc(name = "bodyValue", text = "Value of the element.")	Object bodyValue) throws IOException{
		
		Rule r=Rule.create(ruleJson);
		
		r.addGroupAction(group, bodyElement, bodyValue);
		
		return r.toJson();
	}
	
	
	@ActionDoc(text = "Add condition for tab key last pressed. Returns the Json representation of the rule")
	public static String hueAddTapButtonEqualsCondition(
			@ParamDoc(name = "ruleJson", text = "Json representation of the rule") String ruleJson, 
			@ParamDoc(name = "sensorId", text = "Id of tap device") String sensorId,
			@ParamDoc(name = "button", text = "BUtton id 1..4")int button) throws IOException{
		Rule r=Rule.create(ruleJson);
		
		r.addTapButtonEqualsCondition(sensorId, button);
		return r.toJson();
	}
	
	@ActionDoc(text = "Add condition for change of last pressed. Returns the Json representation of the rule")
	public static String hueAddSensorChangedCondition(
			@ParamDoc(name = "ruleJson", text = "Json representation of the rule") String ruleJson, 
			@ParamDoc(name = "sensorId", text = "Id of sensor device") String sensorId
			) throws IOException{
		
		Rule r=Rule.create(ruleJson);
		r.addTapDeviceChangedCondition(sensorId);
		return r.toJson();
	}
	

	/**
	 * set rule by name
	 * @param name
	 * @param ruleJson
	 * @return
	 */
	@ActionDoc(text = "set rule")
	public static String hueSetRule(
			@ParamDoc(name = "name", text = "Name of the rule")String name, 
			@ParamDoc(name = "ruleJson", text = "Json representation of the rule") String ruleJson){
		
		HueSettings settings=HueContext.getInstance().getBridge().getSettings();
		
		String id=settings.getRule(name);
		
		//logger.debug("found rule id for name '"+name+"': '"+id+"'");
		logger.debug("found rule id for name '{}': '{}'", name,id);
		String result;
		if(id==null){ // create new
			result=postResource("rules",ruleJson);	
		}else{
			result=putResource("rules/"+id,ruleJson);			
		}
		
		return result;
	}
	
	
	@ActionDoc(text = "Tests Connection to hue bridge")
	public static boolean pingHue() {
		String settings=getResource("/");
		if(settings==null)return false;
		
		logger.debug("ping success");
		return true;
	}

	
	enum HttpMethod {
		GET,
		PUT,
		POST
	} ;
	
	/**
	 * get resource 
	 * @param path
	 * @return
	 */
	private static String getResource(String path){
		return resourceRequest(HttpMethod.GET,path,null);
	}
	
	private static String putResource(String path, String body){
		return resourceRequest(HttpMethod.PUT,path,body);
	}
	
	private static String postResource(String path, String body){
		return resourceRequest(HttpMethod.POST,path,body);
	}
		
	private static String resourceRequest(HttpMethod method,String path, String body) {
		
		logger.debug("resourceRequest {} start...",method);
		
		HueBridge bridge=HueContext.getInstance().getBridge();
		Client client=bridge.getClient();
		
		String url=bridge.getUrl()+path;
		WebResource webResource = client.resource(url);

		try {
			ClientResponse response=null;
			switch(method){
				case GET:
					response = webResource.accept("application/json").get(ClientResponse.class);
					break;
				case PUT:
					response = webResource.accept("application/json").put(ClientResponse.class, body);
					break;
				case POST:
					response = webResource.accept("application/json").post(ClientResponse.class, body);
					break;
			}
			
			String responseString = response.getEntity(String.class);

			if (response.getStatus() != 200) {
				logger.warn("Failed to connect to Hue bridge at '"+url+"': HTTP error code: "
						+ response.getStatus());
				return null;
			}
			
			// check if ok
			ObjectMapper mapper = new ObjectMapper();
			try {
				Map <String,Object> responseMap=mapper.readValue(responseString,Map.class);
				
				if(responseMap.containsKey("error")){
					logger.warn("failed send command, body={}",body);
					logger.warn("failed send command, response={}",responseString);
				}
			}catch (IOException e) {
				logger.warn("failed to parse response '{}'",responseString);
			}
			
			return responseString;
		} catch(ClientHandlerException e) {
			logger.warn("Failed to connect to Hue bridgeat '"+url+"': HTTP request timed out.");
			return null;
		}finally{
			logger.debug("resourceRequest done.");
		}
	
	}
//	/**
//	 * send Message to hue bridge
//	 * @param remoteControlAddress
//	 * @param text
//	 * @param options
//	 * @return 
//	 */
//	private static boolean sendMessage(String url, String body, String options) {
//		HueContext context=HueContext.getInstance();
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
//	}

}
