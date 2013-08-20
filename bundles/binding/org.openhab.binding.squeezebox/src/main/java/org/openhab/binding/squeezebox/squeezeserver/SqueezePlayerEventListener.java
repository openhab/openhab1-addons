package org.openhab.binding.squeezebox.squeezeserver;

import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayer.PlayerEvent;
import org.openhab.binding.squeezebox.squeezeserver.SqueezePlayer.STATES;

public interface SqueezePlayerEventListener {
	 public void onSqueezePlayerMuteStateChangeEvent(PlayerEvent event, String id, STATES isMuted);
	 public void onSqueezePlayerPlayStateChangeEvent(PlayerEvent event, String id, STATES isPlaying);
	 public void onSqueezePlayerPauseStateChangeEvent(PlayerEvent event, String id, STATES isPaused);
	 public void onSqueezePlayerStopStateChangeEvent(PlayerEvent event, String id, STATES isStopped);
	 public void onSqueezePlayerPowerStateChangeEvent(PlayerEvent event, String id, STATES isPowered);
	 public void onSqueezePlayerVolumeChangeEvent(PlayerEvent event, String id, byte volume);
	 public void onSqueezePlayerTitleChangeEvent(PlayerEvent event, String id, String title);
	 public void onSqueezePlayerAlbumStateChangeEvent(PlayerEvent event, String id, String album);	 
	 public void onSqueezePlayerArtistStateChangeEvent(PlayerEvent event, String id, String artist);	 
	 public void onSqueezePlayerArtStateChangeEvent(PlayerEvent event, String id, String art);		 
	 public void onSqueezePlayerYearStateChangeEvent(PlayerEvent event, String id, String year);
	 public void onSqueezePlayerGenreStateChangeEvent(PlayerEvent event, String id, String genre);
	 public void onSqueezePlayerRemoteTitleStateChangeEvent(PlayerEvent event, String id, String title);
}