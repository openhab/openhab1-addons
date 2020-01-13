/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.netatmo.internal.weather;

import static org.openhab.binding.netatmo.internal.weather.MeasurementRequest.createKey;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.netatmo.NetatmoBindingProvider;
import org.openhab.binding.netatmo.internal.NetatmoBinding;
import org.openhab.binding.netatmo.internal.NetatmoException;
import org.openhab.binding.netatmo.internal.authentication.OAuthCredentials;
import org.openhab.binding.netatmo.internal.messages.NetatmoError;
import org.openhab.binding.netatmo.internal.weather.GetStationsDataResponse.Device;
import org.openhab.binding.netatmo.internal.weather.GetStationsDataResponse.Module;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding that gets measurements from the Netatmo API every couple of minutes.
 *
 * @author Andreas Brenk
 * @author Thomas.Eichstaedt-Engelen
 * @author Gaël L'hopital
 * @author Rob Nielsen
 * @author Ing. Peter Weiss
 * @since 1.4.0
 */
public class NetatmoWeatherBinding {

    private static final String WIND = "Wind";

    private static final Logger logger = LoggerFactory.getLogger(NetatmoWeatherBinding.class);

    protected static final String CONFIG_PRESSURE_UNIT = "pressureunit";
    protected static final String CONFIG_UNIT_SYSTEM = "unitsystem";

    private Map<Device, PointType> stationPositions = new HashMap<Device, PointType>();

    private NetatmoPressureUnit pressureUnit = NetatmoPressureUnit.DEFAULT_PRESSURE_UNIT;
    private NetatmoUnitSystem unitSystem = NetatmoUnitSystem.DEFAULT_UNIT_SYSTEM;

