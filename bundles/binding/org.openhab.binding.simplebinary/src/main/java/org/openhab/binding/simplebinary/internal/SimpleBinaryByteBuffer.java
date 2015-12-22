/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * ByteBuffer implementation with possibility to have control over read/write mode
 * 
 * @author Vita Tucek
 * @since 1.8.0
 * 
 */
public class SimpleBinaryByteBuffer {

	public enum BufferMode {
		WRITE, READ
	}

	protected ByteBuffer _buffer;
	protected BufferMode _mode = BufferMode.WRITE;
	protected int _size;

	/**
	 * Contruct buffer with defined size
	 * 
	 * @param size
	 *            Required size (bytes)
	 */
	public SimpleBinaryByteBuffer(int size) {
		_size = size;
		_buffer = ByteBuffer.allocate(size);

		_buffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * Buffer reinitialization
	 */
	public void initialize() {
		_mode = BufferMode.WRITE;
		_buffer = ByteBuffer.allocate(_size);

		_buffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * Return actual buffer mode
	 * 
	 * @return
	 */
	public BufferMode getMode() {
		return _mode;
	}

	/**
	 * Provide flip buffer (set into read mode)
	 * 
	 * @return
	 * @throws ModeChangeException
	 */
	public ByteBuffer flip() throws ModeChangeException {
		if (_mode != BufferMode.READ) {
			_mode = BufferMode.READ;
			_buffer.flip();
		} else
			throw new ModeChangeException("flip()", _mode);

		return _buffer;
	}

	/**
	 * Provide compact buffer (set into write mode and clear red items)
	 * 
	 * @return
	 * @throws ModeChangeException
	 */
	public ByteBuffer compact() throws ModeChangeException {
		if (_mode == BufferMode.READ) {
			_mode = BufferMode.WRITE;
			_buffer.compact();
		} else
			throw new ModeChangeException("compact()", _mode);

		return _buffer;
	}

	/**
	 * Provide clear buffer (set into write mode and clear all items)
	 * 
	 * @return
	 */
	public ByteBuffer clear() {
		if (_mode == BufferMode.READ) {
			_mode = BufferMode.WRITE;
			_buffer.clear();
		} else
			_buffer.position(0);

		return _buffer;
	}

	/**
	 * Set byte order
	 * 
	 * @param order
	 * @return
	 */
	public ByteBuffer order(ByteOrder order) {
		return _buffer.order(order);
	}

	/**
	 * Return position in buffer
	 * 
	 * @return
	 */
	public int position() {
		return _buffer.position();
	}

	/**
	 * Set position in buffer
	 * 
	 * @param position
	 * @return
	 */
	public Buffer position(int position) {
		return _buffer.position(position);
	}

	/**
	 * Return bytes remaining in buffer
	 * 
	 * @return
	 */
	public int remaining() {
		return _buffer.remaining();
	}

	/**
	 * Return buffer limit
	 * 
	 * @return
	 */
	public int limit() {
		return _buffer.limit();
	}

	/**
	 * Return buffer capacity
	 * 
	 * @return
	 */
	public int capacity() {
		return _buffer.capacity();
	}

	/**
	 * Rewind buffer to start position in read mode
	 * 
	 * @return
	 * @throws ModeChangeException
	 */
	public Buffer rewind() throws ModeChangeException {
		if (_mode == BufferMode.READ) {
			return _buffer.rewind();
		} else
			throw new ModeChangeException("rewind()", _mode);
	}

	/**
	 * Read byte
	 * 
	 * @return
	 * @throws ModeChangeException
	 */
	public byte get() throws ModeChangeException {
		if (_mode == BufferMode.READ) {
			return _buffer.get();
		} else
			throw new ModeChangeException("get()", _mode);
	}

	/**
	 * Read byte array
	 * 
	 * @param array
	 * @return
	 * @throws ModeChangeException
	 */
	public ByteBuffer get(byte[] array) throws ModeChangeException {
		if (_mode == BufferMode.READ) {
			return _buffer.get(array);
		} else
			throw new ModeChangeException("get(byte[] array)", _mode);
	}

	/**
	 * Read short
	 * 
	 * @return
	 * @throws ModeChangeException
	 */
	public short getShort() throws ModeChangeException {
		if (_mode == BufferMode.READ) {
			return _buffer.getShort();
		} else
			throw new ModeChangeException("getShort()", _mode);
	}

	/**
	 * Put byte array into buffer
	 * 
	 * @param array
	 * @param start
	 * @param bytes
	 * @return
	 * @throws ModeChangeException
	 */
	public ByteBuffer put(byte[] array, int start, int bytes) throws ModeChangeException {
		if (_mode == BufferMode.WRITE) {
			return _buffer.put(array, start, bytes);
		} else
			throw new ModeChangeException("put(byte[] array, int start, int bytes)", _mode);
	}
}
