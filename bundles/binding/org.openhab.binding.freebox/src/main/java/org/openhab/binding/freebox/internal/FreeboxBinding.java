/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freebox.internal;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.List;

import org.openhab.binding.freebox.FreeboxBindingProvider;
import org.openhab.binding.freebox.FreeboxBindingConfig;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.types.Command;
import org.openhab.core.items.Item;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.matmaul.freeboxos.login.*;
import org.matmaul.freeboxos.FreeboxOsClient;
import org.matmaul.freeboxos.FreeboxException;
import org.matmaul.freeboxos.airmedia.AirMediaConfig;
import org.matmaul.freeboxos.connection.ConnectionStatus;
import org.matmaul.freeboxos.system.SystemConfiguration;
import org.matmaul.freeboxos.upnpav.UPnPAVConfig;
import org.matmaul.freeboxos.call.CallEntry;
import org.matmaul.freeboxos.wifi.*;
import org.matmaul.freeboxos.lan.LanConfig;
import org.matmaul.freeboxos.lan.LanHostsConfig;
import org.matmaul.freeboxos.lcd.*;
import org.matmaul.freeboxos.netshare.SambaConfig;
import org.matmaul.freeboxos.connection.xDslStatus;
import org.matmaul.freeboxos.ftp.FtpConfig;

/**
 * Freebox binding for openHAB
 * This implements needed behaviour in order to get connected to the Freebox
 * using ad-hoc connection procedure and then enables uses FreeboxAPI to
 * gather meaningfull informations or execute actions
 * 
 * @author clinique
 * @since 1.5.0
 */

public class FreeboxBinding extends AbstractActiveBinding<FreeboxBindingProvider> implements ManagedService {
	private static final Logger logger = LoggerFactory.getLogger(FreeboxBinding.class);
	private static String appVersion;
	private static String appID;
	private static String appName;
	
	private static String deviceName;
	private static String appToken;
	private static String serverAddress;
	
	private static FreeboxOsClient fbClient;
	private static LoginManager loginManager;

	private Calendar lastPhoneCheck;
	
