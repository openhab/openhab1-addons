/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol;

import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Helper class for encrypting ETHM-1 messages.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class EncryptionHelper {
	private Key key;
	private Cipher encipher;
	private Cipher decipher;

	/**
	 * Creates new instance of encryption helper with given key.
	 * 
	 * @param keyString
	 *            key for integration encryption
	 * @throws GeneralSecurityException on JCE errors
	 */
	public EncryptionHelper(String keyString) throws GeneralSecurityException {
		// we have to check if 192bit support is enabled
		if (Cipher.getMaxAllowedKeyLength("AES") < 192) {
			throw new GeneralSecurityException("JCE does not support 192-bit keys");
		}

		// build encryption/decryption key based on given password
		byte passwordBytes[] = keyString.getBytes();
		byte[] keyBytes = new byte[24];

		for (int i = 0; i < 12; ++i)
			keyBytes[i] = keyBytes[i + 12] = (i < passwordBytes.length) ? passwordBytes[i] : 0x20;

		// create objects for encryption/decryption
		this.key = new SecretKeySpec(keyBytes, "AES");
		this.encipher = Cipher.getInstance("AES/ECB/NoPadding");
		this.encipher.init(Cipher.ENCRYPT_MODE, this.key);
		this.decipher = Cipher.getInstance("AES/ECB/NoPadding");
		this.decipher.init(Cipher.DECRYPT_MODE, this.key);
	}

	/**
	 * Decrypts given buffer of bytes in place.
	 * 
	 * @param buffer
	 *            bytes to decrypt
	 * @throws GeneralSecurityException
	 *             on decryption errors
	 */
	public void decrypt(byte buffer[]) throws GeneralSecurityException {
		byte[] CV = new byte[16];
		byte[] C = new byte[16];
		byte[] TEMP = new byte[16];
		int count = buffer.length;

		CV = this.encipher.doFinal(CV);
		for (int index = 0; count > 0;) {
			if (count > 15) {
				count -= 16;
				System.arraycopy(buffer, index, TEMP, 0, 16);
				System.arraycopy(buffer, index, C, 0, 16);
				C = this.decipher.doFinal(C);
				for (int i = 0; i < 16; ++i) {
					C[i] ^= CV[i];
					CV[i] = TEMP[i];
				}
				System.arraycopy(C, 0, buffer, index, 16);
				index += 16;
			} else {
				System.arraycopy(buffer, index, C, 0, count);
				CV = this.encipher.doFinal(CV);
				for (int i = 0; i < 16; ++i) {
					C[i] ^= CV[i];
				}
				System.arraycopy(C, 0, buffer, index, count);
				count = 0;
			}
		}
	}

	/**
	 * Encrypts given buffer of bytes in place.
	 * 
	 * @param buffer
	 *            bytes to encrypt
	 * @throws GeneralSecurityException
	 *             on encryption errors
	 */
	public void encrypt(byte buffer[]) throws GeneralSecurityException {
		byte[] CV = new byte[16];
		byte[] P = new byte[16];
		int count = buffer.length;

		CV = this.encipher.doFinal(CV);
		for (int index = 0; count > 0;) {
			if (count > 15) {
				count -= 16;
				System.arraycopy(buffer, index, P, 0, 16);
				for (int i = 0; i < 16; ++i) {
					P[i] ^= CV[i];
				}
				P = this.encipher.doFinal(P);
				System.arraycopy(P, 0, CV, 0, 16);
				System.arraycopy(P, 0, buffer, index, 16);
				index += 16;
			} else {
				System.arraycopy(buffer, index, P, 0, count);
				CV = this.encipher.doFinal(CV);
				for (int i = 0; i < 16; ++i) {
					P[i] ^= CV[i];
				}
				System.arraycopy(P, 0, buffer, index, count);
				count = 0;
			}
		}
	}
}
