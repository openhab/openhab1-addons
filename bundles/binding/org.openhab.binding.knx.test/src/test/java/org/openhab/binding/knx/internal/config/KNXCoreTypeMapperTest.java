package org.openhab.binding.knx.internal.config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.knx.internal.dpt.KNXCoreTypeMapper;
import org.openhab.core.types.Type;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXFormatException;

public class KNXCoreTypeMapperTest {

	private KNXCoreTypeMapper typeMapper;

	@Before
	public void init() throws KNXFormatException {
		typeMapper = new KNXCoreTypeMapper();
	}

	@Test
	public void testTypeMapping() throws KNXFormatException {

		// TODO: I don't have any idea yet on what the bytes from KNX are
		// exactly for which datapoints. Will have to refine this.
		byte[] data = new byte[] { 0xF, 0xF };

		Type type = typeMapper.toType(createDP("5.001"), data);
		assertNotNull(type);
		assertEquals("6", type.toString());

		type = typeMapper.toType(createDP("5.004"), data);
		assertNotNull(type);
		assertEquals("15", type.toString());
	}
	
	private Datapoint createDP(String dpt) throws KNXFormatException {
		return new CommandDP(new GroupAddress("1/2/3"), "test", 0, dpt);
	}
}
