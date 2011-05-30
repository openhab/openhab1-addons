/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.ntp;

import java.util.Locale;
import java.util.TimeZone;

import org.openhab.core.binding.BindingProvider;



/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and NTP items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.8.0
 */
public interface NtpBindingProvider extends BindingProvider {
	
	/**
	 * Specifies the Type of the queried DateTime-Object which will be posted on
	 * the internal eventbus.This differentiation is especially necessary for 
	 * the KNX binding.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 */
	public static enum DateTimeModus {
		DATE, TIME;
	}

	/**
	 * Returns whether the given itemName should represent a Date or a Time. This
	 * differentiation is especially necessary for the KNX binding.
	 *   
	 * @param itemName the Item to find the {@link DateTimeModus} for
	 * @return the type of the value which will be posted on the event bus
	 */
	public DateTimeModus getModus(String itemName);
	
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
	
	/**
	 * Returns all items which are mapped to a NTP-Binding
	 * @return item which are mapped to a NTP-Binding
	 */
	Iterable<String> getItemNames();
	
}
