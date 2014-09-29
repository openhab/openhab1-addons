/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.enocean.internal.profiles;

import org.opencean.core.common.ParameterAddress;
import org.opencean.core.common.values.Value;
import org.opencean.core.eep.WindowHandle.Positions;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

/**
 * Bridge class to transform Window Handle positions to window states
 * Use an String Item to visualize the window state
 * 
 * @author Holger Ploch (contact@holger-ploch.de)
 * 
 */
public class WindowHandleProfile extends BasicProfile {


	
	
    public WindowHandleProfile(Item item, EventPublisher eventPublisher) {
        super(item, eventPublisher);
    }
    
    

    @Override
    public void valueChanged(ParameterAddress parameterAddress, Value valueObject) {
        Command command = null;
        
        if (valueObject.getValue().equals(Positions.DOWN.toString()))
        	command = new StringType("CLOSED");
        else if (valueObject.getValue().equals(Positions.MIDDLE.toString()))
        	command = new StringType("OPEN");
        else if (valueObject.getValue().equals(Positions.UP.toString()))
    		command = new StringType("AJAR"); 
          
        postCommand(command);
    }


}



