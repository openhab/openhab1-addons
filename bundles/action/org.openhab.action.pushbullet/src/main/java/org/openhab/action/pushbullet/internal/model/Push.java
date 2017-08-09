/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.pushbullet.internal.model;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents the push request sent to the API.
 *
 * @author Hakan Tandogan
 * @since 1.11.0
 */
public class Push {

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("type")
    private String type;

    @SerializedName("email")
    private String email;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Push {" + "title='" + title + '\'' + ", body='" + body + '\'' + ", type='" + type + '\'' + ", email='"
                + email + '\'' + '}';
    }
}
