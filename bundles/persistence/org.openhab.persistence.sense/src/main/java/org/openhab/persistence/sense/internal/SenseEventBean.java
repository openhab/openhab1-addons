/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.sense.internal;


/**
 * This is a standard Java bean used as an input to the JSON serializer.
 * 
 * @author Kai Kreuzer
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class SenseEventBean {

	private String feedId;
	private String value;
	
	public SenseEventBean(String feedId, String eventValue) {
		this.feedId = feedId;
		this.value = eventValue;
	}

	public String getFeedId() {
		return this.feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String eventValue) {
		this.value = eventValue;
	}
	
}
