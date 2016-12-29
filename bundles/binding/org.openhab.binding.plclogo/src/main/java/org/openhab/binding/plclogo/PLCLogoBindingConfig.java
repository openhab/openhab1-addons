/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo;

import org.openhab.binding.plclogo.internal.PLCLogoBinding;
import org.openhab.binding.plclogo.internal.PLCLogoMemoryConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding configuration for PLCLogo items
 *
 * @author Lehane Kellett
 * @author Vladimir Grebenschikov
 * @author Alexander Falkenstern
 * @since 1.9.0
 */

public class PLCLogoBindingConfig implements BindingConfig {
    private Item item;
    private final String controller;
    private final PLCLogoMemoryConfig rdMem;
    private final PLCLogoMemoryConfig wrMem;
    private boolean invert = false;
    private int analogDelta = 0;
    private int lastValue = 0;
    private boolean isset = false;

    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);

    public PLCLogoBindingConfig(Item item, String configString) throws BindingConfigParseException {
        this.item = item;

        // the config string has the format
        //
        // instancename:memloc.bit [activelow:yes|no]
        //
        String shouldBe = "should be controllername:memloc[.bit] [activelow:yes|no]";
        String[] segments = configString.split(" ");
        if (segments.length > 2) {
            throw new BindingConfigParseException("invalid item format: " + configString + ", " + shouldBe);
        }
        String[] dev = segments[0].split(":");
        if (dev.length < 2) {
            throw new BindingConfigParseException("invalid item name/memory format: " + configString + ", " + shouldBe);
        }

        controller = dev[0];
        rdMem = new PLCLogoMemoryConfig(dev[1]);
        if (dev.length == 3) {
            wrMem = new PLCLogoMemoryConfig(dev[2]);
        } else {
            wrMem = rdMem;
        }

        // check for invert or analogdelta
        if (segments.length == 2) {
            logger.debug("Addtional binding config {}", segments[1]);
            String[] parts = segments[1].split("=");
            if (parts.length != 2) {
                throw new BindingConfigParseException("invalid second parameter: " + configString + ", " + shouldBe);
            }

            if (parts[0].equalsIgnoreCase("activelow")) {
                invert = parts[1].equalsIgnoreCase("yes");
            }
            if (parts[0].equalsIgnoreCase("analogdelta")) {
                analogDelta = Integer.parseInt(parts[1]);
                logger.debug("Setting analogDelta {}", analogDelta);
            }
        }
    }

    public String getController() {
        return controller;
    }

    public PLCLogoMemoryConfig getRD() {
        return rdMem;
    }

    public PLCLogoMemoryConfig getWR() {
        return wrMem;
    }

    public int getAnalogDelta() {
        return this.analogDelta;
    }

    public boolean getInvert() {
        return this.invert;
    }

    public int getLastValue() {
        return this.lastValue;
    }

    public void setLastValue(int lastValue) {
        this.lastValue = lastValue;
        this.isset = true;
    }

    public boolean isSet() {
        return this.isset;
    }

    public Item getItem() {
        return this.item;
    }
}
