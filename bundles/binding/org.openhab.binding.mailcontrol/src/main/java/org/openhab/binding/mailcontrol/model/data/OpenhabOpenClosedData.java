/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class OpenhabOpenClosedData extends OpenhabData<OpenClosedDataType, OpenClosedData> implements OpenhabCommandTransformable<OpenClosedType> {
    public OpenhabOpenClosedData(OpenClosedData data) {
        super(data);
    }

    @Override
    public OpenClosedType getCommandValue() {
        return OpenClosedType.valueOf(data.name());
    }
}
