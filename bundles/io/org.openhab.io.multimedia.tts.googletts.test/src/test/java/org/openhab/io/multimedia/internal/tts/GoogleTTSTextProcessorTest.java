/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.internal.tts;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openhab.io.multimedia.internal.tts.GoogleTTSTextProcessor;

/**
 * Test class for {@link GoogleTTSTextProcessor} of the Google TTS bundle.
 * 
 * @author Dominic Lerbs
 * @since 1.7.0
 * 
 */
public class GoogleTTSTextProcessorTest {

	private GoogleTTSTextProcessor textProcessor;
	private static final int MAX_SENTENCE_LENGTH = 100;

	@Before
	public void init() {
		textProcessor = new GoogleTTSTextProcessor(MAX_SENTENCE_LENGTH);
	}

	@Test
	public void testSplitIntoChunks() {
		String emptySentence = "";
		List<String> splitSentences = textProcessor.splitIntoChunks(emptySentence);
		Assert.assertTrue(splitSentences.isEmpty());

		String oneWord = "One";
		splitSentences = textProcessor.splitIntoChunks(oneWord);
		Assert.assertEquals(1, splitSentences.size());
		Assert.assertEquals(oneWord, splitSentences.get(0));

		String oneSentence = "This is a normal sentence";
		splitSentences = textProcessor.splitIntoChunks(oneSentence);
		Assert.assertEquals(1, splitSentences.size());
		Assert.assertEquals(oneSentence, splitSentences.get(0));

		splitSentences = textProcessor.splitIntoChunks(oneSentence + ".");
		Assert.assertEquals(1, splitSentences.size());
		Assert.assertEquals(oneSentence, splitSentences.get(0));

		String threeSentences = "This is the first Sentence. But there are more! In fact, there are three...";
		splitSentences = textProcessor.splitIntoChunks(threeSentences);
		Assert.assertEquals(3, splitSentences.size());
		Assert.assertEquals("This is the first Sentence", splitSentences.get(0));
		Assert.assertEquals("But there are more", splitSentences.get(1));
		Assert.assertEquals("In fact, there are three", splitSentences.get(2));

		String longSentence = "The method should split sentences which are longer than a given limit, "
				+ "as the Google Translation Service can only handle up to 100 characters at a time";
		Assert.assertTrue(longSentence.length() > MAX_SENTENCE_LENGTH
				&& longSentence.length() < MAX_SENTENCE_LENGTH * 2);
		int splitIndex = longSentence.substring(0, MAX_SENTENCE_LENGTH).lastIndexOf(" ");
		splitSentences = textProcessor.splitIntoChunks(longSentence);
		Assert.assertEquals(2, splitSentences.size());
		Assert.assertEquals(longSentence.substring(0, splitIndex), splitSentences.get(0));
		Assert.assertEquals(longSentence.substring(splitIndex + 1), splitSentences.get(1));
	}

	@Test
	public void testCustomDelimiters() {
		String customDelimiters = "。！";
		textProcessor.setCustomSentenceDelimiters(customDelimiters);
		String sentence = "七転八起。一期一会！";
		List<String> splitSentences = textProcessor.splitIntoChunks(sentence);
		Assert.assertEquals(2, splitSentences.size());
		Assert.assertEquals("七転八起", splitSentences.get(0));
		Assert.assertEquals("一期一会", splitSentences.get(1));
	}

	@Test
	public void testUrlEncodeSentence() {
		String normalSentence = "This is a normal sentence";
		String encodedSentence = GoogleTTSTextProcessor.urlEncodeSentence(normalSentence);
		Assert.assertEquals("This%20is%20a%20normal%20sentence", encodedSentence);

		String specialCharsSentence = "Ä 文章 with ßpecial chÆracters";
		encodedSentence = GoogleTTSTextProcessor.urlEncodeSentence(specialCharsSentence);
		Assert.assertEquals("%C3%84%20%E6%96%87%E7%AB%A0%20with%20%C3%9Fpecial%20ch%C3%86racters", encodedSentence);
	}
}
