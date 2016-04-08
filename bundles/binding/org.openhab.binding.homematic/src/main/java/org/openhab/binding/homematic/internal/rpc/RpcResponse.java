/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.rpc;

/**
 * A RPC response definition for reveiving data from the Homematic server.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public interface RpcResponse {

    /**
     * Returns the decoded methodName.
     */
    public String getMethodName();

    /**
     * Returns the decoded data.
     */
    public Object[] getResponseData();

}
