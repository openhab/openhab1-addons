package org.openhab.binding.lacrosse;

import java.util.Map.Entry;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaCrosseGenericBindingProvider extends
		AbstractGenericBindingProvider implements LaCrosseBindingProvider {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(LaCrosseGenericBindingProvider.class);
	
	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
	 */
	@Override
	public String getBindingType() {
		return "lacrosse";
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.lacrosse.LaCrosseBindingProvider#getItemName(java.lang.String)
	 */
	@Override
	public String getItemName(String id) {
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			LaCrosseBindingConfig cfg = (LaCrosseBindingConfig) entry.getValue();
			if(id.equals(cfg.id + "." + cfg.type)) {
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.lacrosse.LaCrosseBindingProvider#getType(java.lang.String)
	 */
	public String getType(String itemName) {
		LaCrosseBindingConfig bindingConfig = (LaCrosseBindingConfig) bindingConfigs.get(itemName);
		if(bindingConfig != null) {
			return bindingConfig.type;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.AbstractGenericBindingProvider#processBindingConfiguration(java.lang.String, org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		
		super.processBindingConfiguration(context, item, bindingConfig);

		LaCrosseBindingConfig config = new LaCrosseBindingConfig();
		for (String set : bindingConfig.trim().split(",")) {
			String[] configParts = set.split(":");
			if (configParts.length > 2) {
				throw new BindingConfigParseException("eBus binding configuration must not contain more than two parts");
			}
			
			if(configParts[0].trim().equals("id")) {
				config.id = configParts[1];
			}
			if(configParts[0].trim().equals("type")) {
				config.type = configParts[1];
			}
		}

		addBindingConfig(item, config);
	}

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#validateItemType(org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings.
	 */
	class LaCrosseBindingConfig implements BindingConfig {
		public String id;
		public String type;
		public LaCrosseBindingConfig() {

		}
	}
	
}
