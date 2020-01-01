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