	/** 
	 * the refresh interval which is used to poll values from the Freebox
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Freebox Refresh Service";
	}
	
	public void activate() {
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		appVersion = String.format("%d.%d",bundle.getVersion().getMajor(),bundle.getVersion().getMinor()); // something like 1.5
		appID = bundle.getSymbolicName();																// org.openhab.binding.freebox
		appName = bundle.getHeaders().get("Bundle-Name");												// "openHAB Freebox Binding"
		lastPhoneCheck = Calendar.getInstance();
	}
	
	private void setItemValue(Item item, boolean value) {
			eventPublisher.postUpdate(item.getName(), value ? OnOffType.ON : OnOffType.OFF);
	}
	
	private void setItemValue(Item item, String value) {
			eventPublisher.postUpdate(item.getName(), new StringType(value));
	}
	
	private void setItemValue(Item item, Long value) {
			eventPublisher.postUpdate(item.getName(), new DecimalType(value));					
	}
	
	private void setDateTimeValue(Item item, long value) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(value * 1000);
		eventPublisher.postUpdate(item.getName(), new DateTimeType(c));
	}

	
	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void execute() {
		try {
			
			SystemConfiguration sc = fbClient.getSystemManager().getConfiguration();

			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case REBOOT: setItemValue(bindingConfig.item,false);
							break;
						case CPUB: setItemValue(bindingConfig.item,(long)sc.getTemp_cpub());
							break;
						case CPUM: setItemValue(bindingConfig.item,(long)sc.getTemp_cpum());
							break;
						case FAN: setItemValue(bindingConfig.item,(long)sc.getFan_rpm());
							break;
						case FWVERSION: setItemValue(bindingConfig.item,sc.getFirmware_version());
							break;
						case SW: setItemValue(bindingConfig.item,(long)sc.getTemp_sw());
							break;
						case UPTIME: setItemValue(bindingConfig.item,sc.getUptimeVal());
							break;
						default:
							break;
					}
				}
			}				
		} catch (FreeboxException e) {
			logger.info("SystemConfiguration: " + e.getMessage());			
		}
		
		try {
			
			ConnectionStatus cs = fbClient.getConnectionManager().getStatus();

			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case BYTESDOWN: setItemValue(bindingConfig.item,cs.getBytes_down());
							break;
						case BYTESUP: setItemValue(bindingConfig.item,cs.getBytes_up());
							break;
						case IPV4: setItemValue(bindingConfig.item,cs.getIpv4());
							break;
						case LINESTATUS: setItemValue(bindingConfig.item, cs.getState());
							break;
						case RATEDOWN: setItemValue(bindingConfig.item,cs.getRate_down());
							break;
						case RATEUP: setItemValue(bindingConfig.item,cs.getRate_up());
							break;
						default:
							break;
					}
				}
			}				
		} catch (FreeboxException e) {
			logger.info("ConnectionStatus: " + e.getMessage());			
		}
		
		try {
			
			WifiGlobalConfig wc = fbClient.getWifiManager().getGlobalConfig();

			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case WIFISTATUS : setItemValue(bindingConfig.item,wc.getEnabled());
							break;
						default:
							break;
					}
				}
			}				
		} catch (FreeboxException e) {
			logger.info("WifiGlobalConfig: " + e.getMessage());			
		}
		
		try {
			
			LCDConfig lcd = fbClient.getLCDManager().getLCDConfig();

			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case LCDBRIGHTNESS : setItemValue(bindingConfig.item,(long)lcd.getBrightness());
							break;
						case LCDORIENTATION : setItemValue(bindingConfig.item,(long)lcd.getOrientation());
							break;
						case LCDFORCED : setItemValue(bindingConfig.item,lcd.getOrientationForced());
							break;
						default:
							break;
					}
				}
			}				
		} catch (FreeboxException e) {
			logger.info("LCDConfig: " + e.getMessage());			
		}
		
		try {
			
			xDslStatus xdsl = fbClient.getConnectionManager().getxDslStatus();

			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case XDSLSTATUS : setItemValue(bindingConfig.item,xdsl.getStatus());
							break;
						default:
							break;
					}
				}
			}				
		} catch (FreeboxException e) {
			logger.info("xDslStatus: " + e.getMessage());			
		}
		
		try {
			
			FtpConfig fc = fbClient.getFtpManager().getConfig();

			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case FTPSTATUS : setItemValue(bindingConfig.item,fc.getEnabled());
							break;
						default:
							break;
					}
				}
			}				
		} catch (FreeboxException e) {
			logger.info("FtpConfig: " + e.getMessage());			
		}
		
		String mode = null;
		try {
			LanConfig lc = fbClient.getLanManager().getLanConfig();
			mode = lc.getMode();
		} catch (FreeboxException e) {
			logger.info("LanConfig: " + e.getMessage());			
			mode = null;
		}
		if ((mode != null) && !mode.equalsIgnoreCase("bridge")) {
			// Only when Freebox Revolution is not in bridge mode
			try {
				
				AirMediaConfig ac = fbClient.getAirMediaManager().getConfig();

				for (FreeboxBindingProvider provider : providers) {
					Collection<String> items = provider.getItemNames();
					
					for (String itemName: items) {
						FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
						
						switch (bindingConfig.commandType) {
							case AIRMEDIASTATUS : setItemValue(bindingConfig.item,ac.getEnabled());
								break;
							default:
								break;
						}
					}
				}				
			} catch (FreeboxException e) {
				logger.info("AirMediaConfig: " + e.getMessage());			
			}
			
			try {
				
				UPnPAVConfig uc = fbClient.getUPnPAVManager().getConfig();

				for (FreeboxBindingProvider provider : providers) {
					Collection<String> items = provider.getItemNames();
					
					for (String itemName: items) {
						FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
						
						switch (bindingConfig.commandType) {
							case UPNPAVSTATUS : setItemValue(bindingConfig.item,uc.getEnabled());
								break;
							default:
								break;
						}
					}
				}				
			} catch (FreeboxException e) {
				logger.info("UPnPAVConfig: " + e.getMessage());			
			}
		}
		
		try {
			
			SambaConfig sac = fbClient.getNetShareManager().getSambaConfig();

			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case SAMBAFILESTATUS : setItemValue(bindingConfig.item,sac.getFileShareEnabled());
							break;
						case SAMBAPRINTERSTATUS : setItemValue(bindingConfig.item,sac.getPrintShareEnabled());
							break;
						default:
							break;
					}
				}
			}				
		} catch (FreeboxException e) {
			logger.info("SambaConfig: " + e.getMessage());			
		}
		
		try {
			
			LanHostsConfig hc = fbClient.getLanManager().getAllLanHostsConfig();
			
			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case REACHABLEMAC: setItemValue(bindingConfig.item,hc.isMacReachable(bindingConfig.commandParam));
							break;
						case REACHABLEIP: setItemValue(bindingConfig.item,hc.isIpReachable(bindingConfig.commandParam));
							break;
						case REACHABLENAME: setItemValue(bindingConfig.item,hc.isHostNameReachable(bindingConfig.commandParam));
							break;
						default:
							break;
					}
				}
			}				
		} catch (FreeboxException e) {
			logger.info("LanHostsConfig: " + e.getMessage());			
		}
		
		try {
			
			List<CallEntry> appels = fbClient.getCallManager().getCallEntries();
			PhoneCallComparator comparator = new PhoneCallComparator();
			Collections.sort(appels, comparator);

			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (CallEntry call: appels) {
					if (call.getTimeStamp().after(lastPhoneCheck)) {
						for (String itemName: items) {
							FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
							if (bindingConfig.commandParam == null || bindingConfig.commandParam.equalsIgnoreCase(call.getType())) {
								switch (bindingConfig.commandType) {
								case CALLSTATUS :
									setItemValue(bindingConfig.item, call.getType());
									break;
								case CALLDURATION:
									setItemValue(bindingConfig.item, (long)call.getDuration());
									break;
								case CALLNUMBER:
									setItemValue(bindingConfig.item, call.getNumber());
									break;
								case CALLTIMESTAMP:
									setDateTimeValue(bindingConfig.item, call.getDateTime());
									break;
								case CALLNAME :
									setItemValue(bindingConfig.item, call.getName());
									break;
								}
							}
						}						
					}
				}
				lastPhoneCheck.setTimeInMillis(System.currentTimeMillis());
			}				
		} catch (FreeboxException e) {
			logger.info("CallEntries: " + e.getMessage());			
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (!isProperlyConfigured()) {
			logger.info("Freebox binding is not properly configured. Command is ignored.");			
			return;
		}
			
		for (FreeboxBindingProvider provider : providers) {
			FreeboxBindingConfig config = provider.getConfig(itemName);
			if (config == null) continue;
			Boolean value = null;
			switch (config.commandType) {
				case LCDBRIGHTNESS :
					if (command instanceof DecimalType) {
						try {
							LCDConfig lcd = fbClient.getLCDManager().getLCDConfig();
							int valeur = ((DecimalType)command).intValue();						
							lcd.setBrightness(new Integer(valeur));
							fbClient.getLCDManager().setLCDConfig(lcd);
						} catch (FreeboxException e) {
							logger.info(e.getMessage());
						}
					}
					break;
				case LCDORIENTATION :
					if (command instanceof DecimalType) {
						try {
							LCDConfig lcd = fbClient.getLCDManager().getLCDConfig();
							int valeur = ((DecimalType)command).intValue();						
							lcd.setOrientation(new Integer(valeur));
							lcd.setOrientationForced(true);
							fbClient.getLCDManager().setLCDConfig(lcd);
						} catch (FreeboxException e) {
							logger.info(e.getMessage());
						}
					}
					break;
				case LCDFORCED :
					try {
						LCDConfig lcd = fbClient.getLCDManager().getLCDConfig();
						lcd.setOrientationForced(command.equals(OnOffType.ON) ? true : false);
						fbClient.getLCDManager().setLCDConfig(lcd);
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					break;
				case WIFISTATUS :
					try {
						WifiGlobalConfig wc = new WifiGlobalConfig();
						wc.setEnabled(command.equals(OnOffType.ON) ? true : false);
						fbClient.getWifiManager().setGlobalConfig(wc);
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					// Get the current state
					try {
						WifiGlobalConfig wc = fbClient.getWifiManager().getGlobalConfig();
						value = wc.getEnabled();
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					break;
				case REBOOT : 
					try {
						fbClient.getSystemManager().Reboot();
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					break;
				case FTPSTATUS : 
					try {
						FtpConfig fc = new FtpConfig();
						fc.setEnabled(command.equals(OnOffType.ON) ? true : false);
						fbClient.getFtpManager().setConfig(fc);
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					// Get the current state
					try {
						FtpConfig fc = fbClient.getFtpManager().getConfig();
						value = fc.getEnabled();
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					break;
				case AIRMEDIASTATUS : 
					try {
						AirMediaConfig ac = new AirMediaConfig();
						ac.setEnabled(command.equals(OnOffType.ON) ? true : false);
						fbClient.getAirMediaManager().setConfig(ac);
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					// Get the current state
					try {
						AirMediaConfig ac = fbClient.getAirMediaManager().getConfig();
						value = ac.getEnabled();
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					break;
				case UPNPAVSTATUS : 
					try {
						UPnPAVConfig uc = new UPnPAVConfig();
						uc.setEnabled(command.equals(OnOffType.ON) ? true : false);
						fbClient.getUPnPAVManager().setConfig(uc);
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					// Get the current state
					try {
						UPnPAVConfig uc = fbClient.getUPnPAVManager().getConfig();
						value = uc.getEnabled();
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					break;
				case SAMBAFILESTATUS : 
					try {
						SambaConfig sc = new SambaConfig();
						sc.setFileShareEnabled(command.equals(OnOffType.ON) ? true : false);
						fbClient.getNetShareManager().setSambaConfig(sc);
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					// Get the current state
					try {
						SambaConfig sc = fbClient.getNetShareManager().getSambaConfig();
						value = sc.getFileShareEnabled();
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					break;
				case SAMBAPRINTERSTATUS : 
					try {
						SambaConfig sc = new SambaConfig();
						sc.setPrintShareEnabled(command.equals(OnOffType.ON) ? true : false);
						fbClient.getNetShareManager().setSambaConfig(sc);
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					// Get the current state
					try {
						SambaConfig sc = fbClient.getNetShareManager().getSambaConfig();
						value = sc.getPrintShareEnabled();
					} catch (FreeboxException e) {
						logger.info(e.getMessage());
					}
					break;
			}
			// Refresh items with the current ON/OFF state
			if (value != null) {
				updateOnOffItems(config.commandType, value);
			}
		}
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			setProperlyConfigured(false);
			
			deviceName = (String) config.get("device");
			if (isBlank(deviceName)) {	// The only mandatory parameter is tested first
				throw new ConfigurationException("freebox",
					"The parameter 'device' is missing! Please refer to your 'openhab.cfg'");
			}

			String refreshConfig = (String) config.get("refresh");
			if (!isBlank(refreshConfig)) {
				refreshInterval = Long.parseLong(refreshConfig);
			}
			
			serverAddress = (String) config.get("server");
			if (isBlank(serverAddress)) serverAddress = "mafreebox.freebox.fr";
			
			appToken = (String) config.get("apptoken");	
			
			try {
				authorize();
			} catch (FreeboxException e) {
				logger.info(e.getMessage());
			}

			setProperlyConfigured((loginManager != null) && loginManager.isConnected());
			logger.info("Freebox binding " + (isProperlyConfigured() ? "" : "not ") + "properly configured");
		}
	}
	
	/**
	 * Handles connection to the Freebox, including validation of the Apptoken
	 * if none is provided in 'openhab.cfg'
	 * @throws FreeboxException
	 */
	private void authorize() throws FreeboxException {
		logger.debug("Appname : " + appName);
		logger.debug("AppVersion : " + appVersion);
		logger.debug("DeviceName : " + deviceName);
		logger.debug("AppID :" + appID);
		
		fbClient = new FreeboxOsClient(appID,serverAddress);
		loginManager = fbClient.getLoginManager();
		
		TrackAuthorizeStatus authorizeStatus = TrackAuthorizeStatus.UNKNOWN;
		if (isBlank(appToken)) {
			Authorize authorize = loginManager.newAuthorize(appName,appVersion,deviceName);
			appToken = authorize.getAppToken();
			logger.info("####################################################################");
			logger.info("# Please accept activation request directly on your freebox        #");
			logger.info("# Once done, record current Apptoken in your 'openhab.cfg'         #");
			logger.info("# " + appToken +" #");
			logger.info("####################################################################");
				
			do {
				try {
					Thread.sleep(2000);
					authorizeStatus = loginManager.trackAuthorize();
				} catch (InterruptedException e) {
					logger.info(e.getMessage());
				}
					
			} while (authorizeStatus == TrackAuthorizeStatus.PENDING);	
				
		} else {
			authorizeStatus = TrackAuthorizeStatus.GRANTED;
		}
		
		if (authorizeStatus != TrackAuthorizeStatus.GRANTED)
			throw new FreeboxException(authorizeStatus.toString());

		logger.debug("Apptoken valide : [" + appToken + "]");	
		loginManager.setAppToken(appToken);
		loginManager.openSession();
	}
	
	/**
	 * Update switch items attached to a particular command type
	 */
	private void updateOnOffItems(CommandType commandType, boolean value) {
		for (FreeboxBindingProvider provider : providers) {
			Collection<String> items = provider.getItemNames();
			
			for (String itemName: items) {
				FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
				
				if (bindingConfig.commandType == commandType) {
					setItemValue(bindingConfig.item,value);
				}
			}
		}				
	}
	
	/**
	 * A comparator of phone calls by ascending date and time
	 */
	private class PhoneCallComparator implements Comparator<CallEntry> {

		@Override
		public int compare(CallEntry call1, CallEntry call2) {
			int result = 0;
			if (call1.getTimeStamp().before(call2.getTimeStamp())) {
				result = -1;
			} else if (call1.getTimeStamp().after(call2.getTimeStamp())) {
				result = 1;
			}
			return result;
		}

	}
}
