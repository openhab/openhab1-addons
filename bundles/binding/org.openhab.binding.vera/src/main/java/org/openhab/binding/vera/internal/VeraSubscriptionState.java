/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal;

/**
 * The enumeration of the valid states a subscription can be in.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public enum VeraSubscriptionState {
	
	Unsubscribed, 	// initial state
	Subscribing,	// in the process of subscribing
	Subscribed,		// currently subscribed
	Rescheduled;	// currently waiting to re-subscribe
	
}
