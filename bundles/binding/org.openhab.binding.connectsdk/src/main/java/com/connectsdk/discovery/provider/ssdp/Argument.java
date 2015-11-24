/*
 * Argument
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Copyright (c) 2011 stonker.lee@gmail.com https://code.google.com/p/android-dlna/
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

package com.connectsdk.discovery.provider.ssdp;

public class Argument {
    public static final String TAG = "argument";
    public static final String TAG_NAME = "name";
    public static final String TAG_DIRECTION = "direction";
    public static final String TAG_RETVAL = "retval";
    public static final String TAG_RELATED_STATE_VARIABLE = "relatedStateVariable";

    /* Required. Name of formal parameter. */
    String mName;
    /* Required. Defines whether argument is an input or output paramter. */
    String mDirection;
    /* Optional. Identifies at most one output argument as the return value. */
    String mRetval;
    /* Required. Must be the same of a state variable. */
    String mRelatedStateVariable;
}