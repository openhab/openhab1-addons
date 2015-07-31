/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.myhome.fcrisciani.datastructure.command;

/**
 * This command represent a delay between one command and another one. It is
 * very useful if you want to create automation with delay: i.e. open the tent
 * for 10s and then stop. In the example the definition of the delay time is
 * done using this class
 * 
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class DelayInterval extends CommandOPEN {
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //

	private long delayInMillisecond = 0; // Delay time in milliseconds

	// ---- METHODS ---- //

	/**
	 * Create a delay instance
	 * 
	 * @param who
	 *            of the actuator
	 * @param where
	 *            of the actuator
	 * @param delayInMillisecond
	 *            delay to wait express in milliseconds [ms]
	 */
	public DelayInterval(String who, String where, long delayInMillisecond) {
		super("", 6, who, where);
		if (delayInMillisecond < 0) {
			delayInMillisecond = 0;
		}
		this.delayInMillisecond = delayInMillisecond;
	}

	/**
	 * Get command delay
	 * 
	 * @return the delay
	 */
	public long getDelayInMillisecond() {
		return delayInMillisecond;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DelayInterval [delayInMillisecond=");
		builder.append(delayInMillisecond);
		builder.append("]");
		return builder.toString();
	}

}
