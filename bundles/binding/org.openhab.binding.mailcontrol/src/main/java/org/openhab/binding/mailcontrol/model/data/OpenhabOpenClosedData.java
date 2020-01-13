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

import org.creek.mailcontrol.model.data.OpenClosedData;
import org.creek.mailcontrol.model.types.OpenClosedDataType;
import org.openhab.core.library.types.OpenClosedType;

/**
 *
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class OpenhabOpenClosedData extends OpenhabData<OpenClosedDataType, OpenClosedData>
        implements OpenhabCommandTransformable<OpenClosedType> {
    public OpenhabOpenClosedData(OpenClosedData data) {
        super(data);
    }

    @Override
    public OpenClosedType getCommandValue() {
        return OpenClosedType.valueOf(data.name());
    }
}
