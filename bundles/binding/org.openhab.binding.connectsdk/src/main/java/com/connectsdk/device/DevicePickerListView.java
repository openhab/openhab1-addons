/*
 * DevicePickerListView
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

package com.connectsdk.device;

import android.content.Context;
import android.widget.ListView;

import com.connectsdk.core.Util;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManagerListener;
import com.connectsdk.service.command.ServiceCommandError;

public class DevicePickerListView extends ListView implements DiscoveryManagerListener {
    DevicePickerAdapter pickerAdapter;

    public DevicePickerListView(Context context) {
        super(context);

        pickerAdapter = new DevicePickerAdapter(context);

        setAdapter(pickerAdapter);

        DiscoveryManager.getInstance().addListener(this);
    }

    @Override
    public void onDiscoveryFailed(DiscoveryManager manager, ServiceCommandError error) {
        Util.runOnUI(new Runnable () {
            @Override
            public void run() {
                pickerAdapter.clear();
            }
        });
    }

    @Override
    public void onDeviceAdded(DiscoveryManager manager, final ConnectableDevice device) {
        Util.runOnUI(new Runnable () {
            @Override
            public void run() {
                int index = -1;
                for (int i = 0; i < pickerAdapter.getCount(); i++) {
                    ConnectableDevice d = pickerAdapter.getItem(i);

                    String newDeviceName = device.getFriendlyName();
                    String dName = d.getFriendlyName();

                    if (newDeviceName == null) {
                        newDeviceName = device.getModelName();
                    }

                    if (dName == null) {
                        dName = d.getModelName();
                    }

                    if (d.getIpAddress().equals(device.getIpAddress())) {
                        pickerAdapter.remove(d);
                        pickerAdapter.insert(device, i);
                        return;
                    }

                    if (newDeviceName.compareToIgnoreCase(dName) < 0) {
                        index = i;
                        pickerAdapter.insert(device, index);
                        break;
                    }
                }

                if (index == -1)
                    pickerAdapter.add(device);
            }
        });
    }

    @Override
    public void onDeviceUpdated(DiscoveryManager manager, final ConnectableDevice device) {
        Util.runOnUI(new Runnable () {
            @Override
            public void run() {
                pickerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDeviceRemoved(DiscoveryManager manager, final ConnectableDevice device) {
        Util.runOnUI(new Runnable () {
            @Override
            public void run() {
                pickerAdapter.remove(device);
            }
        });
    }
}
