package org.openhab.binding.noolite.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.noolite.internal.noolite4j.PC11xx;

public class TXChoose extends PC11xx{
	
	public TXChoose(byte channels) {

        super();

        Map<Short, ?> devices = new HashMap<Short, Object>(channels);
        availableChannels = channels;
    }

}
