/*
 * PairingDialog
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

import com.connectsdk.service.DeviceService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;


public class PairingDialog {
    Activity activity;
    ConnectableDevice device;

    public PairingDialog(Activity activity, ConnectableDevice device) {
        this.activity = activity;
        this.device = device;
    }

    public AlertDialog getSimplePairingDialog(int titleResId, int messageResId) {
        return new AlertDialog.Builder(activity)
        .setTitle(titleResId)
        .setMessage(messageResId)
        .setPositiveButton(android.R.string.cancel, null)
        .create();
    }

    public AlertDialog getPairingDialog(int resId) {
        return getPairingDialog(activity.getString(resId));
    }

    public AlertDialog getPairingDialog(String message) {
        TextView title = (TextView) activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
        title.setText(message);

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        return new AlertDialog.Builder(activity)
        .setCustomTitle(title)
        .setView(input)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                for (DeviceService service : device.getServices())
                    service.sendPairingKey(value);
            }
        })
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                // pickerDialog.dismiss();
            }
        })
        .create();
    }
}
