/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.fatekplc;

import org.openhab.binding.fatekplc.items.FatekPLCItem;
import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider interface for Fatek PLC
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public interface FatekPLCBindingProvider extends BindingProvider {

	/**
	 * Get Fatek PLC item by item name
	 * @param name item name
	 * @return FatekPLCItem
	 */
	FatekPLCItem geFatektItem(String name);

}
