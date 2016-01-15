/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064;

import org.openhab.binding.fritzboxtr064.internal.FritzboxTr064GenericBindingProvider.FritzboxTr064BindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Boris Bock
 * @since 0.1.0
 */
public interface FritzboxTr064BindingProvider extends BindingProvider {

    public FritzboxTr064BindingConfig getBindingConfigByItemName(String itemName);

}
