/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gpio.linux;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * JNA structure representing native linux 'pollfd' structure.
 *
 * @author Dancho Penev
 * @since 1.5.0
 */
public class pollfd extends Structure {

	/** File descriptor to poll. */
	public int fd;
	/** Types of events poller cares about. */
	public short events;
	/** Types of events that actually occurred. */
	public short revents;

	/** Initializes "pollfd" structure with zeroes. */
	public pollfd() {
		super();
		this.fd = 0;
		this.events = 0;
		this.revents = 0;
	}

	/**
	 * Initializes "pollfd" structure with supplied values.
	 * 
	 * @param fd file descriptor to poll
	 * @param events types of events poller cares about
	 * @param revents types of events that actually occurred
	 */
	public pollfd(int fd, short events, short revents) {
		super();
		this.fd = fd;
		this.events = events;
		this.revents = revents;
	}

	/**
	 * Specifies fields order.
	 * 
	 * @see com.sun.jna.Structure#getFieldOrder()
	 */
	@Override
	protected List<? > getFieldOrder() {
		return Arrays.asList("fd", "events", "revents");
	}
}
