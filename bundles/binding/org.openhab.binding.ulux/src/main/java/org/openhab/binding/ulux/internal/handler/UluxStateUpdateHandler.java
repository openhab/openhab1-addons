package org.openhab.binding.ulux.internal.handler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UluxStateUpdateHandler extends AbstractEventHandler<State> {

	private static final Logger LOG = LoggerFactory.getLogger(UluxStateUpdateHandler.class);

	public UluxStateUpdateHandler(UluxConfiguration configuration, UluxMessageFactory messageFactory,
			UluxDatagramFactory datagramFactory) {
		super(configuration, messageFactory, datagramFactory);
	}

	/**
	 * Creates a list of datagrams for the given state update.
	 * 
	 * @return never {@code null}
	 */
	public Queue<UluxDatagram> handleEvent(UluxBindingConfig config, State type) {
		final Queue<UluxDatagram> datagramList = new ConcurrentLinkedQueue<UluxDatagram>();
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
