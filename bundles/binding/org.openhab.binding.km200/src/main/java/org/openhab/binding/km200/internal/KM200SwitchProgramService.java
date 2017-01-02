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
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The KM200SwitchProgramService representing a switch program service with its all capabilities
 *
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */

public class KM200SwitchProgramService {
    private static final Logger logger = LoggerFactory.getLogger(KM200SwitchProgramService.class);

    protected int maxNbOfSwitchPoints = 8;
    protected int maxNbOfSwitchPointsPerDay = 8;
    protected int switchPointTimeRaster = 10;
    protected String setpointProperty = null;
    protected String positiveSwitch = null;
    protected String negativeSwitch = null;
    protected final String SETPOINT_NIGHT = "night";
    protected final String SETPOINT_DAY = "day";
    protected final String SETPOINT_ON = "on";
    protected final String SETPOINT_OFF = "off";

    protected final String TYPE_MONDAY = "Mo";
    protected final String TYPE_TUESDAY = "Tu";
    protected final String TYPE_WEDNESDAY = "We";
    protected final String TYPE_THURSDAY = "Th";
    protected final String TYPE_FRIDAY = "Fr";
    protected final String TYPE_SATURDAY = "Sa";
    protected final String TYPE_SUNDAY = "Su";

    protected String activeDay = TYPE_MONDAY;
    protected Integer activeCycle = 1;

    /* Night- and daylist for all weekdays */
    HashMap<String, HashMap<String, ArrayList<Integer>>> switchMap = null;

    /* List with all days */
    ArrayList<String> days = null;
    /* List with setpoints */
    ArrayList<String> setpoints = null;

    KM200SwitchProgramService() {
        switchMap = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
        days = new ArrayList<String>();
        days.add(TYPE_MONDAY);
        days.add(TYPE_TUESDAY);
        days.add(TYPE_WEDNESDAY);
        days.add(TYPE_THURSDAY);
        days.add(TYPE_FRIDAY);
        days.add(TYPE_SATURDAY);
        days.add(TYPE_SUNDAY);
        setpoints = new ArrayList<String>();
        setpoints.add(SETPOINT_NIGHT);
        setpoints.add(SETPOINT_DAY);
        setpoints.add(SETPOINT_ON);
        setpoints.add(SETPOINT_OFF);
    }

    /**
     * This function adds a switch to the switchmap
     *
     */
    void addSwitch(String day, String setpoint, int time) {
        boolean newWM = false;
        logger.debug("Adding day: {} setpoint: {} time: {}", day, setpoint, time);
        if (!days.contains(day)) {
            throw new IllegalArgumentException("This type of weekday is not supported, get day: " + day);
        }
        if (!setpoints.contains(setpoint)) {
            throw new IllegalArgumentException("This type of setpoint is not supported, get setpoint: " + setpoint);
        }
        HashMap<String, ArrayList<Integer>> weekMap = switchMap.get(setpoint);
        if (weekMap == null) {
            weekMap = new HashMap<String, ArrayList<Integer>>();
            newWM = true;
        }
        ArrayList<Integer> dayList = weekMap.get(day);
        if (dayList == null) {
            dayList = new ArrayList<Integer>();
            dayList.add(time);
            weekMap.put(day, dayList);
        } else {
            dayList.add(time);
            Collections.sort(dayList);
        }

        if (newWM) {
            switchMap.put(setpoint, weekMap);
        }
    }

    /**
     * This function removes all switches from the switchmap
     *
     */
    void removeAllSwitches() {
        if (switchMap != null) {
            switchMap.clear();
        }
    }

    void setMaxNbOfSwitchPoints(Integer nbr) {
        if (nbr != null) {
            maxNbOfSwitchPoints = nbr;
        }
    }

    void setMaxNbOfSwitchPointsPerDay(Integer nbr) {
        if (nbr != null) {
            maxNbOfSwitchPointsPerDay = nbr;
        }
    }

