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
 * This class represents errors in the response fetched from the API.
 *
 * @author Hakan Tandogan
 * @since 1.11.0
 */
public class PushError {

    @SerializedName("type")
    private String type;

    @SerializedName("message")
    private String message;

    @SerializedName("param")
    private String param;

    @SerializedName("cat")
    private String cat;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    @Override
    public String toString() {
        return "PushError {" + "type='" + type + '\'' + ", message='" + message + '\'' + ", param='" + param + '\''
                + ", cat='" + cat + '\'' + '}';
    }
}
