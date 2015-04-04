/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.atomic.AtomicInteger;

import org.openhab.binding.ulux.internal.UluxBinding;
import org.openhab.binding.ulux.internal.UluxException;

/**
 * A {@link UluxDatagram} contains zero or more {@link UluxMessage}s.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxDatagram {

	private static final int BUFFER_SIZE = 1024;

	private static final AtomicInteger PACKAGE_COUNTER = new AtomicInteger(1);

	private final ByteBuffer buffer;

	private final short switchId;

	private final InetAddress switchAddress;

	public UluxDatagram(final short switchId, final InetAddress switchAddress) {
		this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);

		this.switchId = switchId;
		this.switchAddress = switchAddress;

		addDescriptor();
	}

	private short nextPackageId() {
		final int packageId = PACKAGE_COUNTER.getAndIncrement();

		if (packageId >= Short.MAX_VALUE) {
			PACKAGE_COUNTER.set(1);
		}

		return (short) packageId;
	}

	public void send(final DatagramChannel channel) {
		this.buffer.flip();

		try {
			final SocketAddress target = new InetSocketAddress(this.switchAddress, UluxBinding.PORT);

			LOG.debug("Sending datagram to switch {}.", target);

			channel.send(buffer, target);
		} catch (final IOException e) {
			throw new UluxException("Could not send command to switch!", e);
		}
	}

	private void addDescriptor() {
		// magic bytes: 0x8601
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x86);

		// length: e.g. 0x0010 = 16 Bytes
		buffer.put((byte) 0x10);
		buffer.put((byte) 0x00);

		// protocol version: v2.06
		buffer.put((byte) 0x06);
		buffer.put((byte) 0x02);

		// package id
		buffer.putShort(nextPackageId());

		// TODO UluxConfiguration.getProjectId
		// project id
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x00);

		// firmware version, v1.23
		buffer.put((byte) 0x23);
		buffer.put((byte) 0x01);

		// switch id
		buffer.putShort(this.switchId);

		// TODO UluxConfiguration.getDesignId
		// design id
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x00);
	}

	private void updateLength() {
		this.buffer.putShort(2, (short) this.buffer.position());
	}

	public void addMessage(final UluxMessage message) {
		LOG.debug("Adding message to datagram: {}", message);

		this.buffer.put(message.getBuffer());

		updateLength();
	}

	public boolean hasMessages() {
		return this.buffer.position() > 16;
	}

}
