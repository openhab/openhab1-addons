/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	 * This method sends notification message to mobile apps of all account users through my.openHAB service
	 * 
	 * @param message the {@link String} containing a message to send to specified user id
	 * @param icon the {@link String} containing a name of the icon to be used with this notification
	 * @param severity the {@link String} containing severity (good, info, warning, error) of notification
	 */
	public void sendBroadcastNotification(String message, String icon, String severity);
	
	/**
	 * This method sends notification message to notification log of all account users through my.openHAB service
	 * 
	 * @param message the {@link String} containing a message to send to specified user id
	 * @param icon the {@link String} containing a name of the icon to be used with this notification
	 * @param severity the {@link String} containing severity (good, info, warning, error) of notification
	 */
	public void sendLogNotification(String message, String icon, String severity);

	/**
	 * This method sends SMS message to mobile phones through my.openHAB service
	 * 
	 * @param userId the {@link String} containing my.openHAB user id to send message to
	 * @param message the {@link String} containing a message to send to specified user id
	 */
	public void sendSMS(String phone, String message);

	
}
