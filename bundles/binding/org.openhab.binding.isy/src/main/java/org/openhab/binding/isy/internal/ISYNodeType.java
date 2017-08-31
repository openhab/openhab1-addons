/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy.internal;

/**
 * These are the node types in ISY that are supported / translated.
 * 
 * @author Jon Bullen
 * @since 1.10.0
 */
public enum ISYNodeType {
    SWITCH,
    GROUP,
    CONTACT,
    THERMOSTAT,
    NUMBER,
    STRING,
    DIMMER,
    LOCK,
    HEARTBEAT
}
