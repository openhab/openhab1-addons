package org.openhab.binding.ipx800.internal.itemslot;

import org.openhab.binding.ipx800.internal.Ipx800Binding;
import org.openhab.binding.ipx800.internal.command.Ipx800Port;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * Ipx800items is an extension of an openhab item. Foreach openhab items using ipx800 binding, one Ipx800Item is created.
 * This additionnal layer is mandatory as we would like to get special items like DoubleClic or Power Consumption items.
 * -> The change of an ipx800 input doesn't directly change the state of an openhab item state.
 * So this layer keep item state.
 * @author Seebag
 * @since 1.8.0
 */
public abstract class Ipx800Item {
	protected State lastState;
	private Ipx800Item toItem = null;
	protected Ipx800Binding binding;
	private String itemName;
	/** The port of which item belongs to */
	private Ipx800Port port;
	public abstract State getState();
	
	protected abstract Type toState(String state);
	
	public Ipx800Item() {
	}
	
	/**
	 * This method stop the current background tasks
	 */
	public void destroy() {
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	protected abstract boolean updateStateInternal(Type state);
	
	public boolean updateState(String state) {
		return updateState(toState(state));
	}
	
	public boolean updateState(Type state) {
		boolean changed = updateStateInternal(state);
		if (changed) {
			sendToOutput();
			postState();
		}
		return changed;
	}
	// FIXME migrate to these methods
	public boolean updateStateToCore(Type state) {
		boolean changed = updateStateInternal(state);
		if (changed) {
			postState();
		}
		return changed;
	}
	
	public boolean updateStateToDevice(Type state) {
		boolean changed = updateStateInternal(state);
		if (changed) {
			sendToOutput();
		}
		return changed;
	}
	
	protected void sendToOutput() {
		if (toItem != null) {
			if (toItem instanceof Ipx800OutputItem) {
				((Ipx800OutputItem) toItem).updateAndSend(lastState);
			}
		}
	}
	
	public void setBinding(Ipx800Binding binding) {
		this.binding = binding;
	}
	
	public boolean needsRefresh() {
		return false;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	protected void postState() {
		this.binding.postUpdate(this);
	}

	public void setToItem(Ipx800Item toItem) {
		this.toItem = toItem;
	}
	
	public void setState(State st) {
		this.lastState = st;
	}

	public void setPort(Ipx800Port ipx800Port) {
		this.port = ipx800Port;
	}
	
	public Ipx800Port getPort() {
		return port;
	}
}
