package org.openhab.core.items;

import org.openhab.core.types.DataType;

public interface Item {

	public abstract DataType getState();

	public abstract String getName();

}