/*
 * WebOSTVKeyboardInput
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

package com.connectsdk.service.webos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.connectsdk.core.TextInputStatusInfo;
import com.connectsdk.core.Util;
import com.connectsdk.service.WebOSTVService;
import com.connectsdk.service.capability.TextInputControl.TextInputStatusListener;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceCommand;
import com.connectsdk.service.command.URLServiceSubscription;

public class WebOSTVKeyboardInput {

    WebOSTVService service;
    boolean waiting;
    List<String> toSend;

    static String KEYBOARD_INPUT = "ssap://com.webos.service.ime/registerRemoteKeyboard";
    static String ENTER = "ENTER";
    static String DELETE = "DELETE";

    boolean canReplaceText = false;

    public WebOSTVKeyboardInput(WebOSTVService service) {
        this.service = service;
        waiting = false;

        toSend = new ArrayList<String>();
    }

    public void addToQueue(String input) {
        toSend.add(input);
        if (!waiting) {
            sendData();
        }
    }

    public void sendEnter() {
        toSend.add(ENTER);
        if (!waiting) {
            sendData();
        }
    }

    public void sendDel() {
        if (toSend.size() == 0) {
            toSend.add(DELETE);
            if (!waiting) {
                sendData();
            }
        }
        else {
            toSend.remove(toSend.size()-1);
        }
    }

    private void sendData() {
        waiting = true;

        String uri;
        String typeTest = toSend.get(0);

        JSONObject payload = new JSONObject();

        if (typeTest.equals(ENTER)) {
            toSend.remove(0);
            uri = "ssap://com.webos.service.ime/sendEnterKey";
        }
        else if (typeTest.equals(DELETE)) {
            uri = "ssap://com.webos.service.ime/deleteCharacters";

            int count = 0;
            while (toSend.size() > 0 && toSend.get(0).equals(DELETE)) {
                toSend.remove(0);
                count++;
            }

            try {
                payload.put("count", count);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            uri = "ssap://com.webos.service.ime/insertText";
            StringBuilder sb = new StringBuilder();

            while (toSend.size() > 0 && !(toSend.get(0).equals(DELETE) || toSend.get(0).equals(ENTER))) {
                String text = toSend.get(0);
                sb.append(text);
                toSend.remove(0);
            }

            try {
                payload.put("text", sb.toString());
                payload.put("replace", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                waiting = false;
                if (toSend.size() > 0) 
                    sendData();
            }

            @Override
            public void onError(ServiceCommandError error) {
                waiting = false;
                if (toSend.size() > 0) 
                    sendData();
            }
        };

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(service, uri, payload, true, responseListener);
        request.send();
    }

    public URLServiceSubscription<TextInputStatusListener> connect(final TextInputStatusListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                JSONObject jsonObj = (JSONObject)response;

                TextInputStatusInfo keyboard = parseRawKeyboardData(jsonObj);

                Util.postSuccess(listener, keyboard);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        URLServiceSubscription<TextInputStatusListener> subscription = new URLServiceSubscription<TextInputStatusListener>(service, KEYBOARD_INPUT, null, true, responseListener);
        subscription.send();

        return subscription;
    }

    private TextInputStatusInfo parseRawKeyboardData(JSONObject rawData) {
        boolean focused = false;
        String contentType = null;
        boolean predictionEnabled = false;
        boolean correctionEnabled = false;
        boolean autoCapitalization = false;
        boolean hiddenText = false;
        boolean focusChanged = false;

        TextInputStatusInfo keyboard = new TextInputStatusInfo();
        keyboard.setRawData(rawData);

        try {
            if (rawData.has("currentWidget")) {
                JSONObject currentWidget = (JSONObject) rawData.get("currentWidget");
                focused = (Boolean) currentWidget.get("focus");

                if (currentWidget.has("contentType")) {
                    contentType = (String) currentWidget.get("contentType");
                }
                if (currentWidget.has("predictionEnabled")) {
                    predictionEnabled = (Boolean) currentWidget.get("predictionEnabled");
                }
                if (currentWidget.has("correctionEnabled")) {
                    correctionEnabled = (Boolean) currentWidget.get("correctionEnabled");
                }
                if (currentWidget.has("autoCapitalization")) {
                    autoCapitalization = (Boolean) currentWidget.get("autoCapitalization");
                }
                if (currentWidget.has("hiddenText")) {
                    hiddenText = (Boolean) currentWidget.get("hiddenText");
                }
            }
            if (rawData.has("focusChanged")) 
                focusChanged = (Boolean) rawData.get("focusChanged");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        keyboard.setFocused(focused);
        keyboard.setContentType(contentType);
        keyboard.setPredictionEnabled(predictionEnabled);
        keyboard.setCorrectionEnabled(correctionEnabled);
        keyboard.setAutoCapitalization(autoCapitalization);
        keyboard.setHiddenText(hiddenText);
        keyboard.setFocusChanged(focusChanged);

        return keyboard;
    }

//    public void disconnect() {
//        subscription.unsubscribe();
//    }
}
