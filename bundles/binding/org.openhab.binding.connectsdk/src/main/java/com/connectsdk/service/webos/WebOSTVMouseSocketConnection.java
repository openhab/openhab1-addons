/*
 * WebOSTVMouseSocketConnection
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

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import android.util.Log;

public class WebOSTVMouseSocketConnection {
    public interface WebOSTVMouseSocketListener {
        void onConnected();
    }

    WebSocketClient ws;
    String socketPath;
    WebOSTVMouseSocketListener listener;

    public enum ButtonType {
        HOME,
        BACK,
        UP,
        DOWN,
        LEFT,
        RIGHT,
    }

    public WebOSTVMouseSocketConnection(String socketPath, WebOSTVMouseSocketListener listener) {
        Log.d("PtrAndKeyboardFragment", "got socketPath: " + socketPath);

        this.listener = listener;

        if (socketPath.startsWith("wss:")) {
            this.socketPath = socketPath.replace("wss:", "ws:").replace(":3001/", ":3000/"); // downgrade to plaintext
            Log.d("PtrAndKeyboardFragment", "downgraded socketPath: " + this.socketPath);
        }
        else 
            this.socketPath = socketPath;

        try {
            URI uri = new URI(this.socketPath);
            connectPointer(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void connectPointer(URI uri) {
        if (ws != null) {
            ws.close();
            ws = null;
        }

        ws = new WebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake arg0) {
                Log.d("PtrAndKeyboardFragment", "connected to " + uri.toString());
                if (listener != null) {
                    listener.onConnected();
                }
            }

            @Override
            public void onMessage(String arg0) {
            }

            @Override
            public void onError(Exception arg0) {
            }

            @Override
            public void onClose(int arg0, String arg1, boolean arg2) {
            }
        };

        ws.connect();
    }

    public void disconnect() {
        if (ws != null) {
            ws.close();
            ws = null;
        }
    }

    public boolean isConnected() {
        if (ws == null) 
            System.out.println("ws is null");
        else if (ws.getReadyState() != READYSTATE.OPEN) {
            System.out.println("ws state is not ready");
        }
        return (ws != null) && (ws.getReadyState() == READYSTATE.OPEN);
    }

    public void click() {
        if (isConnected()) {
            ws.send("type:click\n" + "\n");
        }
    }

    public void button(ButtonType type) {
        String keyName; 
        switch (type) {
        case HOME:
            keyName = "HOME";
            break;
        case BACK:
            keyName = "BACK";
            break;
        case UP:
            keyName = "UP";
            break;
        case DOWN:
            keyName = "DOWN";
            break;
        case LEFT:
            keyName = "LEFT";
            break;
        case RIGHT:
            keyName = "RIGHT";
            break;

        default:
            keyName = "NONE";
            break;
        }

        button(keyName);
    }

    public void button(String keyName) {
        if (isConnected()) {
            ws.send("type:button\n" + "name:" + keyName + "\n" + "\n");
        }
    }

    public void move(double dx, double dy) {
        if (isConnected()) {
            ws.send("type:move\n" + "dx:" + dx + "\n" + "dy:" + dy + "\n" + "down:0\n" + "\n");
        }
    }

    public void move(double dx, double dy, boolean drag) {
        if (isConnected()) {
            ws.send("type:move\n" + "dx:" + dx + "\n" + "dy:" + dy + "\n" + "down:" + (drag ? 1 : 0) + "\n" + "\n");
        }
    }

    public void scroll(double dx, double dy) {
        if (isConnected()) {
            ws.send("type:scroll\n" + "dx:" + dx + "\n" + "dy:" + dy + "\n" + "\n");
        }
    }
}
