/*
 * Service
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

import java.util.List;

//import com.connectsdk.core.upnp.parser.Parser;

public class Service {
    public static final String TAG = "service";
    public static final String TAG_SERVICE_TYPE = "serviceType";
    public static final String TAG_SERVICE_ID = "serviceId";
    public static final String TAG_SCPD_URL = "SCPDURL";
    public static final String TAG_CONTROL_URL = "controlURL";
    public static final String TAG_EVENTSUB_URL = "eventSubURL";

    public String baseURL;
    /* Required. UPnP service type. */
    public String serviceType;
    /* Required. Service identifier. */
    public String serviceId;
    /* Required. Relative URL for service description. */
    public String SCPDURL;
    /* Required. Relative URL for control. */
    public String controlURL;
    /* Relative. Relative URL for eventing. */
    public String eventSubURL;

    public List<Action> actionList;
    public List<StateVariable> serviceStateTable;

    /*
     * We don't get SCPD, control and eventSub descriptions at service creation.
     * So call this method first before you use the service.
     */
    public void init() {
//        Parser parser = Parser.getInstance();
    }
}