/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
