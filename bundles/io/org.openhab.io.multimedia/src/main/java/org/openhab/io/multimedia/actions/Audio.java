/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.apache.commons.collections.Closure;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.multimedia.internal.MultimediaActivator;
import org.openhab.io.multimedia.tts.TTSService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Audio {

	private static final String SOUND_DIR = "sounds";
	private static final Logger logger = LoggerFactory.getLogger(Audio.class);
	
	private static final Pattern plsStreamPattern = Pattern.compile("^File[0-9]=(.+)$");

	private static Float macVolumeValue = null;
	
	private static Player streamPlayer = null;
	
	private static Socket shoutCastSocket = null;

	@ActionDoc(text="plays a sound from the sounds folder")
	static public void playSound(
			@ParamDoc(name="filename", text="the filename with extension") String filename) {
		try {
			InputStream is = new FileInputStream(SOUND_DIR + File.separator + filename);
			if (filename.toLowerCase().endsWith(".mp3")) {
				Player player = new Player(is);
				playInThread(player);
			} else {
				AudioInputStream ais = AudioSystem.getAudioInputStream(is);
				Clip clip = AudioSystem.getClip();
				clip.open(ais);
				playInThread(clip);
			}
		} catch (FileNotFoundException e) {
			logger.error("Cannot play sound '{}': {}", new String[] { filename, e.getMessage() } );
		} catch (JavaLayerException e) {
			logger.error("Cannot play sound '{}': {}", new String[] { filename, e.getMessage() } );
		} catch (UnsupportedAudioFileException e) {
			logger.error("Format of sound file '{}' is not supported: {}", new String[] { filename, e.getMessage() } );
		} catch (IOException e) {
			logger.error("Cannot play sound '{}': {}", new String[] { filename, e.getMessage() });
		} catch (LineUnavailableException e) {
			logger.error("Cannot play sound '{}': {}", new String[] { filename, e.getMessage() });
		}
	}

	@ActionDoc(text="plays an audio stream from an url")
	static public synchronized void playStream(
			@ParamDoc(name="url", text="the url of the audio stream") String url) {
		if (streamPlayer != null) {
			// if we are already playing a stream, stop it first
			streamPlayer.close();
			streamPlayer = null;
		}
		if (url == null) {
			// the call was only for stopping the currently playing stream
			return;
		}
		try {
			if(url.toLowerCase().endsWith(".m3u")) {
				InputStream is = new URL(url).openStream();
				String urls = IOUtils.toString(is);
				for(String line : urls.split("\n")) {
					if(!line.isEmpty() && !line.startsWith("#")) {
						url = line;
						break;
					}
				}
			}
			else if (url.toLowerCase().endsWith(".pls")) {
				InputStream is = new URL(url).openStream();
				String urls = IOUtils.toString(is);
				for(String line : urls.split("\n")) {
					if(!line.isEmpty() && line.startsWith("File")) {
						Matcher matcher = plsStreamPattern.matcher(line);
						if (matcher.find()) {
							url=matcher.group(1);
							break;
						}
					}
				}
			}
			URL streamUrl = new URL(url);
			URLConnection connection = streamUrl.openConnection();
			InputStream is = null;
			if (connection.getContentType().equals("unknown/unknown")) {
				//Java does not parse non-standard headers used by SHOUTCast
				int port = streamUrl.getPort()>0 ? streamUrl.getPort() : 80;
				// Manipulate User-Agent to receive a stream
				shoutCastSocket = new Socket(streamUrl.getHost(), port);
				
				OutputStream os = shoutCastSocket.getOutputStream();
				String user_agent = "WinampMPEG/5.09";
				String req = "GET / HTTP/1.0\r\nuser-agent: " + user_agent + "\r\nIcy-MetaData: 1\r\nConnection: keep-alive\r\n\r\n";
				os.write(req.getBytes());
				is = shoutCastSocket.getInputStream();
			}
			else {
				is = streamUrl.openStream();
			}
			if (is!=null) {
				Player player = new Player(is);
				streamPlayer = player;
				playInThread(player);
			}
		} catch (JavaLayerException e) {
			logger.error("Cannot play stream '{}': JavaLayerException - {}", url, e.getMessage());
		} catch (MalformedURLException e) {
			logger.error("Cannot play stream '{}': MalformedURLException - {}", url, e.getMessage());
		} catch (IOException e) {
			logger.error("Cannot play stream '{}': {}", url, e);
		}
	}

	/**
	 * Says the given text..
	 * 
	 * <p>This method checks for registered TTS services. If there is a service
	 * available for the current OS, this will be chosen. Otherwise, it
	 * will pick a (the first) TTS service that is platform-independent.</p>
	 * 
	 * @param text the text to speak
	 */
	@ActionDoc(text="says a given text through the default TTS service")
	static public void say(@ParamDoc(name="text") Object text) {
		say(text.toString(), null);
	}

	/**
	 * Text-to-speech with a given voice.
	 * 
	 * <p>This method checks for registered TTS services. If there is a service
	 * available for the current OS, this will be chosen. Otherwise, it
	 * will pick a (the first) TTS service that is platform-independent.</p>
	 * 
	 * @param text the text to speak
	 * @param voice the name of the voice to use or null, if the default voice should be used
	 */
	@ActionDoc(text="says a given text through the default TTS service with a given voice")
	static public void say(@ParamDoc(name="text") Object text, @ParamDoc(name="voice") String voice) {
		say(text, voice, null);
	}

	/**
	 * Text-to-speech with a given voice.
	 * 
	 * <p>This method checks for registered TTS services. If there is a service
	 * available for the current OS, this will be chosen. Otherwise, it
	 * will pick a (the first) TTS service that is platform-independent.</p>
	 * 
	 * @param text the text to speak
	 * @param voice the name of the voice to use or null, if the default voice should be used
	 * @param device the name of audio device to be used to play the audio or null, if the default output device should be used
	 */
	@ActionDoc(text="says a given text through the default TTS service with a given voice")
	static public void say(@ParamDoc(name="text") Object text, 
						   @ParamDoc(name="voice") String voice,
						   @ParamDoc(name="device") String device) {
		if(StringUtils.isNotBlank(text.toString())) {
			TTSService ttsService = getTTSService(MultimediaActivator.getContext(), System.getProperty("osgi.os"));
			if(ttsService==null) {
				ttsService = getTTSService(MultimediaActivator.getContext(), "any");
			}
			if(ttsService!=null) {
				ttsService.say(text.toString(), voice, device);
			} else {
				logger.error("No TTS service available - tried to say: {}", text);
			}
		}
	}

	@ActionDoc(text="sets the master volume of the host")
	static public void setMasterVolume(
			@ParamDoc(name="volume", text="volume in the range [0,1]") final float volume) throws IOException {
		if(volume<0 || volume>1) {
			throw new IllegalArgumentException("Volume value must be in the range [0,1]!");
		}
		if(isMacOSX()) {
			macVolumeValue = volume;
			setMasterVolumeMac(volume * 100f);			
		} else {
			setMasterVolumeJavaSound(volume);
		}
	}

	@ActionDoc(text="sets the master volume of the host")
	static public void setMasterVolume(@ParamDoc(name="percent") final PercentType percent) throws IOException {
		setMasterVolume(percent.toBigDecimal().floatValue()/100f);
	}

	private static void setMasterVolumeMac(float volume) throws IOException {
		Runtime.getRuntime().exec(new String[] {"osascript", "-e", "set volume output volume " + volume});
	}

	private static void setMasterVolumeJavaSound(final float volume) {
		runVolumeCommand(new Closure() {
			public void execute(Object input) {
				FloatControl volumeControl = (FloatControl) input;
				volumeControl.setValue(volume);
			}
		});
	}

	@ActionDoc(text="increases the master volume of the host")
	static public void increaseMasterVolume(@ParamDoc(name="percent") final float percent) throws IOException {
		if(percent<=0 || percent>100) {
			throw new IllegalArgumentException("Percent must be in the range (0,100]!");
		}
		Float volume = getMasterVolume();
		if(volume==0) {
			// as increasing 0 by x percent will still be 0, we have to set some initial positive value
			volume = 0.001f;
		}
		float newVolume = volume * (1f + percent / 100f);
		if(isMacOSX() && newVolume-volume<.01) {
			// the getMasterVolume() only returns integers, so we have to make sure that we
			// increase the volume level at least by 1%.
			newVolume += .01;
		}
		if(newVolume > 1) {
			newVolume = 1;
		}
		setMasterVolume(newVolume);
	}

	@ActionDoc(text="decreases the master volume of the host")
	static public void decreaseMasterVolume(@ParamDoc(name="percent")final float percent) throws IOException {
		if(percent<=0 || percent>100) {
			throw new IllegalArgumentException("Percent must be in the range (0,100]!");
		}
		float volume = getMasterVolume();
		float newVolume = volume * (1f - percent / 100f);
		if(isMacOSX() && newVolume>0 && volume-newVolume<.01) {
			// the getMasterVolume() only returns integers, so we have to make sure that we
			// decrease the volume level at least by 1%.
			newVolume -= .01;
		}
		if(newVolume < 0) {
			newVolume = 0;
		}
		setMasterVolume(newVolume);
	}

	@ActionDoc(text="gets the master volume of the host", returns="volume as a float in the range [0,1]")
	static public float getMasterVolume() throws IOException {
		if(isMacOSX()) {
			return getMasterVolumeMac();
		} else {
			return getMasterVolumeJavaSound(); 
		}
	}

	private static synchronized float getMasterVolumeMac() throws IOException {
		// we use a cache of the value as the script execution is pretty slow
		if(macVolumeValue==null) {
			Process p = Runtime.getRuntime().exec(new String[] {"osascript", "-e", "output volume of (get volume settings)"});
		 	String value = IOUtils.toString(p.getInputStream()).trim();
			macVolumeValue = Float.valueOf(value) / 100f;
		}
		return macVolumeValue;
	}

	private static float getMasterVolumeJavaSound() throws IOException {
		final Float[] volumes = new Float[1];
		runVolumeCommand(new Closure() {
			public void execute(Object input) {
				FloatControl volumeControl = (FloatControl) input;
				volumes[0] = volumeControl.getValue();
			}
		});
		if(volumes[0]!=null) {
			return volumes[0];
		} else {
			throw new IOException("Cannot determine master volume level");
		}
	}

	private static void runVolumeCommand(Closure closure) {
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		for (Mixer.Info info : infos) {
			Mixer mixer = AudioSystem.getMixer(info);
			if (mixer.isLineSupported(Port.Info.SPEAKER)) {
				Port port;
				try {
					port = (Port) mixer.getLine(Port.Info.SPEAKER);
					port.open();
					if (port.isControlSupported(FloatControl.Type.VOLUME)) {
						FloatControl volume = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
						closure.execute(volume);
					}
					port.close();
				} catch (LineUnavailableException e) {
					logger.error("Cannot access master volume control", e);
				}
			}
		}
	}

	private static void playInThread(final Clip clip) {
		// run in new thread
		new Thread() {
			public void run() {
				try {
					clip.start();
					while (clip.isActive()) {
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
				} finally {
					if (shoutCastSocket!=null) {
						try {
							shoutCastSocket.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}.start();
	}


	/**
	 * Queries the OSGi service registry for a service that provides a TTS implementation
	 * for a given platform.
	 * 
	 * @param context the bundle context to access the OSGi service registry
	 * @param os a valid osgi.os string value or "any" if service should be platform-independent
	 * @return a service instance or null, if none could be found
	 */
	static private TTSService getTTSService(BundleContext context, String os) {
		if(context!=null) {
			String filter = os!=null ? "(os=" + os + ")" : null;
			try {
				Collection<ServiceReference<TTSService>> refs = context.getServiceReferences(TTSService.class, filter);
				if(refs!=null && refs.size() > 0) {
					return (TTSService) context.getService(refs.iterator().next());
				} else {
					return null;
				}
			} catch (InvalidSyntaxException e) {
				// this should never happen
			}
		}
		return null;
	}
	
	private static boolean isMacOSX() {
		return System.getProperty("osgi.os").equals("macosx");
	}

}
