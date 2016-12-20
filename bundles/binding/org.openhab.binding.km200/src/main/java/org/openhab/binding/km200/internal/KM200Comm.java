/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.km200.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.binding.km200.KM200BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;

/**
 * The KM200Comm class does the communication to the device and does any encryption/decryption/converting jobs
 *
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */

class KM200Comm {

    private static final Logger logger = LoggerFactory.getLogger(KM200Comm.class);
    private HttpClient client = null;

    public KM200Comm() {

    }

    /**
     * This function removes zero padding from a byte array.
     *
     */
    public static byte[] removeZeroPadding(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

    /**
     * This function adds zero padding to a byte array.
     *
     */
    public static byte[] addZeroPadding(byte[] bdata, int bSize, String cSet) throws UnsupportedEncodingException {
        int encrypt_padchar = bSize - (bdata.length % bSize);
        byte[] padchars = new String(new char[encrypt_padchar]).getBytes(cSet);
        byte[] padded_data = new byte[bdata.length + padchars.length];
        System.arraycopy(bdata, 0, padded_data, 0, bdata.length);
        System.arraycopy(padchars, 0, padded_data, bdata.length, padchars.length);
        return padded_data;
    }

    /**
     * This function converts a hex string to a byte array
     *
     */
    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    /**
     * This function converts a byte array to a hex string
     *
     */
    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }

        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++) {
            if ((data[i] & 0xFF) < 16) {
                str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
            } else {
                str = str + Integer.toHexString(data[i] & 0xFF);
            }
        }
        return str;
    }

    /**
     * This function does the GET http communication to the device
     *
     */
    public byte[] getDataFromService(KM200Device device, String service) {
        byte[] responseBodyB64 = null;
        // Create an instance of HttpClient.
        if (client == null) {
            client = new HttpClient();
        }

        // Create a method instance.
        GetMethod method = new GetMethod("http://" + device.getIP4Address() + service);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        // Set the right header
        method.setRequestHeader("Accept", "application/json");
        method.addRequestHeader("User-Agent", "TeleHeater/2.2.3");

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            // Check the status and the forbidden 403 Error.
            if (statusCode != HttpStatus.SC_OK) {
                String statusLine = method.getStatusLine().toString();
                if (statusLine.contains(" 403 ")) {
                    byte[] test = new byte[1];
                    return test;
                } else {
                    logger.error("HTTP GET failed: " + method.getStatusLine());
                    return null;
                }
            }
            device.setCharSet(method.getResponseCharSet());
            // Read the response body.
            responseBodyB64 = ByteStreams.toByteArray(method.getResponseBodyAsStream());

        } catch (HttpException e) {
            logger.error("Fatal protocol violation: {}", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Fatal transport error: {}", e);
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return responseBodyB64;
    }

    /**
     * This function does the SEND http communication to the device
     *
     */
    public void sendDataToService(KM200Device device, String service, byte[] data) {
        // Create an instance of HttpClient.
        if (client == null) {
            client = new HttpClient();
        }

        // Create a method instance.
        PostMethod method = new PostMethod("http://" + device.getIP4Address() + service);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        // Set the right header
        method.setRequestHeader("Accept", "application/json");
        method.addRequestHeader("User-Agent", "TeleHeater/2.2.3");
        method.setRequestEntity(new ByteArrayRequestEntity(data));

        try {
            client.executeMethod(method);

        } catch (Exception e) {
            logger.error("Failed to send data {}", e);

        } finally {
            // Release the connection.
            method.releaseConnection();
        }
    }

    /**
     * This function does the decoding for a new message from the device
     *
     */
    public String decodeMessage(KM200Device device, byte[] encoded) {
        String retString = null;
        byte[] decodedB64 = null;

        try {
            decodedB64 = Base64.getMimeDecoder().decode(encoded);
        } catch (IllegalArgumentException e) {
            logger.error("Message is not in valid Base64 scheme: {}", e);
            e.printStackTrace();
        }
        try {
            /* Check whether the length of the decryptData is NOT multiplies of 16 */
            if ((decodedB64.length & 0xF) != 0) {
                /* Return the data */
                retString = new String(decodedB64, device.getCharSet());
                return retString;
            }
            // --- create cipher
            final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(device.getCryptKeyPriv(), "AES"));
            final byte[] decryptedData = cipher.doFinal(decodedB64);
            byte[] decryptedDataWOZP = removeZeroPadding(decryptedData);
            retString = new String(decryptedDataWOZP, device.getCharSet());
            return retString;
        } catch (BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException
                | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            // failure to authenticate
            logger.error("Exception on encoding: {}", e);
            return null;
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException("Algorithms or unlimited crypto files not available", e);
        }
    }

    /**
     * This function does the encoding for a new message to the device
     *
     */
    public byte[] encodeMessage(KM200Device device, String data) {
        byte[] encryptedDataB64 = null;

        try {
            // --- create cipher
            byte[] bdata = data.getBytes(device.getCharSet());
            final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(device.getCryptKeyPriv(), "AES"));
            logger.debug("Create padding..");
            int bsize = cipher.getBlockSize();
            logger.debug("Add Padding and Encrypt AES..");
            final byte[] encryptedData = cipher.doFinal(addZeroPadding(bdata, bsize, device.getCharSet()));
            logger.debug("Encrypt B64..");
            try {
                encryptedDataB64 = Base64.getMimeEncoder().encode(encryptedData);
            } catch (IllegalArgumentException e) {
                logger.error("Base64encoding not possible: {}", e);
                e.printStackTrace();
            }
            return encryptedDataB64;
        } catch (BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException
                | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            // failure to authenticate
            logger.error("Exception on encoding: {}", e);
            return null;
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException("Algorithms or unlimited crypto files not available", e);
        }
    }

    /**
     * This function checks the capabilities of a service on the device
     *
     */
    public void initObjects(KM200Device device, String service) {
        String id = null, type = null, decodedData = null;
        Integer writeable = 0;
        Integer recordable = 0;
        JSONObject nodeRoot = null;
        KM200CommObject newObject = null;
        logger.debug("Init: {}", service);
        if (device.blacklistMap.contains(service)) {
            logger.debug("Service on blacklist: {}", service);
            return;
        }
        byte[] recData = getDataFromService(device, service.toString());
        try {
            if (recData == null) {
                throw new RuntimeException("Communication is not possible!");
            }
            if (recData.length == 0) {
                throw new RuntimeException("No reply from KM200!");
            }
            /* Look whether the communication was forbidden */
            if (recData.length == 1) {
                newObject = new KM200CommObject(service, "", 0, 0, 0);
                device.serviceMap.put(service, newObject);
                return;
            }
            decodedData = decodeMessage(device, recData);
            if (decodedData == null) {
                throw new RuntimeException("Decoding of the KM200 message is not possible!");
            }
            if (decodedData.length() > 0) {
                nodeRoot = new JSONObject(decodedData);
                type = nodeRoot.getString("type");
                id = nodeRoot.getString("id");
            } else {
                logger.error("Get empty reply");
                return;
            }

            /* Check the service features and set the flags */
            if (nodeRoot.has("writeable")) {
                Integer val = nodeRoot.getInt("writeable");
                logger.debug(val.toString());
                writeable = val;
            }
            if (nodeRoot.has("recordable")) {
                Integer val = nodeRoot.getInt("recordable");
                logger.debug(val.toString());
                recordable = val;
            }
            logger.debug("Typ: {}", type);

            newObject = new KM200CommObject(id, type, writeable, recordable);

            /* Check whether the type is a single value containing a string value */
            if (type.equals("stringValue")) {
                Object valObject = null;
                logger.debug("initDevice: type string value: {}", decodedData.toString());
                valObject = new String(nodeRoot.getString("value"));
                newObject.setValue(valObject);
                if (nodeRoot.has("allowedValues")) {
                    List<String> valParas = new ArrayList<String>();
                    JSONArray paras = nodeRoot.getJSONArray("allowedValues");
                    for (int i = 0; i < paras.length(); i++) {
                        String subJSON = (String) paras.get(i);
                        valParas.add(subJSON);
                    }
                    newObject.setValueParameter(valParas);
                }
                device.serviceMap.put(id, newObject);

            } else if (type
                    .equals("floatValue")) { /* Check whether the type is a single value containing a float value */
                Object valObject = null;
                logger.debug("initDevice: type float value: {}", decodedData.toString());
                valObject = new Float(nodeRoot.getDouble("value"));
                newObject.setValue(valObject);
                if (nodeRoot.has("minValue") && nodeRoot.has("maxValue")) {
                    List<Float> valParas = new ArrayList<Float>();
                    valParas.add(new Float(nodeRoot.getDouble("minValue")));
                    valParas.add(new Float(nodeRoot.getDouble("maxValue")));
                    newObject.setValueParameter(valParas);
                }
                device.serviceMap.put(id, newObject);

            } else if (type.equals("switchProgram")) { /* Check whether the type is a switchProgram */
                logger.debug("initDevice: type switchProgram {}", decodedData.toString());
                JSONArray sPoints = nodeRoot.getJSONArray("switchPoints");
                newObject.setValue(sPoints);
                device.serviceMap.put(id, newObject);
                /* have to be completed */

            } else if (type.equals("errorList")) { /* Check whether the type is a errorList */
                logger.debug("initDevice: type errorList: {}", decodedData.toString());
                JSONArray errorValues = nodeRoot.getJSONArray("values");
                newObject.setValue(errorValues);
                /* have to be completed */

            } else if (type.equals("refEnum")) { /* Check whether the type is a refEnum */
                logger.debug("initDevice: type refEnum: {}", decodedData.toString());
                device.serviceMap.put(id, newObject);
                JSONArray refers = nodeRoot.getJSONArray("references");
                for (int i = 0; i < refers.length(); i++) {
                    JSONObject subJSON = refers.getJSONObject(i);
                    id = subJSON.getString("id");
                    initObjects(device, id);
                }

            } else if (type.equals("moduleList")) { /* Check whether the type is a moduleList */
                logger.debug("initDevice: type moduleList: {}", decodedData.toString());
                device.serviceMap.put(id, newObject);
                JSONArray vals = nodeRoot.getJSONArray("values");
                for (int i = 0; i < vals.length(); i++) {
                    JSONObject subJSON = vals.getJSONObject(i);
                    id = subJSON.getString("id");
                    initObjects(device, id);
                }

            } else if (type.equals("yRecording")) { /* Check whether the type is a yRecording */
                logger.debug("initDevice: type yRecording: {}", decodedData.toString());
                device.serviceMap.put(id, newObject);
                /* have to be completed */

            } else if (type.equals("systeminfo")) { /* Check whether the type is a systeminfo */
                logger.debug("initDevice: type systeminfo: {}", decodedData.toString());
                JSONArray sInfo = nodeRoot.getJSONArray("values");
                newObject.setValue(sInfo);
                device.serviceMap.put(id, newObject);
                /* have to be completed */

            } else { /* Unknown type */
                logger.info("initDevice: type unknown for service: {}",
                        service.toString() + "Data:" + decodedData.toString());
                newObject.setValue(decodedData);
                device.serviceMap.put(id, newObject);
            }
        } catch (

        JSONException e) {
            logger.error("Parsingexception in JSON: {} data: {}", e, decodedData);
            e.printStackTrace();
        }
    }

    /**
     * This function checks the state of a service on the device
     *
     */
    public State getProvidersState(KM200Device device, KM200BindingProvider provider, String item) {
        String decodedData = null;
        String service = provider.getService(item);
        String type = null;
        State state = null;
        Class<? extends Item> itemType = provider.getItemType(item);
        JSONObject nodeRoot = null;
        logger.debug("Check state of: {} type: {} item: {}", service, type, itemType.getName().toString());
        if (device.blacklistMap.contains(service)) {
            logger.debug("Service on blacklist: {}", service);
            return null;
        }
        if (device.serviceMap.containsKey(service)) {
            if (device.serviceMap.get(service).getReadable() == 0) {
                logger.error("Service is listed as protected (reading is not possible): {}", service);
                return null;
            }
            type = device.serviceMap.get(service).getServiceType();
        } else {
            logger.error("Service is not in the determined device service list: {}", service);
            return null;
        }
        byte[] recData = getDataFromService(device, service.toString());
        try {
            if (recData == null) {
                throw new RuntimeException("Communication is not possible!");
            }
            if (recData.length == 0) {
                throw new RuntimeException("No reply from KM200!");
            }
            /* Look whether the communication was forbidden */
            if (recData.length == 1) {
                logger.error("Service is listed as readable but communication is forbidden: {}", service);
                return null;
            }
            decodedData = decodeMessage(device, recData);
            logger.debug("Check state of data:" + decodedData);
            if (decodedData == null) {
                throw new RuntimeException("Decoding of the KM200 message is not possible!");
            }
            if (decodedData.length() > 0) {
                nodeRoot = new JSONObject(decodedData);
            } else {
                logger.error("Get empty reply");
                return null;
            }

            /* Check whether the type is a single value containing a string value */
            if (type.equals("stringValue")) {
                logger.debug("initDevice: type string value: {}", decodedData.toString());
                String val = nodeRoot.getString("value");
                if (itemType.isAssignableFrom(SwitchItem.class)) {
                    if (provider.getParameter(item).containsKey("on")) {
                        if (val.equals(provider.getParameter(item).get("off"))) {
                            state = OnOffType.OFF;
                        } else if (val.equals(provider.getParameter(item).get("on"))) {
                            state = OnOffType.ON;
                        }
                    } else {
                        logger.error("Switch-Item only on configured on/off string values: {}", decodedData.toString());
                        return null;
                    }

                } else if (itemType.isAssignableFrom(NumberItem.class)) {
                    try {
                        state = new DecimalType(Float.parseFloat(val));
                    } catch (NumberFormatException e) {
                        logger.error("Conversion of the string value to Decimal wasn't possible, data: {} error: {}",
                                decodedData.toString(), e);
                        return null;
                    }

                } else if (itemType.isAssignableFrom(DateTimeItem.class)) {
                    try {
                        state = new DateTimeType(val);
                    } catch (IllegalArgumentException e) {
                        logger.error("Conversion of the string value to DateTime wasn't possible, data: {} error: {}",
                                decodedData.toString(), e);
                        return null;
                    }

                } else if (itemType.isAssignableFrom(StringItem.class)) {

                    state = new StringType(val);

                } else {
                    logger.error("Bindingtype not supported for string values: {}", itemType.getClass().toString());
                    return null;
                }

                return state;

            } else if (type
                    .equals("floatValue")) { /* Check whether the type is a single value containing a float value */
                logger.debug("state of type float value: {}", decodedData.toString());
                Float val = new Float(nodeRoot.getDouble("value"));
                if (itemType.isAssignableFrom(NumberItem.class)) {
                    state = new DecimalType(val.floatValue());

                } else if (itemType.isAssignableFrom(StringItem.class)) {
                    state = new StringType(val.toString());
                } else {
                    logger.error("Bindingtype not supported for float values: {}", itemType.getClass().toString());
                    return null;
                }
                return state;

            } else if (type.equals("switchProgram")) { /* Check whether the type is a switchProgram */
                logger.info("state of: type switchProgram is not supported yet: {}", decodedData.toString());
                /* have to be completed */

            } else if (type.equals("errorList")) { /* Check whether the type is a errorList */
                logger.info("state of: type errorList is not supported yet: {}", decodedData.toString());
                /* have to be completed */

            } else if (type.equals("yRecording")) { /* Check whether the type is a yRecording */
                logger.info("state of: type yRecording is not supported yet: {}", decodedData.toString());
                /* have to be completed */

            } else if (type.equals("systeminfo")) { /* Check whether the type is a systeminfo */
                logger.info("state of: type systeminfo is not supported yet: {}", decodedData.toString());
                /* have to be completed */
            }

        } catch (

        JSONException e) {
            logger.error("Parsingexception in JSON, data: {} error: {} ", decodedData, e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This function sets the state of a service on the device
     *
     */
    public void sendProvidersState(KM200Device device, KM200BindingProvider provider, String item, Command command) {
        String service = provider.getService(item);
        String type = null;
        KM200CommObject object = null;
        Class<? extends Item> itemType = provider.getItemType(item);
        JSONObject nodeRoot = null;

        logger.debug("Prepare item for send: {} type: {} item: {}", service, type, itemType.getName().toString());
        if (device.blacklistMap.contains(service)) {
            logger.debug("Service on blacklist: {}", service);
            return;
        }
        if (device.serviceMap.containsKey(service)) {
            if (device.serviceMap.get(service).getWriteable() == 0) {
                logger.error("Service is listed as read-only: {}", service);
                return;
            }
            object = device.serviceMap.get(service);
            type = object.getServiceType();
        } else {
            logger.error("Service is not in the determined device service list: {}", service);
            return;
        }

        logger.debug("state of: {} type: {}", command.toString(), type.toString());
        if (itemType.isAssignableFrom(NumberItem.class)) {
            Float val = ((DecimalType) command).floatValue();
            if (object.getValueParameter() != null) {
                List<Float> valParas = (List<Float>) object.getValueParameter();
                Float minVal = valParas.get(0);
                Float maxVal = valParas.get(1);
                if (val < minVal) {
                    val = minVal;
                }
                if (val > maxVal) {
                    val = maxVal;
                }
            }
            if (type.equals("floatValue")) {
                nodeRoot = new JSONObject().put("value", val);
            } else if (type.equals("stringValue")) {
                nodeRoot = new JSONObject().put("value", val.toString());
            } else {
                logger.error("Not supported type for numberItem: {}", type.toString());
            }
        } else if (itemType.isAssignableFrom(StringItem.class)) {
            String val = ((StringItem) command).toString();
            if (object.getValueParameter() != null) {
                List<String> valParas = (List<String>) object.getValueParameter();
                if (!valParas.contains(val)) {
                    throw new IllegalArgumentException("Parameter is not in the service parameterlist:" + val);
                }
            }
            if (type.equals("stringValue")) {
                nodeRoot = new JSONObject().put("value", val);
            } else if (type.equals("floatValue")) {
                nodeRoot = new JSONObject().put("value", Float.parseFloat(val));
            } else {
                logger.error("Not supported type for stringItem: {}", type.toString());
            }

        } else if (itemType.isAssignableFrom(DateTimeItem.class)) {
            String val = ((DateTimeItem) command).toString();
            if (type.equals("stringValue")) {
                nodeRoot = new JSONObject().put("value", val);
            } else {
                logger.error("Not supported type for dateTimeItem: {}", type.toString());
            }

        } else if (itemType.isAssignableFrom(SwitchItem.class)) {
            String val = null;
            if (provider.getParameter(item).containsKey("on")) {
                if (command == OnOffType.OFF) {
                    val = provider.getParameter(item).get("off");
                } else if (command == OnOffType.ON) {
                    val = provider.getParameter(item).get("on");
                }
            } else {
                logger.error("witch-Item only on configured on/off string values {}", command.toString());
                return;
            }
            if (type.equals("stringValue")) {
                nodeRoot = new JSONObject().put("value", val);
            } else {
                logger.error("Not supported type for SwitchItem:{}", type.toString());
            }

        } else {
            logger.error("Bindingtype not supported: {}", itemType.getClass().toString());
            return;
        }
        if (nodeRoot == null) {
            logger.error("Couldn't find the right configuration");
            return;
        }
        logger.debug("Encoding" + nodeRoot.toString());
        byte[] encData = encodeMessage(device, nodeRoot.toString());
        if (encData == null) {
            logger.error("Couldn't encrypt data");
            return;
        }
        logger.debug("Sending");
        sendDataToService(device, service, encData);

    }

}
