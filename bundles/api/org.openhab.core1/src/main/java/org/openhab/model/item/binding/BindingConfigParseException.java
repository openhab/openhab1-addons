/**
 * Copyright (c) 2015-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.item.binding;

/**
 *
 * @author Kai Kreuzer - Initial contribution
 */
public class BindingConfigParseException extends Exception {

    private static final long serialVersionUID = 1434607160082879845L;

    public BindingConfigParseException(String msg) {
        super(msg);
    }

}
