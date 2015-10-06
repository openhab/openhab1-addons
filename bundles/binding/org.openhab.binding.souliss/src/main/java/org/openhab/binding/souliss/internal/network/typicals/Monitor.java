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
						logger.debug("Put on Bus Events - {} = {}", typ
								.getName(),
								((SoulissTServiceNODE_TIMESTAMP) typ)
										.getTIMESTAMP());

					} else if (typ.getType() == Constants.Souliss_T16) {
						// RGB Only
						logger.debug(
								"Put on Bus Events - {} = {}, R={}, G={}, B={}",
								typ.getName(), ((SoulissT16) typ).getState(),
								((SoulissT16) typ).stateRED,
								((SoulissT16) typ).stateGREEN,
								((SoulissT16) typ).stateBLU);

					} else if (typ.getType() == Constants.Souliss_T1A) {
						logger.debug(
								"Put on Bus Events - {} - Bit {} - RawState: {} - Bit State: {}",
								typ.getName(), ((SoulissT1A) typ).getBit(),
								Integer.toBinaryString(((SoulissT1A) typ)
										.getRawState()), ((SoulissT1A) typ)
										.getBitState());
					} else if (typ.getType() == Constants.Souliss_T31) {
						// T31
						logger.debug(
								"Put on Bus Events - Command State: {} - Temperature Measured Value {} - Set Point {}",
								((SoulissT31) typ).getRawCommandState(),
								((SoulissT31) typ)
										.getTemperatureMeasuredValue(),
								((SoulissT31) typ).getSetpointValue());
					} else {
						logger.debug("Put on Bus Events - {} = {}",
								typ.getName(), Float.toString(typ.getState()));
					}

					if (typ.getType() == Constants.Souliss_T16) {
						eventPublisher.postUpdate(typ.getName(),
								((SoulissT16) typ).getOHStateRGB());
					} else if (typ.getType() == Constants.Souliss_T31) {
							// qui inserimento dati per tipico 31
							SoulissT31 typ31 = (SoulissT31) typ;
							if (typ31.getsItemNameMeasuredValue() != null)
								eventPublisher.postUpdate(
										typ31.getsItemNameMeasuredValue(),
										typ31.getOHStateMeasuredValue());
							if (typ31.getsItemNameSetpointValue() != null)
								eventPublisher.postUpdate(
										typ31.getsItemNameSetpointValue(),
										typ31.getOHStateSetpointValue());
							if (typ31.power.getName() != null)
								eventPublisher.postUpdate(
										typ31.power.getName(),
										typ31.power.getOHState());
							if (typ31.heating.getName() != null)
								eventPublisher.postUpdate(
										typ31.heating.getName(),
										typ31.heating.getOHState());
							if (typ31.cooling.getName() != null)
								eventPublisher.postUpdate(
										typ31.cooling.getName(),
										typ31.cooling.getOHState());
							if (typ31.heatingCoolingModeValue.getName() != null)
								eventPublisher
										.postUpdate(
												typ31.heatingCoolingModeValue
														.getName(),
												typ31.heatingCoolingModeValue
														.getOHState());
							if (typ31.fanHigh.getName() != null)
								eventPublisher.postUpdate(
										typ31.fanHigh.getName(),
										typ31.fanHigh.getOHState());
							if (typ31.fanMed.getName() != null)
								eventPublisher.postUpdate(
										typ31.fanMed.getName(),
										typ31.fanMed.getOHState());
							if (typ31.fanLow.getName() != null)
								eventPublisher.postUpdate(
										typ31.fanLow.getName(),
										typ31.fanLow.getOHState());
							if (typ31.fanAutoMode.getName() != null)
								eventPublisher.postUpdate(
										typ31.fanAutoMode.getName(),
										typ31.fanAutoMode.getOHState());
						} else if (typ.getType() == Constants.Souliss_T12) { 
								// qui inserimento dati per tipico 12
								SoulissT12 typ12 = (SoulissT12) typ;
								if (typ12.getOHStateAutoMode() != null)
									eventPublisher.postUpdate(typ12.getsItemNameAutoModeValue(),
											typ12.getOHStateAutoMode());
								if (typ12.getOHStateSwitch() != null)
									eventPublisher.postUpdate(
											typ12.getsItemNameSwitchValue(),
											typ12.getOHStateSwitch());
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

