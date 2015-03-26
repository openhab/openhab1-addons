/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.config;

import java.util.List;

import org.osgi.framework.Version;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * Implements the product class for the XML product database
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class ZWaveDbProduct {
	@XStreamImplicit
	public List<ZWaveDbProductReference> Reference;

	public String Model;

	@XStreamImplicit
	public List<ZWaveDbLabel> Label;

	@XStreamImplicit
	private List<ZWaveDbConfigFile> ConfigFile;

	@XStreamConverter(value=ToAttributedValueConverter.class, strings={"Filename", "VersionMin", "VersionMax"})
	private class ZWaveDbConfigFile {
		String VersionMin;
		String VersionMax;
		
		String Filename;
	}

	public String getConfigFile(String version) {
		if(ConfigFile == null) {
			return null;
		}

		Version vIn = new Version(version);
		String filename = null;
		// Check for a version'ed file
		// There are multiple permutations of the file that we need to account for -:
		// * No version information
		// * Only a min version
		// * Only a max version
		// * Both min and max versions
		// Versions need to be evaluated with the max and min specifiers separately
		// i.e. the part either side of the decimal.
		// So, version 1.3 is lower than version 1.11 
		for(ZWaveDbConfigFile cfg : ConfigFile) {
			// Find a default - ie one with no version information
			if(cfg.VersionMin == null && cfg.VersionMax == null && filename == null) {
				filename = cfg.Filename;
				continue;
			}

			if(cfg.VersionMin != null) {
				Version vMin = new Version(cfg.VersionMin);
				if(vIn.compareTo(vMin) < 0) {
					continue;
				}
			}

			if(cfg.VersionMax != null) {
				Version vMax = new Version(cfg.VersionMax);

				if(vIn.compareTo(vMax) > 0) {
					continue;
				}
			}
			
			// This version matches the criterea
			return cfg.Filename;
		}

		// Otherwise return the default if there was one!
		return filename;
	}
}
