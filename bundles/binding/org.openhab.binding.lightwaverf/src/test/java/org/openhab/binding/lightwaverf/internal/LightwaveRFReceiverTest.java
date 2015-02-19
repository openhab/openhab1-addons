package org.openhab.binding.lightwaverf.internal;

import org.junit.Ignore;
import org.junit.Test;

public class LightwaveRFReceiverTest {

	@Test
	@Ignore
	public void test() throws Exception {
		LightwaveRFReceiver receiver9760 = new LightwaveRFReceiver(new LightwaverfConvertor(), 9760);
		LightwaveRFReceiver receiver9761 = new LightwaveRFReceiver(new LightwaverfConvertor(), 9761);
		receiver9760.start();
		receiver9761.start();
		Thread.sleep(5 * 1000 * 60);	}

}
