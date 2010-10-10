package org.openhab.core.rules.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Audio {

	private static final String SOUND_DIR = "sounds";
	private static final Logger logger = LoggerFactory.getLogger(Audio.class);
	
	private static Player streamPlayer = null;
	
	static public void playSound(String filename) {
		try {
			InputStream is = new FileInputStream(SOUND_DIR + File.separator + filename);
			if(filename.toLowerCase().endsWith(".mp3") ) {
				Player player = new Player(is);
				playInThread(player);
			} else {
				AudioInputStream ais = AudioSystem.getAudioInputStream(is);
				Clip clip = AudioSystem.getClip();
				clip.open(ais);
				playInThread(clip);
			}
		} catch (FileNotFoundException e) {
			logger.error("Cannot play sound '{}'.", filename, e);
		} catch (JavaLayerException e) {
			logger.error("Cannot play sound '{}'.", filename, e);
		} catch (UnsupportedAudioFileException e) {
			logger.error("Format of sound file '{}' is not supported.", filename, e);
		} catch (IOException e) {
			logger.error("Cannot play sound '{}'.", filename, e);
		} catch (LineUnavailableException e) {
			logger.error("Cannot play sound '{}'.", filename, e);
		}
	}

	static public void playStream(String url) {
		if(streamPlayer!=null) {
			// if we are already playing a stream, stop it first
			streamPlayer.close();
		}
		if(url==null) {
			// the call was only for stopping the currently playing stream
			return;
		}
		try {
			InputStream is = new URL(url).openStream();
			Player player = new Player(is);
			streamPlayer = player;
			playInThread(player);
		} catch (JavaLayerException e) {
			logger.error("Cannot play stream '{}'.", url, e);
		} catch (MalformedURLException e) {
			logger.error("Cannot play stream '{}'.", url, e);
		} catch (IOException e) {
			logger.error("Cannot play stream '{}'.", url, e);
		}
	}

	private static void playInThread(final Clip clip) {
		// run in new thread
		new Thread() {
			public void run() {
				try {
					clip.start();
					while(clip.isActive()) {
						sleep(1000L);
					}
					clip.close();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}.start();
	}

	static private void playInThread(final Player player) {
		// run in new thread
		new Thread() {
			public void run() {
				try {
					player.play();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}.start();
	}

}
