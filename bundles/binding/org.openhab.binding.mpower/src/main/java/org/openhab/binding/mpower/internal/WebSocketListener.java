package org.openhab.binding.mpower.internal;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;

public class WebSocketListener implements WebSocketTextListener {
	private mPowerBinding binding;
	private String address;

	private static final Logger logger = LoggerFactory
			.getLogger(WebSocketListener.class);

	public WebSocketListener(String address, mPowerBinding binding) {
		this.address = address;
		this.binding = binding;
	}

	@Override
	public void onOpen(WebSocket webSocket) {
		webSocket.sendTextMessage("{\"time\":4000}");
		logger.debug("[{}]: Websocket opened", "");

	}

	@Override
	public void onError(Throwable e) {
		if (e instanceof ConnectException) {
			logger.debug("[{}]: Websocket connection error", "");
		} else if (e instanceof TimeoutException) {
			logger.debug("[{}]: Websocket timeout error", "");
		} else {
			logger.error("[{}]: Websocket error: {}", "", e.getMessage());
		}
	}

	@Override
	public void onClose(WebSocket webSocket) {
		logger.warn("[{}]: Websocket closed", "");
		webSocket = null;

	}

	@Override
	public void onMessage(String message) {
		SocketState state = new SocketState(message,this.address);
		binding.receivedData(state);
	}

	@Override
	public void onFragment(String fragment, boolean last) {
	}
}