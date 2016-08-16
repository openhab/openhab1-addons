/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.locks.ReentrantLock;

import Moka7.*;

import org.openhab.binding.plclogo.PLCLogoBindingProvider;
import org.openhab.binding.plclogo.PLCLogoBindingConfig;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author g8kmh
 * @since 1.5.0
 */
public class PLCLogoBinding extends AbstractActiveBinding<PLCLogoBindingProvider> implements ManagedService {

  private final ReentrantLock lock = new ReentrantLock();
  private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);
  private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(.*?)$");

  /** 
   * the refresh interval which is used to poll values from the PlcLogo
   * server (optional, defaults to 500ms)
   */
  private long refreshInterval = 5000;

  /** 
   * Buffer for read/write operations
   */
  private byte data[] = new byte[1470];
  
  private static Map<String,PLCLogoConfig> controllers = new HashMap<String, PLCLogoConfig>();

  private int ReadLogoDBArea(S7Client client) {
    int result = 0;

    int offset = 0;
    while ((result == 0) && (offset < data.length)) {
      byte buffer[] = new byte[Math.min(data.length - offset, 1024)];
      result = client.ReadArea(S7.S7AreaDB, 1, offset, buffer.length, buffer);
      System.arraycopy(buffer, 0, data, offset, buffer.length);
      offset = offset + buffer.length;
    }
    
    return result;
  }
  
  private int WriteLogoDBArea(S7Client client) {
    return client.WriteArea(S7.S7AreaDB, 1, 0, data.length, data);
  }

  private void ReconnectLogo(S7Client client) {
    // try and reconnect
    client.Disconnect();
    client.Connect();
    if (client.Connected) {
      logger.warn("Reconnect successful");
    }
  }
  
  public PLCLogoBinding() {
    logger.info("PLCLogoBinding constructor");
  }
  
  public void activate() {
  }
  
  public void deactivate() {
    for (PLCLogoBindingProvider provider : providers) {
      provider.removeBindingChangeListener(this);
    }

    providers.clear();
    Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
    while (entries.hasNext()) {
      Entry<String, PLCLogoConfig> thisEntry =  entries.next();
      PLCLogoConfig logoConfig = (PLCLogoConfig) thisEntry.getValue();
      S7Client LogoS7Client = logoConfig.getS7Client();
      if (LogoS7Client != null) {
        LogoS7Client.Disconnect();
      }
    }
    controllers.clear();
  }

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
    return "PLCLogo Polling Service";
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  protected void execute() {
    if (!bindingsExist()) {
      logger.debug("There is no existing plclogo binding configuration => refresh cycle aborted!");
      return;
    }

    Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
    while (entries.hasNext()) {
      Entry<String, PLCLogoConfig> thisEntry = entries.next();
      String controllerName = (String) thisEntry.getKey();
      PLCLogoConfig logoConfig = (PLCLogoConfig) thisEntry.getValue();
      S7Client LogoS7Client = logoConfig.getS7Client();
      if (LogoS7Client == null) {
        logger.debug("No S7client for "+ controllerName);
      } else {
        lock.lock();
        int result = ReadLogoDBArea(LogoS7Client);
        lock.unlock();

        if (result != 0) {
          logger.warn("Failed to read memory: " + LogoS7Client.ErrorText(result) + " Reconnecting...");
          ReconnectLogo(LogoS7Client);
          return;
        }
        // Now have the LOGO! memory (note: not suitable for S7) - more efficient than multiple reads (test shows <14mS to read all)
        // iterate through bindings to see what has changed - this approach assumes a small number (< 100)of bindings
        // otherwise might see what has changed in memory and map to binding
      }
 
      for (PLCLogoBindingProvider provider : providers) {
        for (String itemName : provider.getItemNames()) {
          PLCLogoBindingConfig config = (PLCLogoBindingConfig) provider.getBindingConfig(itemName);
          if (config.getControllerName().equals(controllerName)) {
            int address = config.getAddress();
            String block = config.getBlock();

            // it is for our currently selected controller
            int currentValue = Integer.MIN_VALUE;
            if (config.isDigital()) {
              currentValue = S7.GetBitAt(data, address, config.getBit()) ? 1 : 0;
            } else {
              currentValue = S7.GetWordAt(data, address);
            }

            if (currentValue != config.getLastValue()) {
              int delta = Math.abs(config.getLastValue() - currentValue);
              if (!config.isDigital() && (delta < config.getThreshold())) {
                continue;
              }

              Item item = provider.getItem(itemName);
              boolean isValid = block.startsWith("I") && item instanceof ContactItem;
              isValid = isValid || (block.startsWith("M") && (item instanceof ContactItem || item instanceof SwitchItem));
              isValid = isValid || (block.startsWith("Q") && item instanceof SwitchItem);
              isValid = isValid || (block.startsWith("NI") && item instanceof ContactItem);
              isValid = isValid || (block.startsWith("NQ") && item instanceof SwitchItem);
              isValid = isValid || (block.startsWith("VB") && (item instanceof ContactItem || item instanceof SwitchItem));
              isValid = isValid || (block.startsWith("VW") && (item instanceof ContactItem || item instanceof SwitchItem));
              if (item instanceof NumberItem || isValid) {
                eventPublisher.postUpdate(itemName, createState(item, currentValue));
                config.setLastvalue(currentValue);
              } else {
                logger.warn("Block " + block + " is incompatible with item " + item.getName());
              }
            }
          }          
        }
      }
    }
  }

  /**
   * @{inheritDoc}
   */
  @Override
  protected void internalReceiveCommand(String itemName, Command command) {
    // the code being executed when a command was sent on the openHAB
    // event bus goes here. This method is only called if one of the 
    // BindingProviders provide a binding for the given 'itemName'.
    // Note itemname is the item name not the controller name/instance!
    //
    super.internalReceiveCommand(itemName, command);
    logger.debug("internalReceiveCommand() is called!");

    for (PLCLogoBindingProvider provider : providers) {
      if (!provider.providesBindingFor(itemName))
        continue;
      
      PLCLogoBindingConfig config = provider.getBindingConfig(itemName);
      if ((config.getAddress() < 0) || (config.getAddress() > 1469)) {
        logger.warn("Invalid write requested at memory location " + config.getAddress() );
        continue;  
      }
        
      if (!controllers.containsKey(config.getControllerName())) {
        logger.warn("Invalid write requested for controller "+ config.getControllerName());
        continue;
      }
      
      // Send command to the LOGO! controller memory
      PLCLogoConfig controller = controllers.get(config.getControllerName());

      S7Client LogoS7Client = controller.getS7Client();
      if (LogoS7Client == null) {
        logger.debug("No S7client for "+ config.getControllerName());
        return;
      } else {
        lock.lock();
        int result = ReadLogoDBArea(LogoS7Client);
        if (result == 0) {
          Item item = config.getItem();
          int address = config.getAddress();
          if (item instanceof NumberItem && !config.isDigital()) {
            if (command instanceof DecimalType) {
              S7.SetWordAt(data, address, ((DecimalType)command).intValue());
            }
          } else if (item instanceof SwitchItem && config.isDigital()) {
            if (command instanceof OnOffType) {
              S7.SetBitAt(data, address, config.getBit(), command == OnOffType.ON ? true : false);
            }
          }

          result = WriteLogoDBArea(LogoS7Client);      
          if (result != 0) {
            logger.warn("Failed to write memory: " + LogoS7Client.ErrorText(result) + " Reconnecting...");
            ReconnectLogo(LogoS7Client);
          }
        } else {
          logger.warn("Failed to read memory: " + LogoS7Client.ErrorText(result) + " Reconnecting...");
          ReconnectLogo(LogoS7Client);
        }
        lock.unlock();
      }
    }
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  protected void internalReceiveUpdate(String itemName, State newState) {
    // the code being executed when a state was sent on the openHAB
    // event bus goes here. This method is only called if one of the 
    // BindingProviders provide a binding for the given 'itemName'.
    super.internalReceiveUpdate(itemName, newState);
    logger.debug("internalReceiveUpdate() is called!");
  }
    
  /**
   * @{inheritDoc}
   */
  @Override
  public void updated(Dictionary<String, ?> config) throws ConfigurationException {
    InetAddress LogoIP;
    Boolean configured = false;
    if (config != null) {
      String refreshIntervalString = (String) config.get("refresh");
      if (StringUtils.isNotBlank(refreshIntervalString)) {
        refreshInterval = Long.parseLong(refreshIntervalString);
      }

      if ( controllers == null ) {
        controllers = new HashMap<String, PLCLogoConfig>();
      }
      
      Enumeration<String> keys = config.keys();
      while (keys.hasMoreElements()) {
        String key = (String) keys.nextElement();

        // the config-key enumeration contains additional keys that we
        // don't want to process here ...
        if ("service.pid".equals(key)) {
          continue;
        }
      
        Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);
        if (!matcher.matches()) {
          continue;
        }

        matcher.reset();
        matcher.find();
        String controllerName = matcher.group(1);
        PLCLogoConfig deviceConfig = controllers.get(controllerName);
        logger.info("Config for "+ controllerName );

        if (deviceConfig == null) {
          deviceConfig = new PLCLogoConfig(controllerName);
          controllers.put(controllerName, deviceConfig);
        }
        if (matcher.group(2).equals("host")) {
          // matcher.find();
          String IP = config.get(key).toString();
          deviceConfig.setIP(IP);
          logger.info("Config for "+ IP );
          configured=true;
        }
        if (matcher.group(2).equals("remoteTSAP")) {
          // matcher.find();
          String remoteTSAP = config.get(key).toString();
          logger.info("Config for " + remoteTSAP);

          deviceConfig.setRemoteTSAP(Integer.decode(remoteTSAP));
        }
        if (matcher.group(2).equals("localTSAP")) {
          // matcher.find();
          String localTSAP = config.get(key).toString();
          logger.info("Config for "+ localTSAP);

          deviceConfig.setLocalTSAP(Integer.decode(localTSAP));
        }
      } //while

    Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
    while (entries.hasNext()) {
      Entry<String, PLCLogoConfig> thisEntry = entries.next();
      String controllerName = (String) thisEntry.getKey();
      PLCLogoConfig logoConfig = (PLCLogoConfig) thisEntry.getValue();
      S7Client LogoS7Client = logoConfig.getS7Client();
      if (LogoS7Client == null) {
        LogoS7Client = new Moka7.S7Client();
      } else {
        LogoS7Client.Disconnect();
      }
      LogoS7Client.SetConnectionParams(logoConfig.getlogoIP(), logoConfig.getLocalTSAP(), logoConfig.getRemoteTSAP());
      logger.info("About to connect to "+ controllerName );

      if ((LogoS7Client.Connect() == 0) && LogoS7Client.Connected) {
        logger.info("Connected to PLC LOGO! device "+ controllerName);
      }  else {
        logger.info("Could not connect to PLC LOGO! device "+ controllerName);
        throw new ConfigurationException("Could not connect to PLC device ", controllerName +" " + logoConfig.getlogoIP().toString() );
      }
      logoConfig.setS7Client(LogoS7Client);
    }
        
    setProperlyConfigured(configured);
    } else {
      logger.info("No configuration for PLCLogoBinding" );
    }
  }

  private State createState(Item item, Object value) {
    if (item instanceof StringType) {
      return new StringType((String) value);
    } else if (item instanceof NumberItem) {
      if (value instanceof Number) {
        return new DecimalType(value.toString());
      } else if (value instanceof String) {
        String stringValue = ((String) value).replaceAll("[^\\d|.]", "");
        return new DecimalType(stringValue);
      } else {
        logger.warn("Item " + item.getName() + " got invalid value " + value.toString() + ".");
        return null;
      }
    } else if (item instanceof SwitchItem) {
      return  ((int)value > 0) ? OnOffType.ON : OnOffType.OFF;
    } else if (item instanceof ContactItem) {
      return  ((int)value > 0) ? OpenClosedType.CLOSED: OpenClosedType.OPEN;
    } else {
      logger.warn("Item " + item.getName() + " got invalid value " + value.toString() + ".");
      return null;
    }
  }

  private class PLCLogoConfig {
    /**
     * Class which represents a LOGO! online controller/PLC connection params
     * and current instance - there may be multiple PLC's
     *  
     * @author g8kmh
     * @since 1.5.0
     */

    private final String instancename;
    private String logoIP;
    private int localTSAP = 0x0300;
    private int remoteTSAP = 0x0200;
    private S7Client LogoS7Client;
    
    public PLCLogoConfig (String instancename){
      this.instancename = instancename;
    }
    public void setIP(String logoIP){
      this.logoIP = logoIP;
    }
    public void setLocalTSAP(int localTSAP){
      this.localTSAP = localTSAP;
    }
    public void setRemoteTSAP(int remoteTSAP){
      this.remoteTSAP = remoteTSAP;
    }
    public void setS7Client(S7Client LogoS7Client){
      this.LogoS7Client=LogoS7Client;
    }
    public String getlogoIP(){
      return logoIP;
    }
    public String getintancename(){
      return instancename;
    }
    public int getLocalTSAP(){
      return localTSAP;
    }
    public int getRemoteTSAP(){
      return remoteTSAP;
    }
    public S7Client getS7Client(){
      return LogoS7Client;
    }
    }

  }

