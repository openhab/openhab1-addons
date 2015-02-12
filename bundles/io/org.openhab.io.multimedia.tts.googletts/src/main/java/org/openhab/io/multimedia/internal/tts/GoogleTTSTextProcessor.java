/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.internal.tts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prepare text to be suitable for calling the Google translate service.
 * 
 * @author Dominic Lerbs
 * @since 1.7.0
 * 
 */
public class GoogleTTSTextProcessor {

	private static final Logger logger = LoggerFactory.getLogger(GoogleTTSTextProcessor.class);
	private final int maxSentenceLength;
	private String sentenceDelimiters = "!.?:;";

	public GoogleTTSTextProcessor(int maxSentenceLength) {
		this.maxSentenceLength = maxSentenceLength;
	}

	public void setCustomSentenceDelimiters(String delimiters) {
		sentenceDelimiters = delimiters;
	}

	/**
	 * Splits the given text into small chunks which are processible by the Google translate service. Guarantees that a
	 * single item in the result list is not longer than {@link #maxSentenceLength}.
	 * 
	 * @param text
	 *            The text to split into chunks.
	 * @return List containing the text chunks
	 */
	public List<String> splitIntoChunks(String text) {
		List<String> splitChunks = new ArrayList<String>();

		Iterator<String> sentenceIterator = Arrays.asList(text.split("[" + sentenceDelimiters + "]")).iterator();
		while (sentenceIterator.hasNext()) {
			String nextSentence = sentenceIterator.next().trim();
			splitChunks.addAll(splitSentence(nextSentence));
		}
		return splitChunks;
	}

	/**
	 * Splits a sentence into multiple chunks if the sentence exceeds the {@link #maxSentenceLength}.
	 * 
	 * @param sentence
	 *            The sentence to split
	 * @return A list containing the split chunks of the sentence
	 */
	private List<String> splitSentence(String sentence) {
		List<String> parts = new ArrayList<String>();

		StringBuilder sentencePart = new StringBuilder();

		Iterator<String> wordIterator = Arrays.asList(StringUtils.split(sentence, ' ')).iterator();
		while (wordIterator.hasNext()) {
			String nextWord = wordIterator.next().trim();
			if (wordLengthWithinLimits(nextWord)) {
				if (sentencePart.length() + nextWord.length() <= maxSentenceLength) {
					sentencePart.append(nextWord).append(' ');
				} else {
					parts.add(sentencePart.toString().trim());
					sentencePart = new StringBuilder(nextWord).append(' ');
				}
			}
		}
		if (sentencePart.length() > 0) {
			parts.add(sentencePart.toString().trim());
		}
		return parts;
	}

	private boolean wordLengthWithinLimits(String word) {
		if (word.isEmpty()) {
			return false;
		} else if (word.length() > maxSentenceLength) {
			logger.warn("Unable to say '{}' as this word is longer than the maximum sentence allowed ({})", word,
					maxSentenceLength);
			return false;
		}
		return true;
	}

	/**
	 * Encodes the given sentence into URL compatible format.
	 * 
	 * @param sentence
	 *            The sentence to convert
	 * @return The sentence in URL compatible format
	 */
	public static String urlEncodeSentence(String sentence) {
		String encodedSentence = "";
		try {
			logger.trace("Encoding sentence to URL format: {}", sentence);
			encodedSentence = URLEncoder.encode(sentence, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("Failed to encode sentence '" + sentence + "'", e);
		}
		encodedSentence = encodedSentence.replace("+", "%20");
		logger.debug("Encoded sentence: " + encodedSentence);
		return encodedSentence;
	}

}
