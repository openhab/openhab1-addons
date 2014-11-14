package org.openhab.binding.zibase.internal;

import java.lang.annotation.Inherited;
import java.util.Arrays;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import fr.zapi.ZbAction;
import fr.zapi.ZbProtocol;
import fr.zapi.ZbResponse;
import fr.zapi.Zibase;

public class zibaseBindingConfigCommand extends zibaseBindingConfig {
	
	/**
	 * position in config array where to find protocol
	 */
	static final int POS_PROTO = 2;
	
	/**
	 * Zibase supported protocols for command sending
	 */
	static final String[] authorizedProtocols = { 
		ZbProtocol.CHACON.toString(),
		ZbProtocol.DOMIA.toString(),
		ZbProtocol.RFS10.toString(),
		ZbProtocol.VISONIC433.toString(),
		ZbProtocol.VISONIC868.toString(),
		ZbProtocol.X10.toString(),
		ZbProtocol.X2D433.toString(),
		ZbProtocol.X2D433ALRM.toString(),
		ZbProtocol.X2D868.toString(),
		ZbProtocol.X2D868ALRM.toString(),
		ZbProtocol.X2D868BOAC.toString(),
		ZbProtocol.X2D868INSH.toString(),
		ZbProtocol.X2D868PIWI.toString(),
		ZbProtocol.ZWAVE.toString(),
	} ;
	
	/**
	 * Constructor
	 * @param configParameters
	 */
	public zibaseBindingConfigCommand(String[] configParameters) {
		super(configParameters);
	}

	
	/**
	 * {@link Inherited}
	 */
	@Override
	public void sendCommand(Zibase zibase, Command command, int dim) {
				
		ZbAction action = ZbAction.valueOf(command.toString());		
		ZbProtocol protocol = ZbProtocol.valueOf(this.getProtocol());
		
		if(dim >= 0) {
			zibase.sendCommand(this.getId(), action, protocol, dim, 1);
		} else {
			zibase.sendCommand(this.getId(), action, protocol);
		}
		
		logger.debug("Send command to " + this.getId() + " : " + action.toString() + " / " + protocol.toString());
	}

	
	/**
	 * get item protocol
	 * @return
	 */
	public String getProtocol() {
		return this.values[this.POS_PROTO];
	}
	
	
	/**
	 * {@link Inherited}
	 */
	protected boolean isItemConfigValid() {
		logger.info("Checking config for Command item " + this.getId());
		
		if (Arrays.binarySearch(this.authorizedProtocols,this.getProtocol()) < 0) {
			logger.error("Unsupported command protocol for item " + this.getId());
			return false;
		}
		
		logger.info("Config OK for Command item " + this.getId());
		return true;
	}


	@Override
	public State getOpenhabStateFromZibaseValue(String zbResponseStr) {
		logger.error("getOpenhabStateFromZibaseValue : not implemented for command item");
		return null;
	}
	
}
