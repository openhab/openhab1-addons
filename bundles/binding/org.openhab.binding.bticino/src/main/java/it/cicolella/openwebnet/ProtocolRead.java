package it.cicolella.openwebnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class ProtocolRead
{
	private String m_message = "";
	
	private static final Logger logger = LoggerFactory
			.getLogger(ProtocolRead.class);

	private Map<String, String> m_properties = new HashMap<String, String>();

	public ProtocolRead(String p_message)
	{
		m_message = p_message;
		logger.info("Instance created for message [" + p_message + "]");
	}

	public void addProperty(String p_key, String p_value)
	{
		// TODO Auto-generated method stub
		logger.info("addProperty Key : " + p_key + ", Value : " + p_value);
		m_properties.put(p_key, p_value);

	}

	public String getProperty(String p_key)
	{
		return (m_properties.get(p_key));
	}
	
	@Override
	public String toString()
	{
		return ("ProtocolRead, Message[" + m_message + "]");
	}

}
