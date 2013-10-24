/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-${year}, openHAB.org <admin@openhab.org>
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


package org.openhab.io.gpio.linux;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * JNA structure representing native linux 'pollfd' structure.
 *
 * @author Dancho Penev
 * @since 1.3.1
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
