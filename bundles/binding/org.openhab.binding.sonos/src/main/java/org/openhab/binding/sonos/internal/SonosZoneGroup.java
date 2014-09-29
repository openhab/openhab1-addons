/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * Inspired by Copyright 2007 David Wheeler

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 * 
 */
package org.openhab.binding.sonos.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * A ZoneGroup is a grouping of various ZonePlayers. Each ZG is "headed up" by  a coordinator, and all other
 * members of the ZG will "point" their queue (of music to play) to the coordinator. This is the mechanism that
 * Sonos is using to implement "multi-room" capabilities into their system/architecture
 * 
 * @author Karel Goderis 
 * @since 1.1.0
 * 
 */
public class SonosZoneGroup implements Cloneable {

	private final List<String> members;
	private final String coordinator;
	private final String id;

	public Object clone() {
		try
		{
			return super.clone();
		}
		catch(Exception e){ return null; }
	}

	public SonosZoneGroup(String id, String coordinator, Collection<String> members) {
		this.members= new ArrayList<String>(members);
		if (!this.members.contains(coordinator)) {
			this.members.add(coordinator);
		}
		this.coordinator = coordinator;
		this.id = id;
	}

	public List<String> getMembers() {
		return members;
	}

	public String getCoordinator() {
		return coordinator;
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof SonosZoneGroup) {
			SonosZoneGroup group = (SonosZoneGroup) obj;
			return group.getId().equals(getId());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
	  return id.hashCode();
	}
	
}
