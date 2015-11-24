/*
 * DiscoveryManager
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

package com.connectsdk.discovery;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;

import com.connectsdk.DefaultPlatform;
import com.connectsdk.core.Util;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.device.ConnectableDeviceStore;
import com.connectsdk.device.DefaultConnectableDeviceStore;
import com.connectsdk.service.DLNAService;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.DeviceService.PairingType;
import com.connectsdk.service.NetcastTVService;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.config.ServiceConfig;
import com.connectsdk.service.config.ServiceConfig.ServiceConfigListener;
import com.connectsdk.service.config.ServiceDescription;

/**
 * ###Overview
 *
 * At the heart of Connect SDK is DiscoveryManager, a multi-protocol service discovery engine with a pluggable architecture. Much of your initial experience with Connect SDK will be with the DiscoveryManager class, as it consolidates discovered service information into ConnectableDevice objects.
 *
 * ###In depth
 * DiscoveryManager supports discovering services of differing protocols by using DiscoveryProviders. Many services are discoverable over [SSDP][0] and are registered to be discovered with the SSDPDiscoveryProvider class.
 *
 * As services are discovered on the network, the DiscoveryProviders will notify DiscoveryManager. DiscoveryManager is capable of attributing multiple services, if applicable, to a single ConnectableDevice instance. Thus, it is possible to have a mixed-mode ConnectableDevice object that is theoretically capable of more functionality than a single service can provide.
 *
 * DiscoveryManager keeps a running list of all discovered devices and maintains a filtered list of devices that have satisfied any of your CapabilityFilters. This filtered list is used by the DevicePicker when presenting the user with a list of devices.
 *
 * Only one instance of the DiscoveryManager should be in memory at a time. To assist with this, DiscoveryManager has static method at sharedManager.
 *
 * Example:
 *
 * @capability kMediaControlPlay
 *
 @code
    DiscoveryManager.init(getApplicationContext());
    DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
    discoveryManager.addListener(this);
    discoveryManager.start();
 @endcode
 *
 * [0]: http://tools.ietf.org/html/draft-cai-ssdp-v1-03
 */
public class DiscoveryManager implements ConnectableDeviceListener, DiscoveryProviderListener, ServiceConfigListener {

    /**
     * Describes a pairing level for a DeviceService. It's used by a DiscoveryManager and all
     * services.
     */
    public enum PairingLevel {
        /**
         * Specifies that pairing is off. DeviceService will never try to pair with a first
         * screen device.
         */
        OFF,

        /**
         * Specifies that pairing is on. DeviceService will try to pair if it is required by a first
         * screen device.
         */
        ON
    }

    // @cond INTERNAL

    public static String CONNECT_SDK_VERSION = "1.6.0";

    private static DiscoveryManager instance;

    Context context;
    ConnectableDeviceStore connectableDeviceStore;

    int rescanInterval = 10;

    private ConcurrentHashMap<String, ConnectableDevice> allDevices;
    private ConcurrentHashMap<String, ConnectableDevice> compatibleDevices;

    ConcurrentHashMap<String, Class<? extends DeviceService>> deviceClasses;
    CopyOnWriteArrayList<DiscoveryProvider> discoveryProviders;

    private CopyOnWriteArrayList<DiscoveryManagerListener> discoveryListeners;
    List<CapabilityFilter> capabilityFilters;

    MulticastLock multicastLock;
    BroadcastReceiver receiver;
    boolean isBroadcastReceiverRegistered = false;

    Timer rescanTimer;

    PairingLevel pairingLevel;

    private boolean mSearching = false;

    // @endcond

    /**
     * Initilizes the Discovery manager with a valid context.  This should be done as soon as possible and it should use getApplicationContext() as the Discovery manager could persist longer than the current Activity.
     * 
     @code
        DiscoveryManager.init(getApplicationContext());
     @endcode
     */
    public static synchronized void init(Context context) {
        instance = new DiscoveryManager(context);
    }

    public static synchronized void destroy() {
        instance.onDestroy();
    }

    /**
     * Initilizes the Discovery manager with a valid context.  This should be done as soon as possible and it should use getApplicationContext() as the Discovery manager could persist longer than the current Activity.
     * 
     * This accepts a ConnectableDeviceStore to use instead of the default device store.
     * 
     @code
        MyConnectableDeviceStore myDeviceStore = new MyConnectableDeviceStore();
        DiscoveryManager.init(getApplicationContext(), myDeviceStore);
     @endcode
     */
    public static synchronized void init(Context context, ConnectableDeviceStore connectableDeviceStore) {
        instance = new DiscoveryManager(context, connectableDeviceStore);
    }

