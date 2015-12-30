package org.openhab.binding.ipx800.internal.itemslot;

import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * Virtual dimmer item
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800Dimmer extends Ipx800Item {
	private PercentType lastState = new PercentType(0);
	private int steps = 1;

	public Ipx800Dimmer(int steps) {
		this.steps = steps;
	}
	public Ipx800Dimmer() {
	}
	
	public void increment(boolean propagate) {
		if (lastState == PercentType.HUNDRED) {
			lastState = PercentType.ZERO;
		} else {
			if (lastState.intValue() + steps >= 100) {
				lastState = PercentType.HUNDRED;
			} else {
				lastState = new PercentType(lastState.intValue() + steps);
			}
		}
		if (propagate) {
			this.postState();
		}
	}

	@Override
	public State getState() {
		return lastState;
	}

	@Override
	protected Type toState(String state) {
		return new PercentType(state);
	}

	@Override
	protected boolean updateStateInternal(Type state) {
		boolean changed = false;
		if (state instanceof PercentType) {
			PercentType commandState = (PercentType) state;
			if (!lastState.equals(commandState)) {
				changed = true;
				lastState = commandState;
			}
		}
		return changed;
	}
}
