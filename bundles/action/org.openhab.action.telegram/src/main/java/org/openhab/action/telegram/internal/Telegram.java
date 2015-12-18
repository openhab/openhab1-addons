/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.telegram.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
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
	private static final String TELEGRAM_PHOTO_URL = "https://api.telegram.org/bot%s/sendPhoto";
	private static final int HTTP_TIMEOUT = 2000;
	private static final int HTTP_PHOTO_TIMEOUT = 10000;

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
		postMethod.getParams().setContentCharset("UTF-8");
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

	@ActionDoc(text = "Sends a Picture via Telegram REST API")
	static public boolean sendTelegramPhoto(
			@ParamDoc(name = "group") String group,
			@ParamDoc(name = "photoURL") String photoURL,
			@ParamDoc(name = "caption") String caption) {

		if (groupTokens.get(group) == null) {
			logger.error("Bot '{}' not defined, action skipped", group);
			return false;
		}

		if (photoURL == null) {
			logger.error("photoURL not defined, action skipped");
			return false;
		}

		// load image from url
		byte[] imageFromURL;

		HttpClient getClient = new HttpClient();
		GetMethod getMethod = new GetMethod(photoURL);
		getMethod.getParams().setSoTimeout(HTTP_PHOTO_TIMEOUT);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		try {
			int statusCode = getClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: {}", getMethod.getStatusLine());
				return false;
			}

			imageFromURL = getMethod.getResponseBody();
		} catch (HttpException e) {
			logger.error("Fatal protocol violation: {}", e.toString());
			return false;
		} catch (IOException e) {
			logger.error("Fatal transport error: {}", e.toString());
			return false;
		} finally {
			getMethod.releaseConnection();
		}

		// parse image type
		String imageType;
		try {
			ImageInputStream iis = ImageIO
					.createImageInputStream(new ByteArrayInputStream(
							imageFromURL));
			Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
			if (!imageReaders.hasNext()) {
				logger.error("photoURL does not represent a known image type");
				return false;
			}
			ImageReader reader = (ImageReader) imageReaders.next();
			imageType = reader.getFormatName();
		} catch (IOException e) {
			logger.error("cannot parse photoURL as image: {}", e.getMessage());
			return false;
		}

		// post photo to telegram
		String url = String.format(TELEGRAM_PHOTO_URL, groupTokens.get(group)
				.getToken());

		PostMethod postMethod = new PostMethod(url);
		try {
			postMethod.getParams().setContentCharset("UTF-8");
			postMethod.getParams().setSoTimeout(HTTP_PHOTO_TIMEOUT);
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(3, false));
			Part[] parts = new Part[caption != null ? 3 : 2];
			parts[0] = new StringPart("chat_id", groupTokens.get(group)
					.getChatId());
			parts[1] = new FilePart("photo", new ByteArrayPartSource(
					String.format("image.%s", imageType), imageFromURL));
			if (caption != null) {
				parts[2] = new StringPart("caption", caption);
			}
			postMethod.setRequestEntity(new MultipartRequestEntity(parts,
					postMethod.getParams()));

			HttpClient client = new HttpClient();
			int statusCode = client.executeMethod(postMethod);

			if (statusCode == HttpStatus.SC_NO_CONTENT
					|| statusCode == HttpStatus.SC_ACCEPTED) {
				return true;
			}

			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: {}", postMethod.getStatusLine());
				return false;
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
}
