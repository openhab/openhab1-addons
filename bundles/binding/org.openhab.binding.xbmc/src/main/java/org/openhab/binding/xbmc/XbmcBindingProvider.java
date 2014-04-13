/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc;

import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider interface. Defines how to get properties from a binding configuration.
 *  
 * @author tlan, Ben Jones
 * @since 1.5.0
 */
public interface XbmcBindingProvider extends BindingProvider {
	String getXbmcInstance(String itemname);
	String getProperty(String itemname);
	boolean isInBound(String itemname);
}
