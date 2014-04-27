/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConverterInstanciation.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
public class ConverterInstanciation {

    private static final Logger logger = LoggerFactory.getLogger(ConverterInstanciation.class);

    public static <T> T instantiate(Class<T> converter) {
        if (converter == null) {
            return null;
        }
        try {
            return converter.newInstance();
        } catch (InstantiationException e) {
            logger.error("Could not instanciate " + converter, e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("Could not instanciate " + converter, e);
            throw new RuntimeException(e);
        }
    }

}