    void setSwitchPointTimeRaster(Integer raster) {
        if (raster != null) {
            switchPointTimeRaster = raster;
        }
    }

    void setSetpointProperty(String property) {
        setpointProperty = property;
    }

    /**
     * This function sets the day
     *
     */
    void setActiveDay(String day) {
        if (!days.contains(day)) {
            throw new IllegalArgumentException("This type of weekday is not supported, get day: " + day);
        }
        activeDay = day;
    }

    /**
     * This function sets the cycle
     *
     */
    void setActiveCycle(Integer cycle) {
        if (cycle > this.getMaxNbOfSwitchPoints() / 2 || cycle > this.getMaxNbOfSwitchPointsPerDay() / 2 || cycle < 1) {
            throw new IllegalArgumentException("The value of cycle is not valid, get cycle: " + cycle.toString());
        }
        activeCycle = cycle;
    }

    /**
     * This function sets the positive switch to the selected day and cycle
     *
     */
    void setActivePositiveSwitch(Integer time) {
        if (time < 0 || time > 1440) {
            throw new IllegalArgumentException("This switch time is invalid, get time: " + time.toString());
        }
        synchronized (switchMap) {
            HashMap<String, ArrayList<Integer>> week = switchMap.get(getPositiveSwitch());
            if (week != null) {
                ArrayList<Integer> daysList = week.get(getActiveDay());
                if (daysList != null) {
                    Integer cycl = getActiveCycle();
                    if (cycl <= daysList.size()) {
                        daysList.set(getActiveCycle() - 1, time);
                    }
                }
            }
        }

    }

    /**
     * This function sets the negative switch to the selected day and cycle
     *
     */
    void setActiveNegativeSwitch(Integer time) {
        if (time < 0 || time > 1440) {
            throw new IllegalArgumentException("This switch time is invalid, get time: " + time.toString());
        }
        synchronized (switchMap) {
            HashMap<String, ArrayList<Integer>> week = switchMap.get(getNegativeSwitch());
            if (week != null) {
                ArrayList<Integer> daysList = week.get(getActiveDay());
                if (daysList != null) {
                    Integer cycl = getActiveCycle();
                    if (cycl <= daysList.size()) {
                        daysList.set(getActiveCycle() - 1, time);
                    }
                }
            }
        }
    }

    /**
     * This function determines the positive und negative switch point names
     *
     */
    void determineSwitchNames(KM200Device device) {
        if (setpointProperty != null) {
            /* Check the positive values like day, on */
            if (device.serviceMap.containsKey(setpointProperty + "/" + SETPOINT_DAY)) {
                positiveSwitch = SETPOINT_DAY;
            } else if (device.serviceMap.containsKey(setpointProperty + "/" + SETPOINT_ON)) {
                positiveSwitch = SETPOINT_ON;
            } else {
                throw new IllegalArgumentException(
                        "The switch points in service " + setpointProperty + " are not supported: ");
            }

            /* Check the negative values like day, on */
            if (device.serviceMap.containsKey(setpointProperty + "/" + SETPOINT_NIGHT)) {
                negativeSwitch = SETPOINT_NIGHT;
            } else if (device.serviceMap.containsKey(setpointProperty + "/" + SETPOINT_OFF)) {
                negativeSwitch = SETPOINT_OFF;
            } else {
                throw new IllegalArgumentException(
                        "The switch points in service " + setpointProperty + " are not supported: ");
            }
        }
    }

    /**
     * This function updates objects the switching points
     *
     */
    void updateSwitches(JSONObject nodeRoot) {
        synchronized (switchMap) {
            /* Update the list of switching points */
            removeAllSwitches();
            JSONArray sPoints = nodeRoot.getJSONArray("switchPoints");
            for (int i = 0; i < sPoints.length(); i++) {
                JSONObject subJSON = sPoints.getJSONObject(i);
                String day = subJSON.getString("dayOfWeek");
                String setpoint = subJSON.getString("setpoint");
                Integer time = subJSON.getInt("time");
                addSwitch(day, setpoint, time);
            }
        }
    }

