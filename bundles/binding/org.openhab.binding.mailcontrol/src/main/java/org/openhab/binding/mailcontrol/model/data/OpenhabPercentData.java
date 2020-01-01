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
package org.openhab.binding.mailcontrol.model.data;

import org.creek.mailcontrol.model.data.PercentData;
import org.creek.mailcontrol.model.types.PercentDataType;
import org.openhab.core.library.types.PercentType;

/**
 *
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class OpenhabPercentData extends OpenhabData<PercentDataType, PercentData>
        implements OpenhabCommandTransformable<PercentType> {
    public OpenhabPercentData(PercentData data) {
        super(data);
    }

    @Override
    public PercentType getCommandValue() {
        return new PercentType(data.toString());
    }
}
