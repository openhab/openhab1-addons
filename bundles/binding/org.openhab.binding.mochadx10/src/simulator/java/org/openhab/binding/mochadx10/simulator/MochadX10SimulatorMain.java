/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
