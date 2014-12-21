/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.binrpc;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes a BIN-RPC message from the Homematic server.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BinRpcResponse {
	private final static Logger logger = LoggerFactory.getLogger(BinRpcResponse.class);

	private byte data[];
	private int dataoffset = 0;
	private String methodName;
	private Object[] responseData;

	/**
	 * Decodes a BIN-RPC message from the given InputStream.
	 */
	public BinRpcResponse(InputStream is, boolean methodHeader) throws IOException, ParseException {
		byte sig[] = new byte[4];
		int l = is.read(sig);
		if (l != sig.length) {
			throw new EOFException("Only " + l + " bytes received reading signature");
		}
		if (sig[0] != 'B' || sig[1] != 'i' || sig[2] != 'n') {
			throw new UnsupportedEncodingException("No BinX signature");
		}
		l = is.read(sig);
		if (l != sig.length) {
			throw new EOFException("Only " + l + " bytes received reading length");
		}
		int datasize = (new BigInteger(sig)).intValue();
		data = new byte[datasize];
		int offset = 0;
		while (datasize > 0) {
			int r = is.read(data, offset, datasize);
			if (r < 1) {
				throw new EOFException("EOF while reading data");
			}
			datasize -= r;
			offset += r;
		}

		if (methodHeader) {
			int slen = readInt();
			methodName = new String(data, dataoffset, slen, "ISO-8859-1");
			dataoffset += slen;
			readInt();
		}

		List<Object> values = new ArrayList<Object>();
		while (dataoffset < data.length) {
			values.add(readRpcValue());
		}
		responseData = values.toArray();
		values.clear();
		data = null;
	}

	/**
	 * Returns the decoded methodName.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Returns the decoded data.
	 */
	public Object[] getResponseData() {
		return responseData;
	}

	private int readInt() {
		byte bi[] = new byte[4];
		System.arraycopy(data, dataoffset, bi, 0, 4);
		dataoffset += 4;
		return (new BigInteger(bi)).intValue();
	}

	private Object readRpcValue() throws UnsupportedEncodingException, ParseException {
		int type = readInt();
		switch (type) {
		case 1:
			return new Integer(readInt());
		case 2:
			return data[dataoffset++] != 0 ? Boolean.TRUE : Boolean.FALSE;
		case 3:
			int len = readInt();
			dataoffset += len;
			return new String(data, dataoffset - len, len, "ISO-8859-1");
		case 4:
			int mantissa = readInt();
			int exponent = readInt();
			BigDecimal bd = new BigDecimal((double) mantissa / (double) (1 << 30) * Math.pow(2, exponent));
			return bd.setScale(6, RoundingMode.HALF_DOWN).doubleValue();
		case 5:
			return new Date(readInt() * 1000);
		case 0x100:
			// Array
			int numElements = readInt();
			Collection<Object> array = new ArrayList<Object>();
			while (numElements-- > 0) {
				array.add(readRpcValue());
			}
			return array.toArray();
		case 0x101:
			// Struct
			numElements = readInt();
			Map<String, Object> struct = new TreeMap<String, Object>();
			while (numElements-- > 0) {
				int slen = readInt();
				String name = new String(data, dataoffset, slen, "ISO-8859-1");
				dataoffset += slen;
				struct.put(name, readRpcValue());
			}
			return struct;

		default:
			for (int x = 0; x < data.length; x++) {
				logger.info(Integer.toHexString(data[x]) + " " + (char) data[x]);
			}
			throw new ParseException("Unknown data type " + type, type);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (methodName != null) {
			sb.append(methodName);
			sb.append("()\n");
		}
		dumpCollection(responseData, sb, 0);
		return sb.toString();
	}

	private void dumpCollection(Object[] c, StringBuilder sb, int indent) {
		if (indent > 0) {
			for (int in = 0; in < indent - 1; in++) {
				sb.append('\t');
			}
			sb.append("[\n");
		}
		for (Object o : c) {
			if (o instanceof Map) {
				dumpMap((Map<?, ?>) o, sb, indent + 1);
			} else if (o instanceof Object[]) {
				dumpCollection((Object[]) o, sb, indent + 1);
			} else {
				for (int in = 0; in < indent; in++) {
					sb.append('\t');
				}
				sb.append(o);
				sb.append('\n');
			}
		}
		if (indent > 0) {
			for (int in = 0; in < indent - 1; in++) {
				sb.append('\t');
			}
			sb.append("]\n");
		}
	}

	private void dumpMap(Map<?, ?> c, StringBuilder sb, int indent) {
		if (indent > 0) {
			for (int in = 0; in < indent - 1; in++) {
				sb.append('\t');
			}
			sb.append("{\n");
		}
		for (Map.Entry<?, ?> me : c.entrySet()) {
			Object o = me.getValue();
			for (int in = 0; in < indent; in++) {
				sb.append('\t');
			}
			sb.append(me.getKey());
			sb.append('=');
			if (o instanceof Map<?, ?>) {
				sb.append("\n");
				dumpMap((Map<?, ?>) o, sb, indent + 1);
			} else if (o instanceof Object[]) {
				sb.append("\n");
				dumpCollection((Object[]) o, sb, indent + 1);
			} else {
				sb.append(o);
				sb.append('\n');
			}
		}
		if (indent > 0) {
			for (int in = 0; in < indent - 1; in++) {
				sb.append('\t');
			}
			sb.append("}\n");
		}
	}
}
