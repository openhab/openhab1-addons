/*
 * TextInputStatusInfo
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 19 Jan 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.core;

import org.json.JSONObject;

/** Normalized reference object for information about a text input event. */
public class TextInputStatusInfo {
    // @cond INTERNAL
    public enum TextInputType {
        DEFAULT,
        URL,
        NUMBER,
        PHONE_NUMBER,
        EMAIL
    }

    boolean focused = false;
    String contentType = null;
    boolean predictionEnabled = false;
    boolean correctionEnabled = false;
    boolean autoCapitalization = false;
    boolean hiddenText = false;
    boolean focusChanged = false;

    JSONObject rawData;
    // @endcond

    public TextInputStatusInfo() {
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    /** Gets the type of keyboard that should be displayed to the user. */
    public TextInputType getTextInputType() {
        TextInputType textInputType = TextInputType.DEFAULT;

        if (contentType != null) {
            if (contentType.equals("number")) {
                textInputType = TextInputType.NUMBER;
            }
            else if (contentType.equals("phonenumber")) {
                textInputType = TextInputType.PHONE_NUMBER;
            }
            else if (contentType.equals("url")) {
                textInputType = TextInputType.URL;
            }
            else if (contentType.equals("email")) {
                textInputType = TextInputType.EMAIL;
            }
        }

        return textInputType;
    }

    /** Sets the type of keyboard that should be displayed to the user. */
    public void setTextInputType(TextInputType textInputType) {
        switch (textInputType) {
        case NUMBER:
            contentType = "number";
            break;
        case PHONE_NUMBER:
            contentType = "phonenumber";
            break;
        case URL:
            contentType = "url";
            break;
        case EMAIL:
            contentType = "number";
            break;
        case DEFAULT:
        default:
            contentType = "email";
            break;
        }
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isPredictionEnabled() {
        return predictionEnabled;
    }

    public void setPredictionEnabled(boolean predictionEnabled) {
        this.predictionEnabled = predictionEnabled;
    }

    public boolean isCorrectionEnabled() {
        return correctionEnabled;
    }

    public void setCorrectionEnabled(boolean correctionEnabled) {
        this.correctionEnabled = correctionEnabled;
    }

    public boolean isAutoCapitalization() {
        return autoCapitalization;
    }

    public void setAutoCapitalization(boolean autoCapitalization) {
        this.autoCapitalization = autoCapitalization;
    }

    public boolean isHiddenText() {
        return hiddenText;
    }

    public void setHiddenText(boolean hiddenText) {
        this.hiddenText = hiddenText;
    }

    /** Gets the raw data from the first screen device about the text input status. */
    public JSONObject getRawData() {
        return rawData;
    }

    /** Sets the raw data from the first screen device about the text input status. */
    public void setRawData(JSONObject data) {
        rawData = data;
    }

    public boolean isFocusChanged() {
        return focusChanged;
    }

    public void setFocusChanged(boolean focusChanged) {
        this.focusChanged = focusChanged;
    }
}
