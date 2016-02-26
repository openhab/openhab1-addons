/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gc100ir;

import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider interface. Defines how to get properties from a binding
 * configuration.
 * 
 * @author Parikshit Thakur & Team
 * @since 1.6.0
 */
public interface GC100IRBindingProvider extends BindingProvider {
	String getGC100Instance(String itemname);

	int getGC100Module(String itemname);

	int getGC100Connector(String itemname);

	String getCode(String itemname);
}
