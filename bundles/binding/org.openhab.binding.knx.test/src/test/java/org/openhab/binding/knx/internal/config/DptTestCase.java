package org.openhab.binding.knx.internal.config;

import org.openhab.core.types.Type;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXFormatException;

public class DptTestCase {
	
	private final Datapoint datapoint;
	private final byte[] data;
	private final Class<? extends Type> typeClass;
	private final String value;
	
	public DptTestCase(String dpt, byte[] data, Class<? extends Type> typeClass, String value) {
		super();
		this.datapoint = createDP(dpt);
		this.data = data;
		this.typeClass = typeClass;
		this.value = value;
	}
	
	private Datapoint createDP(String dpt) {
		try {
			return new CommandDP(new GroupAddress("1/2/3"), "test", 0, dpt);	
		} catch (KNXFormatException ex) {
			throw new RuntimeException(ex);
		}
}

	public Datapoint getDatapoint() {
		return datapoint;
	}

	public byte[] getData() {
		return data;
	}

	public Class<? extends Type> getTypeClass() {
		return typeClass;
	}

	public String getValue() {
		return value;
	}
}
