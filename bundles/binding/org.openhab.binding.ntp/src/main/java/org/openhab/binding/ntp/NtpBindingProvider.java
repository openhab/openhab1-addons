/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ntp;

import java.util.Locale;
import java.util.TimeZone;

import org.openhab.core.binding.BindingProvider;



/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and NTP data.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.8.0
 */
public interface NtpBindingProvider extends BindingProvider {
		
	/**
	 * Returns the configured TimeZone for the given <code>itemName</code>. If
	 * no TimeZone has been configured or the code is unknown the default TimeZone
	 * of the System is returned instead.
	 * 
	 * @param itemName the Item to find the TimeZone for
	 * @return the configured TimeZone or the TimeZone of the System if no TimeZone
	 * has been configured or the code is unknown
	 */
	public TimeZone getTimeZone(String itemName);
	
	/**
	 * Returns the configured Locale for the given <code>itemName</code>. If
	 * no Locale has been configured or the code is unknown the default Locale
	 * of the System is returned instead.
	 * 
	 * @param itemName the Item to find the Locale for
	 * @return the configured Locale or the Locale of the System if no Locale
	 * has been configured or the code is unknown
	 */
	public Locale getLocale(String itemName);
	
}
