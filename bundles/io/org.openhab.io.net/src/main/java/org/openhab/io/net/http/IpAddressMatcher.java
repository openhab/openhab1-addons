package org.openhab.io.net.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Based on: https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/util/matcher/IpAddressMatcher.java
 * License: Apache License, Version 2.0, January 2004
 */
public final class IpAddressMatcher {
	private final int nMaskBits;
	private final InetAddress requiredAddress;

	/**
	 * Takes a specific IP address or a range specified using the IP/Netmask
	 * (e.g. 192.168.1.0/24 or 202.24.0.0/14).
	 * 
	 * @param ipAddress the address or range of addresses from which the request must come.
	 */
	public IpAddressMatcher(String ipAddress) {
		if (ipAddress.indexOf('/') > 0) {
			String[] addressAndMask = ipAddress.split("/");
			ipAddress = addressAndMask[0];
			nMaskBits = Integer.parseInt(addressAndMask[1]);
		} else {
			nMaskBits = -1;
		}
		requiredAddress = parseAddress(ipAddress);
	}

	public boolean matches(String address) {
		InetAddress remoteAddress = parseAddress(address);

		if (!requiredAddress.getClass().equals(remoteAddress.getClass())) {
			return false;
		}

		if (nMaskBits < 0) {
			return remoteAddress.equals(requiredAddress);
		}

		byte[] remAddr = remoteAddress.getAddress();
		byte[] reqAddr = requiredAddress.getAddress();

		int oddBits = nMaskBits % 8;
		int nMaskBytes = nMaskBits / 8 + (oddBits == 0 ? 0 : 1);
		byte[] mask = new byte[nMaskBytes];

		Arrays.fill(mask, 0, oddBits == 0 ? mask.length : mask.length - 1, (byte)0xFF);

		if (oddBits != 0) {
			int finalByte = (1 << oddBits) - 1;
			finalByte <<= 8 - oddBits;
			mask[mask.length - 1] = (byte)finalByte;
		}

		for (int i = 0; i < mask.length; i++) {
			if ((remAddr[i] & mask[i]) != (reqAddr[i] & mask[i])) {
				return false;
			}
		}

		return true;
	}

	private InetAddress parseAddress(String address) {
		try {
			return InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("Failed to parse address" + address, e);
		}
	}
}