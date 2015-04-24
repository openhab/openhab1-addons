/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.builder.ToStringBuilder;
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

	private final short switchId;

	private final InetAddress switchAddress;

	private final List<UluxMessage> messages;

	public UluxDatagram(final short switchId, final InetAddress switchAddress) {
		this.switchId = switchId;
		this.switchAddress = switchAddress;

		this.messages = new LinkedList<UluxMessage>();
	}

	private static short nextPackageId() {
		final int packageId = PACKAGE_COUNTER.getAndIncrement();

		if (packageId >= Short.MAX_VALUE) {
			PACKAGE_COUNTER.set(1);
		}

		return (short) packageId;
	}

	public void send(final DatagramChannel channel) {
		if (!hasMessages()) {
			return; // nothing to send
		}

		final ByteBuffer buffer = prepareBuffer();

		try {
			final SocketAddress target = new InetSocketAddress(this.switchAddress, UluxBinding.PORT);

			LOG.debug("Sending datagram to switch {}.", target);

			channel.send(buffer, target);
		} catch (final IOException e) {
			throw new UluxException("Could not send command to switch!", e);
		}
	}

	/**
	 * Only accessible for tests!
	 */
	protected ByteBuffer prepareBuffer() {
		final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		addDescriptor(buffer);

		for (UluxMessage message : this.messages) {
			buffer.put(message.getBuffer());
		}

		updateLength(buffer);

		buffer.flip();

		return buffer;
	}

	private void addDescriptor(ByteBuffer buffer) {
		// magic bytes: 0x8601
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x86);

		// length: e.g. 0x0010 = 16 Bytes
		buffer.put((byte) 0x10);
		buffer.put((byte) 0x00);

		// protocol version: v2.24
		buffer.put((byte) 0x18);
		buffer.put((byte) 0x02);

		// package id
		buffer.putShort(nextPackageId());

		// TODO UluxConfiguration.getProjectId
		// project id
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x00);

		// firmware version, v1.48
		buffer.put((byte) 0x30);
		buffer.put((byte) 0x01);

		// switch id
		buffer.putShort(this.switchId);

		// TODO UluxConfiguration.getDesignId
		// design id
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x00);
	}

	private void updateLength(ByteBuffer buffer) {
		buffer.putShort(2, (short) buffer.position());
	}

	public void addMessage(final UluxMessage message) {
		LOG.debug("Adding message to datagram: {}", message);

		this.messages.add(message);
	}

	public boolean hasMessages() {
		return !this.messages.isEmpty();
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_PREFIX_STYLE);
		builder.append("switchId", this.switchId);
		builder.append("switchAddress", this.switchAddress);
		builder.append("messages", this.messages);

		return builder.toString();
	}
}
