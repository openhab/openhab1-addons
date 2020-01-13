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
package org.openhab.binding.zwave.internal.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * Implements a label class for the XML product database
 *
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "Label" })
public class ZWaveDbLabel {
    @XStreamAlias("lang")
    String Language;

    String Label;
}