    /**
     * Execute the weather binding from Netatmo Binding Class
     *
     * @param oauthCredentials
     * @param providers
     * @param eventPublisher
     */
    public void execute(OAuthCredentials oauthCredentials, Collection<NetatmoBindingProvider> providers,
            EventPublisher eventPublisher) {
        logger.debug("Querying Netatmo Weather API");

        try {
            GetStationsDataResponse stationsDataResponse = processGetStationsData(
                    oauthCredentials, providers, eventPublisher);
            if (stationsDataResponse == null) {
                return;
            }

            DeviceMeasureValueMap deviceMeasureValueMap = processMeasurements(oauthCredentials, providers,
                    eventPublisher);
            if (deviceMeasureValueMap == null) {
                return;
            }

            for (final NetatmoBindingProvider provider : providers) {
                for (final String itemName : provider.getItemNames()) {
                    final String deviceId = provider.getDeviceId(itemName);
                    final String moduleId = provider.getModuleId(itemName);
                    final NetatmoMeasureType measureType = provider.getMeasureType(itemName);
                    final NetatmoScale scale = provider.getNetatmoScale(itemName);

                    State state = null;
                    if (measureType != null) {
                        switch (measureType) {
                            case MODULENAME:
                                if (moduleId == null) {
                                    for (Device device : stationsDataResponse.getDevices()) {
                                        if (device.getId().equals(deviceId)) {
                                            state = new StringType(device.getModuleName());
                                            break;
                                        }
                                    }
                                } else {
                                    for (Device device : stationsDataResponse.getDevices()) {
                                        for (Module module : device.getModules()) {
                                            if (module.getId().equals(moduleId)) {
                                                state = new StringType(module.getModuleName());
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
                            case TIMESTAMP:
                                state = deviceMeasureValueMap.timeStamp;
                                break;
                            case TEMPERATURE:
                            case CO2:
                            case HUMIDITY:
                            case NOISE:
                            case PRESSURE:
                            case RAIN:
                            case MIN_TEMP:
                            case MAX_TEMP:
                            case MIN_HUM:
                            case MAX_HUM:
                            case MIN_PRESSURE:
                            case MAX_PRESSURE:
                            case MIN_NOISE:
                            case MAX_NOISE:
                            case MIN_CO2:
                            case MAX_CO2:
                            case SUM_RAIN:
                            case WINDSTRENGTH:
                            case WINDANGLE:
                            case GUSTSTRENGTH:
                            case GUSTANGLE: {
                                BigDecimal value = getValue(deviceMeasureValueMap, measureType,
                                        createKey(deviceId, moduleId, scale));
                                // Protect that sometimes Netatmo returns null where
                                // numeric value is awaited (issue #1848)
                                if (value != null) {
                                    if (NetatmoMeasureType.isTemperature(measureType)) {
                                        value = unitSystem.convertTemp(value);
                                    } else if (NetatmoMeasureType.isRain(measureType)) {
                                        value = unitSystem.convertRain(value);
                                    } else if (NetatmoMeasureType.isPressure(measureType)) {
                                        value = pressureUnit.convertPressure(value);
                                    } else if (NetatmoMeasureType.isWind(measureType)) {
                                        value = unitSystem.convertWind(value);
                                    }

                                    state = new DecimalType(value);
                                }
                            }
                                break;
                            case DATE_MIN_TEMP:
                            case DATE_MAX_TEMP:
                            case DATE_MIN_HUM:
                            case DATE_MAX_HUM:
                            case DATE_MIN_PRESSURE:
                            case DATE_MAX_PRESSURE:
                            case DATE_MIN_NOISE:
                            case DATE_MAX_NOISE:
                            case DATE_MIN_CO2:
                            case DATE_MAX_CO2:
                            case DATE_MAX_GUST: {
                                final BigDecimal value = getValue(deviceMeasureValueMap, measureType,
                                        createKey(deviceId, moduleId, scale));
                                if (value != null) {
                                    final Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(value.longValue() * 1000);

                                    state = new DateTimeType(calendar);
                                }
                            }
                                break;
                            case BATTERYPERCENT:
                            case BATTERYSTATUS:
                            case BATTERYVP:
                            case RFSTATUS:
                                for (Device device : stationsDataResponse.getDevices()) {
                                    for (Module module : device.getModules()) {
                                        if (module.getId().equals(moduleId)) {
                                            switch (measureType) {
                                                case BATTERYPERCENT:
                                                case BATTERYVP:
                                                    state = new DecimalType(module.getBatteryPercentage());
                                                    break;
                                                case BATTERYSTATUS:
                                                    state = new DecimalType(module.getBatteryLevel());
                                                    break;
                                                case RFSTATUS:
                                                    state = new DecimalType(module.getRfLevel());
                                                    break;
                                                case MODULENAME:
                                                    state = new StringType(module.getModuleName());
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    }
                                }
                                break;
                            case ALTITUDE:
                            case LATITUDE:
                            case LONGITUDE:
                            case WIFISTATUS:
                            case COORDINATE:
                            case STATIONNAME:
                                for (Device device : stationsDataResponse.getDevices()) {
                                    if (device.getId().equals(deviceId)) {
                                        if (stationPositions.get(device) == null) {
                                            DecimalType altitude = DecimalType.ZERO;
                                            if (device.getAltitude() != null) {
                                                altitude = new DecimalType(device.getAltitude());
                                            }
                                            stationPositions.put(device,
                                                    new PointType(
                                                            new DecimalType(new BigDecimal(device.getLatitude())
                                                                    .setScale(6, BigDecimal.ROUND_HALF_UP)),
                                                    new DecimalType(new BigDecimal(device.getLongitude()).setScale(6,
                                                            BigDecimal.ROUND_HALF_UP)), altitude));
                                        }
                                        switch (measureType) {
                                            case LATITUDE:
                                                state = stationPositions.get(device).getLatitude();
                                                break;
                                            case LONGITUDE:
                                                state = stationPositions.get(device).getLongitude();
                                                break;
                                            case ALTITUDE:
                                                state = new DecimalType(Math.round(unitSystem.convertAltitude(
                                                        stationPositions.get(device).getAltitude().doubleValue())));
                                                break;
                                            case WIFISTATUS:
                                                state = new DecimalType(device.getWifiLevel());
                                                break;
                                            case COORDINATE:
                                                state = stationPositions.get(device);
                                                break;
                                            case STATIONNAME:
                                                state = new StringType(device.getStationName());
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }
                                break;
                        }
                    }

                    if (state != null) {
                        eventPublisher.postUpdate(itemName, state);
                    }
                }
            }
        } catch (NetatmoException ne) {
            logger.error(ne.getMessage());
        }

    }

    private BigDecimal getValue(DeviceMeasureValueMap deviceMeasureValueMap, final NetatmoMeasureType measureType,
            final String requestKey) {
        Map<String, BigDecimal> map = deviceMeasureValueMap.get(requestKey);

        return map != null ? map.get(measureType.getMeasure()) : null;
    }

    static class DeviceMeasureValueMap extends HashMap<String, Map<String, BigDecimal>> {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        DateTimeType timeStamp = null;
    }

    private DeviceMeasureValueMap processMeasurements(OAuthCredentials oauthCredentials,
            Collection<NetatmoBindingProvider> providers, EventPublisher eventPublisher) {
        DeviceMeasureValueMap deviceMeasureValueMap = new DeviceMeasureValueMap();

        for (final MeasurementRequest request : createMeasurementRequests(providers)) {
            final MeasurementResponse response = request.execute();

            logger.debug("Request: {}", request);
            logger.debug("Response: {}", response);

            if (response.isError()) {
                final NetatmoError error = response.getError();

                if (error.isAccessTokenExpired() || error.isTokenNotVaid()) {
                    logger.debug("Token is expired or is not valid, refreshing: code = {} message = {}",
                            error.getCode(), error.getMessage());

                    oauthCredentials.refreshAccessToken();
                    execute(oauthCredentials, providers, eventPublisher);

                    return null;
                } else {
                    logger.error("Error sending measurement request: code = {} message = {}", error.getCode(),
                            error.getMessage());

                    throw new NetatmoException(error.getMessage());
                }
            } else {
                processMeasurementResponse(request, response, deviceMeasureValueMap);
            }
        }

        return deviceMeasureValueMap;
    }

    private GetStationsDataResponse processGetStationsData(OAuthCredentials oauthCredentials, Collection<NetatmoBindingProvider> providers,
            EventPublisher eventPublisher) {

        GetStationsDataRequest stationsDataRequest = new GetStationsDataRequest(oauthCredentials.getAccessToken());
        logger.debug("Request: {}", stationsDataRequest);

        GetStationsDataResponse stationsDataResponse = stationsDataRequest.execute();
        logger.debug("Response: {}", stationsDataResponse);

        if (stationsDataResponse.isError()) {
            final NetatmoError error = stationsDataResponse.getError();

            if (error.isAccessTokenExpired() || error.isTokenNotVaid()) {
                logger.debug("Token is expired or is not valid, refreshing: code = {} message = {}", error.getCode(),
                        error.getMessage());

                oauthCredentials.refreshAccessToken();
                execute(oauthCredentials, providers, eventPublisher);
            } else {
                logger.error("Error processing device list: code = {} message = {}", error.getCode(),
                        error.getMessage());

                throw new NetatmoException(error.getMessage());
            }

            return null; // abort processing
        } else {
            processGetStationsDataResponse(stationsDataResponse, providers);
        }

        return stationsDataResponse;
    }

    /**
     * Processes an incoming {@link GetStationsDataResponse}.
     * <p>
     */
    private void processGetStationsDataResponse(final GetStationsDataResponse response,
            Collection<NetatmoBindingProvider> providers) {
        // Prepare a map of all known device measurements
        final Map<String, Device> deviceMap = new HashMap<String, Device>();
        final Map<String, Set<String>> deviceMeasurements = new HashMap<String, Set<String>>();

        for (final Device device : response.getDevices()) {
            final String deviceId = device.getId();
            deviceMap.put(deviceId, device);

            for (final String measurement : device.getMeasurements()) {
                if (!deviceMeasurements.containsKey(deviceId)) {
                    deviceMeasurements.put(deviceId, new HashSet<String>());
                }

                deviceMeasurements.get(deviceId).add(measurement);
            }
        }

        // Prepare a map of all known module measurements
        final Map<String, Module> moduleMap = new HashMap<String, Module>();
        final Map<String, Set<String>> moduleMeasurements = new HashMap<String, Set<String>>();
        final Map<String, String> mainDeviceMap = new HashMap<String, String>();

        for (final Device device : response.getDevices()) {
            final String deviceId = device.getId();

            for (final Module module : device.getModules()) {
                final String moduleId = module.getId();
                moduleMap.put(moduleId, module);

                for (final String measurement : module.getMeasurements()) {
                    if (!moduleMeasurements.containsKey(moduleId)) {
                        moduleMeasurements.put(moduleId, new HashSet<String>());
                        mainDeviceMap.put(moduleId, deviceId);
                    }

                    moduleMeasurements.get(moduleId).add(measurement);
                }
            }
        }

        // Remove all configured items from the maps
        for (final NetatmoBindingProvider provider : providers) {
            for (final String itemName : provider.getItemNames()) {
                final String deviceId = provider.getDeviceId(itemName);
                final String moduleId = provider.getModuleId(itemName);
                final NetatmoMeasureType measureType = provider.getMeasureType(itemName);

                final Set<String> measurements;

                if (moduleId != null) {
                    measurements = moduleMeasurements.get(moduleId);
                } else {
                    measurements = deviceMeasurements.get(deviceId);
                }

                if (measurements != null) {
                    String measure = measureType != NetatmoMeasureType.WINDSTRENGTH ? measureType.getMeasure() : WIND;
                    measurements.remove(measure);
                }
            }
        }

        // Log all unconfigured measurements
        final StringBuilder message = new StringBuilder();
        for (Entry<String, Set<String>> entry : deviceMeasurements.entrySet()) {
            final String deviceId = entry.getKey();
            final Device device = deviceMap.get(deviceId);

            for (String measurement : entry.getValue()) {
                message.append("\t" + deviceId + "#" + measurement + " (" + device.getModuleName() + ")\n");
            }
        }

        for (Entry<String, Set<String>> entry : moduleMeasurements.entrySet()) {
            final String moduleId = entry.getKey();
            final Module module = moduleMap.get(moduleId);

            for (String measurement : entry.getValue()) {
                if (measurement.equals(WIND)) {
                    measurement = NetatmoMeasureType.WINDSTRENGTH.toString().toLowerCase();
                }
                message.append("\t" + mainDeviceMap.get(moduleId) + "#" + moduleId + "#" + measurement + " ("
                        + module.getModuleName() + ")\n");
            }
        }
        if (message.length() > 0) {
            message.insert(0, "The following Netatmo measurements are not yet configured:\n");
            logger.info(message.toString());
        }
    }

    /**
     * Creates the necessary requests to query the Netatmo API for all measures
     * that have a binding. One request can query all measures of a single
     * device or module.
     */
    private Collection<MeasurementRequest> createMeasurementRequests(Collection<NetatmoBindingProvider> providers) {
        final Map<String, MeasurementRequest> requests = new HashMap<String, MeasurementRequest>();

        for (final NetatmoBindingProvider provider : providers) {
            for (final String itemName : provider.getItemNames()) {
                final NetatmoMeasureType measureType = provider.getMeasureType(itemName);

                if (measureType != null) {
                    switch (measureType) {
                        case TEMPERATURE:
                        case CO2:
                        case HUMIDITY:
                        case NOISE:
                        case PRESSURE:
                        case RAIN:
                        case MIN_TEMP:
                        case MAX_TEMP:
                        case MIN_HUM:
                        case MAX_HUM:
                        case MIN_PRESSURE:
                        case MAX_PRESSURE:
                        case MIN_NOISE:
                        case MAX_NOISE:
                        case MIN_CO2:
                        case MAX_CO2:
                        case SUM_RAIN:
                        case DATE_MIN_TEMP:
                        case DATE_MAX_TEMP:
                        case DATE_MIN_HUM:
                        case DATE_MAX_HUM:
                        case DATE_MIN_PRESSURE:
                        case DATE_MAX_PRESSURE:
                        case DATE_MIN_NOISE:
                        case DATE_MAX_NOISE:
                        case DATE_MIN_CO2:
                        case DATE_MAX_CO2:
                        case WINDSTRENGTH:
                        case WINDANGLE:
                        case GUSTSTRENGTH:
                        case GUSTANGLE:
                        case DATE_MAX_GUST:
                            final NetatmoScale scale = provider.getNetatmoScale(itemName);
                            addMeasurement(requests, provider, itemName, measureType, scale);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return requests.values();
    }

    private void addMeasurement(final Map<String, MeasurementRequest> requests, final NetatmoBindingProvider provider,
            final String itemName, final NetatmoMeasureType measureType, final NetatmoScale scale) {

        final String userid = provider.getUserid(itemName);
        final OAuthCredentials oauthCredentials = NetatmoBinding.getOAuthCredentials(userid);

        if (oauthCredentials != null) {
            final String deviceId = provider.getDeviceId(itemName);
            final String moduleId = provider.getModuleId(itemName);
            final String requestKey = createKey(deviceId, moduleId, scale);

            if (!requests.containsKey(requestKey)) {
                requests.put(requestKey,
                        new MeasurementRequest(oauthCredentials.getAccessToken(), deviceId, moduleId, scale));
            }
            requests.get(requestKey).addMeasure(measureType);
        }
    }

    private void processMeasurementResponse(final MeasurementRequest request, final MeasurementResponse response,
            DeviceMeasureValueMap deviceMeasureValueMap) {
        final List<BigDecimal> values = response.getBody().get(0).getValues().get(0);

        Map<String, BigDecimal> valueMap = deviceMeasureValueMap.get(request.getKey());
        if (valueMap == null) {
            valueMap = new HashMap<String, BigDecimal>();

            deviceMeasureValueMap.put(request.getKey(), valueMap);
            deviceMeasureValueMap.timeStamp = new DateTimeType(response.getTimeStamp());
        }

        int index = 0;
        for (final String measure : request.getMeasures()) {
            final BigDecimal value = values.get(index);
            valueMap.put(measure, value);
            index++;
        }
    }

    public NetatmoPressureUnit getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(NetatmoPressureUnit pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public NetatmoUnitSystem getUnitSystem() {
        return unitSystem;
    }

    public void setUnitSystem(NetatmoUnitSystem unitSystem) {
        this.unitSystem = unitSystem;
    }

}
