/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal.communication.websocket;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 * This object holds updates received from the Plex websocket connection for api level 2
 *
 * @author Jeroen Idserda
 * @since 1.9.0
 */
@JsonRootName(value = "NotificationContainer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationContainer {

    private String type;

    private Integer size;

    @JsonProperty(value = "PlaySessionStateNotification")
    private List<PlaySessionStateNotification> stateNotifications = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<PlaySessionStateNotification> getStateNotifications() {
        return stateNotifications;
    }

    public void setStateNotifications(List<PlaySessionStateNotification> stateNotifications) {
        this.stateNotifications = stateNotifications;
    }

}
