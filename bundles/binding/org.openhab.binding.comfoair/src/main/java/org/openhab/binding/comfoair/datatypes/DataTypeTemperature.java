/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.comfoair.datatypes;

import org.openhab.binding.comfoair.handling.ComfoAirCommandType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Class to handle temperature values
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public class DataTypeTemperature implements ComfoAirDataType {

	/**
	 * {@inheritDoc}
	 */
	public State convertToState(int[] data, ComfoAirCommandType commandType) {

		return new DecimalType(
			(((double) data[commandType.getGetReplyDataPos()[0]]) / 2) - 20);
	}

	/**
	 * {@inheritDoc}
	 */
	public int[] convertFromState(State value, ComfoAirCommandType commandType) {

		int[] template = commandType.getChangeDataTemplate();

		template[commandType.getChangeDataPos()] =
			(int) (((DecimalType) value).doubleValue() + 20) * 2;

		return template;
	}
	
}
