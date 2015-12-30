/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.internal.tts;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Dictionary;
import java.util.List;
import java.util.Vector;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import org.openhab.io.multimedia.tts.TTSService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A TTS service implementation utilizing the text-to-speech functionality of Google Translate.
 * 
 * @author Dominic Lerbs
 * @since 1.7.0
 * 
 */
public class TTSServiceGoogleTTS implements TTSService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(TTSServiceGoogleTTS.class);

	/** User agent for HTTP requests as requests without agent are ignored */
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) "
			+ "Gecko/20100101 Firefox/34.0";
	/** Maximum sentence length. Google translate only supports 100 characters at a time. */
	private static final int MAX_SENTENCE_LENGTH = 100;

	private static final String SENTENCE_DELIMITERS_PROPERTY = "sentenceDelimiters";
	private static final String LANGUAGE_PROPERTY = "language";
	private static final String TRANSLATE_URL_PROPERTY = "translateUrl";

	private String ttsLanguage = "en";
	private String translateUrl = "http://translate.google.com/translate_tts?tl=%s&q=%s&client=t";

	private final GoogleTTSTextProcessor textProcessor = new GoogleTTSTextProcessor(MAX_SENTENCE_LENGTH);

	public void activate() {
		logger.debug("GoogleTTS service has been activated");
	}

	public void deactivate() {
		logger.debug("GoogleTTS service has been deactivated");
	}

	/**
	 * {@inheritDoc}
	 */
	public void say(String text, String voiceName, String outputDevice) {
		logger.info("Executing GoogleTTS for text '{}' in language {}", text, ttsLanguage);
		BufferedInputStream stream = null;

		try {
			List<String> sentences = textProcessor.splitIntoChunks(text);
			InputStream completeStream = getSpeechForText(sentences);
			Player playMP3 = new Player(completeStream);
			playMP3.play();
		} catch (IOException e) {
			logger.warn("Error while connecting to Google translate service", e);
			if (e instanceof FileNotFoundException) {
				logger.warn("Possibly unsupported language '{}'?", ttsLanguage);
			}
		} catch (JavaLayerException e) {
			logger.warn("Unable to play InputStream for text " + text, e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	/**
	 * Converts the given sentences into audio and returns it as an {@link InputStream}.
	 * 
	 * @param sentences
	 *            The text to be converted to audio
	 * @return {@link InputStream} with audio output
	 * @throws IOException
	 *             Exception if the connection could not be established properly
	 */
	private InputStream getSpeechForText(List<String> sentences) throws IOException {
		Vector<InputStream> inputStreams = new Vector<InputStream>(sentences.size());
		for (String sentence : sentences) {
			String encodedSentence = GoogleTTSTextProcessor.urlEncodeSentence(sentence);
			URL url = new URL(String.format(translateUrl, ttsLanguage, encodedSentence));
			inputStreams.add(getInputStreamFromUrl(url));
		}
		return new SequenceInputStream(inputStreams.elements());
	}

	/**
	 * Connects to the given {@link URL} and returns the response as an {@link InputStream}.
	 * 
	 * @param url
	 *            The {@link URL} to connect to
	 * @return The response as {@link InputStream}
	 * @throws IOException
	 *             Exception if the connection could not be established properly
	 */
	private InputStream getInputStreamFromUrl(URL url) throws IOException {
		logger.debug("Connecting to URL " + url.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.addRequestProperty("User-Agent", USER_AGENT);
		connection.connect();
		return new BufferedInputStream(connection.getInputStream());
	}

	/**
	 * {@inheritDoc}
	 */
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		if (properties != null) {
			String language = (String) properties.get(LANGUAGE_PROPERTY);
			if (!StringUtils.isBlank(language)) {
				logger.debug("Using TTS language from config: " + ttsLanguage);
				ttsLanguage = language;
			}

			String delimiters = (String) properties.get(SENTENCE_DELIMITERS_PROPERTY);
			if (!StringUtils.isBlank(delimiters)) {
				logger.debug("Using custom sentence delimiters from config: " + delimiters);
				textProcessor.setCustomSentenceDelimiters(delimiters);
			}

			String configTranslateUrl = (String) properties.get(TRANSLATE_URL_PROPERTY);
			if (!StringUtils.isBlank(configTranslateUrl)) {
				logger.debug("Using custom translate URL from config: " + configTranslateUrl);
				translateUrl = configTranslateUrl;
			}
		}
	}
}
