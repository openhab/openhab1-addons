package org.openhab.binding.chromecast.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.chromecast.ChromeCastBindingProvider;
import org.openhab.binding.chromecast.internal.ChromeCastGenericBindingProvider.ChromecastBindingConfig.Direction;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author ibaton
 * @since 1.7
 */
public class ChromeCastGenericBindingProvider extends AbstractGenericBindingProvider implements ChromeCastBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(ChromeCastGenericBindingProvider.class);
		
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "chromecast";
	}

	/**
	 * @{inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem) && !(item instanceof SwitchItem) &&
				!(item instanceof DimmerItem) && !(item instanceof NumberItem)){
			throw new BindingConfigParseException( "item '"+item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', but only String, Switch or Dimmer items are allowed.");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Item config
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
				
		ChromecastBindingConfig itemConfig = parseBidirectionalBindingConfig(item, bindingConfig);
		if (itemConfig != null) {
			addBindingConfig(item, itemConfig);	
		}else {
			logger.info("Not valid config");
		}
	}
	
	public boolean containsChromecastInstance(String itemname) {
		logger.info("containsChromecastInstance!");
		this.removeConfigurations(itemname);
		return bindingConfigs.containsKey(itemname);
	}
	
	public ChromecastBindingConfig getChromecastInstance(String itemname) {
		return (ChromecastBindingConfig) bindingConfigs.get(itemname);
	}
	
	/**
	 * Parse item binding 
	 * 
	 * @param bindingConfig
	 * @param item
	 * @throws BindingConfigParseException
	 */
	private ChromecastBindingConfig parseBidirectionalBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException{
		
		String[] parts = bindingConfig.split(":");
		
		if(parts.length == 3) {
			if(ChromecastBindingConfig.DIRECTION_MAP.containsKey(parts[1])) {
				String ip = StringUtils.trim(parts[0]);
				String command = StringUtils.trim(parts[2]);
				
				Direction direction = ChromecastBindingConfig.DIRECTION_MAP.get(parts[1]);
				ChromecastBindingConfig itemConfig = new ChromecastBindingConfig(item, ip, direction, command);
				return itemConfig;
			}else {
				logger.error("Item configuration direction must be either <, = or >.");
			}
		}else {
			logger.error("Item configuration must have 3 parts");
		}
		
		return null;
	}
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the chromecast
	 * binding provider.
	 */
	public static class ChromecastBindingConfig implements BindingConfig {

		enum Direction {In, Out, IO}
		public static final Map<String,Direction> DIRECTION_MAP = new HashMap<String,Direction>();
		static {
			DIRECTION_MAP.put("<", Direction.In);
			DIRECTION_MAP.put("=", Direction.IO);
			DIRECTION_MAP.put(">", Direction.Out);
		}
		
		public Item item;
		public String ip;
		public String command;
		public Direction direction;
		public Class<? extends Item> itemType;
		
		public ChromecastBindingConfig(Item item, String ip, Direction direction, String command){
			this.ip = ip;
			this.command = command;
			this.direction = direction;
			this.item = item;
		}
		
		public String getIP(){
			return ip;
		}
		
		public Direction getDirection() {
			return direction;
		}
		
		public String getCommand(){
			return command;
		}
		
		public Item getItem(){
			return item;
		}
		
		public String getItemName(){
			return item.getName();
		}
		
		public Class<? extends Item> getType(){
			return item.getClass();
		}
	}
}
