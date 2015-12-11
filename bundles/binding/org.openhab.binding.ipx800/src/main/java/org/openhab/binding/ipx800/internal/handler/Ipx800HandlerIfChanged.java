package org.openhab.binding.ipx800.internal.handler;

import java.util.Map;

import org.openhab.binding.ipx800.internal.itemslot.Ipx800Item;

/**
 * This handler update items if the state changed
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800HandlerIfChanged implements Ipx800Handler {
	/** Previous state of this port as string */
	private String lastState = "";
	@Override
	public boolean updateState(Map<String, Ipx800Item> items, String state) {
		boolean changed = false;
		if (!state.equals(lastState)) {
			for (Ipx800Item itemSlot : items.values()) {
				changed |= itemSlot.updateState(state);
			}
			lastState = state;
		}
		return changed;
	}
}
