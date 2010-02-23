package org.openhab.core.library.items;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.DataType;

public class SwitchItem extends GenericItem {
	
	private static List<Class<? extends DataType>> acceptedDataTypes = new ArrayList<Class<? extends DataType>>();
	static {
		acceptedDataTypes.add(OnOffType.class);
	}
	
	public SwitchItem(String name) {
		super(name);
	}

	public void send(OnOffType command) {
		internalSend(command);
	}

	protected List<Class<? extends DataType>> getAcceptedDataTypes() {
		return acceptedDataTypes;
	}
}
