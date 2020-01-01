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

import org.creek.mailcontrol.model.data.HSBData;
import org.creek.mailcontrol.model.types.HSBDataType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.PercentType;

/**
 *
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class OpenhabHSBData extends OpenhabData<HSBDataType, HSBData>implements OpenhabCommandTransformable<HSBType> {
    public OpenhabHSBData(HSBData data) {
        super(data);
    }

    @Override
    public HSBType getCommandValue() {
        DecimalType h = new DecimalType(data.getHue());
        PercentType s = new PercentType((int) data.getSaturation());
        PercentType b = new PercentType((int) data.getBrightness());
        return new HSBType(h, s, b);
    }

    @Override
    public String toString() {
        return getClass().getName() + ": [data: [" + data.toString() + "]]";
    }
}
