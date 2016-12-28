/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.km200.internal;

/**
 * The KM200ServiceTypes enum is representing the known main service paths
 *
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */

public enum KM200ServiceTypes {
    DHWCIRCUITS {
        @Override
        public String getDescription() {
            return "/dhwCircuits";
        }
    },
    GATEWAY {
        @Override
        public String getDescription() {
            return "/gateway";
        }
    },
    HEATINGCIRCUITS {
        @Override
        public String getDescription() {
            return "/heatingCircuits";
        }
    },
    HEATSOURCES {
        @Override
        public String getDescription() {
            return "/heatSources";
        }
    },
    NOTIFICATIONS {
        @Override
        public String getDescription() {
            return "/notifications";
        }
    },
    RECORDINGS {
        @Override
        public String getDescription() {
            return "/recordings";
        }
    },
    SOLARCIRCUITS {
        @Override
        public String getDescription() {
            return "/solarCircuits";
        }
    },
    SYSTEM {
        @Override
        public String getDescription() {
            return "/system";
        }
    };

    public abstract String getDescription();

}
