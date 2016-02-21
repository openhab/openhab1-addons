package com.connectsdk.service.webos;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.WebSocket;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.WindowManager;

import com.connectsdk.core.Util;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.service.DeviceService.PairingType;
import com.connectsdk.service.WebOSTVService;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommand;
import com.connectsdk.service.command.ServiceCommand.ServiceCommandProcessor;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.command.URLServiceSubscription;
import com.connectsdk.service.config.WebOSTVServiceConfig;

@SuppressLint("DefaultLocale")
public class WebOSTVServiceSocketClient extends WebSocketClient implements ServiceCommandProcessor {

    static final String WEBOS_PAIRING_PROMPT = "PROMPT";
    static final String WEBOS_PAIRING_PIN = "PIN";
    static final String WEBOS_PAIRING_COMBINED = "COMBINED";

    public enum State {
        NONE,
        INITIAL,
        CONNECTING,
        REGISTERING,
        REGISTERED,
        DISCONNECTING
    }

    WebOSTVServiceSocketClientListener mListener;
    WebOSTVService mService;

    int nextRequestId = 1;

    TrustManager customTrustManager;
    State state = State.INITIAL;

    JSONObject manifest;

    static final int PORT = 3001;

    // Queue of commands that should be sent once register is complete
    LinkedHashSet<ServiceCommand<ResponseListener<Object>>> commandQueue = new LinkedHashSet<ServiceCommand<ResponseListener<Object>>>();

    public SparseArray<ServiceCommand<? extends Object>> requests = new SparseArray<ServiceCommand<? extends Object>>();

    boolean mConnectSucceeded = false;
    Boolean mConnected;

    public WebOSTVServiceSocketClient(WebOSTVService service, URI uri) {
        super(uri);

        this.mService = service;
        state = State.INITIAL;

        setDefaultManifest();
    }

    public static URI getURI(WebOSTVService service) {
        String uriString = "wss://" + service.getServiceDescription().getIpAddress() + ":" + PORT;
        URI uri = null;

        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return uri;
    }

    public WebOSTVServiceSocketClientListener getListener() {
        return mListener;
    }

    public void setListener(WebOSTVServiceSocketClientListener mListener) {
        this.mListener = mListener;
    }

    public State getState() {
        return state;
    }

    public void connect() {
        synchronized (this) {
            if (state != State.INITIAL) {
                Log.w(Util.T, "already connecting; not trying to connect again: " + state);
                return; // don't try to connect again while connected
            }

            state = State.CONNECTING;
        }

        setupSSL();

        super.connect();
    }

    public void disconnect() {
        disconnectWithError(null);
    }

    public void disconnectWithError(ServiceCommandError error) {
        this.close();

        state = State.INITIAL;

        if (mListener != null)
            mListener.onCloseWithError(error);
    }

    public void clearRequests() {
        if (requests != null) {
            requests.clear();
        }
    }

