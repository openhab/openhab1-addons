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
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemChangeListener;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.MeasurementItem;
import org.openhab.core.library.items.RollerblindItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.sitemap.Group;
import org.openhab.model.sitemap.Selection;
import org.openhab.model.sitemap.SitemapFactory;
import org.openhab.model.sitemap.Switch;
import org.openhab.model.sitemap.Text;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIProvider;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides items by parsing a Misterhouse mht file.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class MhItemProvider implements ItemProvider, ItemUIProvider, ManagedService {
	
	private static final Logger logger = LoggerFactory.getLogger(MhItemProvider.class);
	
	/** to keep track of all item change listeners */
	private Collection<ItemChangeListener> listeners = new HashSet<ItemChangeListener>();
	
	/** the URL to the misterhouse config file */
	private URL configFileURL;
	
	public synchronized GenericItem[] getItems() {
		if(configFileURL!=null) {
			try {
				InputStream is = configFileURL.openStream();
				return MhtFileParser.parse(is);
			} catch (IOException e) {
				logger.error("Cannot read config file: " + e.getMessage());
			} catch (ParserException e) {
				logger.error("Cannot parse config file: " + e.getMessage());
			}
		} else {
			logger.debug("No config file has been set yet.");
		}
		return new GenericItem[0];
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			try {
				configFileURL = new URL("file", "", (String) config.get("mhtFile"));
			} catch (MalformedURLException e) {
				logger.error("Cannot locate config file", e);
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

	@Override
	public String getIcon(String itemName) {
		return MhtFileParser.iconMap.get(itemName);
	}

	@Override
	public String getLabel(String itemName) {
		return MhtFileParser.labelMap.get(itemName);
	}

	@Override
	public Widget getDefaultWidget(Class<? extends Item> itemType, String itemName) {
		Widget w = null;
		
		// if the itemType is not defined, try to get it from the item name
		if(itemType==null) {
			for(Item item : getItems()) {
				if(item.getName().equals(itemName)) itemType = item.getClass();
			}
			if(itemType==null) return null;
		}
		if(itemType.equals(SwitchItem.class)) {
			Switch s = SitemapFactory.eINSTANCE.createSwitch();
			s.setItem(itemName);
			w = s;
		}
		if(itemType.equals(GroupItem.class)) {
			Group g = SitemapFactory.eINSTANCE.createGroup();
			g.setItem(itemName);
			w = g;
		}
		if(itemType.equals(MeasurementItem.class)) {
			Text t = SitemapFactory.eINSTANCE.createText();
			t.setItem(itemName);
			w = t;
		}
		if(itemType.equals(ContactItem.class)) {
			Text t = SitemapFactory.eINSTANCE.createText();
			t.setItem(itemName);
			w = t;
		}
		if(itemType.equals(RollerblindItem.class)) {
			Switch s = SitemapFactory.eINSTANCE.createSwitch();
			s.setItem(itemName);
			w = s;
		}
		if(itemType.equals(StringItem.class)) {
			Selection s = SitemapFactory.eINSTANCE.createSelection();
			s.setItem(itemName);
			w = s;
		}
		return w;
	}

}
