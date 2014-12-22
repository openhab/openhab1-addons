/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.carddav.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides static methods that can be used in automation rules for
 * resolving contacts from carddav.
 * 
 * @author Robert Delbrück
 * @since 1.6.0
 */
public class Carddav {
	
	private static CarddavActionService instance;

	private static final Logger logger = LoggerFactory.getLogger(Carddav.class);

	/**
	 * Resolves a contact from a telephone number
	 * 
	 * @param phone phone number to resolve
	 * 
	 * @return the <code>Contact</code>
	 */
	@ActionDoc(text="Resolves a contact from a telephone number", 
			returns="the contact")
	public static Contact resolveContactByPhoneNumber(@ParamDoc(name="phone", text="phone number") String phone) {
		if (!CarddavActionService.isProperlyConfigured) {
			logger.debug("Carddav client is not yet configured > execution aborted!");
			return null;
		}
		
		Contact contact = instance.foundContactByPhone(phone);
		return contact;
	}
	
	/**
	 * Resolves a name from a telephone number
	 * 
	 * @param phone phone number to resolve
	 * 
	 * @return the <code>Contact</code>
	 */
	@ActionDoc(text="Resolves a contact from a telephone number", 
			returns="the contact")
	public static String resolveNameByPhoneNumber(@ParamDoc(name="phone", text="phone number") String phone) {
		if (!CarddavActionService.isProperlyConfigured) {
			logger.debug("Carddav client is not yet configured > execution aborted!");
			return null;
		}
		
		Contact contact = instance.foundContactByPhone(phone);
		if (contact == null) {
			return phone;
		}
		return contact.getFullName();
	}
	
	/**
	 * 
	 * @param latitude part of location
	 * @param longitude part of location
	 * @return string representation of contact
	 */
	@ActionDoc(text="Resolves a contact from a location", 
			returns="string representation of contact")
	public static String resolveContactByLocation(@ParamDoc(name="latitude", text="latitude of location") String latitude, @ParamDoc(name="longitude", text="longitude of location") String longitude) {
		if (!CarddavActionService.isProperlyConfigured) {
			logger.debug("Carddav client is not yet configured > execution aborted!");
			return null;
		}
		
		logger.debug("searching for contact... (latitude={}, longitude={}", latitude, longitude);
		
		Address address = instance.foundAddress(latitude, longitude);
		if (address == null) {
			return latitude + ", " + longitude;
		}
		return address.getContact().getFullName() + " [" + address.getLabel() + "]";
	}

	public static CarddavActionService getInstance() {
		return instance;
	}

	public static void setInstance(CarddavActionService instance) {
		Carddav.instance = instance;
	}
	
	
}
