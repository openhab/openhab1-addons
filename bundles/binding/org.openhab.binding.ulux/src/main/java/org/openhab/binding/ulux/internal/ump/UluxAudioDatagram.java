package org.openhab.binding.ulux.internal.ump;

import static org.openhab.binding.ulux.internal.UluxBinding.AUDIO_PORT;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

import org.openhab.binding.ulux.internal.UluxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxAudioDatagram implements UluxDatagram {

	private static final Logger LOG = LoggerFactory.getLogger(UluxAudioDatagram.class);

	private static final int BUFFER_SIZE = 904;

	private final InetAddress switchAddress;
	private final SocketAddress switchSocket;
	private final short index;
	private final byte[] audioFrame;

	public UluxAudioDatagram(final short switchId, final InetAddress switchAddress, short index, byte[] audioFrame) {
		this.switchAddress = switchAddress;
		this.index = index;
		this.audioFrame = audioFrame;

		this.switchSocket = new InetSocketAddress(this.switchAddress, AUDIO_PORT);
	}

	@Override
	public void send(DatagramChannel channel) {
		final ByteBuffer buffer = prepareBuffer();

		try {
			LOG.debug("Sending audio datagram to switch {}.", this.switchSocket);

			channel.send(buffer, this.switchSocket);
		} catch (final IOException e) {
			throw new UluxException("Could not send audio frame to switch!", e);
		}
	}

	@Override
	public ByteBuffer prepareBuffer() {
		final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		addHeader(buffer);
		addAudioFrame(buffer);

		buffer.flip();

		return buffer;
	}

	private void addHeader(ByteBuffer buffer) {
		// EtherCAT frame header
		// .... .011 1000 0110 Length: 0x0386 = 902
		// .... 0... .... .... Reserved: Valid
		// 0100 .... .... .... Type: NV
		buffer.putShort((short) 0x4386);

		final byte[] addr = switchAddress.getAddress();
		buffer.put(addr[0]);
		buffer.put(addr[1]);
		buffer.put(addr[2]);
		buffer.put(addr[3]);

		// TwinCAT Network Vars
		buffer.put((byte) 0x01); // .1
		buffer.put((byte) 0x01); // .1

		buffer.putShort((short) 0x0001); // Count, always 0x0001
		buffer.putShort(index); // Cycle index, increases every packet, seems to start with 9
		buffer.putShort((short) 0x0000); // ???, always 0x0000

		buffer.putShort((short) 0x0000); // Variable Id, always 0x0000
		buffer.putShort((short) 0x0000); // Hash, always 0x0000
		buffer.putShort((short) 0x0372); // Length, i.e. 0x0372 = 882
		buffer.putShort((short) 0x0000); // Quality, always 0x0000
	}

	private void addAudioFrame(final ByteBuffer buffer) {
		buffer.put(this.audioFrame);
	}
}
