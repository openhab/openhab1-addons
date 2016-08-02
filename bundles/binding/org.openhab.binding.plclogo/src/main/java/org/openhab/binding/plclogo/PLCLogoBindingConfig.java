package org.openhab.binding.plclogo;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class PLCLogoBindingConfig implements BindingConfig {
  private Item item;
  private final String controller;
  private final String block;
  private final boolean invert;
  private final int threshold;
  private int lastValue;
  
  public PLCLogoBindingConfig(Item item, String controller, String block,  boolean invert, int threshold) {
    this.item = item;
    this.controller = controller;
    this.block = block.toUpperCase();
    this.threshold = threshold;
    this.invert = invert;
    this.lastValue = Integer.MIN_VALUE;    
  }
  
  public void setLastvalue(int lastValue) {  
    this.lastValue = lastValue;
  }

  public String getControllerName() {    
    return this.controller;
  }

  public String getBlock () {
    return this.block;
  }
  public int getAddress () {
    int address = -1;

		// TODO Add some validation to input parameters!
		if (block.substring(0,1).equals("I")) {
			// I starts at 1024 for 8 bytes
			address = 1024 + ((Integer.parseInt(block.substring(1, block.length())) - 1) / 8);
		} else if (block.substring(0,2).equals("AI")) {
			// AI starts at 1032 for 32 bytes --> 16 words
			address = 1032 + ((Integer.parseInt(block.substring(2, block.length())) - 1) * 2);
		} else if (block.substring(0,1).equals("Q")) {
			// Q starts at 1064 for 8 bytes
			address = 1064 + ((Integer.parseInt(block.substring(1, block.length())) - 1) / 8);
		} else if (block.substring(0,2).equals("AQ")) {
			// AQ starts at 1072 for 32 bytes --> 16 words
			address = 1072 + ((Integer.parseInt(block.substring(2, block.length())) - 1) * 2);
		} else if (block.substring(0,1).equals("M")) {
			// Markers starts at 1104 for 14 bytes
			address = 1104 + ((Integer.parseInt(block.substring(1, block.length())) - 1) / 8);
		} else if (block.substring(0,2).equals("AM")) {
			// Analog markers starts at 1118 for 128 bytes --> 64 words
			address = 1118 + ((Integer.parseInt(block.substring(2, block.length())) - 1) * 2);
		} else if (block.substring(0,2).equals("NI")) {
			// Network inputs starts at 1246 for 16 bytes
			address = 1246 + ((Integer.parseInt(block.substring(1, block.length())) - 1) / 8);
		} else if (block.substring(0,3).equals("NAI")) {
			// Network analog inputs starts at 1262 for 128 bytes --> 64 words
			address = 1262 + ((Integer.parseInt(block.substring(3, block.length())) - 1) * 2);
		} else if (block.substring(0,2).equals("NQ")) {
			// Network outputs starts at 1390 for 16 bytes
			address = 1390 + ((Integer.parseInt(block.substring(1, block.length())) - 1) / 8);
		} else if (block.substring(0,3).equals("NAQ")) {
			// Network analog inputs starts at 1406 for 64 bytes --> 32 words
			address = 1406 + ((Integer.parseInt(block.substring(3, block.length())) - 1) * 2);
		} else if (block.substring(0,2).equals("VB") || block.substring(0,2).equals("VW")) {
      int dot = block.indexOf(".", 2);
      address = Integer.parseInt(block.substring(2, dot < 0 ? block.length() : dot));
    }
    
    return address;
  }

  public int getBit () {
    int bit = -1;

		if (block.substring(0,1).equals("I")) {
			bit = (Integer.parseInt(block.substring(1, block.length())) - 1) % 8;
		} else if (block.substring(0,1).equals("Q")) {
			bit = (Integer.parseInt(block.substring(1, block.length())) - 1) % 8;
		} else if (block.substring(0,1).equals("M")) {
			bit = (Integer.parseInt(block.substring(1, block.length())) - 1) % 8;
		} else if (block.substring(0,2).equals("NI")) {
			bit = (Integer.parseInt(block.substring(1, block.length())) - 1) % 8;
		} else if (block.substring(0,2).equals("NQ")) {
			bit = (Integer.parseInt(block.substring(1, block.length())) - 1) % 8;
		} else if (block.substring(0,2).equals("VB") || block.substring(0,2).equals("VW")) {
      int dot = block.indexOf(".", 2);
      if (dot >= 0) {
        bit = Integer.parseInt(block.substring(dot + 1, block.length()));
      }
    }
    
    return bit;
  }
  
  public int getThreshold() {
    return this.threshold;
  }
  public boolean getInvert() {
    return this.invert;
  }
  public int getLastValue() {
    return this.lastValue;
  }

  public Item getItem() {
    return this.item;
  }
  
  public boolean isDigital() {
    return block.startsWith("I") || block.startsWith("Q") || block.startsWith("M") ||
           block.startsWith("NI") || block.startsWith("NQ") ||
           ((block.startsWith("VB") || block.startsWith("VW")) && (getBit() >= 0));
  }
  
  public boolean isInput() {
    return block.startsWith("I") || block.startsWith("AI") ||
           block.startsWith("NI") || block.startsWith("NAI") ||
           block.startsWith("VB") || block.startsWith("VW");
  }
}
