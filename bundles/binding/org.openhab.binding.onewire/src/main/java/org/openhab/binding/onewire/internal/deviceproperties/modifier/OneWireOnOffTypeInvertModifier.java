package org.openhab.binding.onewire.internal.deviceproperties.modifier;

import org.openhab.core.library.types.OnOffType;

/**
 * This InvertModifier inverts the given Value to the opposite for OnOffType.
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireOnOffTypeInvertModifier extends AbstractOneWireOnOffTypeModifier {

	/* (non-Javadoc)
	 * @see org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#getModifierName()
	 */
	public String getModifierName() {
		return "Invert OnOffType modifier for OnOffType";
	}

	@Override
	public OnOffType modifyOnOffType4Read(OnOffType pvOnOffTypeValue) {
		if (pvOnOffTypeValue == null) {
			return null;
		} else if (pvOnOffTypeValue.equals(OnOffType.ON)) {
			return OnOffType.OFF;
		} else if (pvOnOffTypeValue.equals(OnOffType.OFF)) {
			return OnOffType.ON;
		} else {
			throw new IllegalStateException("Unknown OnOffType state:" + pvOnOffTypeValue.toString());
		}
	}

	@Override
	public OnOffType modifyOnOffType4Write(OnOffType onOffTypeValue) {
		return modifyOnOffType4Read(onOffTypeValue);
	}
}
