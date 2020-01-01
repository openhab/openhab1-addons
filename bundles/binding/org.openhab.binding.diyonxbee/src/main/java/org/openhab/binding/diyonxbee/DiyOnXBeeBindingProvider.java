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
package org.openhab.binding.diyonxbee;

import java.util.List;

import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.types.State;

/**
 * @author juergen.richtsfeld@gmail.com
 * @since 1.9
 */
public interface DiyOnXBeeBindingProvider extends AutoUpdateBindingProvider {

	String getId(String itemName);

	String getRemote(String itemName);

	boolean isSensor(String itemName);

	Integer getMaxValue(String itemName);

	List<Class<? extends State>> getAvailableItemTypes(String itemName);
}
