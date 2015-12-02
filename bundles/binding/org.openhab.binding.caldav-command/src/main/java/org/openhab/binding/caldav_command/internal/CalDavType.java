/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_command.internal;

/**
 * Item type.
 * Actions which can be performed on items.
 * 
 * @author Robert
 * @since 1.8.0
 */
public enum CalDavType {
	/**
	 * get the value from the event (triggered at BEGIN or END
	 */
	VALUE,
	/**
	 * get the date when the item will be next switched
	 */
	DATE,
	/**
	 * disables the event triggered execution
	 */
	DISABLE
}
