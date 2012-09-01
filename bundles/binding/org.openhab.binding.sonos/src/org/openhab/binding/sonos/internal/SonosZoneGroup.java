package org.openhab.binding.sonos.internal;

import java.util.ArrayList;
import java.util.Collection;
/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
 * 
 */
import java.util.List;

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
