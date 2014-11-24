/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.gfx;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * Reads character streams and replaces tokens in the format ${...}.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class TokenReplacingReader extends Reader {
	protected PushbackReader pushbackReader;
	protected TokenResolver tokenResolver;
	protected StringBuilder tokenNameBuffer = new StringBuilder();
	protected String tokenValue = null;
	protected int tokenValueIndex = 0;

	public TokenReplacingReader(Reader source, TokenResolver resolver) {
		this.pushbackReader = new PushbackReader(source, 2);
		this.tokenResolver = resolver;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(CharBuffer target) throws IOException {
		throw new RuntimeException("Operation not supported");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		if (tokenValue != null) {
			if (tokenValueIndex < tokenValue.length()) {
				return tokenValue.charAt(tokenValueIndex++);
			}
			if (tokenValueIndex == tokenValue.length()) {
				tokenValue = null;
				tokenValueIndex = 0;
			}
		}

		int data = pushbackReader.read();
		if (data != '$')
			return data;

		data = pushbackReader.read();
		if (data != '{') {
			pushbackReader.unread(data);
			return '$';
		}
		tokenNameBuffer.delete(0, tokenNameBuffer.length());

		data = pushbackReader.read();
		while (data != '}') {
			tokenNameBuffer.append((char) data);
			data = pushbackReader.read();
		}

		tokenValue = tokenResolver.resolveToken(tokenNameBuffer.toString());

		if (tokenValue == null) {
			tokenValue = "${" + tokenNameBuffer.toString() + "}";
		}
		if (tokenValue.length() == 0) {
			return read();
		}
		return tokenValue.charAt(tokenValueIndex++);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(char cbuf[]) throws IOException {
		return read(cbuf, 0, cbuf.length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(char cbuf[], int off, int len) throws IOException {
		int charsRead = 0;
		for (int i = 0; i < len; i++) {
			int nextChar = read();
			if (nextChar == -1) {
				if (charsRead == 0) {
					charsRead = -1;
				}
				break;
			}
			charsRead = i + 1;
			cbuf[off + i] = (char) nextChar;
		}
		return charsRead;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		pushbackReader.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long skip(long n) throws IOException {
		throw new RuntimeException("Operation not supported");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean ready() throws IOException {
		return pushbackReader.ready();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean markSupported() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mark(int readAheadLimit) throws IOException {
		throw new RuntimeException("Operation not supported");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() throws IOException {
		throw new RuntimeException("Operation not supported");
	}
}