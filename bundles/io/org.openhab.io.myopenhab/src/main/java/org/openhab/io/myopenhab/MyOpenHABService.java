/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.io.myopenhab;


/**
 * This interface defines how to use MyOpenHAB service connection
 * 
 * @author Victor Belov
 * @since 1.3.0
 */
public interface MyOpenHABService {
	
	/**
	 * This method sends notification message to mobile app through my.openHAB service
	 * 
	 * @param userId the {@link String} containing my.openHAB user id to send message to
	 * @param message the {@link String} containing a message to send to specified user id
	 * @param icon the {@link String} containing a name of the icon to be used with this notification
	 * @param severity the {@link String} containing severity (good, info, warning, error) of notification
	 */
	public void sendNotification(String userId, String message, String icon, String severity);

	/**
	 * This method sends SMS message to mobile phones through my.openHAB service
	 * 
	 * @param userId the {@link String} containing my.openHAB user id to send message to
	 * @param message the {@link String} containing a message to send to specified user id
	 */
	public void sendSMS(String phone, String message);

	
}
