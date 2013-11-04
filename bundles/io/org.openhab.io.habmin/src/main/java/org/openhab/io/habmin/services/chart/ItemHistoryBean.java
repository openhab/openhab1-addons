/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.habmin.services.chart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
//import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * This is a java bean that is used with JAXB to serialize items
 * to XML or JSON.
 *  
 * @author Chris Jackson
 * @since 1.3.0
 *
 */
@XmlRootElement(name="history")
public class ItemHistoryBean {

	public String name;
	public String type;
	public String totalrecords;
	public String link;

	public String icon;
	public String label;
	public String units;
	public String format;
	public String map;
	
	public List<String> groups;
	public List<String> services;	
	
	public String timestart;
	public String timeend;
	public String statemax;
	public String timemax;
	public String statemin;
	public String timemin;
	public String stateavg;
	public String datapoints;

	public List<HistoryDataBean> data;
	
	public ItemHistoryBean() {};

	public void addData(String time, String value) {
		if(data == null)
			data = new ArrayList<HistoryDataBean>();
		HistoryDataBean newVal = new HistoryDataBean();
		newVal.time = time;
		newVal.state = value;
		data.add(newVal);
	}
	
//	@JsonSerialize(using = ItemHistoryBean.JsonHistorySerializer.class)
	public static class HistoryDataBean {
//		@XmlAttribute
		public String time;
		
//		@XmlValue
		public String state;
	}

	public class JsonHistorySerializer extends JsonSerializer<HistoryDataBean>{

		@Override
		public void serialize(HistoryDataBean history, JsonGenerator gen, SerializerProvider provider) throws IOException,
				JsonProcessingException {
			String jsonHistory = new String("["+history.time+","+history.state+"]");			
	        gen.writeString(jsonHistory);
		}
	 
	}
}
