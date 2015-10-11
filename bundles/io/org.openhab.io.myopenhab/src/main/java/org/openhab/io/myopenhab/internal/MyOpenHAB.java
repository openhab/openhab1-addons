/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.myopenhab.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.io.myopenhab.MyOpenHABService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class provides static methods that can be used in automation rules
 * for using my.openHAB functionality. 
 * 
 * @author Victor Belov
 * @since 1.3.0
 */
public class MyOpenHAB {
	
	private static final Logger logger = LoggerFactory.getLogger(MyOpenHAB.class);
	
	static MyOpenHABService mMyOpenHABService = null;
	
	/**
	 * Sends a simple push notification to mobile devices of user
	 * 
	 * @param userId the my.openHAB user id of the recipient
	 * @param message the body of the notification
	 * 
	 */
	@ActionDoc(text="Sends a push notification to mobile devices of user with userId")
	static public void sendNotification(String userId, String message) {
		sendNotification(userId, message, null, null);
	}

	/**
	 * Sends an advanced push notification to mobile devices of user
	 * 
	 * @param userId the my.openHAB user id of the recipient
	 * @param message the body of the notification
	 * @param icon name for the notification
	 * @param severity category for the notification
	 */
	@ActionDoc(text="Sends a push notification to mobile devices of user with userId")
	static public void sendNotification(String userId, String message, String icon, String severity) {
		logger.debug("sendNotification '{}' to user {}", message, userId);
		if (mMyOpenHABService != null) {
			mMyOpenHABService.sendNotification(userId, message, icon, severity);
		}
	}
	
	/**
	 * Sends a simple notification to log. Log notifications are not pushed to user
	 * devices but are shown to all account users in notifications log
	 * 
	 * @param message the body of the notification
	 * 
	 */
	@ActionDoc(text="Sends a log notification which is shown in notifications log to all account users")
	static public void sendLogNotification(String message) {
		sendLogNotification(message, null, null);
	}

	/**
	 * Sends an advanced notification to log. Log notifications are not pushed to user
	 * devices but are shown to all account users in notifications log
	 * 
	 * @param message the body of the notification
	 * @param icon name for the notification
	 * @param severity category for the notification
	 */
	@ActionDoc(text="Sends a log notification which is shown in notifications log to all account users")
	
	static public void sendLogNotification(String message, String icon, String severity) {
		if (mMyOpenHABService != null) {
			mMyOpenHABService.sendLogNotification(message, icon, severity);
		}
	}

	/**
	 * Sends a simple broadcast notification. Broadcast notifications are pushed to all
	 * mobile devices of all users of the account
	 * 
	 * @param message the body of the notification
	 * 
	 */
	@ActionDoc(text="Sends a broadcast notification to all mobile devices of all account users")
	static public void sendBroadcastNotification(String message) {
		sendBroadcastNotification(message, null, null);
	}

	/**
	 * Sends an advanced broadcast notification. Broadcast notifications are pushed to all
	 * mobile devices of all users of the account
	 * 
	 * @param message the body of the notification
	 * @param icon name for the notification
	 * @param severity category for the notification
	 */
	@ActionDoc(text="Sends a broadcast notification to all mobile devices of all account users")
	
	static public void sendBroadcastNotification(String message, String icon, String severity) {
		if (mMyOpenHABService != null) {
			mMyOpenHABService.sendBroadcastNotification(message, icon, severity);
		}
	}

	/**
	 * Sends an SMS to mobile phone of user
	 * 
	 * @param phone the user's phone number in international format like +49XXXXXXXXXX
	 * @param message the body of the sms
	 */
	@ActionDoc(text="Sends an SMS to mobile phone of user")
	static public void sendSms(String phone, String message) {
		logger.debug("sendSMS()");
		if (mMyOpenHABService != null) {
			mMyOpenHABService.sendSMS(phone, message);
		}
	}

}
