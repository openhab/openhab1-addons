/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.horizon;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Jurgen Kuijpers
 * @since 1.9.0
 */
public interface HorizonBindingProvider extends BindingProvider {

    /**
     * This method returns the corresponding horizon keycommand for the given itemCommand and itemName
     */
    String getHorizonCommand(String itemName, String command);

}
