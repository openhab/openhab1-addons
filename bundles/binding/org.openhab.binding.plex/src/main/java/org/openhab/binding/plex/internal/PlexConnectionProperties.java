/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the connection properties for a Plex server
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class PlexConnectionProperties {

    private static final Logger logger = LoggerFactory.getLogger(PlexConnectionProperties.class);

    private String host;

    private int port = 32400;

    private String token;

    private URI uri;

    private String username;

    private String password;

    private PlexApiLevel apiLevel = PlexApiLevel.getLatest();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setUri(String uri) {
        if (!StringUtils.isBlank(uri)) {
            try {
                this.uri = new URI(uri);
            } catch (URISyntaxException e) {
                logger.debug(String.format("Error parsing uri %s", uri), e);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PlexApiLevel getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(PlexApiLevel apiLevel) {
        this.apiLevel = apiLevel;
    }

    public boolean hasToken() {
        return !StringUtils.isBlank(getToken());
    }

}
