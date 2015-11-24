/*
 * DevicePickerAdaper
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

import java.util.HashMap;

import com.connectsdk.discovery.DiscoveryManager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class DevicePickerAdapter extends ArrayAdapter<ConnectableDevice> {
    int resource, textViewResourceId, subTextViewResourceId;
    HashMap<String, ConnectableDevice> currentDevices = new HashMap<String, ConnectableDevice>();
    Context context;

    DevicePickerAdapter(Context context) {
        this(context, android.R.layout.simple_list_item_2);
    }

    DevicePickerAdapter(Context context, int resource) {
        this(context, resource, android.R.id.text1, android.R.id.text2);
    }

    DevicePickerAdapter(Context context, int resource, int textViewResourceId, int subTextViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.subTextViewResourceId = subTextViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = View.inflate(getContext(), resource, null);
        }

        ConnectableDevice device = this.getItem(position);
        String text;
        if (device.getFriendlyName() != null) {
            text = device.getFriendlyName();
        }
        else {
            text = device.getModelName();
        }

        view.setBackgroundColor(Color.BLACK);

        TextView textView = (TextView) view.findViewById(textViewResourceId);
        textView.setText(text);
        textView.setTextColor(Color.WHITE);

        boolean isDebuggable =  (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        boolean hasNoFilters = DiscoveryManager.getInstance().getCapabilityFilters().size() == 0;

        String serviceNames = device.getConnectedServiceNames();
        boolean hasServiceNames = (serviceNames != null && serviceNames.length() > 0);

        boolean shouldShowServiceNames = hasServiceNames && (isDebuggable || hasNoFilters);

        TextView subTextView = (TextView) view.findViewById(subTextViewResourceId);

        if (shouldShowServiceNames) {
            subTextView.setText(serviceNames);
            subTextView.setTextColor(Color.WHITE);
        } else {
            subTextView.setText(null);
        }

        return view;
    }
}
