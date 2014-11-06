package org.openhab.binding.intertechno.internal.parser;

import java.nio.ByteBuffer;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.library.types.OnOffType;

public class IntertechnoReceiveParser {
	private String address;
	private OnOffType command;
	
	public IntertechnoReceiveParser(String data)
	{
		String complete = formatReceiveData(data);
		if (StringUtils.isNotBlank(complete))
		{
			setAddress(StringUtils.left(complete, 10));
			String cmd = StringUtils.right(complete, 2);
			if (cmd.equals("FF")){setCommand(OnOffType.ON);}
			if (cmd.equals("F0")){setCommand(OnOffType.OFF);}
		}
	}
	private String formatReceiveData(String data)
	{
		data = StringUtils.removeStart(data, "IR");
		data = StringUtils.substringBefore(data, "_");
		int i = Integer.parseInt(data);
		byte[] bytes = ByteBuffer.allocate(7).putInt(i).array();
		String bits = toBit(bytes);
		String tri = bin2tristate(bits);
		return tri;		
	}
	
	private String toBit(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a) {
			String s = Integer.toBinaryString(0x100 + b);
			sb.append(s.subSequence(s.length() - 8, s.length()));
		}

		return sb.toString().substring(8, 32);
	}

	private String bin2tristate(String in)
	{
		int i=0;
		int pos = 0;
		StringBuilder sb = new StringBuilder();
		while (i < in.length()/2)
		{
			String first = in.substring(pos,pos+1);
			String second = in.substring(pos+1,pos+2);
			
			if (first.equals("0") &&  second.equals("0")) {
				sb.append("0");
			} else if (first.equals("1") &&  second.equals("1")) {
				sb.append("1");
			} else if (first.equals("0") &&  second.equals("1")) {
				sb.append("F");
			}
			pos++;
			pos++;
			i++;
		}
		return sb.toString();
		
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public OnOffType getCommand() {
		return command;
	}
	public void setCommand(OnOffType command) {
		this.command = command;
	}
}
