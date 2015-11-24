/*
 * ResponseListener
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

package com.connectsdk.service.capability.listeners;

/**
 * Generic asynchronous operation response success handler block. If there is any response data to be processed, it will be provided via the responseObject parameter.
 * 
 * @param responseObject Contains the output data as a generic object reference. This value may be any of a number of types as defined by T in subclasses of ResponseListener. It is also possible that responseObject will be nil for operations that don't require data to be returned (move mouse, send key code, etc).
 */
public interface ResponseListener<T> extends ErrorListener {

    /**
     * Returns the success of the call of type T.
     * 
     * @param object Response object, can be any number of object types, depending on the protocol/capability/etc
     */
    abstract public void onSuccess(T object);
}
