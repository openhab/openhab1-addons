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
package org.openhab.binding.weather.internal.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.openhab.binding.weather.internal.model.ProviderName;

/**
 * This annotation marks the provider data structure for forecasts.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface Forecast {

    public ProviderName provider();

    public String property();

}
