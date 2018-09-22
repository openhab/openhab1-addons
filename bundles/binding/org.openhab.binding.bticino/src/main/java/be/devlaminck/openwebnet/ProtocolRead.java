/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package be.devlaminck.openwebnet;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProtocolRead for OpenWebNet - OpenHab binding Based on code from Mauro
 * Cicolella (as part of the FREEDOMOTIC framework)
 * (https://github.com/freedomotic
 * /freedomotic/tree/master/plugins/devices/openwebnet) and on code of Flavio
 * Fcrisciani released as EPL
 * (https://github.com/fcrisciani/java-myhome-library)
 *
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.7.0
 */
public class ProtocolRead {
    private String m_message = "";

    private static final Logger logger = LoggerFactory.getLogger(ProtocolRead.class);

    private Map<String, String> m_properties = new HashMap<String, String>();

    public ProtocolRead(String p_message) {
        m_message = p_message;
        logger.debug("Instance created for message [{}]", p_message);
    }

    public void addProperty(String p_key, String p_value) {
        // TODO Auto-generated method stub
        logger.debug("addProperty Key : {}, Value : {}", p_key, p_value);
        m_properties.put(p_key, p_value);
    }

    public String getProperty(String p_key) {
        return (m_properties.get(p_key));
    }

    @Override
    public String toString() {
        return ("ProtocolRead, Message[" + m_message + "]");
    }
}
