/**
 *
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.5.0
 * 
 */

package be.devlaminck.openwebnet;

public interface IBticinoEventListener
{
	public void handleEvent(ProtocolRead p_protocol_read) throws Exception;
}
