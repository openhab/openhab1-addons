/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.TellstickActivator;
import org.openhab.binding.tellstick.internal.TellstickBinding;
/**
 * Just used to test JNA.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class TestJNA {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new TellstickActivator().start(null);
		new TellstickBinding().activate();
		// Do not exit!
		// Do not exit!
        while (true) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
	}

}
