package org.openhab.binding.onewire.internal.deviceproperties.modifier;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;

/**
 * Abstract class which defines a TypeModifier for Openhab OnOffType
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
abstract public class AbstractOneWireOnOffTypeModifier implements InterfaceOneWireTypeModifier {

	/* (non-Javadoc)
	 * @see org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#modify4Read(org.openhab.core.types.Type)
	 */
	public Type modify4Read(Type pvType) {
		if (pvType == null) {
			return null;
		}

		if (pvType instanceof OnOffType) {
			return modifyOnOffType4Read((OnOffType) pvType);
		} else {
			throw new ClassCastException("unexpected class, expected: " + OnOffType.class + " type is:" + pvType.getClass());
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#modify4Write(org.openhab.core.types.Type)
	 */
	public Type modify4Write(Type pvType) {
		if (pvType == null) {
			return null;
		}

		if (pvType instanceof OnOffType) {
			return modifyOnOffType4Write((OnOffType) pvType);
		} else {
			throw new ClassCastException("unexpected class, expected: " + OnOffType.class + " type is:" + pvType.getClass());
		}
	}

	/**
	 * @param pvOnOffTypeValue
	 * @return modified Type
	 */
	abstract public OnOffType modifyOnOffType4Read(OnOffType pvOnOffTypeValue);

	/**
	 * @param pvOnOffTypeValue
	 * @return modified Type
	 */
	abstract public OnOffType modifyOnOffType4Write(OnOffType pvOnOffTypeValue);

}
