/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.util.Iterator;
import java.util.Map.Entry;
import org.openhab.core.events.EventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provide to read typical state and put it on Openhab bus events
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class Monitor {
	private SoulissTypicals soulissTypicalsRecipients;
	private static Logger logger = LoggerFactory.getLogger(Monitor.class);
	private EventPublisher eventPublisher;

	/**
	 * Constructor
	 * 
	 * @author Tonino Fazio
	 * @since 1.7.0
	 */
	public Monitor(SoulissTypicals typicals, int iRefreshTime,
			EventPublisher _eventPublisher) {
		soulissTypicalsRecipients = typicals;
		logger.info("Start MonitorThread");
		eventPublisher = _eventPublisher;
	}

	/**
	 * Check and sleep for REFRESH_TIME mills
	 * 
	 * @author Tonino Fazio
	 * @since 1.7.0
	 */
	public void tick() {
		// Check all souliss'typicals (items) and get only the ones that
			// has been updated
			check(soulissTypicalsRecipients);
	}

	/**
	 * Goes though the hashtable and send on the openHAB bus only the souliss'
	 * typicals that has been updated
	 * 
	 * @author Tonino Fazio
	 * @since 1.7.0
	 * @param SoulissTypicals
	 *            typicals
	 */
	private void check(SoulissTypicals typicals) {
		Iterator<Entry<String, SoulissGenericTypical>> iteratorTypicals = soulissTypicalsRecipients
				.getIterator();
		synchronized (iteratorTypicals) {
			while (iteratorTypicals.hasNext()) {
				SoulissGenericTypical typ = iteratorTypicals.next().getValue();
				if (typ.isUpdated()) {
					// Here we start the openHAB's methods used to update the
					// item values
					if (typ.getType() == Constants.Souliss_TService_NODE_TIMESTAMP) {
						// All values are float out of TIMESTAMP that is a
						// string
						logger.debug("Put on Bus Events - {} = {}", typ.getName(), ((SoulissTServiceNODE_TIMESTAMP) typ).getTIMESTAMP());
						
					} else if (typ.getType() == Constants.Souliss_T16) {
						// RGB Only
						logger.debug("Put on Bus Events - {} = {}, R={}, G={}, B={}", typ.getName(),((SoulissT16) typ).getState(), ((SoulissT16) typ).stateRED,((SoulissT16) typ).stateGREEN,((SoulissT16) typ).stateBLU);
						
					} else if (typ.getType() == Constants.Souliss_T1A) {
						logger.debug("Put on Bus Events - {} - Bit {} - RawState: {} - Bit State: {}",typ.getName(),((SoulissT1A) typ).getBit(),Integer.toBinaryString(((SoulissT1A) typ).getRawState()),((SoulissT1A) typ).getBitState()); 
					} else {
						logger.debug("Put on Bus Events - {} = {}", typ.getName(), Float.toString(typ.getState()));
					}

					if (typ.getType() == Constants.Souliss_T16) {
						eventPublisher.postUpdate(typ.getName(),
								((SoulissT16) typ).getOHStateRGB());
					} else {
						eventPublisher.postUpdate(typ.getName(),
								typ.getOHState());
					}
					typ.resetUpdate();
				}
			}
		}
	}
}
