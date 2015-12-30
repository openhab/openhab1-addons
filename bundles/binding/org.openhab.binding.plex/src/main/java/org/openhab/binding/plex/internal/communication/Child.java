/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal.communication;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Part of the {@link Update} object 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Child {
	
	@JsonProperty(value = "_elementType")
	private String elementType;
	
	private String key;

	private String sessionKey;
	
	private String state;
	
	private Integer viewOffset;

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getViewOffset() {
		return viewOffset;
	}

	public void setViewOffset(Integer viewOffset) {
		this.viewOffset = viewOffset;
	}

}
