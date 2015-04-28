package org.openhab.binding.ulux.internal.ump;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public interface UluxDatagram {

	ByteBuffer prepareBuffer();

	void send(DatagramChannel channel);

}
