package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.TellstickActivator;
import org.openhab.binding.tellstick.internal.TellstickBinding;

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
