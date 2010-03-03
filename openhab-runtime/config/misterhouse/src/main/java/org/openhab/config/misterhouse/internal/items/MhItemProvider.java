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
package org.openhab.config.misterhouse.internal.items;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;

import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemChangeListener;
import org.openhab.core.items.ItemProvider;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * This class provides items by parsing a Misterhouse mht file.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class MhItemProvider implements ItemProvider, ManagedService {
	
	/** to keep track of all item change listeners */
	private Collection<ItemChangeListener> listeners = new HashSet<ItemChangeListener>();
	
	/** the URL to the misterhouse config file */
	private URL configFileURL;
	
	public GenericItem[] getItems() {
		if(configFileURL!=null) {
			try {
				InputStream is = configFileURL.openStream();
				return MhtFileParser.parse(is);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserException e) {
				e.printStackTrace();
			}
		}
		return new GenericItem[0];
	}

	@SuppressWarnings({ "rawtypes" })
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			try {
				configFileURL = new URL("file", "", (String) config.get("mhtFile"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		notifyListeners();
	}

	/** 
	 * notifies all listeners that they should reload the complete set of items 
	 */
	private void notifyListeners() {
		for(ItemChangeListener listener : listeners) {
			listener.allItemsChanged(this);
		}
	}

	public void addItemChangeListener(ItemChangeListener listener) {
		listeners.add(listener);
	}

	public void removeItemChangeListener(ItemChangeListener listener) {
		listeners.remove(listener);
	}
}
