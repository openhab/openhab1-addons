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

import org.creek.mailcontrol.model.data.StringData;
import org.creek.mailcontrol.model.types.StringDataType;
import org.openhab.core.library.types.StringType;

/**
 *
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class OpenhabStringData extends OpenhabData<StringDataType, StringData>
        implements OpenhabCommandTransformable<StringType> {
    public OpenhabStringData(StringData data) {
        super(data);
    }

    @Override
    public StringType getCommandValue() {
        return new StringType(data.toString());
    }
}
