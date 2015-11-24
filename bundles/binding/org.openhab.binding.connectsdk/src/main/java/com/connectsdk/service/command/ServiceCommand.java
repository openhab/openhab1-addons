/*
 * ServiceCommand
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

package com.connectsdk.service.command;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONObject;

import com.connectsdk.service.capability.listeners.ResponseListener;

/**
 * Internal implementation of ServiceCommand for URL-based commands
 */
public class ServiceCommand<T extends ResponseListener<? extends Object>> {
    public static final String TYPE_REQ = "request";
    public static final String TYPE_SUB = "subscribe";
    public static final String TYPE_GET = "GET";
    public static final String TYPE_POST = "POST";
    public static final String TYPE_DEL = "DELETE";
    public static final String TYPE_PUT = "PUT";

    protected ServiceCommandProcessor processor;
    protected String httpMethod; // WebOSTV: {request, subscribe}, NetcastTV: {GET, POST}
    protected Object payload;
    protected String target;

    int requestId;

    ResponseListener<Object> responseListener;

    public ServiceCommand(ServiceCommandProcessor processor, String targetURL, Object payload, ResponseListener<Object> listener) {
        this.processor = processor;
        this.target = targetURL;
        this.payload = payload;
        this.responseListener = listener;
        this.httpMethod = TYPE_POST;
    }

    public ServiceCommand(ServiceCommandProcessor processor, String uri, JSONObject payload, boolean isWebOS, ResponseListener<Object> listener) {
        this.processor = processor;
        target = uri;
        this.payload = payload;
        requestId = -1;
        httpMethod = "request";
        responseListener = listener;
    }

    public void send() {
        processor.sendCommand(this);
    }

    public ServiceCommandProcessor getCommandProcessor() {
        return processor;
    }

    public void setCommandProcessor(ServiceCommandProcessor processor) {
        this.processor = processor;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getTarget() { 
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public HttpRequestBase getRequest() {
        if (target == null) {
            throw new IllegalStateException("ServiceCommand has no target url");
        }

        if (this.httpMethod.equalsIgnoreCase(TYPE_GET)) {
            return new HttpGet(target);
        } else if (this.httpMethod.equalsIgnoreCase(TYPE_POST)) {
            return new HttpPost(target);
        } else if (this.httpMethod.equalsIgnoreCase(TYPE_DEL)) {
            return new HttpDelete(target);
        } else if (this.httpMethod.equalsIgnoreCase(TYPE_PUT)) {
            return new HttpPut(target);
        } else {
            return null;
        }
    }

    public ResponseListener<Object> getResponseListener() {
        return responseListener;
    }

    public interface ServiceCommandProcessor {
        public void unsubscribe(URLServiceSubscription<?> subscription);
        public void unsubscribe(ServiceSubscription<?> subscription);
        public void sendCommand(ServiceCommand<?> command);
    }
}