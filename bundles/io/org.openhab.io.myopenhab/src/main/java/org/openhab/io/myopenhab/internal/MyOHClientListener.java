/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.myopenhab.internal;

/** 
 * This interface provides callbacks from MyOpenHABClient
 * 
 * @author Victor Belov
 * @since 1.7.0
 *
 */

public interface MyOHClientListener {
	/**
	 * This method receives command for an item from my.openHAB client and should post it
	 * into openHAB
	 * 
	 * @param item the {@link String} containing item name
	 * @param command the {@link String} containing a command
	 */
	public void sendCommand(String item, String command);
}
