package org.openhab.binding.zibase.internal;

import java.lang.annotation.Inherited;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import fr.zapi.ZbResponse;
import fr.zapi.Zibase;
import fr.zapi.utils.XmlSimpleParse;

public class zibaseBindingConfigReceiver extends zibaseBindingConfig {
	
	/**
	 *  Map that associate a tag name returned by the zibase with a type that openhab understand (eg: for eventpublisher update).
	 *  This Map also allow to control item's config
	 *  TODO: complete 
	 *  TODO: check if it can be put in config file
	 */
	static final HashMap<String, Class<?>> valueStateMap;
	static {
		valueStateMap = new HashMap<String, Class<?>>();
		valueStateMap.put("rf"		, org.openhab.core.library.types.StringType.class);	// protocol
		valueStateMap.put("noise"	, org.openhab.core.library.types.DecimalType.class);// Rf signal noise
		valueStateMap.put("lev"		, org.openhab.core.library.types.DecimalType.class);// Rf signal strenght
		valueStateMap.put("dev"		, org.openhab.core.library.types.StringType.class);	// device's name
		valueStateMap.put("bat"		, org.openhab.core.library.types.StringType.class);	// device's battery state : Ok or Ko
		valueStateMap.put("ch"		, org.openhab.core.library.types.DecimalType.class);// device's Rf Channel
		valueStateMap.put("tem"		, org.openhab.core.library.types.DecimalType.class);// temperature
		valueStateMap.put("temc"	, org.openhab.core.library.types.DecimalType.class);// temperature ceil
		valueStateMap.put("tra"		, org.openhab.core.library.types.DecimalType.class);// total rain
		valueStateMap.put("cra"		, org.openhab.core.library.types.DecimalType.class);// current rain
		valueStateMap.put("uvl"		, org.openhab.core.library.types.DecimalType.class);// Ultra violet level
		valueStateMap.put("awi"		, org.openhab.core.library.types.DecimalType.class);// Average wind
		valueStateMap.put("dir"		, org.openhab.core.library.types.DecimalType.class);// wind direction
		valueStateMap.put("sta"		, org.openhab.core.library.types.StringType.class);	// unknown
		valueStateMap.put("kwh"		, org.openhab.core.library.types.DecimalType.class);// kilowatts per hour
		valueStateMap.put("w"		, org.openhab.core.library.types.DecimalType.class);// total watt consumption
		valueStateMap.put("hum"		, org.openhab.core.library.types.DecimalType.class);// humidity
		valueStateMap.put("area"	, org.openhab.core.library.types.StringType.class);	// area (eg. for alarm devices)
		valueStateMap.put("flag1"	, org.openhab.core.library.types.StringType.class);	// custom flag 
		valueStateMap.put("flag2"	, org.openhab.core.library.types.StringType.class);	// custom flag
		valueStateMap.put("flag3"	, org.openhab.core.library.types.StringType.class);	// custom flag
	}
	
	/**
	 * openhab type constructor to use with the item.
	 * putting this here avoid to do the matching at runtime
	 */
	private Constructor<?> constructor = null;
	
	
	/**
	 * constructor
	 * @param configParameters
	 */
	public zibaseBindingConfigReceiver(String[] configParameters) {
		super(configParameters);
	}

	/**
	 * {@link Inherited}
	 */
	@Override
	public void sendCommand(Zibase zibase, Command command, int dim) {
		logger.error("sendCommand : not implemeted for Config receiver" );
	}
	

	/**
	 * {@link Inherited}
	 */
	protected boolean isItemConfigValid() {
		logger.info("Checking config for Command item " + this.getId());
		
		if (zibaseBindingConfigReceiver.getValueStateMap().containsKey(this.values[this.POS_VALUES])) {
			logger.error("Unsupported value identifier for item " + this.getId());
			return false;
		}
		
		logger.info("Config OK for Receiver item " + this.getId());
		return true;
	}
	
	
	/**
	 * get valueStateMap
	 * @return valueStateMap
	 */
	public static HashMap<String, Class<?>> getValueStateMap() {
		return valueStateMap;
	}

	
	@Override
	public State getOpenhabStateFromZibaseValue(String zbResponseStr) {
		if(constructor != null) {
			try {
				String zibaseValue = XmlSimpleParse.getTagValue(this.values[this.POS_VALUES], zbResponseStr);
				return (State) constructor.newInstance(zibaseValue);
			} catch (Exception e) {
				logger.error("unable to convert zibase value to openHab State : " + e.toString());
					e.printStackTrace();
				}
			} 
			
		return null;
	}

}
