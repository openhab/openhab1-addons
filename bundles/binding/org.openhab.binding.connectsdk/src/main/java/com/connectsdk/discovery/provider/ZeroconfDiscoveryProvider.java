/*
 * ZeroconfDiscoveryProvider
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 18 Apr 2014
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

package com.connectsdk.discovery.provider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import android.content.Context;
import android.util.Log;

import com.connectsdk.core.Util;
import com.connectsdk.discovery.DiscoveryFilter;
import com.connectsdk.discovery.DiscoveryProvider;
import com.connectsdk.discovery.DiscoveryProviderListener;
import com.connectsdk.service.config.ServiceDescription;

public class ZeroconfDiscoveryProvider implements DiscoveryProvider {
    private static final String HOSTNAME = "connectsdk";

    JmDNS jmdns;
    InetAddress srcAddress;

    private Timer scanTimer;

    List<DiscoveryFilter> serviceFilters;

    ConcurrentHashMap<String, ServiceDescription> foundServices;
    CopyOnWriteArrayList<DiscoveryProviderListener> serviceListeners;

    boolean isRunning = false;

    ServiceListener jmdnsListener = new ServiceListener() {

        @Override
        public void serviceResolved(ServiceEvent ev) {
            @SuppressWarnings("deprecation")
            String ipAddress = ev.getInfo().getHostAddress();
            if (!Util.isIPv4Address(ipAddress)) {
                // Currently, we only support ipv4
                return;
            }

            String friendlyName = ev.getInfo().getName();
            int port = ev.getInfo().getPort();

            ServiceDescription foundService = foundServices.get(ipAddress);

            boolean isNew = foundService == null;
            boolean listUpdateFlag = false;

            if (isNew) {
                foundService = new ServiceDescription();
                foundService.setUUID(ipAddress);
                foundService.setServiceFilter(ev.getInfo().getType());
                foundService.setIpAddress(ipAddress);
                foundService.setServiceID(serviceIdForFilter(ev.getInfo().getType()));
                foundService.setPort(port);
                foundService.setFriendlyName(friendlyName);

                listUpdateFlag = true;
            }
            else {
                if (!foundService.getFriendlyName().equals(friendlyName)) {
                    foundService.setFriendlyName(friendlyName);
                    listUpdateFlag = true;
                }
            }

            if (foundService != null)
                foundService.setLastDetection(new Date().getTime());

            foundServices.put(ipAddress, foundService);

            if (listUpdateFlag) {
                for (DiscoveryProviderListener listener: serviceListeners) {
                    listener.onServiceAdded(ZeroconfDiscoveryProvider.this, foundService);
                }
            }
        }

        @Override
        public void serviceRemoved(ServiceEvent ev) {
            @SuppressWarnings("deprecation")
            String uuid = ev.getInfo().getHostAddress();
            final ServiceDescription service = foundServices.get(uuid);

            if (service != null) {
                Util.runOnUI(new Runnable() {

                    @Override
                    public void run() {
                        for (DiscoveryProviderListener listener : serviceListeners) {
                            listener.onServiceRemoved(ZeroconfDiscoveryProvider.this, service);
                        }
                    }
                });
            }
        }

        @Override
        public void serviceAdded(ServiceEvent event) {
            // Required to force serviceResolved to be called again
            // (after the first search)
            jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
        }
    };

    public ZeroconfDiscoveryProvider(Context context) {
        foundServices = new ConcurrentHashMap<String, ServiceDescription>(8, 0.75f, 2);

        serviceListeners = new CopyOnWriteArrayList<DiscoveryProviderListener>();
        serviceFilters = new CopyOnWriteArrayList<DiscoveryFilter>();

        try {
            srcAddress = Util.getIpAddress(context);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if (isRunning)
            return;

        isRunning = true;

        scanTimer = new Timer();
        scanTimer.schedule(new MDNSSearchTask(), 100, RESCAN_INTERVAL);
    }

    protected JmDNS createJmDNS() throws IOException {
        if (srcAddress != null)
            return JmDNS.create(srcAddress, HOSTNAME);
        else
            return null;
    }

    private class MDNSSearchTask extends TimerTask {

        @Override
        public void run() {
            List<String> killKeys = new ArrayList<String>();

            long killPoint = new Date().getTime() - TIMEOUT;

            for (String key : foundServices.keySet()) {
                ServiceDescription service = foundServices.get(key);
                if (service == null || service.getLastDetection() < killPoint) {
                    killKeys.add(key);
                }
            }

            for (String key : killKeys) {
                final ServiceDescription service = foundServices.get(key);

                if (service != null) {
                    Util.runOnUI(new Runnable() {

                        @Override
                        public void run() {
                            for (DiscoveryProviderListener listener : serviceListeners) {
                                listener.onServiceRemoved(ZeroconfDiscoveryProvider.this, service);
                            }
                        }
                    });
                }

                if (foundServices.containsKey(key))
                    foundServices.remove(key);
            }

            rescan();
        }
    }

    @Override
    public void stop() {
        isRunning = false;

        if (scanTimer != null) {
            scanTimer.cancel();
            scanTimer = null;
        }

        if (jmdns != null) {
            for (DiscoveryFilter searchTarget : serviceFilters) {
                String filter = searchTarget.getServiceFilter();
                jmdns.removeServiceListener(filter, jmdnsListener);
            }
        }
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    public void reset() {
        stop();
        foundServices.clear();
    }

    @Override
    public void rescan() {
        try {
            if (jmdns != null) {
                jmdns.close();
                jmdns = null;
            }
            jmdns = createJmDNS();

            if (jmdns != null) {
                for (DiscoveryFilter searchTarget : serviceFilters) {
                    String filter = searchTarget.getServiceFilter();
                    jmdns.addServiceListener(filter, jmdnsListener);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addListener(DiscoveryProviderListener listener) {
        serviceListeners.add(listener);
    }

    @Override
    public void removeListener(DiscoveryProviderListener listener) {
        serviceListeners.remove(listener);
    }

    @Override
    public void addDeviceFilter(DiscoveryFilter filter) {
        if (filter.getServiceFilter() == null) {
            Log.e(Util.T, "This device filter does not have zeroconf filter info");
        } else {
            serviceFilters.add(filter);
        }
    }

    @Override
    public void removeDeviceFilter(DiscoveryFilter filter) {
        serviceFilters.remove(filter);
    }

    @Override
    public void setFilters(List<DiscoveryFilter> filters) {
        serviceFilters = filters;
    }

    @Override
    public boolean isEmpty() {
        return serviceFilters.size() == 0;
    }

    public String serviceIdForFilter(String filter) {
        String serviceId = "";

        for (DiscoveryFilter serviceFilter : serviceFilters) {
            String ssdpFilter = serviceFilter.getServiceFilter();
            if (ssdpFilter.equals(filter)) {
                return serviceFilter.getServiceId();
            }
        }

        return serviceId;
    }
}