    private void setDefaultManifest() {
        manifest = new JSONObject();
        List<String> permissions = mService.getPermissions();

        try {
            manifest.put("manifestVersion", 1);
//            manifest.put("appId", 1);
//            manifest.put("vendorId", 1);
//            manifest.put("localizedAppNames", 1);
            manifest.put("permissions",  convertStringListToJSONArray(permissions));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray convertStringListToJSONArray(List<String> list) {
        JSONArray jsonArray = new JSONArray();

        for(String str: list) {
            jsonArray.put(str);
        }

        return jsonArray;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        mConnectSucceeded = true;

        this.handleConnected();
    }

    @Override
    public void onMessage(String data) {
        Log.d(Util.T, "webOS Socket [IN] : " + data);

        this.handleMessage(data);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("onClose: " + code + ": " + reason);
        this.handleConnectionLost(true, null);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("onError: " + ex);

        if (!mConnectSucceeded) {
            this.handleConnectError(ex);
        } else {
            this.handleConnectionLost(false, ex);
        }
    }

    protected void handleConnected() {
        helloTV();
    }

    protected void handleConnectError(Exception ex) {
        System.err.println("connect error: " + ex.toString());

        if (mListener != null)
            mListener.onFailWithError(new ServiceCommandError(0, "connection error", null));
    }

    protected void handleMessage(String data) {
        try {
            JSONObject obj = new JSONObject(data);

            handleMessage(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    protected void handleMessage(JSONObject message) {
        Boolean shouldProcess = true;

        if (mListener != null)
            shouldProcess = mListener.onReceiveMessage(message);

        if (!shouldProcess)
            return;

        String type = message.optString("type");
        Object payload = message.opt("payload");

        String strId = message.optString("id");
        Integer id = null;
        ServiceCommand<ResponseListener<Object>> request = null;

        if (isInteger(strId)) {
            id = Integer.valueOf(strId);

            try
            {
                request = (ServiceCommand<ResponseListener<Object>>) requests.get(id);
            } catch (ClassCastException ex)
            {
                // since request is assigned to null, don't need to do anything here
            }
        }

        if (type.length() == 0)
            return;

        if ("response".equals(type)) {
            if (request != null) {
//                Log.d(Util.T, "Found requests need to handle response");
                if (payload != null) {
                    Util.postSuccess(request.getResponseListener(), payload);
                } 
                else {
                    Util.postError(request.getResponseListener(), new ServiceCommandError(-1, "JSON parse error", null));
                }

                if (!(request instanceof URLServiceSubscription)) {
                    if (!(payload instanceof JSONObject && ((JSONObject) payload).has("pairingType")))
                        requests.remove(id);
                }
            } else {
                System.err.println("no matching request id: " + strId + ", payload: " + payload.toString());
            }
        } else if ("registered".equals(type)) {
            if (!(mService.getServiceConfig() instanceof WebOSTVServiceConfig)) {
                mService.setServiceConfig(new WebOSTVServiceConfig(mService.getServiceConfig().getServiceUUID()));
            }

            if (payload instanceof JSONObject) {
                String clientKey = ((JSONObject) payload).optString("client-key");
                ((WebOSTVServiceConfig) mService.getServiceConfig()).setClientKey(clientKey);

                // Track SSL certificate
                // Not the prettiest way to get it, but we don't have direct access to the SSLEngine
                ((WebOSTVServiceConfig) mService.getServiceConfig()).setServerCertificate(customTrustManager.getLastCheckedCertificate());

                handleRegistered();

                if (id != null)
                    requests.remove(id);
            }
        } else if ("error".equals(type)) {
            String error = message.optString("error");
            if (error.length() == 0)
                return;

            int errorCode = -1;
            String errorDesc = null;

            try {
                String [] parts = error.split(" ", 2);
                errorCode = Integer.parseInt(parts[0]);
                errorDesc = parts[1];
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (payload != null) {
                Log.d(Util.T, "Error Payload: " + payload.toString());
            }

            if (message.has("id")) {
                Log.d(Util.T, "Error Desc: " + errorDesc);

                if (request != null) {
                    Util.postError(request.getResponseListener(), new ServiceCommandError(errorCode, errorDesc, payload));

                    if (!(request instanceof URLServiceSubscription)) 
                        requests.remove(id);

                }
            }
        } else if ("hello".equals(type)) {
            JSONObject jsonObj = (JSONObject)payload;

            if (mService.getServiceConfig().getServiceUUID() != null) {
                if (!mService.getServiceConfig().getServiceUUID().equals(jsonObj.optString("deviceUUID"))) {
                    ((WebOSTVServiceConfig)mService.getServiceConfig()).setClientKey(null);
                    ((WebOSTVServiceConfig)mService.getServiceConfig()).setServerCertificate((String)null);
                    mService.getServiceConfig().setServiceUUID(null);
                    mService.getServiceDescription().setIpAddress(null);
                    mService.getServiceDescription().setUUID(null);

                    disconnect();
                }
            }
            else {
                String uuid = jsonObj.optString("deviceUUID");
                mService.getServiceConfig().setServiceUUID(uuid);
                mService.getServiceDescription().setUUID(uuid);
            }

            state = State.REGISTERING;
            sendRegister();
        }
    }

    private void helloTV() {
        Context context = DiscoveryManager.getInstance().getContext();
        PackageManager packageManager = context.getPackageManager();

        // app Id
        String packageName = context.getPackageName();

        // SDK Version
        String sdkVersion = DiscoveryManager.CONNECT_SDK_VERSION;

        // Device Model
        String deviceModel = Build.MODEL;

        // OS Version
        String OSVersion = String.valueOf(android.os.Build.VERSION.SDK_INT);

        // resolution
        //WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //Display display = wm.getDefaultDisplay();

        //@SuppressWarnings("deprecation")
        //int width = display.getWidth(); // deprecated, but still needed for supporting API levels 10-12

        //@SuppressWarnings("deprecation")
        //int height = display.getHeight(); // deprecated, but still needed for supporting API levels 10-12

        String screenResolution = String.format("%dx%d", 0, 0); 

        // app Name
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (final NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");

        // app Region
        //Locale current = context.getResources().getConfiguration().locale;
        Locale current = Locale.getDefault();
        String appRegion = current.getDisplayCountry();

        JSONObject payload = new JSONObject();
        try {
            payload.put("sdkVersion", sdkVersion);
            payload.put("deviceModel", deviceModel);
            payload.put("OSVersion", OSVersion);
            payload.put("resolution", screenResolution);
            payload.put("appId", packageName);
            payload.put("appName", applicationName);
            payload.put("appRegion", appRegion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int dataId = this.nextRequestId++;

        JSONObject sendData = new JSONObject();
        try {
            sendData.put("id", dataId);
            sendData.put("type", "hello");
            sendData.put("payload", payload);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, null, sendData, true, null);
        this.sendCommandImmediately(request);
    }

    protected void sendRegister() {
        ResponseListener<Object> listener = new ResponseListener<Object>() {

            @Override
            public void onError(ServiceCommandError error) {
                state = State.INITIAL;
                
                if (mListener != null)
                    mListener.onRegistrationFailed(error);
            }

            @Override
            public void onSuccess(Object object) {
                if (object instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject)object;
                    String type = jsonObj.optString("pairingType");
                    PairingType pairingType = getPairingTypeFromString(type);

                    if (mListener != null)
                        mListener.onBeforeRegister(pairingType);
                }
            }
        };

        int dataId = this.nextRequestId++;

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, null, null, listener);
        command.setRequestId(dataId);

        JSONObject headers = new JSONObject();
        JSONObject payload = new JSONObject();

        try {
            headers.put("type", "register");
            headers.put("id", dataId);

            if (!(mService.getServiceConfig() instanceof WebOSTVServiceConfig)) {
                mService.setServiceConfig(new WebOSTVServiceConfig(mService.getServiceConfig().getServiceUUID()));
            }

            if (((WebOSTVServiceConfig)mService.getServiceConfig()).getClientKey() != null) {
                payload.put("client-key", ((WebOSTVServiceConfig)mService.getServiceConfig()).getClientKey());
            }

            String pairingTypeString = getPairingTypeString();
            if (pairingTypeString != null) {
                payload.put("pairingType", pairingTypeString);
            }

            if (manifest != null) {
                payload.put("manifest", manifest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requests.put(dataId, command);

        sendMessage(headers, payload);
    }

    private PairingType getPairingTypeFromString(String pairingTypeString) {
        if (WEBOS_PAIRING_PROMPT.equalsIgnoreCase(pairingTypeString)) {
            return PairingType.FIRST_SCREEN;
        } else if (WEBOS_PAIRING_PIN.equalsIgnoreCase(pairingTypeString)) {
            return PairingType.PIN_CODE;
        } else if (WEBOS_PAIRING_COMBINED.equalsIgnoreCase(pairingTypeString)) {
            return PairingType.MIXED;
        }
        return PairingType.NONE;
    }

    private String getPairingTypeString() {
        PairingType pairingType = mService.getPairingType();
        if (pairingType != null) {
            switch (pairingType) {
                case FIRST_SCREEN:
                    return WEBOS_PAIRING_PROMPT;
                case PIN_CODE:
                    return WEBOS_PAIRING_PIN;
                case MIXED:
                    return WEBOS_PAIRING_COMBINED;
            }
        }
        return null;
    }

    public void sendPairingKey(String pairingKey) {
        ResponseListener<Object> listener = new ResponseListener<Object>() {

            @Override
            public void onError(ServiceCommandError error) {
                state = State.INITIAL;
                
                if (mListener != null)
                    mListener.onFailWithError(error);
            }

            @Override
            public void onSuccess(Object object) { }
        };
        
        String uri = "ssap://pairing/setPin";

        int dataId = this.nextRequestId++;
        
        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, null, null, listener);
        command.setRequestId(dataId);
        
        JSONObject headers = new JSONObject();
        JSONObject payload = new JSONObject();
        
        try {
            headers.put("type", "request");
            headers.put("id", dataId);
            headers.put("uri", uri);
            
            payload.put("pin", pairingKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requests.put(dataId, command);
        
        sendMessage(headers, payload);
    }

    protected void handleRegistered() {
        state = State.REGISTERED;

        if (!commandQueue.isEmpty()) {
            LinkedHashSet<ServiceCommand<ResponseListener<Object>>> tempHashSet = new LinkedHashSet<ServiceCommand<ResponseListener<Object>>>(commandQueue);
            for (ServiceCommand<ResponseListener<Object>> command : tempHashSet) {
                Log.d(Util.T, "executing queued command for " + command.getTarget());

                sendCommandImmediately(command);
                commandQueue.remove(command);
            }
        }

        if (mListener != null)
            mListener.onConnect();

//        ConnectableDevice storedDevice = connectableDeviceStore.getDevice(mService.getServiceConfig().getServiceUUID());
//        if (storedDevice == null) {
//            storedDevice = new ConnectableDevice(
//                    mService.getServiceDescription().getIpAddress(), 
//                    mService.getServiceDescription().getFriendlyName(), 
//                    mService.getServiceDescription().getModelName(), 
//                    mService.getServiceDescription().getModelNumber());
//        }
//        storedDevice.addService(WebOSTVService.this);
//        connectableDeviceStore.addDevice(storedDevice);
    }

    @SuppressWarnings("unchecked")
    public void sendCommand(ServiceCommand<?> command) {
        Integer requestId;
        if (command.getRequestId() == -1) {
            requestId = this.nextRequestId++;
            command.setRequestId(requestId);
        }
        else {
            requestId = command.getRequestId();
        }

        requests.put(requestId, command);

        if (state == State.REGISTERED) {
            this.sendCommandImmediately(command);
        } else if (state == State.CONNECTING || state == State.DISCONNECTING){
            Log.d(Util.T, "queuing command for " + command.getTarget());
            commandQueue.add((ServiceCommand<ResponseListener<Object>>) command);
        } else {
            Log.d(Util.T, "queuing command and restarting socket for " + command.getTarget());
            commandQueue.add((ServiceCommand<ResponseListener<Object>>) command);
            connect();
        }
    }

    public void unsubscribe(URLServiceSubscription<?> subscription) {
        int requestId = subscription.getRequestId();

        if (requests.get(requestId) != null) {
            JSONObject headers = new JSONObject();

            try{
                headers.put("type", "unsubscribe");
                headers.put("id", String.valueOf(requestId));
            } catch (JSONException e)
            {
                // Safe to ignore
                e.printStackTrace();
            }

            sendMessage(headers, null);
            requests.remove(requestId);
        }
    }

    public void unsubscribe(ServiceSubscription<?> subscription) { }

    protected void sendCommandImmediately(ServiceCommand<?> command) {
        JSONObject headers = new JSONObject();
        JSONObject payload = (JSONObject) command.getPayload();
        String payloadType = "";

        try
        {
            payloadType = payload.getString("type");
        } catch (Exception ex)
        {
            // ignore
        }

        if (payloadType.equals("p2p"))
        {
            Iterator<?> iterator = payload.keys();

            while (iterator.hasNext())
            {
                String key = (String) iterator.next();

                try
                {
                    headers.put(key, payload.get(key));
                } catch (JSONException ex)
                {
                    // ignore
                }
            }

            this.sendMessage(headers, null);
        } 
        else if (payloadType.equals("hello")) {
            this.send(payload.toString());
        }
        else {
            try
            {
                headers.put("type", command.getHttpMethod());
                headers.put("id", String.valueOf(command.getRequestId()));
                headers.put("uri", command.getTarget());
            } catch (JSONException ex)
            {
                // TODO: handle this
            }


            this.sendMessage(headers, payload);
        }
    }

    private void setSSLContext(SSLContext sslContext) {
        setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sslContext));
    }

    protected void setupSSL() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            customTrustManager = new TrustManager();
            sslContext.init(null, new TrustManager [] {customTrustManager}, null);
            setSSLContext(sslContext);

            if (!(mService.getServiceConfig() instanceof WebOSTVServiceConfig)) {
                mService.setServiceConfig(new WebOSTVServiceConfig(mService.getServiceConfig().getServiceUUID()));
            }
            customTrustManager.setExpectedCertificate(((WebOSTVServiceConfig)mService.getServiceConfig()).getServerCertificate());
        } catch (KeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public boolean isConnected() {
        return this.getReadyState() == WebSocket.READYSTATE.OPEN;
    }

    public void sendMessage(JSONObject packet, JSONObject payload) {
//        JSONObject packet = new JSONObject();

        try {
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                packet.put(entry.getKey(), entry.getValue());
//            }

            if (payload != null) {
                packet.put("payload", payload);
            }
        } catch (JSONException e) {
            throw new Error(e);
        }

        if (isConnected()) {
            String message = packet.toString();

            Log.d(Util.T, "webOS Socket [OUT] : " + message);

            this.send(message);
        }
        else {
            System.err.println("connection lost");
            handleConnectionLost(false, null);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleConnectionLost(boolean cleanDisconnect, Exception ex) {
        ServiceCommandError error = null;

        if (ex != null || !cleanDisconnect)
            error = new ServiceCommandError(0, "conneciton error", ex);

        if (mListener != null)
            mListener.onCloseWithError(error);

        for (int i = 0; i < requests.size(); i++) {
            ServiceCommand<ResponseListener<Object>> request = (ServiceCommand<ResponseListener<Object>>) requests.get(requests.keyAt(i));

            if (request != null)
                Util.postError(request.getResponseListener(), new ServiceCommandError(0, "connection lost", null));
        }

        clearRequests();
    }

    public void setServerCertificate(X509Certificate cert) {
        if (!(mService.getServiceConfig() instanceof WebOSTVServiceConfig)) {
            mService.setServiceConfig(new WebOSTVServiceConfig(mService.getServiceConfig().getServiceUUID()));
        }

        ((WebOSTVServiceConfig)mService.getServiceConfig()).setServerCertificate(cert);
    }

    public void setServerCertificate(String cert) {
        if (!(mService.getServiceConfig() instanceof WebOSTVServiceConfig)) {
            mService.setServiceConfig(new WebOSTVServiceConfig(mService.getServiceConfig().getServiceUUID()));
        }

        ((WebOSTVServiceConfig)mService.getServiceConfig()).setServerCertificate(loadCertificateFromPEM(cert));
    }

    public X509Certificate getServerCertificate() {
        if (!(mService.getServiceConfig() instanceof WebOSTVServiceConfig)) {
            mService.setServiceConfig(new WebOSTVServiceConfig(mService.getServiceConfig().getServiceUUID()));
        }

        return ((WebOSTVServiceConfig)mService.getServiceConfig()).getServerCertificate();
    }

    public String getServerCertificateInString() {
        if (!(mService.getServiceConfig() instanceof WebOSTVServiceConfig)) {
            mService.setServiceConfig(new WebOSTVServiceConfig(mService.getServiceConfig().getServiceUUID()));
        }

        return exportCertificateToPEM(((WebOSTVServiceConfig)mService.getServiceConfig()).getServerCertificate());
    }

    private String exportCertificateToPEM(X509Certificate cert) {
        try {
            return Base64.encodeToString(cert.getEncoded(), Base64.DEFAULT);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private X509Certificate loadCertificateFromPEM(String pemString) {
        CertificateFactory certFactory;
        try {
            certFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(pemString.getBytes("US-ASCII"));

            return (X509Certificate)certFactory.generateCertificate(inputStream);
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        // only got here if we didn't return false
        return true;
    }

    class TrustManager implements X509TrustManager {
        X509Certificate expectedCert;
        X509Certificate lastCheckedCert;

        public void setExpectedCertificate(X509Certificate cert) {
            this.expectedCert = cert;
        }

        public X509Certificate getLastCheckedCertificate () {
            return lastCheckedCert;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.d(Util.T, "Expecting device cert " + (expectedCert != null ? expectedCert.getSubjectDN() : "(any)"));

            if (chain != null && chain.length > 0) {
                X509Certificate cert = chain[0];

                lastCheckedCert = cert;

                if (expectedCert != null) {
                    byte [] certBytes = cert.getEncoded();
                    byte [] expectedCertBytes = expectedCert.getEncoded();

                    Log.d(Util.T, "Device presented cert " + cert.getSubjectDN());

                    if (!Arrays.equals(certBytes, expectedCertBytes)) {
                        throw new CertificateException("certificate does not match");
                    }
                }
            } else {
                lastCheckedCert = null;
                throw new CertificateException("no server certificate");
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public interface WebOSTVServiceSocketClientListener {

        public void onConnect();
        public void onCloseWithError(ServiceCommandError error);
        public void onFailWithError(ServiceCommandError error);

        public void onBeforeRegister(PairingType pairingType);
        public void onRegistrationFailed(ServiceCommandError error);
        public Boolean onReceiveMessage(JSONObject message);

    }

}
