/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

/**
 * Interface to implement for all command classes that implement the Indicator
 * commands like GET/SET value.
 * @author Pedro Paixao
 * @since 1.8.0
 */
public interface ZWaveIndicatorCommands extends ZWaveGetCommands, ZWaveSetCommands {
}
