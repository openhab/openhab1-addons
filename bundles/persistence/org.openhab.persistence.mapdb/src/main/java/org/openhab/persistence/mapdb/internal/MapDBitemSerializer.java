/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.mapdb.internal;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import org.mapdb.Serializer;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * Serializer to serialize items to and from Mapdb format
 *
 * @author Jens Viebig
 * @since 1.7.0
 *
 */
public class MapDBitemSerializer implements Serializer<MapDBItem>, Serializable {

	private static final long serialVersionUID = 1L;

	public MapDBitemSerializer() {
	}

	@Override
	public void serialize(DataOutput out, MapDBItem item) throws IOException {
		out.writeUTF(item.getName());
		out.writeUTF(item.getState().getClass().getSimpleName());
		out.writeUTF(item.getState().toString());
		out.writeLong(item.getTimestamp().getTime());
	}

	@Override
	public MapDBItem deserialize(DataInput in, int available)
			throws IOException {
		MapDBItem item = new MapDBItem();
		item.setName(in.readUTF());
		String stateType = in.readUTF();

		String stateStr = in.readUTF();

		State state = null;

		if ("DecimalType".equals(stateType)) {
			state = DecimalType.valueOf(stateStr);
		} else if ("HSBType".equals(stateType)) {
			state = HSBType.valueOf(stateStr);
		} else if ("PercentType".equals(stateType)) {
			state = PercentType.valueOf(stateStr);
		} else if ("OnOffType".equals(stateType)) {
			state = OnOffType.valueOf(stateStr);
		} else if ("OpenClosedType".equals(stateType)) {
			state = OpenClosedType.valueOf(stateStr);
		} else if ("DateTimeType".equals(stateType)) {
			state = DateTimeType.valueOf(stateStr);
		} else
			state = StringType.valueOf(stateStr);

		item.setState(state);
		item.setTimestamp(new Date(in.readLong()));
		return item;
	}

	@Override
	public int fixedSize() {
		return -1;
	}

}
