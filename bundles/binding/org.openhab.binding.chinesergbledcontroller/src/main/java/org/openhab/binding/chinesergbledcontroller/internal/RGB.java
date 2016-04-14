package org.openhab.binding.chinesergbledcontroller.internal;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RGB {
	private static final Logger logger = 
			LoggerFactory.getLogger(ChineseRGBLedControllerBinding.class);

	int red;
	int green;
	int blue;
	RGB() {
		red=255;
		green=255;
		blue=255;
	}
	RGB(int r,int g,int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	public void HSLtoRGB(float hue, float saturation, float brightness){		
		logger.debug("HSLtoRGB(hue:{},sat:{},bri:{}",hue,saturation,brightness);
		Color color = Color.getHSBColor(hue/360, saturation/100, brightness/100);

		this.red=color.getRed();
		this.green=color.getGreen();
		this.blue=color.getBlue();
		logger.debug("R:{},G:{},B:{}",red,green,blue);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	
	@Override
	public String toString() {
		return "RGB [red=" + red + ", green=" + green + ", blue=" + blue + "]";
	}
}
