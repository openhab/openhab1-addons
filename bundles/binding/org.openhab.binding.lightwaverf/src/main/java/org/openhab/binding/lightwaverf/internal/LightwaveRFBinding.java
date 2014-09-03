/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import org.openhab.binding.lightwaverf.LightwaveRFBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Neil Renaud
 * @since 1.6
 */
public class LightwaveRFBinding extends AbstractActiveBinding<LightwaveRFBindingProvider> {

	private final Logger logger = LoggerFactory.getLogger(LightwaveRFBinding.class);
	private static final long POLLING_INTERVAL = 100; //100ms
	private static final String THREAD_NAME = "LightwaveRF Receiver Thread";
	private LightwaveRFReceiver receiver = null;
	private LightwaveRFSender sender = null;

	@Override
	public void activate() {
//		receiver = new LightwaveRFReceiver();
//		receiver.start();
//		sender = new LightwaveRFSender();
//		sender.start();
	}

	@Override
	public void deactivate() {
//		receiver.stop();
//		receiver = null;
//		sender.stop();
//		sender = null;
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate() is called!");
	}

	@Override
	protected void execute(){
		logger.debug("Execute called");
	}

	@Override
	protected long getRefreshInterval(){
		return POLLING_INTERVAL;
	}

	@Override
	protected String getName(){
		return THREAD_NAME;
	}
}
