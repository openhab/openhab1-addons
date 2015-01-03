package org.openhab.binding.lacrosse.test;

import java.math.BigDecimal;

import org.openhab.binding.lacrosse.connector.NumberAverage;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		NumberAverage avg = new NumberAverage(3, 2);
		BigDecimal add = avg.add(BigDecimal.valueOf(30.2f));
		
		add = avg.add(BigDecimal.valueOf(32.2f));
		add = avg.add(BigDecimal.valueOf(31.1f));
//		add.setScale(3);
		
		System.out.println("TestMain.main()" + add);
		
//		LaCrosseConnector connector = new LaCrosseConnector();
//		connector.open("COM15");
//		
//		try {
//			Thread.sleep(90000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
