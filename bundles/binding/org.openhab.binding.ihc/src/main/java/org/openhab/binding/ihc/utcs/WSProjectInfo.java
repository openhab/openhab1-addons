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

package org.openhab.binding.ihc.utcs;

/**
 * <p>
 * Java class for WSProjectInfo complex type.
 * 
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006
 * (06:55:48 PDT) WSDL2Java emitter.
 */

public class WSProjectInfo {
	private int visualMinorVersion;

	private int visualMajorVersion;

	private int projectMajorRevision;

	private int projectMinorRevision;

	private WSDate lastmodified;

	private java.lang.String projectNumber;

	private java.lang.String customerName;

	private java.lang.String installerName;

	public WSProjectInfo() {
	}

	public WSProjectInfo(int visualMinorVersion, int visualMajorVersion,
			int projectMajorRevision, int projectMinorRevision,
			WSDate lastmodified, java.lang.String projectNumber,
			java.lang.String customerName, java.lang.String installerName) {
		this.visualMinorVersion = visualMinorVersion;
		this.visualMajorVersion = visualMajorVersion;
		this.projectMajorRevision = projectMajorRevision;
		this.projectMinorRevision = projectMinorRevision;
		this.lastmodified = lastmodified;
		this.projectNumber = projectNumber;
		this.customerName = customerName;
		this.installerName = installerName;
	}

	/**
	 * Gets the visualMinorVersion value for this WSProjectInfo.
	 * 
	 * @return visualMinorVersion
	 */
	public int getVisualMinorVersion() {
		return visualMinorVersion;
	}

	/**
	 * Sets the visualMinorVersion value for this WSProjectInfo.
	 * 
	 * @param visualMinorVersion
	 */
	public void setVisualMinorVersion(int visualMinorVersion) {
		this.visualMinorVersion = visualMinorVersion;
	}

	/**
	 * Gets the visualMajorVersion value for this WSProjectInfo.
	 * 
	 * @return visualMajorVersion
	 */
	public int getVisualMajorVersion() {
		return visualMajorVersion;
	}

	/**
	 * Sets the visualMajorVersion value for this WSProjectInfo.
	 * 
	 * @param visualMajorVersion
	 */
	public void setVisualMajorVersion(int visualMajorVersion) {
		this.visualMajorVersion = visualMajorVersion;
	}

	/**
	 * Gets the projectMajorRevision value for this WSProjectInfo.
	 * 
	 * @return projectMajorRevision
	 */
	public int getProjectMajorRevision() {
		return projectMajorRevision;
	}

	/**
	 * Sets the projectMajorRevision value for this WSProjectInfo.
	 * 
	 * @param projectMajorRevision
	 */
	public void setProjectMajorRevision(int projectMajorRevision) {
		this.projectMajorRevision = projectMajorRevision;
	}

	/**
	 * Gets the projectMinorRevision value for this WSProjectInfo.
	 * 
	 * @return projectMinorRevision
	 */
	public int getProjectMinorRevision() {
		return projectMinorRevision;
	}

	/**
	 * Sets the projectMinorRevision value for this WSProjectInfo.
	 * 
	 * @param projectMinorRevision
	 */
	public void setProjectMinorRevision(int projectMinorRevision) {
		this.projectMinorRevision = projectMinorRevision;
	}

	/**
	 * Gets the lastmodified value for this WSProjectInfo.
	 * 
	 * @return lastmodified
	 */
	public WSDate getLastmodified() {
		return lastmodified;
	}

	/**
	 * Sets the lastmodified value for this WSProjectInfo.
	 * 
	 * @param lastmodified
	 */
	public void setLastmodified(WSDate lastmodified) {
		this.lastmodified = lastmodified;
	}

	/**
	 * Gets the projectNumber value for this WSProjectInfo.
	 * 
	 * @return projectNumber
	 */
	public java.lang.String getProjectNumber() {
		return projectNumber;
	}

	/**
	 * Sets the projectNumber value for this WSProjectInfo.
	 * 
	 * @param projectNumber
	 */
	public void setProjectNumber(java.lang.String projectNumber) {
		this.projectNumber = projectNumber;
	}

	/**
	 * Gets the customerName value for this WSProjectInfo.
	 * 
	 * @return customerName
	 */
	public java.lang.String getCustomerName() {
		return customerName;
	}

	/**
	 * Sets the customerName value for this WSProjectInfo.
	 * 
	 * @param customerName
	 */
	public void setCustomerName(java.lang.String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Gets the installerName value for this WSProjectInfo.
	 * 
	 * @return installerName
	 */
	public java.lang.String getInstallerName() {
		return installerName;
	}

	/**
	 * Sets the installerName value for this WSProjectInfo.
	 * 
	 * @param installerName
	 */
	public void setInstallerName(java.lang.String installerName) {
		this.installerName = installerName;
	}
}