    /**
     * Get a shared instance of DiscoveryManager.
     */
    public static synchronized DiscoveryManager getInstance() {
        if (instance == null)
            throw new Error("Call DiscoveryManager.init(Context) first");

        return instance;
    }

    // @cond INTERNAL
    /**
     * Create a new instance of DiscoveryManager.
     * Direct use of this constructor is not recommended. In most cases,
     * you should use DiscoveryManager.getInstance() instead.
     */
    public DiscoveryManager(Context context) {
        this(context, new DefaultConnectableDeviceStore(context));
    }

    /**
     * Create a new instance of DiscoveryManager.
     * Direct use of this constructor is not recommended. In most cases,
     * you should use DiscoveryManager.getInstance() instead.
     */
    public DiscoveryManager(Context context, ConnectableDeviceStore connectableDeviceStore) {
        this.context = context;
        this.connectableDeviceStore = connectableDeviceStore;

        allDevices = new ConcurrentHashMap<String, ConnectableDevice>(8, 0.75f, 2);
        compatibleDevices = new ConcurrentHashMap<String, ConnectableDevice>(8, 0.75f, 2);

        deviceClasses = new ConcurrentHashMap<String, Class<? extends DeviceService>>(4, 0.75f, 2);
        discoveryProviders = new CopyOnWriteArrayList<DiscoveryProvider>();

        discoveryListeners = new CopyOnWriteArrayList<DiscoveryManagerListener>();

        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        multicastLock = wifiMgr.createMulticastLock(Util.T);
        multicastLock.setReferenceCounted(true);

        capabilityFilters = new ArrayList<CapabilityFilter>();
        pairingLevel = PairingLevel.OFF;

        receiver = new BroadcastReceiver() { 

            @Override 
            public void onReceive(Context context, Intent intent) { 
                String action = intent.getAction();

                if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

                    switch (networkInfo.getState()) {
                    case CONNECTED:
                        if (mSearching) {
                            for (DiscoveryProvider provider : discoveryProviders) {
                                provider.restart();
                            }
                        }

                        break;

                    case DISCONNECTED:
                        Log.w(Util.T, "Network connection is disconnected");

                        for (DiscoveryProvider provider : discoveryProviders) {
                            provider.reset();
                        }

                        allDevices.clear();

                        for (ConnectableDevice device: compatibleDevices.values()) {
                            handleDeviceLoss(device);
                        }
                        compatibleDevices.clear();

                        break;

                    case CONNECTING:
                        break;
                    case DISCONNECTING:
                        break;
                    case SUSPENDED:
                        break;
                    case UNKNOWN:
                        break;
                    }
                }
            } 
        };

