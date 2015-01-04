package org.openhab.binding.lacrosse;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.lacrosse.connector.LaCrosseConnector;
import org.openhab.config.core.ConfigDispatcher;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaCrosseBinding extends AbstractBinding<LaCrosseBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(LaCrosseBinding.class);
	
	private LaCrosseConnector connector;
	
	private HashMap<Integer, Integer> addressMapping;
	
	private HashMap<Integer, Long> lastSensorUpdate;

	private long mappingModeTimer;
	
	public enum MappingMode	{
		NONE, AUTO, BY_ACTION
	}
	
	private MappingMode automaticMappingMode = MappingMode.AUTO;
	
	public LaCrosseBinding() {
		addressMapping = new HashMap<Integer, Integer>();
		lastSensorUpdate = 	new HashMap<Integer, Long>();
	}

	@Override
	public void receiveCommand(String itemName, Command command) {
		//TODO: Add set timer command for BY_ACTION mode
		super.receiveCommand(itemName, command);
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		logger.info("Update LaCrosse Binding ...");
		
		if(connector != null && connector.isOpen()) {
			connector.close();
		}

		if (properties != null) {
			String newPort = (String) properties.get("port"); //$NON-NLS-1$

			connector = new LaCrosseConnector(this);
			connector.open(newPort);
		}
	}

	public void postUpdate(String id, String type, State newState) {
		for (LaCrosseBindingProvider provider : providers) {
			String itemName = provider.getItemName(id + "." + type);
			if(StringUtils.isNotEmpty(itemName)) {
				eventPublisher.postUpdate(itemName, newState);
			}
		}
	}

	public void postSensorData(int address, BigDecimal temperature, 
			BigDecimal humidity, boolean batteryNew, boolean batteryWeak) {
		
		String addr = String.valueOf(address);
		boolean found = false;
		
		lastSensorUpdate.put(address, System.currentTimeMillis());

		// search for a set mapping
		if(addressMapping.containsKey(address)) {
			logger.debug("Use lacrosse id " + addressMapping.get(address) +
					" for sensor id " + address);
			
			// Found mapping for this address, use that
			addr = String.valueOf(addressMapping.get(address));
		}
		
		for (LaCrosseBindingProvider provider : providers) {
			String itemName = provider.getItemName(addr+".temperature");
			if(StringUtils.isNotEmpty(itemName)) {
				found = true;
				break;
			}
		}
		
		if(found) {
			logger.debug("Receive sensor data for: " + addr);
			
			postUpdate(addr, "temperature", new DecimalType(temperature));
			postUpdate(addr, "humidity", new DecimalType(humidity));
			postUpdate(addr, "batteryNew", getOnOff(batteryNew));
			postUpdate(addr, "batteryLow", getOnOff(batteryWeak));
			
		} else {
			
			// start new mapping process only if the new battery flag is set
			if(!batteryNew) {
				logger.debug("Received sensor data from unknown sensor: " + addr);
				
			} else {
				logger.info("Received sensor data from a NEW unknown sensor: " + addr);
				
				boolean autoMap = false;
				if(automaticMappingMode == MappingMode.BY_ACTION) {
					if(mappingModeTimer > System.currentTimeMillis()-(10*6000)) {
						logger.info("LaCrosse Mapping Mode active ...");;
						autoMap = true;
					}
				} else if(automaticMappingMode == MappingMode.AUTO) {
					autoMap = true;
				}

				if(autoMap) {
					// sensors between 1min to 30days
					List<Integer> lostSensors = getLostSensors(60*1000, 30*24*60*60*1000);
					if(lostSensors.isEmpty()) {
						logger.debug("Currently no offline sensor available to replace.");
						
					} else if(lostSensors.size() == 1) {
						logger.info("Found a offline sensor id to replace with new sensor id!");
	
						// now remove this old sensor id, is now replaced
						lastSensorUpdate.remove(lostSensors.get(0));
						
						// check if a mapping exists, than use this target id
						if(addressMapping.containsKey(lostSensors.get(0))) {
							addressMapping.put(address, addressMapping.get(lostSensors.get(0)));
						} else {
							addressMapping.put(address, lostSensors.get(0));
						}
						
						writeMappingFile();
						
						logger.info("Replace LaCrosse sensor Id {} with new Id {}",
							lostSensors.get(0),	address);
					}
				}
			}
		}
	}
	
	/**
	 * Search sensors with last update between min and max offset
	 * @param minOffset
	 * @param maxOffset
	 * @return
	 */
	private List<Integer> getLostSensors(long minOffset, long maxOffset) {
		long min = System.currentTimeMillis() - minOffset;
		long max = System.currentTimeMillis() - maxOffset;
		
		ArrayList<Integer> lostSensorList = new ArrayList<Integer>();
		ArrayList<Integer> cleanupList = new ArrayList<Integer>();

		for (Entry<Integer, Long> entry : lastSensorUpdate.entrySet()) {
			if(entry.getValue() < min) {
				
				logger.info("Lost sensor " + entry.getKey() + " found ...");
				lostSensorList.add(entry.getKey());
				
				if(entry.getValue() < max) {
					logger.info("Outdated lost sensor " + entry.getKey() + " found ...");
					
					cleanupList.add(entry.getKey());
					lostSensorList.remove(entry.getKey());
				}
				
			}
		}
		
		if(!cleanupList.isEmpty()) {
			for (Integer id : cleanupList) {
				logger.warn("Remove " + id + " outdated id ...");
				lastSensorUpdate.remove(id);
			}
		}
		
		return lostSensorList;
	}
	
	/**
	 * Returns the mapping file
	 * @return
	 */
	private File getMappingFile() {
		String defaultConfigFilePath = ConfigDispatcher.getConfigFolder();
		return new File(defaultConfigFilePath, "lacrosse-mapping.json");
	}
	
	/**
	 * write the current mapping to a json file
	 */
	private void writeMappingFile() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(getMappingFile(), addressMapping);
		} catch (JsonGenerationException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * read the json file to the current mapping object
	 */
	@SuppressWarnings("unchecked")
	private void readMappingFile() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			File mappingFile = getMappingFile();
			
			if(mappingFile.exists()) {
				HashMap<String, Integer> tmpMapping = (HashMap<String, Integer>) mapper.readValue(getMappingFile(), Map.class);
				
				addressMapping.clear();
				for (Entry<String, Integer> entry : tmpMapping.entrySet()) {
					addressMapping.put(Integer.parseInt(entry.getKey()), entry.getValue());
				}
			}

		} catch (JsonParseException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * @{inheritDoc
	 */
	public void activate() {
		readMappingFile();
	}

	/**
	 * @{inheritDoc
	 */
	public void deactivate() {
		if(connector != null) {
			connector.close();
			connector = null;
		}
	}
	
	/**
	 * Converts boolean to a valid OnOffType
	 * @param state
	 * @return
	 */
	private OnOffType getOnOff(boolean state) {
		return state ? OnOffType.ON : OnOffType.OFF;
	}
}