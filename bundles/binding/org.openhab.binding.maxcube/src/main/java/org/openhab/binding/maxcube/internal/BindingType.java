/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

/**
* This enumeration represents the different binding types provided by the MAX!Cube generic binding. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public enum BindingType {
	//All devices
	BATTERY("battery"),
	MODE("mode"), 
	ACTUAL("actual"),
	TEMPCOMFORT("tempcomfort"),
	TEMPECO("tempeco"),
	TEMPSETPOINTMAX("tempsetpointmax"),
	TEMPSETPOINTMIN("tempsetpointmin"),
	TEMPOFFSET("tempoffset"),
	TEMPOPENWINDOW("tempopenwindow"),
	DURATIONOPENWINDOW("durationopenwindow"),
	PROGRAMDATA("programdata"),
	BOOSTDURATION("boostduration"),
	BOOSTVALVE("boostvalve"),
	VALVE("valve"),
	DECALCIFICATION("decalcification"),
	VALVEMAXIMUM("valvemaximum"),
	VALVEOFFSET("valveoffset");
	
	/** The name that have to be configured in items.config*/
	private String configValue;
	
	/** Constructor to provide the String value, that has to be used in items.config */
	BindingType(String configValue) {
		this.configValue = configValue;
	}
	
	public String getConfigValue() {
		return this.configValue;
	}
	
	@Override
	public String toString() {
		return configValue;
	}
}