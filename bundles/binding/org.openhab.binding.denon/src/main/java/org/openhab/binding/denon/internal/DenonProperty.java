/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

/**
 * Subset of the usable properties. 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public enum DenonProperty {
	
	INPUT("INPUT"), 
	SURROUND_MODE("SURROUNDMODE"),
	COMMAND("COMMAND"),
	MASTER_VOLUME("MV"),
	ZONE_VOLUME("ZV"),
	POWER("PW"),
	POWER_MAINZONE("ZM"),
	MUTE("MU"),
	ARTIST("ARTIST"),
	TRACK("TRACK"),
	ALBUM("ALBUM");
	
	private String code;
	
	private DenonProperty(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
