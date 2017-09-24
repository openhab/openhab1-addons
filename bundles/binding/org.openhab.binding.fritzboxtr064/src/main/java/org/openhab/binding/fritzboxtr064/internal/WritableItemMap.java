/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

/**
 * {@link ItemMap} for a SOAP service which supports writing values to the FritzBox.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public interface WritableItemMap extends ItemMap {

    String getWriteServiceCommand();

    String getWriteDataInName();

    String getWriteDataInNameAdditional();

}