    /**
     * This function updates objects JSONData on the actual set switch points.
     *
     */
    String getUpdatedJSONData(KM200CommObject parObject) {
        synchronized (switchMap) {
            JSONArray sPoints = new JSONArray();
            for (String day : days) {
                if (switchMap.get(getPositiveSwitch()).containsKey(day)) {
                    for (Integer j = 0; j < switchMap.get(getPositiveSwitch()).get(day).size(); j++) {
                        JSONObject tmpObj = new JSONObject();
                        tmpObj.put("dayOfWeek", day);
                        tmpObj.put("setpoint", getPositiveSwitch());
                        tmpObj.put("time", switchMap.get(getPositiveSwitch()).get(day).get(j));
                        sPoints.put(tmpObj);
                        tmpObj = new JSONObject();
                        tmpObj.put("dayOfWeek", day);
                        tmpObj.put("setpoint", getNegativeSwitch());
                        tmpObj.put("time", switchMap.get(getNegativeSwitch()).get(day).get(j));
                        sPoints.put(tmpObj);
                    }
                }
            }
            logger.debug("New switching points: {}", sPoints.toString());
            JSONObject switchRoot = new JSONObject(parObject.getJSONData());
            switchRoot.remove("switchPoints");
            switchRoot.put("switchPoints", sPoints);
            parObject.setJSONData(switchRoot.toString());
            return sPoints.toString();
        }
    }

    int getMaxNbOfSwitchPoints() {
        return maxNbOfSwitchPoints;
    }

    int getMaxNbOfSwitchPointsPerDay() {
        return maxNbOfSwitchPointsPerDay;
    }

    int getSwitchPointTimeRaster() {
        return switchPointTimeRaster;
    }

    String getSetpointProperty() {
        return setpointProperty;
    }

    String getPositiveSwitch() {
        return positiveSwitch;
    }

    String getNegativeSwitch() {
        return negativeSwitch;
    }

    /**
     * This function returns the number auf cycles
     *
     */
    Integer getNbrCycles() {
        synchronized (switchMap) {
            HashMap<String, ArrayList<Integer>> week = switchMap.get(getPositiveSwitch());
            if (week != null) {
                ArrayList<Integer> daysList = week.get(getActiveDay());
                if (daysList != null) {
                    return daysList.size();
                }
            }
        }
        return null;
    }

    /**
     * This function returns the selected day
     *
     */
    String getActiveDay() {
        return activeDay;
    }

    /**
     * This function returns the selected cycle
     *
     */
    Integer getActiveCycle() {
        return activeCycle;
    }

    /**
     * This function returns the positive switch to the selected day and cycle
     *
     */
    Integer getActivePositiveSwitch() {
        synchronized (switchMap) {
            HashMap<String, ArrayList<Integer>> week = switchMap.get(getPositiveSwitch());
            if (week != null) {
                ArrayList<Integer> daysList = week.get(getActiveDay());
                if (daysList != null) {
                    Integer cycl = getActiveCycle();
                    if (cycl <= daysList.size()) {
                        return (daysList.get(getActiveCycle() - 1));
                    }
                }
            }
        }
        return 0;
    }

    /**
     * This function returns the negative switch to the selected day and cycle
     *
     */
    Integer getActiveNegativeSwitch() {
        synchronized (switchMap) {
            HashMap<String, ArrayList<Integer>> week = switchMap.get(getNegativeSwitch());
            if (week != null) {
                ArrayList<Integer> daysList = week.get(getActiveDay());
                if (daysList != null) {
                    Integer cycl = getActiveCycle();
                    if (cycl <= daysList.size()) {
                        return (daysList.get(getActiveCycle() - 1));
                    }
                }
            }
        }
        return 0;
    }
}
