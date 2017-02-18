
/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.km200.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The KM200ErrorService representing a error service with its all capabilities
 *
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */

public class KM200ErrorService {

    private static final Logger logger = LoggerFactory.getLogger(KM200ErrorService.class);

    protected Integer activeError = 1;

    /* List for all errors */
    ArrayList<HashMap<String, String>> errorMap = null;

    KM200ErrorService() {
        errorMap = new ArrayList<HashMap<String, String>>();
    }

    /**
     * This function removes all errors from the list
     *
     */
    void removeAllErrors() {
        synchronized (errorMap) {
            if (errorMap != null) {
                errorMap.clear();
            }
        }
    }

    /**
     * This function updates the errors
     *
     */
    void updateErrors(JSONObject nodeRoot) {
        synchronized (errorMap) {
            /* Update the list of errors */
            try {
                removeAllErrors();
                JSONArray sPoints = nodeRoot.getJSONArray("values");
                for (int i = 0; i < sPoints.length(); i++) {
                    JSONObject subJSON = sPoints.getJSONObject(i);
                    HashMap<String, String> valMap = new HashMap<String, String>();
                    Map<String, Object> oMap = subJSON.toMap();
                    for (String para : oMap.keySet()) {
                        logger.debug("Set: {} val: {}", para, oMap.get(para));
                        valMap.put(para, oMap.get(para).toString());
                    }
                    errorMap.add(valMap);
                }
            } catch (Exception e) {
                logger.error("Error in parsing of the errorlist: {}", e.getMessage());
            }
        }
    }

    /**
     * This function returns the number of errors
     *
     */
    Integer getNbrErrors() {
        synchronized (errorMap) {
            return errorMap.size();
        }
    }

    /**
     * This function sets the actual errors
     *
     */
    void setActiveError(Integer error) {
        if (error < 1) {
            error = 1;
        }
        if (error > getNbrErrors()) {
            error = getNbrErrors();
        }
        synchronized (activeError) {
            activeError = error;
        }
    }

    /**
     * This function returns the selected error
     *
     */
    Integer getActiveError() {
        synchronized (activeError) {
            return activeError;
        }
    }

    /**
     * This function returns a error string with all parameters
     *
     */
    String getErrorString() {
        String value = "";
        synchronized (errorMap) {
            Integer actN = getActiveError();
            if (errorMap.size() < actN || errorMap.size() == 0) {
                return null;
            }
            /* is the time value existing ("t") the use it on the begin */
            if (errorMap.get(actN - 1).containsKey("t")) {
                value = errorMap.get(actN - 1).get("t");
                for (String para : errorMap.get(actN - 1).keySet()) {
                    if (!para.equals("t")) {
                        value += " " + para + ":" + errorMap.get(actN - 1).get(para);
                    }
                }
            } else {
                for (String para : errorMap.get(actN - 1).keySet()) {
                    value += para + ":" + errorMap.get(actN - 1).get(para) + " ";
                }
            }
            return value;
        }
    }
}
