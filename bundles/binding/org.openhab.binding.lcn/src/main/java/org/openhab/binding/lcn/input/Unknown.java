/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.input;

import org.openhab.binding.lcn.connection.Connection;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Stores unknown LCN-PCHK input data.
 * 
 * @author Tobias Jüttner
 */
public class Unknown extends Input {
	
	/** Unchanged data received from LCN-PCHK. */
	private final String input;
	
	/**
	 * Constructor specifying input data received from LCN-PCHK.
	 * 
	 * @param input the input from LCN-PCHK
	 */
	Unknown(String input) {
		this.input = input;
	}
	
	/**
	 * Gets the originally received input data.
	 * 
	 * @return the input data from LCN-PCHK
	 */
	public String getInput() {
		return this.input;
	}
	
	/** {@inheritDoc} */
	@Override
	public void process(Connection conn) { }
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		// Not used for visualization
		return false;
	}
	
}
