/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.dmx.daemon;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.openhab.binding.dmx.DmxConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMX Connection Implementation using lib485 as the DMX target.
 */
public class DaemonConnection implements DmxConnection {

	private static final Logger logger = 
		LoggerFactory.getLogger(DaemonConnection.class);

	private static RandomAccessFile fileHandle;
	
	@Override
	public void open(String connectionString) throws Exception {
		String dev = "/var/dmx/state";
// Configured path doesn't appear to be being passed in.  Hardcode it here instead.
//		if (connectionString != null && !connectionString.isEmpty()) {
//			dev = connectionString;
//		}
		fileHandle = new RandomAccessFile(dev,"rw");
		
		fileHandle.seek(0);
		fileHandle.write(0);	
	}

	@Override
	public void close() {
		
		if (fileHandle != null) {
			try {
				fileHandle.close();
			} catch (IOException e) {
				logger.warn("Could not close socket.", e);
			}
		}
		fileHandle = null;
	}
	
	@Override
	public boolean isClosed() {
		return fileHandle == null;
	}
	
	@Override
	public void sendDmx(byte[] buffer) throws Exception {
		logger.debug("Sending Data to DMX");
		fileHandle.seek(1);
		fileHandle.write(buffer);
	}
	

}
