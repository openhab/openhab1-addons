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
	 * @param item the name of the item to return the label text for
	 * @return the label text to be used in the UI or null if undefined.
	 */
	public String getLabel(String itemName);
	
	/**
	 * Provides a default widget for a given item (class). This is used whenever
	 * the UI needs to be created dynamically and there is no other source
	 * of information about the widgets.
	 * @param itemType the class of the item
	 * @param itemName the item name to get the default widget for
	 * 
	 * @return a widget implementation that can be used for the given item
	 */
	public Widget getDefaultWidget(Class<? extends Item> itemType, String itemName);
}
