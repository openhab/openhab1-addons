/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.urtsi.internal;

/**
 * The ArrayHelper is needed due to a limitation in Xtend:
 * https://groups.google.com/forum/#!msg/xtend-lang/p33jDUNh0Hg/z4u7B7BHhJgJ
 * 
 * After a Xtend update to >= 2.4.0 it can be replaced by using ArrayLiterals: 
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=377904)
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
public class ArrayHelper {

	/**
	 * The method returns a byte[] of the given size. 
	 * @param size of the resulting byte[]
	 * @return
	 */
	public static byte[] getByteArray(int size) {
		return new byte[size];
	}
}
