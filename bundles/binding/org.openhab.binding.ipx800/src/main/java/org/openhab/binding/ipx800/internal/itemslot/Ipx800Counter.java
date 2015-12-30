package org.openhab.binding.ipx800.internal.itemslot;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * Simple counter item
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800Counter extends Ipx800Item {
	private int lastState = 0;

	public Ipx800Counter() {
	}
	
	@Override
	public State getState() {
		return new DecimalType(lastState);
	}

	@Override
	protected Type toState(String state) {
		return new DecimalType(Integer.parseInt(state));
	}

	@Override
	protected boolean updateStateInternal(Type state) {
		if (state instanceof DecimalType) {
			lastState = ((DecimalType)state).intValue();
		}
		return true;
	}

}
