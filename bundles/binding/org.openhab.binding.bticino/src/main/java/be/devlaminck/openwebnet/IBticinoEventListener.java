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
