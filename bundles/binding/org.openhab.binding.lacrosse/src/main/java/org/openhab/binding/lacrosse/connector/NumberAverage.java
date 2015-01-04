package org.openhab.binding.lacrosse.connector;

import java.math.BigDecimal;

public class NumberAverage {

	private int size;
	private int pos;
	private BigDecimal[] values;
	private int scale;
	
	public NumberAverage(int size, int scale) {
		this.size = size;
		this.scale = scale;
		this.pos = 0;
		values = new BigDecimal[size];
	}
	
	public BigDecimal add(BigDecimal n) {
		
		// rolling
		if(pos == this.size) {
			pos = 0;
		}

		values[pos++] = n;
		return get();
	}
	
	public BigDecimal get() {
		BigDecimal b = new BigDecimal(0);
		int elems = 0;
		for (BigDecimal val : values) {
			if(val != null) {
				elems++;
				b = b.add(val);
			}
		}
		
		return b.divide(BigDecimal.valueOf(elems), scale, BigDecimal.ROUND_HALF_UP);
	}
	
}
