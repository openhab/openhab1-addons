/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal;

/**
 * Configuration class for binding configuration settings.
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CalDavConfig {
	private String key;
	private String username;
	private String password;
	private String url;
	private int reloadMinutes = 60;
	private int preloadMinutes = 60 * 24;
	private int historicLoadMinutes = 0;
	private boolean disableCertificateVerification;
	private boolean lastModifiedFileTimeStampValid = true;

	public CalDavConfig() {
	}

	public CalDavConfig(String key, String username, String password,
			String url, int reloadMinutes, int historicLoadMinutes) {
		this.key = key;
		this.username = username;
		this.password = password;
		this.url = url;
		this.reloadMinutes = reloadMinutes;
		this.historicLoadMinutes = historicLoadMinutes;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getReloadMinutes() {
		return reloadMinutes;
	}

	public void setReloadMinutes(int reloadMinutes) {
		this.reloadMinutes = reloadMinutes;
	}

	public int getPreloadMinutes() {
		return preloadMinutes;
	}

	public void setPreloadMinutes(int preloadMinutes) {
		this.preloadMinutes = preloadMinutes;
	}

	public boolean isDisableCertificateVerification() {
		return disableCertificateVerification;
	}

	public void setDisableCertificateVerification(
			boolean disableCertificateVerification) {
		this.disableCertificateVerification = disableCertificateVerification;
	}

	public int getHistoricLoadMinutes() {
		return historicLoadMinutes;
	}

	public void setHistoricLoadMinutes(int historicLoadMinutes) {
		this.historicLoadMinutes = historicLoadMinutes;
	}

	public boolean isLastModifiedFileTimeStampValid() {
		return lastModifiedFileTimeStampValid;
	}

	public void setLastModifiedFileTimeStampValid(
			boolean lastModifiedFileTimeStampValid) {
		this.lastModifiedFileTimeStampValid = lastModifiedFileTimeStampValid;
	}

	@Override
	public String toString() {
		return "CalDavConfig [key=" + key + ", username=" + username
				+ ", password=" + password + ", url=" + url
				+ ", reloadMinutes=" + reloadMinutes + ", preloadMinutes="
				+ preloadMinutes + ", disableCertificateVerification="
				+ disableCertificateVerification
				+ ", lastModifiedFileTimeStampValid="
				+ lastModifiedFileTimeStampValid + "]";
	}

}
