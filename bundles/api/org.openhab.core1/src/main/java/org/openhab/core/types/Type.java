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
package org.openhab.core.types;

import java.util.Formatter;

/**
 * This is a parent interface for all states and commands.
 * It was introduced as many states can be commands at the same time and
 * vice versa. E.g a light can have the state ON or OFF and one can
 * also send ON and OFF as commands to the device. This duality is
 * captured by this marker interface and allows implementing classes
 * to be both state and command at the same time.
 *
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public interface Type {

    /**
     * Formats the value of this type according to a pattern (see {@link Formatter}).
     *
     * @param pattern the pattern to use
     * @return the formatted string
     */
    public String format(String pattern);

}
