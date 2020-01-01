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
package org.openhab.binding.cardio2e;

import java.util.Map;

import org.openhab.binding.cardio2e.internal.Cardio2eGenericBindingProvider.Cardio2eBindingConfig;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransaction;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Manuel Alberto Guerrero Díaz
 * @since 1.11.0
 */
public interface Cardio2eBindingProvider extends BindingProvider {
	/**
	 * @param itemName
	 *            The name of the item
	 * @return The item with the given name
	 */
	Item getItem(String itemName);

	/**
	 * @param itemName
	 *            The name of the item
	 * @return Cardio2eBindingConfig mapped to the item with the given name
	 */
	Cardio2eBindingConfig getConfig(String itemName);

	/**
	 * @param itemName
	 *            The name of the item
	 * @return Reverse Order Cardio2eBindingConfig mapped to the item with the
	 *         given name
	 */
	Cardio2eBindingConfig getReverseOrderConfig(String itemName);

	/**
	 * @param transaction
	 *            A valid Cardio2eTransaction
	 * @return A Map that contains only the BindingConfigs with
	 *         Cardio2eTransaction like "transaction" and their IntemNames
	 */
	Map<String, BindingConfig> getMatchBindingConfigs(
			Cardio2eTransaction transaction);
}
