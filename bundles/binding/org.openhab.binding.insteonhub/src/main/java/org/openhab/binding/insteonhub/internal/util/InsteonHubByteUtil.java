/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

/**
 * Utility functions for dealing with hex/buffers/bytes
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubByteUtil {
	
	public static int byteToUnsignedInt(byte b) {
		int i = b;
		if (i < 0) {
			i += 256;
		}
		return i;
	}

	public static String encodeDeviceHex(byte[] buf, int off) {
		byte[] devBuf = Arrays.copyOfRange(buf, off, off + 3);
		return Hex.encodeHexString(devBuf).replace(" ", "");
	}

	public static void fillBuffer(InputStream in, byte[] buf, int off)
			throws IOException {
		while (off < buf.length) {
			int numRead = in.read(buf, off, buf.length - off);
			if (numRead == -1) {
				throw new IOException("Unexpected end of stream");
			}
			off += numRead;
		}
	}

	public static byte readByte(InputStream in) throws IOException {
		int v = in.read();
		if (v == -1) {
			throw new IOException("Unexpected end of stream");
		}
		return (byte) v;
	}
	
	public static boolean checkBit(int value, int idx) {
		return (value & (1 << idx)) != 0;
	}
}
