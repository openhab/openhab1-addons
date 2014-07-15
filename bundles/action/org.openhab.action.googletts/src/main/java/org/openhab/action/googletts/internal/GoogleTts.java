/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.googletts.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.BreakIterator;
import java.util.Locale;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains the methods that are made available in scripts and rules
 * for GoogleTTS.
 * 
 * @author goldman.vlad
 * @since 1.4.0
 */
public class GoogleTts {

	public static final String GOOGLETTS_CACHE_PATH = "etc/googletts/";
	public static final String GOOGLETTS_URL_TEMPLATE = "http://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=%s";

	private static final Logger logger = LoggerFactory
			.getLogger(GoogleTts.class);
	public static String defaultLang;
	public static boolean isCacheEnabled = true;

	@ActionDoc(text = "Google Text To Speach", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static synchronized boolean voice(
			@ParamDoc(name = "text", text = "Text to be said") String text,
			@ParamDoc(name = "lang", text = "language") String lang) {
		logger.debug("GoogleTTS action called with text:" + text);
		if (!GoogleTtsActionService.isProperlyConfigured) {
			logger.error("GoogleTTS action is not yet configured - execution aborted!");
			return false;
		}

		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		String sentenceText = "";
		String sentenceEncoded = "";
		iterator.setText(text);
		int start = iterator.first();
		// Brake input text into sentences and process one by one
		for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
				.next()) {
			sentenceText = text.substring(start, end);
			logger.debug("GoogleTTS encoding hash for: " + sentenceText);
			String hashFilename = GoogleTtsActionService.MD5(sentenceText);
			logger.debug("GoogleTTS hash filename: " + hashFilename);
			if (isCacheEnabled
					&& GoogleTtsActionService.textAudioFiles
							.contains(hashFilename)) {

				logger.debug("GoogleTTS text found in cache and will be played from file: "
						+ hashFilename);
				playTextAudioFromCache(hashFilename);
				logger.debug("GoogleTTS Finished playing segment");

			} else {
				logger.debug("GoogleTTS cacheEnabled " + isCacheEnabled
						+ " or cached voice not found.");
				try {
					sentenceEncoded = URLEncoder.encode(sentenceText, "UTF-8");
					logger.debug("GoogleTTS encoded sentence:"
							+ sentenceEncoded);
				} catch (UnsupportedEncodingException e) {
					logger.error(
							"GoogleTTS could not encode" + sentenceEncoded, e);
					return false;
				}
				String urlString = String.format(GOOGLETTS_URL_TEMPLATE,
						sentenceEncoded, lang);
				downloadAndPlay(urlString, hashFilename);
			}
		}

		logger.debug("GoogleTTS voice completed");
		return true;
	}

	public static synchronized boolean voice(
			@ParamDoc(name = "text", text = "the something to do") String text) {
		if (!GoogleTtsActionService.isProperlyConfigured) {
			logger.debug("GoogleTTS action is not yet configured - execution aborted!");
			return false;
		}

		return voice(text, GoogleTts.defaultLang);

	}

	private static void playTextAudioFromCache(String hashFilename) {
		File textAudioFile = new File(GOOGLETTS_CACHE_PATH + hashFilename);
		if (!textAudioFile.exists()) {
			logger.error("Cached file not found on disk. Removing from record");
			return;
		}
		logger.debug("GoogleTTS found file on disk: " + hashFilename);
		BufferedInputStream in = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(textAudioFile);
			in = new BufferedInputStream(fis);
			logger.debug("GoogleTTS playing");
			final Player mp3player = new Player(in);
			mp3player.play();
		} catch (JavaLayerException e) {
			logger.error(
					"Could not play the file. See exception trace for details",
					e);
		} catch (IOException e) {
			logger.error(
					"Could not play the file. See exception trace for details",
					e);
		} finally {
			if (in != null) {
				try { // outmost stream propogates close
					in.close();
				} catch (IOException ex) {
					// ignore quietly
				}
			}
		}
	}

	private static void downloadAndPlay(String urlString, String hashFilename) {
		BufferedInputStream in = null;
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			in = new BufferedInputStream(conn.getInputStream());
			if (isCacheEnabled) {
				logger.debug("GoogleTTS saving audio to file.");
				saveVoiceAudioToFile(in, hashFilename);
				GoogleTtsActionService.textAudioFiles.add(hashFilename);
				logger.debug("GoogleTTS playing audio from file.");
				playTextAudioFromCache(hashFilename);
			} else {
				logger.debug("GoogleTTS cache disabled, playing audio from server.");
				Player mp3player = new Player(in);
				mp3player.play();
			}
		} catch (JavaLayerException e) {
			logger.error(
					"Could not play the file. See exception trace for details",
					e);
		} catch (IOException e) {
			logger.error(
					"Could not play the file. See exception trace for details",
					e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					// ignore quietly
				}
			}
		}
	}

	private static void saveVoiceAudioToFile(BufferedInputStream in,
			String hashFilename) throws IOException {
		OutputStream outputStream = new FileOutputStream(GOOGLETTS_CACHE_PATH
				+ hashFilename);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				outputStream, 1024);
		byte data[] = new byte[1024];
		while (in.read(data, 0, 1024) >= 0) {
			bufferedOutputStream.write(data);
		}
		bufferedOutputStream.close();
		in.close();

	}

}
