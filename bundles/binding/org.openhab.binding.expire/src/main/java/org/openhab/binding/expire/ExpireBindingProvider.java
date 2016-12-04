/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.expire;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * @author Michael Wyraz
 * @since 1.9.0
 */
public interface ExpireBindingProvider extends BindingProvider {
    public String getDurationString(String itemName);

    public long getDuration(String itemName);

    public State getExpireState(String itemName);

    public Command getExpireCommand(String itemName);
}
