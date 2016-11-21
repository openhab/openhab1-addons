/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.converter.property;

import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Date converter with a UTC pattern.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public class JsonStandardDateConverter extends AbstractDateConverter {
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX";

    public JsonStandardDateConverter() {
        super(DATE_PATTERN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConverterType getType() {
        return ConverterType.JSON_DATE;
    }

}
