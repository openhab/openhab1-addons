/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.model.data;

import org.creek.mailcontrol.model.data.IncreaseDecreaseData;
import org.creek.mailcontrol.model.types.IncreaseDecreaseDataType;
import org.openhab.core.library.types.IncreaseDecreaseType;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class OpenhabIncreaseDecreaseData extends OpenhabData<IncreaseDecreaseDataType, IncreaseDecreaseData> implements OpenhabCommandTransformable<IncreaseDecreaseType> {
    public OpenhabIncreaseDecreaseData(IncreaseDecreaseData data) {
        super(data);
    }

    @Override
    public IncreaseDecreaseType getCommandValue() {
        return IncreaseDecreaseType.valueOf(data.name());
    }
}
