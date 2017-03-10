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

    protected final Integer MIN_TIME = 0;
    protected final Integer MAX_TIME = 1430;

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
    }

    /**
     * This function inits the week list
     *
     */
    void initWeeklist(String setpoint) {
        HashMap<String, ArrayList<Integer>> weekMap = switchMap.get(setpoint);
        if (weekMap == null) {
            weekMap = new HashMap<String, ArrayList<Integer>>();
            for (String day : days) {
                weekMap.put(day, new ArrayList<Integer>());
            }
            switchMap.put(setpoint, weekMap);
        }
    }

    /**
     * This function adds a switch to the switchmap
     *
     */
    void addSwitch(String day, String setpoint, int time) {
        logger.debug("Adding day: {} setpoint: {} time: {}", day, setpoint, time);
        if (!days.contains(day)) {
            logger.error("This type of weekday is not supported, get day: {}", day);
            throw new IllegalArgumentException("This type of weekday is not supported, get day: " + day);
        }
        if (!setpoints.contains(setpoint)) {
            logger.error("This type of setpoint is not supported, get setpoint: {}", setpoint);
            throw new IllegalArgumentException("This type of setpoint is not supported, get setpoint: " + setpoint);
        }
        HashMap<String, ArrayList<Integer>> weekMap = switchMap.get(setpoint);
        if (weekMap == null) {
            initWeeklist(setpoint);
            weekMap = switchMap.get(setpoint);
        }
        ArrayList<Integer> dayList = weekMap.get(day);
        dayList.add(time);
        Collections.sort(dayList);
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
            logger.error("This type of weekday is not supported, get day: {}", day);
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
            logger.error("The value of cycle is not valid, get cycle: {}", cycle);
            throw new IllegalArgumentException("The value of cycle is not valid, get cycle: " + cycle.toString());
        }
        /* limit the cycle to the next one after last (for creating a new one) */
        if (cycle > (getNbrCycles() + 1) || getNbrCycles() == 0) {
            cycle = getNbrCycles() + 1;
        }
        activeCycle = cycle;
    }

    /**
     * This function sets the positive switch to the selected day and cycle
     *
     */
    void setActivePositiveSwitch(Integer time) {
        if (time < MIN_TIME) {
            time = MIN_TIME;
        }
        if (time > MAX_TIME) {
            time = MAX_TIME;
        }
        synchronized (switchMap) {
            HashMap<String, ArrayList<Integer>> week = switchMap.get(getPositiveSwitch());
            if (week != null) {
                ArrayList<Integer> daysList = week.get(getActiveDay());
                if (daysList != null) {
                    Integer actC = getActiveCycle();
                    Integer nbrC = getNbrCycles();
                    Integer nSwitch = null;
                    Boolean newS = false;
                    if (nbrC < actC) {
                        /* new Switch */
                        newS = true;
                    }
                    if (switchMap.get(getNegativeSwitch()).get(getActiveDay()).size() < actC) {
                        nSwitch = 0;
                    } else {
                        nSwitch = switchMap.get(getNegativeSwitch()).get(getActiveDay()).get(actC - 1);
                    }
                    /* The positiv switch cannot be higher then the negative */
                    if (time > (nSwitch - getSwitchPointTimeRaster()) && nSwitch > 0) {
                        time = nSwitch;
                        if (nSwitch < MAX_TIME) {
                            time -= getSwitchPointTimeRaster();
                        }
                    }
                    /* Check whether the time would overlap with the previous one */
                    if (actC > 1) {
                        Integer nPrevSwitch = switchMap.get(getNegativeSwitch()).get(getActiveDay()).get(actC - 2);
                        /* The positiv switch cannot be lower then the previous negative */
                        if (time < (nPrevSwitch + getSwitchPointTimeRaster())) {
                            time = nPrevSwitch + getSwitchPointTimeRaster();
                        }
                    }
                    if (newS) {
                        daysList.add(time);
                    } else {
                        daysList.set(actC - 1, time);
                    }
                    checkRemovement();
                }
            }
        }

    }

    Boolean newS = false;

    /**
     * This function sets the negative switch to the selected day and cycle
     *
     */
    void setActiveNegativeSwitch(Integer time) {
        if (time < MIN_TIME) {
            time = MIN_TIME;
        }
        if (time > MAX_TIME) {
            time = MAX_TIME;
        }
        synchronized (switchMap) {
            HashMap<String, ArrayList<Integer>> week = switchMap.get(getNegativeSwitch());
            if (week != null) {
                ArrayList<Integer> daysList = week.get(getActiveDay());
                if (daysList != null) {
                    Integer nbrC = getNbrCycles();
                    Integer actC = getActiveCycle();
                    Integer pSwitch = null;
                    Boolean newS = false;
                    if (nbrC < actC) {
                        /* new Switch */
                        newS = true;
                    }
                    /* Check whether the positive switch is existing too */
                    if (switchMap.get(getPositiveSwitch()).get(getActiveDay()).size() < actC) {
                        /* No -> new Switch */
                        pSwitch = 0;
                    } else {
                        pSwitch = switchMap.get(getPositiveSwitch()).get(getActiveDay()).get(actC - 1);
                    }
                    /* The negative switch cannot be lower then the positive */
                    if (time < (pSwitch + getSwitchPointTimeRaster())) {
                        time = pSwitch + getSwitchPointTimeRaster();
                    }
                    /* Check whether the time would overlap with the next one */
                    if (nbrC > actC) {
                        Integer pNextSwitch = switchMap.get(getPositiveSwitch()).get(getActiveDay()).get(actC);
                        /* The negative switch cannot be higher then the next positive switch */
                        if (time > (pNextSwitch - getSwitchPointTimeRaster()) && pNextSwitch > 0) {
                            time = pNextSwitch - getSwitchPointTimeRaster();
                        }
                    }
                    if (newS) {
                        daysList.add(time);
                    } else {
                        daysList.set(actC - 1, time);
                    }
                    checkRemovement();
                }
            }
        }
    }

    /**
     * This function checks whether the actual cycle have to be removed (Both times set to MAX_TIME)
     *
     */
    void checkRemovement() {
        if (getActiveNegativeSwitch().equals(MAX_TIME) && getActivePositiveSwitch().equals(MAX_TIME)
                && getNbrCycles() > 0) {
            switchMap.get(getNegativeSwitch()).get(getActiveDay()).remove(getActiveCycle() - 1);
            switchMap.get(getPositiveSwitch()).get(getActiveDay()).remove(getActiveCycle() - 1);
        }
    }

    /**
     * This function determines the positive and negative switch point names
     * TO-DO: Check the parent service and enable more then two setpoints
     */
    void determineSwitchNames(KM200Device device) {
        if (setpointProperty != null) {
            HashMap<String, ArrayList<Integer>> weekMap = null;
            weekMap = switchMap.get(positiveSwitch);
            if (weekMap == null) {
                initWeeklist(positiveSwitch);
            }
            weekMap = switchMap.get(negativeSwitch);
            if (weekMap == null) {
                initWeeklist(negativeSwitch);
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
            logger.debug("sPoints: {}", nodeRoot);
            for (int i = 0; i < sPoints.length(); i++) {
                JSONObject subJSON = sPoints.getJSONObject(i);
                String day = subJSON.getString("dayOfWeek");
                String setpoint = subJSON.getString("setpoint");
                Integer time = subJSON.getInt("time");
                if (positiveSwitch == null) {
                    /* The first switchpoint is always positive */
                    positiveSwitch = setpoint;
                    logger.debug("positiveSwitch: {}", positiveSwitch);
                    setpoints.add(positiveSwitch);

                } else if (negativeSwitch == null && !setpoint.equals(positiveSwitch)) {
                    /* The second switchpoint is always negative */
                    negativeSwitch = setpoint;
                    logger.debug("negativeSwitch: {}", negativeSwitch);
                    setpoints.add(negativeSwitch);
                }
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
            Boolean prepareNewOnly = false;
            JSONArray sPoints = new JSONArray();
            for (String day : days) {
                if (switchMap.get(getPositiveSwitch()).containsKey(day)
                        && switchMap.get(getNegativeSwitch()).containsKey(day)) {
                    Integer j;
                    Integer minDays = Math.min(switchMap.get(getPositiveSwitch()).get(day).size(),
                            switchMap.get(getNegativeSwitch()).get(day).size());
                    for (j = 0; j < minDays; j++) {
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

                    /* Check whether one object for a new cycle is already created */
                    if (switchMap.get(getPositiveSwitch()).get(day).size() > minDays) {
                        JSONObject tmpObj = new JSONObject();
                        tmpObj.put("dayOfWeek", day);
                        tmpObj.put("setpoint", getPositiveSwitch());
                        tmpObj.put("time", switchMap.get(getPositiveSwitch()).get(day).get(j));
                        sPoints.put(tmpObj);
                        prepareNewOnly = true;
                    } else if (switchMap.get(getNegativeSwitch()).get(day).size() > minDays) {
                        JSONObject tmpObj = new JSONObject();
                        tmpObj.put("dayOfWeek", day);
                        tmpObj.put("setpoint", getNegativeSwitch());
                        tmpObj.put("time", switchMap.get(getNegativeSwitch()).get(day).get(j));
                        sPoints.put(tmpObj);
                        prepareNewOnly = true;
                    }
                }
            }
            logger.debug("New switching points: {}", sPoints);
            JSONObject switchRoot = new JSONObject(parObject.getJSONData());
            switchRoot.remove("switchPoints");
            switchRoot.put("switchPoints", sPoints);
            parObject.setJSONData(switchRoot.toString());
            /* Preparation for are new cycle, don't sent it to the device */
            if (prepareNewOnly) {
                return null;
            } else {
                return sPoints.toString();
            }
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
     * This function returns the number of cycles
     *
     */
    Integer getNbrCycles() {
        synchronized (switchMap) {
            HashMap<String, ArrayList<Integer>> weekP = switchMap.get(getPositiveSwitch());
            HashMap<String, ArrayList<Integer>> weekN = switchMap.get(getNegativeSwitch());
            if (weekP != null && weekN != null) {
                ArrayList<Integer> daysListP = weekP.get(getActiveDay());
                ArrayList<Integer> daysListN = weekN.get(getActiveDay());
                return Math.min(daysListP.size(), daysListN.size());
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
                if (daysList.size() > 0) {
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
                if (daysList.size() > 0) {
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
