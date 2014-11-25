/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.communication;

/**
 * Class describing the pilight version and optional low and high pass filter frequencies.    
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class Firmware {
	
	private String version;
	
	private String hpf;
	
	private String lpf;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHpf() {
		return hpf;
	}

	public void setHpf(String hpf) {
		this.hpf = hpf;
	}

	public String getLpf() {
		return lpf;
	}

	public void setLpf(String lpf) {
		this.lpf = lpf;
	}

}
