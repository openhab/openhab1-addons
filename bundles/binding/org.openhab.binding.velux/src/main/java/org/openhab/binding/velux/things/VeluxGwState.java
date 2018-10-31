/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product representation.
 * <P>
 * Combined set of information describing a single Velux gateway state.
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxGwState {
    private final Logger logger = LoggerFactory.getLogger(VeluxGwState.class);

    
    // Type definition
    
    public enum VeluxGatewayState {
    	GW_S_UNKNOWN(		-1,		"Unkwown state."),
    	GW_S_TEST(			 0,		"Test mode."),
    	GW_S_GWM_EMPTY(		 1,		"Gateway mode, no actuator nodes in the system table."),
    	GW_S_GWM(			 2,		"Gateway mode, with one or more actuator nodes in the system table."),
    	GW_S_BM_UNCONFIG(	 3,		"Beacon mode, not configured by a remote controller."),
    	GW_S_BM(			 4,		"Beacon mode, has been configured by a remote controller."),
        GW_STATE_RESERVED(	255,	"Reserved");

        // Class internal

        private int stateValue;
        private String stateDescription;

        // Reverse-lookup map for getting a VeluxGatewayState from an TypeId
        private static final Map<Integer, VeluxGatewayState> lookupTypeId2Enum = new HashMap<Integer, VeluxGatewayState>();

        static {
            for (VeluxGatewayState stateValue : VeluxGatewayState.values()) {
                lookupTypeId2Enum.put(stateValue.getStateValue(), stateValue);
            }
        }

        // Constructor

        private VeluxGatewayState(int stateValue, String stateDescription) {
            this.stateValue = stateValue;
            this.stateDescription = stateDescription;
        }

        VeluxGatewayState(String categoryString) {
            try {
                this.stateValue = VeluxGatewayState.valueOf(categoryString).getStateValue();
            } catch (IllegalArgumentException e) {
                try {
                      this.stateValue = VeluxGatewayState.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase())
                            .getStateValue();
                } catch (IllegalArgumentException e2) {
                    this.stateValue = -1;
                }
            }
        }

        // Class access methods

        public int getStateValue() {
            return stateValue;
        }

        public String getStateDescription() {
            return stateDescription;
        }

        public static VeluxGatewayState get(int stateValue) {
            return lookupTypeId2Enum.get(stateValue);
        }
        
        
    }
    
    public enum VeluxGatewaySubState {
    	GW_SS_UNKNOWN(		-1,		"Unkwown state."),
    	GW_SS_IDLE(			 0,		"Idle state."),
    	GW_SS_P1(		 	 1,		"Performing task in Configuration Service handler."),
    	GW_SS_P2(			 2,		"Performing Scene Configuration."),
    	GW_SS_P3(	 		 3,		"Performing Information Service Configuration."),
    	GW_SS_P4(			 4,		"Performing Contact input Configuration."),
        GW_SS_PFF(			88,		"Reserved");

        // Class internal

        private int stateValue;
        private String stateDescription;

        // Reverse-lookup map for getting a VeluxGatewayState from an TypeId
        private static final Map<Integer, VeluxGatewaySubState> lookupTypeId2Enum = new HashMap<Integer, VeluxGatewaySubState>();

        static {
            for (VeluxGatewaySubState stateValue : VeluxGatewaySubState.values()) {
                lookupTypeId2Enum.put(stateValue.getStateValue(), stateValue);
            }
        }

        // Constructor

        private VeluxGatewaySubState(int stateValue, String stateDescription) {
            this.stateValue = stateValue;
            this.stateDescription = stateDescription;
        }

        VeluxGatewaySubState(String categoryString) {
            try {
                this.stateValue = VeluxGatewaySubState.valueOf(categoryString).getStateValue();
            } catch (IllegalArgumentException e) {
                try {
                      this.stateValue = VeluxGatewaySubState.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase())
                            .getStateValue();
                } catch (IllegalArgumentException e2) {
                    this.stateValue = -1;
                }
            }
        }

        // Class access methods

        public int getStateValue() {
            return stateValue;
        }

        public String getStateDescription() {
            return stateDescription;
        }

        public static VeluxGatewaySubState get(int stateValue) {
            return lookupTypeId2Enum.get(stateValue);
        }
    }


    // Class internal

    private VeluxGatewayState gwState;
    private byte subStateValue;

    // Constructor

    public VeluxGwState(byte stateValue, byte subStateValue) {
        logger.trace("VeluxGwState() created.");

        this.gwState = VeluxGatewayState.get(stateValue);
        this.subStateValue = subStateValue;
    }

    
    // Class access methods

    public String toString() {
        return this.gwState.name();
    }

    public String toDescription() {
        return this.gwState.getStateDescription();
    }
        
    public byte getSubState() {
        return this.subStateValue;
    }

}

/**
 * end-of-VeluxGWState.java
 */
