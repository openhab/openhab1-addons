/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.model.data;

import org.creek.mailcontrol.model.data.DecimalData;
import org.creek.mailcontrol.model.types.DecimalDataType;
import org.openhab.core.library.types.DecimalType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class OpenhabDecimalData extends OpenhabData<DecimalDataType, DecimalData> implements OpenhabCommandTransformable<DecimalType> {
    public OpenhabDecimalData(DecimalData data) {
        super(data);
    }

    @Override
    public DecimalType getCommandValue() {
        return new DecimalType(data.toString());
    }
}
