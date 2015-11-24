/*
 * DeviceServiceReachability
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Jeffrey Glenn on 16 Apr 2014
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

package com.connectsdk.etc.helper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DeviceServiceReachability {
    private static final int TIMEOUT = 10000;
    private InetAddress ipAddress;
    private Thread testThread;

    private DeviceServiceReachabilityListener listener;

    public DeviceServiceReachability() { }

    public DeviceServiceReachability(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public DeviceServiceReachability(InetAddress ipAddress, DeviceServiceReachabilityListener listener) {
        this.ipAddress = ipAddress;
        this.listener = listener;
    }

    public static DeviceServiceReachability getReachability(InetAddress ipAddress, DeviceServiceReachabilityListener listener) {
        return new DeviceServiceReachability(ipAddress, listener);
    }

    public static DeviceServiceReachability getReachability(final String ipAddress, DeviceServiceReachabilityListener listener) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            return null;
        }
        return getReachability(addr, listener);
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isRunning() {
        return testThread != null && testThread.isAlive();
    }

    public DeviceServiceReachabilityListener getListener() {
        return listener;
    }

    public void setListener(DeviceServiceReachabilityListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (isRunning())
            return;

        testThread = new Thread(testReachability);
        testThread.start();
    }

    public void stop() {
        if (!isRunning())
            return;

        testThread.interrupt();
        testThread = null;
    }

    private void unreachable() {
        stop();

        if (listener != null)
            listener.onLoseReachability(this);
    }

    private Runnable testReachability = new Runnable() {

        @Override
        public void run() {
            try {
                while (true) {
                    if (!ipAddress.isReachable(TIMEOUT))
                        unreachable();
                    Thread.sleep(TIMEOUT);
                }
            } catch (IOException e) {
                unreachable();
            } catch (InterruptedException e) { }
        }
    };

    public interface DeviceServiceReachabilityListener {
        public void onLoseReachability(DeviceServiceReachability reachability);
    }
}