        registerBroadcastReceiver();
    }
    // @endcond

    private void registerBroadcastReceiver() {
        if (!isBroadcastReceiverRegistered) {
            isBroadcastReceiverRegistered = true;

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            context.registerReceiver(receiver, intentFilter);
        }
    }

    private void unregisterBroadcastReceiver() {
        if (isBroadcastReceiverRegistered) {
            isBroadcastReceiverRegistered = false;

            context.unregisterReceiver(receiver);
        }
    }

    /**
     * Listener which should receive discovery updates. It is not necessary to set this listener property unless you are implementing your own device picker. Connect SDK provides a default DevicePicker which acts as a DiscoveryManagerListener, and should work for most cases.
     *
     * If you have provided a capabilityFilters array, the listener will only receive update messages for ConnectableDevices which satisfy at least one of the CapabilityFilters. If no capabilityFilters array is provided, the listener will receive update messages for all ConnectableDevice objects that are discovered.
     */
    public void addListener(DiscoveryManagerListener listener) {
        // notify listener of all devices so far
        for (ConnectableDevice device: compatibleDevices.values()) {
            listener.onDeviceAdded(this, device);
        }
        discoveryListeners.add(listener);
    }

    /**
     * Removes a previously added listener
     */
    public void removeListener(DiscoveryManagerListener listener) {
        discoveryListeners.remove(listener);
    }

    public void setCapabilityFilters(CapabilityFilter ... capabilityFilters) {
        setCapabilityFilters(Arrays.asList(capabilityFilters));
    }

    public void setCapabilityFilters(List<CapabilityFilter> capabilityFilters) {
        this.capabilityFilters = capabilityFilters;

        for (ConnectableDevice device: compatibleDevices.values()) {
            handleDeviceLoss(device);
        }

        compatibleDevices.clear();

        for (ConnectableDevice device: allDevices.values()) {
            if (deviceIsCompatible(device)) {
                compatibleDevices.put(device.getIpAddress(), device);

                handleDeviceAdd(device);
            }
        }
    }

    /**
     * Returns the list of capability filters.
     */
    public List<CapabilityFilter> getCapabilityFilters() {
        return capabilityFilters;
    }

    public boolean deviceIsCompatible(ConnectableDevice device) {
        if (capabilityFilters == null || capabilityFilters.size() == 0) {
            return true;
        }

        boolean isCompatible = false;

        for (CapabilityFilter filter: this.capabilityFilters) {
            if (device.hasCapabilities(filter.capabilities)) {
                isCompatible = true;
                break;
            }
        }

        return isCompatible;
    }
    // @cond INTERNAL

    /**
     * Registers a commonly-used set of DeviceServices with DiscoveryManager. This method will be called on first call of startDiscovery if no DeviceServices have been registered.
     *
     * - CastDiscoveryProvider
     *   + CastService
     * - SSDPDiscoveryProvider
     *   + DIALService
     *   + DLNAService (limited to LG TVs, currently)
     *   + NetcastTVService
     *   + RokuService
     *   + WebOSTVService
     *   + MultiScreenService
     * - ZeroconfDiscoveryProvider
     *   + AirPlayService
     */
    @SuppressWarnings("unchecked")
    public void registerDefaultDeviceTypes() {
        final HashMap<String, String> devicesList = DefaultPlatform.getDeviceServiceMap();

        for (HashMap.Entry<String, String> entry : devicesList.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try { 
                registerDeviceService((Class<DeviceService>) Class.forName(key), (Class<DiscoveryProvider>)Class.forName(value));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Registers a DeviceService with DiscoveryManager and tells it which DiscoveryProvider to use to find it. Each DeviceService has a JSONObject of discovery parameters that its DiscoveryProvider will use to find it.
     *
     * @param deviceClass Class for object that should be instantiated when DeviceService is found
     * @param discoveryClass Class for object that should discover this DeviceService. If a DiscoveryProvider of this class already exists, then the existing DiscoveryProvider will be used.
     */
    public void registerDeviceService(Class<? extends DeviceService> deviceClass, Class<? extends DiscoveryProvider> discoveryClass) {
        if (!DeviceService.class.isAssignableFrom(deviceClass))
            return;

        if (!DiscoveryProvider.class.isAssignableFrom(discoveryClass))
            return;

        try {
            DiscoveryProvider discoveryProvider = null;

            for (DiscoveryProvider dp : discoveryProviders) {
                if (dp.getClass().isAssignableFrom(discoveryClass)) {
                    discoveryProvider = dp;
                    break;
                }
            }

            if (discoveryProvider == null) {
                Constructor<? extends DiscoveryProvider> myConstructor = discoveryClass.getConstructor(Context.class);
                Object myObj = myConstructor.newInstance(context);
                discoveryProvider = (DiscoveryProvider) myObj;

                discoveryProvider.addListener(this);
                discoveryProviders.add(discoveryProvider);
            }
            Method m = deviceClass.getMethod("discoveryFilter");
            Object result = m.invoke(null);
            DiscoveryFilter discoveryFilter = (DiscoveryFilter) result;
            String serviceId = discoveryFilter.getServiceId();

            deviceClasses.put(serviceId, deviceClass);

            discoveryProvider.addDeviceFilter(discoveryFilter);
            if (mSearching){
            	discoveryProvider.restart();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unregisters a DeviceService with DiscoveryManager. If no other DeviceServices are set to being discovered with the associated DiscoveryProvider, then that DiscoveryProvider instance will be stopped and shut down.
     *
     * @param deviceClass Class for DeviceService that should no longer be discovered
     * @param discoveryClass Class for DiscoveryProvider that is discovering DeviceServices of deviceClass type
     */
    public void unregisterDeviceService(Class<?> deviceClass, Class<?> discoveryClass) {
        if (!DeviceService.class.isAssignableFrom(deviceClass)) {
            return;
        }

        if (!DiscoveryProvider.class.isAssignableFrom(discoveryClass)) {
            return;
        }

        try {
            DiscoveryProvider discoveryProvider = null;

            for (DiscoveryProvider dp: discoveryProviders) {
                if (dp.getClass().isAssignableFrom(discoveryClass)) {
                    discoveryProvider = dp;
                    break;
                }
            }

            if (discoveryProvider == null) 
                return;

            Method m = deviceClass.getMethod("discoveryFilter");
            Object result = m.invoke(null);
            DiscoveryFilter discoveryFilter = (DiscoveryFilter) result;
            String serviceId = discoveryFilter.getServiceId();

            // do not remove provider if there is no such service
            if (null == deviceClasses.remove(serviceId)) {
                return;
            }

            discoveryProvider.removeDeviceFilter(discoveryFilter);

            if (discoveryProvider.isEmpty()) {
                discoveryProvider.stop();
                discoveryProviders.remove(discoveryProvider);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    // @endcond

    /**
     * Start scanning for devices on the local network.
     */
    public void start() {
        if (mSearching)
            return;

        if (discoveryProviders == null) {
            return;
        }

        mSearching = true;
        multicastLock.acquire();

        Util.runOnUI(new Runnable() {

            @Override
            public void run() {
                if (discoveryProviders.size() == 0) {
                    registerDefaultDeviceTypes();
                }

                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected()) {
                    for (DiscoveryProvider provider : discoveryProviders) {
                        provider.start();
                    }
                } else {
                    Log.w(Util.T, "Wifi is not connected yet");

                    Util.runOnUI(new Runnable() {

                        @Override
                        public void run() {
                            for (DiscoveryManagerListener listener : discoveryListeners)
                                listener.onDiscoveryFailed(DiscoveryManager.this, new ServiceCommandError(0, "No wifi connection", null));
                        }
                    });
                }
            }
        });
    }

    /**
     * Stop scanning for devices.
     */
    public void stop() {
        if (!mSearching)
            return;

        mSearching = false;

        for (DiscoveryProvider provider : discoveryProviders) {
            provider.stop();
        }

        if (multicastLock.isHeld()) {
            multicastLock.release();
        }
    }

    /**
     * ConnectableDeviceStore object which loads & stores references to all discovered devices. Pairing codes/keys, SSL certificates, recent access times, etc are kept in the device store.
     *
     * ConnectableDeviceStore is a protocol which may be implemented as needed. A default implementation, DefaultConnectableDeviceStore, exists for convenience and will be used if no other device store is provided.
     *
     * In order to satisfy user privacy concerns, you should provide a UI element in your app which exposes the ConnectableDeviceStore removeAll method.
     *
     * To disable the ConnectableDeviceStore capabilities of Connect SDK, set this value to nil. This may be done at the time of instantiation with `DiscoveryManager.init(context, null);`.
     */
    public void setConnectableDeviceStore(ConnectableDeviceStore connectableDeviceStore) {
        this.connectableDeviceStore = connectableDeviceStore;
    }

    /**
     * ConnectableDeviceStore object which loads & stores references to all discovered devices. Pairing codes/keys, SSL certificates, recent access times, etc are kept in the device store.
     *
     * ConnectableDeviceStore is a protocol which may be implemented as needed. A default implementation, DefaultConnectableDeviceStore, exists for convenience and will be used if no other device store is provided.
     *
     * In order to satisfy user privacy concerns, you should provide a UI element in your app which exposes the ConnectableDeviceStore removeAll method.
     *
     * To disable the ConnectableDeviceStore capabilities of Connect SDK, set this value to nil. This may be done at the time of instantiation with `DiscoveryManager.init(context, null);`.
     */
    public ConnectableDeviceStore getConnectableDeviceStore() {
        return connectableDeviceStore;
    }

    // @cond INTERNAL
    public void handleDeviceAdd(ConnectableDevice device) {
        if (!deviceIsCompatible(device)) 
            return;

        compatibleDevices.put(device.getIpAddress(), device);

        for (DiscoveryManagerListener listenter: discoveryListeners) {
            listenter.onDeviceAdded(this, device);
        }
    }

    public void handleDeviceUpdate(ConnectableDevice device) {
        if (deviceIsCompatible(device)) {
            if (device.getIpAddress() != null && compatibleDevices.containsKey(device.getIpAddress())) {
                for (DiscoveryManagerListener listenter: discoveryListeners) {
                    listenter.onDeviceUpdated(this, device);
                }
            }
            else {
                handleDeviceAdd(device);
            }
        }
        else {
            compatibleDevices.remove(device.getIpAddress());
            handleDeviceLoss(device);
        }
    }

    public void handleDeviceLoss(ConnectableDevice device) {
        for (DiscoveryManagerListener listenter: discoveryListeners) {
            listenter.onDeviceRemoved(this, device);
        }

        device.disconnect();
    }

    public boolean isNetcast(ServiceDescription description) {
        boolean isNetcastTV = false;

        String modelName = description.getModelName();
        String modelDescription = description.getModelDescription();

        if (modelName != null && modelName.toUpperCase(Locale.US).equals("LG TV")) {
            if (modelDescription != null && !(modelDescription.toUpperCase(Locale.US).contains("WEBOS"))) {
                if (description.getServiceID().equals(NetcastTVService.ID)) {
                    isNetcastTV = true;
                }
            }
        }

        return isNetcastTV;
    }

    // @endcond

    /**
     * List of all devices discovered by DiscoveryManager. Each ConnectableDevice object is keyed against its current IP address.
     */
    public Map<String, ConnectableDevice> getAllDevices() {
        return allDevices;
    }

    /**
     * Filtered list of discovered ConnectableDevices, limited to devices that match at least one of the CapabilityFilters in the capabilityFilters array. Each ConnectableDevice object is keyed against its current IP address.
     */
    public Map<String, ConnectableDevice> getCompatibleDevices() {
        return compatibleDevices;
    }

    /**
     * The pairingLevel property determines whether capabilities that require pairing (such as entering a PIN) will be available.
     *
     * If pairingLevel is set to ConnectableDevicePairingLevelOn, ConnectableDevices that require pairing will prompt the user to pair when connecting to the ConnectableDevice.
     *
     * If pairingLevel is set to ConnectableDevicePairingLevelOff (the default), connecting to the device will avoid requiring pairing if possible but some capabilities may not be available.
     */
    public PairingLevel getPairingLevel() {
        return pairingLevel;
    }

    /**
     * The pairingLevel property determines whether capabilities that require pairing (such as entering a PIN) will be available.
     *
     * If pairingLevel is set to ConnectableDevicePairingLevelOn, ConnectableDevices that require pairing will prompt the user to pair when connecting to the ConnectableDevice.
     *
     * If pairingLevel is set to ConnectableDevicePairingLevelOff (the default), connecting to the device will avoid requiring pairing if possible but some capabilities may not be available.
     */
    public void setPairingLevel(PairingLevel pairingLevel) {
        this.pairingLevel = pairingLevel;
    }

    // @cond INTERNAL
    public Context getContext() {
        return context;
    }

    public void onDestroy() {
        unregisterBroadcastReceiver();
    }

    public List<DiscoveryProvider> getDiscoveryProviders() {
        return new ArrayList<DiscoveryProvider>(discoveryProviders);
    }

    @Override
    public void onServiceConfigUpdate(ServiceConfig serviceConfig) {
        if (connectableDeviceStore == null) {
            return;
        }
        for (ConnectableDevice device : getAllDevices().values()) {
            if (null != device.getServiceWithUUID(serviceConfig.getServiceUUID())) {
                connectableDeviceStore.updateDevice(device);
            }
        }
    }

    @Override
    public void onCapabilityUpdated(ConnectableDevice device, List<String> added, List<String> removed) {
        handleDeviceUpdate(device);
    }

    @Override public void onConnectionFailed(ConnectableDevice device, ServiceCommandError error) { } 
    @Override public void onDeviceDisconnected(ConnectableDevice device) { } 
    @Override public void onDeviceReady(ConnectableDevice device) { } 
    @Override public void onPairingRequired(ConnectableDevice device, DeviceService service, PairingType pairingType) { } 

    @Override
    public void onServiceAdded(DiscoveryProvider provider, ServiceDescription serviceDescription) {
        Log.d(Util.T, "Service added: " + serviceDescription.getFriendlyName() + " (" + serviceDescription.getServiceID() + ")");

        boolean deviceIsNew = !allDevices.containsKey(serviceDescription.getIpAddress());
        ConnectableDevice device = null;

        if (deviceIsNew) {
            if (connectableDeviceStore != null) {
                device = connectableDeviceStore.getDevice(serviceDescription.getUUID());

                if (device != null) {
                    allDevices.put(serviceDescription.getIpAddress(), device);
                    device.setIpAddress(serviceDescription.getIpAddress());
                }
            }
        } else {
            device = allDevices.get(serviceDescription.getIpAddress());
        }

        if (device == null) {
            device = new ConnectableDevice(serviceDescription);
            device.setIpAddress(serviceDescription.getIpAddress());
            allDevices.put(serviceDescription.getIpAddress(), device);
            deviceIsNew = true;
        }

        device.setFriendlyName(serviceDescription.getFriendlyName());
        device.setLastDetection(Util.getTime());
        device.setLastKnownIPAddress(serviceDescription.getIpAddress());
        //  TODO: Implement the currentSSID Property in DiscoveryManager
//        device.setLastSeenOnWifi(currentSSID);

        addServiceDescriptionToDevice(serviceDescription, device);

        if (device.getServices().size() == 0) {
            // we get here when a non-LG DLNA TV is found

            allDevices.remove(serviceDescription.getIpAddress());

            return;
        }

        if (deviceIsNew)
            handleDeviceAdd(device);
        else
            handleDeviceUpdate(device);
    } 

    @Override
    public void onServiceRemoved(DiscoveryProvider provider, ServiceDescription serviceDescription) {
        if (serviceDescription == null) {
            Log.w(Util.T, "onServiceRemoved: unknown service description");

            return;
        }

        Log.d(Util.T, "onServiceRemoved: friendlyName: " + serviceDescription.getFriendlyName());

        ConnectableDevice device = allDevices.get(serviceDescription.getIpAddress());

        if (device != null) { 
            device.removeServiceWithId(serviceDescription.getServiceID());

            if (device.getServices().isEmpty()) {
                allDevices.remove(serviceDescription.getIpAddress());

                handleDeviceLoss(device);
            }
            else {
                handleDeviceUpdate(device);
            }
        }
    }

    @Override
    public void onServiceDiscoveryFailed(DiscoveryProvider provider, ServiceCommandError error) {
        Log.w(Util.T, "DiscoveryProviderListener, Service Discovery Failed");
    } 

    @SuppressWarnings("unchecked")
    public void addServiceDescriptionToDevice(ServiceDescription desc, ConnectableDevice device) {
        Log.d(Util.T, "Adding service " + desc.getServiceID() + " to device with address " + device.getIpAddress() + " and id " + device.getId());

        Class<? extends DeviceService> deviceServiceClass = deviceClasses.get(desc.getServiceID());

        if (deviceServiceClass == null)
            return;

        if (deviceServiceClass == DLNAService.class) {
            if (desc.getLocationXML() == null)
                return;
        } else if (deviceServiceClass == NetcastTVService.class) {
            if (!isNetcast(desc))
                return;
        } 

        ServiceConfig serviceConfig = null;

        if (connectableDeviceStore != null)
            serviceConfig = connectableDeviceStore.getServiceConfig(desc);

        if (serviceConfig == null)
            serviceConfig = new ServiceConfig(desc);

        serviceConfig.setListener(DiscoveryManager.this);

        boolean hasType = false;
        boolean hasService = false;

        for (DeviceService service : device.getServices()) {
            if (service.getServiceDescription().getServiceID().equals(desc.getServiceID())) {
                hasType = true;
                if (service.getServiceDescription().getUUID().equals(desc.getUUID())) {
                    hasService = true;
                }
                break;
            }
        }

        if (hasType) {
            if (hasService) {
                device.setServiceDescription(desc);

                DeviceService alreadyAddedService = device.getServiceByName(desc.getServiceID());

                if (alreadyAddedService != null)
                    alreadyAddedService.setServiceDescription(desc);

                return;
            }

            device.removeServiceByName(desc.getServiceID());
        }

        DeviceService deviceService = DeviceService.getService(deviceServiceClass, desc, serviceConfig);

        if (deviceService != null) {
            deviceService.setServiceDescription(desc);
            device.addService(deviceService);
        }
    }
    // @endcond
}
