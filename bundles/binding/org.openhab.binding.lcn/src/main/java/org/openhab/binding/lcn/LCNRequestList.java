/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openhab.binding.lcn.LCNBinding.Credentials;
import org.openhab.binding.lcn.logic.data.LCNInputModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a list of LCNInputModules that require repeated status requests in order to be maintained.
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNRequestList extends ArrayList<LCNInputModule> {

	/**Generated serial.*/
	private static final long serialVersionUID = -7749420080699961019L;
	/**Represents whether the list has an active request or not.*/
	public volatile boolean locked = false;
	/**Logger to handle output.*/
	Logger logger = LoggerFactory.getLogger(LCNRequestList.class);
	
	/**
	 * {@inheritDoc}
	 * This method does not actually check the content of the contained Objects, but only the Objects themselves.
	 * Therefore it is not recommended to use this method. Please use {@link #contains(Object, int)} instead.
	 */
	@Override
	@Deprecated
	public boolean contains(Object o) {
		return super.contains(o);
	}
	
	/**
	 * Since this list contains LCNInputModules, comparison has to use the correct methods
	 * of said class. Classes could be different instances but still refer to the same module.
	 * @param o The object which is to be checked.
	 * @return true if a reasonably similar object is contained, otherwise false.
	 */
	public boolean contains(Object o, int homeSegment) {
		
		boolean result = false;
			
		try {
			
			LCNInputModule mod = (LCNInputModule) o;
			
			for (LCNInputModule module : this) {
				
				if (null != module && module.equals(mod, homeSegment)) {
					
					result = true;
					break;
					
				}
				
			}
			
		} catch (ClassCastException exc) {
			
		}

		return result;
		
	}	
	
	/**
	 * {@inheritDoc}
	 * This method does not actually check the content of the contained Objects, but only the Objects themselves.
	 * Therefore it is not recommended to use this method. Please use {@link #remove(Object, int)} instead.
	 */
	@Override
	@Deprecated
	public boolean remove(Object o) {	
		return super.remove(o);		
	}
	
	/**
	 * Overload for 'remove' since it needs to use a different form of 'equals'.
	 * @param o The Object in question.
	 * @param homeSegment The home segment.
	 */
	public synchronized void remove(Object o, int homeSegment) {
		
		if (this.contains(o, homeSegment)) {
			
			Iterator<LCNInputModule> iter = this.iterator();
			
			while(iter.hasNext()) {
				
				LCNInputModule temp = iter.next();
				
				if (temp.equals(o, homeSegment)) {
					iter.remove();
				}
				
			}
			
		}
		
	}
	
	/**
	 * Deletes all elements that are connected to the given provider.
	 * @param provider The provider for which all stored Objects have to be deleted.
	 */
	public void clear(LCNBindingProvider provider, Map<String, String> firmwares, Map<String, Credentials> credentialMap) {
		List<LCNInputModule> toDelete = new ArrayList<LCNInputModule>();
		for (LCNInputModule mod : this) {
			if (provider.contains(mod)) {
				toDelete.add(mod);
			}
		}
		
		for (LCNInputModule mod : toDelete) {
			firmwares.remove(LCNInputModule.generateKey(mod, credentialMap.get(mod.id).homeSegment));
			this.remove(mod); //is fine here, because we got the object from this very list, so it HAS to be there!
		}
		
	}
	
}
