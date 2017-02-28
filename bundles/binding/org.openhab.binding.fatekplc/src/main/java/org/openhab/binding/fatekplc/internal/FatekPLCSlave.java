/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.fatekplc.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.fatekplc.items.CommandException;
import org.openhab.binding.fatekplc.items.FatekPLCItem;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.simplify4u.jfatek.FatekException;
import org.simplify4u.jfatek.FatekPLC;
import org.simplify4u.jfatek.FatekReadMixDataCmd;
import org.simplify4u.jfatek.io.FatekIOException;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of one fatek slave.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekPLCSlave {

	/**
	 * Main logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FatekPLCSlave.class);

	private final StateCache stateCache;

	private EventPublisher eventPublisher;

	private String name;

	private FatekPLC fatekPLC = null;

	public FatekPLCSlave(String name, EventPublisher eventPublisher) {
		this.name = name;
		this.eventPublisher = eventPublisher;
		this.stateCache = new StateCache();
	}

	public String getName() {
		return name;
	}

	public void disconnect() throws FatekIOException {

		if (fatekPLC != null) {
			fatekPLC.close();
		}
	}

	/**
	 * Configure slave property
	 *
	 * @param name property name
	 * @param value config value
	 * @throws ConfigurationException if something wrong with configuration, eg. Unknown property
	 */
	public void configure(String name, Object value) throws ConfigurationException {

		logger.debug("Configure item: {} to {}", name, value);

		if ("connectionUri".equals(name)) {
			try {
				disconnect();
				fatekPLC = new FatekPLC((String) value);
				logger.info("New Fatek PLC connectionUri={}", value);
			} catch (Exception e) {
				fatekPLC = null;
				throw new ConfigurationException(name, e.getMessage());
			}
		} else {
			throw new ConfigurationException(name, "Unknown property");
		}

		logger.debug("Configure item: {} end", name);
	}

	/**
	 * Read current data from Fatek PLC and publish new state on OpenHab bus.
	 *
	 * @param items items to updates
	 * @throws FatekIOException
	 * @throws FatekException
	 */
	public void updateItems(List<FatekPLCItem> items) throws FatekIOException, FatekException {

		// collect unique register to read from PLC
		Set<Reg> regsToUpdate = new HashSet<>();
		for (FatekPLCItem item : items) {
			regsToUpdate.addAll(item.getRegs());
		}

		Map<Reg, RegValue> response;
		synchronized (fatekPLC) {
			// read register
			response = new FatekReadMixDataCmd(fatekPLC, regsToUpdate).send();
		}

		// distribute read data
		for (FatekPLCItem fatekItem : items) {

			State newState = fatekItem.getState(response);

			if (stateCache.isStateChange(fatekItem, newState)) {
				eventPublisher.postUpdate(fatekItem.getItemName(), newState);
			}
		}
	}

	public void command(FatekPLCItem item, Command command) {

		logger.debug("FatekPLC command item={}, command={} {}", item, command, command.getClass());

		try {
			synchronized(fatekPLC) {
				item.command(fatekPLC, command);
			}
		} catch (CommandException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
