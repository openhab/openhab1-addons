/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.gpio.linux;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Provides interface to subset of native functions exported by standard libc Linux
 * library.
 *
 * @author Dancho Penev
 * @since 1.5.0
 */
public interface LibC extends Library {
	
	/** Open for reading only. */
	public static final int O_RDONLY = 00;

	/** Open in nonblocking mode. */
	public static final int O_NONBLOCK = 04000;

	/** There is urgent data to read. */
	public static final short POLLPRI = 0x002;

	/** Error condition. */
	public static final short POLLERR = 0x008;

	/** The offset is set to <code>offset</code> bytes. */
	public static final int SEEK_SET = 0;

	/** 
	 * The offset is set to its current location plus
	 * <code>offset</code> bytes.
	 */
	public static final int SEET_CUR = 1;

	/**
	 * The offset is set to the size of the file plus
	 * <code>offset</code> bytes.
	 */
	public static final int SEEK_END = 2;

	public static final LibC INSTANCE = (LibC) Native.loadLibrary("c", LibC.class);

	/**
	 * Open and possibly create a file or device.
	 * 
	 * @param path file path
	 * @param oflag file status flags and file access modes
	 * @return new file descriptor, or -1 if an error occurred
	 * @throws LastErrorException exception representing a non-zero
	 * 		error code returned in errno
	 */
	public int open(String path, int oflag) throws LastErrorException;

	/**
	 * Closes a file descriptor, so that it no longer refers to any
	 * file and may be reused.
	 * 
	 * @param fd file descriptor for already opened file
	 * @return zero on success, otherwise -1
	 * @throws LastErrorException exception representing a non-zero
	 * 		error code returned in errno
	 */
	public int close(int fd) throws LastErrorException;

	/**
	 * Waits for one of a set of file descriptors to become ready to
	 * perform I/O.
	 * 
	 * @param fds set of file descriptors to be monitored
	 * @param nfds the number of items in the fds array
	 * @param timeout the minimum number of milliseconds that poll()
	 * 		will block
	 * @return positive number representing the number of structures
	 * 		which have nonzero revents fields on success, otherwise -1
	 * @throws LastErrorException exception representing a non-zero
	 * 		error code returned in errno
	 */
	public int poll(Structure[] fds, int nfds, int timeout) throws LastErrorException;

	/**
	 * Read up to <code>count</code> bytes from file descriptor
	 * <code>fd</code> into the buffer starting at <code>buf</code>.
	 * 
	 * @param fd file descriptor of file to read from
	 * @param buf the address of buffer to read to
	 * @param count number of bytes to read
	 * @return number of read bytes on success, otherwise -1
	 * @throws LastErrorException exception representing a non-zero
	 * 		error code returned in errno
	 */
	public int read(int fd, Pointer buf, int count) throws LastErrorException;

	/**
	 * Reposition read/write file offset.
	 * 
	 * @param fd file descriptor of already opened file
	 * @param offset new offset relative to <code>whence</code>
	 * @param whence either <code>SEEK_SET</code>, <code>SEET_CUR</code>
	 * 		or <code>SEEK_END</code>
	 * @return the resulting offset location as measured in bytes from
	 * 		the beginning of the file on success, otherwise -1
	 * @throws LastErrorException exception representing a non-zero
	 * 		error code returned in errno
	 */
	public NativeLong lseek(int fd, NativeLong offset, int whence) throws LastErrorException;
}
