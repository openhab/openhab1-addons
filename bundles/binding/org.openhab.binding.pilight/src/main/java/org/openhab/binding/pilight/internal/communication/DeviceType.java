/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.communication;

/**
 * Different types of devices in pilight 
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class DeviceType {
	
	public static Integer SERVER = -1;
	
	public static Integer SWITCH = 1;
	
	public static Integer DIMMER = 2;
	
	public static Integer VALUE = 3;

	public static Integer CONTACT = 6;

}
