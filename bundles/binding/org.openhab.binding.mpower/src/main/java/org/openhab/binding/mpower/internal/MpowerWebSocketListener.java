package org.openhab.binding.mpower.internal;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;

/**
 * Ubiquiti mPower strip binding
 * 
 * Websocket listener
 * 
 * @author magcode
 */
public class MpowerWebSocketListener implements WebSocketTextListener {
	private MpowerBinding binding;
	private String address;

	private static final Logger logger = LoggerFactory
			.getLogger(MpowerWebSocketListener.class);

	public MpowerWebSocketListener(String address, MpowerBinding binding) {
		this.address = address;
		this.binding = binding;
	}

	@Override
	public void onOpen(WebSocket webSocket) {
		logger.debug("[{}]: Websocket opened", "");
	}

	@Override
	public void onError(Throwable e) {
		if (e instanceof ConnectException) {

			logger.debug("[{}]: Websocket connection error", "");
		} else if (e instanceof TimeoutException) {
			logger.debug("[{}]: Websocket timeout error", "");
		} else {
			logger.debug(e.getStackTrace().toString(), "");
			logger.error("[{}]: Websocket error: {}", "", e.getMessage());
		}
	}

	@Override
	public void onClose(WebSocket webSocket) {
		logger.debug("[{}]: Websocket closed", "");
		webSocket = null;

	}

	@Override
	public void onMessage(String message) {
		MpowerSocketState state = new MpowerSocketState(message, this.address);
		binding.receivedData(state);
	}

	@Override
	public void onFragment(String fragment, boolean last) {
	}
}