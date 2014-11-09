/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbcdrc;

import org.openhab.core.binding.BindingProvider;

/**
 * @author magcode
 * @since 1.6.0-SNAPSHOT
 */
public interface XBCDrcBindingProvider extends BindingProvider {
	public XBCDrcBindingConfig getConfigForRcCode(String rcCode);
}
