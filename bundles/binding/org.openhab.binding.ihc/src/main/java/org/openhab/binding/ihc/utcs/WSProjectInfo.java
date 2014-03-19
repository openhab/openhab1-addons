/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
