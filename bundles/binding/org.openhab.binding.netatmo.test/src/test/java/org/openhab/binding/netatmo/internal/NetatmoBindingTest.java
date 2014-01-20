/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Ignore;
import org.junit.Test;
import org.openhab.core.library.items.NumberItem;

/**
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class NetatmoBindingTest {

	private static final String CLIENT_ID = "";
	private static final String CLIENT_SECRET = "";
	private static final String REFRESH_TOKEN = "";

	@Test
	@Ignore("needs real client credentials")
	public void testExecute() throws Exception {
		final Dictionary<String, String> config = new Hashtable<String, String>();
		config.put(NetatmoBinding.CONFIG_CLIENT_ID, CLIENT_ID);
		config.put(NetatmoBinding.CONFIG_CLIENT_SECRET, CLIENT_SECRET);
		config.put(NetatmoBinding.CONFIG_REFRESH_TOKEN, REFRESH_TOKEN);

		final NetatmoBinding binding = new NetatmoBinding();
		binding.updated(config);

		final NetatmoGenericBindingProvider provider = new NetatmoGenericBindingProvider();
		provider.processBindingConfiguration("netatmo.items", new NumberItem(
				"Netatmo_OfficeInParis_Bosssoffice_Temperature"),
				"70:ee:50:00:02:20#Temperature");
		provider.processBindingConfiguration("netatmo.items", new NumberItem(
				"Netatmo_OfficeInParis_NetatmoHQ_Temperature"),
				"70:ee:50:00:02:20#02:00:00:00:02:a0#Temperature");
		provider.processBindingConfiguration("netatmo.items", new NumberItem(
				"Netatmo_OfficeInParis_NetatmoHQ_Humidity"),
				"70:ee:50:00:02:20#02:00:00:00:02:a0#Humidity");
		binding.addBindingProvider(provider);

		binding.execute();
	}
}
