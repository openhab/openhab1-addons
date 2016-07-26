package org.openhab.binding.plclogo.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLCLogoMemoryConfig
{
		private String location;  // Logo-style memory location like Q10
		private String kind;      // normalizaed memory type VB|VW|I|Q|M|AO|AQ|AM
		private int address;      // address in logo memory block
		private int bit;          // informational bit (-1 if not used)

		private static final Logger logger =
				LoggerFactory.getLogger(PLCLogoBinding.class);

		public PLCLogoMemoryConfig(String mem) throws BindingConfigParseException
		{
			String[] memparts = mem.split("\\.");
			location = memparts[0];
			bit = -1;
			if (memparts.length > 1) {
				logger.debug("Memory map parts " + memparts[0] + " : " + memparts[1]);
				bit = Integer.parseInt(memparts[1]);
				if (bit > 7)
					throw new BindingConfigParseException("Invalid bit in " + mem + " - bit should be 0-7");
			}

			parseAddress(location);
		}

		public String getLocation ()
		{
			return this.location;
		}

		public String getKind ()
		{
			return this.kind;
		}

		public int getAddress ()
		{
			return this.address;
		}

		public int getBit ()
		{
			return this.bit;
		}

		public boolean isInRange()
		{
			return address > 849 && address < 942; // should be 0-1469 for BA08
		}

		// bit location[0] and real mem loc[1]
		private void parseAddress(String memloc)
		{
			// I , Q and M have bit values derived: I1 is equivalent to VB923.0
			// TODO Add some validation to input parameters!

			if (memloc.length() < 2)
				return;

			int idx;   // normalized index of element (starting from 0), for all but VB|VW
			if (Character.isDigit(memloc.charAt(1)))
			{
				kind = memloc.substring(0,1).toUpperCase();
				idx = Integer.parseInt(memloc.substring(1)) - 1;
			}
			else
			{
				kind = memloc.substring(0,2).toUpperCase();
				idx = Integer.parseInt(memloc.substring(2)) - 1;
			}

			if (kind.equals("VB") || kind.equals("VW")) {
				address = Integer.parseInt(memloc.substring(2));
			} else
			if (kind.equals("I")) {
				// I starts at 923 for three bytes
				address = 923 + idx/8;
				bit = idx%8;
			} else
			if (kind.equals("Q")) {
				// Q starts at 942 for two bytes
				address  = 942 + idx/8;
				bit = idx%8;
			} else
			if (kind.equals("M")) {
				// Markers starts at 948 for two bytes
				address = 948 + idx/8;
				bit = idx%8;
			} else
			if (kind.equals("AI")) {
				// AI starts at 926 for 8 words
				address =  926 + idx*2;
			} else
			if (kind.equals("AQ")) {
				// AQ starts at 944 for 2 words
				address =  944 + idx*2;
			} else
			if (kind.equals("AM")) {
				// AM starts at 952 for 16 words
				address =  952 + idx*2;
			}

			logger.debug("Memory map for " + memloc + " = " + address + ((bit != -1)?("." + bit):""));
			return;
/*
 * 		0BA8:
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

 */
		}

		public boolean isDigital() {
			return	kind.equals("I") || kind.equals("Q") || kind.equals("M") ||
					kind.equals("NI") || kind.equals("NQ") ||
					((kind.equals("VB") || kind.equals("VW")) && (getBit() >= 0));
		}

		public boolean isInput() {
			return 	kind.equals("I") || kind.equals("AI") ||
					kind.equals("NI") || kind.equals("NAI") ||
					kind.equals("VB") || kind.equals("VW");
		}


}
