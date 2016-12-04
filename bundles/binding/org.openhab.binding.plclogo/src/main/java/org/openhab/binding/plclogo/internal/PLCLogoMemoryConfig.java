package org.openhab.binding.plclogo.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLCLogoMemoryConfig 
{
		private final String location;
		private final int bit;
		private final int address;

		private static final Logger logger =
				LoggerFactory.getLogger(PLCLogoBinding.class);

		public PLCLogoMemoryConfig(String mem) throws BindingConfigParseException
		{
			String[] memparts = mem.split("\\.");
			location = memparts[0];
			int obit = -1;
			if (memparts.length > 1) {
				logger.debug("Memory map parts " + memparts[0] + " : " + memparts[1]);
				obit = Integer.parseInt(memparts[1]);
				if (obit > 7)
					throw new BindingConfigParseException("Invalid bit in " + mem + " - bit should be 0-7");
			}
			
			int[] addr = convertToAddress(location);

			bit = (addr[0] >= 0) ? addr[0] : obit;
			address = addr[1];
		}

		public String getLocation ()
		{
			return this.location;
		}

		public int getAddress ()
		{
			return this.address;
		}

		public int getBit ()
		{
			return this.bit;
		}		

		// bit location[0] and real mem loc[1]
		private int[] convertToAddress(String memloc) 
		{
			int[] retval = new int[2];
			
			retval[0] = -1;
			retval[1] = -1;
			// I , Q and M have bit values derived: I1 is equivalent to VB923.0
			// TODO Add some validation to input parameters!

			if (memloc.length() < 2)
				return null;

			String mt; // normalizaed memory type VB|VW|I|Q|M|AO|AQ|AM
			int idx;   // normalized index of element (starting from 0), for all but VB|VW
			if (Character.isDigit(memloc.charAt(1)))
			{
				mt = memloc.substring(0,1).toUpperCase();
				idx = Integer.parseInt(memloc.substring(1)) - 1;
			}
			else
			{
				mt = memloc.substring(0,2).toUpperCase();
				idx = Integer.parseInt(memloc.substring(2)) - 1;
			}

			if (mt.equals("VB") || mt.equals("VW")) {
				retval[1] = Integer.parseInt(memloc.substring(2));
			} else
			if (mt.equals("I")) {
				// I starts at 923 for three bytes
				retval[1] = 923 + idx/8;
				retval[0] = idx%8;
			} else
			if (mt.equals("Q")) {
				// Q starts at 942 for two bytes
				retval[1] = 942 + idx/8;
				retval[0] = idx%8;
			} else
			if (mt.equals("M")) {
				// Markers starts at 948 for two bytes
				retval[1] = 948 + idx/8;
				retval[0] = idx%8;
			} else
			if (mt.equals("AI")) {
				// AI starts at 926 for 8 words
				retval[1] =  926 + idx*2;
			} else
			if (mt.equals("AQ")) {
				// AQ starts at 944 for 2 words
				retval[1] =  944 + idx*2;
			} else
			if (mt.equals("AM")) {
				// AM starts at 952 for 16 words
				retval[1] =  952 + idx*2;
			}

			logger.debug("Memory map for " + memloc + " = " + retval[1] + ((retval[0] != -1)?("." + retval[0]):""));
			return retval;
		}

}
