/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package org.openhab.binding.aleoncean.internal.converter;

import org.openhab.binding.aleoncean.internal.ActionIn;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public abstract class ParameterClassTypeClassConverter extends StandardConverter {

    public ParameterClassTypeClassConverter(final ActionIn actionIn) {
        super(actionIn);
    }

}
