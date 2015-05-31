package org.openhab.binding.hue.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openhab.binding.hue.internal.data.BridgeData;
import org.openhab.binding.hue.internal.data.HueSettings;
import org.openhab.binding.hue.internal.hardware.HueBridge;
import org.openhab.binding.hue.internal.tools.JSONTransformer;
import org.openhab.binding.hue.internal.tools.SsdpDiscovery;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrey.Pereverzin
 * @since 1.8.0
 */
@SuppressWarnings("rawtypes")
public class HueBridgeDiscoverer {
    private static final String DEFAULT_SECRET = "openHAB";

    static final Logger logger = LoggerFactory.getLogger(HueBridgeDiscoverer.class);

    private static final String IP_PROPERTY = "ip";
    private static final String SECRET_PROPERTY = "secret";
    private static final String DISCOVERY_URL_PROPERTY = "discoveryurl";

    private final Dictionary config;

    public HueBridgeDiscoverer(Dictionary config) {
        this.config = config;
    }
    
    public HueBridge discoverHueBridge() throws ConfigurationException {
        HueBridge activeBridge = null;
        
        String bridgeSecret = DEFAULT_SECRET;
        String secret = (String) config.get(SECRET_PROPERTY);
        if (StringUtils.isNotBlank(secret)) {
            bridgeSecret = secret;
        }
        
        activeBridge = discoverFromIpProperty(bridgeSecret);
        if (activeBridge != null) {
            return activeBridge;
        }

        activeBridge = discoverFromDiscoveryUrl(bridgeSecret);
        if (activeBridge != null) {
            return activeBridge;
        }

        return discoverUsingSspdDiscovery(bridgeSecret);
    }
    
    private String readBridgeData(URL discoveryUrl) throws ConfigurationException {
        BufferedReader br = null;
        try {
            URLConnection conn = discoveryUrl.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = br.readLine();
            logger.debug("Line read from discovery url: " + inputLine);
            return inputLine;
        } catch (IOException ex) {
            String msg = "Failed to close BufferedReader after reading from discovery URL";
            logger.error(msg, ex);
            throw new ConfigurationException(DISCOVERY_URL_PROPERTY, msg, ex);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    logger.warn("Failed to close BufferedReader after reading from discovery URL", ex);
                }
            }
        }
    }
    
    private HueBridge discoverFromIpProperty(String bridgeSecret) {
        String bridgeIP = null;
        String ip = (String) config.get(IP_PROPERTY);
        if (StringUtils.isNotBlank(ip)) {
            bridgeIP = ip;
            logger.debug("HUE bridge IP is defined in properties: " + bridgeIP);
            return buildHueBridge(bridgeIP, bridgeSecret);
        }
        
        return null;
    }
    
    private HueBridge discoverFromDiscoveryUrl(String bridgeSecret) throws ConfigurationException {
        String discoveryUrlString = (String) config.get(DISCOVERY_URL_PROPERTY);
        if (StringUtils.isNotBlank(discoveryUrlString)) {
            logger.debug("HUE bridge discovery URL is defined in properties: " + discoveryUrlString);
            return buildBridgeFromDiscoveryUrl(discoveryUrlString, bridgeSecret);
        }
        
        logger.debug("HUE bridge discovery URL is not defined in properties");
        return null;
    }

    private HueBridge buildHueBridge(String bridgeIP, String bridgeSecret) {
        logger.info("Trying to connect to HUE bridge: " + bridgeIP);
        HueBridge activeBridge = new HueBridge(bridgeIP, bridgeSecret);
        activeBridge.pairBridgeIfNecessary();
        
        HueSettings settings = activeBridge.getSettings();
        if (settings == null) {
            logger.warn("Hue settings were null, maybe misconfigured bridge IP.");
            return null;
        }

        logger.info("Connected to HUE bridge: " + bridgeIP);
        return activeBridge;
    }

    private HueBridge buildBridgeFromDiscoveryUrl(String discoveryUrlString, String bridgeSecret) throws ConfigurationException {
        logger.info("Discovering HUE bridge at " + discoveryUrlString);
        try {
            URL discoveryUrl = new URL(discoveryUrlString);
            String bridgeDataProperty = readBridgeData(discoveryUrl);
            JSONParser parser = new JSONParser();
            JSONTransformer transformer = new JSONTransformer();
            parser.parse(bridgeDataProperty, transformer);

            JSONArray bridgeDataArray = (JSONArray) transformer.getResult();
            List<BridgeData> bridgeDataList = new ArrayList<BridgeData>();
            for(Object bridgeDataObject: bridgeDataArray) {
                BridgeData bridgeData = new BridgeData((JSONObject)bridgeDataObject);
                bridgeDataList.add(bridgeData);
                logger.info("HUE bridge discovered: " + bridgeData.getInternalIpAddress());
            }
            
            if (bridgeDataList.size() == 0) {
                logger.info("No HUE bridge discovered");
                return null;
            }
            
            return findHueBridge(bridgeDataList, bridgeSecret);
        } catch (MalformedURLException ex) {
            logger.warn(DISCOVERY_URL_PROPERTY, ex);
            throw new ConfigurationException(DISCOVERY_URL_PROPERTY, discoveryUrlString, ex);
        } catch (ParseException ex) {
            logger.warn(DISCOVERY_URL_PROPERTY, ex);
            throw new ConfigurationException(DISCOVERY_URL_PROPERTY, discoveryUrlString, ex);
        }
    }

    private HueBridge findHueBridge(List<BridgeData> bridgeDataList, String bridgeSecret) {
        for(BridgeData bridgeData: bridgeDataList) {
            String bridgeIP = bridgeData.getInternalIpAddress();
            logger.debug("HUE bridge IP is found via discovery URL: " + bridgeIP);
            return buildHueBridge(bridgeIP, bridgeSecret);
        }

        logger.info("Failed to connect to any discovered HUE bridge");
        return null;
    }
    
    private HueBridge discoverUsingSspdDiscovery(String bridgeSecret) {
        try {
            String bridgeIP = new SsdpDiscovery().findIpForResponseKeywords("description.xml", "FreeRTOS");
            logger.debug("HUE bridge IP is found via SsdpDiscovery tool: " + bridgeIP);
            return buildHueBridge(bridgeIP, bridgeSecret);
        } catch (IOException e) {
            logger.warn("Could not find hue bridge automatically. Please make sure it is switched on and connected to the same network as openHAB. If it permanently fails you may configure the IP address of your hue bridge manually in the openHAB configuration.");
        }
        
        return null;
    }
}
