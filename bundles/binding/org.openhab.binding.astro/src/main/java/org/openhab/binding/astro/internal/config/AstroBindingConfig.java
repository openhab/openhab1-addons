/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.config;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.core.binding.BindingConfig;

/**
 * Class with the astro bindingConfig parsed from an item file.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class AstroBindingConfig implements BindingConfig {
	private PlanetName planetName;
	private String type;
	private String property;
	private int offset;

	/**
	 * Creates a astro config without an offset.
	 */
	public AstroBindingConfig(PlanetName planetName, String type, String property) {
		this(planetName, type, property, 0);
	}

	/**
	 * Creates a astro config.
	 */
	public AstroBindingConfig(PlanetName planetName, String type, String property, int offset) {
		this.planetName = planetName;
		this.type = type;
		this.property = property;
		this.offset = offset;
	}

	/**
	 * Returns the planet name.
	 */
	public PlanetName getPlanetName() {
		return planetName;
	}

	/**
	 * Returns the property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the full property string.
	 */
	public String getPlanetProperty() {
		return type + "." + property;
	}

	/**
	 * Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(planetName).append(type).append(property).append(offset).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AstroBindingConfig)) {
			return false;
		}
		AstroBindingConfig comp = (AstroBindingConfig) obj;
		return new EqualsBuilder().append(planetName, comp.getPlanetName()).append(type, comp.getType())
				.append(property, comp.getProperty()).append(offset, comp.getOffset()).isEquals();
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("planet", planetName.toString().toLowerCase()).append("type", type).append("property", property);
		if (offset != 0) {
			tsb.append("offset", offset);
		}
		return tsb.toString();
	}
}
