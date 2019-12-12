# Squeezebox Actions

Interact directly with your Squeezebox devices from within rules and scripts.

## Prerequisites

You must install and configure the Squeezebox binding (1.x).
The `id` you specify in your configuration will be used to identify which player to perform the specified action on.
For example, in the configuration `Kitchen_Player.id=de:ad:be:ef:12:34` would be identified as `Kitchen_Player` in your rules.

## Actions

### Standard Actions

*   `squeezeboxPower(String playerId, boolean power)`
*   `squeezeboxMute(String playerId, boolean mute)`
*   `squeezeboxVolume(String playerId, int volume)`
*   `squeezeboxPlay(String playerId)`
*   `squeezeboxPause(String playerId)`
*   `squeezeboxStop(String playerId)`
*   `squeezeboxNext(String playerId)`
*   `squeezeboxPrev(String playerId)`

### Actions to play a URL on your Squeezebox devices

For example, start a radio stream when you wake up in the morning.

*   `squeezeboxPlayUrl(String playerId, String url)`: Plays the URL on the specified player using the current volume
*   `squeezeboxPlayUrl(String playerId, String url, int volume)`: Plays the URL on the specified player at the specified volume

### Actions to speak a message on your Squeezebox devices

*   `squeezeboxSpeak(String playerId, String message)`
*   `squeezeboxSpeak(String playerId, String message, int volume)`
*   `squeezeboxSpeak(String playerId, String message, int volume, boolean resumePlayback)`

Note: You need to have filled in the `ttsurl` details within the Squeezebox section in the configuration.
Given the changes Google have made to their TTS usage allowances, you might have better luck registering for a key at <http://www.voicerss.org/api/>.

You can check you have the url and api working by pasting it into a browser with some text at the end

```
https://api.voicerss.org/?key=YOUR_KEY_GOES_HERE&f=44khz_16bit_stereo&hl=en-gb&src=This is Major Tom to Ground Control
```

Then you can use the action in your rules however you want.

## Examples

### Send an announcement to the specified player using the current volume

```java
squeezeboxSpeak("Kitchen_Player", "This is Major Tom to Ground Control")
```

### Send an announcement to the specified player at the specified volume

```java
squeezeboxSpeak("Kitchen_Player", "I'm stepping through the door", 100)
```

### Send an announcement to the specified player at the specified volume

If `resumePlayback=true` resume to actual playlist after finishing message.

You might have to tweak some settings on your Squeezebox server and player regarding what defeats what when you add a song in the middle of a playlist.

```java
squeezeboxSpeak("Kitchen_Player", "And I'm floating in a most peculiar way", 100, false)
```

### Generating dynamic strings

```java
squeezeboxSpeak("Kitchen_Player"," temperature outside is " + Weather_Temperature.state.format("%d") + " degrees celsius",75,true)
```
