/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.resolvbus.internal;

public interface ResolVBUSReceiver extends Runnable{
	
	public void stopListener();
	public void initializeReceiver(String host, int port, String password);
	public void initializeReceiver(String serialPort, String password);
	

}
