/* 
 * openHAB, the open Home Automation Bus.
 * Copyright 2010, openHAB.org
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhab.ui.webapp.internal;

import java.util.Hashtable;

import javax.servlet.Filter;

import org.apache.wicket.protocol.http.WicketFilter;
import org.openhab.ui.webapp.internal.http.MainApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Extension of the default OSGi bundle activator
 */
public final class WebAppActivator implements BundleActivator {
	/**
	 * Called whenever the OSGi framework starts our bundle
	 */
	public void start(BundleContext bc) throws Exception {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("pattern", "webapp/*");
		props.put("applicationClassName", MainApplication.class.getName());
		final WicketFilter filter = new WicketFilter();
		bc.registerService(Filter.class.getName(), filter, props);
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(BundleContext bc) throws Exception {
	}
}
