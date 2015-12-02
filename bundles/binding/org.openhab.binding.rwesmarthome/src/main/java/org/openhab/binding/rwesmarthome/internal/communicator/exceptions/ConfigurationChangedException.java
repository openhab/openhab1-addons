/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator.exceptions;

/**
 * Exception if the configuration has changed on the SHC in between.
 * 
 * @author ollie-dev
 *
 */
public class ConfigurationChangedException extends SHFunctionalException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4999720741275874292L;

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public ConfigurationChangedException(String message) {
		super(message);
	}
}
