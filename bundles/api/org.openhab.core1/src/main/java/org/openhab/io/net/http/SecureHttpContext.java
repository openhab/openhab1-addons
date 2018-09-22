/**
 * Copyright (c) 2015-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.net.http;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.http.HttpContext;

/**
 * Implementation of {@link HttpContext} which adds Basic-Authentication
 * functionality to openHAB.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class SecureHttpContext implements HttpContext {

    private HttpContext defaultContext = null;

    public SecureHttpContext() {
    }

    public SecureHttpContext(HttpContext defaultContext, final String realm) {
        this.defaultContext = defaultContext;
    }

    /**
     * <p>
     * 
     * @{inheritDoc}
     *               </p>
     *               <p>
     *               Delegates to <code>defaultContext.getMimeType()</code>
     */
    @Override
    public String getMimeType(String name) {
        return this.defaultContext.getMimeType(name);
    }

    /**
     * <p>
     * 
     * @{inheritDoc}
     *               </p>
     *               <p>
     *               Delegates to <code>defaultContext.getResource()</code>
     */
    @Override
    public URL getResource(String name) {
        return this.defaultContext.getResource(name);
    }

    @Override
    public boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return true;
    }

}
