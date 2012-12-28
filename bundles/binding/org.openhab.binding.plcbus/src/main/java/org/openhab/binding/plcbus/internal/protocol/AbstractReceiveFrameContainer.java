/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.plcbus.internal.protocol;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Class for a ReceiveFrameContainer
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public abstract class AbstractReceiveFrameContainer implements IReceiveFrameContainer {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractReceiveFrameContainer.class);

	protected List<ReceiveFrame> receiveFrames;

	/**
	 * Constructor
	 */
	public AbstractReceiveFrameContainer() {
		receiveFrames = new ArrayList<ReceiveFrame>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(IByteProvider byteProvider) {
		while (!isReceivingCompleted()) {
			parseFrame(byteProvider);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract boolean isReceivingCompleted();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract ReceiveFrame getAnswerFrame();
	
	
	private void parseFrame(IByteProvider byteProvider) {
		try {
			byte currentByte = byteProvider.getByte();

			if (currentByte == Frame.START_BYTE) {
				int length = byteProvider.getByte();

				if (length > 0) {
					byte[] data = byteProvider.getBytes(length + 1);

					ReceiveFrame frame = new ReceiveFrame();
					frame.parse(data);

					receiveFrames.add(frame);
				}
			}
		} catch (Exception e) {
			logger.error("Error while parsing ReceiveFrame");
		}
	}

}
