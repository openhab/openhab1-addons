/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal;

/**
 * The DSMRVersion specifies the DSMR specification version
 * <p>
 * The following versions are special versions:
 * <p>
 * <ul>
 * <li>NONE - This can be used for filtering purposes
 * <li>ALL - This can be used to identify all versions
 * <p>
 * This enumeration also has some convenience arrays for grouping versions
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public enum DSMRVersion {
	/** Special version for filtering purposes */
	NONE("None"),
	/** Special version that indicates all versions */
	ALL("All"),
	/** DSMR Specification v2.1 */
	V21("v2.1"),
	/** DSMR Specification v2.2 */
	V22("v2.2"),
	/** DSMR Specification v3.0 */
	V30("v3.0"),
	/** DSMR Specification v4.0 */
	V40("v4.0"),
	/** DSMR Specification v4.04 */
	V404("v4.04");

	/** String representation of the DSMR version */
	public final String version;

	/**
	 * Construct a new Enum type
	 * 
	 * @param version
	 *            String representation of the Enum
	 */
	private DSMRVersion(String version) {
		this.version = version;
	}

	/** Compatibility for all versions */
	public static final DSMRVersion[] ALL_VERSIONS = new DSMRVersion[] { V21,
			V22, V30, V40, V404 };
	/** Compatibility for versions v2.1 and later */
	public static final DSMRVersion[] V21_UP = new DSMRVersion[] { V21, V22,
			V30, V40, V404 };
	/** Compatibility for versions v2.2 and later */
	public static final DSMRVersion[] V22_UP = new DSMRVersion[] { V22, V30,
			V40, V404 };
	/** Compatibility for versions v3.0 and later */
	public static final DSMRVersion[] V30_UP = new DSMRVersion[] { V30, V40,
			V404 };
	/** Compatibility for versions v4.0 and later */
	public static final DSMRVersion[] V40_UP = new DSMRVersion[] { V40 };
	/** Compatibility for versions v4.04 and later */
	public static final DSMRVersion[] V404_UP = new DSMRVersion[] { V404 };
	/** Compatibility for all v2.x versions */
	public static final DSMRVersion[] V2_VERSIONS = new DSMRVersion[] { V21,
			V22 };
	/** Compatibility for all v3.x versions */
	public static final DSMRVersion[] V3_VERSIONS = new DSMRVersion[] { V30 };
	/** Compatibility for all v4.x versions */
	public static final DSMRVersion[] V4_VERSIONS = new DSMRVersion[] { V40,
			V404 };

	/**
	 * Returns the DSMRVersion object for the specified version. If the version
	 * is not found the NONE version is returned
	 * 
	 * @param version
	 *            String containing the version
	 * @return corresponding {@link DMSRVersion} or NONE if a unknown version is
	 *         specified
	 */
	public static DSMRVersion getDSMRVersion(String version) {
		for (DSMRVersion t : DSMRVersion.values()) {
			if (t.version.equalsIgnoreCase(version)) {
				return t;
			}
		}
		return NONE;
	}
}
