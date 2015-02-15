package org.openhab.action.hue.internal;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.openhab.action.hue.AbstractHueResource;

/**
 * represents a Scene
 * @author gernot
 *
 */
public class Scene extends AbstractHueResource {
	@JsonProperty
	String name;
	
	@JsonProperty
	List<String> lights;

	public Scene(String name, List<String> lights) {
		super();
		this.name = name;
		this.lights = lights;
	}
	
}
