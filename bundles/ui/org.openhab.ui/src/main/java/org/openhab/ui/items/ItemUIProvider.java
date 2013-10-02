/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.ui.items;

import org.openhab.core.items.Item;
import org.openhab.model.sitemap.Widget;


/**
 * This interface describes the methods that need to be implemented by a provider that
 * wants to define the appearance of an item in the UI. 
 * 
 * @author Kai Kreuzer
 *
 */
public interface ItemUIProvider {

	/** 
	 * Returns the name of a bitmap to use as an icon. This name does not need to include
	 * state information or a file extension; if these are missing, they will be automatically added.
	 * 
	 * TODO: Note that the consumer of this provider needs to be aware of these icons, so we
	 * have some implicit dependency between them. Maybe worth refactoring?
	 * 
	 * @param itemName the name of the item to return the icon for
	 * @return the name of the icon to use or null if undefined.
	 */
	public String getIcon(String itemName);
	
	/**
	 * Returns the label text to be used for an item in the UI.
	 * 
	 * @param item the name of the item to return the label text for
	 * @return the label text to be used in the UI or null if undefined.
	 */
	public String getLabel(String itemName);
	
	/**
	 * Provides a default widget for a given item (class). This is used whenever
	 * the UI needs to be created dynamically and there is no other source
	 * of information about the widgets.
	 * 
	 * @param itemType the class of the item
	 * @param itemName the item name to get the default widget for
	 * 
	 * @return a widget implementation that can be used for the given item
	 */
	public Widget getDefaultWidget(Class<? extends Item> itemType, String itemName);
	
	/**
	 * <p>Provides a widget for a given item. This can be used to overwrite the widget
	 * listed in the sitemap. A use case for this is that the sitemap defines merely
	 * the parent-child-relation of widgets, but the concrete widget to be used for
	 * rendering might be selected dynamically at runtime.</p>
	 * <p>If the sitemap widget should not be overridden, this method must return 
	 * <code>null</code>.</p>
	 * 
	 * @param itemName the item name to get the widget for
	 * @return a widget to use for the given item or <code>null</code> if sitemap should not be overridden.
	 */
	public Widget getWidget(String itemName);
}
