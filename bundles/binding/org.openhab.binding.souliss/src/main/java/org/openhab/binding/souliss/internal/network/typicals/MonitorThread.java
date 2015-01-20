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
 * @author Antonino-Fazio
 */
public class MonitorThread extends Thread {

	int REFRESH_TIME;
	private SoulissTypicals soulissTypicalsRecipients;
	private static Logger LOGGER = LoggerFactory.getLogger(MonitorThread.class);
	// SoulissUpdater sUpdater=new SoulissUpdater ();
	EventPublisher eventPublisher;

	public MonitorThread(SoulissTypicals typicals, int iRefreshTime,
			EventPublisher _eventPublisher) {
		// TODO Auto-generated constructor stub
		REFRESH_TIME = iRefreshTime;
		soulissTypicalsRecipients = typicals;
		LOGGER.info("Avvio MonitorThread");
		eventPublisher = _eventPublisher;
	}

	@Override
	public void run() {
		while (true) {
			try {
				// Check all souliss'typicals (items) and get only the ones that has been updated
				check(soulissTypicalsRecipients);

				Thread.sleep(REFRESH_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
			super.run();
		}
	}

	/**
	 * Goes though the hast table and send on the openHAB bus only the
	 * souliss' typicals that has been updated
	 * 
	 * @param typicals
	 */
	private void check(SoulissTypicals typicals) {
		// TODO Auto-generated method stub
		Iterator<Entry<String, SoulissGenericTypical>> iteratorTypicals = soulissTypicalsRecipients
				.getIterator();
		synchronized (iteratorTypicals) {
			while (iteratorTypicals.hasNext()) {
				SoulissGenericTypical typ = iteratorTypicals.next().getValue();
				if (typ.isUpdated()) {
					// Here we start the openHAB's methods used to update the item values

					if (typ.getType() == Constants.Souliss_TService_NODE_TIMESTAMP) {
						// All values are float out of TIMESTAMP that is a string
						LOGGER.debug("Put on Bus Events - "
								+ typ.getName()
								+ " = "
								+ ((SoulissTServiceNODE_TIMESTAMP) typ)
										.getTIMESTAMP());
					} else if (typ.getType() == Constants.Souliss_T16) {
						// RGB Only
						LOGGER.debug("Put on Bus Events - " + typ.getName()
								+ " = " + ((SoulissT16) typ).getState()
								+ ", R=" + ((SoulissT16) typ).stateRED + ", G="
								+ ((SoulissT16) typ).stateGREEN + ", B="
								+ ((SoulissT16) typ).stateBLU);
					} else if (typ.getType() == Constants.Souliss_T1A) {
						LOGGER.debug("Put on Bus Events - "
								+ typ.getName()
								+ " - Bit "
								+ ((SoulissT1A) typ).getBit()
								+ " - RawState: "
								+ Integer.toBinaryString(((SoulissT1A) typ)
										.getRawState()) + " - Bit State: "
								+ ((SoulissT1A) typ).getBitState());
					} else {
						LOGGER.debug("Put on Bus Events - " + typ.getName()
								+ " = " + Float.toString(typ.getState()));
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
