/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.telegram.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides static methods that can be used in automation rules for
 * sending Telegrams.
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public class Telegram {

	private static final Logger logger = LoggerFactory
			.getLogger(Telegram.class);

	private static final String TELEGRAM_URL = "https://api.telegram.org/bot%s/sendMessage";
	private static final int HTTP_TIMEOUT = 2000;

	private static Map<String, TelegramBot> groupTokens = new HashMap<String, TelegramBot>();

	public static void addToken(String group, String chatId, String token) {
		groupTokens.put(group, new TelegramBot(chatId, token));
	}

	@ActionDoc(text = "Sends a Telegram via Telegram REST API - direct message")
	static public boolean sendTelegram(@ParamDoc(name = "group") String group,
			@ParamDoc(name = "message") String message) {

		if (groupTokens.get(group) == null) {
			logger.error("Bot '{}' not defined, action skipped", group);
			return false;
		}

		String url = String.format(TELEGRAM_URL, groupTokens.get(group)
				.getToken());

		HttpClient client = new HttpClient();

		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setSoTimeout(HTTP_TIMEOUT);
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		NameValuePair[] data = {
				new NameValuePair("chat_id", groupTokens.get(group).getChatId()),
				new NameValuePair("text", message) };
		postMethod.setRequestBody(data);

		try {
			int statusCode = client.executeMethod(postMethod);

			if (statusCode == HttpStatus.SC_NO_CONTENT
					|| statusCode == HttpStatus.SC_ACCEPTED) {
				return true;
			}

			if (statusCode != HttpStatus.SC_OK) {
				logger.warn("Method failed: {}", postMethod.getStatusLine());
				return false;
			}

			InputStream tmpResponseStream = postMethod
					.getResponseBodyAsStream();
			Header encodingHeader = postMethod
					.getResponseHeader("Content-Encoding");
			if (encodingHeader != null) {
				for (HeaderElement ehElem : encodingHeader.getElements()) {
					if (ehElem.toString().matches(".*gzip.*")) {
						tmpResponseStream = new GZIPInputStream(
								tmpResponseStream);
						logger.debug("GZipped InputStream from {}", url);
					} else if (ehElem.toString().matches(".*deflate.*")) {
						tmpResponseStream = new InflaterInputStream(
								tmpResponseStream);
						logger.debug("Deflated InputStream from {}", url);
					}
				}
			}

			String responseBody = IOUtils.toString(tmpResponseStream);
			if (!responseBody.isEmpty()) {
				logger.debug(responseBody);
			}

		} catch (HttpException e) {
			logger.error("Fatal protocol violation: {}", e.toString());
		} catch (IOException e) {
			logger.error("Fatal transport error: {}", e.toString());
		} finally {
			postMethod.releaseConnection();
		}

		return true;
	}

	@ActionDoc(text = "Sends a Telegram via Telegram REST API - build message with format and args")
	static public boolean sendTelegram(@ParamDoc(name = "group") String group,
			@ParamDoc(name = "format") String format,
			@ParamDoc(name = "args") Object... args) {

		return sendTelegram(group, String.format(format, args));
	}
}
