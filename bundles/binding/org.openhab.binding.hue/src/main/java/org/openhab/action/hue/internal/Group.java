package org.openhab.action.hue.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openhab.action.hue.AbstractHueResource;

public class Group extends AbstractHueResource {

	private List<String> lights=new ArrayList<String>();

	/**
	 * create group w/p lights
	 * @param name
	 */
	public Group(String name) {
		this.name=name;
	}

	public List<String> getLights() {
		return lights;
	}

	public void setLights(List<String> lights) {
		this.lights = lights;
	}

	@JsonProperty
	String name;	
	
	/**
	 * create Rule from json description
	 * @param json
	 * @return
	 * @throws IOException 
	 */
	@JsonIgnore
	public static Group create(String json) throws IOException{		
		return (Group) create(json,Group.class);		
	}
}
