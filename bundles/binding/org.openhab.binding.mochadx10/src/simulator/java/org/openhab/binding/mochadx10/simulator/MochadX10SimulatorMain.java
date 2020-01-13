/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mochadx10.simulator;

import java.io.IOException;

/**
 * This is the main thread of a Mochad X10 host server
 * 
 * @author Jack Sleuters
 * @since 1.7.0
 *
 */
public class MochadX10SimulatorMain {

	public static void main(String[] args) throws IOException, InterruptedException {
		MochadX10Simulator simulator = new MochadX10Simulator();
		
		simulator.start();
	}
}
