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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.action.hue.AbstractHueResource;
import org.openhab.binding.hue.internal.common.HueContext;
import org.openhab.binding.hue.internal.data.HueSettings;
import org.openhab.binding.hue.internal.data.HueSettings.SettingsTree;
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
public class HueActions {

	private static final Logger logger = LoggerFactory.getLogger(HueActions.class);

	
	
//	/**
//	 * set Group to those id's; if a group with this name doesn't exist, create a new own
//	 * TODO: create similar procedure with Items/Groups passed directly!!!
//	 * @param name 
//	 * @param hueIds a list of hue ID's seperated by ';'
//	 * @return id of generated hue group
//	 */
//	public static String hueSetGroup(String name,String hueIds){
//		logger.error("not yet implemented");
//		return "0";
//	}
	
	
	/*
	 * 
	 * set settings for this scene on the light(s)
	 */
	@ActionDoc(text = "Set settings for a scene")
	public static String hueSetSceneSettings(
			@ParamDoc(name = "sceneId", text = "Id of the Scene")String sceneId,
			@ParamDoc(name = "lightIds", text = "List of Lights seperated by ','")String lightIds,
			@ParamDoc(name = "body", text = "Settings for those lights in scene as json body") String body){
		
		String result="";
		for(String light:splitIdString(lightIds)){	
			result=resourceRequest(HttpMethod.PUT,"scenes/"+sceneId+"/lights/"+light+"/state",body);
		}
		return result;
	}

	
	/**
	 * create scene with this name
	 * @param sceneName
	 * @return message from bridge or null if failed
	 */
	@ActionDoc(text = "Create a new Scene on the bridge, and store all lights' settings for this scene")
	public static String hueSetScene(
			@ParamDoc(name = "sceneId", text = "Id of the Scene")					String sceneId,
			@ParamDoc(name = "sceneName", text = "Name of the Scene")				String sceneName, 
			@ParamDoc(name = "lightIds", text = "List of Lights seperated by ','") 	String lights){
		Scene s=new Scene(sceneName,splitIdString(lights));
		String result=resourceRequest(HttpMethod.PUT,"scenes/"+sceneId,s);
		
		return result;
				
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
	
	@ActionDoc(text = "Set scene for this tap button")
	public static void hueSetSceneForButton(
			@ParamDoc(name = "rule name", text = "rule name") String ruleName,
			@ParamDoc(name = "sensorId", text = "Id of tap device") String sensorId,
			@ParamDoc(name = "button", text = "Button id 1..4")int button,
			@ParamDoc(name = "scene", text = "scene id") String scene) throws IOException{
		
		
		Rule r=new Rule(ruleName);
		r.addTapButtonEqualsCondition(sensorId, button);
		r.addTapDeviceChangedCondition(sensorId);
		r.addGroupAction("0", "scene", scene);
		
		r.addTapButtonEqualsCondition(sensorId, button);
		
		hueSetRule(r.toJson());
	}

	@ActionDoc(text = "Delete all rules on Bridge. This have can serious sideeffects on Settings stored with the hue app!")
	public static String hueDeleteAllRules(){
		HueSettings settings=getHueSettings();
		SettingsTree rules=settings.getRules();
		
		
		for(String ruleId:rules.nodes()){
			resourceRequest(HttpMethod.DELETE, "rules/"+ruleId, (String)null);			
		}
		return "";
	}
	/**
	 * set rule by name
	 * @param name
	 * @param ruleJson
	 * @return
	 */
	@ActionDoc(text = "set rule")
	public static String hueSetRule(
			@ParamDoc(name = "ruleJson", text = "Json representation of the rule") String ruleJson){
		
		Rule r;
		try {
			r = Rule.create(ruleJson);
			String name=r.name;
			String id = getRuleId(name);
			
			//logger.debug("found rule id for name '"+name+"': '"+id+"'");
			logger.debug("found rule id for name '{}': '{}'", name,id);
			String result;
			if(id==null){ // create new
				result=postResource("rules",ruleJson);	
			}else{
				result=putResource("rules/"+id,ruleJson);			
			}
			return result;
		
		} catch (IOException e) {
			logger.warn("Failed to create rule from body '"+ruleJson+"'", e);
			return "";
		}

	}


	/**
	 * get ruled id for name
	 * @param name
	 * @return
	 */
	private static String getRuleId(String name) {
		HueSettings settings=getHueSettings();
		
		String id=settings.getRule(name);
		return id;
	}

	
	private static HueSettings getHueSettings() {
		return HueContext.getInstance().getBridge().getSettings();
	}
	
	
	@ActionDoc(text = "Tests Connection to hue bridge")
	public static boolean pingHue() {
		String settings=getResource("/");
		if(settings==null)return false;
		
		logger.debug("ping success");
		return true;
	}

	
	/**
	 * create a List<String> from id's seperated by , and blanks; ignore empty ones
	 * @param ids
	 * @return
	 */
	protected static List<String> splitIdString(String ids){
		
		
		String[] ii=ids.split("[,\\s]+");
		ArrayList<String> il=new ArrayList<String>();
		for(String s:ii){
			if(s.length()!=0){
				il.add(s);
			}
		}
		return  il;
	}
	
	enum HttpMethod {
		GET,
		PUT,
		POST,
		DELETE
	} ;
	
	/**
	 * get resource 
	 * @param path
	 * @return
	 */
	private static String getResource(String path){
		return resourceRequest(HttpMethod.GET,path,(String)null);
	}
	
	/**
	 * 
	 * @param path
	 * @param body
	 * @return
	 */
	private static String putResource(String path, String body){
		return resourceRequest(HttpMethod.PUT,path,body);
	}
	
	/**
	 * 
	 * @param path
	 * @param body
	 * @return
	 */
	private static String postResource(String path, String body){
		return resourceRequest(HttpMethod.POST,path,body);
	}
	
	/**
	 * 
	 * @param method
	 * @param  inside bridge w/o api+secret part, e.g. /lights/1
	 * @param resource
	 * @return
	 */
	private static String resourceRequest(HttpMethod method,String path, AbstractHueResource resource) {
		String body=null;
		if(resource!=null){
			try {
				body=resource.toJson();
			} catch (Exception e) {
				logger.warn("failed to serialize to json for path '"+path+"'",e);
				
			}		
		}
		return resourceRequest(method,path,body);
	}
	/**
	 * request resource
	 * @param method
	 * @param path inside bridge w/o api+secret part, e.g. /lights/1
	 * @param body
	 * @return
	 */
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
				case DELETE:
					response = webResource.accept("application/json").delete(ClientResponse.class);
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
				@SuppressWarnings("unchecked")
				List<Map <String,Object>> responses=mapper.readValue(responseString,List.class);
				for(Map <String,Object> item:responses){
					if(item.containsKey("error")){
						logger.warn("failed send command, url={}",url);
						logger.warn("failed send command, body={}",body);
						logger.warn("failed send command, response={}",responseString);
					}
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
