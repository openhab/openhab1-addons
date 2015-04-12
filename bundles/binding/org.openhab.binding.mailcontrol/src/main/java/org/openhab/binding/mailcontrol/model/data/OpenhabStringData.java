/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class OpenhabStringData extends OpenhabData<StringDataType, StringData> implements OpenhabCommandTransformable<StringType> {
    public OpenhabStringData(StringData data) {
        super(data);
    }

    @Override
    public StringType getCommandValue() {
        return new StringType(data.toString());
    }
}
