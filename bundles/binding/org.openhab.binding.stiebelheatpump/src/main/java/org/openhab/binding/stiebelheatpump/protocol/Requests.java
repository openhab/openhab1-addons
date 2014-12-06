/**
 * Copyright 2014 
 * This file is part of stiebel heat pump reader.
 * It is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * It is  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with the project. 
 * If not, see http://www.gnu.org/licenses/.
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Requests class for Stiebel heat pump.
 * 
 * @author Peter Kreutzer
 */
@XmlRootElement(name = "requests")
@XmlAccessorType(XmlAccessType.FIELD)
public class Requests {

	@XmlElement(name = "request")
	private List<Request> requests = new ArrayList<Request>();

	public Requests() {
	}

	public Requests(List<Request> requests) {
		this.requests = requests;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}

	public static <T> List<T> searchIn(List<T> list, Matcher<T> m) {
		List<T> r = new ArrayList<T>();
		for (T t : list) {
			if (m.matches(t)) {
				r.add(t);
			}
		}
		return r;
	}

	public interface Matcher<T> {
		public boolean matches(T t);
	}
}
