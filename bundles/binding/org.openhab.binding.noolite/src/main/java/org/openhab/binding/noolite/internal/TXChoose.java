package org.openhab.binding.noolite.internal;

/**
 * This class is responsible for choosing PC dongle type.
 * 
 * @author Petr Shatsillo
 * @since 1.0.0
 */

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
