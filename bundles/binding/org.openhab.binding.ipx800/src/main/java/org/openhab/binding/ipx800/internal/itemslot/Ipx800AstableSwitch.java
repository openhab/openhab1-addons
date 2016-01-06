package org.openhab.binding.ipx800.internal.itemslot;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;

/**
 * Astable switch item
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800AstableSwitch extends Ipx800Item {
	
	public Ipx800AstableSwitch() {
		lastState = OnOffType.OFF;
	}
	
	@Override
	public OnOffType getState() {
		return (OnOffType) lastState;
	}

	@Override
	protected boolean updateStateInternal(Type state) {
		boolean changed = false;
		if (state instanceof OnOffType) {
			OnOffType commandState = (OnOffType) state;
			if (commandState == OnOffType.ON) {
				switchState(false);
				changed = true;
			}
		}
		return changed;
	}
	
	public OnOffType switchState(boolean propagate) {
		lastState = lastState == OnOffType.ON ? OnOffType.OFF : OnOffType.ON;
		if (propagate) {
			this.postState();
			this.sendToOutput();
		}
		return getState();
	}

	@Override
	protected Type toState(String state) {
		return state.charAt(0) == '1' ? OnOffType.ON : OnOffType.OFF;
	}
}
