/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.core.library.types;

import org.openhab.core.types.Command;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;

/**
 * @author Thomas.Eichstaedt-Engelen
 */
public class SceneType implements PrimitiveType, State, Command {
	
	public final static int ACTIVATE_SCENE = new Integer(0);
	public final static int LEARN_SCENE = new Integer(1);

	protected Integer mode;
	protected Integer sceneNumber; 
	
	public SceneType() {
		this.mode = ACTIVATE_SCENE;
		this.sceneNumber = new Integer(-1);
	}
	
	public SceneType(Integer sceneNumber) {
		this(ACTIVATE_SCENE, sceneNumber);
	}

	public SceneType(Integer mode, Integer sceneNumber) {
		this.mode = mode;
		this.sceneNumber = sceneNumber;
	}
	
	public SceneType(String sceneNumber) {
		this("" + ACTIVATE_SCENE, sceneNumber);
	}
	
	public SceneType(String mode, String sceneNumber) {
		this.mode = new Integer(mode);
		this.sceneNumber = new Integer(sceneNumber);
	}

	public String toString() {
		return sceneNumber.toString();
	}
	

	public static SceneType valueOf(String sceneNumber) {
		return new SceneType(sceneNumber);
	}
	
	public static SceneType valueOf(String mode, String sceneNumber) {
		return new SceneType(mode, sceneNumber);
	}
	
	
	public String format(String pattern) {
		return String.format(pattern, sceneNumber);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result
				+ ((sceneNumber == null) ? 0 : sceneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SceneType other = (SceneType) obj;
		if (mode == null) {
			if (other.mode != null)
				return false;
		} else if (!mode.equals(other.mode))
			return false;
		if (sceneNumber == null) {
			if (other.sceneNumber != null)
				return false;
		} else if (!sceneNumber.equals(other.sceneNumber))
			return false;
		return true;
	}
	

}
