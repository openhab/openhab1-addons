/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.connectors;

import org.openhab.binding.smlreader.internal.SmlReaderBinding;
import org.openmuc.jsml.structures.SML_File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a basic implementation of a SML device connector.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
 abstract class ConnectorBase implements ISmlConnector {
	 
	 protected static final Logger logger = LoggerFactory.getLogger(SmlReaderBinding.class);
	 
	/**
	* Contructor for basic members.
	*
	* This constructor has to be called from derived classes!
	*/
	ConnectorBase() {
	}
	
	/**
	* Open connection.
	*
	*/
	abstract protected void openConnection();

	/**
	* Close connection.
	 * @throws ConnectorException 
	*
	*/
	abstract protected void closeConnection();
	
	/**
	* Close connection.
	 * @throws ConnectorException 
	*
	*/
	abstract protected SML_File getMeterValuesInternal();

	@Override
	public SML_File getMeterValues()
	{
		SML_File smlFile = null;
		
		try {
			openConnection();
			smlFile = getMeterValuesInternal();
		} finally {
			closeConnection();			
		}

		return smlFile;
	}
}
 