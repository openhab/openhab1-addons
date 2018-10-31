/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product representation.
 * <P>
 * Combined set of information describing a single Velux product.
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxGwFirmware {
    private final Logger logger = LoggerFactory.getLogger(VeluxGwFirmware.class);

    // Class internal

    private String firmwareVersion;

    // Constructor

    public VeluxGwFirmware(String firmwareVersion) {
        logger.trace("VeluxGwFirmware() created.");

        this.firmwareVersion = firmwareVersion;
    }

    
    // Class access methods

    public String getfirmwareVersion() {
        return this.firmwareVersion;
    }


}

/**
 * end-of-VeluxGWFirmware.java
 */
