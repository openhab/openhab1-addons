/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.quist.samy.remocon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.util.Arrays;

/**
 * Copied from https://github.com/keremkusmezer/SamyGo-Android-Remote/tree/master/src/de/quist/samy/remocon,
 * since there is no binary build available anymore. Thanks to Tom Quist!
 *  
 * @author Tom Quist
 */
public class RemoteReader {

	public interface ReplyHandler {
		
		void onRegistrationSuccessfull();
		
		void onRegistrationDenied();
		
		void onRegistrationTimeout();

		void onUnknownStatus(char[] status);

		void onClose(IOException e);
		
	}

	private static final char[] REGISTRATION_ALLOWED = new char[] {0x64, 0x00, 0x01, 0x00};
	private static final char[] REGISTRATION_DENIED = new char[] {0x64, 0x00, 0x00, 0x00};
	private static final char[] REGISTRATION_TIMEOUT = new char[] {0x65, 0x00};
	
	@SuppressWarnings("unused")
	private static final char[] EVERYTHING_IS_FINE = new char[] {0x00, 0x00, 0x00, 0x00};
	
	

	private InputStream in;
	private ReplyHandler handler;
	private Thread worker;
	private InputStreamReader reader;
	private boolean active;

	RemoteReader(InputStream in, ReplyHandler handler) {
		this.in = in;
		this.handler = handler;
	}
	
	public synchronized void start() {
		if (active) return;
		this.reader = new InputStreamReader(in);
		active = true;
		this.worker = new Thread(new Runnable() {
			
			public void run() {
				try {
					work();
				} catch (IOException e) {
					if (active) {
						handler.onClose(e);
					}
					active = false;
				}
			}
		});
		this.worker.start();
		
	}
	
	@SuppressWarnings("unused")
	private void work() throws IOException, InterruptedIOException {
		while (active) {
			int messageType = reader.read();
			String identifier = readText(reader);
			char[] status = readCharArray(reader);
			if (Arrays.equals(REGISTRATION_ALLOWED, status)) {
				handler.onRegistrationSuccessfull();
			} else if (Arrays.equals(REGISTRATION_DENIED, status)) {
				handler.onRegistrationDenied();
			} else if (Arrays.equals(REGISTRATION_TIMEOUT, status)) {
				handler.onRegistrationTimeout();
			} else {
				handler.onUnknownStatus(status);
			}
		}
	}

	private static String readText(Reader reader) throws IOException {
		char[] buffer = readCharArray(reader);
		return new String(buffer);
	}
	
	private static char[] readCharArray(Reader reader) throws IOException {
		if (reader.markSupported()) reader.mark(1024);
		int length = reader.read();
		int delimiter = reader.read();
		if (delimiter != 0) {
			if (reader.markSupported()) reader.reset();
			throw new IOException("Unsupported reply exception");
		}
		char[] buffer = new char[length];
		reader.read(buffer);
		return buffer;
	}
	
	public synchronized void stop() {
		if (!active) return;
		this.active = false;
		if (this.worker != null) {
			try {
				in.close();
			} catch (IOException e) {
				
			}
			worker.interrupt();
		}
	}
	
}
