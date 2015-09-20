/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 package org.openhab.binding.sunnywebbox.requests;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.monoid.web.Content;
import us.monoid.web.Resty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
 /*
 * @author Manolis Nikiforakis
 * @author Yiannis Gkoufas
 * @since 1.5.0
 */
public class GetProcessDataRequest {
	
	String version;
	String proc;
	String id;
	String format;
    HashMap<String,List<HashMap<String,String>>> params = new HashMap<String,List<HashMap<String,String>>>();
	
	private static final Logger logger = LoggerFactory
			.getLogger(GetPlantOverviewRequest.class);
	
	public GetProcessDataRequest() {
		this.version="1.0";
		this.id="1";
		this.format="JSON";
		this.proc="GetProcessData";
	}
	
	public void setDeviceIds(List<Object> ids)
	{
		List<HashMap<String,String>> devices = new ArrayList<HashMap<String,String>>();
		
		for(Object id : ids)
		{
			HashMap<String,String> keyMap = new HashMap<String,String>();
			keyMap.put("key", id.toString());
			devices.add(keyMap);
		}
		params.put("devices", devices);
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getProc() {
		return proc;
	}
	public void setProc(String proc) {
		this.proc = proc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public HashMap<String,HashMap<String,Object>> getProcessDataPerDevice(String URL){
		
		HashMap<String,HashMap<String,Object>> toRet = new HashMap<String,HashMap<String,Object>> ();
		
		Resty r = new Resty();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();		
    	String json = gson.toJson(this);
    	
//    	logger.debug("getProcessDataRequest : {}",json);
    	
    	String req = "RPC="+json;
		try {
			String response = r.json("http://"+URL+"/rpc",
					new Content("application/json",req.getBytes())).toObject().toString();
			
			logger.debug("JSON received:{}", response);
			
			List<Object> values = JsonPath.read(response, "$.result.devices[*]");
			for(Object channelForDevicekey : values){
				JSONObject jsonObj = (JSONObject) channelForDevicekey;
				JSONArray channelValues = (JSONArray) jsonObj.get("channels");
				String deviceKey = (String) jsonObj.get("key");
				
				HashMap<String,Object> valuesForDevice = new HashMap<String,Object>();
				
				for(Object channelValue : channelValues)
				{
				 JSONObject jsonObjValue = (JSONObject) channelValue;
				 String meta = (String) jsonObjValue.get("meta");
				 meta = meta.replaceAll("\\.", "");
				 
				 Object value = jsonObjValue.get("value");
				 if(meta.equals("Error")) continue;
				 
				 if(NumberUtils.isNumber(value.toString()))
				 {
				  valuesForDevice.put(meta, Double.parseDouble(value.toString())); 
				 } else {
				  valuesForDevice.put(meta, value);
				 }
				}
				valuesForDevice.put("updated", new Date());
				toRet.put(deviceKey, valuesForDevice);
			}
//			logger.debug("class is {}",values.get(0).getClass());
//			logger.debug("process data for devices {}", values);
			return toRet;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

