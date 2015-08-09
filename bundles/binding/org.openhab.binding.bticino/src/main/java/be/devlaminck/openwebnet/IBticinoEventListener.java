/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package be.devlaminck.openwebnet;

/**
 * This interface must be implemented by every object that wants to process an
 * event on the bticino bus. The event is encapsulated in the ProtocolRead
 * object.
 * 
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.7.0
 */
public interface IBticinoEventListener {
	
	void handleEvent(ProtocolRead p_protocol_read) throws Exception;
	
}
