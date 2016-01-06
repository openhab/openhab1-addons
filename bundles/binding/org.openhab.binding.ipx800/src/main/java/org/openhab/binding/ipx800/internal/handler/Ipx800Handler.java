package org.openhab.binding.ipx800.internal.handler;

import java.util.Map;

import org.openhab.binding.ipx800.internal.itemslot.Ipx800Item;

/**
 * Generic class to handle items updates connected to an ipx800 port
 * @author Seebag
 * @since 1.8.0
 */
public interface Ipx800Handler {
	public boolean updateState(Map<String, Ipx800Item> items, String state);
}
