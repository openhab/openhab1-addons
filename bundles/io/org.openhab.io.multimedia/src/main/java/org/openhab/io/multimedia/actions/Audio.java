/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.io.multimedia.actions;

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
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.apache.commons.collections.Closure;
import org.apache.commons.io.IOUtils;
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

	private static Player streamPlayer = null;

	static public void playSound(String filename) {
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
		if (streamPlayer != null) {
			// if we are already playing a stream, stop it first
			streamPlayer.close();
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

	static public void speak(String text) {
		speak(text, null);
	}

	static public void speak(String text, String voice) {
		TTSService ttsService = getTTSService(MultimediaActivator.getContext(), System.getProperty("osgi.os"));
		if(ttsService==null) {
			ttsService = getTTSService(MultimediaActivator.getContext(), "any");
		}
		if(ttsService!=null) {
			ttsService.speak(text, voice);
		} else {
			logger.error("No TTS service available - tried to say: {}", text);
		}
	}

	static public void setMasterVolume(final float volume) {
		if(volume<0 || volume>1) {
			throw new IllegalArgumentException("Volume value must be in the range [0,1]!");
		}
		runVolumeCommand(new Closure() {
			public void execute(Object input) {
				FloatControl volumeControl = (FloatControl) input;
				volumeControl.setValue(volume);
			}
		});
	}

	static public void increaseMasterVolume(final float percent) {
		if(percent<=0 || percent>100) {
			throw new IllegalArgumentException("Percent must be in the range (0,100]!");
		}
		runVolumeCommand(new Closure() {
			public void execute(Object input) {
				FloatControl volumeControl = (FloatControl) input;
				float volume = volumeControl.getValue();
				float newVolume = volume * (1f + percent / 100f);
				if(newVolume > 1) {
					newVolume = 1;
				}
				volumeControl.setValue(newVolume);
			}
		});
	}

	static public void decreaseMasterVolume(final float percent) {
		if(percent<=0 || percent>100) {
			throw new IllegalArgumentException("Percent must be in the range (0,100]!");
		}
		runVolumeCommand(new Closure() {
			public void execute(Object input) {
				FloatControl volumeControl = (FloatControl) input;
				float volume = volumeControl.getValue();
				float newVolume = volume * (1f - percent / 100f);
				volumeControl.setValue(newVolume);
			}
		});
	}

	static public float getMasterVolume() throws IOException {
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
				}
			}
		}.start();
	}


	/**
	 * Queries the OSGi service registry for a service that provides a transformation service of
	 * a given transformation type (e.g. REGEX, XSLT, etc.)
	 * 
	 * @param transformationType the desired transformation type
	 * @return a service instance or null, if none could be found
	 */
	static public TTSService getTTSService(BundleContext context, String os) {
		if(context!=null) {
			String filter = os!=null ? "(os=" + os + ")" : null;
			try {
				ServiceReference[] refs = context.getServiceReferences(TTSService.class.getName(), filter);
				if(refs!=null && refs.length > 0) {
					return (TTSService) context.getService(refs[0]);
				} else {
					return null;
				}
			} catch (InvalidSyntaxException e) {
				// this should never happen
			}
		}
		return null;
	}

}
