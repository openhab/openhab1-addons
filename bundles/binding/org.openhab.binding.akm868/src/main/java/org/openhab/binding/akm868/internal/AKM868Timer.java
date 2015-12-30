/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.akm868.internal;

/**
 * This is self composed Timer
 * 
 * @author Michael Heckmann
 * @since 1.8.0
 */
public class AKM868Timer {

	private long startTime;
	private int id;

	public AKM868Timer(int id) {
		this.id = id;
		startTime = System.currentTimeMillis();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long duration() {
		long now = System.currentTimeMillis();
		return now - startTime;
	}

	public void restart() {
		startTime = System.currentTimeMillis();
	}

	public boolean hasTimedOut(long timeout) {
		long now = System.currentTimeMillis();
		long difference = now - startTime;
		if (difference > timeout) {
			return true;
		} else {
			return false;
		}
	}

}
