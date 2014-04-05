/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.config;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public enum ContextConfig {
	
	slat,			// important if it's a roller shutter: to have a item to control the slats
	awning,			// important for roller shutter: if it's a marquee/awning to show the right icon -> open-close 
	apartment,		// to use a Number or StringItem to make apartment calls
	zone			// to use a Number or StringItem to make zone calls

}
