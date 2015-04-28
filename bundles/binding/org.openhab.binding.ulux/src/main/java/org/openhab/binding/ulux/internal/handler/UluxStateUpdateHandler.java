package org.openhab.binding.ulux.internal.handler;

import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import java.util.LinkedList;
import java.util.List;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.UluxConfiguration;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.UluxDatagramFactory;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.UluxMessageFactory;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

public class UluxStateUpdateHandler {

	private final UluxDatagramFactory datagramFactory;

	@SuppressWarnings("unused")
	private final UluxConfiguration configuration;

	private final UluxMessageFactory messageFactory = new UluxMessageFactory();

	public UluxStateUpdateHandler(UluxConfiguration configuration, UluxDatagramFactory datagramFactory) {
		this.configuration = configuration;
		this.datagramFactory = datagramFactory;
	}

	/**
	 * Creates a list of datagrams for the given state update.
	 * 
	 * @return never {@code null}
	 */
	public List<UluxDatagram> handleUpdate(UluxBindingConfig config, State type) {
		final List<UluxDatagram> datagramList = new LinkedList<UluxDatagram>();
		final UluxMessage message;

		switch (config.getType()) {
		case EDIT_VALUE:
			if (type instanceof DecimalType) {
				message = messageFactory.createEditValueMessage(config, (DecimalType) type);
			} else if (type instanceof OnOffType) {
				message = messageFactory.createEditValueMessage(config, (OnOffType) type);
			} else if (type instanceof StringType) {
				message = messageFactory.createTextMessage(config, (StringType) type);
			} else {
				// TODO OpenClosedType
				// TODO UpDownType
				LOG.debug("Outgoing message '{}' for item '{}' not yet supported!", type, config);
				message = null;
			}
			break;
		default:
			message = null; // ignore
			break;
		}

		if (message != null) {
			final UluxMessageDatagram datagram = datagramFactory.createMessageDatagram(config);
			datagram.addMessage(message);

			datagramList.add(datagram);
		}

		return datagramList;
	}

}
