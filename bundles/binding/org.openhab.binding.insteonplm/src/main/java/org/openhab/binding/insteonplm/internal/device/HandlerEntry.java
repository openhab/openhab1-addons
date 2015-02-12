/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.util.HashMap;

/**
 * Ugly little helper class to facilitate late instantiation of handlers
 * 
 * @author Bernd Pfrommer
 * @since 1.7.0
 */
public class HandlerEntry {
	HandlerEntry(String name, HashMap<String, String> params) {
		m_hname = name;
		m_params = params;
	}
	HashMap<String, String> m_params = null;
	String m_hname = null;

	HashMap<String, String> getParams()	{ return m_params; } 
	String getName()					{ return m_hname; }
}
