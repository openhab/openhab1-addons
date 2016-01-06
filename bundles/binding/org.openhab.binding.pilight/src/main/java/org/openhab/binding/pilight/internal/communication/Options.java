/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.communication;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.openhab.binding.pilight.internal.serializers.BooleanToIntegerSerializer;

/**
 * Options that can be set as a pilight client.      
 * 
 * @author Jeroen Idserda
 * @since 1.7
 */
public class Options {
	
	public static String MEDIA_ALL = "all";
	
	public static String MEDIA_WEB = "web";
	
	public static String MEDIA_MOBILE = "mobile";
	
	public static String MEDIA_DESKTOP = "desktop";

	@JsonSerialize(using = BooleanToIntegerSerializer.class, include=Inclusion.NON_NULL)
	private Boolean core;
	
	@JsonSerialize(using = BooleanToIntegerSerializer.class, include=Inclusion.NON_NULL)
	private Boolean receiver;
	
	@JsonSerialize(using = BooleanToIntegerSerializer.class, include=Inclusion.NON_NULL)
	private Boolean config;
	
	@JsonSerialize(using = BooleanToIntegerSerializer.class, include=Inclusion.NON_NULL)
	private Boolean forward;
	
	@JsonSerialize(using = BooleanToIntegerSerializer.class, include=Inclusion.NON_NULL)
	private Boolean stats;
	
	private String uuid;
	
	private String media;
	
	public Boolean getCore() {
		return core;
	}

	public void setCore(Boolean core) {
		this.core = core;
	}

	public Boolean getReceiver() {
		return receiver;
	}

	public void setReceiver(Boolean receiver) {
		this.receiver = receiver;
	}

	public Boolean getConfig() {
		return config;
	}

	public void setConfig(Boolean config) {
		this.config = config;
	}

	public Boolean getForward() {
		return forward;
	}

	public void setForward(Boolean forward) {
		this.forward = forward;
	}

	public Boolean getStats() {
		return stats;
	}

	public void setStats(Boolean stats) {
		this.stats = stats;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}
	
}
