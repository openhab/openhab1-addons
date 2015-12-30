package org.openhab.binding.ipx800.internal.itemslot;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;
import org.openhab.core.types.State;

/**
 * Output item
 * @author Seebag
 * @since 1.8.0
 */
public class Ipx800OutputItem extends Ipx800AstableSwitch {
	private boolean pulseMode = false;
	private Ipx800Item fromItem = null;

	public Ipx800OutputItem() {
	}
	
	public Ipx800OutputItem(boolean pulseMode) {
		this.pulseMode = pulseMode;
	}
	
	public boolean isPulseMode() {
		return pulseMode;
	}
	
	public void setFromItem(Ipx800Item fromItem) {
		this.fromItem = fromItem;
	}
	
	public void updateAndSend(State state) {
		//lastState = state; // Not posted ?
		switchState(false);
		getPort().getDevice().setOutput(this);
	}
	
	@Override
	protected boolean updateStateInternal(Type state) {
		boolean changed = false;
		if (state instanceof OnOffType) {
			OnOffType commandState = (OnOffType) state;
			if (!lastState.equals(commandState)) {
				changed = true;
				lastState = commandState;
			}
		}
		if (changed && fromItem != null) {
			fromItem.updateStateToCore(lastState);
		}
		return changed;
	}

}
