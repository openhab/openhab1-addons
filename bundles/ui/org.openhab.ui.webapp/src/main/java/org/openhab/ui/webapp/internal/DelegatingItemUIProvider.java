/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.ui.webapp.internal;

import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.sitemap.SitemapFactory;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIProvider;

/**
 * This class provides a simple way to ask different item providers by a 
 * single method call, i.e. the consumer does not need to iterate over all
 * registered providers as this is done inside this class.
 * 
 * @author Kai Kreuzer
 * @since 0.2.0
 *
 */
public class DelegatingItemUIProvider implements ItemUIProvider {
	
	private final WebAppService service;
	
	public DelegatingItemUIProvider(WebAppService webAppService) {
		this.service = webAppService;
	}

	@Override
	public String getIcon(String itemName) {		
		for(ItemUIProvider provider : service.getItemUIProviders()) {
			String currentIcon = provider.getIcon(itemName);
			if(currentIcon!=null) {
				return currentIcon;
			}
		}
		// do some reasonable default, if no provider had an answer
		// try to get the item type from the item name
		Class<? extends Item> itemType = getItemType(itemName);
		if(itemType==null) return null;
		
		// we handle items here that have no specific widget,
		// e.g. the default widget of a rollerblind is "Switch".
		// We want to provide a dedicated default icon for it
		// like "rollerblind".
		if (itemType.equals(NumberItem.class) ||
				itemType.equals(ContactItem.class) ||
				itemType.equals(RollershutterItem.class)) {
			return itemType.getSimpleName().replace("Item", "").toLowerCase();
		}
		return null;
	}

	@Override
	public String getLabel(String itemName) {
		for(ItemUIProvider provider : service.getItemUIProviders()) {
			String currentLabel = provider.getLabel(itemName);
			if(currentLabel!=null) {
				return currentLabel;
			}
		}
		return null;
	}

	@Override
	public Widget getDefaultWidget(Class<? extends Item> itemType, String itemName) {
		for(ItemUIProvider provider : service.getItemUIProviders()) {
			Widget widget = provider.getDefaultWidget(itemType, itemName);
			if(widget!=null) {
				return widget;
			}
		}

		// do some reasonable default, if no provider had an answer
		// if the itemType is not defined, try to get it from the item name
		if(itemType==null) {
			itemType = getItemType(itemName);
		}
		if(itemType==null) return null;
		
		if (itemType.equals(SwitchItem.class)) {
			return SitemapFactory.eINSTANCE.createSwitch();
		}
		if (itemType.equals(GroupItem.class)) {
			return SitemapFactory.eINSTANCE.createGroup();
		}
		if (itemType.equals(NumberItem.class)) {
			return SitemapFactory.eINSTANCE.createText();
		}
		if (itemType.equals(ContactItem.class)) {
			return SitemapFactory.eINSTANCE.createText();
		}
		if (itemType.equals(RollershutterItem.class)) {
			return SitemapFactory.eINSTANCE.createSwitch();
		}
		if (itemType.equals(StringItem.class)) {
			return SitemapFactory.eINSTANCE.createText();
		}

		return null;
	}

	private Class<? extends Item> getItemType(String itemName) {
		try {
			Item item = service.getItemRegistry().getItem(itemName);
			return item.getClass();
		} catch (ItemNotFoundException e) {
			return null;
		} catch (ItemNotUniqueException e) {
			return null;
		}
	}

}
