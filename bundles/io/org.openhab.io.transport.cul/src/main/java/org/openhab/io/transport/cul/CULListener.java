/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul;

/**
 * Listen to received events from the CUL. These events can be either received
 * data or an exception thrown while trying to read data.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public interface CULListener {

	public void dataReceived(String data);

	public void error(Exception e);

}
