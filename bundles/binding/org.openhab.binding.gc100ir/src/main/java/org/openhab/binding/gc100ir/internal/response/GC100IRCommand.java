/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gc100ir.internal.response;

/**
 * An interface class defining commands from a GC100IR device.
 *
 * @author Stephen Liang
 * @since 1.9.0
 *
 */
public interface GC100IRCommand {
    /**
     * Gets the command code associated with this command. Example codes are completeir, unknowncommand, sendir.
     *
     * @return A GC100IRCommandCode, such as completeir.
     */
    public GC100IRCommandCode getCommandCode();
}
