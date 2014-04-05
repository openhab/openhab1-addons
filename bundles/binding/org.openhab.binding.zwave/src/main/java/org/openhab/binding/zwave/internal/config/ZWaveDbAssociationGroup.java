/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This implements the configuration group for the XML product database.
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveDbAssociationGroup {
		Integer Index;
		Integer Maximum;
		boolean SetToController;
		@XStreamImplicit
		public List<ZWaveDbLabel> Label;
		@XStreamImplicit
		public List<ZWaveDbLabel> Help;
		
		ZWaveDbAssociationGroup() {
			SetToController = false;
		}
}
