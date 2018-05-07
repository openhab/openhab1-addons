/**
 * Copyright (c) 2015-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.tts;

// NOTE: This interface is only kept in order to allow openHAB 1.x TTS services to correctly compile.
// openHAB 2 is NOT compatible with these services, not even through the compatibility layer.
// Instead, ESH/openHAB2 compatible TTS services should be used.

/**
 *
 * @author Kai Kreuzer - Initial contribution
 */
public interface TTSService {

    void say(String text, String voiceName, String outputDevice);
}
